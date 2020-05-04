package sujithvakkomfreelance.com.enterprisemanager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Iterator;

import me.bendik.simplerangeview.SimpleRangeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.localdata.dataproviders.DeliveryHeaderProvider;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryLine;
import sujithvakkomfreelance.com.components.web.IWebClient;
import sujithvakkomfreelance.com.enterprisemanager.adaptors.DeliveryLineAdaptor;
import sujithvakkomfreelance.com.enterprisemanager.customviews.ItemMultiSelectDialog;

public class DeliveryActionsActivity extends AppCompatActivity implements    DatePickerDialog.OnDateSetListener {
    private static final String TAG = "DeliveryActionsActivity";
    private DeliveryHeader delivery;
    private View parentView;
    private FloatingActionButton bCallCustomer;
    private FloatingActionButton bCallCustomerCare;
    private FloatingActionButton bCallCustomerLocation;
    private FloatingActionMenu customerContactMenu;
    private FloatingActionMenu deliveryActionMenu;
    private FloatingActionMenu.OnMenuToggleListener onMenuToggleListenerCustomerContactMenu;

    private FloatingActionMenu.OnMenuToggleListener onMenuToggleListenerDeliveryActionMenu;
    private SimpleRangeView deliveryPreferredTimeOfTheDay;
    private AppCompatTextView tVDeliveryCustomerName;
    private AppCompatTextView tVDeliveryCustomerAddress;
    private AppCompatTextView tVDeliveryCustomerPhone;
    private AppCompatTextView tVDeliveryCustomerEmirate;
    private AppCompatTextView tVDeliveryReceipt;
    private AppCompatTextView tVDeliveryReceiptDate;
    private AppCompatTextView tVDeliveryDateWidgetMonth;
    private AppCompatTextView tVDeliveryDateWidgetDate;
    private AppCompatTextView tVDeliveryCustomerRemark;
    private FloatingActionButton bDelivered;
    private FloatingActionButton bDeliveryRejected;
    private FloatingActionButton bDeliveryReschedule;
    private ListView listViewItems;

    private String deliveryRejectReasonsSelected = "";
    private boolean[] checkedItems;


    private final static String[] labelsTimeSlots = new String[]{"08:00", "11:00", "15:00", "19:00"};
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_actions);
        toolbar = findViewById(R.id.deliverActionsToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getStatusBarColor();
        }
        toolbar.setTitle(R.string.title_activity_delivery_actions);

        onMenuToggleListenerCustomerContactMenu =
                new FloatingActionMenu.OnMenuToggleListener() {
                    @Override
                    public void onMenuToggle(boolean opened) {
                        deliveryActionMenu.toggle(!opened);
                    }
                };
        onMenuToggleListenerDeliveryActionMenu =
                new FloatingActionMenu.OnMenuToggleListener() {
                    @Override
                    public void onMenuToggle(boolean opened) {
                        customerContactMenu.toggle(!opened);
                    }
                };

        parentView = findViewById(R.id.deliverActionsParent);

        customerContactMenu = findViewById(R.id.customerContactMenu);

        deliveryActionMenu = findViewById(R.id.deliveryActionMenu);

        customerContactMenu.setClosedOnTouchOutside(true);

        deliveryActionMenu.setClosedOnTouchOutside(true);

        bCallCustomer = findViewById(R.id.bCallCustomer);
        bCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCustomer();
            }
        });

        bCallCustomerCare = findViewById(R.id.bCallCustomerCare);
        bCallCustomerCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCustomerCare();
            }
        });

        bCallCustomerLocation = findViewById(R.id.bCallCustomerLocation);
        bCallCustomerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCustomerLocationMap();
            }
        });

        bDelivered = findViewById(R.id.bDelivered);
        bDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delivery != null)
                    loadCustomerAcceptance(delivery);
            }
        });

        bDeliveryRejected = findViewById(R.id.bDeliveryRejected);
        bDeliveryRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delivery != null)
                    loadCustomerRejection(delivery);
            }
        });

        bDeliveryReschedule = findViewById(R.id.bDeliveryReschedule);
        bDeliveryReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delivery != null)
                    reschedule(delivery);
            }
        });

        tVDeliveryCustomerName = findViewById(R.id.tVDeliveryCustomerName);

        tVDeliveryCustomerAddress = findViewById(R.id.tVDeliveryCustomerAddress);

        tVDeliveryCustomerPhone = findViewById(R.id.tVDeliveryCustomerPhone);

        tVDeliveryCustomerEmirate = findViewById(R.id.tVDeliveryCustomerEmirate);

        tVDeliveryReceipt = findViewById(R.id.tVDeliveryReceipt);

        tVDeliveryReceiptDate = findViewById(R.id.tVDeliveryReceiptDate);

        tVDeliveryDateWidgetMonth = findViewById(R.id.tVDeliveryDateWidgetMonth);

        tVDeliveryDateWidgetDate = findViewById(R.id.tVDeliveryDateWidgetDate);

        tVDeliveryCustomerRemark = findViewById(R.id.tVDeliveryCustomerRemark);

        listViewItems = findViewById(R.id.listViewItems);
        deliveryPreferredTimeOfTheDay = findViewById(R.id.deliveryPreferedTimeOfTheDay);
        deliveryPreferredTimeOfTheDay.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return labelsTimeSlots[i];
            }
        });
        deliveryPreferredTimeOfTheDay.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                setUpTimeSlot();
            }
        });

        delivery = (DeliveryHeader) getIntent().getSerializableExtra(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            try {
                if (delivery == null)
                    delivery = (DeliveryHeader) extras.getSerializable(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
                ex.printStackTrace();
            }
        }
        setUpTimeSlot();
        setUpDeliveryDetails();
        setUpLineDetails();
        setTitle();
    }

    public void setTitle() {
        if (delivery != null) {
            try {
                toolbar.setTitle(delivery.getDescription());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setUpLineDetails() {
        DeliveryLineAdaptor adaptor = new DeliveryLineAdaptor(
                this,
                R.layout.simple_list_item,
                delivery.DeliveryLines
        );
        listViewItems.setAdapter(
                adaptor
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS, delivery);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        delivery = (DeliveryHeader) getIntent().getSerializableExtra(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS);
        setUpTimeSlot();
        setUpDeliveryDetails();
        setUpLineDetails();
        setTitle();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    private void loadCustomerLocationMap() {
        Uri gmmIntentUri = Uri.parse("geo:" + delivery.getGPS_CORDINATES() + "?q=" + delivery.getGPS_CORDINATES() + "(" + "Here" + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void callCustomer() {
        try {
            delivery.UserName = Util.getToken(getApplicationContext()).user_name;
            try {
                String baseUrl = Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(
                                AppLiterals.APPLICATION_GSON_BUILDER));

                Retrofit retrofit = builder.build();

                IWebClient client = retrofit.create(IWebClient.class);

                Call<String> call = client.UpdateLog(
                        AppLiterals.DELIVERY_STATUS.DISPATCHED,
                        delivery.getDriver(), "Calling: " + delivery.getCustomerPhone(),
                        delivery.HeaderId,
                        -1);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.i(TAG, "Call log updated: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "Call log updat error: " + t.getMessage());
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Call log updat error: " + e.getMessage());
            }

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + delivery.getCustomerPhone().replaceAll("-", "")));
            Snackbar.make(parentView, "Calling: " + delivery.getCustomerPhone(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            startActivity(callIntent);
        } catch (Exception ex) {
            Snackbar.make(parentView, "Error calling: " + ex.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void callCustomerCare() {
        try {
            delivery.UserName = Util.getToken(getApplicationContext()).user_name;
            try {
                String baseUrl = Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(
                                AppLiterals.APPLICATION_GSON_BUILDER));

                Retrofit retrofit = builder.build();

                IWebClient client = retrofit.create(IWebClient.class);

                Call<String> call = client.UpdateLog(
                        AppLiterals.DELIVERY_STATUS.DISPATCHED,
                        delivery.getDriver(), "Calling: " + "Office",
                        delivery.HeaderId,
                        -1);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.i(TAG, "Call log updated: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "Call log updat error: " + t.getMessage());
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Call log updat error: " + e.getMessage());
            }

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + Util.getCustomerCareNumber()));
            Snackbar.make(parentView, "Calling: " + Util.getCustomerCareNumber(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            startActivity(callIntent);
        } catch (Exception ex) {
            Snackbar.make(parentView, "Error calling: " + ex.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void setUpTimeSlot() {
        try {
            switch (delivery.getTimeSlot()) {
                case "Morning":
                    deliveryPreferredTimeOfTheDay.setStart(0);
                    deliveryPreferredTimeOfTheDay.setEnd(1);
                    break;
                case "After Noon":
                    deliveryPreferredTimeOfTheDay.setStart(1);
                    deliveryPreferredTimeOfTheDay.setEnd(2);
                    break;
                case "Evening":
                    deliveryPreferredTimeOfTheDay.setStart(2);
                    deliveryPreferredTimeOfTheDay.setEnd(3);
                    break;
                case "Any Time":
                    deliveryPreferredTimeOfTheDay.setStart(1);
                    deliveryPreferredTimeOfTheDay.setEnd(3);
                    break;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
        }
    }

    private void setUpDeliveryDetails() {
        if (delivery != null) {
            try {
                tVDeliveryReceipt.setText(delivery.getReceipt());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                tVDeliveryReceiptDate.setText(
                        AppLiterals.APPLICATION_DATE_SMALL_FORMAT.format(delivery.getReceiptDate()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                tVDeliveryCustomerEmirate.setText(delivery.getEmirate());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                tVDeliveryCustomerName.setText(delivery.getCustomerName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                tVDeliveryCustomerAddress.setText(delivery.getCustomerAddress());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                tVDeliveryCustomerPhone.setText(delivery.getCustomerPhone());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                tVDeliveryDateWidgetMonth.setText(
                        AppLiterals.APPLICATION_DATE_MONTH_YEAR_FORMAT.format(delivery.getReceiptDate()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                tVDeliveryDateWidgetDate.setText(
                        AppLiterals.APPLICATION_DATE_DAY_FORMAT.format(delivery.getReceiptDate()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                tVDeliveryCustomerRemark.setText(delivery.getRemark());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void loadCustomerAcceptance(DeliveryHeader delivery) {
        ItemMultiSelectDialog dialog = new ItemMultiSelectDialog();
        dialog.setDelivery(this.delivery);
        dialog.setOnItemSelectAcceptListner(new ItemMultiSelectDialog.OnItemSelectAcceptListner() {
            @Override
            public void onItemSelectAccpted(DeliveryHeader delivery) {
                DeliveryActionsActivity.this.delivery.DeliveryLines = delivery.DeliveryLines;
                int selectedCount = 0;
                for (DeliveryLine line : DeliveryActionsActivity.this.delivery.DeliveryLines) {
                    if (line.IsChecked) {
                        line.Status = AppLiterals.DELIVERY_STATUS.DELIVERED;
                        selectedCount++;
                    }
                }
                if (selectedCount > 0) {
                    Intent intent = new Intent(getApplicationContext(), CustomerAcceptanceActivity.class);
                    intent.putExtra(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS, delivery);
                    DeliveryActionsActivity.this.startActivityForResult(intent, AppLiterals.CUSTOMER_SURVEY);
                }
            }
        });
        dialog.show(getSupportFragmentManager(),AppLiterals.DIALOG_TAG.SELECT_ITEM);
    }

    private void loadCustomerRejection(DeliveryHeader delivery) {
        ItemMultiSelectDialog dialog = new ItemMultiSelectDialog();
        dialog.setDelivery(this.delivery);
        dialog.setOnItemSelectAcceptListner(new ItemMultiSelectDialog.OnItemSelectAcceptListner() {
            @Override
            public void onItemSelectAccpted(final DeliveryHeader delivery) {
                DeliveryActionsActivity.this.delivery.DeliveryLines = delivery.DeliveryLines;
                int selectedCount =0;
                for (DeliveryLine line: DeliveryActionsActivity.this.delivery.DeliveryLines) {
                    if (line.IsChecked) {
                        line.Status = AppLiterals.DELIVERY_STATUS.REJECTED;
                        selectedCount++;
                    }
                }
                if(selectedCount>0) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(DeliveryActionsActivity.this);
                    final String[] deliveryRejectReasons = getResources().getStringArray(R.array.delivery_reject_reasons);
                    mBuilder.setTitle(R.string.rejection_reason_title);
                    mBuilder.setSingleChoiceItems(
                            deliveryRejectReasons,
                            -1,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deliveryRejectReasonsSelected = deliveryRejectReasons[which];
                                }
                            }
                    );
                    mBuilder.setCancelable(true);
                    mBuilder.setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!deliveryRejectReasonsSelected.isEmpty()) {
                                updateDeliveryReject(deliveryRejectReasonsSelected);
                                Intent intent = new Intent();
                                intent.putExtra(AppLiterals.YES_OR_NO, AppLiterals.YES);
                                intent.putExtra(AppLiterals.MESSAGE, "Rejected " + deliveryRejectReasonsSelected);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS,delivery);
                                intent.putExtras(bundle);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Snackbar snackbar =
                                        Snackbar.make(parentView, "No reason selected.", Snackbar.LENGTH_LONG);
                                View view = snackbar.getView();
                                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).
                                        setTextColor(getResources().getColor(R.color.color_danger));
                                snackbar.show();
                            }
                        }
                    });
                    mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }
                    );
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            }
        });
        dialog.show(getSupportFragmentManager(),AppLiterals.DIALOG_TAG.SELECT_ITEM);
    }

    private void updateDeliveryReject(final String rejectionReason) {
        //delivery.setStatus(AppLiterals.REJECTED);
        delivery.setRemarks(
                (delivery.getRemark() == null ? "" : delivery.getRemark()) +
                        " " +
                        rejectionReason);

        for (DeliveryLine line : delivery.DeliveryLines) {
            if (line.getStatus() == AppLiterals.DELIVERY_STATUS.REJECTED)
                line.setRemarks(
                        (line.getRemark() == null ? "" : line.getRemark()) +
                                " " +
                                rejectionReason);
        }

        DeliveryHeaderProvider provider = new DeliveryHeaderProvider();
        long result = provider.save(getApplicationContext(), delivery);

        Iterator itr = delivery.DeliveryLines.iterator();
        while (itr.hasNext())
        {
            DeliveryLine x = (DeliveryLine)itr.next();
            if (!x.IsChecked)
                itr.remove();
        }

        if (result > 0) {
            try {
                String baseUrl = Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(
                                AppLiterals.APPLICATION_GSON_BUILDER));

                Retrofit retrofit = builder.build();

                IWebClient client = retrofit.create(IWebClient.class);

                Call<DeliveryHeader> call = client.SetDeliveryJobs(delivery);
                call.enqueue(new Callback<DeliveryHeader>() {
                    @Override
                    public void onResponse(Call<DeliveryHeader> call, Response<DeliveryHeader> response) {
                        Util.addToSynch(delivery, "Success",true);
                        Snackbar snackbar = Util.SimpleSnackBar(parentView, "Updated successfully.", Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.success_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.success_foreground)
                        );
                        snackbar.show();
                    }

                    @Override
                    public void onFailure(Call<DeliveryHeader> call, Throwable t) {
                        Util.addToSynch(delivery, t.getMessage());
                        Snackbar snackbar = Util.SimpleSnackBar(parentView, "Error updating.", Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.error_foreground)
                        );
                        snackbar.show();
                    }
                });
                /*
                Call<String> callLog = client.UpdateLog(
                        AppLiterals.DELIVERY_STATUS.REJECTED,
                        delivery.getDriver(),
                        rejectionReason,
                        delivery.HeaderId,
                        null);

                callLog.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Util.addToSynch(delivery, rejectionReason);
                    }
                });
                */
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reschedule(DeliveryHeader delivery) {
        ItemMultiSelectDialog dialog = new ItemMultiSelectDialog();
        dialog.setDelivery(this.delivery);
        dialog.setOnItemSelectAcceptListner(new ItemMultiSelectDialog.OnItemSelectAcceptListner() {
            @Override
            public void onItemSelectAccpted(DeliveryHeader delivery) {
                DeliveryActionsActivity.this.delivery.DeliveryLines = delivery.DeliveryLines;
                int count = 0;
                for (DeliveryLine line : DeliveryActionsActivity.this.delivery.DeliveryLines) {
                    if (line.IsChecked) {
                        line.Status = AppLiterals.DELIVERY_STATUS.RESCHEDULED;
                        count++;
                    }
                }
                if (count > 0) {
                    selectDate(DeliveryActionsActivity.this.delivery.getScheduledDate());
                }
            }
        });
        dialog.show(getSupportFragmentManager(), AppLiterals.DIALOG_TAG.SELECT_ITEM);
    }

    private void selectDate(Date selectedDate) {
        DatePickerDialog dialog = null;
        if(selectedDate==null){
            selectedDate = Util.getCurrentDate();
        }
        try {
            dialog = new DatePickerDialog(DeliveryActionsActivity.this,
                    DeliveryActionsActivity.this,
                    selectedDate.getYear(),
                    selectedDate.getMonth(),
                    selectedDate.getDate());
            dialog.getDatePicker().setMinDate(
                    selectedDate.getTime());
            dialog.updateDate(
                    selectedDate.getYear() + 1900,
                    selectedDate.getMonth(),
                    selectedDate.getDate());
            dialog.show();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
        }
    }

    private void updateDeliveryReschedule(final String remark, Date selectedDate) {
        final Integer tempStatus = delivery.Status;
        final String tempRemark = delivery.getRemark();
        final boolean[] failed = {false};
        //delivery.setStatus(AppLiterals.REJECTED);
        for (DeliveryLine line : delivery.DeliveryLines) {
            if (line.IsChecked) {
                line.ScheduledDate = selectedDate;
                String remarkTemp = (line.getRemark() == null ? "" : line.getRemark()) +
                        " Rescheduled: " +
                        AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(selectedDate);
                line.setRemarks(remarkTemp);
            }
        }

        DeliveryHeaderProvider provider = new DeliveryHeaderProvider();
        long result = provider.save(getApplicationContext(), delivery);

        Iterator itr = delivery.DeliveryLines.iterator();
        while (itr.hasNext())
        {
            DeliveryLine x = (DeliveryLine)itr.next();
            if (!x.IsChecked)
                itr.remove();
        }

        if (result > 0) {
            try {
                String baseUrl = Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(
                                AppLiterals.APPLICATION_GSON_BUILDER));

                Retrofit retrofit = builder.build();

                IWebClient client = retrofit.create(IWebClient.class);

                Call<DeliveryHeader> call = client.SetDeliveryJobs(delivery);
                call.enqueue(new Callback<DeliveryHeader>() {
                    @Override
                    public void onResponse(Call<DeliveryHeader> call, Response<DeliveryHeader> response) {
                        Util.addToSynch(delivery, "Success", true);
                        Snackbar snackbar = Util.SimpleSnackBar(parentView, "Updated successfully.", Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.success_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.success_foreground)
                        );
                        snackbar.show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<DeliveryHeader> call, Throwable t) {
                        Util.addToSynch(delivery, t.getMessage());
                        Snackbar snackbar = Util.SimpleSnackBar(parentView, "Error updating.", Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.error_foreground)
                        );
                        snackbar.show();

                    }
                });
                /*
                Call<String> callLog = client.UpdateLog(
                        AppLiterals.DELIVERY_STATUS.RESCHEDULED,
                        delivery.getDriver(),
                        remark,
                        delivery.HeaderId,
                        null);

                callLog.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
                */
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date selectedDate = new Date(year-1900,month,dayOfMonth);
        updateDeliveryReschedule("Reschedule Date: " +
                AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(selectedDate),selectedDate);
        Intent intent = new Intent();
        intent.putExtra(AppLiterals.YES_OR_NO, AppLiterals.YES);
        intent.putExtra(AppLiterals.MESSAGE,"Reschedule Date: " + selectedDate.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}