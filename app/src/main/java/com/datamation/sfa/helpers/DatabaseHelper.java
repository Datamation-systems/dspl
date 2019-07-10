package com.datamation.sfa.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // database information
    public static final String DATABASE_NAME = "sfa_database.db";
    public static final int DATABASE_VERSION = 5;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * ############################ server table Details
     * #################################
     */
    // table
    public static final String TABLE_SERVER_DB = "serverdb";
    // table attributes
    public static final String SERVER_DB_ID = "server_db_id";
    public static final String SERVER_DB_NAME = "server_db_name";

    //common string

    public static final String REFNO = "RefNo";
    public static final String TXNDATE = "TxnDate";

    private static final String CREATE_SERVER_DB_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SERVER_DB + " (" + SERVER_DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SERVER_DB_NAME + " TEXT); ";
    public static final String TABLE_CUSTOMER = "Customer";
    // table attributes
    public static final String CUSTOMER_CODE = "CustomerCode";
    public static final String CUSTOMER_NAME = "CustomerName";
    public static final String CUSTOMER_ADD1 = "CustomerAdd1";
    public static final String CUSTOMER_ADD2 = "CustomerAdd2";
    public static final String CUSTOMER_STATUS = "CustomerStatus";//0-active , 1- new
    public static final String CUSTOMER_ROUTE = "CustomerRoute";
    public static final String CUSTOMER_MOB = "CustomerMob";
    public static final String CUSTOMER_EMAIL = "CustomerEmail";

    // table

    // create String
    private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMER + " (" + CUSTOMER_CODE + " TEXT, "
            + CUSTOMER_NAME +
            " TEXT, " + CUSTOMER_ADD1 + " TEXT, " + CUSTOMER_ADD2 + " TEXT, " + CUSTOMER_STATUS + " TEXT, " +
            CUSTOMER_ROUTE + " TEXT, "+ CUSTOMER_MOB + " TEXT, " + CUSTOMER_EMAIL + " TEXT); ";


    /**
     * ############################ ReferenceSetting table Details
     * ################################
     */

    // table
    public static final String TABLE_REFERENCE_SETTING = "ReferenceSetting";
    // table attributes
    public static final String REFSETTING_ID = "Setting_id";// ok
    public static final String REFSETTING_SETTINGS_CODE = "SettingsCode";// ok
    public static final String REFSETTING_CHAR_VAL = "CharVal";// ok
    public static final String REFSETTING_REMARKS = "Remarks";// ok

    // create String
    private static final String CREATE_REFSETTING_TABLE = "CREATE  TABLE IF NOT EXISTS " +
            TABLE_REFERENCE_SETTING + " (" + REFSETTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            REFSETTING_SETTINGS_CODE + " TEXT, " +
            REFSETTING_CHAR_VAL +  " TEXT, " +
            REFSETTING_REMARKS + " TEXT); ";

    // table
    public static final String TABLE_REFERENCE = "Reference";
    // table attributes
    public static final String REFERENCE_REPCODE = "RepCode";
    public static final String REFERENCE_RECORD_ID = "RecordId";
    public static final String REFERENCE_SETTINGS_CODE = "SettingsCode";
    public static final String REFERENCE_NNUM_VAL = "nNumVal";
    public static final String REFERENCE_NYEAR_VAL = "nYear";
    public static final String REFERENCE_NMONTH_VAL = "nMonth";

    // create String
    private static final String CREATE_REFERENCE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_REFERENCE + " (" + REFERENCE_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFERENCE_REPCODE +  " TEXT, " + REFERENCE_SETTINGS_CODE + " TEXT, " + REFERENCE_NNUM_VAL + " TEXT, " + REFERENCE_NYEAR_VAL + " TEXT, " + REFERENCE_NMONTH_VAL + " TEXT); ";

    // table
    public static final String TABLE_ROUTE = "Route";
    public static final String ROUTE_ID = "RouteID";
    // table attributes

    public static final String ROUTE_NAME = "RouteName";
    public static final String ROUTE_CODE = "RouteCode";

    // create String
    private static final String CREATE_ROUTE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_ROUTE + " (" + ROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ROUTE_CODE + " TEXT, " + ROUTE_NAME + " TEXT) ";

    public static final String TABLE_NONPRDHED = "DaynPrdHed";
    // table attributes
    public static final String NONPRDHED_ID = "NonprdHed_id";

    public static final String NONPRDHED_REPCODE = "RepCode";

    public static final String NONPRDHED_REMARK = "Remarks";
    public static final String NONPRDHED_ADDDATE = "AddDate";
    public static final String NONPRDHED_IS_SYNCED = "ISsync";
    public static final String NONPRDHED_DEBCODE = "DebCode";
    public static final String NONPRDHED_LONGITUDE = "Longitude";
    public static final String NONPRDHED_LATITUDE = "Latitude";
    public static final String NONPRDHED_IS_ACTIVE = "ISActive";
    // create String
    private static final String CREATE_TABLE_NONPRDHED = "CREATE  TABLE IF NOT EXISTS " + TABLE_NONPRDHED +
            " (" + NONPRDHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO
            + " TEXT, " + TXNDATE + " TEXT, "  + NONPRDHED_REPCODE + " TEXT, " + NONPRDHED_REMARK +
            " TEXT, "  + NONPRDHED_ADDDATE + " TEXT,"  + NONPRDHED_IS_SYNCED + " TEXT," + NONPRDHED_DEBCODE +
            " TEXT," + NONPRDHED_LATITUDE + " TEXT," + NONPRDHED_LONGITUDE + " TEXT,"  + NONPRDHED_IS_ACTIVE + " TEXT); ";
    /**
     * ############################ FDaynonprdDet
     * ################################
     */
    public static final String TABLE_NONPRDDET = "DaynPrdDet";
    // table attributes

    public static final String NONPRDDET_ID = "NonprdDet_id";

    public static final String NONPRDDET_REASON = "Reason";
    public static final String NONPRDDET_REASON_CODE = "ReasonCode";



    private static final String CREATE_TABLE_NONPRDDET = "CREATE  TABLE IF NOT EXISTS "
            + TABLE_NONPRDDET + " (" + NONPRDDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REFNO + " TEXT, "
            + NONPRDDET_REASON_CODE + " TEXT, "
            + NONPRDDET_REASON + " TEXT); ";


    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-ATTENDANCE-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public static final String TABLE_ATTENDANCE = "Attendance";
    public static final String ATTENDANCE_ID = "Id";
    public static final String ATTENDANCE_DATE = "tDate";
    public static final String ATTENDANCE_S_TIME = "StartTime";
    public static final String ATTENDANCE_F_TIME = "EndTime";
    public static final String ATTENDANCE_VEHICLE = "Vehicle";
    public static final String ATTENDANCE_S_KM = "StartKm";
    public static final String ATTENDANCE_F_KM = "EndKm";
    public static final String ATTENDANCE_ROUTE = "Route";

    public static final String ATTENDANCE_DRIVER = "Driver";
    public static final String ATTENDANCE_ASSIST = "Assist";

    public static final String ATTENDANCE_DISTANCE = "Distance";
    public static final String ATTENDANCE_IS_SYNCED = "IsSynced";

    public static final String ATTENDANCE_REPCODE = "RepCode";
    public static final String ATTENDANCE_MAC = "MacAdd";

    public static final String CREATE_ATTENDANCE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDANCE + " (" + ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            ATTENDANCE_DATE + " TEXT, " + ATTENDANCE_S_TIME + " TEXT, " + ATTENDANCE_F_TIME + " TEXT, " + ATTENDANCE_VEHICLE + " TEXT, " + ATTENDANCE_S_KM + " TEXT, " + ATTENDANCE_F_KM + " TEXT, " + ATTENDANCE_DISTANCE + " TEXT, " + ATTENDANCE_IS_SYNCED + " TEXT, "

            + ATTENDANCE_REPCODE + " TEXT, " + ATTENDANCE_DRIVER + " TEXT, " + ATTENDANCE_ASSIST + " TEXT, " + ATTENDANCE_MAC + " TEXT, " + ATTENDANCE_ROUTE + " TEXT ); ";


    /**
     * ############################ Reason table Details
     * ################################
     */
    //table items
    public static final String TABLE_ITEMS = "Items";
    public static final String ITEM_ID = "ItemId";
    public static final String ITEM_CODE = "ItemCode";
    public static final String ITEM_NAME = "ItemName";
    public static final String PRI_LCODE = "PriLCode";
    public static final String STATUS = "Status";


    private static final String CREATE_TABLE_ITEMS = "CREATE  TABLE IF NOT EXISTS " + TABLE_ITEMS + " (" +
            ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ITEM_CODE + " TEXT, " + ITEM_NAME + " TEXT, " + PRI_LCODE +
            " TEXT, " + STATUS +
            " TEXT) ";
    //--

    // table ORDER
    public static final String TABLE_ORDER = "OrderHeader";
    // table attributes
    public static final String ORDER_ID = "OrderId";
    public static final String ORDER_CUSCODE = "CustomerCode";
    public static final String ORDER_START_TIME = "StartTime";
    public static final String ORDER_END_TIME = "EndTime";
    public static final String ORDER_LONGITUDE = "Longitude";
    public static final String ORDER_LATITUDE = "Latitude";
    public static final String ORDER_MANU_REF = "ManuRef";
    public static final String ORDER_REMARKS = "Remarks";
    public static final String ORDER_REPCODE = "RepCode";
    public static final String ORDER_TOTAL_AMT = "TotalAmt";
    public static final String ORDER_ADDDATE = "AddDate";

    public static final String ORDER_IS_SYNCED = "isSynced";
    public static final String ORDER_IS_ACTIVE = "isActive";
    public static final String ORDER_ROUTE_CODE = "RouteCode";
    public static final String ORDER_DELIV_DATE = "DeliverDate";

    // create String

    private static final String CREATE_TABLE_ORDER = " CREATE  TABLE IF NOT EXISTS " + TABLE_ORDER + " (" +
            ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ORDER_ADDDATE + " TEXT, " +
            ORDER_CUSCODE + " TEXT, " +
            ORDER_MANU_REF + " TEXT, " +
            REFNO + " TEXT, " +
            ORDER_REMARKS + " TEXT, " +
            ORDER_REPCODE + " TEXT, " +
            ORDER_TOTAL_AMT + " TEXT, " +
            TXNDATE + " TEXT, " +
            ORDER_LONGITUDE + " TEXT, " +
            ORDER_LATITUDE + " TEXT, " +
            ORDER_START_TIME + " TEXT, " +
            ORDER_IS_SYNCED + " TEXT, " +
            ORDER_IS_ACTIVE + " TEXT, " +
            ORDER_ROUTE_CODE + " TEXT, " +
            ORDER_DELIV_DATE + " TEXT, " +
            ORDER_END_TIME + " TEXT) ";
    //--
//    private static final String CREATE_ORDER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER +
//            " (" + ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            ORDER_ADDDATE + " TEXT, " +
//            ORDER_CUSCODE + " TEXT, " +
//            ORDER_MANU_REF + " TEXT, " +
//            ORDER_REFNO + " TEXT, " +
//            ORDER_REMARKS + " TEXT, " +
//            ORDER_REPCODE + " TEXT, " +
//            ORDER_TOTAL_AMT + " TEXT, " +
//            ORDER_TXN_DATE + " TEXT, " +
//            ORDER_LONGITUDE + " TEXT, " +
//            ORDER_LATITUDE + " TEXT, " +
//            ORDER_START_TIME + " TEXT, " +
//            ORDER_IS_SYNCED + " TEXT, " +
//            ORDER_IS_ACTIVE + " TEXT, " +
//            ORDER_ROUTE_CODE + " TEXT, " +
//            ORDER_END_TIME + " TEXT) ";

    //------------------ temp table crate for PreSales detail data saved------------------------------------
    public static final String TABLE_PRODUCT = "Products";
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_ITEMCODE = "itemcode";
    public static final String PRODUCT_ITEMNAME = "itemname";
    public static final String PRODUCT_PRICE = "price";

    public static final String PRODUCT_PRILCODE = "prilcode";
    public static final String PRODUCT_QTY = "qty";   //------------------ temp table crate for PreSales detail data saved------------------------------------


    private static final String CREATE_PRODUCT_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_PRODUCT + " ("
            + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PRODUCT_ITEMCODE + " TEXT, "
            + PRODUCT_ITEMNAME + " TEXT, "
            + PRODUCT_PRICE + " TEXT, "
            + REFNO + " TEXT, "
            + PRODUCT_PRILCODE + " TEXT, "
            + PRODUCT_QTY + " TEXT); ";
    private static final String INDEX_PRODUCTS = "CREATE UNIQUE INDEX IF NOT EXISTS Products_pre ON " +
            TABLE_PRODUCT + " (itemcode,itemname);";

    /**
     * ############################ Orderdetail table Details
     * ################################
     */

    // table
    public static final String TABLE_ORDER_DETAIL = "OrderDetail";
    // table attributes
    public static final String ORDDET_ID = "OrderDetId";
    public static final String ORDDET_AMT = "Amt";
    public static final String ORDDET_ITEM_CODE = "Itemcode";
    public static final String ORDDET_PRIL_CODE = "PriLCode";
    public static final String ORDDET_QTY = "Qty";

    public static final String ORDDET_PRICE = "Price";
    public static final String ORDDET_IS_ACTIVE = "isActive";
    public static final String ORDDET_ITEMNAME = "ItemName";
    //----------------------------------------------------------------------------------------------------------------------------

    // create String
    private static final String CREATE_ORDDET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_DETAIL +
            " (" + ORDDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ORDDET_AMT + " TEXT, " +
            ORDDET_ITEM_CODE + " TEXT, " +
            ORDDET_PRIL_CODE + " TEXT, " +
            ORDDET_QTY + " TEXT, " +
            REFNO + " TEXT, " +
            ORDDET_PRICE + " TEXT, " +
            ORDDET_ITEMNAME + " TEXT, " +
            ORDDET_IS_ACTIVE  + " TEXT); ";

    private static final String ORDDET_IDX = "CREATE UNIQUE INDEX IF NOT EXISTS idxordet_duplicate ON " +
            TABLE_ORDER_DETAIL + " (" + REFNO + "," + ORDDET_ITEM_CODE +  ")";



    // table
    public static final String TABLE_ITEMPRI = "ItemPri";
    // table attributes
    public static final String ITEMPRI_ID = "ItemPri_id";
    public static final String ITEMPRI_ITEM_CODE = "ItemCode";
    public static final String ITEMPRI_PRICE = "Price";
    public static final String ITEMPRI_PRIL_CODE = "PrilCode";

    private static final String CREATE_TABLE_ITEMPRI = "CREATE  TABLE IF NOT EXISTS " + TABLE_ITEMPRI + " (" +
            ITEMPRI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ITEMPRI_ITEM_CODE + " TEXT, " + ITEMPRI_PRICE + " TEXT, " + ITEMPRI_PRIL_CODE +
            " TEXT) ";

    /**
     * ############################ FDayExpHed ################################
     */

    public static final String TABLE_DAYEXPHED = "DayExpHed";
    // table attributes
    public static final String FDAYEXPHED_ID = "FDayExpHed_id";


    public static final String FDAYEXPHED_REPNAME = "RepName";
    public static final String FDAYEXPHED_DEALCODE = "DealCode";
    public static final String FDAYEXPHED_COSTCODE = "CostCode";
    public static final String FDAYEXPHED_REPCODE = "RepCode";
    public static final String FDAYEXPHED_REMARKS = "Remarks";
    public static final String FDAYEXPHED_AREACODE = "AreaCode";
    public static final String FDAYEXPHED_ADDUSER = "AddUser";
    public static final String FDAYEXPHED_ADDDATE = "AddDate";
    public static final String FDAYEXPHED_ADDMATCH = "AddMach";
    public static final String FDAYEXPHED_LONGITUDE = "Longitude";
    public static final String FDAYEXPHED_LATITUDE = "Latitude";
    public static final String FDAYEXPHED_ISSYNC = "issync";
    public static final String FDAYEXPHED_ACTIVESTATE = "ActiveState";
    public static final String FDAYEXPHED_TOTAMT = "TotAmt";
    public static final String FDAYEXPHED_ADDRESS = "Address";

    // create String
    private static final String CREATE_DAYEXPHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_DAYEXPHED + " (" + FDAYEXPHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FDAYEXPHED_REPNAME + " TEXT, " + FDAYEXPHED_DEALCODE + " TEXT, " + FDAYEXPHED_COSTCODE + " TEXT, " + FDAYEXPHED_REPCODE + " TEXT, " + FDAYEXPHED_REMARKS + " TEXT, " + FDAYEXPHED_AREACODE + " TEXT, " + FDAYEXPHED_ADDUSER + " TEXT, " + FDAYEXPHED_ADDDATE + " TEXT, " + FDAYEXPHED_ADDMATCH + " TEXT, " + FDAYEXPHED_LONGITUDE + " TEXT," + FDAYEXPHED_LATITUDE + " TEXT," + FDAYEXPHED_ISSYNC + " TEXT," + FDAYEXPHED_ACTIVESTATE + " TEXT," + FDAYEXPHED_TOTAMT + " TEXT," + FDAYEXPHED_ADDRESS + " TEXT); ";

    /**
     * ############################ FDayExpDet ################################
     */
    public static final String TABLE_DAYEXPDET = "DayExpDet";
    // table attributes
    public static final String DAYEXPDET_ID = "DayExpDet_id";

    public static final String DAYEXPDET_EXPCODE = "ExpCode";
    public static final String DAYEXPDET_AMT = "Amt";

    // create String
    private static final String CREATE_DAYEXPDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_DAYEXPDET + " (" + DAYEXPDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO +  " TEXT, " + DAYEXPDET_EXPCODE + " TEXT, " + DAYEXPDET_AMT + " TEXT); ";




    private static final String INDEX_CUSTOMER = "CREATE UNIQUE INDEX IF NOT EXISTS ui_debtor ON " + TABLE_CUSTOMER +
            " (CustomerCode);";

    //NEW CUSTOMER REGISTRATION
    public static final String TABLE_NEW_CUSTOMER = "NewCustomer";
    public static final String TABLE_REC_ID = "recID"; //1
    public static final String CUSTOMER_ID = "customerID"; //2
    public static final String CUSTOMER_OTHER_CODE = "otherCode";//3
    public static final String COMPANY_REG_CODE = "comRegCode"; //4
    public static final String NAME = "Name"; //5
    public static final String NIC = "Nic"; //6
    public static final String ADDRESS1 = "Address1"; //7
    public static final String ADDRESS2 = "Address2"; //8
    public static final String CITY = "City"; //9
    public static final String PHONE = "Phone"; //10
    public static final String MOBILE = "Mobile"; //27
    public static final String FAX = "Fax"; //11
    public static final String E_MAIL = "Email"; //12
    public static final String C_TOWN = "customer_Town";  //13
    public static final String DISTRICT = "District"; //15
    public static final String OLD_CODE = "old_Code"; //16

    public static final String C_IMAGE = "Image"; //18
    public static final String C_IMAGE1 = "Image1";  //19
    public static final String C_IMAGE2 = "Image2"; //20
    public static final String C_IMAGE3 = "Image3";  //21
    public static final String C_LONGITUDE = "lng";  //22
    public static final String C_LATITUDE = "lat"; //23
    public static final String C_ADD_DATE = "AddDate"; //24
    public static final String C_ADD_MACH = "AddMach"; //25
    public static final String C_IS_SYNCED = "isSynced"; //26

    private static final String CREATE_NEW_CUSTOMER = "CREATE  TABLE IF NOT EXISTS " + TABLE_NEW_CUSTOMER + " ("
            + TABLE_REC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CUSTOMER_ID + " TEXT, "
            + NAME + " TEXT, "
            + NIC + " TEXT, "
            + CUSTOMER_OTHER_CODE + " TEXT, "
            + COMPANY_REG_CODE + " TEXT, "
            + DISTRICT + " TEXT, "
            + C_TOWN + " TEXT, "
            + ROUTE_ID + " TEXT, "
            + ADDRESS1 + " TEXT, "
            + ADDRESS2 + " TEXT, "
            + CITY + " TEXT, "
            + MOBILE + " TEXT, "
            + PHONE + " TEXT, "
            + FAX + " TEXT, "
            + E_MAIL + " TEXT, "
            + OLD_CODE + " TEXT, "
            + C_IMAGE + " TEXT, "
            + C_IMAGE1 + " TEXT, "
            + C_IMAGE2 + " TEXT, "
            + C_IMAGE3 + " TEXT, "
            + C_LONGITUDE + " TEXT, "
            + C_LATITUDE + " TEXT, "
            + C_ADD_DATE + " TEXT, "
            + C_ADD_MACH + " TEXT, "
            + C_IS_SYNCED + " TEXT); ";
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-FOrdDisc table details*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public static final String TABLE_FORDDISC = "FOrdDisc";


    public static final String FORDDISC_REFNO1 = "RefNo1";
    public static final String FORDDISC_ITEMCODE = "itemcode";
    public static final String FORDDISC_DISAMT = "DisAmt";
    public static final String FORDDISC_DISPER = "DisPer";

    public static final String CREATE_FORDDISC_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FORDDISC + " (" + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FORDDISC_REFNO1 + " TEXT, " + FORDDISC_ITEMCODE + " TEXT, " + FORDDISC_DISAMT + " TEXT, " + FORDDISC_DISPER + " TEXT ); ";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-FOrdFreeIss table info-*-**-**-**-**-**-**-**-*-*-*-*/

    public static final String TABLE_FORDFREEISS = "FOrdFreeIss";


    public static final String FORDFREEISS_REFNO1 = "RefNo1";
    public static final String FORDFREEISS_ITEMCODE = "ItemCode";
    public static final String FORDFREEISS_QTY = "Qty";

    public static final String CREATE_FORDFREEISS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FORDFREEISS + " (" + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FORDFREEISS_REFNO1 + " TEXT, " + FORDFREEISS_ITEMCODE + " TEXT, " + FORDFREEISS_QTY + " TEXT ); ";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-**-**-**-**-**-**-*-*-*-*/

    /**
     * ############################ fDebtor table Details
     * ################################
     */

    // table
    public static final String TABLE_FDEBTOR = "fDebtor";
    // table attributes
    public static final String FDEBTOR_ID = "_id";
    public static final String FDEBTOR_CODE = "DebCode";
    public static final String FDEBTOR_NAME = "DebName";
    public static final String FDEBTOR_ADD1 = "DebAdd1";
    public static final String FDEBTOR_ADD2 = "DebAdd2";
    public static final String FDEBTOR_ADD3 = "DebAdd3";
    public static final String FDEBTOR_TELE = "DebTele";
    public static final String FDEBTOR_MOB = "DebMob";
    public static final String FDEBTOR_EMAIL = "DebEMail";
    public static final String FDEBTOR_CREATEDATE = "CretDate";
    public static final String FDEBTOR_TOWN_CODE = "TownCode";
    public static final String FDEBTOR_AREA_CODE = "AreaCode";
    public static final String FDEBTOR_DBGR_CODE = "DbGrCode";
    public static final String FDEBTOR_STATUS = "Status";
    public static final String FDEBTOR_ADD_USER = "AddUser";
    public static final String FDEBTOR_ADD_DATE_DEB = "AddDateDEB";
    public static final String FDEBTOR_ADD_MACH = "AddMach";
    public static final String FDEBTOR_CRD_PERIOD = "CrdPeriod";
    public static final String FDEBTOR_CHK_CRD_PRD = "ChkCrdPrd";
    public static final String FDEBTOR_CRD_LIMIT = "CrdLimit";
    public static final String FDEBTOR_CHK_CRD_LIMIT = "ChkCrdLmt";
    public static final String FDEBTOR_REP_CODE = "RepCode";
    public static final String FDEBTOR_RANK_CODE = "RankCode";
    public static final String FDEBTOR_TAXREG = "TaxReg";
    public static final String FDEBTOR_PRIL_CODE = "PrilCode";
    public static final String FDEBTOR_ROUTE_CODE = "RouteCode";
    public static final String FDEBTOR_SUMMARY = "DebSumary";
    public static final String FDEBTOR_CHK_MUSTFREE = "ChkMustFree";
    public static final String FDEBTOR_CHK_FREE = "ChkFree";

    // create String
    private static final String CREATE_FDEBTOR_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FDEBTOR + " (" + FDEBTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FDEBTOR_CODE + " TEXT, " + FDEBTOR_NAME + " TEXT, " + FDEBTOR_ADD1 + " TEXT, " + FDEBTOR_ADD2 + " TEXT, " + FDEBTOR_ADD3 + " TEXT, " + FDEBTOR_TELE + " TEXT, " + FDEBTOR_MOB + " TEXT, " + FDEBTOR_EMAIL + " TEXT, " + FDEBTOR_CREATEDATE + " TEXT, " + FDEBTOR_TOWN_CODE + " TEXT, " + FDEBTOR_AREA_CODE + " TEXT, " + FDEBTOR_DBGR_CODE + " TEXT, " + FDEBTOR_STATUS + " TEXT, " + FDEBTOR_ADD_USER + " TEXT, " + FDEBTOR_ADD_DATE_DEB + " TEXT, " + FDEBTOR_ADD_MACH + " TEXT, " + FDEBTOR_CRD_PERIOD + " TEXT, " + FDEBTOR_CHK_CRD_PRD + " TEXT, " + FDEBTOR_CRD_LIMIT + " TEXT, " + FDEBTOR_CHK_CRD_LIMIT + " TEXT, " + FDEBTOR_REP_CODE + " TEXT, " + FDEBTOR_RANK_CODE + " TEXT, " + FDEBTOR_TAXREG + " TEXT, " + FDEBTOR_PRIL_CODE + " TEXT, " + FDEBTOR_ROUTE_CODE + " TEXT, " + FDEBTOR_SUMMARY + " TEXT, " + FDEBTOR_CHK_MUSTFREE + " TEXT, " + FDEBTOR_CHK_FREE + " TEXT); ";

    private static final String TESTDEBTOR = "CREATE UNIQUE INDEX IF NOT EXISTS idxdebtor_something ON " + TABLE_FDEBTOR + " (" + FDEBTOR_CODE + ")";

    /**
     * ############################ fControl table Details
     * ################################
     */

    // table
    public static final String TABLE_FCONTROL = "fControl";
    // table attributes
    public static final String FCONTROL_ID = "fcontrol_id";
    public static final String FCONTROL_COM_NAME = "ComName";
    public static final String FCONTROL_COM_ADD1 = "ComAdd1";
    public static final String FCONTROL_COM_ADD2 = "ComAdd2";
    public static final String FCONTROL_COM_ADD3 = "ComAdd3";
    public static final String FCONTROL_COM_TEL1 = "comtel1";
    public static final String FCONTROL_COM_TEL2 = "comtel2";
    public static final String FCONTROL_COM_FAX = "comfax1";
    public static final String FCONTROL_COM_EMAIL = "comemail";
    public static final String FCONTROL_COM_WEB = "comweb";
    public static final String FCONTROL_FYEAR = "confyear";
    public static final String FCONTROL_TYEAR = "contyear";
    public static final String FCONTROL_COM_REGNO = "comRegNo";
    public static final String FCONTROL_FTXN = "ConfTxn";
    public static final String FCONTROL_TTXN = "ContTxn";
    public static final String FCONTROL_CRYSTALPATH = "Crystalpath";
    public static final String FCONTROL_VATCMTAXNO = "VatCmTaxNo";
    public static final String FCONTROL_NBTCMTAXNO = "NbtCmTaxNo";
    public static final String FCONTROL_SYSTYPE = "SysType";
    public static final String FCONTROL_DEALCODE = "DealCode";
    public static final String FCONTROL_BASECUR = "basecur";
    public static final String FCONTROL_BALGCRLM = "BalgCrLm";
    public static final String FCONTROL_CONAGE1 = "conage1";
    public static final String FCONTROL_CONAGE2 = "conage2";
    public static final String FCONTROL_CONAGE3 = "conage3";
    public static final String FCONTROL_CONAGE4 = "conage4";
    public static final String FCONTROL_CONAGE5 = "conage5";
    public static final String FCONTROL_CONAGE6 = "conage6";
    public static final String FCONTROL_CONAGE7 = "conage7";
    public static final String FCONTROL_CONAGE8 = "conage8";
    public static final String FCONTROL_CONAGE9 = "conage9";
    public static final String FCONTROL_CONAGE10 = "conage10";
    public static final String FCONTROL_CONAGE11 = "conage11";
    public static final String FCONTROL_CONAGE12 = "conage12";
    public static final String FCONTROL_CONAGE13 = "conage13";
    public static final String FCONTROL_CONAGE14 = "conage14";
    public static final String FCONTROL_SALESACC = "salesacc";

    // create String
    private static final String CREATE_FCONTROL_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FCONTROL + " (" + FCONTROL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FCONTROL_COM_NAME + " TEXT, " + FCONTROL_COM_ADD1 + " TEXT, " + FCONTROL_COM_ADD2 + " TEXT, " + FCONTROL_COM_ADD3 + " TEXT, " + FCONTROL_COM_TEL1 + " TEXT, " + FCONTROL_COM_TEL2 + " TEXT, " + FCONTROL_COM_FAX + " TEXT, " + FCONTROL_COM_EMAIL + " TEXT, " + FCONTROL_COM_WEB + " TEXT, " + FCONTROL_FYEAR + " TEXT, " + FCONTROL_TYEAR + " TEXT, " + FCONTROL_COM_REGNO + " TEXT, " + FCONTROL_FTXN + " TEXT, " + FCONTROL_TTXN + " TEXT, " + FCONTROL_CRYSTALPATH + " TEXT, " + FCONTROL_VATCMTAXNO + " TEXT, " + FCONTROL_NBTCMTAXNO + " TEXT, " + FCONTROL_SYSTYPE + " TEXT, " + FCONTROL_DEALCODE + " TEXT, " + FCONTROL_BASECUR + " TEXT, " + FCONTROL_BALGCRLM + " TEXT, " + FCONTROL_CONAGE1 + " TEXT, " + FCONTROL_CONAGE2 + " TEXT, " + FCONTROL_CONAGE3 + " TEXT, " + FCONTROL_CONAGE4 + " TEXT, " + FCONTROL_CONAGE5 + " TEXT, " + FCONTROL_CONAGE6 + " TEXT, " + FCONTROL_CONAGE7 + " TEXT, " + FCONTROL_CONAGE8 + " TEXT, " + FCONTROL_CONAGE9 + " TEXT, " + FCONTROL_CONAGE10 + " TEXT, " + FCONTROL_CONAGE11 + " TEXT, " + FCONTROL_CONAGE12 + " TEXT, " + FCONTROL_CONAGE13 + " TEXT, " + FCONTROL_CONAGE14 + " TEXT, " + FCONTROL_SALESACC + " TEXT); ";

    /**
     * ############################ fCompanySetting table Details
     * ################################
     */

    // table
    public static final String TABLE_FCOMPANYSETTING = "fCompanySetting";
    // table attributes
    public static final String FCOMPANYSETTING_ID = "fcomset_id";// ok
    public static final String FCOMPANYSETTING_SETTINGS_CODE = "cSettingsCode";// ok
    public static final String FCOMPANYSETTING_GRP = "cSettingGrp";// ok
    public static final String FCOMPANYSETTING_LOCATION_CHAR = "cLocationChar";// ok
    public static final String FCOMPANYSETTING_CHAR_VAL = "cCharVal";// ok
    public static final String FCOMPANYSETTING_NUM_VAL = "nNumVal";// ok
    public static final String FCOMPANYSETTING_REMARKS = "cRemarks";// ok
    public static final String FCOMPANYSETTING_TYPE = "nType";// ok
    // public static final String FCOMPANYSETTING_UPDATEFLAG = "bUpdateFlag";
    public static final String FCOMPANYSETTING_COMPANY_CODE = "cCompanyCode";// ok
    // public static final String FCOMPANYSETTING_RECORD_ID = "recordid";
    // public static final String FCOMPANYSETTING_TIMESTAMP_COLUMN =
    // "timestamp_column";

    // create String
    private static final String CREATE_FCOMPANYSETTING_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FCOMPANYSETTING + " (" + FCOMPANYSETTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FCOMPANYSETTING_SETTINGS_CODE + " TEXT, " + FCOMPANYSETTING_GRP + " TEXT, " + FCOMPANYSETTING_LOCATION_CHAR + " TEXT, " + FCOMPANYSETTING_CHAR_VAL + " TEXT, " + FCOMPANYSETTING_NUM_VAL + " TEXT, " + FCOMPANYSETTING_REMARKS + " TEXT, " + FCOMPANYSETTING_TYPE + " TEXT, "
            // + FCOMPANYSETTING_UPDATEFLAG+ " TEXT, "
            + FCOMPANYSETTING_COMPANY_CODE + " TEXT); ";
    // + FCOMPANYSETTING_RECORD_ID+ " TEXT, "
    // + FCOMPANYSETTING_TIMESTAMP_COLUMN+ " TEXT); ";

    private static final String IDXCOMSETT = "CREATE UNIQUE INDEX IF NOT EXISTS idxcomsett_something ON " + TABLE_FCOMPANYSETTING + " (" + FCOMPANYSETTING_SETTINGS_CODE + ")";

    /**
     * ############################ fRoute table Details
     * ################################
     */

    // table
    public static final String TABLE_FROUTE = "fRoute";
    // table attributes
    public static final String FROUTE_ID = "route_id";
    public static final String FROUTE_REPCODE = "RepCode";
    public static final String FROUTE_ROUTECODE = "RouteCode";
    public static final String FROUTE_ROUTE_NAME = "RouteName";
    public static final String FROUTE_RECORDID = "RecordId";
    public static final String FROUTE_ADDDATE = "AddDate";
    public static final String FROUTE_ADD_MACH = "AddMach";
    public static final String FROUTE_ADD_USER = "AddUser";
    public static final String FROUTE_AREACODE = "AreaCode";
    public static final String FROUTE_DEALCODE = "DealCode";
    public static final String FROUTE_FREQNO = "FreqNo";
    public static final String FROUTE_KM = "Km";
    public static final String FROUTE_MINPROCALL = "MinProcall";
    public static final String FROUTE_RDALORATE = "RDAloRate";
    public static final String FROUTE_RDTARGET = "RDTarget";
    public static final String FROUTE_REMARKS = "Remarks";
    public static final String FROUTE_STATUS = "Status";
    public static final String FROUTE_TONNAGE = "Tonnage";

    // create String
    private static final String CREATE_FROUTE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FROUTE + " (" + FROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FROUTE_REPCODE + " TEXT, " + FROUTE_ROUTECODE + " TEXT, " + FROUTE_ROUTE_NAME + " TEXT, " + FROUTE_RECORDID + " TEXT, " + FROUTE_ADDDATE + " TEXT, " + FROUTE_ADD_MACH + " TEXT, " + FROUTE_ADD_USER + " TEXT, " + FROUTE_AREACODE + " TEXT, " + FROUTE_DEALCODE + " TEXT, " + FROUTE_FREQNO + " TEXT, " + FROUTE_KM + " TEXT, " + FROUTE_MINPROCALL + " TEXT, " + FROUTE_RDALORATE + " TEXT, " + FROUTE_RDTARGET + " TEXT, " + FROUTE_REMARKS + " TEXT, " + FROUTE_STATUS + " TEXT, " + FROUTE_TONNAGE + " TEXT); ";

    /**
     * ############################ fBank table Details
     * ################################
     */

    // table
    public static final String TABLE_FBANK = "fBank";
    // table attributes
    public static final String FBANK_ID = "bankre_id";
    public static final String FBANK_RECORD_ID = "RecordId";
    public static final String FBANK_BANK_CODE = "bankcode";
    public static final String FBANK_BANK_NAME = "bankname";
    public static final String FBANK_BANK_ACC_NO = "bankaccno";
    public static final String FBANK_BRANCH = "Branch";
    public static final String FBANK_ADD1 = "bankadd1";
    public static final String FBANK_ADD2 = "bankadd2";
    public static final String FBANK_ADD_DATE = "AddDate";
    public static final String FBANK_ADD_MACH = "AddMach";
    public static final String FBANK_ADD_USER = "AddUser";

    // create String
    private static final String CREATE_FBANK_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FBANK + " (" + FBANK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FBANK_RECORD_ID + " TEXT, " + FBANK_BANK_CODE + " TEXT, " + FBANK_BANK_NAME + " TEXT, " + FBANK_BANK_ACC_NO + " TEXT, " + FBANK_BRANCH + " TEXT, " + FBANK_ADD1 + " TEXT, " + FBANK_ADD2 + " TEXT, " + FBANK_ADD_MACH + " TEXT, " + FBANK_ADD_USER + " TEXT, " + FBANK_ADD_DATE + " TEXT); ";

    private static final String TESTBANK = "CREATE UNIQUE INDEX IF NOT EXISTS idxbank_something ON " + TABLE_FBANK + " (" + FBANK_BANK_CODE + ")";

    /**
     * ############################ fReason table Details
     * ################################
     */

    public static final String TABLE_FREASON = "fReason";
    // table attributes
    public static final String FREASON_ID = "freason_id";
    public static final String FREASON_ADD_DATE = "AddDate";
    public static final String FREASON_ADD_MACH = "AddMach";
    public static final String FREASON_ADD_USER = "AddUser";
    public static final String FREASON_CODE = "ReaCode";
    public static final String FREASON_NAME = "ReaName";
    public static final String FREASON_REATCODE = "ReaTcode";
    public static final String FREASON_RECORD_ID = "RecordId";
    public static final String FREASON_TYPE = "Type";

    // create String
    private static final String CREATE_FREASON_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FREASON + " (" + FREASON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FREASON_ADD_DATE + " TEXT, " + FREASON_ADD_MACH + " TEXT, " + FREASON_ADD_USER + " TEXT, " + FREASON_CODE + " TEXT, " + FREASON_NAME + " TEXT, " + FREASON_REATCODE + " TEXT, " + FREASON_RECORD_ID + " TEXT, " + FREASON_TYPE + " TEXT); ";

    /**
     * ############################ fExpense table Details
     * ################################
     */

    // fDebtor table
    public static final String TABLE_FEXPENSE = "fExpense";
    // fDebtor table attributes
    public static final String FEXPENSE_ID = "uexp_id";
    public static final String FEXPENSE_CODE = "ExpCode";
    public static final String FEXPENSE_GRP_CODE = "ExpGrpCode";
    public static final String FEXPENSE_NAME = "ExpName";
    public static final String FEXPENSE_RECORDID = "RecordId";
    public static final String FEXPENSE_STATUS = "Status";
    public static final String FEXPENSE_ADD_MACH = "AddMach";
    public static final String FEXPENSE_ADD_USER = "AddUser";
    public static final String FEXPENSE_ADD_DATE = "AddDate";

    // create String
    private static final String CREATE_FEXPENSE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FEXPENSE + " (" + FEXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FEXPENSE_CODE + " TEXT, " + FEXPENSE_GRP_CODE + " TEXT, " + FEXPENSE_NAME + " TEXT, " + FEXPENSE_RECORDID + " TEXT, " + FEXPENSE_STATUS + " TEXT, " + FEXPENSE_ADD_MACH + " TEXT, " + FEXPENSE_ADD_DATE + " TEXT, " + FEXPENSE_ADD_USER + " TEXT); ";

    /**
     * ############################ fTown table Details
     * ################################
     */

    // table
    public static final String TABLE_FTOWN = "fTown";
    // table attributes
    public static final String FTOWN_ID = "townre_id";
    public static final String FTOWN_RECORDID = "RecordId";
    public static final String FTOWN_CODE = "TownCode";
    public static final String FTOWN_NAME = "TownName";
    public static final String FTOWN_DISTR_CODE = "DistrCode";
    public static final String FTOWN_ADDDATE = "AddDate";
    public static final String FTOWN_ADD_MACH = "AddMach";
    public static final String FTOWN_ADD_USER = "AddUser";

    // create String
    private static final String CREATE_FTOWN_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FTOWN + " (" + FTOWN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FTOWN_RECORDID + " TEXT, " + FTOWN_CODE + " TEXT, " + FTOWN_NAME + " TEXT, " + FTOWN_DISTR_CODE + " TEXT, " + FTOWN_ADDDATE + " TEXT, " + FTOWN_ADD_MACH + " TEXT, " + FTOWN_ADD_USER + " TEXT); ";

    /**
     * ############################ FTrgCapUL table Details
     * ################################
     */

    // table
    public static final String TABLE_FTRGCAPUL = "FTrgCapUL";
    // table attributes
    public static final String FTRGCAPUL_ID = "ftrg_id";
    public static final String FTRGCAPUL_ADD_DATE = "AddDate";
    public static final String FTRGCAPUL_ADD_MACH = "AddMach";
    public static final String FTRGCAPUL_ADD_USER = "AddUser";
    public static final String FTRGCAPUL_DEAL_CODE = "DealCode";
    public static final String FTRGCAPUL_MONTH = "Month";
    public static final String FTRGCAPUL_QTY = "Qty";
    public static final String FTRGCAPUL_RECORDID = "RecordId";
    public static final String FTRGCAPUL_REP_CODE = "RepCode";
    public static final String FTRGCAPUL_SKU_CODE = "SKUCode";
    public static final String FTRGCAPUL_YEAR = "Year";

    // create String
    private static final String CREATE_FTRGCAPUL_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FTRGCAPUL + " (" + FTRGCAPUL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FTRGCAPUL_ADD_DATE + " TEXT, " + FTRGCAPUL_ADD_MACH + " TEXT, " + FTRGCAPUL_ADD_USER + " TEXT, " + FTRGCAPUL_DEAL_CODE + " TEXT, " + FTRGCAPUL_MONTH + " TEXT, " + FTRGCAPUL_QTY + " TEXT, " + FTRGCAPUL_RECORDID + " TEXT, " + FTRGCAPUL_REP_CODE + " TEXT, " + FTRGCAPUL_SKU_CODE + " TEXT, " + FTRGCAPUL_YEAR + " TEXT); ";

    /**
     * ############################ fType table Details
     * ################################
     */

    // table
    public static final String TABLE_FTYPE = "fType";
    // table attributes
    public static final String FTYPE_ID = "ftype_id";
    public static final String FTYPE_ADD_DATE = "AddDate";
    public static final String FTYPE_ADD_MACH = "AddMach";
    public static final String FTYPE_ADD_USER = "AddUser";
    public static final String FTYPE_RECORDID = "RecordId";
    public static final String FTYPE_CODE = "TypeCode";
    public static final String FTYPE_NAME = "TypeName";

    // create String
    private static final String CREATE_FTYPE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FTYPE + " (" + FTYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FTYPE_ADD_DATE + " TEXT, " + FTYPE_ADD_MACH + " TEXT, " + FTYPE_ADD_USER + " TEXT, " + FTYPE_RECORDID + " TEXT, " + FTYPE_CODE + " TEXT, " + FTYPE_NAME + " TEXT); ";

    /**
     * ############################ fSubBrand table Details
     * ################################
     */

    // table
    public static final String TABLE_FSUBBRAND = "fSubBrand";
    // table attributes
    public static final String FSUBBRAND_ID = "fsubbrand_id";
    public static final String FSUBBRAND_ADD_DATE = "AddDate";
    public static final String FSUBBRAND_ADD_MACH = "AddMach";
    public static final String FSUBBRAND_ADD_USER = "AddUser";
    public static final String FSUBBRAND_RECORDID = "RecordId";
    public static final String FSUBBRAND_CODE = "SBrandCode";
    public static final String FSUBBRAND_NAME = "SBrandName";

    // create String
    private static final String CREATE_FSUBBRAND_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FSUBBRAND + " (" + FSUBBRAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FSUBBRAND_ADD_DATE + " TEXT, " + FSUBBRAND_ADD_MACH + " TEXT, " + FSUBBRAND_ADD_USER + " TEXT, " + FSUBBRAND_RECORDID + " TEXT, " + FSUBBRAND_CODE + " TEXT, " + FSUBBRAND_NAME + " TEXT); ";

    /**
     * ############################ fCost table Details
     * ################################
     */

    // table
    public static final String TABLE_FCOST = "fCost";
    // table attributes
    public static final String FCOST_ID = "fcost_id";
    public static final String FCOST_CODE = "CostCode";
    public static final String FCOST_NAME = "CostName";

    // create String
    private static final String CREATE_FCOST_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FCOST + " (" + FCOST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FCOST_CODE + " TEXT, " + FCOST_NAME + " TEXT); ";

    /**
     * ############################ fGroup table Details
     * ################################
     */

    // table
    public static final String TABLE_FGROUP = "fGroup";
    // table attributes
    public static final String FGROUP_ID = "fgroup_id";
    public static final String FGROUP_ADD_DATE = "AddDate";
    public static final String FGROUP_ADD_MACH = "AddMach";
    public static final String FGROUP_ADD_USER = "AddUser";
    public static final String FGROUP_CODE = "GroupCode";
    public static final String FGROUP_NAME = "GroupName";
    public static final String FGROUP_RECORDID = "RecordId";

    // create String
    private static final String CREATE_FGROUP_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FGROUP + " (" + FGROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FGROUP_ADD_DATE + " TEXT, " + FGROUP_ADD_MACH + " TEXT, " + FGROUP_ADD_USER + " TEXT, " + FGROUP_CODE + " TEXT, " + FGROUP_NAME + " TEXT, " + FGROUP_RECORDID + " TEXT); ";

    /**
     * ############################ fSku table Details
     * ################################
     */

    // table
    public static final String TABLE_FSKU = "fSku";
    // table attributes
    public static final String FSKU_ID = "fsku_id";
    public static final String FSKU_ADD_DATE = "AddDate";
    public static final String FSKU_ADD_MACH = "AddMach";
    public static final String FSKU_ADD_USER = "AddUser";
    public static final String FSKU_BRAND_CODE = "BrandCode";
    public static final String FSKU_GROUP_CODE = "GroupCode";
    public static final String FSKU_ITEM_STATUS = "ItemStatus";
    public static final String FSKU_MUST_SALE = "MustSale";
    public static final String FSKU_NOUCASE = "NOUCase";
    public static final String FSKU_ORDSEQ = "OrdSeq";
    public static final String FSKU_RECORDID = "RecordId";
    public static final String FSKU_SUB_BRAND_CODE = "SBrandCode";
    public static final String FSKU_CODE = "SKUCode";
    public static final String FSKU_NAME = "SkuName";
    public static final String FSKU_SIZE_CODE = "SkuSizCode";
    public static final String FSKU_TONNAGE = "Tonnage";
    public static final String FSKU_TYPE_CODE = "TypeCode";
    public static final String FSKU_UNIT = "Unit";

    // create String
    private static final String CREATE_FSKU_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FSKU + " (" + FSKU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FSKU_ADD_DATE + " TEXT, " + FSKU_ADD_MACH + " TEXT, " + FSKU_ADD_USER + " TEXT, " + FSKU_BRAND_CODE + " TEXT, " + FSKU_GROUP_CODE + " TEXT, " + FSKU_ITEM_STATUS + " TEXT, " + FSKU_MUST_SALE + " TEXT, " + FSKU_NOUCASE + " TEXT, " + FSKU_ORDSEQ + " TEXT, " + FSKU_RECORDID + " TEXT, " + FSKU_SUB_BRAND_CODE + " TEXT, " + FSKU_CODE + " TEXT, " + FSKU_NAME + " TEXT, " + FSKU_SIZE_CODE + " TEXT, " + FSKU_TONNAGE + " TEXT, " + FSKU_TYPE_CODE + " TEXT, " + FSKU_UNIT + " TEXT); ";

    /**
     * ############################ fbrand table Details
     * ################################
     */

    // table
    public static final String TABLE_FBRAND = "fbrand";
    // table attributes
    public static final String FBRAND_ID = "fbrand_id";
    public static final String FBRAND_ADD_MACH = "AddMach";
    public static final String FBRAND_ADD_USER = "AddUser";
    public static final String FBRAND_CODE = "BrandCode";
    public static final String FBRAND_NAME = "BrandName";
    public static final String FBRAND_RECORDID = "RecordId";

    // create String
    private static final String CREATE_FBRAND_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FBRAND + " (" + FBRAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FBRAND_ADD_MACH + " TEXT, " + FBRAND_ADD_USER + " TEXT, " + FBRAND_CODE + " TEXT, " + FBRAND_NAME + " TEXT, " + FBRAND_RECORDID + " TEXT); ";

    /**
     * ############################ FOrdHed table Details
     * ################################
     */
    public static final String TABLE_FPRODUCT = "fProducts";
    public static final String FPRODUCT_ID = "id";
    public static final String FPRODUCT_ITEMCODE = "itemcode";
    public static final String FPRODUCT_ITEMNAME = "itemname";
    public static final String FPRODUCT_PRICE = "price";
    public static final String FPRODUCT_QOH = "qoh";
    public static final String FPRODUCT_MIN_PRICE = "minPrice";
    public static final String FPRODUCT_MAX_PRICE = "maxPrice";
    public static final String FPRODUCT_QTY = "qty";
    public static final String FPRODUCT_CHANGED_PRICE = "ChangedPrice";
    public static final String FPRODUCT_TXNTYPE= "TxnType";

    private static final String CREATE_FPRODUCT_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FPRODUCT + " ("
            + FPRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FPRODUCT_ITEMCODE + " TEXT, "
            + FPRODUCT_ITEMNAME + " TEXT, "
            + FPRODUCT_PRICE + " TEXT, "
            + FPRODUCT_MIN_PRICE + " TEXT, "
            + FPRODUCT_MAX_PRICE + " TEXT, "
            + FPRODUCT_QOH + " TEXT, "
            + FPRODUCT_CHANGED_PRICE + " TEXT, "
            + FPRODUCT_TXNTYPE + " TEXT, "
            + FPRODUCT_QTY + " TEXT); ";
    private static final String INDEX_FPRODUCTS = "CREATE UNIQUE INDEX IF NOT EXISTS ui_product ON " + TABLE_FPRODUCT + " (itemcode);";

    // table
    public static final String TABLE_FORDHED = "FOrdHed";
    // table attributes
    public static final String FORDHED_ID = "FOrdHed_id";
    public static final String FORDHED_ADD_DATE = "AddDate";
    public static final String FORDHED_ADD_MACH = "AddMach";
    public static final String FORDHED_ADD_USER = "AddUser";
    public static final String FORDHED_APP_DATE = "Appdate";
    public static final String FORDHED_APPSTS = "Appsts";
    public static final String FORDHED_APP_USER = "AppUser";
    public static final String FORDHED_BP_TOTAL_DIS = "BPTotalDis";
    public static final String FORDHED_B_TOTAL_AMT = "BTotalAmt";
    public static final String FORDHED_B_TOTAL_DIS = "BTotalDis";
    public static final String FORDHED_B_TOTAL_TAX = "BTotalTax";
    public static final String FORDHED_COST_CODE = "CostCode";
    public static final String FORDHED_CUR_CODE = "CurCode";
    public static final String FORDHED_CUR_RATE = "CurRate";
    public static final String FORDHED_DEB_CODE = "DebCode";
    public static final String FORDHED_DIS_PER = "DisPer";
    public static final String FORDHED_START_TIME_SO = "startTimeSO";
    public static final String FORDHED_END_TIME_SO = "endTimeSO";
    public static final String FORDHED_LONGITUDE = "Longitude";
    public static final String FORDHED_LATITUDE = "Latitude";
    public static final String FORDHED_LOC_CODE = "LocCode";
    public static final String FORDHED_MANU_REF = "ManuRef";
    public static final String FORDHED_RECORD_ID = "RecordId";
    public static final String FORDHED_REMARKS = "Remarks";
    public static final String FORDHED_REPCODE = "RepCode";
    public static final String FORDHED_TAX_REG = "TaxReg";
    public static final String FORDHED_TIMESTAMP_COLUMN = "Timestamp_column";
    public static final String FORDHED_TOTAL_AMT = "TotalAmt";
    public static final String FORDHED_TOTALDIS = "TotalDis";
    public static final String FORDHED_TOTAL_TAX = "TotalTax";
    public static final String FORDHED_TXN_TYPE = "TxnType";

    public static final String FORDHED_ADDRESS = "gpsAddress";
    public static final String FORDHED_TOTAL_ITM_DIS = "TotalItemDis";
    public static final String FORDHED_TOT_MKR_AMT = "TotMkrAmt";
    public static final String FORDHED_IS_SYNCED = "isSynced";
    public static final String FORDHED_IS_ACTIVE = "isActive";
    public static final String FORDHED_DELV_DATE = "DelvDate";
    public static final String FORDHED_HED_DIS_VAL = "HedDisVal";
    public static final String FORDHED_HED_DIS_PER_VAL = "HedDisPerVal";
    public static final String FORDHED_ROUTE_CODE = "RouteCode";
    public static final String FORDHED_PAYMENT_TYPE = "PaymentType";
    public static final String FORDHED_UPLOAD_TIME = "UploadTime";
    // create String
    private static final String CREATE_FORDHED_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FORDHED + " (" + FORDHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FORDHED_ADD_MACH + " TEXT, " + FORDHED_ADD_DATE + " TEXT," + FORDHED_ADD_USER + " TEXT, " + FORDHED_APP_DATE + " TEXT, " + FORDHED_ADDRESS + " TEXT, " + FORDHED_APPSTS + " TEXT, " + FORDHED_APP_USER + " TEXT, " + FORDHED_BP_TOTAL_DIS + " TEXT, " + FORDHED_B_TOTAL_AMT + " TEXT, " + FORDHED_B_TOTAL_DIS + " TEXT, " + FORDHED_B_TOTAL_TAX + " TEXT, " + FORDHED_COST_CODE + " TEXT, " + FORDHED_CUR_CODE + " TEXT, " + FORDHED_CUR_RATE + " TEXT, " + FORDHED_DEB_CODE + " TEXT, " + FORDHED_LOC_CODE + " TEXT, " + FORDHED_MANU_REF + " TEXT, " + FORDHED_DIS_PER + " TEXT, " + FORDHED_RECORD_ID + " TEXT, " + REFNO + " TEXT, " + FORDHED_REMARKS + " TEXT, " + FORDHED_REPCODE + " TEXT, " + FORDHED_TAX_REG + " TEXT, " + FORDHED_TIMESTAMP_COLUMN + " TEXT, " + FORDHED_TOTAL_TAX + " TEXT, " + FORDHED_TOTAL_AMT + " TEXT, " + FORDHED_TOTALDIS + " TEXT, " + FORDHED_TOTAL_ITM_DIS + " TEXT, " + FORDHED_TOT_MKR_AMT + " TEXT, " + FORDHED_TXN_TYPE + " TEXT, " + TXNDATE + " TEXT, " + FORDHED_LONGITUDE + " TEXT, " + FORDHED_LATITUDE + " TEXT, " + FORDHED_START_TIME_SO + " TEXT, " + FORDHED_IS_SYNCED + " TEXT, " + FORDHED_IS_ACTIVE + " TEXT, " + FORDHED_DELV_DATE + " TEXT, " + FORDHED_ROUTE_CODE + " TEXT, " + FORDHED_HED_DIS_VAL + " TEXT, " + FORDHED_HED_DIS_PER_VAL + " TEXT," + FORDHED_PAYMENT_TYPE + " TEXT," + FORDHED_END_TIME_SO + " TEXT," + FORDHED_UPLOAD_TIME + " TEXT); ";

    /**
     * ############################ FOrddet table Details
     * ################################
     */

    // table
    public static final String TABLE_FORDDET = "FOrddet";
    // table attributes
    public static final String FORDDET_ID = "stodet_id";
    public static final String FORDDET_AMT = "Amt";
    public static final String FORDDET_BAL_QTY = "BalQty";
    public static final String FORDDET_B_AMT = "BAmt";
    public static final String FORDDET_B_DIS_AMT = "BDisAmt";
    public static final String FORDDET_BP_DIS_AMT = "BPDisAmt";
    public static final String FORDDET_B_SELL_PRICE = "BSellPrice";
    public static final String FORDDET_BT_TAX_AMT = "BTaxAmt";
    public static final String FORDDET_BT_SELL_PRICE = "BTSellPrice";
    public static final String FORDDET_CASE = "Cases";
    public static final String FORDDET_CASE_QTY = "CaseQty";
    public static final String FORDDET_DIS_AMT = "DisAmt";
    public static final String FORDDET_DIS_PER = "DisPer";
    public static final String FORDDET_FREE_QTY = "freeqty";
    public static final String FORDDET_ITEM_CODE = "Itemcode";
    public static final String FORDDET_P_DIS_AMT = "PDisAmt";
    public static final String FORDDET_PRIL_CODE = "PrilCode";
    public static final String FORDDET_QTY = "Qty";
    public static final String FORDDET_DIS_VAL_AMT = "DisValAmt";
    public static final String FORDDET_PICE_QTY = "PiceQty";
    public static final String FORDDET_REA_CODE = "ReaCode";
    public static final String FORDDET_TYPE = "Types";
    public static final String FORDDET_RECORD_ID = "RecordId";

    public static final String FORDDET_SELL_PRICE = "SellPrice";
    public static final String FORDDET_SEQNO = "SeqNo";
    public static final String FORDDET_TAX_AMT = "TaxAmt";
    public static final String FORDDET_TAX_COM_CODE = "TaxComCode";
    public static final String FORDDET_TIMESTAMP_COLUMN = "timestamp_column";
    public static final String FORDDET_T_SELL_PRICE = "TSellPrice";

    public static final String FORDDET_TXN_TYPE = "TxnType";
    public static final String FORDDET_IS_ACTIVE = "isActive";

    public static final String FORDDET_ITEMNAME = "ItemName";
    public static final String FORDDET_PACKSIZE = "PackSize";

    // create String
    private static final String CREATE_FORDDET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FORDDET + " (" + FORDDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FORDDET_AMT + " TEXT, " + FORDDET_BAL_QTY + " TEXT, " + FORDDET_B_AMT + " TEXT, " + FORDDET_B_DIS_AMT + " TEXT, " + FORDDET_BP_DIS_AMT + " TEXT, " + FORDDET_B_SELL_PRICE + " TEXT, " + FORDDET_BT_TAX_AMT + " TEXT, " + FORDDET_BT_SELL_PRICE + " TEXT, " + FORDDET_CASE + " TEXT, " + FORDDET_CASE_QTY + " TEXT, " + FORDDET_DIS_AMT + " TEXT, " + FORDDET_DIS_PER + " TEXT, " + FORDDET_FREE_QTY + " TEXT, " + FORDDET_ITEM_CODE + " TEXT, " + FORDDET_P_DIS_AMT + " TEXT, " + FORDDET_PRIL_CODE + " TEXT, " + FORDDET_QTY + " TEXT, " + FORDDET_DIS_VAL_AMT + " TEXT, " + FORDDET_PICE_QTY + " TEXT, " + FORDDET_REA_CODE + " TEXT, " + FORDDET_TYPE + " TEXT, " + FORDDET_RECORD_ID + " TEXT, " + REFNO + " TEXT, " + FORDDET_SELL_PRICE + " TEXT, " + FORDDET_SEQNO + " TEXT, " + FORDDET_TAX_AMT + " TEXT, " + FORDDET_TAX_COM_CODE + " TEXT, " + FORDDET_TIMESTAMP_COLUMN + " TEXT, " + FORDDET_T_SELL_PRICE + " TEXT, "

            + FORDDET_ITEMNAME + " TEXT, " + FORDDET_PACKSIZE + " TEXT, "

            + TXNDATE + " TEXT, " + FORDDET_IS_ACTIVE + " TEXT, " + FORDDET_TXN_TYPE + " TEXT); ";

    /**
     * ############################ finvHed table Details
     * ################################
     */

    public static final String TABLE_FINVHED = "finvHed";
    public static final String FINVHED_ID = "Id";

    public static final String FINVHED_REFNO1 = "RefNo1";

    public static final String FINVHED_MANUREF = "ManuRef";
    public static final String FINVHED_COSTCODE = "CostCode";
    public static final String FINVHED_CURCODE = "CurCode";
    public static final String FINVHED_CURRATE = "CurRate";
    public static final String FINVHED_DEBCODE = "DebCode";
    public static final String FINVHED_REMARKS = "Remarks";
    public static final String FINVHED_TXNTYPE = "TxnType";
    public static final String FINVHED_LOCCODE = "LocCode";
    public static final String FINVHED_PAYTYPE = "PayType";
    public static final String FINVHED_SETTING_CODE = "SettingCode";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-order discount table-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*/
    public static final String FINVHED_REPCODE = "RepCode";
    public static final String FINVHED_CONTACT = "Contact";
    public static final String FINVHED_CUSADD1 = "CusAdd1";
    public static final String FINVHED_CUSADD2 = "CusAdd2";
    public static final String FINVHED_CUSADD3 = "CusAdd3";
    public static final String FINVHED_CUSTELE = "CusTele";
    public static final String FINVHED_TOTALDIS = "TotalDis";
    public static final String FINVHED_TOTALTAX = "TotalTax";
    public static final String FINVHED_TOTALAMT = "TotalAmt";
    public static final String FINVHED_TAXREG = "TaxReg";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-fSMS table Details-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*/
    public static final String FINVHED_ADDUSER = "AddUser";
    public static final String FINVHED_ADDDATE = "AddDate";
    public static final String FINVHED_ADDMACH = "AddMach";
    public static final String FINVHED_START_TIME_SO = "startTime";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-finvHed table Details-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*/
    public static final String FINVHED_END_TIME_SO = "endTime";
    public static final String FINVHED_LONGITUDE = "Longitude";
    public static final String FINVHED_LATITUDE = "Latitude";
    public static final String FINVHED_ADDRESS = "Address";
    public static final String FINVHED_IS_SYNCED = "isSynced";
    public static final String FINVHED_IS_ACTIVE = "isActive";
    public static final String FINVHED_AREACODE = "areacode";
    public static final String FINVHED_ROUTECODE = "routecode";
    public static final String FINVHED_TOURCODE = "tourcode";
    public static final String TABLE_FINVDET = "finvDet";
    public static final String FINVDET_ID = "id";

    public static final String FINVDET_PICE_QTY = "PiceQty";
    public static final String FINVDET_TYPE = "Types";
    public static final String FINVDET_IS_ACTIVE = "isActive";
    public static final String FINVDET_AMT = "Amt";
    public static final String FINVDET_BAL_QTY = "BalQty";
    public static final String FINVDET_B_AMT = "BAmt";
    public static final String FINVDET_B_SELL_PRICE = "BSellPrice";
    public static final String FINVDET_BT_TAX_AMT = "BTaxAmt";
    public static final String FINVDET_BT_SELL_PRICE = "BTSellPrice";
    public static final String FINVDET_DIS_AMT = "DisAmt";
    public static final String FINVDET_DIS_PER = "DisPer";
    public static final String FINVDET_ITEM_CODE = "Itemcode";
    public static final String FINVDET_PRIL_CODE = "PrilCode";
    public static final String FINVDET_QTY = "Qty";
    public static final String FINVDET_RECORD_ID = "RecordId";
    public static final String FINVDET_SELL_PRICE = "SellPrice";
    public static final String FINVDET_SEQNO = "SeqNo";
    public static final String FINVDET_TAX_AMT = "TaxAmt";
    public static final String FINVDET_TAX_COM_CODE = "TaxComCode";
    public static final String FINVDET_T_SELL_PRICE = "TSellPrice";

    public static final String FINVDET_TXN_TYPE = "TxnType";
    public static final String FINVDET_FREEQTY = "FreeQty";
    public static final String FINVDET_COMDISPER = "ComDisPer";
    public static final String FINVDET_BRAND_DISPER = "BrandDisPer";

    /*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-finvDet table Details-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*/
    public static final String FINVDET_DISVALAMT = "DisValAmt";
    public static final String FINVDET_COMPDISC = "CompDisc";
    public static final String FINVDET_BRAND_DISC = "BrandDisc";
    public static final String FINVDET_QOH = "Qoh";
    public static final String FINVDET_SCHDISPER = "SchDisPer";
    public static final String FINVDET_PRICE = "Price";
    public static final String FINVDET_CHANGED_PRICE = "ChangedPrice";

    private static final String CREATE_FINVHED_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FINVHED + " (" + FINVHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FINVHED_REFNO1 + " TEXT, " + TXNDATE + " TEXT, " + FINVHED_PAYTYPE + " TEXT, " + FINVHED_SETTING_CODE + " TEXT, "+ FINVHED_MANUREF + " TEXT, " + FINVHED_COSTCODE + " TEXT, " + FINVHED_CURCODE + " TEXT, " + FINVHED_CURRATE + " TEXT, " + FINVHED_DEBCODE + " TEXT, " + FINVHED_REMARKS + " TEXT, " + FINVHED_TXNTYPE + " TEXT, " + FINVHED_LOCCODE + " TEXT, " + FINVHED_REPCODE + " TEXT, " + FINVHED_CONTACT + " TEXT, " + FINVHED_CUSADD1 + " TEXT, " + FINVHED_CUSADD2 + " TEXT, " + FINVHED_CUSADD3 + " TEXT, " + FINVHED_CUSTELE + " TEXT, " + FINVHED_TOTALDIS + " TEXT, " + FINVHED_TOTALTAX + " TEXT, " + FINVHED_TAXREG + " TEXT, " + FINVHED_ADDUSER + " TEXT, " + FINVHED_ADDDATE + " TEXT, " + FINVHED_ADDMACH + " TEXT, " + FINVHED_START_TIME_SO + " TEXT, " + FINVHED_END_TIME_SO + " TEXT, " + FINVHED_TOTALAMT + " TEXT, " + FINVHED_LONGITUDE + " TEXT, " + FINVHED_LATITUDE + " TEXT, " + FINVHED_ADDRESS + " TEXT, " + FINVHED_IS_SYNCED + " TEXT, " + FINVHED_AREACODE + " TEXT, " + FINVHED_ROUTECODE + " TEXT, " + FINVHED_TOURCODE + " TEXT, " + FINVHED_IS_ACTIVE + " TEXT); ";
    private static final String CREATE_FINVDET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FINVDET + " (" + FINVDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FINVDET_AMT + " TEXT, " + FINVDET_BAL_QTY + " TEXT, " + FINVDET_B_AMT + " TEXT, " + FINVDET_B_SELL_PRICE + " TEXT, " + FINVDET_BT_TAX_AMT + " TEXT, " + FINVDET_BT_SELL_PRICE + " TEXT, " + FINVDET_DIS_AMT + " TEXT, " + FINVDET_DIS_PER + " TEXT, " + FINVDET_ITEM_CODE + " TEXT, " + FINVDET_PRIL_CODE + " TEXT, " + FINVDET_QTY + " TEXT, " + FINVDET_PICE_QTY + " TEXT, " + FINVDET_TYPE + " TEXT, " + FINVDET_RECORD_ID + " TEXT, " + REFNO + " TEXT, " + FINVDET_SELL_PRICE + " TEXT, " + FINVDET_SEQNO + " TEXT, " + FINVDET_TAX_AMT + " TEXT, " + FINVDET_TAX_COM_CODE + " TEXT, " + FINVDET_T_SELL_PRICE + " TEXT, " + TXNDATE + " TEXT, " + FINVDET_IS_ACTIVE + " TEXT, " + FINVDET_TXN_TYPE + " TEXT," + FINVDET_COMDISPER + " TEXT DEFAULT '0'," + FINVDET_BRAND_DISPER + " TEXT DEFAULT '0'," + FINVDET_DISVALAMT + " TEXT DEFAULT '0'," + FINVDET_BRAND_DISC + " TEXT DEFAULT '0'," + FINVDET_QOH + " TEXT DEFAULT '0'," + FINVDET_FREEQTY + " TEXT DEFAULT '0'," + FINVDET_SCHDISPER + " TEXT DEFAULT '0',"
            + FINVDET_PRICE + " TEXT," + FINVDET_CHANGED_PRICE + " TEXT DEFAULT '0' ,"+ FINVDET_COMPDISC + " TEXT DEFAULT '0'); ";

    public static final String TABLE_INVTAXRG = "fInvTaxRg";
    public static final String INVTAXRG_ID = "Id";

    public static final String INVTAXRG_TAXCODE = "TaxCode";
    public static final String INVTAXRG_RGNO = "RGNo";
    public static final String CREATE_FINVTAXRG_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INVTAXRG + " (" + INVTAXRG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + INVTAXRG_TAXCODE + " TEXT, " + INVTAXRG_RGNO + " TEXT ); ";
    public static final String TABLE_INVTAXDT = "fInvTaxDT";
    public static final String INVTAXDT_ID = "Id";

    public static final String INVTAXDT_ITEMCODE = "ItemCode";
    public static final String INVTAXDT_TAXCOMCODE = "TaxComCode";
    public static final String INVTAXDT_TAXCODE = "TaxCode";
    public static final String INVTAXDT_TAXPER = "TaxPer";
    public static final String INVTAXDT_RATE = "TaxRate";
    public static final String INVTAXDT_SEQ = "TaxSeq";
    public static final String INVTAXDT_DETAMT = "TaxDetAmt";
    public static final String INVTAXDT_BDETAMT = "BTaxDetAmt";
    public static final String INVTAXDT_TAXTYPE = "TaxType";
    public static final String CREATE_FINVTAXDT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INVTAXDT + " (" + INVTAXDT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + INVTAXDT_ITEMCODE + " TEXT, " + INVTAXDT_TAXCOMCODE + " TEXT, " + INVTAXDT_TAXCODE + " TEXT, " + INVTAXDT_TAXPER + " TEXT, " + INVTAXDT_RATE + " TEXT, " + INVTAXDT_SEQ + " TEXT, " + INVTAXDT_DETAMT + " TEXT, " + INVTAXDT_TAXTYPE + " TEXT, " + INVTAXDT_BDETAMT + " TEXT ); ";
    public static final String TABLE_FTAX = "fTax";
    public static final String FTAX_ID = "Id";
    public static final String FTAX_TAXCODE = "TaxCode";
    public static final String FTAX_TAXNAME = "TaxName";
    public static final String FTAX_TAXPER = "TaxPer";
    public static final String FTAX_TAXREGNO = "TaxRegNo";
    public static final String CREATE_FTAX_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FTAX + " (" + FTAX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FTAX_TAXCODE + " TEXT, " + FTAX_TAXNAME + " TEXT, " + FTAX_TAXPER + " TEXT, " + FTAX_TAXREGNO + " TEXT ); ";
    public static final String TABLE_FTAXHED = "fTaxHed";
    public static final String FTAXHED_ID = "Id";
    public static final String FTAXHED_COMCODE = "TaxComCode";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-invoice TaxRG -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*/
    public static final String FTAXHED_COMNAME = "TaxComName";
    public static final String FTAXHED_ACTIVE = "Active";
    public static final String CREATE_FTAXHED_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FTAXHED + " (" + FTAXHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FTAXHED_COMCODE + " TEXT, " + FTAXHED_COMNAME + " TEXT, " + FTAXHED_ACTIVE + " TEXT ); ";
    public static final String TABLE_FTAXDET = "fTaxDet";
    public static final String FTAXDET_ID = "Id";
    public static final String FTAXDET_COMCODE = "TaxComCode";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-invoice TAX DT-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*/
    public static final String FTAXDET_TAXCODE = "TaxCode";
    public static final String FTAXDET_RATE = "Rate";
    public static final String FTAXDET_SEQ = "Seq";
    public static final String FTAXDET_TAXVAL = "TaxVal";
    public static final String FTAXDET_TAXTYPE = "TaxType";
    public static final String CREATE_FTAXDET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FTAXDET + " (" + FTAXDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FTAXDET_COMCODE + " TEXT, " + FTAXDET_TAXCODE + " TEXT, " + FTAXDET_RATE + " TEXT, " + FTAXDET_TAXVAL + " TEXT, " + FTAXDET_TAXTYPE + " TEXT, " + FTAXDET_SEQ + " TEXT ); ";
    /**
     * ############################ FCompanyBranch table Details
     * ################################
     */
    public static final String TABLE_DEBITEMPRI = "fDebItemPri";
    public static final String DEBITEMPRI_ID = "Id";
    public static final String DEBITEMPRI_BRANDCODE = "BrandCode";
    public static final String DEBITEMPRI_DEBCODE = "DebCode";
    public static final String DEBITEMPRI_DISPER = "Disper";

    private static final String CREATE_DEBITEMPRI_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_DEBITEMPRI + " (" + DEBITEMPRI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DEBITEMPRI_BRANDCODE + " TEXT, " + DEBITEMPRI_DEBCODE + " TEXT, " + DEBITEMPRI_DISPER + " TEXT); ";
    public static final String TABLE_FDISPHED = "FDispHed";
    public static final String FDISPHED_ID = "Id";

    public static final String FDISPHED_REFNO1 = "RefNo1";
    public static final String FDISPHED_MANUREF = "ManuRef";
    public static final String FDISPHED_TOTALAMT = "TotalAmt";
    public static final String FDISPHED_LOCCODE = "LocCode";
    public static final String FDISPHED_COSTCODE = "CostCode";
    public static final String FDISPHED_DEBCODE = "DebCode";
    public static final String FDISPHED_REPCODE = "RepCode";
    public static final String FDISPHED_REMARKS = "Remarks";
    public static final String FDISPHED_TXNTYPE = "TxnType";
    public static final String FDISPHED_INVOICE = "Invoice";
    public static final String FDISPHED_CONTACT = "Contact";
    public static final String FDISPHED_CUSADD1 = "CusAdd1";
    public static final String FDISPHED_CUSADD2 = "CusAdd2";
    public static final String FDISPHED_CUSADD3 = "CusAdd3";
    public static final String FDISPHED_CUSTELE = "CusTele";
    public static final String FDISPHED_ADDUSER = "AddUser";
    public static final String FDISPHED_ADDDATE = "AddDate";
    public static final String FDISPHED_ADDMACH = "AddMach";
    public static final String TABLE_FDISPDET = "FDispDet";
    public static final String FDISPDET_ID = "Id";

    public static final String FDISPDET_TXNTYPE = "TxnType";
    public static final String FDISPDET_ITEMCODE = "ItemCode";
    public static final String FDISPDET_QTY = "Qty";
    public static final String FDISPDET_BALQTY = "BalQty";
    public static final String FDISPDET_SAQTY = "SAQty";
    public static final String FDISPDET_BALSAQTY = "BalSAQty";
    public static final String FDISPDET_FIQTY = "FIQty";
    public static final String FDISPDET_BALFIQTY = "BalFIQty";
    public static final String FDISPDET_COSTPRICE = "CostPrice";
    public static final String FDISPDET_AMT = "Amt";
    public static final String FDISPDET_SEQNO = "SeqNo";
    public static final String FDISPDET_REFNO1 = "RefNo1";
    public static final String TABLE_FDISPISS = "FDispIss";
    public static final String FDISPISS_ID = "Id";
    public static final String FDISPISS_LOCCODE = "LocCode";
    public static final String FDISPISS_STKRECNO = "StkRecNo";
    ;
    public static final String FDISPISS_STKRECDATE = "StkRecDate";
    public static final String FDISPISS_STKTXNNO = "StkTxnNo";
    public static final String FDISPISS_STKTXNDATE = "StkTxnDate";
    public static final String FDISPISS_STKTXNTYPE = "StkTxnType";

    // --------------------------------------------------------------------------------------------------------------
    public static final String FDISPISS_ITEMCODE = "ItemCode";
    public static final String FDISPISS_QTY = "Qty";
    public static final String FDISPISS_BALQTY = "BalQty";
    public static final String FDISPISS_COSTPRICE = "CostPrice";
    public static final String FDISPISS_AMT = "Amt";
    public static final String FDISPISS_OTHCOST = "OthCost";
    public static final String FDISPISS_REFNO1 = "Refno1";

    private static final String CREATE_FDISPHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISPHED + " (" + FDISPHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT," + TXNDATE + " TEXT," + FDISPHED_REFNO1 + " TEXT," + FDISPHED_MANUREF + " TEXT," + FDISPHED_TOTALAMT + " TEXT," + FDISPHED_LOCCODE + " TEXT," + FDISPHED_COSTCODE + " TEXT," + FDISPHED_DEBCODE + " TEXT," + FDISPHED_REPCODE + " TEXT," + FDISPHED_REMARKS + " TEXT," + FDISPHED_TXNTYPE + " TEXT," + FDISPHED_INVOICE + " TEXT," + FDISPHED_CONTACT + " TEXT," + FDISPHED_CUSADD1 + " TEXT," + FDISPHED_CUSADD2 + " TEXT," + FDISPHED_CUSADD3 + " TEXT," + FDISPHED_CUSTELE + " TEXT," + FDISPHED_ADDUSER + " TEXT," + FDISPHED_ADDDATE + " TEXT," + FDISPHED_ADDMACH + " TEXT);";
    private static final String CREATE_FDISPDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISPDET + " (" + FDISPDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT ," + TXNDATE + " TEXT," + FDISPDET_TXNTYPE + " TEXT," + FDISPDET_ITEMCODE + " TEXT," + FDISPDET_QTY + " TEXT DEFAULT '0'," + FDISPDET_BALQTY + " TEXT DEFAULT '0'," + FDISPDET_SAQTY + " TEXT DEFAULT '0'," + FDISPDET_BALSAQTY + " TEXT DEFAULT '0'," + FDISPDET_FIQTY + " TEXT DEFAULT '0'," + FDISPDET_BALFIQTY + " TEXT DEFAULT '0'," + FDISPDET_COSTPRICE + " TEXT," + FDISPDET_AMT + " TEXT," + FDISPDET_REFNO1 + " TEXT," + FDISPDET_SEQNO + " TEXT);";
    private static final String CREATE_FDISPISS_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISPISS + " (" + FDISPISS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT," + TXNDATE + " TEXT," + FDISPISS_LOCCODE + " TEXT," + FDISPISS_STKRECNO + " TEXT," + FDISPISS_STKRECDATE + " TEXT," + FDISPISS_STKTXNNO + " TEXT," + FDISPISS_STKTXNDATE + " TEXT," + FDISPISS_STKTXNTYPE + " TEXT," + FDISPISS_ITEMCODE + " TEXT," + FDISPISS_QTY + " TEXT," + FDISPISS_BALQTY + " TEXT," + FDISPISS_COSTPRICE + " TEXT," + FDISPISS_AMT + " TEXT," + FDISPISS_REFNO1 + " TEXT," + FDISPISS_OTHCOST + " TEXT);";

    // table
    public static final String TABLE_FCOMPANYBRANCH = "FCompanyBranch";
    // table attributes
    public static final String FCOMPANYBRANCH_ID = "fcombra_id";
    public static final String FCOMPANYBRANCH_BRANCH_CODE = "BranchCode";
    public static final String FCOMPANYBRANCH_RECORD_ID = "RecordId";
    public static final String FCOMPANYBRANCH_CSETTINGS_CODE = "cSettingsCode";
    public static final String FCOMPANYBRANCH_NNUM_VAL = "nNumVal";
    public static final String FCOMPANYBRANCH_NYEAR_VAL = "nYear";
    public static final String FCOMPANYBRANCH_NMONTH_VAL = "nMonth";

    // create String
    private static final String CREATE_FCOMPANYBRANCH_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FCOMPANYBRANCH + " (" + FCOMPANYBRANCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FCOMPANYBRANCH_BRANCH_CODE + " TEXT, " + FCOMPANYBRANCH_RECORD_ID + " TEXT, " + FCOMPANYBRANCH_CSETTINGS_CODE + " TEXT, " + FCOMPANYBRANCH_NNUM_VAL + " TEXT, " + FCOMPANYBRANCH_NYEAR_VAL + " TEXT, " + FCOMPANYBRANCH_NMONTH_VAL + " TEXT); ";

    /**
     * ############################ fSalRep table Details
     * ################################
     */

    // table
    public static final String TABLE_FSALREP = "fSalRep";
    // table attributes
    public static final String FSALREP_ID = "fsalrep_id";
    public static final String FSALREP_ASE_CODE = "ASECode";
    public static final String FSALREP_AREA_CODE = "AreaCode";
    public static final String FSALREP_DEAL_CODE = "DealCode";
    public static final String FSALREP_RECORD_ID = "RecordId";
    public static final String FSALREP_REP_CODE = "RepCode";
    public static final String FSALREP_REP_PREFIX = "RepPrefix";
    public static final String FSALREP_REP_TCODE = "RepTCode";
    public static final String FSALREP_REP_PHONE_NO = "repPhoneNo";
    public static final String FSALREP_REP_NAME = "RepName";
    public static final String FSALREP_REP_EMAIL = "RepEMail";
    public static final String FSALREP_REP_MOB = "RepMob";
    public static final String FSALREP_PASSWORD = "Password";
    public static final String FSALREP_COSTCODE = "CostCode";// Password
    public static final String FSALREP_ADDMACH = "AddMach";
    public static final String FSALREP_ADDUSER = "AddUser";
    public static final String FSALREP_RECORDID = "RecordId";
    public static final String FSALREP_REPCODE = "RepCode";
    public static final String FSALREP_EMAIL = "Email";
    public static final String FSALREP_REPID = "RepId";
    public static final String FSALREP_MOBILE = "Mobile";
    public static final String FSALREP_LOCCODE = "LocCode";
    public static final String FSALREP_MACID = "macId";
    public static final String FSALREP_NAME = "name";
    public static final String FSALREP_PREFIX = "prefix";
    public static final String FSALREP_STATUS = "status";
    public static final String FSALREP_TELE = "telephone";
    public static final String FSALREP_PRILCODE = "prillcode";

    // create String
    private static final String CREATE_FSALREP_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FSALREP + " (" + FSALREP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FSALREP_ASE_CODE + " TEXT, "+ FSALREP_PRILCODE + " TEXT, " + FSALREP_AREA_CODE + " TEXT, " + FSALREP_DEAL_CODE + " TEXT, " + FSALREP_RECORD_ID + " TEXT, " + FSALREP_REP_CODE + " TEXT, " + FSALREP_REP_PREFIX + " TEXT, " + FSALREP_REP_TCODE + " TEXT, " + FSALREP_REP_PHONE_NO + " TEXT, " + FSALREP_REP_NAME + " TEXT, " + FSALREP_REP_EMAIL + " TEXT, " + FSALREP_REP_MOB + " TEXT, " + FSALREP_PASSWORD + " TEXT, "+ FSALREP_LOCCODE+ " TEXT, " + FSALREP_COSTCODE + " TEXT); ";

    /**
     * ############################ FDDbNote table Details
     * ################################
     */

    // table
    public static final String TABLE_FDDBNOTE = "FDDbNote";
    // table attributes
    public static final String FDDBNOTE_ID = "recinv_id";
    public static final String FDDBNOTE_RECORD_ID = "RecordId";

    public static final String FDDBNOTE_REF_INV = "RefInv";
    public static final String FDDBNOTE_SALE_REF_NO = "SaleRefNo";
    public static final String FDDBNOTE_MANU_REF = "ManuRef";
    public static final String FDDBNOTE_TXN_TYPE = "TxnType";

    public static final String FDDBNOTE_CUR_CODE = "CurCode";
    public static final String FDDBNOTE_CUR_RATE = "CurRate";
    public static final String FDDBNOTE_DEB_CODE = "DebCode";
    public static final String FDDBNOTE_REP_CODE = "RepCode";
    public static final String FDDBNOTE_TAX_COM_CODE = "TaxComCode";
    public static final String FDDBNOTE_TAX_AMT = "TaxAmt";
    public static final String FDDBNOTE_B_TAX_AMT = "BTaxAmt";
    public static final String FDDBNOTE_AMT = "Amt";
    public static final String FDDBNOTE_B_AMT = "BAmt";
    public static final String FDDBNOTE_TOT_BAL = "TotBal";
    public static final String FDDBNOTE_TOT_BAL1 = "TotBal1";
    public static final String FDDBNOTE_OV_PAY_AMT = "OvPayAmt";
    public static final String FDDBNOTE_REMARKS = "Remarks";
    public static final String FDDBNOTE_CR_ACC = "CrAcc";
    public static final String FDDBNOTE_PRT_COPY = "PrtCopy";
    public static final String FDDBNOTE_GL_POST = "GlPost";
    public static final String FDDBNOTE_GL_BATCH = "glbatch";
    public static final String FDDBNOTE_ADD_USER = "AddUser";
    public static final String FDDBNOTE_ADD_DATE = "AddDate";
    public static final String FDDBNOTE_ADD_MACH = "AddMach";
    public static final String FDDBNOTE_REFNO1 = "RefNo1";
    public static final String FDDBNOTE_REFNO2 = "RefNo2";
    public static final String FDDBNOTE_REPNAME = "RepName";
    public static final String FDDBNOTE_ENTER_AMT = "EnterAmt";
    public static final String FDDBNOTE_REMARK = "Remark";
    public static final String FDDBNOTE_ENT_REMARK = "EntRemark";
    public static final String FDDBNOTE_PDAAMT = "PdaAmt";
    public static final String FDDBNOTE_RECEIPT_TYPE = "ReceiptType";


    // create String
    private static final String CREATE_FDDBNOTE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FDDBNOTE + " (" + FDDBNOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FDDBNOTE_RECORD_ID + " TEXT, " +
            REFNO + " TEXT, " + FDDBNOTE_REFNO2 + " TEXT, " + FDDBNOTE_REPNAME + " TEXT, " +
            FDDBNOTE_RECEIPT_TYPE + " TEXT, " + FDDBNOTE_REMARK + " TEXT, " + FDDBNOTE_ENT_REMARK + " TEXT, " + FDDBNOTE_PDAAMT + " TEXT, " + FDDBNOTE_REF_INV + " TEXT, " + FDDBNOTE_ENTER_AMT + " TEXT, " + FDDBNOTE_SALE_REF_NO + " TEXT, " + FDDBNOTE_MANU_REF + " TEXT, " + FDDBNOTE_TXN_TYPE + " TEXT, " + TXNDATE + " TEXT, " + FDDBNOTE_CUR_CODE + " TEXT, " + FDDBNOTE_CUR_RATE + " TEXT, " + FDDBNOTE_DEB_CODE + " TEXT, " + FDDBNOTE_REP_CODE + " TEXT, " + FDDBNOTE_TAX_COM_CODE + " TEXT, " + FDDBNOTE_TAX_AMT + " TEXT, " + FDDBNOTE_B_TAX_AMT + " TEXT, " + FDDBNOTE_AMT + " TEXT, " + FDDBNOTE_B_AMT + " TEXT, " + FDDBNOTE_TOT_BAL + " TEXT, " + FDDBNOTE_TOT_BAL1 + " TEXT, " + FDDBNOTE_OV_PAY_AMT + " TEXT, " + FDDBNOTE_REMARKS + " TEXT, " + FDDBNOTE_CR_ACC + " TEXT, " + FDDBNOTE_PRT_COPY + " TEXT, " + FDDBNOTE_GL_POST + " TEXT, " + FDDBNOTE_GL_BATCH + " TEXT, " + FDDBNOTE_ADD_USER + " TEXT, " + FDDBNOTE_ADD_DATE + " TEXT, " + FDDBNOTE_ADD_MACH + " TEXT, " + FDDBNOTE_REFNO1 + " TEXT); ";

    private static final String TESTDDBNOTE = "CREATE UNIQUE INDEX IF NOT EXISTS idxddbnote_something ON " + TABLE_FDDBNOTE + " (" + REFNO + ")";

    /**
     * ############################ Ffreedeb table Details
     * ################################
     */

    // table
    public static final String TABLE_FFREEDEB = "Ffreedeb";
    // table attributes
    public static final String FFREEDEB_ID = "Ffreedeb_id";

    public static final String FFREEDEB_DEB_CODE = "Debcode";
    public static final String FFREEDEB_ADD_USER = "AddUser";
    public static final String FFREEDEB_ADD_DATE = "AddDate";
    public static final String FFREEDEB_ADD_MACH = "AddMach";
    public static final String FFREEDEB_RECORD_ID = "RecordId";
    public static final String FFREEDEB_TIMESTAMP_COLUMN = "timestamp_column";

    // create String
    private static final String CREATE_FFREEDEB_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FFREEDEB + " (" + FFREEDEB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FFREEDEB_DEB_CODE + " TEXT, " + FFREEDEB_ADD_USER + " TEXT, " + FFREEDEB_ADD_DATE + " TEXT, " + FFREEDEB_ADD_MACH + " TEXT, " + FFREEDEB_RECORD_ID + " TEXT, " + FFREEDEB_TIMESTAMP_COLUMN + " TEXT); ";

    private static final String TESTFREEDEB = "CREATE UNIQUE INDEX IF NOT EXISTS idxfreedeb_something ON " + TABLE_FFREEDEB + " (" + REFNO + "," + FFREEDEB_DEB_CODE + ")";

    /**
     * ############################ Ffreedet table Details
     * ################################
     */

    // table
    public static final String TABLE_FFREEDET = "Ffreedet";
    // table attributes
    public static final String FFREEDET_ID = "Ffreedet_id";

    public static final String FFREEDET_ITEM_CODE = "Itemcode";
    public static final String FFREEDET_RECORD_ID = "RecordId";

    // create String
    private static final String CREATE_FFREEDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FFREEDET + " (" + FFREEDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FFREEDET_ITEM_CODE + " TEXT, " + FFREEDET_RECORD_ID + " TEXT); ";

    private static final String IDXFREEDET = "CREATE UNIQUE INDEX IF NOT EXISTS idxfreedet_something ON " + TABLE_FFREEDET + " (" + REFNO + ", " + FFREEDET_ITEM_CODE + ")";

    /**
     * ############################ Ffreehed table Details
     * ################################
     */

    // table
    public static final String TABLE_FFREEHED = "Ffreehed";
    // table attributes
    public static final String FFREEHED_ID = "Ffreehed_id";


    public static final String FFREEHED_DISC_DESC = "DiscDesc";
    public static final String FFREEHED_PRIORITY = "Priority";
    public static final String FFREEHED_VDATEF = "Vdatef";
    public static final String FFREEHED_VDATET = "Vdatet";
    public static final String FFREEHED_REMARKS = "Remarks";
    public static final String FFREEHED_RECORD_ID = "RecordId";
    public static final String FFREEHED_ITEM_QTY = "ItemQty";
    public static final String FFREEHED_FREE_IT_QTY = "FreeItQty";
    public static final String FFREEHED_FTYPE = "Ftype";

    // create String
    private static final String CREATE_FFREEHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FFREEHED + " (" + FFREEHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FFREEHED_DISC_DESC + " TEXT, " + FFREEHED_PRIORITY + " TEXT, " + FFREEHED_VDATEF + " TEXT, " + FFREEHED_VDATET + " TEXT, " + FFREEHED_REMARKS + " TEXT, " + FFREEHED_RECORD_ID + " TEXT, " + FFREEHED_ITEM_QTY + " TEXT, " + FFREEHED_FREE_IT_QTY + " TEXT, " + FFREEHED_FTYPE + " TEXT); ";


    private static final String IDXFREEHED = "CREATE UNIQUE INDEX IF NOT EXISTS idxfreehed_something ON " + TABLE_FFREEHED + " (" + REFNO + ")";
    /**
     * ############################ FfreeSlab table Details
     * ################################
     */

    // table
    public static final String TABLE_FFREESLAB = "Ffreeslab";
    // table attributes
    public static final String FFREESLAB_ID = "Ffreeslab_id";

    public static final String FFREESLAB_QTY_F = "Qtyf";
    public static final String FFREESLAB_QTY_T = "Qtyt";
    public static final String FFREESLAB_FITEM_CODE = "fItemCode";
    public static final String FFREESLAB_FREE_QTY = "freeQty";
    public static final String FFREESLAB_ADD_USER = "AddUser";
    public static final String FFREESLAB_ADD_DATE = "AddDate";
    public static final String FFREESLAB_ADD_MACH = "AddMach";
    public static final String FFREESLAB_RECORD_ID = "RecordId";
    public static final String FFREESLAB_TIMESTAP_COLUMN = "timestamp_column";
    public static final String FFREESLAB_SEQ_NO = "seqno";

    // create String
    private static final String CREATE_FFREESLAB_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FFREESLAB + " (" + FFREESLAB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FFREESLAB_QTY_F + " TEXT, " + FFREESLAB_QTY_T + " TEXT, " + FFREESLAB_FITEM_CODE + " TEXT, " + FFREESLAB_FREE_QTY + " TEXT, " + FFREESLAB_ADD_USER + " TEXT, " + FFREESLAB_ADD_DATE + " TEXT, " + FFREESLAB_ADD_MACH + " TEXT, " + FFREESLAB_RECORD_ID + " TEXT, " + FFREESLAB_TIMESTAP_COLUMN + " TEXT, " + FFREESLAB_SEQ_NO + " TEXT); ";

    private static final String IDXFREESLAB = "CREATE UNIQUE INDEX IF NOT EXISTS idxfreeslab_something ON " + TABLE_FFREESLAB + " (" + REFNO + ", " + FFREESLAB_SEQ_NO + ")";

    /**
     * ############################ fFreeItem table Details
     * ################################
     */

    // table
    public static final String TABLE_FFREEITEM = "fFreeItem";
    // table attributes
    public static final String FFREEITEM_ID = "fFreeItem_id";

    public static final String FFREEITEM_ITEMCODE = "Itemcode";
    public static final String FFREEITEM_RECORD_ID = "RecordId";

    // create String
    private static final String CREATE_FFREEITEM_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FFREEITEM + " (" + FFREEITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FFREEITEM_ITEMCODE + " TEXT, " + FFREEITEM_RECORD_ID + " TEXT); ";

    private static final String IDXFREEITEM = "CREATE UNIQUE INDEX IF NOT EXISTS idxfreeitem_something ON " + TABLE_FFREEITEM + " (" + REFNO + ", " + FFREEITEM_ITEMCODE + ")";
    /**
     * ############################ fItem table Details
     * ################################
     */

    // table
    public static final String TABLE_FITEM = "fItem";
    // table attributes
    public static final String FITEM_ID = "fItem_id";
    public static final String FITEM_ADD_MATCH = "AddMach";
    public static final String FITEM_ADD_USER = "AddUser";
    public static final String FITEM_AVG_PRICE = "AvgPrice";
    public static final String FITEM_BRAND_CODE = "BrandCode";
    public static final String FITEM_GROUP_CODE = "GroupCode";
    public static final String FITEM_ITEM_CODE = "ItemCode";
    public static final String FITEM_ITEM_NAME = "ItemName";
    public static final String FITEM_ITEM_STATUS = "ItemStatus";
    public static final String FITEM_MUST_SALE = "MustSale";
    public static final String FITEM_NOU_CASE = "NOUCase";
    public static final String FITEM_ORD_SEQ = "OrdSeq";
    public static final String FITEM_PRIL_CODE = "PrilCode";
    public static final String FITEM_RE_ORDER_LVL = "ReOrderLvl";
    public static final String FITEM_RE_ORDER_QTY = "ReOrderQty";
    public static final String FITEM_TAX_COM_CODE = "TaxComCode";
    public static final String FITEM_TYPE_CODE = "TypeCode";
    public static final String FITEM_UNIT_CODE = "UnitCode";
    public static final String FITEM_VEN_P_CODE = "VenPcode";
    public static final String FITEM_CAT_CODE = "CatCode";
    public static final String FITEM_PACK = "Pack";
    public static final String FITEM_PACK_SIZE = "PackSize";
    public static final String FITEM_SUP_CODE = "SupCode";
    public static final String FITEM_MUST_FREE = "ChkMustFre";

    // create String
    private static final String CREATE_FITEM_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FITEM + " (" + FITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FITEM_ADD_MATCH + " TEXT, " + FITEM_ADD_USER + " TEXT, " + FITEM_AVG_PRICE + " TEXT, " + FITEM_BRAND_CODE + " TEXT, " + FITEM_GROUP_CODE + " TEXT, " + FITEM_ITEM_CODE + " TEXT, " + FITEM_ITEM_NAME + " TEXT, " + FITEM_ITEM_STATUS + " TEXT, " + FITEM_MUST_SALE + " TEXT, " + FITEM_NOU_CASE + " TEXT, " + FITEM_ORD_SEQ + " TEXT, " + FITEM_PRIL_CODE + " TEXT, " + FITEM_RE_ORDER_LVL + " TEXT, " + FITEM_RE_ORDER_QTY + " TEXT, " + FITEM_TAX_COM_CODE + " TEXT, " + FITEM_TYPE_CODE + " TEXT, " + FITEM_UNIT_CODE + " TEXT, " + FITEM_CAT_CODE + " TEXT, " + FITEM_PACK + " TEXT, " + FITEM_PACK_SIZE + " TEXT, " + FITEM_SUP_CODE + " TEXT, " + FITEM_VEN_P_CODE + " TEXT, " + FITEM_MUST_FREE + " TEXT); ";

    private static final String TESTITEM = "CREATE UNIQUE INDEX IF NOT EXISTS idxitem_something ON " + TABLE_FITEM + " (" + FITEM_ITEM_CODE + ")";

    /**
     * ############################ fItemLoc table Details
     * ################################
     */

    // table
    public static final String TABLE_FITEMLOC = "fItemLoc";
    // table attributes
    public static final String FITEMLOC_ID = "fItemLoc_id";
    public static final String FITEMLOC_ITEM_CODE = "ItemCode";
    public static final String FITEMLOC_LOC_CODE = "LocCode";
    public static final String FITEMLOC_QOH = "QOH";

    // create String
    private static final String CREATE_FITEMLOC_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FITEMLOC + " (" + FITEMLOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FITEMLOC_ITEM_CODE + " TEXT, " + FITEMLOC_LOC_CODE + " TEXT, " + FITEMLOC_QOH + " TEXT); ";

    private static final String TESTITEMLOC = "CREATE UNIQUE INDEX IF NOT EXISTS idxitemloc_something ON " + TABLE_FITEMLOC + " (" + FITEMLOC_ITEM_CODE + "," + FITEMLOC_LOC_CODE + ")";

    /**
     * ############################ fItemPri table Details
     * ################################
     */
    // table
    public static final String TABLE_FITEMPRI = "fItemPri";
    // table attributes
    public static final String FITEMPRI_ID = "fItemPri_id";
    public static final String FITEMPRI_ADD_MACH = "AddMach";
    public static final String FITEMPRI_ADD_USER = "AddUser";
    public static final String FITEMPRI_ITEM_CODE = "ItemCode";
    public static final String FITEMPRI_PRICE = "Price";
    public static final String FITEMPRI_PRIL_CODE = "PrilCode";
    public static final String FITEMPRI_TXN_MACH = "TxnMach";
    public static final String FITEMPRI_TXN_USER = "Txnuser";
    public static final String FITEMPRI_COST_CODE = "CostCode";

    // create String
    private static final String CREATE_FITEMPRI_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FITEMPRI + " (" + FITEMPRI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FITEMPRI_ADD_MACH + " TEXT, " + FITEMPRI_ADD_USER + " TEXT, " + FITEMPRI_ITEM_CODE + " TEXT, " + FITEMPRI_PRICE + " TEXT, " + FITEMPRI_PRIL_CODE + " TEXT, " + FITEMPRI_TXN_MACH + " TEXT, " + FITEMPRI_TXN_USER + " TEXT, " + FITEMPRI_COST_CODE + " TEXT); ";

    private static final String TESTITEMPRI = "CREATE UNIQUE INDEX IF NOT EXISTS idxitempri_something ON " + TABLE_FITEMPRI + " (" + FITEMPRI_ITEM_CODE + "," + FITEMPRI_PRIL_CODE + "," + FITEMPRI_COST_CODE + ")";

    /**
     * ############################ fArea table Details
     * ################################
     */
    // table
    public static final String TABLE_FAREA = "fArea";
    // table attributes
    public static final String FAREA_ID = "fArea_id";
    public static final String FAREA_ADD_MACH = "AddMach";
    public static final String FAREA_ADD_USER = "AddUser";
    public static final String FAREA_AREA_CODE = "AreaCode";
    public static final String FAREA_AREA_NAME = "AreaName";
    public static final String FAREA_DEAL_CODE = "DealCode";
    public static final String FAREA_REQ_CODE = "RegCode";

    // create String
    private static final String CREATE_FAREA_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FAREA + " (" + FAREA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FAREA_ADD_MACH + " TEXT, " + FAREA_ADD_USER + " TEXT, " + FAREA_AREA_CODE + " TEXT, " + FAREA_AREA_NAME + " TEXT, " + FAREA_DEAL_CODE + " TEXT, " + FAREA_REQ_CODE + " TEXT); ";
    /**
     * ############################ fLocations table Details
     * ################################
     */
    // table
    public static final String TABLE_FLOCATIONS = "fLocations";
    // table attributes
    public static final String FLOCATIONS_ID = "fLocations_id";
    public static final String FLOCATIONS_ADD_MACH = "AddMach";
    public static final String FLOCATIONS_ADD_USER = "AddUser";
    public static final String FLOCATIONS_LOC_CODE = "LocCode";
    public static final String FLOCATIONS_LOC_NAME = "LocName";
    public static final String FLOCATIONS_LOC_T_CODE = "LoctCode";
    public static final String FLOCATIONS_REP_CODE = "RepCode";
    public static final String FLOCATIONS_COST_CODE = "CostCode";

    // create String
    private static final String CREATE_FLOCATIONS_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FLOCATIONS + " (" + FLOCATIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FLOCATIONS_ADD_MACH + " TEXT, " + FLOCATIONS_ADD_USER + " TEXT, " + FLOCATIONS_LOC_CODE + " TEXT, " + FLOCATIONS_LOC_NAME + " TEXT, " + FLOCATIONS_LOC_T_CODE + " TEXT, " + FLOCATIONS_REP_CODE + " TEXT, " + FLOCATIONS_COST_CODE + " TEXT); ";
    /**
     * ############################ fDealer table Details
     * ################################
     */
    // table
    public static final String TABLE_FDEALER = "fDealer";
    // table attributes
    public static final String FDEALER_ID = "fDealer_id";
    public static final String FDEALER_ADD_DATE = "AddDate";
    public static final String FDEALER_ADD_MACH = "AddMach";
    public static final String FDEALER_ADD_USER = "AddUser";
    public static final String FDEALER_CUS_TYP_CODE = "CusTypCode";
    public static final String FDEALER_DEAL_ADD1 = "DealAdd1";
    public static final String FDEALER_DEAL_ADD2 = "DealAdd2";
    public static final String FDEALER_DEAL_ADD3 = "DealAdd3";
    public static final String FDEALER_DEAL_CODE = "DealCode";
    public static final String FDEALER_DEAL_EMAIL = "DealEMail";
    public static final String FDEALER_DEAL_GD_CODE = "DealGdCode";
    public static final String FDEALER_DEAL_MOB = "DealMob";
    public static final String FDEALER_DEAL_NAME = "DealName";
    public static final String FDEALER_DEAL_TELE = "DealTele";
    public static final String FDEALER_DISTANCE = "Distance";
    public static final String FDEALER_STATUS = "Status";
    public static final String FDEALER_PREFIX = "DealPreFix";
    // create String
    private static final String CREATE_FDEALER_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDEALER + " (" + FDEALER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FDEALER_ADD_DATE + " TEXT, " + FDEALER_ADD_MACH + " TEXT, " + FDEALER_ADD_USER + " TEXT, " + FDEALER_CUS_TYP_CODE + " TEXT, " + FDEALER_DEAL_ADD1 + " TEXT, " + FDEALER_DEAL_ADD2 + " TEXT, " + FDEALER_DEAL_ADD3 + " TEXT, " + FDEALER_DEAL_CODE + " TEXT, " + FDEALER_DEAL_EMAIL + " TEXT, " + FDEALER_DEAL_GD_CODE + " TEXT, " + FDEALER_DEAL_MOB + " TEXT, " + FDEALER_DEAL_NAME + " TEXT, " + FDEALER_DEAL_TELE + " TEXT, " + FDEALER_DISTANCE + " TEXT, " + FDEALER_STATUS + " TEXT," + FDEALER_PREFIX + " TEXT); ";

    /**
     * ############################ fMerch table Details
     * ################################
     */
    // table
    public static final String TABLE_FMERCH = "fMerch";
    // table attributes
    public static final String FMERCH_ID = "fMerch_id";
    public static final String FMERCH_CODE = "MerchCode";
    public static final String FMERCH_NAME = "MerchName";
    public static final String FMERCH_ADD_USER = "AddUser";
    public static final String FMERCH_ADD_DATE = "AddDate";
    public static final String FMERCH_ADD_MACH = "AddMach";
    public static final String FMERCH_RECORD_ID = "RecordId";
    public static final String FMERCH_TIMESTAMP_COLUMN = "timestamp_column";

    // create String
    private static final String CREATE_FMERCH_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FMERCH + " (" + FMERCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FMERCH_CODE + " TEXT, " + FMERCH_NAME + " TEXT, " + FMERCH_ADD_USER + " TEXT, " + FMERCH_ADD_DATE + " TEXT, " + FMERCH_ADD_MACH + " TEXT, " + FMERCH_RECORD_ID + " TEXT, " + FMERCH_TIMESTAMP_COLUMN + " TEXT); ";

    /**
     * ############################ FfreeMslab table Details
     * ################################
     */
    // table
    public static final String TABLE_FFREEMSLAB = "FfreeMslab";
    // table attributes
    public static final String FFREEMSLAB_ID = "FfreeMslab_id";

    public static final String FFREEMSLAB_QTY_F = "Qtyf";
    public static final String FFREEMSLAB_QTY_T = "Qtyt";
    public static final String FFREEMSLAB_ITEM_QTY = "ItemQty";
    public static final String FFREEMSLAB_FREE_IT_QTY = "FreeItQty";
    public static final String FFREEMSLAB_ADD_USER = "AddUser";
    public static final String FFREEMSLAB_ADD_DATE = "AddDate";
    public static final String FFREEMSLAB_ADD_MACH = "AddMach";
    public static final String FFREEMSLAB_RECORD_ID = "RecordId";
    public static final String FFREEMSLAB_TIMESTAMP_COLUMN = "timestamp_column";
    public static final String FFREEMSLAB_SEQ_NO = "seqno";

    // create String
    private static final String CREATE_FFREEMSLAB_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FFREEMSLAB + " (" + FFREEMSLAB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FFREEMSLAB_QTY_F + " TEXT, " + FFREEMSLAB_QTY_T + " TEXT, " + FFREEMSLAB_ITEM_QTY + " TEXT, " + FFREEMSLAB_FREE_IT_QTY + " TEXT, " + FFREEMSLAB_ADD_USER + " TEXT, " + FFREEMSLAB_ADD_DATE + " TEXT, " + FFREEMSLAB_ADD_MACH + " TEXT, " + FFREEMSLAB_RECORD_ID + " TEXT, " + FFREEMSLAB_TIMESTAMP_COLUMN + " TEXT, " + FFREEMSLAB_SEQ_NO + " TEXT); ";

    private static final String IDXFREEMSLAB = "CREATE UNIQUE INDEX IF NOT EXISTS idxfreemslab_something ON " + TABLE_FFREEMSLAB + " (" + REFNO + ", " + FFREEMSLAB_SEQ_NO + ")";
    /**
     * ############################ fRouteDet table Details
     * ################################
     */
    // table
    public static final String TABLE_FROUTEDET = "fRouteDet";
    // table attributes
    public static final String FROUTEDET_ID = "fRouteDet_id";
    public static final String FROUTEDET_DEB_CODE = "DebCode";
    public static final String FROUTEDET_ROUTE_CODE = "RouteCode";

    // create String
    private static final String CREATE_FROUTEDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FROUTEDET + " (" + FROUTEDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FROUTEDET_DEB_CODE + " TEXT, " + FROUTEDET_ROUTE_CODE + " TEXT); ";

    private static final String TESTROUTEDET = "CREATE UNIQUE INDEX IF NOT EXISTS idxrutdet_something ON " + TABLE_FROUTEDET + " (" + FROUTEDET_DEB_CODE + "," + FROUTEDET_ROUTE_CODE + ")";

    /**
     * ############################ FDiscvhed table Details
     * ################################
     */
    // table
    public static final String TABLE_FDISCVHED = "FDiscvhed";
    // table attributes
    public static final String FDISCVHED_ID = "FDiscvhed_id";

    public static final String FDISCVHED_DISC_DESC = "DiscDesc";
    public static final String FDISCVHED_PRIORITY = "Priority";
    public static final String FDISCVHED_DIS_TYPE = "DisType";
    public static final String FDISCVHED_V_DATE_F = "Vdatef";
    public static final String FDISCVHED_V_DATE_T = "Vdatet";
    public static final String FDISCVHED_REMARKS = "Remarks";

    // create String
    private static final String CREATE_FDISCVHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISCVHED + " (" + FDISCVHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FDISCVHED_DISC_DESC + " TEXT, " + FDISCVHED_PRIORITY + " TEXT, " + FDISCVHED_DIS_TYPE + " TEXT, " + FDISCVHED_V_DATE_F + " TEXT, " + FDISCVHED_V_DATE_T + " TEXT, " + FDISCVHED_REMARKS + " TEXT); ";
    /**
     * ############################ Fdiscvdet table Details
     * ################################
     */
    // table
    public static final String TABLE_FDISCVDET = "Fdiscvdet";
    // table attributes
    public static final String FDISCVDET_ID = "FDiscvdet_id";

    public static final String FDISCVDET_VALUE_F = "Valuef";
    public static final String FDISCVDET_VALUE_T = "Valuet";
    public static final String FDISCVDET_DISPER = "Disper";
    public static final String FDISCVDET_DIS_AMT = "Disamt";

    // create String
    private static final String CREATE_FDISCVDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISCVDET + " (" + FDISCVDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FDISCVDET_VALUE_F + " TEXT, " + FDISCVDET_VALUE_T + " TEXT, " + FDISCVDET_DISPER + " TEXT, " + FDISCVDET_DIS_AMT + " TEXT); ";

    /**
     * ############################ FDiscvdeb table Details
     * ################################
     */
    // table
    public static final String TABLE_FDISCVDEB = "FDiscvdeb";
    // table attributes
    public static final String FDISCVDEB_ID = "FDiscvdet_id";

    public static final String FDISCVDED_DEB_CODE = "Debcode";

    // create String
    private static final String CREATE_FDISCVDEB_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISCVDEB + " (" + FDISCVDEB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FDISCVDED_DEB_CODE + " TEXT); ";

    /**
     * ############################ fdisched table Details
     * ################################
     */
    // table
    public static final String TABLE_FDISCHED = "fdisched";
    // table attributes
    public static final String FDISCHED_ID = "fdisched_id";


    public static final String FDISCHED_DISC_DESC = "DiscDesc";
    public static final String FDISCHED_PRIORITY = "Priority";
    public static final String FDISCHED_DIS_TYPE = "DisType";
    public static final String FDISCHED_V_DATE_F = "Vdatef";
    public static final String FDISCHED_V_DATE_T = "Vdatet";
    public static final String FDISCHED_REMARK = "Remarks";
    public static final String FDISCHED_ADD_USER = "AddUser";
    public static final String FDISCHED_ADD_DATE = "AddDate";
    public static final String FDISCHED_ADD_MACH = "AddMach";
    public static final String FDISCHED_RECORD_ID = "RecordId";
    public static final String FDISCHED_TIMESTAMP_COLUMN = "timestamp_column";
    // create String
    private static final String CREATE_FDISCHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISCHED + " (" + FDISCHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FDISCHED_DISC_DESC + " TEXT, " + FDISCHED_PRIORITY + " TEXT, " + FDISCHED_DIS_TYPE + " TEXT, " + FDISCHED_V_DATE_F + " TEXT, " + FDISCHED_V_DATE_T + " TEXT, " + FDISCHED_REMARK + " TEXT, " + FDISCHED_ADD_USER + " TEXT, " + FDISCHED_ADD_DATE + " TEXT, " + FDISCHED_ADD_MACH + " TEXT, " + FDISCHED_RECORD_ID + " TEXT, " + FDISCHED_TIMESTAMP_COLUMN + " TEXT); ";

    /**
     * ############################ FDiscdet table Details
     * ################################
     */

    // table
    public static final String TABLE_FDISCDET = "FDiscdet";
    // table attributes
    public static final String FDISCDET_ID = "FDiscdet_id";

    public static final String FDISCDET_ITEM_CODE = "itemcode";
    public static final String FDISCDET_RECORD_ID = "RecordId";
    public static final String FDISCHED_TIEMSTAMP_COLUMN = "timestamp_column";

    // create String
    private static final String CREATE_FDISCDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISCDET + " (" + FDISCDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FDISCDET_ITEM_CODE + " TEXT, " + FDISCDET_RECORD_ID + " TEXT, " + FDISCHED_TIEMSTAMP_COLUMN + " TEXT); ";

    /**
     * ############################ FDiscdeb table Details
     * ################################
     */

    // table
    public static final String TABLE_FDISCDEB = "FDiscdeb";
    // table attributes
    public static final String FDISCDEB_ID = "FDiscdet_id";

    public static final String FDISCDEB_DEB_CODE = "debcode";
    public static final String FDISCDEB_RECORD_ID = "RecordId";
    public static final String FDISCDEB_TIEMSTAMP_COLUMN = "timestamp_column";

    // create String
    private static final String CREATE_FDISCDEB_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISCDEB + " (" + FDISCDEB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FDISCDEB_DEB_CODE + " TEXT, " + FDISCDET_RECORD_ID + " TEXT, " + FDISCHED_TIEMSTAMP_COLUMN + " TEXT); ";

    /**
     * ############################ FDiscslab table Details
     * ################################
     */

    // table
    public static final String TABLE_FDISCSLAB = "FDiscslab";
    // table attributes
    public static final String FDISCSLAB_ID = "FDiscdet_id";

    public static final String FDISCSLAB_SEQ_NO = "seqno";
    public static final String FDISCSLAB_QTY_F = "Qtyf";
    public static final String FDISCSLAB_QTY_T = "Qtyt";
    public static final String FDISCSLAB_DIS_PER = "disper";
    public static final String FDISCSLAB_DIS_AMUT = "disamt";
    public static final String FDISCSLAB_RECORD_ID = "RecordId";
    public static final String FDISCSLAB_TIMESTAMP_COLUMN = "timestamp_column";

    // create String
    private static final String CREATE_FDISCSLAB_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDISCSLAB + " (" + FDISCSLAB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FDISCSLAB_SEQ_NO + " TEXT, " + FDISCSLAB_QTY_F + " TEXT, " + FDISCSLAB_QTY_T + " TEXT, " + FDISCSLAB_DIS_PER + " TEXT, " + FDISCSLAB_DIS_AMUT + " TEXT, " + FDISCSLAB_RECORD_ID + " TEXT, " + FDISCSLAB_TIMESTAMP_COLUMN + " TEXT); ";

    /**
     * ############################ FItenrHed table Details
     * ################################
     */

    // table
    public static final String TABLE_FITENRHED = "FItenrHed";
    // table attributes
    public static final String FITENRHED_ID = "FItenrHed_id";
    public static final String FITENRHED_COST_CODE = "CostCode";
    public static final String FITENRHED_DEAL_CODE = "DealCode";
    public static final String FITENRHED_MONTH = "Month";

    public static final String FITENRHED_REMARKS1 = "Remarks1";
    public static final String FITENRHED_REP_CODE = "RepCode";
    public static final String FITENRHED_YEAR = "Year";

    // create String
    private static final String CREATE_FITENRHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FITENRHED + " (" + FITENRHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FITENRHED_COST_CODE + " TEXT, " + FITENRHED_DEAL_CODE + " TEXT, " + FITENRHED_MONTH + " TEXT, " + REFNO + " TEXT, " + FITENRHED_REMARKS1 + " TEXT, " + FITENRHED_REP_CODE + " TEXT, " + FITENRHED_YEAR + " TEXT); ";

    /**
     * ############################ FItenrDet table Details
     * ################################
     */

    // table
    public static final String TABLE_FITENRDET = "FItenrDet";
    // table attributes
    public static final String FITENRDET_ID = "FItenrDet_id";
    public static final String FITENRDET_NO_OUTLET = "NoOutlet";
    public static final String FITENRDET_NO_SHCUCAL = "NoShcuCal";
    public static final String FITENRDET_RD_TARGET = "RDTarget";

    public static final String FITENRDET_REMARKS = "Remarks";
    public static final String FITENRDET_ROUTE_CODE = "RouteCode";


    // create String
    private static final String CREATE_FITENRDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FITENRDET + " (" + FITENRDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FITENRDET_NO_OUTLET + " TEXT, " + FITENRDET_NO_SHCUCAL + " TEXT, " + FITENRDET_RD_TARGET + " TEXT, " + REFNO + " TEXT, " + FITENRDET_REMARKS + " TEXT, " + FITENRDET_ROUTE_CODE + " TEXT, " + TXNDATE + " TEXT); ";
    /**
     * ############################ FIteDebDet table Details
     * ################################
     */

    // table
    public static final String TABLE_FITEDEBDET = "FIteDebDet";
    // table attributes
    public static final String FITEDEBDET_ID = "FItenrDet_id";
    public static final String FITEDEBDET_DEB_CODE = "DebCode";

    public static final String FITEDEBDET_ROUTE_CODE = "RouteCode";


    // create String
    private static final String CREATE_FITEDEBDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FITEDEBDET + " (" + FITEDEBDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FITEDEBDET_DEB_CODE + " TEXT, " + REFNO + " TEXT, " + FITEDEBDET_ROUTE_CODE + " TEXT, " + TXNDATE + " TEXT); ";

    /**
     * ############################ FinvHedL3 table Details
     * ################################
     */

    // table
    public static final String TABLE_FINVHEDL3 = "FinvHedL3";
    // table attributes
    public static final String FINVHEDL3_ID = "FinvHedL3_id";
    public static final String FINVHEDL3_DEB_CODE = "DebCode";
    public static final String FINVHEDL3_REF_NO1 = "RefNo1";

    public static final String FINVHEDL3_TOTAL_AMT = "TotalAmt";
    public static final String FINVHEDL3_TOTAL_TAX = "TotalTax";


    // create String
    private static final String CREATE_FINVHEDL3_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FINVHEDL3 + " (" + FINVHEDL3_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FINVHEDL3_DEB_CODE + " TEXT, " + REFNO + " TEXT, " + FINVHEDL3_REF_NO1 + " TEXT, " + FINVHEDL3_TOTAL_AMT + " TEXT, " + FINVHEDL3_TOTAL_TAX + " TEXT, " + TXNDATE + " TEXT); ";

    private static final String TESTINVHEDL3 = "CREATE UNIQUE INDEX IF NOT EXISTS idxinvhedl3_something ON " + TABLE_FINVHEDL3 + " (" + REFNO + ")";

    /**
     * ############################ FinvHedL3 table Details
     * ################################
     */

    // table
    public static final String TABLE_FINVDETL3 = "FinvDetL3";
    // table attributes
    public static final String FINVDETL3_ID = "FinvDetL3_id";
    public static final String FINVDETL3_AMT = "Amt";
    public static final String FINVDETL3_ITEM_CODE = "ItemCode";
    public static final String FINVDETL3_QTY = "Qty";

    public static final String FINVDETL3_SEQ_NO = "SeqNo";
    public static final String FINVDETL3_TAX_AMT = "TaxAmt";
    public static final String FINVDETL3_TAX_COM_CODE = "TaxComCode";


    // create String
    private static final String CREATE_FINVDETL3_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FINVDETL3 + " (" + FINVDETL3_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FINVDETL3_AMT + " TEXT, " + FINVDETL3_ITEM_CODE + " TEXT, " + FINVDETL3_QTY + " TEXT, " + REFNO + " TEXT, " + FINVDETL3_SEQ_NO + " TEXT, " + FINVDETL3_TAX_AMT + " TEXT, " + FINVDETL3_TAX_COM_CODE + " TEXT, " + TXNDATE + " TEXT); ";

    private static final String TESTINVDETL3 = "CREATE UNIQUE INDEX IF NOT EXISTS idxinvdetl3_something ON " + TABLE_FINVDETL3 + " (" + REFNO + "," + FINVDETL3_ITEM_CODE + ")";

    /**
     * ############################ FTranDet ################################
     */
    public static final String TABLE_FTRANDET = "FTranDet";
    // table attributes
    public static final String FTRANDET_ID = "FTranDet_id";


    public static final String FTRANDET_LOCCODE = "LocCode";
    public static final String FTRANDET_TXNTYPE = "TxnType";
    public static final String FTRANDET_SEQNO = "SeqNo";
    public static final String FTRANDET_ITEMCODE = "ItemCode";
    public static final String FTRANDET_QTY = "Qty";
    public static final String FTRANDET_AMT = "Amt";

    public static final String FTRANDET_CASEQTY = "CaseQty";
    public static final String FTRANDET_PICEQTY = "PiceQty";
    public static final String REMQTY = "remqty";

    // create String
    private static final String CREATE_FTRANDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FTRANDET + " (" + FTRANDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FTRANDET_LOCCODE + " TEXT, " + FTRANDET_TXNTYPE + " TEXT, " + FTRANDET_SEQNO + " TEXT, " + FTRANDET_ITEMCODE + " TEXT, " + FTRANDET_QTY + " TEXT, " + FTRANDET_AMT + " TEXT," + FTRANDET_CASEQTY + " TEXT," + FTRANDET_PICEQTY + " TEXT," + REMQTY + " TEXT); ";

    /**
     * ############################ FTranHed ################################
     */

    public static final String TABLE_FTRANHED = "FTranHed";
    // table attributes
    public static final String FTRANHED_ID = "FTranHed_id";


    public static final String FTRANHED_MANUREF = "ManuRef";
    public static final String FTRANHED_COSTCODE = "CostCode";
    public static final String FTRANHED_REMARKS = "Remarks";
    public static final String FTRANHED_TXNTYPE = "TxnType";
    public static final String FTRANHED_TOTALAMT = "TotalAmt";
    public static final String FTRANHED_DELPERSN = "DelPerson";
    public static final String FTRANHED_DELADD1 = "DelAdd1";
    public static final String FTRANHED_DELADD2 = "DelAdd2";
    public static final String FTRANHED_DELADD3 = "DelAdd3";
    public static final String FTRANHED_VEHICALNO = "VehicalNo";
    public static final String FTRANHED_PRTCOPY = "PrtCopy";
    public static final String FTRANHED_GLPOST = "GlPost";
    public static final String FTRANHED_GLBATCH = "GlBatch";
    public static final String FTRANHED_ADDUSER = "AddUser";
    public static final String FTRANHED_ADDDATE = "AddDate";

    public static final String FTRANHED_ADDMACH = "AddMach";
    public static final String FTRANHED_DEALCODE = "DealCode";

    public static final String FTRANHED_LONGITUDE = "Longitude";
    public static final String FTRANHED_LATITUDE = "Latitude";
    public static final String FTRANHED_LOCFROM = "Locfrom";
    public static final String FTRANHED_LOCTO = "Locto";
    public static final String FTRANHED_IS_SYNCED = "issync";
    public static final String FTRANHED_ACTIVE_STATE = "ActiveState";

    // create String
    private static final String CREATE_FTRANHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FTRANHED + " (" + FTRANHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FTRANHED_MANUREF + " TEXT, " + FTRANHED_COSTCODE + " TEXT, " + FTRANHED_REMARKS + " TEXT, " + FTRANHED_TXNTYPE + " TEXT, " + FTRANHED_TOTALAMT + " TEXT, " + FTRANHED_DELPERSN + " TEXT," + FTRANHED_DELADD1 + " TEXT," + FTRANHED_DELADD2 + " TEXT, " + FTRANHED_DELADD3 + " TEXT, " + FTRANHED_VEHICALNO + " TEXT, " + FTRANHED_PRTCOPY + " TEXT, " + FTRANHED_GLPOST + " TEXT, " + FTRANHED_GLBATCH + " TEXT, " + FTRANHED_ADDUSER + " TEXT," + FTRANHED_ADDDATE + " TEXT," + FTRANHED_ADDMACH + " TEXT," + FTRANHED_DEALCODE + " TEXT," + FTRANHED_LONGITUDE + " TEXT," + FTRANHED_LATITUDE + " TEXT," + FTRANHED_LOCFROM + " TEXT," + FTRANHED_LOCTO + " TEXT," + FTRANHED_IS_SYNCED + " TEXT," + FTRANHED_ACTIVE_STATE + " TEXT); ";

    /**
     * ############################ FTranIss ################################
     */

    public static final String TABLE_FTRANISS = "FTranIss";
    // table attributes
    public static final String FTRANISS_ID = "FTranHed_id";


    public static final String FTRANISS_STKRECNO = "StkreCno";
    public static final String FTRANISS_STKRECDATE = "StkrecDate";
    public static final String FTRANISS_STKTXNNO = "StkTxnNo";
    public static final String FTRANISS_STKTXNDATE = "StkTxnDate";
    public static final String FTRANISS_STKTXNTYPE = "StkTxnType";
    public static final String FTRANISS_LOCCODE = "LocCode";
    public static final String FTRANISS_ITEMCODE = "ItemCode";
    public static final String FTRANISS_QTY = "Qty";
    public static final String FTRANISS_COSTPRICE = "CostPrice";
    public static final String FTRANISS_AMT = "Amt";
    public static final String FTRANISS_OTHCOST = "OthCost";

    // create String
    private static final String CREATE_FTRANISS_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FTRANISS + " (" + FTRANISS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FTRANISS_STKRECNO + " TEXT, " + FTRANISS_STKRECDATE + " TEXT, " + FTRANISS_STKTXNNO + " TEXT, " + FTRANISS_STKTXNDATE + " TEXT, " + FTRANISS_STKTXNTYPE + " TEXT, " + FTRANISS_LOCCODE + " TEXT," + FTRANISS_ITEMCODE + " TEXT," + FTRANISS_QTY + " TEXT, " + FTRANISS_COSTPRICE + " TEXT, " + FTRANISS_AMT + " TEXT, " + FTRANISS_OTHCOST + " TEXT); ";

    /**
     * ############################ FDaynonprdHed
     * ################################
     */



    public static final String TABLE_FDAMHED = "FDamHed";
    // table attributes
    public static final String FDAMHED_ID = "FDamHed_id";


    public static final String FDAMHED_MANUREF = "ManuRef";
    public static final String FDAMHED_DEALCODE = "DealCode";
    public static final String FDAMHED_COSTCODE = "CostCode";
    public static final String FDAMHED_REMARKS = "Remarks";
    public static final String FDAMHED_TXNTYPE = "TxnType";
    public static final String FDAMHED_TOTALAMT = "TotalAmt";
    public static final String FDAMHED_REACODE = "ReaCode";
    public static final String FDAMHED_ITEMTYPE = "ItemType";
    public static final String FDAMHED_GLPOST = "GlPost";
    public static final String FDAMHED_GLBATCH = "GlBatch";
    public static final String FDAMHED_ADDUSER = "AddUser";
    public static final String FDAMHED_ADDDATE = "AddDate";

    public static final String FDAMHED_ADDMACH = "AddMach";

    public static final String FDAMHED_LOCFROM = "Locfrom";
    public static final String FDAMHED_LOCTO = "Locto";
    public static final String FDAMHED_IS_SYNCED = "issync";
    public static final String FDAMHED_ACTIVE_STATE = "ActiveState";

    // create String
    private static final String CREATE_FDAMHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDAMHED + " (" + FDAMHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FDAMHED_MANUREF + " TEXT, " + FDAMHED_COSTCODE + " TEXT, " + FDAMHED_REMARKS + " TEXT, " + FDAMHED_TXNTYPE + " TEXT, " + FDAMHED_TOTALAMT + " TEXT, " + FDAMHED_REACODE + " TEXT, " + FDAMHED_ITEMTYPE + " TEXT, " + FDAMHED_GLPOST + " TEXT, " + FDAMHED_GLBATCH + " TEXT, " + FDAMHED_ADDUSER + " TEXT," + FDAMHED_ADDDATE + " TEXT," + FDAMHED_ADDMACH + " TEXT," + FDAMHED_DEALCODE + " TEXT," + FDAMHED_LOCFROM + " TEXT," + FDAMHED_LOCTO + " TEXT," + FDAMHED_IS_SYNCED + " TEXT," + FDAMHED_ACTIVE_STATE + " TEXT); ";

    /**
     * ############################ FDamDet ################################
     */
    public static final String TABLE_FDAMDET = "FDamDet";
    // table attributes
    public static final String FDAMDET_ID = "FTranDet_id";


    public static final String FDAMDET_LOCCODE = "LocCode";
    public static final String FDAMDET_TXNTYPE = "TxnType";
    public static final String FDAMDET_SEQNO = "SeqNo";
    public static final String FDAMDET_ITEMCODE = "ItemCode";
    public static final String FDAMDET_REACODE = "ReaCode";
    public static final String FDAMDET_REANAME = "ReaName";
    public static final String FDAMDET_QTY = "Qty";
    public static final String FDAMDET_AMT = "Amt";
    public static final String FDAMDET_CASEQTY = "CaseQty";
    public static final String FDAMDET_PICEQTY = "PiceQty";
    public static final String REMQTYDAM = "remqty";

    // create String
    private static final String CREATE_FDAMDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDAMDET + " (" + FDAMDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FDAMDET_LOCCODE + " TEXT, " + FDAMDET_TXNTYPE + " TEXT, " + FDAMDET_SEQNO + " TEXT, " + FDAMDET_ITEMCODE + " TEXT, " + FDAMDET_REACODE + " TEXT, " + FDAMDET_REANAME + " TEXT, " + FDAMDET_QTY + " TEXT, " + FDAMDET_AMT + " TEXT," + FDAMDET_CASEQTY + " TEXT," + FDAMDET_PICEQTY + " TEXT," + REMQTYDAM + " TEXT); ";

    /**
     * ############################ FAdjHed ################################
     */

    public static final String TABLE_FADJHED = "FAdjHed";
    // table attributes
    public static final String FADJHED_ID = "FAdjHed_id";


    public static final String FADJHED_COSTCODE = "CostCode";
    public static final String FADJHED_MANUREF = "ManuRef";
    public static final String FADJHED_REFNO2 = "RefNo2";
    public static final String FADJHED_TXNTYPE = "TxnType";
    public static final String FADJHED_TOTALAMT = "TotalAmt";
    public static final String FADJHED_REMARKS = "Remarks";
    public static final String FADJHED_LOCCODE = "LocCode";
    public static final String FADJHED_DEALCODE = "DealCode";
    public static final String FADJHED_REACODE = "ReaCode";
    public static final String FADJHED_GLPOST = "GlPost";
    public static final String FADJHED_GLBATCH = "GlBatch";
    public static final String FADJHED_ADDUSER = "AddUser";
    public static final String FADJHED_ADDDATE = "AddDate";
    public static final String FADJHED_ADDMACH = "AddMach";

    public static final String FADJHED_IS_SYNCED = "issync";
    public static final String FADJHED_ACTIVE_STATE = "ActiveState";

    // create String
    private static final String CREATE_FADJHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FADJHED + " (" + FADJHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FADJHED_COSTCODE + " TEXT, " + FADJHED_MANUREF + " TEXT, " + FADJHED_REFNO2 + " TEXT, " + FADJHED_TXNTYPE + " TEXT, " + FADJHED_TOTALAMT + " TEXT, " + FADJHED_REMARKS + " TEXT, " + FADJHED_LOCCODE + " TEXT, " + FADJHED_DEALCODE + " TEXT, " + FADJHED_REACODE + " TEXT, " + FADJHED_GLPOST + " TEXT, " + FADJHED_GLBATCH + " TEXT, " + FADJHED_ADDUSER + " TEXT," + FADJHED_ADDDATE + " TEXT," + FADJHED_ADDMACH + " TEXT," + FADJHED_IS_SYNCED + " TEXT," + FADJHED_ACTIVE_STATE + " TEXT); ";

    /**
     * ############################ FAdjDet ################################
     */
    public static final String TABLE_FADJDET = "FAdjDet";
    // table attributes
    public static final String FADJDET_ID = "FTranDet_id";


    public static final String FADJDET_TXNTYPE = "TxnType";
    public static final String FADJDET_SEQNO = "SeqNo";
    public static final String FADJDET_ITEMCODE = "ItemCode";
    public static final String FADJDET_QTY = "Qty";
    public static final String FADJDET_CASEQTY = "CaseQty";
    public static final String FADJDET_PICEQTY = "PiceQty";
    public static final String FADJDET_COSTPRICE = "CostPrice";
    public static final String FADJDET_AMT = "Amt";
    public static final String FADJDET_REACODE = "ReaCode";
    public static final String FADJDET_REANAME = "ReaName";

    // create String
    private static final String CREATE_FADJDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FADJDET + " (" + FADJDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FADJDET_TXNTYPE + " TEXT, " + FADJDET_SEQNO + " TEXT, " + FADJDET_ITEMCODE + " TEXT, " + FADJDET_QTY + " TEXT, " + FADJDET_CASEQTY + " TEXT," + FADJDET_PICEQTY + " TEXT," + FADJDET_COSTPRICE + " TEXT, " + FADJDET_AMT + " TEXT, " + FADJDET_REACODE + " TEXT, " + FADJDET_REANAME + " TEXT ); ";

    /**
     * ############################ FDayExpHed ################################
     * ############################ FDayExpDet ################################
     */
    public static final String TABLE_FDAYEXPDET = "FDayExpDet";
    // table attributes
    public static final String FDAYEXPDET_ID = "FDayExpDet_id";

    public static final String FDAYEXPDET_SEQNO = "SeqNo";
    public static final String FDAYEXPDET_EXPCODE = "ExpCode";
    public static final String FDAYEXPDET_AMT = "Amt";
    // create String
    private static final String CREATE_FDAYEXPDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDAYEXPDET + " (" + FDAYEXPDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TXNDATE + " TEXT, " + FDAYEXPDET_SEQNO + " TEXT, " + FDAYEXPDET_EXPCODE + " TEXT, " + FDAYEXPDET_AMT + " TEXT); ";
    /**
     * ############################ FInvRHed ################################
     */
    public static final String TABLE_FINVRHED = "FInvRHed";
    public static final String FINVRHED_ID = "id";// 1

    public static final String FINVRHED_MANUREF = "ManuRef";

    public static final String FINVRHED_COSTCODE = "CostCode";
    public static final String FINVRHED_DEBCODE = "DebCode";
    public static final String FINVRHED_REMARKS = "Remarks";
    public static final String FINVRHED_TXNTYPE = "TxnType";
    public static final String FINVRHED_INV_REFNO= "InvRefNo";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public static final String FINVRHED_LOCCODE = "LOCCode";
    public static final String FINVRHED_REPCODE = "RepCode";
    public static final String FINVRHED_REASON_CODE = "ReasonCode";
    public static final String FINVRHED_TOTAL_TAX = "TotalTax";
    public static final String FINVRHED_TOTAL_AMT = "TotalAmt";
    public static final String FINVRHED_TOTAL_DIS = "TotalDis";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public static final String FINVRHED_TAX_REG = "TaxReg";
    public static final String FINVRHED_ADD_DATE = "AddDate";
    public static final String FINVRHED_ADD_MACH = "AddMach";
    public static final String FINVRHED_ADD_USER = "AddUser";
    public static final String FINVRHED_ROUTE_CODE = "RouteCode";
    public static final String FINVRHED_LONGITUDE = "Longitude";
    public static final String FINVRHED_LATITUDE = "Latitude";
    public static final String FINVRHED_IS_ACTIVE = "IsActive";
    public static final String FINVRHED_IS_SYNCED = "IsSync";
    public static final String FINVRHED_ADDRESS = "Address";
    public static final String FINVRHED_START_TIME = "StartTime";
    public static final String FINVRHED_END_TIME = "EndTime";
    public static final String TABLE_FINVRDET = "FInvRDet";
    public static final String FINVRDET_ID = "id";

    public static final String FINVRDET_ITEMCODE = "ItemCode";
    public static final String FINVRDET_TAXCOMCODE = "TaxComCode";
    public static final String FINVRDET_PRILCODE = "PrilCode";
    public static final String FINVRDET_TXN_DATE = "TxnDate";
    public static final String FINVRDET_TXN_TYPE = "TxnType";
    public static final String FINVRDET_COST_PRICE = "CostPrice";
    public static final String FINVRDET_SELL_PRICE = "SellPrice";
    public static final String FINVRDET_T_SELL_PRICE = "TSellPrice";
    public static final String FINVRDET_AMT = "Amt";
    public static final String FINVRDET_DIS_AMT = "DisAmt";
    public static final String FINVRDET_TAX_AMT = "TaxAmt";
    public static final String FINVRDET_QTY = "Qty";
    public static final String FINVRDET_BAL_QTY = "BalQty";
    public static final String FINVRDET_IS_ACTIVE = "IsActive";
    public static final String FINVRDET_SEQNO = "SeqNo";
    public static final String FINVRDET_REASON_CODE = "ReasonCode";
    public static final String FINVRDET_REASON_NAME = "ReasonName";
    public static final String FINVRDET_RETURN_TYPE = "ReturnType";
    public static final String FINVRDET_CHANGED_PRICE = "ChangedPrice"; /**
     * ############################ FRepLoc ################################
     */
    private static final String CREATE_FINVRHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FINVRHED + " ("
                    + FINVRHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //1
                    + REFNO + " TEXT, "
                    + TXNDATE + " TEXT, "
                    + FINVRHED_MANUREF + " TEXT, "
                    + FINVRHED_COSTCODE + " TEXT, "
                    + FINVRHED_DEBCODE + " TEXT, "
                    + FINVRHED_REMARKS + " TEXT, "
                    + FINVRHED_TXNTYPE + " TEXT, "
                    + FINVRHED_INV_REFNO + " TEXT, "
                    + FINVRHED_ADD_DATE + " TEXT, "
                    + FINVRHED_ADD_MACH + " TEXT, "
                    + FINVRHED_ADD_USER + " TEXT, "
                    + FINVRHED_LOCCODE + " TEXT, "
                    + FINVRHED_IS_ACTIVE + " TEXT, "
                    + FINVRHED_IS_SYNCED + " TEXT, "
                    + FINVRHED_REPCODE + " TEXT, "
                    + FINVRHED_REASON_CODE + " TEXT, "
                    + FINVRHED_TAX_REG + " TEXT, "
                    + FINVRHED_TOTAL_AMT + " TEXT, "
                    + FINVRHED_TOTAL_TAX + " TEXT, "
                    + FINVRHED_TOTAL_DIS + " TEXT, "
                    + FINVRHED_ROUTE_CODE + " TEXT, "
                    + FINVRHED_LONGITUDE + " TEXT, "
                    + FINVRHED_LATITUDE + " TEXT, "
                    + FINVRHED_ADDRESS + " TEXT, "
                    + FINVRHED_START_TIME + " TEXT, "
                    + FINVRHED_END_TIME + " TEXT); ";

    private static final String CREATE_FINVRDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FINVRDET + " ( "
            + FINVRDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REFNO + " TEXT, "
            + FINVRDET_ITEMCODE + " TEXT, "
            + FINVRDET_TAXCOMCODE + " TEXT, "
            + FINVRDET_PRILCODE + " TEXT, "
            + FINVRDET_TXN_DATE + " TEXT, "
            + FINVRDET_TXN_TYPE + " TEXT, "
            + FINVRDET_COST_PRICE + " TEXT, "
            + FINVRDET_SELL_PRICE + " TEXT, "
            + FINVRDET_T_SELL_PRICE + " TEXT, "
            + FINVRDET_AMT + " TEXT, "
            + FINVRDET_DIS_AMT + " TEXT, "
            + FINVRDET_TAX_AMT + " TEXT, "
            + FINVRDET_QTY + " TEXT, "
            + FINVRDET_BAL_QTY + " TEXT, "
            + FINVRDET_IS_ACTIVE + " TEXT, "
            + FINVRDET_REASON_CODE + " TEXT, "
            + FINVRDET_REASON_NAME + " TEXT, "
            + FINVRDET_RETURN_TYPE + " TEXT, "
            + FINVRDET_CHANGED_PRICE + " TEXT DEFAULT 0, "
            + FINVRDET_SEQNO + " TEXT); ";
    public static final String TABLE_FREPLOC = "FRepLoc";
    // table attributes
    public static final String FREPLOC_ID = "FRepLoc_id";
    public static final String FREPLOC_REPCODE = "RepCode";
    public static final String FREPLOC_LOCCODE = "LocCode";
    public static final String FREPLOC_COSTCODE = "CostCose";
    // create String
    private static final String CREATE_FREPLOC_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FREPLOC + " (" + FREPLOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FREPLOC_REPCODE + " TEXT, " + FREPLOC_LOCCODE + " TEXT, " + FREPLOC_COSTCODE + " TEXT); ";
    // --------------------------------------------------------------------------------------------------------------
    public static final String TABLE_TEMP_FDEBTOR = "FTempDebtor";
    // table attributes
    // create String
    private static final String CREATE_TABLE_TEMP_FDEBTOR = "CREATE  TABLE IF NOT EXISTS " + TABLE_TEMP_FDEBTOR + " (" + FDEBTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FDEBTOR_CODE + " TEXT, " + FDEBTOR_NAME + " TEXT); ";
    // --------------------------------------------------------------------------------------------------------------
    public static final String TABLE_FPRECDET = "fpRecDet";
    public static final String FPRECDET_ID = "Id";
    public static final String FPRECDET_REFNO1 = "RefNo1";
    public static final String FPRECDET_REFNO2 = "RefNo2";
    public static final String FPRECDET_DEBCODE = "DebCode";
    public static final String FPRECDET_SALEREFNO = "SaleRefNo";
    public static final String FPRECDET_MANUREF = "ManuRef";
    public static final String FPRECDET_TXNTYPE = "TxnType";

    public static final String FPRECDET_DTXNDATE = "DtxnDate";
    public static final String FPRECDET_DTXNTYPE = "DtxnType";
    public static final String FPRECDET_DCURCODE = "DCurCode";
    public static final String FPRECDET_DCURRATE = "DCurRate";
    public static final String FPRECDET_OCURRATE = "OCurRate";
    public static final String FPRECDET_REPCODE = "RepCode";
    public static final String FPRECDET_AMT = "Amt";
    public static final String FPRECDET_BAMT = "BAmt";
    public static final String FPRECDET_ALOAMT = "AloAmt";
    public static final String FPRECDET_OVPAYAMT = "OvPayAmt";
    public static final String FPRECDET_OVPAYBAL = "OvPayBal";
    public static final String FPRECDET_RECORDID = "RecordId";
    public static final String FPRECDET_TIMESTAMP = "timestamp_column";
    public static final String FPRECDET_ISDELETE = "IsDelete";
    public static final String FPRECDET_REMARK = "Remark";
    private static final String CREATE_FPRECDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FPRECDET + " (" + FPRECDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FPRECDET_REFNO1 + " TEXT, " + FPRECDET_REFNO2 + " TEXT, " + FPRECDET_DEBCODE + " TEXT, " + FPRECDET_SALEREFNO + " TEXT, "

            + FPRECDET_MANUREF + " TEXT, " + FPRECDET_TXNTYPE + " TEXT, " + TXNDATE + " TEXT, "

            + FPRECDET_DTXNDATE + " TEXT, " + FPRECDET_DTXNTYPE + " TEXT, " + FPRECDET_DCURCODE + " TEXT, " + FPRECDET_DCURRATE + " TEXT, "

            + FPRECDET_OCURRATE + " TEXT, " + FPRECDET_REPCODE + " TEXT, " + FPRECDET_AMT + " TEXT, " + FPRECDET_BAMT + " TEXT, "

            + FPRECDET_ALOAMT + " TEXT, " + FPRECDET_OVPAYAMT + " TEXT, " + FPRECDET_REMARK + " TEXT, " + FPRECDET_OVPAYBAL + " TEXT, " + FPRECDET_RECORDID + " TEXT, " + FPRECDET_ISDELETE + " TEXT, " + FPRECDET_TIMESTAMP + " TEXT );";

    /*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public static final String TABLE_FPRECHED = "fpRecHed";
    public static final String FPRECHED_ID = "Id";
    public static final String FPRECHED_REFNO1 = "RefNo1";
    public static final String FPRECHED_MANUREF = "ManuRef";
    public static final String FPRECHED_SALEREFNO = "SaleRefNo";
    public static final String FPRECHED_REPCODE = "RepCode";
    public static final String FPRECHED_TXNTYPE = "TxnType";
    public static final String FPRECHED_CHQNO = "Chqno";
    public static final String FPRECHED_CHQDATE = "ChqDate";

    public static final String FPRECHED_CURCODE = "CurCode";
    public static final String FPRECHED_CURRATE1 = "CurRate1";
    public static final String FPRECHED_DEBCODE = "DebCode";
    public static final String FPRECHED_TOTALAMT = "TotalAmt";
    public static final String FPRECHED_BTOTALAMT = "BTotalAmt";
    public static final String FPRECHED_PAYTYPE = "PayType";
    public static final String FPRECHED_PRTCOPY = "PrtCopy";
    public static final String FPRECHED_REMARKS = "Remarks";
    public static final String FPRECHED_ADDUSER = "AddUser";
    public static final String FPRECHED_ADDMACH = "AddMach";
    public static final String FPRECHED_ADDDATE = "AddDate";
    public static final String FPRECHED_RECORDID = "RecordId";
    public static final String FPRECHED_TIMESTAMP = "timestamp_column";
    public static final String FPRECHED_CURRATE = "CurRate";
    public static final String FPRECHED_CUSBANK = "CusBank";
    public static final String FPRECHED_COST_CODE = "CostCode";
    public static final String FPRECHED_LONGITUDE = "Longitude";
    public static final String FPRECHED_LATITUDE = "Latitude";
    public static final String FPRECHED_ADDRESS = "Address";
    public static final String FPRECHED_START_TIME = "StartTime";
    public static final String FPRECHED_END_TIME = "EndTime";
    public static final String FPRECHED_ISACTIVE = "IsActive";
    public static final String FPRECHED_ISSYNCED = "IsSynced";
    public static final String FPRECHED_ISDELETE = "IsDelete";
    public static final String FPRECHED_BANKCODE = "BankCode";
    public static final String FPRECHED_BRANCHCODE = "BranchCode";
    private static final String CREATE_FPRECHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FPRECHED + " (" + FPRECHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            REFNO + " TEXT, " + FPRECHED_REFNO1 + " TEXT, " + FPRECHED_MANUREF + " TEXT, " + FPRECHED_SALEREFNO + " TEXT, " +

            FPRECHED_REPCODE + " TEXT, " + FPRECHED_TXNTYPE + " TEXT, " + FPRECHED_CHQNO + " TEXT, " + FPRECHED_CHQDATE + " TEXT, " + TXNDATE + " TEXT, " + FPRECHED_CURCODE + " TEXT, " +

            FPRECHED_CURRATE1 + " TEXT, " + FPRECHED_DEBCODE + " TEXT, " + FPRECHED_TOTALAMT + " TEXT, " + FPRECHED_BANKCODE + " TEXT, " + FPRECHED_BRANCHCODE + " TEXT, " +

            FPRECHED_BTOTALAMT + " TEXT, " + FPRECHED_PAYTYPE + " TEXT, " + FPRECHED_PRTCOPY + " TEXT, " + FPRECHED_REMARKS + " TEXT, " + FPRECHED_ADDUSER + " TEXT, " + FPRECHED_ADDMACH + " TEXT, " + FPRECHED_ADDDATE + " TEXT, " +

            FPRECHED_RECORDID + " TEXT, " + FPRECHED_TIMESTAMP + " TEXT, " + FPRECHED_ISDELETE + " TEXT, " + FPRECHED_COST_CODE + " TEXT, " +

            FPRECHED_LONGITUDE + " TEXT, " + FPRECHED_LATITUDE + " TEXT, " + FPRECHED_ADDRESS + " TEXT, " + FPRECHED_START_TIME + " TEXT, " + FPRECHED_END_TIME + " TEXT, " + FPRECHED_ISACTIVE + " TEXT, " + FPRECHED_ISSYNCED + " TEXT, " + FPRECHED_CURRATE + " TEXT, " + FPRECHED_CUSBANK + " TEXT);";

    /*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public static final String TABLE_FPRECHEDS = "fpRecHedS";

    private static final String CREATE_FPRECHEDS_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FPRECHEDS + " (" + FPRECHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            REFNO + " TEXT, " + FPRECHED_REFNO1 + " TEXT, " + FPRECHED_MANUREF + " TEXT, " + FPRECHED_SALEREFNO + " TEXT, " +

            FPRECHED_REPCODE + " TEXT, " + FPRECHED_TXNTYPE + " TEXT, " + FPRECHED_CHQNO + " TEXT, " + FPRECHED_CHQDATE + " TEXT, " + TXNDATE + " TEXT, " + FPRECHED_CURCODE + " TEXT, " +

            FPRECHED_CURRATE1 + " TEXT, " + FPRECHED_DEBCODE + " TEXT, " + FPRECHED_TOTALAMT + " TEXT, " + FPRECHED_BANKCODE + " TEXT, " + FPRECHED_BRANCHCODE + " TEXT, " +

            FPRECHED_BTOTALAMT + " TEXT, " + FPRECHED_PAYTYPE + " TEXT, " + FPRECHED_PRTCOPY + " TEXT, " + FPRECHED_REMARKS + " TEXT, " + FPRECHED_ADDUSER + " TEXT, " + FPRECHED_ADDMACH + " TEXT, " + FPRECHED_ADDDATE + " TEXT, " +

            FPRECHED_RECORDID + " TEXT, " + FPRECHED_TIMESTAMP + " TEXT, " + FPRECHED_ISDELETE + " TEXT, " + FPRECHED_COST_CODE + " TEXT, "

            + FPRECHED_LONGITUDE + " TEXT, " + FPRECHED_LATITUDE + " TEXT, " + FPRECHED_ADDRESS + " TEXT, " + FPRECHED_START_TIME + " TEXT, " + FPRECHED_END_TIME + " TEXT, " + FPRECHED_ISACTIVE + " TEXT, " + FPRECHED_ISSYNCED + " TEXT, " + FPRECHED_CURRATE + " TEXT, " + FPRECHED_CUSBANK + " TEXT);";

    public static final String TABLE_FPRECDETS = "fpRecDetS";

    private static final String CREATE_FPRECDETS_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FPRECDETS + " (" + FPRECDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + FPRECDET_REFNO1 + " TEXT, " + FPRECDET_REFNO2 + " TEXT, " + FPRECDET_DEBCODE + " TEXT, " + FPRECDET_SALEREFNO + " TEXT, "

            + FPRECDET_MANUREF + " TEXT, " + FPRECDET_TXNTYPE + " TEXT, " + TXNDATE + " TEXT, "

            + FPRECDET_DTXNDATE + " TEXT, " + FPRECDET_DTXNTYPE + " TEXT, " + FPRECDET_DCURCODE + " TEXT, " + FPRECDET_DCURRATE + " TEXT, "

            + FPRECDET_OCURRATE + " TEXT, " + FPRECDET_REPCODE + " TEXT, " + FPRECDET_AMT + " TEXT, " + FPRECDET_BAMT + " TEXT, " + FPRECDET_ISDELETE + " TEXT, "

            + FPRECDET_REMARK + " TEXT, " + FPRECDET_ALOAMT + " TEXT, " + FPRECDET_OVPAYAMT + " TEXT, " + FPRECDET_OVPAYBAL + " TEXT, " + FPRECDET_RECORDID + " TEXT, " + FPRECDET_TIMESTAMP + " TEXT );";
    /**
     * ############################ fRouteDet table Details
     * ################################
     */
    public static final String TABLE_FSTKIN = "fSTKIn";
    public static final String FSTKIN_ID = "Id";
    public static final String FSTKIN_BALQTY = "BalQty";
    public static final String FSTKIN_COSTPRICE = "CostPrice";
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-fTaxDet table Details-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*/
    public static final String FSTKIN_INQTY = "InQty";
    public static final String FSTKIN_ITEMCODE = "ItemCode";
    public static final String FSTKIN_LOCCODE = "LocCode";
    public static final String FSTKIN_OTHCOST = "OthCost";
    public static final String FSTKIN_STKREC_DATE = "StkRecDate";
    public static final String FSTKIN_STKRECNO = "StkRecNo";
    public static final String FSTKIN_TXNTYPE = "TxnType";
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-fTOUR-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public static final String TABLE_FSIZEIN = "fSizeIn";
    public static final String FSIZEIN_ID = "Id";
    public static final String FSIZEIN_STKRECDATE = "STKRecDate";
    public static final String FSIZEIN_LOCCODE = "LocCode";
    public static final String FSIZEIN_TXNTYPE = "TxnType";
    public static final String FSIZEIN_STKRECNO = "STKRecNo";
    public static final String FSIZEIN_ITEMCODE = "ItemCode";
    public static final String FSIZEIN_SIZECODE = "SizeCode";
    public static final String FSIZEIN_GROUPCODE = "GroupCode";
    public static final String FSIZEIN_QTY = "Qty";
    public static final String FSIZEIN_BALQTY = "BalQty";
    public static final String FSIZEIN_COSTPRICE = "CostPrice";
    public static final String FSIZEIN_PRICE = "Price";
    public static final String FSIZEIN_OTHCOST = "OthCost";

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-STKIN-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public static final String FSIZEIN_APPSTAT = "AppStat";
    public static final String FSIZEIN_ADDDATE = "AddDate";
    public static final String FSIZEIN_MRPPRICE = "MRPPrice";
    public static final String FSIZEIN_ORDREFNO = "OrdRefNo";
    public static final String TABLE_FSTKISS = "fStkIss";
    public static final String FSTKISS_ID = "Id";

    public static final String FSTKISS_LOCCODE = "LocCode";
    public static final String FSTKISS_STKRECNO = "STKRecNo";
    public static final String FSTKISS_STKTXNNO = "STKTxnNo";
    public static final String FSTKISS_STKTXNDATE = "STKTxnDate";
    public static final String FSTKISS_STKRECDATE = "STKRecDate";
    public static final String FSTKISS_STKTXNTYPE = "STKTxnType";

    /*-*-*-*--*-*-*-*-*-*-fSizeIn table-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public static final String FSTKISS_ITEMCODE = "ItemCode";
    public static final String FSTKISS_QTY = "Qty";
    public static final String FSTKISS_COSTPRICE = "CostPrice";
    private static final String CREATE_STKIN_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FSTKIN + " (" + FSTKIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT," + TXNDATE + " TEXT," + FSTKIN_STKREC_DATE + " TEXT," + FSTKIN_LOCCODE + " TEXT," + FSTKIN_ITEMCODE + " TEXT," + FSTKIN_INQTY + " TEXT," + FSTKIN_BALQTY + " TEXT," + FSTKIN_COSTPRICE + " TEXT," + FSTKIN_OTHCOST + " TEXT," + FSTKIN_STKRECNO + " TEXT," + FSTKIN_TXNTYPE + " TEXT); ";
    private static final String CREATE_FSIZEIN_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FSIZEIN + " (" + FSIZEIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT," + TXNDATE + " TEXT," + FSIZEIN_STKRECDATE + " TEXT," + FSIZEIN_LOCCODE + " TEXT," + FSIZEIN_TXNTYPE + " TEXT," + FSIZEIN_STKRECNO + " TEXT," + FSIZEIN_ITEMCODE + " TEXT," + FSIZEIN_SIZECODE + " TEXT," + FSIZEIN_GROUPCODE + " TEXT," + FSIZEIN_QTY + " TEXT," + FSIZEIN_BALQTY + " TEXT," + FSIZEIN_COSTPRICE + " TEXT," + FSIZEIN_PRICE + " TEXT," + FSIZEIN_OTHCOST + " TEXT," + FSIZEIN_APPSTAT + " TEXT," + FSIZEIN_ORDREFNO + " TEXT," + FSIZEIN_ADDDATE + " TEXT," + FSIZEIN_MRPPRICE + " TEXT); ";
    private static final String CREATE_FSTKISS_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FSTKISS + " (" + FSTKISS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT," + TXNDATE + " TEXT," + FSTKISS_LOCCODE + " TEXT," + FSTKISS_STKRECNO + " TEXT," + FSTKISS_STKTXNNO + " TEXT," + FSTKISS_STKTXNDATE + " TEXT," + FSTKISS_STKRECDATE + " TEXT," + FSTKISS_STKTXNTYPE + " TEXT," + FSTKISS_ITEMCODE + " TEXT," + FSTKISS_COSTPRICE + " TEXT," + FSTKISS_QTY + " TEXT); ";

    // table
    public static final String TABLE_FORDSTAT = "fOrdStat";
    // table attributes
    public static final String FORDSTAT_ID = "fOrdStat_id";
    public static final String FORDSTAT_ORDREFNO = "OrdRefNo";
    public static final String FORDSTAT_INVREFNO = "InvRefNo";
    public static final String FORDSTAT_OFINREFNO = "OfInRef";
    // create String
    private static final String CREATE_FORDSTAT_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FORDSTAT + " (" + FORDSTAT_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FORDSTAT_ORDREFNO + " TEXT, " + FORDSTAT_INVREFNO + " TEXT, " + FORDSTAT_OFINREFNO + " TEXT); ";
    public static final String TABLE_FGPSLOC = "fGPSLoc";
    // table attributes
    public static final String FGPSLOC_ID = "fGPSLoc_id";
    public static final String FGPSLOC_REPCODE = "RepCode";
    public static final String FGPSLOC_GPSDATE = "GPSDate";
    public static final String FGPSLOC_LONGITUDE = "Longitude";
    public static final String FGPSLOC_LATITUDE = "Latitude";
    public static final String FGPSLOC_BATTPER = "Battper";
    public static final String FGPSLOC_SEQNO = "SeqNo";
    public static final String FGPSLOC_ISSYNCED = "isSync";
    // create String
    private static final String CREATE_FGPSLOC_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FGPSLOC + " (" + FGPSLOC_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FGPSLOC_REPCODE + " TEXT, " + FGPSLOC_GPSDATE + " TEXT, "
            + FGPSLOC_LONGITUDE + " TEXT, " + FGPSLOC_LATITUDE + " TEXT, " + FGPSLOC_BATTPER + " TEXT, "
            + FGPSLOC_SEQNO + " TEXT, " + FGPSLOC_ISSYNCED + " TEXT); ";
    // Pre Product Table
    public static final String TABLE_FPRODUCT_PRE = "fProducts_pre";
    public static final String FPRODUCT_ID_PRE = "id";
    public static final String FPRODUCT_ITEMCODE_PRE = "itemcode_pre";
    public static final String FPRODUCT_ITEMNAME_PRE = "itemname_pre";
    public static final String FPRODUCT_PRICE_PRE = "price_pre";
    public static final String FPRODUCT_QOH_PRE = "qoh_pre";
    public static final String FPRODUCT_QTY_PRE = "qty_pre";
    private static final String CREATE_FPRODUCT_PRE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FPRODUCT_PRE + " ("
            + FPRODUCT_ID_PRE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FPRODUCT_ITEMCODE_PRE + " TEXT, "
            + FPRODUCT_ITEMNAME_PRE + " TEXT, "
            + FPRODUCT_PRICE_PRE + " TEXT, "
            + FPRODUCT_QOH_PRE + " TEXT, "
            + FPRODUCT_QTY_PRE + " TEXT); ";
;

    public static final String TABLE_FTOURHED = "fTourHed";
    public static final String TOURHED_ID = "Id";

    public static final String TOURHED_MANUREF = "ManuRef";
    public static final String TOURHED_LORRYCODE = "LorryCode";
    public static final String TOURHED_ROUTECODE = "RouteCode";
    public static final String TOURHED_AREACODE = "AreaCode";
    public static final String TOURHED_COSTCODE = "CostCode";
    public static final String TOURHED_REMARKS = "Remarks";
    public static final String TOURHED_LOCCODEF = "LocCodeF";
    public static final String TOURHED_LOCCODE = "LocCode";
    public static final String TOURHED_REPCODE = "RepCode";
    public static final String TOURHED_HELPERCODE = "HelperCode";
    public static final String TOURHED_ADDUSER = "AddUser";
    public static final String TOURHED_ADDMACH = "AddMach";
    public static final String TOURHED_DRIVERCODE = "DriverCode";
    public static final String TOURHED_VANLOADFLG = "VanLoadFlg";
    public static final String TOURHED_CLSFLG = "Clsflg";
    public static final String TOURHED_TOURTYPE = "TourType";

    private static final String CREATE_FTOURHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FTOURHED + " (" + TOURHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFNO + " TEXT, " + TOURHED_MANUREF + " TEXT, " + TXNDATE + " TEXT, " + TOURHED_LORRYCODE + " TEXT, " + TOURHED_ROUTECODE + " TEXT, " + TOURHED_AREACODE + " TEXT, " + TOURHED_COSTCODE + " TEXT, " + TOURHED_REMARKS + " TEXT, " + TOURHED_LOCCODEF + " TEXT, " + TOURHED_LOCCODE + " TEXT, " + TOURHED_REPCODE + " TEXT, " + TOURHED_HELPERCODE + " TEXT, " + TOURHED_ADDUSER + " TEXT, " + TOURHED_ADDMACH + " TEXT, " + TOURHED_DRIVERCODE + " TEXT, " + TOURHED_VANLOADFLG + " TEXT, " + TOURHED_CLSFLG + " TEXT, " + TOURHED_TOURTYPE + " TEXT); ";

    @Override
    public void onCreate(SQLiteDatabase arg0) {

        arg0.execSQL(CREATE_SERVER_DB_TABLE);
        arg0.execSQL(CREATE_FDEBTOR_TABLE);
        arg0.execSQL(CREATE_FCONTROL_TABLE);
        arg0.execSQL(CREATE_FCOMPANYSETTING_TABLE);
        // arg0.execSQL(CREATE_FINVDET_TABLE);
        arg0.execSQL(CREATE_FROUTE_TABLE);
        arg0.execSQL(CREATE_FBANK_TABLE);
        arg0.execSQL(CREATE_FREASON_TABLE);
        arg0.execSQL(CREATE_FEXPENSE_TABLE);
        arg0.execSQL(CREATE_FTOWN_TABLE);
        arg0.execSQL(CREATE_FTRGCAPUL_TABLE);
        arg0.execSQL(CREATE_FTYPE_TABLE);
        arg0.execSQL(CREATE_FSUBBRAND_TABLE);
        arg0.execSQL(CREATE_FGROUP_TABLE);
        arg0.execSQL(CREATE_FSKU_TABLE);
        arg0.execSQL(CREATE_FBRAND_TABLE);
        arg0.execSQL(CREATE_FORDHED_TABLE);
        arg0.execSQL(CREATE_FORDDET_TABLE);
        arg0.execSQL(CREATE_FCOMPANYBRANCH_TABLE);
        arg0.execSQL(CREATE_FSALREP_TABLE);
        arg0.execSQL(CREATE_FDDBNOTE_TABLE);
        arg0.execSQL(CREATE_FFREEDEB_TABLE);
        arg0.execSQL(CREATE_FFREEDET_TABLE);
        arg0.execSQL(CREATE_FFREEHED_TABLE);
        arg0.execSQL(CREATE_FFREESLAB_TABLE);
        arg0.execSQL(CREATE_FFREEITEM_TABLE);
        arg0.execSQL(CREATE_FITEM_TABLE);
        arg0.execSQL(CREATE_FITEMLOC_TABLE);
        arg0.execSQL(CREATE_FITEMPRI_TABLE);
        arg0.execSQL(CREATE_FAREA_TABLE);
        arg0.execSQL(CREATE_FLOCATIONS_TABLE);
        arg0.execSQL(CREATE_FDEALER_TABLE);
        arg0.execSQL(CREATE_FFREEMSLAB_TABLE);
        arg0.execSQL(CREATE_FMERCH_TABLE);
        arg0.execSQL(CREATE_FROUTEDET_TABLE);
        arg0.execSQL(CREATE_FDISCVHED_TABLE);
        arg0.execSQL(CREATE_FDISCVDET_TABLE);
        arg0.execSQL(CREATE_FDISCVDEB_TABLE);
        arg0.execSQL(CREATE_FDISCHED_TABLE);
        arg0.execSQL(CREATE_FDISCDET_TABLE);
        arg0.execSQL(CREATE_FDISCDEB_TABLE);
        arg0.execSQL(CREATE_FDISCSLAB_TABLE);
        arg0.execSQL(CREATE_FITENRHED_TABLE);
        arg0.execSQL(CREATE_FITENRDET_TABLE);
        arg0.execSQL(CREATE_FITEDEBDET_TABLE);
        arg0.execSQL(CREATE_FINVHEDL3_TABLE);
        arg0.execSQL(CREATE_FINVDETL3_TABLE);
        //arg0.execSQL(CREATE_FINVHED_TABLE);
        arg0.execSQL(CREATE_FTRANDET_TABLE);
        arg0.execSQL(CREATE_FTRANHED_TABLE);
        arg0.execSQL(CREATE_FTRANISS_TABLE);
        arg0.execSQL(CREATE_TABLE_NONPRDHED);
        arg0.execSQL(CREATE_TABLE_NONPRDDET);
        arg0.execSQL(CREATE_FDAMHED_TABLE);
        arg0.execSQL(CREATE_FDAMDET_TABLE);
        // arg0.execSQL(CREATE_FDAYEXPHED_TABLE);
        arg0.execSQL(CREATE_FDAYEXPDET_TABLE);
        arg0.execSQL(CREATE_FADJHED_TABLE);
        arg0.execSQL(CREATE_FADJDET_TABLE);
        arg0.execSQL(CREATE_FORDDISC_TABLE);
        arg0.execSQL(CREATE_FORDFREEISS_TABLE);
        arg0.execSQL(CREATE_FCOST_TABLE);
        arg0.execSQL(TESTITEM);
        arg0.execSQL(TESTITEMLOC);
        arg0.execSQL(TESTITEMPRI);
        arg0.execSQL(TESTINVHEDL3);
        arg0.execSQL(TESTINVDETL3);
        arg0.execSQL(TESTROUTEDET);
        arg0.execSQL(TESTFREEDEB);
        arg0.execSQL(TESTDEBTOR);
        arg0.execSQL(TESTDDBNOTE);
        arg0.execSQL(TESTBANK);
        arg0.execSQL(IDXCOMSETT);
        arg0.execSQL(IDXFREEHED);
        arg0.execSQL(IDXFREEDET);
        arg0.execSQL(IDXFREEITEM);
        arg0.execSQL(IDXFREESLAB);
        arg0.execSQL(CREATE_FINVRHED_TABLE);
        arg0.execSQL(CREATE_FINVRDET_TABLE);
        arg0.execSQL(CREATE_FREPLOC_TABLE);
        arg0.execSQL(CREATE_TABLE_TEMP_FDEBTOR);
        arg0.execSQL(CREATE_FPRECHED_TABLE);
        arg0.execSQL(CREATE_FPRECDET_TABLE);
        arg0.execSQL(CREATE_FPRECHEDS_TABLE);
        arg0.execSQL(CREATE_FPRECDETS_TABLE);
        arg0.execSQL(CREATE_FORDSTAT_TABLE);
        arg0.execSQL(CREATE_FGPSLOC_TABLE);
        arg0.execSQL(CREATE_FPRODUCT_TABLE);

        // --------------------- Nuwan ------------------------
        arg0.execSQL(CREATE_FPRODUCT_PRE_TABLE);

        arg0.execSQL(CREATE_FTOURHED_TABLE);
        // ---------------------------------------------------

    }
    // --------------------------------------------------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

        this.onCreate(arg0);
        try {
            arg0.execSQL(CREATE_FPRODUCT_TABLE);
            arg0.execSQL(CREATE_FINVHED_TABLE);
            arg0.execSQL(CREATE_FINVDET_TABLE);
            arg0.execSQL(CREATE_FINVRHED_TABLE);
            arg0.execSQL(CREATE_FINVRDET_TABLE);

        } catch (SQLiteException e) {
        }
//        try {
//            arg0.execSQL("ALTER TABLE "+TABLE_FSALREP+" ADD COLUMN LocCode TEXT ");
//        } catch (SQLiteException e) {
//            Log.v("SQLiteException", e.toString());
//        }
//        try {
//            arg0.execSQL("ALTER TABLE "+TABLE_FSALREP+" ADD COLUMN prillcode TEXT ");
//        } catch (SQLiteException e) {
//            Log.v("SQLiteException", e.toString());
//        }
//        try {
//            arg0.execSQL("ALTER TABLE "+TABLE_FINVHED+" ADD COLUMN SettingCode TEXT ");
//        } catch (SQLiteException e) {
//            Log.v("SQLiteException", e.toString());
//        }
//        try {
//            arg0.execSQL("ALTER TABLE "+TABLE_FINVHED+" ADD COLUMN areacode TEXT ");
//        } catch (SQLiteException e) {
//            Log.v("SQLiteException", e.toString());
//        }
//        try {
//            arg0.execSQL("ALTER TABLE "+TABLE_FINVHED+" ADD COLUMN endTime TEXT ");
//        } catch (SQLiteException e) {
//            Log.v("SQLiteException", e.toString());
//        }
//        try {
//            arg0.execSQL("ALTER TABLE "+TABLE_FINVHED+" ADD COLUMN startTime TEXT ");
//        } catch (SQLiteException e) {
//            Log.v("SQLiteException", e.toString());
//        }
//        try {
//            arg0.execSQL("ALTER TABLE "+TABLE_FINVHED+" ADD COLUMN PayType TEXT ");
//        } catch (SQLiteException e) {
//            Log.v("SQLiteException", e.toString());
//        }
        try {
            arg0.execSQL("ALTER TABLE "+TABLE_FINVHED+" ADD COLUMN tourcode TEXT ");
        } catch (SQLiteException e) {
            Log.v("SQLiteException", e.toString());
        }
    }
}