package sujithvakkomfreelance.com.components.localdata.contracts;

import android.provider.BaseColumns;

public class DeliveryReaderContract {
    private DeliveryReaderContract(){}
    public   static final String SQL_CREATE_DELIVERY =
            "CREATE TABLE " + Delivery.TABLE_NAME + " (" +
                    Delivery.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Delivery.COLUMN_NAME_ROWID + " TEXT," +
                    Delivery.COLUMN_NAME_TRANS_TYPE + " TEXT," +
                    Delivery.COLUMN_NAME_RECEIPTID + " TEXT," +
                    Delivery.COLUMN_NAME_TRANSACTIONID + " TEXT," +
                    Delivery.COLUMN_NAME_TRANSDATE + " TEXT," +
                    Delivery.COLUMN_NAME_LINENUM + " TEXT," +
                    Delivery.COLUMN_NAME_ITEMID + " TEXT," +
                    Delivery.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    Delivery.COLUMN_NAME_QUANTITY + " INTEGER," +
                    Delivery.COLUMN_NAME_SEIINGPRICE + " REAL," +
                    Delivery.COLUMN_NAME_COMMENT + " TEXT," +
                    Delivery.COLUMN_NAME_DELIVARYTYPE + " TEXT," +
                    Delivery.COLUMN_NAME_ACCOUNTNUM + " TEXT," +
                    Delivery.COLUMN_NAME_NAME + " TEXT," +
                    Delivery.COLUMN_NAME_STREET + " TEXT," +
                    Delivery.COLUMN_NAME_ADDRESS + " TEXT," +
                    Delivery.COLUMN_NAME_DELIVERY_ADDRESS + " TEXT," +
                    Delivery.COLUMN_NAME_EMIRATE + " TEXT," +
                    Delivery.COLUMN_NAME_GPS + " TEXT," +
                    Delivery.COLUMN_NAME_REQ_NUMBER + " TEXT," +
                    Delivery.COLUMN_NAME_INT_STATUS + " INTEGER," +
                    Delivery.COLUMN_NAME_DT_STATUS + " INTEGER," +
                    Delivery.COLUMN_NAME_DT_SCHEDULED + " TEXT," +
                    Delivery.COLUMN_NAME_REMARKS + " TEXT," +
                    Delivery.COLUMN_NAME_DT_ESTIMATED + " TEXT," +
                    Delivery.COLUMN_NAME_LPO_NUMBER + " TEXT," +
                    Delivery.COLUMN_NAME_TIME_SLOT + " TEXT," +
                    Delivery.COLUMN_NAME_SCHEDULED_ASPER + " TEXT," +
                    Delivery.COLUMN_NAME_DRIVER_NAME + " TEXT," +
                    Delivery.COLUMN_NAME_FAILED_REASON + " TEXT," +
                    Delivery.COLUMN_NAME_CUST_NO + " TEXT," +
                    Delivery.COLUMN_NAME_SITE_NO + " TEXT," +
                    Delivery.COLUMN_NAME_EMP_ID + " TEXT," +
                    Delivery.COLUMN_NAME_CREATED_ON+ " TEXT," +
                    Delivery.COLUMN_NAME_WARRANTY + " TEXT," +
                    Delivery.COLUMN_NAME_RECEIPT_CREATED + " INTEGER," +
                    Delivery.COLUMN_NAME_ORA_REQ_NUMBER + " TEXT," +
                    Delivery.COLUMN_NAME_ORA_ORDER_NO + " TEXT," +
                    Delivery.COLUMN_NAME_ORA_ORDER_STATUS + " TEXT," +
                    Delivery.COLUMN_NAME_ORA_SHIPMENT_STATUS + " TEXT," +
                    Delivery.COLUMN_NAME_SHIPMENT_NUMBER + " TEXT," +
                    Delivery.COLUMN_NAME_RECEIPT_NUMBER + " TEXT," +
                    Delivery.COLUMN_NAME_REQUISITION_HEADER_ID + " REAL," +
                    Delivery.COLUMN_NAME_REQUISITION_LINE_ID + " REAL," +
                    Delivery.COLUMN_NAME_VEHICLE_CODE + " TEXT," +
                    Delivery.COLUMN_NAME_GPS_CORDINATES + " TEXT," +
                    Delivery.COLUMN_NAME_MAP_URL + " TEXT" +
                    ")" ;
    public static final String SQL_DROP_DELIVERY =
            "DROP TABLE IF EXISTS " + Delivery.TABLE_NAME;
    public static final String SQL_INDEX_JOB =
            "CREATE UNIQUE INDEX IF NOT EXISTS " +
                    Delivery.TABLE_INDEX+
                    " ON "+
                    Delivery.TABLE_NAME+
                    " ( "+
                    Delivery.COLUMN_NAME_RECEIPTID+
                    ", "+
                    Delivery.COLUMN_NAME_LINENUM+
                    ")";
    public static final String SQL_SELECT=
            "SELECT "+ Delivery.COLUMN_NAME_ID+","+
                    Delivery.COLUMN_NAME_ROWID+","+
                    Delivery.COLUMN_NAME_TRANS_TYPE+","+
                    Delivery.COLUMN_NAME_RECEIPTID+","+
                    Delivery.COLUMN_NAME_TRANSACTIONID+","+
                    Delivery.COLUMN_NAME_TRANSDATE+","+
                    Delivery.COLUMN_NAME_LINENUM+","+
                    Delivery.COLUMN_NAME_ITEMID+","+
                    Delivery.COLUMN_NAME_DESCRIPTION+","+
                    Delivery.COLUMN_NAME_QUANTITY+","+
                    Delivery.COLUMN_NAME_SEIINGPRICE+","+
                    Delivery.COLUMN_NAME_COMMENT+","+
                    Delivery.COLUMN_NAME_DELIVARYTYPE+","+
                    Delivery.COLUMN_NAME_ACCOUNTNUM+","+
                    Delivery.COLUMN_NAME_NAME+","+
                    Delivery.COLUMN_NAME_STREET+","+
                    Delivery.COLUMN_NAME_ADDRESS+","+
                    Delivery.COLUMN_NAME_DELIVERY_ADDRESS+","+
                    Delivery.COLUMN_NAME_EMIRATE+","+
                    Delivery.COLUMN_NAME_GPS+","+
                    Delivery.COLUMN_NAME_REQ_NUMBER+","+
                    Delivery.COLUMN_NAME_INT_STATUS+","+
                    Delivery.COLUMN_NAME_DT_STATUS+","+
                    Delivery.COLUMN_NAME_DT_SCHEDULED+","+
                    Delivery.COLUMN_NAME_REMARKS+","+
                    Delivery.COLUMN_NAME_DT_ESTIMATED+","+
                    Delivery.COLUMN_NAME_LPO_NUMBER+","+
                    Delivery.COLUMN_NAME_TIME_SLOT+","+
                    Delivery.COLUMN_NAME_SCHEDULED_ASPER+","+
                    Delivery.COLUMN_NAME_DRIVER_NAME+","+
                    Delivery.COLUMN_NAME_FAILED_REASON+","+
                    Delivery.COLUMN_NAME_CUST_NO+","+
                    Delivery.COLUMN_NAME_SITE_NO+","+
                    Delivery.COLUMN_NAME_EMP_ID+","+
                    Delivery.COLUMN_NAME_CREATED_ON+","+ Delivery.COLUMN_NAME_WARRANTY+","+
                    Delivery.COLUMN_NAME_RECEIPT_CREATED+","+
                    Delivery.COLUMN_NAME_ORA_REQ_NUMBER+","+
                    Delivery.COLUMN_NAME_ORA_ORDER_NO+","+
                    Delivery.COLUMN_NAME_ORA_ORDER_STATUS+","+
                    Delivery.COLUMN_NAME_ORA_SHIPMENT_STATUS+","+
                    Delivery.COLUMN_NAME_SHIPMENT_NUMBER+","+
                    Delivery.COLUMN_NAME_RECEIPT_NUMBER+","+
                    Delivery.COLUMN_NAME_REQUISITION_HEADER_ID+","+
                    Delivery.COLUMN_NAME_REQUISITION_LINE_ID+","+
                    Delivery.COLUMN_NAME_VEHICLE_CODE+","+
                    Delivery.COLUMN_NAME_GPS_CORDINATES+","+
                    Delivery.COLUMN_NAME_MAP_URL+
                    " FROM "+ Delivery.TABLE_NAME;
    public static  final  String[] COLUMS = {Delivery.COLUMN_NAME_ID,
            Delivery.COLUMN_NAME_ROWID,
            Delivery.COLUMN_NAME_TRANS_TYPE,
            Delivery.COLUMN_NAME_RECEIPTID,
            Delivery.COLUMN_NAME_TRANSACTIONID,
            Delivery.COLUMN_NAME_TRANSDATE,
            Delivery.COLUMN_NAME_LINENUM,
            Delivery.COLUMN_NAME_ITEMID,
            Delivery.COLUMN_NAME_DESCRIPTION,
            Delivery.COLUMN_NAME_QUANTITY,
            Delivery.COLUMN_NAME_SEIINGPRICE,
            Delivery.COLUMN_NAME_COMMENT,
            Delivery.COLUMN_NAME_DELIVARYTYPE,
            Delivery.COLUMN_NAME_ACCOUNTNUM,
            Delivery.COLUMN_NAME_NAME,
            Delivery.COLUMN_NAME_STREET,
            Delivery.COLUMN_NAME_ADDRESS,
            Delivery.COLUMN_NAME_DELIVERY_ADDRESS,
            Delivery.COLUMN_NAME_EMIRATE,
            Delivery.COLUMN_NAME_GPS,
            Delivery.COLUMN_NAME_REQ_NUMBER,
            Delivery.COLUMN_NAME_INT_STATUS,
            Delivery.COLUMN_NAME_DT_STATUS,
            Delivery.COLUMN_NAME_DT_SCHEDULED,
            Delivery.COLUMN_NAME_REMARKS,
            Delivery.COLUMN_NAME_DT_ESTIMATED,
            Delivery.COLUMN_NAME_LPO_NUMBER,
            Delivery.COLUMN_NAME_TIME_SLOT,
            Delivery.COLUMN_NAME_SCHEDULED_ASPER,
            Delivery.COLUMN_NAME_DRIVER_NAME,
            Delivery.COLUMN_NAME_FAILED_REASON,
            Delivery.COLUMN_NAME_CUST_NO,
            Delivery.COLUMN_NAME_SITE_NO,
            Delivery.COLUMN_NAME_EMP_ID,
            Delivery.COLUMN_NAME_CREATED_ON,
            Delivery.COLUMN_NAME_WARRANTY,
            Delivery.COLUMN_NAME_RECEIPT_CREATED,
            Delivery.COLUMN_NAME_ORA_REQ_NUMBER,
            Delivery.COLUMN_NAME_ORA_ORDER_NO,
            Delivery.COLUMN_NAME_ORA_ORDER_STATUS,
            Delivery.COLUMN_NAME_ORA_SHIPMENT_STATUS,
            Delivery.COLUMN_NAME_SHIPMENT_NUMBER,
            Delivery.COLUMN_NAME_RECEIPT_NUMBER,
            Delivery.COLUMN_NAME_REQUISITION_HEADER_ID,
            Delivery.COLUMN_NAME_REQUISITION_LINE_ID,
            Delivery.COLUMN_NAME_VEHICLE_CODE,
            Delivery.COLUMN_NAME_GPS_CORDINATES,
            Delivery.COLUMN_NAME_MAP_URL
    };

    public static class Delivery implements BaseColumns {
        public static final String TABLE_INDEX = "DELIVERY_INDEX_JOB";
        public static final String TABLE_NAME = "delivery";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_ROWID = "ROWID";
        public static final String COLUMN_NAME_TRANS_TYPE = "TRANS_TYPE";
        public static final String COLUMN_NAME_RECEIPTID = "RECEIPTID";
        public static final String COLUMN_NAME_TRANSACTIONID = "TRANSACTIONID";
        public static final String COLUMN_NAME_TRANSDATE = "TRANSDATE";
        public static final String COLUMN_NAME_LINENUM = "LINENUM";
        public static final String COLUMN_NAME_ITEMID = "ITEMID";
        public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
        public static final String COLUMN_NAME_QUANTITY = "QUANTITY";
        public static final String COLUMN_NAME_SEIINGPRICE = "SEIINGPRICE";
        public static final String COLUMN_NAME_COMMENT = "COMMENT";
        public static final String COLUMN_NAME_DELIVARYTYPE = "DELIVARYTYPE";
        public static final String COLUMN_NAME_ACCOUNTNUM = "ACCOUNTNUM";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_STREET = "STREET";
        public static final String COLUMN_NAME_ADDRESS = "ADDRESS";
        public static final String COLUMN_NAME_DELIVERY_ADDRESS = "DELIVERY_ADDRESS";
        public static final String COLUMN_NAME_EMIRATE = "EMIRATE";
        public static final String COLUMN_NAME_GPS = "GPS";
        public static final String COLUMN_NAME_REQ_NUMBER = "REQ_NUMBER";
        public static final String COLUMN_NAME_INT_STATUS = "INT_STATUS";
        public static final String COLUMN_NAME_DT_STATUS = "DT_STATUS";
        public static final String COLUMN_NAME_DT_SCHEDULED = "DT_SCHEDULED";
        public static final String COLUMN_NAME_REMARKS = "REMARKS";
        public static final String COLUMN_NAME_DT_ESTIMATED = "DT_ESTIMATED";
        public static final String COLUMN_NAME_LPO_NUMBER = "LPO_NUMBER";
        public static final String COLUMN_NAME_TIME_SLOT = "TIME_SLOT";
        public static final String COLUMN_NAME_SCHEDULED_ASPER = "SCHEDULED_ASPER";
        public static final String COLUMN_NAME_DRIVER_NAME = "DRIVER_NAME";
        public static final String COLUMN_NAME_FAILED_REASON = "FAILED_REASON";
        public static final String COLUMN_NAME_CUST_NO = "CUST_NO";
        public static final String COLUMN_NAME_SITE_NO = "SITE_NO";
        public static final String COLUMN_NAME_EMP_ID = "EMP_ID";
        public static final String COLUMN_NAME_CREATED_ON = "CREATED_ON";
        public static final String COLUMN_NAME_WARRANTY = "WARRANTY";
        public static final String COLUMN_NAME_RECEIPT_CREATED = "RECEIPT_CREATED";
        public static final String COLUMN_NAME_ORA_REQ_NUMBER = "ORA_REQ_NUMBER";
        public static final String COLUMN_NAME_ORA_ORDER_NO = "ORA_ORDER_NO";
        public static final String COLUMN_NAME_ORA_ORDER_STATUS = "ORA_ORDER_STATUS";
        public static final String COLUMN_NAME_ORA_SHIPMENT_STATUS = "ORA_SHIPMENT_STATUS";
        public static final String COLUMN_NAME_SHIPMENT_NUMBER = "SHIPMENT_NUMBER";
        public static final String COLUMN_NAME_RECEIPT_NUMBER = "RECEIPT_NUMBER";
        public static final String COLUMN_NAME_REQUISITION_HEADER_ID = "REQUISITION_HEADER_ID";
        public static final String COLUMN_NAME_REQUISITION_LINE_ID = "REQUISITION_LINE_ID";
        public static final String COLUMN_NAME_VEHICLE_CODE = "VEHICLE_CODE";
        public static final String COLUMN_NAME_GPS_CORDINATES = "GPS_CORDINATES";
        public static final String COLUMN_NAME_MAP_URL = "MAP_URL";
    }

}
