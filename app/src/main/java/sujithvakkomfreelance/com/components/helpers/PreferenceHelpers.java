package sujithvakkomfreelance.com.components.helpers;

import android.content.SharedPreferences;

public class PreferenceHelpers {
    public static String getPreference(SharedPreferences preference, String keyString, String defaultValue) {
        String result =
                preference.getString(keyString, defaultValue);
        return result;
    }
}
