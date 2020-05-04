package sujithvakkomfreelance.com.enterprisemanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.bendik.simplerangeview.SimpleRangeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.helpers.PreferenceHelpers;
import sujithvakkomfreelance.com.components.localdata.dataproviders.DeliveryHeaderProvider;
import sujithvakkomfreelance.com.components.localdata.dataproviders.delivery;
import sujithvakkomfreelance.com.components.localdata.dataproviders.sync;
import sujithvakkomfreelance.com.components.models.LoginToken;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryLine;
import sujithvakkomfreelance.com.components.web.IWebClient;


public class AcceptJobActivity extends AppCompatActivity {
    private static final String TAG = "AcceptJobActivity";
    private SimpleRangeView deliveryPreferedTimeOfTheDay;
    private DeliveryHeader delivery;
    private View parentView;
    private AppCompatTextView tVDeliveryDesc;
    private AppCompatTextView tVDeliveryCustomerName;
    private AppCompatTextView tVDeliveryCustomerAddress;
    private AppCompatTextView tVDeliveryCustomerPhone;
    private AppCompatTextView tVDeliveryCustomerEmirate;
    private AppCompatTextView tVDeliveryReceipt;
    private AppCompatTextView tVDeliveryReceiptDate;
    private AppCompatTextView tVDeliveryDateWidgetMonth;
    private AppCompatTextView tVDeliveryDateWidgetDate;
    private AppCompatButton bDeliveryLoad;
    private AppCompatButton bDeliveryRejectLoad;
    private ListView listViewItems;

    private final static String[] labelsTimeSlots=new String[]{"08:00","11:00","15:00","19:00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_job);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        parentView = findViewById(R.id.activity_accept_job_patent);

        //Description View
        tVDeliveryDesc = findViewById(R.id.tVDeliveryDesc);

        tVDeliveryCustomerName = findViewById(R.id.tVDeliveryCustomerName);

        tVDeliveryCustomerAddress = findViewById(R.id.tVDeliveryCustomerAddress);

        tVDeliveryCustomerPhone = findViewById(R.id.tVDeliveryCustomerPhone);

        tVDeliveryCustomerEmirate = findViewById(R.id.tVDeliveryCustomerEmirate);

        tVDeliveryReceipt = findViewById(R.id.tVDeliveryReceipt);

        tVDeliveryReceiptDate = findViewById(R.id.tVDeliveryReceiptDate);

        tVDeliveryDateWidgetMonth = findViewById(R.id.tVDeliveryDateWidgetMonth);

        tVDeliveryDateWidgetDate = findViewById(R.id.tVDeliveryDateWidgetDate);

        bDeliveryLoad = findViewById(R.id.bDeliveryLoad);
        bDeliveryLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDelivery(true);
            }
        });

        bDeliveryRejectLoad = findViewById(R.id.bDeliveryRejectLoad);
        bDeliveryRejectLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDelivery(false);
            }
        });

        listViewItems = findViewById(R.id.listViewItems);

        setUpDeliveryDetails();

        try {
            tVDeliveryDesc.setText(delivery.getCustomerLabel());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //
        deliveryPreferedTimeOfTheDay = findViewById(R.id.deliveryPreferedTimeOfTheDay);
        deliveryPreferedTimeOfTheDay.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return labelsTimeSlots[i];
            }
        });
        deliveryPreferedTimeOfTheDay.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                setUpTimeSlot();
            }
        });
        setUpTimeSlot();
        //deliveryMapView.setStyleUrl("https://tile.jawg.io/jawg-streets.json?access-token=" + AppLiterals.JAWG_API_KEY);
    }

    private void loadDelivery(boolean accept) {
        try {
            if (accept) {
                delivery.setStatus(AppLiterals.DELIVERY_STATUS.DISPATCHED);
                LoginToken token = Util.getToken(getApplicationContext());

                for(DeliveryLine line : delivery.DeliveryLines) {
                    if (line.Remarks == null) line.Remarks = "";
                    line.Remarks =
                            ( " Loaded By : "+token.user_name).trim()
                    ;
                }

                delivery.setDriver(token.user_name);
                DeliveryHeaderProvider provider = new DeliveryHeaderProvider();
                long result = provider.save(getApplicationContext(), delivery);
                if (result > 0) {
                    try {
                        String baseUrl =
                                Util.getSettingById(AcceptJobActivity.this,R.string.pref_head_office_key);
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
                                Util.addToSynch(delivery,"Success",true);

                                AlertDialog dialog = new AlertDialog.Builder(AcceptJobActivity.this)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        AcceptJobActivity.this.finish();
                                                    }
                                                }
                                        )
                                        .setMessage("Order Loaded: " + response.body().OrderNumber)
                                        .setTitle("Success.")
                                        .setCancelable(false).create();

                                dialog.show();
                            }

                            @Override
                            public void onFailure(Call<DeliveryHeader> call, Throwable t) {
                                Util.addToSynch(delivery,t.getMessage());
                                //DeliveryHeaderProvider provider = new DeliveryHeaderProvider();
                                //delivery.setStatus(AppLiterals.DELIVERY_STATUS.READY);
                                //long result = provider.save(getApplicationContext(), delivery);
                                Snackbar snackbar = Util.SimpleSnackBar(parentView, "Error in connection.", Snackbar.LENGTH_LONG);
                                View view = snackbar.getView();
                                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                        getResources().getColor(R.color.error_foreground)
                                );
                                snackbar.show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(parentView, "Incomplete data: " + delivery.getReceipt(), Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                }
            } else {
                Snackbar.make(parentView, "Rejected delivery" + delivery.getReceipt(), Snackbar.LENGTH_LONG)
                        .setAction("Item Accepted Job", null).show();
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
            Snackbar.make(parentView, "Some error occurred: " + delivery.getReceipt(), Snackbar.LENGTH_LONG)
                    .setAction("Item Accepted Job", null).show();
        }
    }

    private void setUpDeliveryDetails() {
        if(delivery!=null) {
            try {
                tVDeliveryDesc.setText(delivery.getCustomerLabel());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
            setUpLineDetails();
        }
    }

    private void setUpLineDetails() {
        listViewItems.setAdapter(new ArrayAdapter<DeliveryLine>(
                getApplicationContext(),
                R.layout.simple_list_item,
                this.delivery.DeliveryLines
        ));
    }


    private void setUpTimeSlot() {
        try {
            switch (delivery.getTimeSlot()) {
                case "Morning":
                    deliveryPreferedTimeOfTheDay.setStart(0);
                    deliveryPreferedTimeOfTheDay.setEnd(1);
                    break;
                case "After Noon":
                    deliveryPreferedTimeOfTheDay.setStart(1);
                    deliveryPreferedTimeOfTheDay.setEnd(2);
                    break;
                case "Evening":
                    deliveryPreferedTimeOfTheDay.setStart(2);
                    deliveryPreferedTimeOfTheDay.setEnd(3);
                    break;
                case "Any Time":
                    deliveryPreferedTimeOfTheDay.setStart(1);
                    deliveryPreferedTimeOfTheDay.setEnd(3);
                    break;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS,delivery);
    }
}