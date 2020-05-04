package sujithvakkomfreelance.com.components.localdata.dataproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.localdata.IDataClass;
import sujithvakkomfreelance.com.components.localdata.LocalDataHelper;
import sujithvakkomfreelance.com.components.localdata.contracts.UserReaderContract;
import sujithvakkomfreelance.com.components.models.LoginToken;

public class UserDetail implements Serializable , IDataClass {

    public String signature;
    public String session;
    public String user_name;
    public String full_name;
    public String vehicle_code;
    public String profile;

    public UserDetail(String userName, String fullName, String vehicleCode, String profile, String signature, String session) {
        this.user_name = userName;
        this.full_name = fullName;
        this.vehicle_code = vehicleCode;
        this.profile = profile;
        this.signature = signature;
        this.session = session;
    }

    public static UserDetail getUser(Context context) {
        UserDetail userDetail = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(UserReaderContract.User.TABLE_NAME,
                    UserReaderContract.COLUMS,
                    null,
                    null,
                    null,
                    null,
                    UserReaderContract.User.COLUMN_NAME_USER_NAME,
                    "1");
            if (cursor != null && cursor.moveToFirst()) {
                String userName = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_USER_NAME));
                String fullName = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_FULL_NAME));
                String vehicleCode = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_VEHICLE_CODE));
                String profile = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_PROFILE));
                String sign = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_SIGN));
                String session = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_SESSION));
                userDetail = new UserDetail(userName, fullName, vehicleCode, profile, sign,session);
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        databaseHelper.close();
        return userDetail;
    }

    public static LoginToken getUserToken(Context context){
        LoginToken userDetail = null;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            Cursor cursor =
                    db.query(UserReaderContract.User.TABLE_NAME,
                            UserReaderContract.COLUMS,
                            null,
                            null,
                            null,
                            null,
                            UserReaderContract.User.COLUMN_NAME_USER_NAME,"1");
                    //db.rawQuery(UserReaderContract.SQL_GET_USER,null);
            if (cursor != null && cursor.moveToFirst()) {
                String userName = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_USER_NAME));
                String fullName = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_FULL_NAME));
                String vehicleCode = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_VEHICLE_CODE));
                String profile = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_PROFILE));
                String sign = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_SIGN));
                String session = cursor.getString(cursor
                        .getColumnIndex(UserReaderContract.User.COLUMN_NAME_SESSION));

                userDetail = new LoginToken(userName, fullName,vehicleCode,profile,sign,session);
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        databaseHelper.close();
        return userDetail;
    }

    public static boolean Logoff(Context context) {
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        long result = 0;
        try {
            result = db.delete(UserReaderContract.User.TABLE_NAME, null, null);

        } catch (Exception ex) {
            result = 0;
        }
        db.close();
        databaseHelper.close();
        return result>0;
    }

    public long save(Context context) {
        long result = -1;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserReaderContract.User.COLUMN_NAME_USER_NAME, user_name);
        values.put(UserReaderContract.User.COLUMN_NAME_FULL_NAME, full_name);
        values.put(UserReaderContract.User.COLUMN_NAME_VEHICLE_CODE, vehicle_code);
        values.put(UserReaderContract.User.COLUMN_NAME_PROFILE, profile);
        values.put(UserReaderContract.User.COLUMN_NAME_SIGN, signature);
        values.put(UserReaderContract.User.COLUMN_NAME_SESSION, session);
        boolean exists = this.exists(context);
        db.beginTransaction();
        try {
            if (!exists)
                result = db.insertOrThrow(UserReaderContract.User.TABLE_NAME,
                        null,
                        values);
            else
                result = db.update(UserReaderContract.User.TABLE_NAME,
                        values,
                        UserReaderContract.User.COLUMN_NAME_USER_NAME + "=?",
                        new String[]{this.user_name});
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.endTransaction();
        db.close();
        databaseHelper.close();
        return result;
    }

    private boolean exists(Context context) {
        return getUser(context) != null;
    }

    public String getRole() {
        try {
            String role = Util.getJsonValue(this.profile, AppLiterals.PROFILE_ROOT, AppLiterals.PROFILE_ROLE);
            return role;
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
}
