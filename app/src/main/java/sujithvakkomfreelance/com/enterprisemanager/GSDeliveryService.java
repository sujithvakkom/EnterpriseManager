package sujithvakkomfreelance.com.enterprisemanager;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.helpers.LocationHelper;
import sujithvakkomfreelance.com.components.helpers.PreferenceHelpers;
import sujithvakkomfreelance.com.components.localdata.dataproviders.DeliveryHeaderProvider;
import sujithvakkomfreelance.com.components.localdata.dataproviders.delivery;
import sujithvakkomfreelance.com.components.localdata.dataproviders.location_tracker;
import sujithvakkomfreelance.com.components.localdata.dataproviders.sync;
import sujithvakkomfreelance.com.components.models.LocationTracker;
import sujithvakkomfreelance.com.components.models.LoginToken;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.web.IWebClient;

import static sujithvakkomfreelance.com.components.AppLiterals.CHANNEL_ID;
import static sujithvakkomfreelance.com.components.AppLiterals.DELIVERY_CLASS_NAME;
import static sujithvakkomfreelance.com.components.AppLiterals.DELIVERY_HEADER_CLASS_NAME;

public class GSDeliveryService extends Service {
    public GSDeliveryService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                notificationIntent,
                0);

        Thread thread = new Thread(new LocationBroadcaster());
        thread.start();

        Thread threadSync = new Thread(new DataSync());
        threadSync.start();

        Notification notification = new NotificationCompat.Builder(this,
                CHANNEL_ID)
                .setContentTitle(AppLiterals.NOTIFICATION_TITLE)
                .setContentText("Service Started.")
                .setSmallIcon(R.drawable.ic_package_norification)
                .setContentIntent(pendingIntent).build();
        startForeground(1, notification);

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.\
        return null;
    }

    private class LocationBroadcaster implements Runnable {
        private final static String TAG = "LocationBroadcaster";

        @Override
        public void run() {
            final LoginToken loginToken = Util.getToken(getApplicationContext());
            if (loginToken != null)
                do {
                    try {
                        LocationHelper locationHelper = new LocationHelper(getApplicationContext());
                        locationHelper.getLastKnownLocation(new LocationHelper.onLocationHelperSuccessListener() {
                            @Override
                            public void lastKnownLocation(Location location) {
                                Double longitude = location.getLongitude();
                                Double latitude = location.getLatitude();
                                Float accuracy = location.getAccuracy();
                                location_tracker tracker = new location_tracker(
                                        null,
                                        loginToken.vehicle_code,
                                        Util.getCurrentDate(),
                                        latitude,
                                        longitude,
                                        accuracy
                                );

                                LocationTracker trackerLast = location_tracker.getLastKnownLocation(getApplicationContext());

                                if (trackerLast == null) {
                                    tracker.save(getApplicationContext());
                                    return;
                                }

                                Location lastLocation = new Location("Last Location");
                                lastLocation.setLatitude(trackerLast.getLatitude());
                                lastLocation.setLongitude(trackerLast.getLongitude());
                                if (lastLocation.distanceTo(location) > 10) {
                                    tracker.save(getApplicationContext());
                                }
                            }
                        });
                        Thread.sleep(Util.getInterval());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (true);
        }
    }

    private class DataSync implements Runnable {
        private final static String TAG = "DataSync";

        @Override
        public void run() {

            try {
                do {
                    ArrayList<location_tracker> test = new ArrayList<>();
                    test = location_tracker.getLocationTrackers(getApplicationContext(), 0);
                    ArrayList<LocationTracker> locations = new ArrayList<>();
                    for (LocationTracker location : test
                    ) {
                        locations.add(location);
                    }
                    if (locations.size() > 0)
                        saveLocationTracks(locations);

                    ArrayList<sync> synchList = sync.getSynchList(getApplicationContext(), false);
                    if (synchList != null)
                        for (final sync sync : synchList)
                            if (!sync.getSynced())
                                if (sync.getTag() == DELIVERY_CLASS_NAME) {
                                    try {
                                        delivery delivery = (delivery) sync.getValueBase();
                                        String baseUrl = Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                                        Retrofit.Builder builder = new Retrofit.Builder()
                                                .baseUrl(baseUrl)
                                                .addConverterFactory(GsonConverterFactory.create(
                                                        AppLiterals.APPLICATION_GSON_BUILDER));

                                        Retrofit retrofit = builder.build();

                                        IWebClient client = retrofit.create(IWebClient.class);

                                        Call<String> call = client.UpdateDelivery(delivery.getReceiptLineIdentifier(), AppLiterals.DELIVERY_STATUS.REJECTED, delivery.getDriver());
                                        call.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                sync.setSynced(true);
                                                sync.save(getApplicationContext());
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                sync.setSynced(false);
                                                sync.save(getApplicationContext());
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (sync.getTag() == DELIVERY_HEADER_CLASS_NAME) {
                                    try {
                                        DeliveryHeader delivery = (DeliveryHeader) sync.getValueBase();
                                        String baseUrl =
                                                Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
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
                                                sync.setSynced(true);
                                                sync.save(getApplicationContext());
                                            }

                                            @Override
                                            public void onFailure(Call<DeliveryHeader> call, Throwable t) {
                                                sync.setSynced(false);
                                                sync.save(getApplicationContext());
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                    try {
                        getNotifications();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Thread.sleep(Util.getInterval());
                }
                while (true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void getNotifications() {
            final LoginToken user = Util.getToken(getApplicationContext());
            String role = Util.getJsonValue(user.profile, AppLiterals.PROFILE_ROOT, AppLiterals.PROFILE_ROLE);
            if (role == null) role = "";
            if (role.equals(AppLiterals.DRIVER)) {

                try {
                    String baseUrl = Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create(
                                    AppLiterals.APPLICATION_GSON_BUILDER));

                    Retrofit retrofit = builder.build();

                    IWebClient client = retrofit.create(IWebClient.class);

                    Call<List<DeliveryHeader>> call = client.GetDeliveryJobsNotifications(
                            Util.getToken(getApplicationContext()).vehicle_code,
                            Integer.toString(AppLiterals.DELIVERY_STATUS.DISPATCHED));
                    call.enqueue(new Callback<List<DeliveryHeader>>() {
                        @Override
                        public void onResponse(Call<List<DeliveryHeader>> call, Response<List<DeliveryHeader>> response) {
                            List<DeliveryHeader> result = response.body();
                            if (result != null) {
                                int n = result.size();
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                        .setSmallIcon(R.drawable.ic_package_norification)
                                        .setContentTitle("Delivery scheduled")
                                        .setContentText("You have " + n + " new deliveries scheduled.")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                notificationManager.notify(1, builder.build());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<DeliveryHeader>> call, Throwable t) {
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void saveLocationTracks(final ArrayList<LocationTracker> locationTrackers) {
            try {
                String baseUrl =
                        Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(
                                AppLiterals.APPLICATION_GSON_BUILDER));

                Retrofit retrofit = builder.build();

                IWebClient client = retrofit.create(IWebClient.class);

                Call<String> call = client.PostUserLocation(locationTrackers);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body() != null)
                            Log.i(TAG, response.body());
                        for (LocationTracker tempTracker : locationTrackers
                        ) {
                            tempTracker.setSynched(1);
                            ((location_tracker) tempTracker).save(getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "Error running remote query.");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}