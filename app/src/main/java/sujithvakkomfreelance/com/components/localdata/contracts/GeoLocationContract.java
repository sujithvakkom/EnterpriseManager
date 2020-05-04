package sujithvakkomfreelance.com.components.localdata.contracts;

import android.provider.BaseColumns;

public class GeoLocationContract {
    private GeoLocationContract(){}

    public   static final String SQL_CREATE_LOCATION_TRACKER =
            "CREATE TABLE " + GeoLocation.TABLE_NAME + " (" +
                    GeoLocation.COLUMN_NAME_VEHICLE_CODE+ " TEXT"+
                    "," +
                    GeoLocation.COLUMN_NAME_LATITUDE + " REAL "+
                    "," +
                    GeoLocation.COLUMN_NAME_LONGITUDE + " REAL "+
                    "," +
                    GeoLocation.COLUMN_NAME_CREATED_TIME + " TEXT "+
                    "," +
                    GeoLocation.COLUMN_NAME_ACCURACY + " REAL "+
                    "," +
                    GeoLocation.COLUMN_NAME_SYNCHED + " INTEGER "+
                    ")";

    public static final String SQL_DROP_LOCATION_TRACKER =
            "DROP TABLE IF EXISTS " + GeoLocation.TABLE_NAME;

    public static final String SQL_GET_LOCATION_TRACKER =
            "SELECT " + GeoLocation.COLUMN_NAME_VEHICLE_CODE+
                    ", "+
                    GeoLocation.COLUMN_NAME_LATITUDE +
                    ", " +
                    GeoLocation.COLUMN_NAME_LONGITUDE +
                    ", " +
                    GeoLocation.COLUMN_NAME_CREATED_TIME +
                    ", " +
                    GeoLocation.COLUMN_NAME_ACCURACY +
                    ", " +
                    GeoLocation.COLUMN_NAME_SYNCHED +
                    " FROM "+ GeoLocation.TABLE_NAME;
    public static  final  String[] COLUMS ={
            GeoLocation.COLUMN_NAME_ROWID,
            GeoLocation.COLUMN_NAME_VEHICLE_CODE,
            GeoLocation.COLUMN_NAME_LATITUDE ,
            GeoLocation.COLUMN_NAME_LONGITUDE,
            GeoLocation.COLUMN_NAME_CREATED_TIME,
            GeoLocation.COLUMN_NAME_ACCURACY,
            GeoLocation.COLUMN_NAME_SYNCHED
    };

    public static class GeoLocation implements BaseColumns {
        public static final String TABLE_NAME = "location_tracker";
        public static final String COLUMN_NAME_ROWID = "rowid";
        public static final String COLUMN_NAME_VEHICLE_CODE = "vehicle_code";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE= "longitude";
        public static final String COLUMN_NAME_CREATED_TIME= "created_time";
        public static final String COLUMN_NAME_ACCURACY= "accuracy";
        public static final String COLUMN_NAME_SYNCHED = "synched";
    }
}