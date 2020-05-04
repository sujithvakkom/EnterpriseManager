package sujithvakkomfreelance.com.components.localdata.contracts;

import android.provider.BaseColumns;

public class DeliveryHeaderContract {
    private DeliveryHeaderContract(){}

    public   static final String SQL_CREATE_DELIVERY_HEADER =
            "CREATE TABLE " + DeliveryHeader.TABLE_NAME + " (" +
                    DeliveryHeader.COLUMN_NAME_HEADER_ID+" INTEGER "+
                    ","+DeliveryHeader.COLUMN_NAME_RECEIPT+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_ORDER_NUMBER+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_CUSTOMER_NAME+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_SALE_DATE+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_PHONE+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_STATUS+" INTEGER "+
                    ","+DeliveryHeader.COLUMN_NAME_SCHEDULED_DATE+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_TIME_SLOT+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_DRIVER_NAME+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_VEHICLE_CODE+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_GPS+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_MAP_URL+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_REMARKS+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_CUSTOMER_ADDRESS+" TEXT "+
                    ","+DeliveryHeader.COLUMN_NAME_EMIRATE+" TEXT "+
                    ")";

    public static final String SQL_DROP_DELIVERY_HEADER =
            "DROP TABLE IF EXISTS " + DeliveryHeader.TABLE_NAME;

    public static final String SQL_GET_SYNCH =
            "SELECT " +
                    DeliveryHeader.COLUMN_NAME_HEADER_ID+
                    ","+DeliveryHeader.COLUMN_NAME_RECEIPT+
                    ","+DeliveryHeader.COLUMN_NAME_ORDER_NUMBER+
                    ","+DeliveryHeader.COLUMN_NAME_CUSTOMER_NAME+
                    ","+DeliveryHeader.COLUMN_NAME_SALE_DATE+
                    ","+DeliveryHeader.COLUMN_NAME_PHONE+
                    ","+DeliveryHeader.COLUMN_NAME_STATUS+
                    ","+DeliveryHeader.COLUMN_NAME_SCHEDULED_DATE+
                    ","+DeliveryHeader.COLUMN_NAME_DRIVER_NAME+
                    ","+DeliveryHeader.COLUMN_NAME_VEHICLE_CODE+
                    ","+DeliveryHeader.COLUMN_NAME_GPS+
                    ","+DeliveryHeader.COLUMN_NAME_MAP_URL+
                    ","+DeliveryHeader.COLUMN_NAME_REMARKS+
                    ","+DeliveryHeader.COLUMN_NAME_CUSTOMER_ADDRESS+
                    ","+DeliveryHeader.COLUMN_NAME_EMIRATE+
                    ","+DeliveryHeader.COLUMN_NAME_TIME_SLOT+
                    " FROM "+ DeliveryHeader.TABLE_NAME;
    public static  final  String[] COLUMS = {
            DeliveryHeader.COLUMN_NAME_ROWID,
            DeliveryHeader.COLUMN_NAME_HEADER_ID,
            DeliveryHeader.COLUMN_NAME_RECEIPT,
            DeliveryHeader.COLUMN_NAME_ORDER_NUMBER,
            DeliveryHeader.COLUMN_NAME_CUSTOMER_NAME,
            DeliveryHeader.COLUMN_NAME_SALE_DATE,
            DeliveryHeader.COLUMN_NAME_PHONE,
            DeliveryHeader.COLUMN_NAME_STATUS,
            DeliveryHeader.COLUMN_NAME_SCHEDULED_DATE,
            DeliveryHeader.COLUMN_NAME_DRIVER_NAME,
            DeliveryHeader.COLUMN_NAME_VEHICLE_CODE,
            DeliveryHeader.COLUMN_NAME_GPS,
            DeliveryHeader.COLUMN_NAME_MAP_URL,
            DeliveryHeader.COLUMN_NAME_REMARKS,
            DeliveryHeader.COLUMN_NAME_CUSTOMER_ADDRESS,
            DeliveryHeader.COLUMN_NAME_EMIRATE,
            DeliveryHeader.COLUMN_NAME_TIME_SLOT
    };

    public static class DeliveryHeader implements BaseColumns {
        public static final String TABLE_NAME = "DeliveryHeader";
        public static final String COLUMN_NAME_ROWID = "rowid";
        public static final String COLUMN_NAME_HEADER_ID = "HeaderId";
        public static final String COLUMN_NAME_RECEIPT = "Receipt";
        public static final String COLUMN_NAME_ORDER_NUMBER = "OrderNumber";
        public static final String COLUMN_NAME_CUSTOMER_NAME = "CustomerName";
        public static final String COLUMN_NAME_SALE_DATE = "SaleDate";
        public static final String COLUMN_NAME_PHONE = "Phone";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_SCHEDULED_DATE = "ScheduledDate";
        public static final String COLUMN_NAME_TIME_SLOT = "TimeSlot";
        public static final String COLUMN_NAME_DRIVER_NAME = "DriverName";
        public static final String COLUMN_NAME_VEHICLE_CODE = "VehicleCode";
        public static final String COLUMN_NAME_GPS = "GPS";
        public static final String COLUMN_NAME_MAP_URL = "MapURL";
        public static final String COLUMN_NAME_REMARKS = "Remarks";
        public static final String COLUMN_NAME_CUSTOMER_ADDRESS = "DeliveryAddress";
        public static final String COLUMN_NAME_EMIRATE = "Emirate";
    }
}