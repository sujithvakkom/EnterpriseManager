package sujithvakkomfreelance.com.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sujithvakkomfreelance.com.components.localdata.dataproviders.delivery;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;

public class AppLiterals {
    public static final int REQUEST_READ_INTERNET= 1000;
    public static final int REQUEST_FORGROUND_SERVICE= 1001;
    public static final int REQUEST_USE_BIOMETRIC= 1002;
    public static final int REQUEST_READ_CONTACTS = 0;

    /***
     * UserDetails
     */
    public static final String USER_DETAIL = "UserDetails";
    public static final String DRIVER = "driver";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_FORMAT_MONTH_YEAR = "MMM yyyy";
    private static final String DATE_FORMAT_DAY = "dd";
    private static final String DATE_FORMAT_SMALL = "yyyy-MM-dd";
    private static final String DATE_FORMAT_SMALL_VIEW = "dd-MMM-yyyy";
    private static final Locale LOCALE_EN = new Locale.Builder().setLanguage("en").setRegion("US").build();

    /***
     * "yyyy-MM-dd'T'HH:mm:ss"
     * Used to save date in DB and broadcast over JSON
     */
    public static final DateFormat APPLICATION_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT,LOCALE_EN);

    /***
     * "yyyy-MM-dd"
     * Save small date format
     */
    public static final DateFormat APPLICATION_DATE_SMALL_FORMAT = new SimpleDateFormat(DATE_FORMAT_SMALL,LOCALE_EN);

    /***
     * "MMM yyyy"
     * Display month year format
     */
    public static final DateFormat APPLICATION_DATE_MONTH_YEAR_FORMAT = new SimpleDateFormat(DATE_FORMAT_MONTH_YEAR,LOCALE_EN);

    /***
     * "dd"
     * Display date number format
     */
    public static final DateFormat APPLICATION_DATE_DAY_FORMAT = new SimpleDateFormat(DATE_FORMAT_DAY,LOCALE_EN);

    /***
     * "dd-MMM-yyyy"
     * Display date format
     */
    public static final DateFormat APPLICATION_DATE_SMALL_VIEW_FORMAT = new SimpleDateFormat(DATE_FORMAT_SMALL_VIEW,LOCALE_EN);

    public static  final int JOB_ACCEPTANCE=1002;
    public static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    /***
     * Application GSON Builder
     */
    public static final Gson APPLICATION_GSON_BUILDER = new GsonBuilder()
            .setDateFormat(AppLiterals.DATE_FORMAT)
            .create();

    public static final String BARCODE = "barcode";
    public static final String PROFILE_ROOT = "root";
    public static final String PROFILE_ROLE = "role";
    public static final int BARCODE_REQUEST_CODE = 1001;
    public static  final int CUSTOMER_SURVEY=1003;
    public static final int DELIVERY_ACTIONS = 1004;
    public static final String YES = "YES";
    public static final String YES_OR_NO = "YES_OR_NO";
    public static final String NO = "NO";
    public static final String LOGIN_TOKEN = "LOGIN_TOKEN";
    public static final int RESULT_OK = -1;
    public static final String STATUS = "status";

    /*
    mapbox:mapbox_styleUrl="https://tile.jawg.io/jawg-streets.json?access-token=jwpq5TaBN0WkRlPJAY7jOU4gtjzrVPdT3hFMdVJVz8Zs6o7Wf5Iex3wo06tBYXwy"
    mapbox:mapbox_uiLogo="false"
    mapbox:mapbox_uiAttribution="false"
    * */
    public static final String JAWG_API_KEY = "jwpq5TaBN0WkRlPJAY7jOU4gtjzrVPdT3hFMdVJVz8Zs6o7Wf5Iex3wo06tBYXwy";

    public static final String CHANNEL_ID = "gstores.merchandiser2.channel";
    public static final CharSequence CHANNEL_NAME = "Merchandiser Notification";
    public static final CharSequence NOTIFICATION_TITLE = "GS Merchandiser";
    public static final String MESSAGE = "MESSAGE";


    public static final String DELIVERY_CLASS_NAME = delivery.class.getName();

    public static final String DELIVERY_HEADER_CLASS_NAME = DeliveryHeader.class.getName();

    public class INSTANCE_STATE_KEYS{
        public static final String DELIVERY_DETAILS = "DELIVERY_DETAILS";
    }

    public class DIALOG_TAG{
        public static final String SELECT_ITEM = "SELECT_ITEM";
    }

    /**
     * Delivery Status
     */
    public static final  class DELIVERY_STATUS{
        /**
         * 8
         */
        public static final int READY = 8;
        /**
         * 9
         */
        public static  final int DISPATCHED=9;
        /**
         * 11
         */
        public static  final int DELIVERED=11;
        /**
         * 16
         */
        public static  final int REJECTED=16;
        /**
         * 14
         */
        public static  final int RESCHEDULED=14;



        //Biometric uniq key pair
        public static final String KEY_NAME = "enterprisemanager_biokey";
        public static final Integer POSTED = 999;
    }
}
