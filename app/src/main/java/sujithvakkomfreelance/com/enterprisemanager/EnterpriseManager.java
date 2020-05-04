package sujithvakkomfreelance.com.enterprisemanager;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import sujithvakkomfreelance.com.components.AppLiterals;

public class EnterpriseManager extends Application {
    private static final String TAG = "EnterpriseManager";
    @Override
    public void onCreate() {
        super.onCreate();
        /*
        *
        mapbox:mapbox_styleUrl="https://tile.jawg.io/jawg-streets.json?access-token=jwpq5TaBN0WkRlPJAY7jOU4gtjzrVPdT3hFMdVJVz8Zs6o7Wf5Iex3wo06tBYXwy"
        mapbox:mapbox_uiLogo="false"
        mapbox:mapbox_uiAttribution="false"
        * */
        try {
            Mapbox.getInstance(getApplicationContext(), "jwpq5TaBN0WkRlPJAY7jOU4gtjzrVPdT3hFMdVJVz8Zs6o7Wf5Iex3wo06tBYXwy");
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,ex.getMessage());
        }
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    AppLiterals.CHANNEL_ID,
                    AppLiterals.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
