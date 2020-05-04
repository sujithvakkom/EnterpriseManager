package sujithvakkomfreelance.com.components.localdata.dataproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.localdata.IDataClass;
import sujithvakkomfreelance.com.components.localdata.LocalDataHelper;
import sujithvakkomfreelance.com.components.localdata.contracts.DeliveryReaderContract;
import sujithvakkomfreelance.com.components.localdata.contracts.GeoLocationContract;
import sujithvakkomfreelance.com.components.models.LocationTracker;

public class location_tracker extends LocationTracker implements IDataClass{
    private static final String TAG = "location_tracker";

    public location_tracker(
            @Nullable Integer id,
            String vehicleCode,
            Date createdTime,
            Double latitude,
            Double longitude,
            Float accuracy) {
        super(id,vehicleCode,createdTime, latitude, longitude, accuracy);
    }

    public static ArrayList<location_tracker> getLocationTrackers(Context context, Integer synched) {
        ArrayList<location_tracker> result = new ArrayList<>();
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        String temp = synched.toString();
        try {
            Cursor cursor = db.query(
                    GeoLocationContract.GeoLocation.TABLE_NAME,
                    GeoLocationContract.COLUMS,
                    GeoLocationContract.GeoLocation.COLUMN_NAME_SYNCHED + " = ? ",
                    new String[]{temp},
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                do {
                    Date createdDateTemp = new Date();
                    try {
                        createdDateTemp =
                                AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_CREATED_TIME)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    result.add(new location_tracker(
                            cursor.getInt(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_ROWID)),
                            cursor.getString(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_VEHICLE_CODE)),
                            createdDateTemp,
                            cursor.getDouble(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_LATITUDE)),
                            cursor.getDouble(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_LONGITUDE)),
                            cursor.getFloat(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_ACCURACY))));
                }
                while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Error getting locations: " + ex.getMessage());
        }
        db.close();
        dataHelper.close();
        return result;
    }

    public static location_tracker getLastKnownLocation(Context context) {
        location_tracker result = null;
        try {
            LocalDataHelper dataHelper = new LocalDataHelper(context);
            SQLiteDatabase db = dataHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    GeoLocationContract.GeoLocation.TABLE_NAME,
                    GeoLocationContract.COLUMS,
                    null,
                    null,
                    null, null,
                    GeoLocationContract.GeoLocation.COLUMN_NAME_ROWID + " DESC ",
                    "1"
            );
            if (cursor.moveToFirst()) {
                do {
                    Date createdDateTemp = new Date();
                    try {
                        createdDateTemp =
                                AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_CREATED_TIME)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    result = (new location_tracker(
                            cursor.getInt(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_ROWID)),
                            cursor.getString(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_VEHICLE_CODE)),
                            createdDateTemp,
                            cursor.getDouble(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_LATITUDE)),
                            cursor.getDouble(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_LONGITUDE)),
                            cursor.getFloat(cursor.getColumnIndex(GeoLocationContract.GeoLocation.COLUMN_NAME_ACCURACY))));
                }
                while (cursor.moveToNext());
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Log.i(TAG,"Error getting last location: "+ex.getMessage());
        }
        return result;
    }

    @Override
    public long save(Context context) {
        long result = -1;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GeoLocationContract.GeoLocation.COLUMN_NAME_VEHICLE_CODE, this.getVehicleCode());
        String dateString =AppLiterals.APPLICATION_DATE_FORMAT.format( this.getCreatedTime());
        values.put(GeoLocationContract.GeoLocation.COLUMN_NAME_CREATED_TIME,
                dateString
                );
        values.put(GeoLocationContract.GeoLocation.COLUMN_NAME_LATITUDE, this.getLatitude());
        values.put(GeoLocationContract.GeoLocation.COLUMN_NAME_LONGITUDE, this.getLongitude());
        values.put(GeoLocationContract.GeoLocation.COLUMN_NAME_ACCURACY, this.getAccuracy());
        values.put(GeoLocationContract.GeoLocation.COLUMN_NAME_SYNCHED, this.getSynched());
        db.beginTransaction();
        try {
            if(this.getId()==null)
            result = db.insert(
                    GeoLocationContract.GeoLocation.TABLE_NAME,
                    null,
                    values);
            else
                result  = db.update(
                    GeoLocationContract.GeoLocation.TABLE_NAME,
                    values,
                    GeoLocationContract.GeoLocation.COLUMN_NAME_ROWID +" = ?",
                    new String[]{this.getId().toString()}
            );
            Log.i(TAG,"Tracker save success.");
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG,"Error save: "+ex.getMessage());
        }
        db.endTransaction();
        db.close();
        databaseHelper.close();
        return 0;
    }
}
