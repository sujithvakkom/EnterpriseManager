package sujithvakkomfreelance.com.components.localdata.contracts;

import android.provider.BaseColumns;

public class UserReaderContract {
    private UserReaderContract(){}
    public   static final String SQL_CREATE_USER =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User.COLUMN_NAME_USER_NAME+ " TEXT PRIMARY KEY," +
                    User.COLUMN_NAME_FULL_NAME + " TEXT,"+
                    User.COLUMN_NAME_VEHICLE_CODE + " TEXT,"+
                    User.COLUMN_NAME_PROFILE + " TEXT, "+
                    User.COLUMN_NAME_SIGN + " TEXT, "+
                    User.COLUMN_NAME_SESSION + " TEXT"+
                    ")";

    public static final String SQL_DROP_USER =
            "DROP TABLE IF EXISTS " + User.TABLE_NAME;

    public static final String SQL_GET_USER =
            "SELECT " +User.COLUMN_NAME_USER_NAME+", "+
                    User.COLUMN_NAME_FULL_NAME+", "+
                    User.COLUMN_NAME_VEHICLE_CODE+", "+
                    User.COLUMN_NAME_PROFILE+", "+
                    User.COLUMN_NAME_SIGN+", "+
                    User.COLUMN_NAME_SESSION+
                    " FROM "+User.TABLE_NAME;
    public static  final  String[] COLUMS ={
            User.COLUMN_NAME_USER_NAME,
            User.COLUMN_NAME_FULL_NAME,
            User.COLUMN_NAME_VEHICLE_CODE,
            User.COLUMN_NAME_PROFILE,
            User.COLUMN_NAME_SIGN,
            User.COLUMN_NAME_SESSION
    };

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USER_NAME= "user_name";
        public static final String COLUMN_NAME_FULL_NAME = "full_name";
        public static final String COLUMN_NAME_VEHICLE_CODE = "vehicle_code";
        public static final String COLUMN_NAME_PROFILE = "profile";
        public static final String COLUMN_NAME_SIGN = "sign";
        public static final String COLUMN_NAME_SESSION = "session";
    }
}
