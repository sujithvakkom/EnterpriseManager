package sujithvakkomfreelance.com.components.localdata.dataproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.localdata.LocalDataHelper;
import sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryLine;

import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_CUSTOMER_NAME;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_DRIVER_NAME;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_GPS;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_HEADER_ID;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_MAP_URL;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_ORDER_NUMBER;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_PHONE;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_RECEIPT;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_REMARKS;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_SALE_DATE;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_SCHEDULED_DATE;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_STATUS;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_TIME_SLOT;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_VEHICLE_CODE;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract.DeliveryHeader.TABLE_NAME;

public class DeliveryHeaderProvider {

    public long save(Context context, DeliveryHeader delivery) {
        long result = 0;
        if(delivery.DeliveryLines!=null) {
            for (DeliveryLine line : delivery.DeliveryLines) {
                delivery.Status = line.Status;
                if (line.Status == AppLiterals.DELIVERY_STATUS.DISPATCHED) {
                    break;
                }
                if (delivery.getScheduledDate() == null)
                    delivery.ScheduledDate = line.ScheduledDate;
                else if (delivery.getScheduledDate().compareTo(line.ScheduledDate) > 0) {
                    delivery.ScheduledDate = line.ScheduledDate;
                }
            }
        }

        ContentValues values = new ContentValues();
        try {
            values.put(COLUMN_NAME_CUSTOMER_NAME, delivery.CustomerName);
            values.put(COLUMN_NAME_DRIVER_NAME, delivery.DriverName);
            values.put(COLUMN_NAME_GPS, delivery.GPS);
            values.put(COLUMN_NAME_HEADER_ID, delivery.HeaderId);
            values.put(COLUMN_NAME_MAP_URL, delivery.MapURL);
            values.put(COLUMN_NAME_ORDER_NUMBER, delivery.OrderNumber);
            values.put(COLUMN_NAME_PHONE, delivery.Phone);
            values.put(COLUMN_NAME_RECEIPT, delivery.Receipt);
            values.put(COLUMN_NAME_REMARKS, delivery.Remarks);
            if (delivery.SaleDate != null)
                values.put(COLUMN_NAME_SALE_DATE,
                        AppLiterals.APPLICATION_DATE_FORMAT.format(delivery.SaleDate));
            if (delivery.ScheduledDate != null)
                values.put(COLUMN_NAME_SCHEDULED_DATE,
                        AppLiterals.APPLICATION_DATE_FORMAT.format(delivery.ScheduledDate));
            values.put(COLUMN_NAME_STATUS, delivery.Status);
            values.put(COLUMN_NAME_TIME_SLOT, delivery.TimeSlot);
            values.put(COLUMN_NAME_VEHICLE_CODE, delivery.VehicleCode);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
        LocalDataHelper helper = new LocalDataHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (this.exists(delivery, context)) {
            db.beginTransaction();
            try {

                result = db.update(TABLE_NAME,
                        values,
                        DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_HEADER_ID + " = ?",
                        new String[]{delivery.HeaderId.toString()});
                if (result > 0) {
                    for (DeliveryLine line :
                            delivery.DeliveryLines
                    ) {
                        DeliveryLineProvider provider = new DeliveryLineProvider();
                        if (provider.save(db, line, delivery.HeaderId) > 0) {
                        } else {
                            throw new Exception();
                        }
                    }
                }
                db.setTransactionSuccessful();
            } catch (Exception ex) {
                ex.printStackTrace();
                result = 0;
            } finally {

                db.endTransaction();
                db.close();
                helper.close();
            }
        } else {
            try {
                db.beginTransaction();
                result = db.insert(TABLE_NAME, null, values);
                if (result > 0) {
                    for (DeliveryLine line :
                            delivery.DeliveryLines
                    ) {
                        DeliveryLineProvider provider = new DeliveryLineProvider();
                        if (provider.save(db, line, delivery.HeaderId) > 0) {
                        } else {
                            throw new Exception();
                        }
                    }
                }
                db.setTransactionSuccessful();
            } catch (Exception ex) {
                ex.printStackTrace();
                result = 0;
            } finally {
                db.endTransaction();
                db.close();
                helper.close();
            }
        }
        return result;
    }

    private boolean exists(DeliveryHeader data, Context db) {
        return  this.getDeliveryJob(db,data.HeaderId)!=null;
    }

    public ArrayList<DeliveryHeader> getList(Context context, String user_name, Integer status) {
        ArrayList<DeliveryHeader> deliveries = new ArrayList<>();
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = db.query(DeliveryHeaderContract.DeliveryHeader.TABLE_NAME,
                DeliveryHeaderContract.COLUMS,
                DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_DRIVER_NAME + "=?" +
                        (status == null ? "" : " AND " +
                                DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_STATUS + "=?"
                        )
                ,
                status == null ?
                        new String[]{user_name} :
                        new String[]{user_name, status.toString()}
                , null, null, DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_STATUS);
        DeliveryHeader temp;

        if (cursor.moveToFirst()) {
            do {
                try {
                    temp = new DeliveryHeader();
                    temp.HeaderId = cursor.getInt(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_HEADER_ID));
                    temp.Receipt = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_RECEIPT));
                    temp.DriverName = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_DRIVER_NAME));
                    temp.OrderNumber = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_ORDER_NUMBER));
                    temp.CustomerName = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_CUSTOMER_NAME));
                    temp.DeliveryAddress = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_CUSTOMER_ADDRESS));
                    temp.Emirate = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_EMIRATE));
                    temp.GPS = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_GPS));
                    temp.MapURL = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_MAP_URL));
                    temp.Phone = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_PHONE));
                    temp.Remarks = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_REMARKS));
                    try {
                        temp.ScheduledDate =
                                AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                        getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_SCHEDULED_DATE)));
                    } catch (Exception ex) {
                        DeliveryLineProvider provider = new DeliveryLineProvider();
                        ArrayList<DeliveryLine> list = provider.getList(context, temp.HeaderId, AppLiterals.DELIVERY_STATUS.DISPATCHED);
                        for (DeliveryLine x:list
                             ) {
                            if( temp.ScheduledDate == null)
                            temp.ScheduledDate = x.ScheduledDate;
                            else {
                                if(temp.getScheduledDate().compareTo(x.ScheduledDate)>0)
                                    temp.ScheduledDate = x.ScheduledDate;
                            }
                        }
                        ex.printStackTrace();
                    }
                    try {
                        temp.SaleDate =
                                AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                        getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_SALE_DATE)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        temp.Status = cursor.
                                getInt(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_STATUS));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    temp.TimeSlot = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_TIME_SLOT));
                    deliveries.add(temp);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        db.close();
        dataHelper.close();
        return deliveries;
    }

    public DeliveryHeader getDeliveryJob(Context context,  Integer headerID) {
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(DeliveryHeaderContract.DeliveryHeader.TABLE_NAME,
                    DeliveryHeaderContract.COLUMS,
                    DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_HEADER_ID + "=?"
                    , new String[]{headerID.toString()}
                    , null, null, DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_STATUS);
            DeliveryHeader temp;

            if (cursor.moveToFirst()) {
                do {
                    try {
                        temp = new DeliveryHeader();
                        temp.HeaderId = cursor.getInt(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_HEADER_ID));
                        temp.Receipt = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_RECEIPT));
                        temp.OrderNumber = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_ORDER_NUMBER));
                        temp.CustomerName = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_CUSTOMER_NAME));
                        temp.DeliveryAddress = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_CUSTOMER_ADDRESS));
                        temp.Emirate = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_EMIRATE));
                        temp.GPS = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_GPS));
                        temp.MapURL = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_MAP_URL));
                        temp.Phone = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_PHONE));
                        temp.Remarks = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_REMARKS));
                        try {
                            temp.ScheduledDate =
                                    AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                            getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_SCHEDULED_DATE)));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.SaleDate =
                                    AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                            getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_SALE_DATE)));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        temp.TimeSlot = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_TIME_SLOT));
                        return temp;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        db.close();
        dataHelper.close();
        return null;
    }

    public DeliveryHeader getDeliveryJob(Context context,  String orderNumber) {
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(DeliveryHeaderContract.DeliveryHeader.TABLE_NAME,
                    DeliveryHeaderContract.COLUMS,
                    DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_ORDER_NUMBER + "=?"
                    , new String[]{orderNumber}
                    , null, null, DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_STATUS);
            DeliveryHeader temp;

            if (cursor.moveToFirst()) {
                do {
                    try {
                        temp = new DeliveryHeader();
                        temp.HeaderId = cursor.getInt(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_HEADER_ID));
                        temp.Receipt = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_RECEIPT));
                        temp.OrderNumber = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_ORDER_NUMBER));
                        temp.CustomerName = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_CUSTOMER_NAME));
                        temp.DeliveryAddress = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_CUSTOMER_ADDRESS));
                        temp.Emirate = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_EMIRATE));
                        temp.GPS = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_GPS));
                        temp.MapURL = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_MAP_URL));
                        temp.Phone = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_PHONE));
                        temp.Remarks = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_REMARKS));
                        try {
                            temp.ScheduledDate =
                                    AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                            getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_SCHEDULED_DATE)));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.SaleDate =
                                    AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                            getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_SALE_DATE)));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        temp.TimeSlot = cursor.getString(cursor.getColumnIndex(DeliveryHeaderContract.DeliveryHeader.COLUMN_NAME_TIME_SLOT));
                        return temp;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        db.close();
        dataHelper.close();
        return null;
    }

}
