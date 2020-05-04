package sujithvakkomfreelance.com.enterprisemanager;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.localdata.dataproviders.UserDetail;
import sujithvakkomfreelance.com.components.web.IWebClient;
import sujithvakkomfreelance.com.enterprisemanager.customviews.HomeLoaderHelper;

import static sujithvakkomfreelance.com.components.AppLiterals.REQUEST_FORGROUND_SERVICE;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;
        UserDetail token = Util.getToken(getApplicationContext());
        if (token != null && (token.session == null ? "" : token.session).isEmpty()) {
            UserDetail.Logoff(getApplicationContext());
            token = null;
        } else
            updateToken(token, getApplicationContext());
        // go straight to main if a token is stored
        if (token != null) {
            activityIntent = HomeLoaderHelper.loadHome(getApplicationContext());
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.FOREGROUND_SERVICE},
                                REQUEST_FORGROUND_SERVICE);
                    }
                }
            } else {
                startServices();
            }
        } else {
            activityIntent = new Intent(this, LoginActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }

    private void updateToken(UserDetail token, Context context) {
        try {
            String baseUrl =
                    Util.getSettingById(context, R.string.pref_head_office_key);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            IWebClient client = retrofit.create(IWebClient.class);

            Call<UserDetail> call = client.GetUserProfileFor(token.user_name, token.session);
            call.enqueue(new Callback<UserDetail>() {
                @Override
                public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                    UserDetail tokenNew = response.body();
                    if(tokenNew!=null)
                    tokenNew.save(getApplicationContext());
                }

                @Override
                public void onFailure(Call<UserDetail> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startServices() {
        if (!isServiceRunning(GSDeliveryService.class.getName())) {
            Intent intentGSMerchantService = new Intent(this, GSDeliveryService.class);
            ContextCompat.startForegroundService(this, intentGSMerchantService);
        }
    }

    private boolean isServiceRunning(String serviceName) {
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i
                    .next();

            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                serviceRunning = true;

                if (runningServiceInfo.foreground) {
                    //service run in foreground
                }
            }
        }
        return serviceRunning;
    }
}
