package sujithvakkomfreelance.com.components.localdata.dataproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;

import sujithvakkomfreelance.com.components.localdata.LocalDataHelper;
import sujithvakkomfreelance.com.components.localdata.contracts.SyncContract;

public class synch <T> {
    final Class<T> typeParameterClass;
    public String tag;
    public T valueBase;
    public String value;
    public boolean synched;

    public synch(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public synch() {
        typeParameterClass = null;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        if (value == null) {
            Gson gson = new Gson();
            value = gson.toJson(valueBase);
        }
        return value;
    }

    public T getValueBase() {
        if (value != null) {
            Gson gson = new Gson();
            valueBase =
                    gson.fromJson(value, typeParameterClass);
        } else valueBase = null;
        return valueBase;
    }

    public boolean getSynced() {
        return synched;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setSynched(boolean synched) {
        this.synched = synched;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ArrayList<synch> getSynchList(Context context, boolean isSynched) {
        ArrayList<synch> result = null;
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();

        Cursor cursor = db.query(SyncContract.Sync.TABLE_NAME,
                SyncContract.COLUMS,
                SyncContract.Sync.COLUMN_NAME_SYNCHED + "=? ",
                new String[]{(isSynched == true ? "1" : "0")},
                null,
                null,
                SyncContract.Sync.COLUMN_NAME_TAG);
        synch temp;
        if (cursor.moveToFirst()) {
            do {
                try {
                    temp = new synch();
                    temp.setTag(cursor.getString(cursor.getColumnIndex(SyncContract.Sync.COLUMN_NAME_TAG)));
                    temp.setValue(cursor.getString(cursor.getColumnIndex(SyncContract.Sync.COLUMN_NAME_VALUE)));
                    temp.setSynched(cursor.getInt(cursor.getColumnIndex(SyncContract.Sync.COLUMN_NAME_SYNCHED)) > 0);
                    result.add(temp);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        db.close();
        dataHelper.close();
        return null;
    }

    public boolean save(Context context) {
        long result = 0;
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(SyncContract.Sync.COLUMN_NAME_TAG, getTag());
            values.put(SyncContract.Sync.COLUMN_NAME_VALUE, getValue());
            values.put(SyncContract.Sync.COLUMN_NAME_SYNCHED, getSynced());

            db.beginTransaction();
            try {
                result = db.insertOrThrow(SyncContract.Sync.TABLE_NAME, null, values);
                db.setTransactionSuccessful();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            db.endTransaction();
            db.close();
            dataHelper.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result > 0;
    }
}
