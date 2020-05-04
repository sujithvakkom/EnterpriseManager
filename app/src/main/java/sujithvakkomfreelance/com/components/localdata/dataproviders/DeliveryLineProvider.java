package sujithvakkomfreelance.com.components.localdata.dataproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.localdata.LocalDataHelper;
import sujithvakkomfreelance.com.components.localdata.contracts.DeliveryHeaderContract;
import sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryLine;

import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_DESCRIPTION;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_DRIVER_NAME;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_GPS;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_HEADER_ID;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_ITEM_CODE;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_LINE_ID;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_LINE_NUMBER;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_MAP_URL;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_ORDER_QUANTITY;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_REMARKS;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_SALE_DATE;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_SCHEDULED_DATE;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_SELLING_PRICE;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_STATUS;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_TIME_SLOT;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.COLUMN_NAME_VEHICLE_CODE;
import static sujithvakkomfreelance.com.components.localdata.contracts.DeliveryLineContract.DeliveryLine.TABLE_NAME;

public class DeliveryLineProvider  {
    private  static  final String TAG = "DeliveryLineProvider";
    public long save(Context context, DeliveryLine line, Integer headerId) {
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        db.beginTransaction();
        long result =0;
        try {
            result = save(db, line, headerId);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
            result = 0;
        }
        db.endTransaction();
        db.close();
        dataHelper.close();
        return result;
    }

    public long save(SQLiteDatabase db, DeliveryLine line, Integer headerId) throws Exception {
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_VEHICLE_CODE, line.VehicleCode);
            values.put(COLUMN_NAME_TIME_SLOT, line.TimeSlot);
            values.put(COLUMN_NAME_STATUS, line.Status);
            if (line.ScheduledDate != null)
                values.put(COLUMN_NAME_SCHEDULED_DATE,
                        AppLiterals.APPLICATION_DATE_FORMAT.format(line.ScheduledDate));
            if (line.SaleDate != null)
                values.put(COLUMN_NAME_SALE_DATE,
                        AppLiterals.APPLICATION_DATE_FORMAT.format(line.SaleDate));
            values.put(COLUMN_NAME_REMARKS, line.Remarks);
            values.put(COLUMN_NAME_MAP_URL, line.MapURL);
            values.put(COLUMN_NAME_HEADER_ID, headerId);
            values.put(COLUMN_NAME_GPS, line.GPS);
            values.put(COLUMN_NAME_DRIVER_NAME, line.DriverName);
            values.put(COLUMN_NAME_DESCRIPTION, line.Description);
            values.put(COLUMN_NAME_ITEM_CODE, line.ItemCode);
            values.put(COLUMN_NAME_LINE_ID, line.LineId);
            values.put(COLUMN_NAME_LINE_NUMBER, line.LineNumber);
            values.put(COLUMN_NAME_ORDER_QUANTITY, line.OrderQuantity);
            values.put(COLUMN_NAME_SELLING_PRICE, line.SelleingPrice);
            if (this.exists(db, line, headerId)) {
                Log.i(TAG,"Line found updating.");
                return db.update(TABLE_NAME,
                        values,
                        COLUMN_NAME_LINE_ID + "= ? AND " +
                                COLUMN_NAME_HEADER_ID + "= ? ",
                        new String[]{line.LineId.toString(), headerId.toString()}
                );
            }
            else {
                Log.i(TAG,"New line inserting.");
                return db.insert(TABLE_NAME, null, values);
            }
        } catch (Exception ex) {
            Log.i(TAG,"Error Saving.");
            ex.printStackTrace();
            throw ex;
        }
    }

    private boolean exists(SQLiteDatabase db, DeliveryLine line, Integer headerId) {
        return this.getDeliveryLine(db,line.LineId,headerId)!=null;
    }

    private DeliveryLine getDeliveryLine(SQLiteDatabase db, Integer lineId, Integer headerId) {

        Cursor cursor = db.query(DeliveryLineContract.DeliveryLine.TABLE_NAME,
                DeliveryLineContract.COLUMS,
                DeliveryLineContract.DeliveryLine.COLUMN_NAME_HEADER_ID + "=? AND " +
                        DeliveryLineContract.DeliveryLine.COLUMN_NAME_LINE_ID + "=?"
                , new String[]{headerId.toString(), lineId.toString()}
                , null, null, DeliveryLineContract.DeliveryLine.COLUMN_NAME_STATUS);

        DeliveryLine temp;
        if (cursor.moveToFirst()) {
            try {
                temp = new DeliveryLine();
                try {
                    temp.SaleDate =
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                    getString(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_SALE_DATE)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    temp.ScheduledDate =
                            AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                    getString(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_SCHEDULED_DATE)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    temp.Description = cursor.
                            getString(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_DESCRIPTION));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    temp.ItemCode = cursor.
                            getString(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_ITEM_CODE));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    temp.LineId = cursor.
                            getInt(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_LINE_ID));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    temp.LineNumber = cursor.
                            getInt(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_LINE_NUMBER));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    temp.OrderQuantity = cursor.
                            getDouble(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_ORDER_QUANTITY));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    temp.SelleingPrice = cursor.
                            getDouble(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_SELLING_PRICE));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    temp.Status = cursor.
                            getInt(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_STATUS));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return temp;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }finally {
                cursor.close();
            }
        }else
        return null;
    }

    public ArrayList<DeliveryLine> getList(Context context, Integer headerId, Integer status) {
        ArrayList<DeliveryLine> deliveryLines = new ArrayList<>();
        LocalDataHelper dataHelper = new LocalDataHelper(context);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = db.query(DeliveryLineContract.DeliveryLine.TABLE_NAME,
                DeliveryLineContract.COLUMS,
                DeliveryLineContract.DeliveryLine.COLUMN_NAME_HEADER_ID + "=?" +
                        (status == null ? "" : " AND " +
                                DeliveryLineContract.DeliveryLine.COLUMN_NAME_STATUS + "=?"
                        )
                ,
                status == null ?
                        new String[]{headerId.toString()} :
                        new String[]{headerId.toString(), status.toString()}
                , null, null, DeliveryLineContract.DeliveryLine.COLUMN_NAME_STATUS);

        DeliveryLine temp;
        try {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        temp = new DeliveryLine();
                        try {
                            temp.SaleDate =
                                    AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                            getString(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_SALE_DATE)));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.ScheduledDate =
                                    AppLiterals.APPLICATION_DATE_FORMAT.parse(cursor.
                                            getString(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_SCHEDULED_DATE)));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.Description = cursor.
                                    getString(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_DESCRIPTION));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.ItemCode = cursor.
                                    getString(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_ITEM_CODE));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.LineId = cursor.
                                    getInt(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_LINE_ID));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.LineNumber = cursor.
                                    getInt(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_LINE_NUMBER));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.OrderQuantity = cursor.
                                    getDouble(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_ORDER_QUANTITY));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.SelleingPrice = cursor.
                                    getDouble(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_SELLING_PRICE));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            temp.Status = cursor.
                                    getInt(cursor.getColumnIndex(DeliveryLineContract.DeliveryLine.COLUMN_NAME_STATUS));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        deliveryLines.add(temp);
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
        return deliveryLines;
    }
}
