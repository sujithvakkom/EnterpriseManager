package sujithvakkomfreelance.com.components.localdata.contracts;

import android.provider.BaseColumns;

public class SettingsContract {
    private SettingsContract(){}
    public   static final String SQL_CREATE_SETTINGS =
            "CREATE TABLE " + Settings.TABLE_NAME + " (" +
                    Settings.COLUMN_NAME_USER_NAME+ " TEXT PRIMARY KEY," +
                    Settings.COLUMN_NAME_NAME + " TEXT , "+
                    Settings.COLUMN_NAME_NAME +" TEXT , "+
                    Settings.COLUMN_NAME_VALUE +" TEXT "+
                    ")";
    public static final String SQL_DELETE_SETTINGS =
            "DROP TABLE IF EXISTS " + Settings.TABLE_NAME;
    public static final String SQL_GET_SETTINGS =
            "SELECT " + Settings.COLUMN_NAME_USER_NAME+", "+
                    Settings.COLUMN_NAME_USER_NAME+", "+
                    Settings.COLUMN_NAME_NAME+", "+
                    Settings.COLUMN_NAME_VALUE+
                    " FROM "+ Settings.TABLE_NAME;


    public static class Settings implements BaseColumns {
        public static final String TABLE_NAME = "settings";
        public static final String COLUMN_NAME_USER_NAME = "user_name";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_VALUE = "value";
    }
}