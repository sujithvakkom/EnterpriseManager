package sujithvakkomfreelance.com.components.localdata.contracts;

import android.provider.BaseColumns;

public class DeliveryLineContract {
    private DeliveryLineContract(){}

    public   static final String SQL_CREATE_DELIVERY_LINE =
            "CREATE TABLE " + DeliveryLine.TABLE_NAME + " (" +
                    DeliveryLine.COLUMN_NAME_HEADER_ID+" INTEGER "+
                    ","+ DeliveryLine.COLUMN_NAME_LINE_ID+" INTEGER "+
                    ","+ DeliveryLine.COLUMN_NAME_SALE_DATE+" TEXT "+
                    ","+ DeliveryLine.COLUMN_NAME_LINE_NUMBER+" INTEGER "+
                    ","+ DeliveryLine.COLUMN_NAME_ITEM_CODE+" TEXT "+
                    ","+ DeliveryLine.COLUMN_NAME_DESCRIPTION+" TEXT "+
                    ","+ DeliveryLine.COLUMN_NAME_ORDER_QUANTITY+" REAL "+
                    ","+ DeliveryLine.COLUMN_NAME_SELLING_PRICE+" REAL "+
                    ","+ DeliveryLine.COLUMN_NAME_STATUS+" INTEGER "+
                    ","+ DeliveryLine.COLUMN_NAME_SCHEDULED_DATE+" TEXT "+
                    ","+ DeliveryLine.COLUMN_NAME_TIME_SLOT+" TEXT "+
                    ","+ DeliveryLine.COLUMN_NAME_DRIVER_NAME+" TEXT "+
                    ","+ DeliveryLine.COLUMN_NAME_VEHICLE_CODE+" TEXT "+
                    ","+ DeliveryLine.COLUMN_NAME_GPS+" TEXT "+
                    ","+ DeliveryLine.COLUMN_NAME_MAP_URL+" TEXT "+
                    ","+ DeliveryLine.COLUMN_NAME_REMARKS+" TEXT "+
                    ")";

    public static final String SQL_DROP_DELIVERY_LINE =
            "DROP TABLE IF EXISTS " + DeliveryLine.TABLE_NAME;

    public static final String SQL_GET_DELIVERY_LINE =
            "SELECT " +
                    DeliveryLine.COLUMN_NAME_HEADER_ID+
                    ","+ DeliveryLine.COLUMN_NAME_LINE_ID+
                    ","+ DeliveryLine.COLUMN_NAME_SALE_DATE+
                    ","+ DeliveryLine.COLUMN_NAME_LINE_NUMBER+
                    ","+ DeliveryLine.COLUMN_NAME_ITEM_CODE+
                    ","+ DeliveryLine.COLUMN_NAME_DESCRIPTION+
                    ","+ DeliveryLine.COLUMN_NAME_ORDER_QUANTITY+
                    ","+ DeliveryLine.COLUMN_NAME_SELLING_PRICE+
                    ","+ DeliveryLine.COLUMN_NAME_STATUS+
                    ","+ DeliveryLine.COLUMN_NAME_SCHEDULED_DATE+
                    ","+ DeliveryLine.COLUMN_NAME_DRIVER_NAME+
                    ","+ DeliveryLine.COLUMN_NAME_VEHICLE_CODE+
                    ","+ DeliveryLine.COLUMN_NAME_GPS+
                    ","+ DeliveryLine.COLUMN_NAME_MAP_URL+
                    ","+ DeliveryLine.COLUMN_NAME_REMARKS+
                    " FROM "+ DeliveryLine.TABLE_NAME;
    public static  final  String[] COLUMS = {
            DeliveryLine.COLUMN_NAME_ROWID,
            DeliveryLine.COLUMN_NAME_HEADER_ID,
            DeliveryLine.COLUMN_NAME_LINE_ID,
            DeliveryLine.COLUMN_NAME_SALE_DATE,
            DeliveryLine.COLUMN_NAME_LINE_NUMBER,
            DeliveryLine.COLUMN_NAME_ITEM_CODE,
            DeliveryLine.COLUMN_NAME_DESCRIPTION,
            DeliveryLine.COLUMN_NAME_ORDER_QUANTITY,
            DeliveryLine.COLUMN_NAME_SELLING_PRICE,
            DeliveryLine.COLUMN_NAME_STATUS,
            DeliveryLine.COLUMN_NAME_SCHEDULED_DATE,
            DeliveryLine.COLUMN_NAME_DRIVER_NAME,
            DeliveryLine.COLUMN_NAME_VEHICLE_CODE,
            DeliveryLine.COLUMN_NAME_GPS,
            DeliveryLine.COLUMN_NAME_MAP_URL,
            DeliveryLine.COLUMN_NAME_REMARKS
    };

    public static class DeliveryLine implements BaseColumns {
        public static final String TABLE_NAME = "DeliveryLine";
        public static final String COLUMN_NAME_ROWID = "rowid";
        public static final String COLUMN_NAME_HEADER_ID = "HeaderId";
        public static final String COLUMN_NAME_LINE_ID = "LineId";
        public static final String COLUMN_NAME_SALE_DATE = "SaleDate";
        public static final String COLUMN_NAME_LINE_NUMBER = "LineNumber";
        public static final String COLUMN_NAME_ITEM_CODE = "ItemCode";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_ORDER_QUANTITY = "OrderQuantity";
        public static final String COLUMN_NAME_SELLING_PRICE = "SelleingPrice";
        public static final String COLUMN_NAME_STATUS = "Status";
        public static final String COLUMN_NAME_SCHEDULED_DATE = "ScheduledDate";
        public static final String COLUMN_NAME_TIME_SLOT = "TimeSlot";
        public static final String COLUMN_NAME_DRIVER_NAME = "DriverName";
        public static final String COLUMN_NAME_VEHICLE_CODE = "VehicleCode";
        public static final String COLUMN_NAME_GPS = "GPS";
        public static final String COLUMN_NAME_MAP_URL = "MapURL";
        public static final String COLUMN_NAME_REMARKS = "Remarks";


        ;
    }
}