package sujithvakkomfreelance.com.components.localdata.dataproviders;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.localdata.IDataClass;
import sujithvakkomfreelance.com.components.localdata.LocalDataHelper;
import sujithvakkomfreelance.com.components.localdata.contracts.DeliveryReaderContract;
import sujithvakkomfreelance.com.components.localdata.logEnabled;

public class delivery extends logEnabled implements Serializable, IDataClass {

    public delivery(Integer id,
                    Integer ROWID,
                    String TRANS_TYPE,
                    String RECEIPTID,
                    String TRANSACTIONID,
                    Date TRANSDATE,
                    Double LINENUM,
                    String ITEMID,
                    String DESCRIPTION,
                    Double QUANTITY,
                    Double SEIINGPRICE,
                    String COMMENT,
                    String DELIVARYTYPE,
                    String ACCOUNTNUM,
                    String NAME,
                    String STREET,
                    String ADDRESS,
                    String DELIVERY_ADDRESS,
                    String EMIRATE,
                    String GPS,
                    String REQ_NUMBER,
                    Double Int_STATUS,
                    Date DT_STATUS,
                    Date DT_SCHEDULED,
                    String REMARKS,
                    Date DT_ESTIMATED,
                    String LPO_NUMBER,
                    String TIME_SLOT,
                    String SCHEDULED_ASPER,
                    String DRIVER_NAME,
                    String FAILED_REASON,
                    Integer CUST_NO,
                    Integer SITE_NO,
                    Integer EMP_ID,
                    Date CREATED_ON,
                    String WARRANTY,
                    int RECEIPT_CREATED,
                    String ORA_REQ_NUMBER,
                    String ORA_ORDER_NO,
                    String ORA_ORDER_STATUS,
                    String ORA_SHIPMENT_STATUS,
                    String SHIPMENT_NUMBER,
                    String RECEIPT_NUMBER,
                    Double REQUISITION_HEADER_ID,
                    Double REQUISITION_LINE_ID,
                    String VEHICLE_CODE,
                    String GPS_CORDINATES,
                    String MAP_URL
    ) {
        this.id = id;
        this.ROWID = ROWID;
        this.TRANS_TYPE = TRANS_TYPE;
        this.RECEIPTID = RECEIPTID;
        this.TRANSACTIONID = TRANSACTIONID;
        this.TRANSDATE = TRANSDATE;
        this.LINENUM = LINENUM;
        this.ITEMID = ITEMID;
        this.DESCRIPTION = DESCRIPTION;
        this.QUANTITY = QUANTITY;
        this.SEIINGPRICE = SEIINGPRICE;
        this.COMMENT = COMMENT;
        this.DELIVARYTYPE = DELIVARYTYPE;
        this.ACCOUNTNUM = ACCOUNTNUM;
        this.NAME = NAME;
        this.STREET = STREET;
        this.ADDRESS = ADDRESS;
        this.DELIVERY_ADDRESS = DELIVERY_ADDRESS;
        this.EMIRATE = EMIRATE;
        this.GPS = GPS;
        this.REQ_NUMBER = REQ_NUMBER;
        this.Int_STATUS = Int_STATUS;
        this.DT_STATUS = DT_STATUS;
        this.DT_SCHEDULED = DT_SCHEDULED;
        this.REMARKS = REMARKS;
        this.DT_ESTIMATED = DT_ESTIMATED;
        this.LPO_NUMBER = LPO_NUMBER;
        this.TIME_SLOT = TIME_SLOT;
        this.SCHEDULED_ASPER = SCHEDULED_ASPER;
        this.DRIVER_NAME = DRIVER_NAME;
        this.FAILED_REASON = FAILED_REASON;
        this.CUST_NO = CUST_NO;
        this.SITE_NO = SITE_NO;
        this.EMP_ID = EMP_ID;
        this.CREATED_ON = CREATED_ON;
        this.WARRANTY = WARRANTY;
        this.RECEIPT_CREATED = RECEIPT_CREATED;
        this.ORA_REQ_NUMBER = ORA_REQ_NUMBER;
        this.ORA_ORDER_NO = ORA_ORDER_NO;
        this.ORA_ORDER_STATUS = ORA_ORDER_STATUS;
        this.ORA_SHIPMENT_STATUS = ORA_SHIPMENT_STATUS;
        this.SHIPMENT_NUMBER = SHIPMENT_NUMBER;
        this.RECEIPT_NUMBER = RECEIPT_NUMBER;
        this.REQUISITION_HEADER_ID = REQUISITION_HEADER_ID;
        this.REQUISITION_LINE_ID = REQUISITION_LINE_ID;
        this.VEHICLE_CODE = VEHICLE_CODE;
        this.GPS_CORDINATES = GPS_CORDINATES;
        this.MAP_URL = GPS_CORDINATES;
    }

    public Integer id;
    public Integer ROWID;
    public String TRANS_TYPE;
    public String RECEIPTID;
    public String TRANSACTIONID;
    public Date TRANSDATE;
    public Double LINENUM;
    public String ITEMID;
    public String DESCRIPTION;
    public Double QUANTITY;
    public Double SEIINGPRICE;
    public String COMMENT;
    public String DELIVARYTYPE;
    public String ACCOUNTNUM;
    public String NAME;
    public String STREET;
    public String ADDRESS;
    public String DELIVERY_ADDRESS;
    public String EMIRATE;
    public String GPS;
    public String REQ_NUMBER;
    public Double Int_STATUS;
    public Date DT_STATUS;
    public Date DT_SCHEDULED;
    public String REMARKS;
    public Date DT_ESTIMATED;
    public String LPO_NUMBER;
    public String TIME_SLOT;
    public String SCHEDULED_ASPER;
    public String DRIVER_NAME;
    public String FAILED_REASON;
    public Integer CUST_NO;
    public Integer SITE_NO;
    public Integer EMP_ID;
    public Date CREATED_ON;
    public String WARRANTY;
    public int RECEIPT_CREATED;
    public String ORA_REQ_NUMBER;
    public String ORA_ORDER_NO;
    public String ORA_ORDER_STATUS;
    public String ORA_SHIPMENT_STATUS;
    public String SHIPMENT_NUMBER;
    public String RECEIPT_NUMBER;
    public Double REQUISITION_HEADER_ID;
    public Double REQUISITION_LINE_ID;
    public String VEHICLE_CODE;
    public String GPS_CORDINATES;
    public String MAP_URL;

    public void setRemarks(String REMARKS) {
        this.REMARKS = REMARKS;
    }

    public String getReceipt() {
        return RECEIPTID;
    }

    @Override
    public String toString() {
        return DESCRIPTION;
    }

    @Override
    public long save(Context context) {
        long result = -1;
        LocalDataHelper databaseHelper = new LocalDataHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ID, id);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ROWID, ROWID);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANS_TYPE, TRANS_TYPE);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPTID, RECEIPTID);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANSACTIONID, TRANSACTIONID);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANSDATE, AppLiterals.APPLICATION_DATE_FORMAT.format(TRANSDATE));
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_LINENUM, LINENUM);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ITEMID, ITEMID);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_DESCRIPTION, DESCRIPTION);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_QUANTITY, QUANTITY);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_SEIINGPRICE, SEIINGPRICE);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_COMMENT, COMMENT);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_DELIVARYTYPE, DELIVARYTYPE);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ACCOUNTNUM, ACCOUNTNUM);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_NAME, NAME);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_STREET, STREET);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ADDRESS, ADDRESS);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_DELIVERY_ADDRESS, DELIVERY_ADDRESS);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_EMIRATE, EMIRATE);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_GPS, GPS);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_REQ_NUMBER, REQ_NUMBER);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_INT_STATUS, Int_STATUS);
            try {
                values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_STATUS, AppLiterals.APPLICATION_DATE_FORMAT.format(DT_STATUS));
            } catch (Exception ex) {
                values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_STATUS, AppLiterals.APPLICATION_DATE_FORMAT.format(DT_SCHEDULED));
            }
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_SCHEDULED, AppLiterals.APPLICATION_DATE_FORMAT.format(DT_SCHEDULED));
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_REMARKS, REMARKS);
            try {
                values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_ESTIMATED, AppLiterals.APPLICATION_DATE_FORMAT.format(DT_ESTIMATED));
            } catch (Exception ex) {
                values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_ESTIMATED, AppLiterals.APPLICATION_DATE_FORMAT.format(DT_SCHEDULED));
            }
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_LPO_NUMBER, LPO_NUMBER);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_TIME_SLOT, TIME_SLOT);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_SCHEDULED_ASPER, SCHEDULED_ASPER);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_DRIVER_NAME, DRIVER_NAME);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_FAILED_REASON, FAILED_REASON);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_CUST_NO, CUST_NO);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_SITE_NO, SITE_NO);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_EMP_ID, EMP_ID);
            try {
                values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_CREATED_ON, AppLiterals.APPLICATION_DATE_FORMAT.format(CREATED_ON));
            } catch (Exception ex) {
                values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_CREATED_ON, AppLiterals.APPLICATION_DATE_FORMAT.format(DT_SCHEDULED));
            }
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_WARRANTY, WARRANTY);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPT_CREATED, RECEIPT_CREATED);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_REQ_NUMBER, ORA_REQ_NUMBER);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_ORDER_NO, ORA_ORDER_NO);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_ORDER_STATUS, ORA_ORDER_STATUS);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_SHIPMENT_STATUS, ORA_SHIPMENT_STATUS);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_SHIPMENT_NUMBER, SHIPMENT_NUMBER);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPT_NUMBER, RECEIPT_NUMBER);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_REQUISITION_HEADER_ID, REQUISITION_HEADER_ID);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_REQUISITION_LINE_ID, REQUISITION_LINE_ID);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_VEHICLE_CODE, VEHICLE_CODE);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_GPS_CORDINATES, GPS_CORDINATES);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_REMARKS, REMARKS);
            values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_MAP_URL, MAP_URL);

            delivery deliveryExists = delivery.getDelivery(context, this.getID(), this.getROWID().toString());
            if (deliveryExists == null) {
                db.beginTransaction();
                try {
                    result = db.insert(DeliveryReaderContract.Delivery.TABLE_NAME, null, values);
                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                db.endTransaction();
            } else {
                db.beginTransaction();
                try {
                    result = db.update(DeliveryReaderContract.Delivery.TABLE_NAME,
                            values,
                            DeliveryReaderContract.Delivery.COLUMN_NAME_ID + "=? " + " AND " +
                                    DeliveryReaderContract.Delivery.COLUMN_NAME_ROWID + "=?",
                            new String[]{this.getID().toString(), this.getROWID().toString()}
                    );
                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                db.endTransaction();
            }
        } catch (NullPointerException ex) {
            result = -1;
        } catch (Exception ex) {
            result = -1;
            ex.printStackTrace();
        }
        db.close();
        databaseHelper.close();
        return result;
    }

    private static delivery getDelivery(Context context, Integer id, String rowID) {

        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = db.query(DeliveryReaderContract.Delivery.TABLE_NAME,
                DeliveryReaderContract.COLUMS,
                DeliveryReaderContract.Delivery.COLUMN_NAME_ID + "=? " + " AND " +
                        DeliveryReaderContract.Delivery.COLUMN_NAME_ROWID + "=?",
                new String[]{id.toString(), rowID},
                null,
                null,
                DeliveryReaderContract.Delivery.COLUMN_NAME_INT_STATUS);
        delivery temp = null;
        if (cursor.moveToFirst()) {
            do {
                try {
                    temp = new delivery(
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ID)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ROWID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANS_TYPE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPTID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANSACTIONID)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANSDATE))),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_LINENUM)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ITEMID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DESCRIPTION)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_QUANTITY)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SEIINGPRICE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_COMMENT)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DELIVARYTYPE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ACCOUNTNUM)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_NAME)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_STREET)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ADDRESS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DELIVERY_ADDRESS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_EMIRATE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_GPS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REQ_NUMBER)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_INT_STATUS)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_STATUS))),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_SCHEDULED))),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REMARKS)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_ESTIMATED))),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_LPO_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TIME_SLOT)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SCHEDULED_ASPER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DRIVER_NAME)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_FAILED_REASON)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_CUST_NO)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SITE_NO)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_EMP_ID)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_CREATED_ON)))
                            ,
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_WARRANTY)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPT_CREATED)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_REQ_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_ORDER_NO)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_ORDER_STATUS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_SHIPMENT_STATUS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SHIPMENT_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPT_NUMBER)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REQUISITION_HEADER_ID)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REQUISITION_LINE_ID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_VEHICLE_CODE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_GPS_CORDINATES)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_MAP_URL))
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        db.close();
        dataHelper.close();
        return temp;
    }

    public String getCustomerAddress() {
        return this.ADDRESS;
    }

    public String getCustomerPhone() {
        return this.STREET;
    }

    public String getGPS_CORDINATES() {
        return this.GPS_CORDINATES;
    }

    public static ArrayList<delivery> getList(Context context, String user_name, Integer status) {
        ArrayList<delivery> deliveries = new ArrayList<>();
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = db.query(DeliveryReaderContract.Delivery.TABLE_NAME,
                DeliveryReaderContract.COLUMS,
                DeliveryReaderContract.Delivery.COLUMN_NAME_DRIVER_NAME + "=?" +
                        (status == null ? "" : " AND " +
                                DeliveryReaderContract.Delivery.COLUMN_NAME_INT_STATUS + "=?"
                        )
                ,
                status == null ?
                        new String[]{user_name} :
                        new String[]{user_name, status.toString()}
                , null, null, DeliveryReaderContract.Delivery.COLUMN_NAME_INT_STATUS);
        delivery temp;
        if (cursor.moveToFirst()) {
            do {
                try {
                    temp = new delivery(
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ID)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ROWID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANS_TYPE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPTID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANSACTIONID)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANSDATE))),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_LINENUM)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ITEMID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DESCRIPTION)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_QUANTITY)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SEIINGPRICE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_COMMENT)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DELIVARYTYPE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ACCOUNTNUM)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_NAME)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_STREET)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ADDRESS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DELIVERY_ADDRESS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_EMIRATE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_GPS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REQ_NUMBER)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_INT_STATUS)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_STATUS))),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_SCHEDULED))),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REMARKS)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_ESTIMATED))),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_LPO_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TIME_SLOT)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SCHEDULED_ASPER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DRIVER_NAME)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_FAILED_REASON)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_CUST_NO)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SITE_NO)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_EMP_ID)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_CREATED_ON)))
                            ,
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_WARRANTY)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPT_CREATED)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_REQ_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_ORDER_NO)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_ORDER_STATUS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_SHIPMENT_STATUS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SHIPMENT_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPT_NUMBER)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REQUISITION_HEADER_ID)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REQUISITION_LINE_ID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_VEHICLE_CODE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_GPS_CORDINATES)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_MAP_URL))
                    );
                    deliveries.add(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        db.close();
        dataHelper.close();
        return deliveries;
    }

    public static ArrayList<delivery> getListForReceipt(Context context, String user_name, String receipt) {
        ArrayList<delivery> deliveries = new ArrayList<>();
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = db.query(DeliveryReaderContract.Delivery.TABLE_NAME,
                DeliveryReaderContract.COLUMS,
                DeliveryReaderContract.Delivery.COLUMN_NAME_DRIVER_NAME + "=? AND " +
                        DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPTID + "=?",
                new String[]{user_name, receipt}, null, null, DeliveryReaderContract.Delivery.COLUMN_NAME_INT_STATUS);
        delivery temp;
        if (cursor.moveToFirst()) {
            do {
                try {
                    temp = new delivery(
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ID)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ROWID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANS_TYPE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPTID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANSACTIONID)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TRANSDATE))),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_LINENUM)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ITEMID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DESCRIPTION)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_QUANTITY)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SEIINGPRICE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_COMMENT)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DELIVARYTYPE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ACCOUNTNUM)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_NAME)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_STREET)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ADDRESS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DELIVERY_ADDRESS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_EMIRATE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_GPS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REQ_NUMBER)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_INT_STATUS)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_STATUS))),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_SCHEDULED))),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REMARKS)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DT_ESTIMATED))),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_LPO_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_TIME_SLOT)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SCHEDULED_ASPER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_DRIVER_NAME)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_FAILED_REASON)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_CUST_NO)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SITE_NO)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_EMP_ID)),
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_CREATED_ON)))
                            ,
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_WARRANTY)),
                            cursor.getInt(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPT_CREATED)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_REQ_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_ORDER_NO)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_ORDER_STATUS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_ORA_SHIPMENT_STATUS)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_SHIPMENT_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_RECEIPT_NUMBER)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REQUISITION_HEADER_ID)),
                            cursor.getDouble(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_REQUISITION_LINE_ID)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_VEHICLE_CODE)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_GPS_CORDINATES)),
                            cursor.getString(cursor.getColumnIndex(DeliveryReaderContract.Delivery.COLUMN_NAME_MAP_URL))
                    );
                    deliveries.add(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        db.close();
        dataHelper.close();
        return deliveries;
    }

    public String getCustomerName() {
        return NAME;
    }

    public void setStatus(int delivered) {
        this.Int_STATUS = (double) delivered;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getEmirate() {
        return EMIRATE;
    }

    public Date getScheduledDate() {
        return DT_SCHEDULED;
    }

    public Double getQty() {
        return QUANTITY;
    }

    public Date getReceiptDate() {
        return TRANSDATE;
    }

    public String getReceiptRemark() {
        return REMARKS;
    }

    public void setDriver(String user_name) {
        this.DRIVER_NAME = user_name;
    }

    public String getDriver() {
        return this.DRIVER_NAME;
    }

    public Double getStatus() {
        return this.Int_STATUS;
    }

    public String getTimeSlot() {
        return TIME_SLOT;
    }

    public Integer getID() {
        return id;
    }

    public Integer getROWID() {
        return ROWID;
    }

    public double getLineNumber() {
        return LINENUM;
    }

    public String getReceiptLineIdentifier() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);
        String receiptLineIdentifier = this.getReceipt() + df.format(this.getLineNumber());
        return receiptLineIdentifier;
    }

    public int delete(Context context) {
        int result = -1;
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ID, id);
        values.put(DeliveryReaderContract.Delivery.COLUMN_NAME_ROWID, ROWID);
        try {

            result = db.delete(DeliveryReaderContract.Delivery.TABLE_NAME,
                    DeliveryReaderContract.Delivery.COLUMN_NAME_ID + "=? " + "AND " +
                            DeliveryReaderContract.Delivery.COLUMN_NAME_ROWID + "=? ",
                    new String[]{id.toString(), ROWID.toString()});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
        dataHelper.close();
        return result;
    }

    public String getRemark() {
        return REMARKS;
    }
}