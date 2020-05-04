package sujithvakkomfreelance.com.enterprisemanager.customviews;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.models.LoginToken;
import sujithvakkomfreelance.com.enterprisemanager.DeliveryActivity;
import sujithvakkomfreelance.com.enterprisemanager.GSDeliveryService;

public class HomeLoaderHelper {


    public static Intent loadHome(Context context) {
        Intent activityIntent;
        LoginToken user = Util.getToken(context);
        //String[] role = (String[]) Util.getJsonValue(user.profile, AppLiterals.PROFILE_ROOT,AppLiterals.PROFILE_ROLE);
        //if(role==null)role=new String[]{""};
        activityIntent = new Intent(context, DeliveryActivity.class);
        activityIntent.putExtra(AppLiterals.USER_DETAIL,user);
        return activityIntent;
    }
}
