package sujithvakkomfreelance.com.components;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sujithvakkomfreelance.com.components.helpers.PreferenceHelpers;
import sujithvakkomfreelance.com.components.localdata.dataproviders.UserDetail;
import sujithvakkomfreelance.com.components.localdata.dataproviders.sync;
import sujithvakkomfreelance.com.components.localdata.logEnabled;
import sujithvakkomfreelance.com.components.models.LoginToken;
import sujithvakkomfreelance.com.components.web.IWebClient;
import sujithvakkomfreelance.com.enterprisemanager.R;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class Util {
    private static final String TAG = "Util";
    public static LoginToken getToken(Context context) {
        LoginToken user = UserDetail.getUserToken(context);
        return  user;
    }

    public static String getJsonValue(String jSON,String root,String key){
        String value = null;
        try {
            JSONObject reader = new JSONObject(jSON);
            JSONObject main = reader.getJSONObject(root);
            value = main.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,"Error parsing: "+e.getMessage());
        }
        catch (NullPointerException e){
            e.printStackTrace();
            Log.e(TAG,"Error parsing: "+e.getMessage());
        }
        return  value;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * R.string.pref_head_office_key
     * @return A new instance of fragment DeliveryJobsFragment.
     */
    public static String getSettingById(Context context, int id){
        String defaultValue;
        switch (id){
            case R.string.pref_head_office_key:
                defaultValue = context.getResources().getString(
                        R.string.pref_head_office_default
                );
                break;
                default:
                    defaultValue = "";
        }

        String baseUrl = PreferenceHelpers.getPreference(
                PreferenceManager.getDefaultSharedPreferences(context),
                context.getResources().getString(id),
                defaultValue);

        return baseUrl;
    }

    public static void Logoff(Context context) {
        UserDetail.Logoff(context);
    }

    public static String getCustomerCareNumber() {
        return "+97142823700";
    }

    /***
     * Get current date as integer string
     * @return
     */
    public static String getTodayDate() {
        final Calendar c = Calendar.getInstance();
        int todaysDate =     (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:",String.valueOf(todaysDate));
        return(String.valueOf(todaysDate));
    }

    /***
     * Get current time as integer string
     * @return
     */
    public static String getCurrentTimeString() {

        final Calendar c = Calendar.getInstance();
        int currentTime =     (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:",String.valueOf(currentTime));
        return(String.valueOf(currentTime));

    }

    /***
     * Get current Date
     * @return
     */
    public static Date getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    /**
    * Add to syncable items to sync for later
    * */
    public  static <T> void addToSynch(T value, String updateRemark) {
        addToSynch(value,updateRemark,false);
    }
    public  static <T> void addToSynch(T value, String updateRemark,boolean synked) {
        try {
            ((logEnabled)value).setRemarkForLog(updateRemark);
            sync<T> sync = new sync( Class.forName(value.getClass().getName()));
            sync.setValueBase(value);
            sync.setSynced(false);
            sync.save(getApplicationContext());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static long getInterval() {
        long min = 60000;
        return min*1;
    }

    public static long getInterval(int id) {
        long min = 60000;
        return min*10;
    }

    public static Snackbar SimpleSnackBar(View parentView, String messgage, int lengthLong) {
        return Snackbar.make(parentView, messgage, lengthLong);
    }

    public static void updateToken(UserDetail token, Context context, final IUpdateTokenCallback callback) {
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
                    if(response.body()!=null)
                        callback.updated(response.body());
                }

                @Override
                public void onFailure(Call<UserDetail> call, Throwable t) {
                    Log.e(Util.TAG,t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (Objects.equals(v, e))
                return true;
        return false;
    }
}
