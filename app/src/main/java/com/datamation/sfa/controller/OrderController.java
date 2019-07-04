package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Order;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.PRESALE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OrderController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "OrderController";

    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;

    public OrderController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateOrdHed(ArrayList<PRESALE> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (PRESALE ordHed : list) {

                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER + " WHERE " + dbHelper.REFNO
                        + " = '" + ordHed.getORDER_REFNO() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(dbHelper.REFNO, ordHed.getORDER_REFNO());
                values.put(dbHelper.ORDER_ADDDATE, ordHed.getORDER_ADD_DATE());
                values.put(dbHelper.ORDER_CUSCODE, ordHed.getORDER_DEB_CODE());
                values.put(dbHelper.ORDER_START_TIME, ordHed.getORDER_ADD_TIME());
                values.put(dbHelper.ORDER_LONGITUDE, ordHed.getORDER_LONGITUDE());
                values.put(dbHelper.ORDER_LATITUDE, ordHed.getORDER_LATITUDE());
                values.put(dbHelper.ORDER_MANU_REF, ordHed.getORDER_MANUAL_NUMBER());
                values.put(dbHelper.ORDER_REMARKS, ordHed.getORDER_REMARKS());
                values.put(dbHelper.ORDER_REPCODE, ordHed.getORDER_REP_CODE());
                values.put(dbHelper.ORDER_TOTAL_AMT, ordHed.getORDER_TOT_AMT());
                values.put(dbHelper.ORDER_TXN_DATE, ordHed.getORDER_TXN_DATE());
                values.put(dbHelper.ORDER_ROUTE_CODE, ordHed.getORDER_ROUTE_CODE());
                values.put(dbHelper.ORDER_IS_SYNCED, "0");
                values.put(dbHelper.ORDER_IS_ACTIVE, ordHed.getORDER_IS_ACTIVE());

                int cn = cursor.getCount();
                if (cn > 0) {
                    count = dB.update(dbHelper.TABLE_ORDER, values, dbHelper.REFNO + " =?",
                            new String[] { String.valueOf(ordHed.getORDER_REFNO()) });
                } else {
                    count = (int) dB.insert(dbHelper.TABLE_ORDER, null, values);
                }

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


    @SuppressWarnings("static-access")
    public int restData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER + " WHERE " + dbHelper.REFNO + " = '"
                    + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                int success = dB.delete(dbHelper.TABLE_ORDER, dbHelper.REFNO + " ='" + refno + "'", null);
                Log.v("Success", success + "");
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

    @SuppressWarnings("static-access")
    public int InactiveStatusUpdate(String refno) {

        int count = 0;
        String UploadDate = "";
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UploadDate = sdf.format(cal.getTime());
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER + " WHERE " + dbHelper.REFNO + " = '"
                    + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(dbHelper.ORDER_IS_ACTIVE, "0");
            values.put(dbHelper.ORDER_END_TIME, UploadDate.split(" ")[1]);
            values.put(dbHelper.ORDER_ADDDATE, UploadDate.split(" ")[0]);
            values.put(dbHelper.ORDER_LATITUDE, SharedPref.getInstance(context).getGlobalVal("startLatitude"));
            values.put(dbHelper.ORDER_LONGITUDE, SharedPref.getInstance(context).getGlobalVal("startLongitude"));

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(dbHelper.TABLE_ORDER, values, dbHelper.REFNO + " =?",
                        new String[] { String.valueOf(refno) });
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

    @SuppressWarnings("static-access")
    public int updateIsSynced(boolean status,String refNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();


            values.put(dbHelper.ORDER_IS_SYNCED, "1");

            if (status) {
                count = dB.update(dbHelper.TABLE_ORDER, values, dbHelper.REFNO + " =?",
                        new String[] { String.valueOf(refNo) });
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

    public ArrayList<Order> getAllActiveOrdHed(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Order> list = new ArrayList<Order>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + dbHelper.TABLE_ORDER + " Where " + dbHelper.ORDER_IS_ACTIVE
                + "='1' and " + dbHelper.REFNO + "='" + refno + "' and " + dbHelper.ORDER_IS_SYNCED + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            Order ordHed = new Order();

//            ordHed.setORDHED_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ID)));
//            ordHed.setORDHED_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
//            ordHed.setORDHED_CUS_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_CUSCODE)));
//            ordHed.setORDHED_START_TIME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_START_TIME)));
//            ordHed.setORDHED_END_TIME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_END_TIME)));
//            ordHed.setORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_LONGITUDE)));
//            ordHed.setORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_LATITUDE)));
//            ordHed.setORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_MANU_REF)));
//            ordHed.setORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_REMARKS)));
//            ordHed.setORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_REPCODE)));
//            ordHed.setORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_TOTAL_AMT)));
//            ordHed.setORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_TXN_DATE)));
//            ordHed.setORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_IS_SYNCED)));
//            ordHed.setORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_IS_ACTIVE)));
//            ordHed.setORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ROUTE_CODE)));

            list.add(ordHed);

        }

        return list;
    }


    @SuppressWarnings("static-access")
    public ArrayList<Order> getAllUnSyncOrdHed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Order> list = new ArrayList<Order>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + dbHelper.TABLE_ORDER + " Where " + dbHelper.ORDER_IS_ACTIVE
                + "='0' and " + dbHelper.ORDER_IS_SYNCED + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        while (cursor.moveToNext()) {

            Order order = new Order();
            OrderDetailController detDS = new OrderDetailController(context);
            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
//            order.setNextNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
//
//            order.setORDHED_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ID)));
//            order.setORDHED_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
//            order.setORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ADDDATE)));
//
//            order.setORDHED_CUS_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_CUSCODE)));
//
//            order.setORDHED_START_TIME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_START_TIME)));
//            order.setORDHED_END_TIME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_END_TIME)));
//            order.setORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_LONGITUDE)));
//            order.setORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_LATITUDE)));
//            order.setORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_MANU_REF)));
//            order.setORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_REMARKS)));
//            order.setORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_REPCODE)));
//
//            order.setORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_TOTAL_AMT)));
//            order.setORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_TXN_DATE)));
//
//            order.setORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_IS_ACTIVE)));
//            order.setORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_DELIV_DATE)));
//            order.setORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ROUTE_CODE)));
//
//            order.setSoDetArrayList(detDS.getAllUnSync(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO))));
////            preSalesMapper.setIssuList(
////                    issueDS.getActiveIssues(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_CUSCODE))));

            list.add(order);

        }

        return list;
    }
    public ArrayList<Order> getAllOrders() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Order> list = new ArrayList<Order>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + dbHelper.TABLE_ORDER;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        while (cursor.moveToNext()) {

            Order order = new Order();
            OrderDetailController detDS = new OrderDetailController(context);
            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
//            order.setNextNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
//
//            order.setORDHED_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ID)));
//            order.setORDHED_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
//            order.setORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ADDDATE)));
//
//            order.setORDHED_CUS_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_CUSCODE)));
//
//            order
//                    .setORDHED_START_TIME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_START_TIME)));
//            order
//                    .setORDHED_END_TIME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_END_TIME)));
//            order.setORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_LONGITUDE)));
//            order.setORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_LATITUDE)));
//            order.setORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_MANU_REF)));
//            order.setORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_REMARKS)));
//            order.setORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_REPCODE)));
//
//            order.setORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_TOTAL_AMT)));
//            order.setORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_TXN_DATE)));
//
//            order.setORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_IS_ACTIVE)));
//            order.setORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_IS_SYNCED)));
//            order.setORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_DELIV_DATE)));
//            order.setORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ROUTE_CODE)));
//
//            order
//                    .setSoDetArrayList(detDS.getAllActives(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO))));
////            preSalesMapper.setIssuList(
////                    issueDS.getActiveIssues(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_CUSCODE))));

            list.add(order);

        }

        return list;
    }
    public ArrayList<Order> getAllActiveOrdHed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Order> list = new ArrayList<Order>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + dbHelper.TABLE_ORDER + " Where " + dbHelper.ORDER_IS_ACTIVE
                + "='1' and " + dbHelper.ORDER_IS_SYNCED + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        while (cursor.moveToNext()) {

            Order order = new Order();
            OrderDetailController detDS = new OrderDetailController(context);
            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
//            order.setNextNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
//
//            order.setORDHED_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ID)));
//            order.setORDHED_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
//            order.setORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ADDDATE)));
//
//            order.setORDHED_CUS_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_CUSCODE)));
//
//            order
//                    .setORDHED_START_TIME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_START_TIME)));
//            order
//                    .setORDHED_END_TIME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_END_TIME)));
//            order.setORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_LONGITUDE)));
//            order.setORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_LATITUDE)));
//            order.setORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_MANU_REF)));
//            order.setORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_REMARKS)));
//            order.setORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_REPCODE)));
//
//            order.setORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_TOTAL_AMT)));
//            order.setORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_TXN_DATE)));
//
//            order.setORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_IS_ACTIVE)));
//            order.setORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_DELIV_DATE)));
//            order.setORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_ROUTE_CODE)));
//
//            order
//                    .setSoDetArrayList(detDS.getAllActives(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO))));
////            preSalesMapper.setIssuList(
////                    issueDS.getActiveIssues(cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_CUSCODE))));

            list.add(order);

        }

        return list;

    }
    public String getRefnoByDebcode(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER + " WHERE " + dbHelper.REFNO + "='"
                + refno + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_CUSCODE));

        }
        return "";

    }

    @SuppressWarnings("static-access")
    public int DeleteOldOrders(String DateFrom, String DateTo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER + " WHERE " +dbHelper.ORDER_TXN_DATE + " BETWEEN '"+ DateFrom + "' AND '" + DateTo + "' AND " + dbHelper.ORDER_IS_ACTIVE + "= '0' AND " + dbHelper.ORDER_IS_SYNCED + " ='1' " ;
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                int success = dB.delete(dbHelper.TABLE_ORDER, dbHelper.ORDER_TXN_DATE + " BETWEEN '"+ DateFrom + "' AND '" + DateTo + "' AND " + dbHelper.ORDER_IS_ACTIVE + "= '0' AND " + dbHelper.ORDER_IS_SYNCED + " ='1' ", null);
                count = success;
                Log.v("Success", success + "");
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

    public String getRefnoToDelete(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER + " WHERE " + dbHelper.REFNO + "='"
                + refno + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(dbHelper.ORDER_IS_SYNCED));

        }

        return "";
    }

    public int undoOrdHedByID(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_ORDER + " WHERE " + dbHelper.REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_ORDER, dbHelper.REFNO + "='" + RefNo + "'", null);

            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return count;

    }
}
