package sujithvakkomfreelance.com.components.localdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract;
import sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract;
import sujithvakkomfreelance.com.components.localdata.contracts.DeliveryReaderContract;
import sujithvakkomfreelance.com.components.localdata.contracts.GeoLocationContract;
import sujithvakkomfreelance.com.components.localdata.contracts.SyncContract;
import sujithvakkomfreelance.com.components.localdata.contracts.UserReaderContract;

public class LocalDataHelper extends SQLiteOpenHelper implements ISQLCommandBase {
    public LocalDataHelper( Context context) {
        super(context.getApplicationContext(), NAME, null, DATABASE_VERSION);
        //SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(DeliveryReaderContract.Delivery.TABLE_NAME,null,null);
        //db.delete(UserReaderContract.User.TABLE_NAME,null,null);
        //db.execSQL(UserReaderContract.SQL_DROP_USER);
        //db.execSQL(UserReaderContract.SQL_CREATE_USER);
        //ContentValues contentValues =
        //new ContentValues();contentValues.put(
        //        DeliveryReaderContract.Delivery.COLUMN_NAME_DRIVER_NAME , "Driver1"

        //);
        //db.update(DeliveryReaderContract.Delivery.TABLE_NAME,contentValues,null,null
        //        );
        //db.close();
/*
        SQLiteDatabase db = this.getWritableDatabase();
        String addCml = "ALTER TABLE " +
                UserReaderContract.User.TABLE_NAME +
                " ADD " +
                UserReaderContract.User.COLUMN_NAME_SESSION +
                " TEXT ";
        db.execSQL(addCml);
        db.close();
        */
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DeliveryReaderContract.SQL_CREATE_DELIVERY);
        db.execSQL(DeliveryReaderContract.SQL_INDEX_JOB);
        db.execSQL(UserReaderContract.SQL_CREATE_USER);
        db.execSQL(SyncContract.SQL_CREATE_SYNCH);
        db.execSQL(GeoLocationContract.SQL_CREATE_LOCATION_TRACKER);
        db.execSQL(DeliveryHeaderContract.SQL_CREATE_DELIVERY_HEADER);
        db.execSQL(DeliveryLineContract.SQL_CREATE_DELIVERY_LINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<newVersion) {
            db.execSQL(UserReaderContract.SQL_DROP_USER);
            db.execSQL(UserReaderContract.SQL_CREATE_USER);
        }
        //onCreate(db);
    }

}
