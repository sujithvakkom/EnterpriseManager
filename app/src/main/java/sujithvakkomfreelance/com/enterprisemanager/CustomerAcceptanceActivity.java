package sujithvakkomfreelance.com.enterprisemanager;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Iterator;

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
import sujithvakkomfreelance.com.enterprisemanager.customviews.DrawableView;

public class CustomerAcceptanceActivity extends AppCompatActivity {

    private static final String TAG = "CustomerAcceptActivity";

    private AppCompatTextView tVCustmerNameFor;
    private AppCompatTextView tVDeliveryDesc;
    private AppCompatRatingBar rBCustomerRating;
    private DrawableView dCustomerDraw;
    private AppCompatButton bDeliveryCancel;
    private AppCompatButton bDeliveryAccepted;
    private AppCompatButton bErase;
    private DeliveryHeader delivery;
    private boolean signIsClean;
    private View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_acceptance);
        parentView = findViewById(R.id.activity_customer_parent);
        tVCustmerNameFor = findViewById(R.id.tVCustmerNameFor);
        tVDeliveryDesc = findViewById(R.id.tVDeliveryDesc);
        dCustomerDraw = findViewById(R.id.dCustomerDraw);
        rBCustomerRating = findViewById(R.id.rBCustomerRating);
        bDeliveryCancel = findViewById(R.id.bDeliveryCancel);
        bDeliveryAccepted = findViewById(R.id.bDeliveryAccepted);
        bErase = findViewById(R.id.bErase);

        bDeliveryCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        bDeliveryAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!signIsClean && rBCustomerRating.getRating() > 0)
                    delivered();
                else {
                    String message="";
                    if (signIsClean) {
                        message= "Please Sign.";
                    } else if (rBCustomerRating.getRating() == 0) {
                        message =message+ (message.isEmpty()?"": System.lineSeparator())+ "Please rate experience.";
                    }
                    Snackbar snackbar = Util.SimpleSnackBar(parentView, message, Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.error_background));
                    ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                            getResources().getColor(R.color.error_foreground)
                    );
                    snackbar.show();
                }
            }
        });

        bErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dCustomerDraw.clear();
            }
        });

        dCustomerDraw.setOnStarDrawing(new DrawableView.onStarDrawing() {
            @Override
            public void startedDrawingListener(boolean isClean) {
                signIsClean = isClean;
            }
        });

        delivery = (DeliveryHeader) getIntent().getSerializableExtra(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS);
        if (savedInstanceState == null) {
            try{
                this.delivery = (DeliveryHeader)savedInstanceState.getSerializable(
                        AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS
                );
            }catch (Exception ex) {
                ex.printStackTrace();
                delivery = null;
            }
            if(delivery==null) {
                Bundle extras = getIntent().getExtras();
                try {
                    if (delivery == null)
                        delivery = (DeliveryHeader) extras.getSerializable(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }

        setUpDeliveryDetails();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS, delivery);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.delivery = (DeliveryHeader)savedInstanceState.getSerializable(
                AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS
        );
    }

    private void setUpDeliveryDetails() {

        if (delivery != null) {
            try {
                tVDeliveryDesc.setText(delivery.getDescription());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                tVCustmerNameFor.setText(delivery.getCustomerName() + "\nPlease Sign here.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void delivered() {
        dCustomerDraw.setDrawingCacheEnabled(true);
        String tempDir = Environment.getExternalStorageDirectory() +
                "/" + getResources().getString(R.string.external_dir) + "/";
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);

        prepareDirectory(tempDir);
        String uniqueId = Util.getTodayDate() + "_" + Util.getCurrentTimeString() + "_" + Math.random();
        String current = uniqueId + ".png";

        dCustomerDraw.save(getApplicationContext(), new File(directory, current));

        //delivery.setStatus(AppLiterals.DELIVERED);

        for(DeliveryLine line : delivery.DeliveryLines) {
            if (line.Remarks == null) line.Remarks = "";
            line.Remarks =
                    (" Customer Rating : " +
                            Integer.toString(((Float) rBCustomerRating.getRating()).intValue()) +
                            "/" +
                            Integer.toString(rBCustomerRating.getNumStars())).trim();
        }

        DeliveryHeaderProvider provider = new DeliveryHeaderProvider();
        long result = provider.save(getApplicationContext(), delivery);

        if (result > 0) {
            try {
                String baseUrl = Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(
                                AppLiterals.APPLICATION_GSON_BUILDER));

                Retrofit retrofit = builder.build();

                IWebClient client = retrofit.create(IWebClient.class);

                Iterator itr = delivery.DeliveryLines.iterator();
                while (itr.hasNext())
                {
                    DeliveryLine x = (DeliveryLine)itr.next();
                    if (!x.IsChecked)
                        itr.remove();
                }

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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        intent.putExtra(AppLiterals.YES_OR_NO, AppLiterals.YES);
        intent.putExtra(AppLiterals.MESSAGE, "Delivered : " + delivery.getDescription());
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean prepareDirectory(String dir) {
        try {
            if (makedirs(dir)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not initiate File System.. Is Sdcard mounted properly?", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean makedirs(String tempDir) {
        File tempdir = new File(tempDir);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory()) {
            File[] files = tempdir.listFiles();
            for (File file : files) {
                if (!file.delete()) {
                    System.out.println("Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }
}
