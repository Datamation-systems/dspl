package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FInvRHed;
import com.datamation.sfa.model.Order;

import java.util.ArrayList;

public class SalesReturnController
{
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "SFA";

    public SalesReturnController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateInvRHed(ArrayList<FInvRHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (FInvRHed invrHed : list) {

                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FINVRHED + " WHERE " + dbHelper.REFNO
                        + " = '" + invrHed.getFINVRHED_REFNO() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(dbHelper.REFNO, invrHed.getFINVRHED_REFNO());
                values.put(dbHelper.TXNDATE, invrHed.getFINVRHED_TXN_DATE());
                values.put(dbHelper.FINVRHED_REMARKS, invrHed.getFINVRHED_REMARKS());
                values.put(dbHelper.FINVRHED_DEBCODE, invrHed.getFINVRHED_DEBCODE());
                values.put(dbHelper.FINVRHED_TOTAL_AMT, invrHed.getFINVRHED_TOTAL_AMT());
                values.put(dbHelper.FINVRHED_ADD_DATE, invrHed.getFINVRHED_ADD_DATE());
                values.put(dbHelper.FINVRHED_ADD_MACH, invrHed.getFINVRHED_ADD_MACH());
                values.put(dbHelper.FINVRHED_ADD_USER, invrHed.getFINVRHED_ADD_USER());
                values.put(dbHelper.FINVRHED_MANUREF, invrHed.getFINVRHED_MANUREF());
                values.put(dbHelper.FINVRHED_IS_ACTIVE, invrHed.getFINVRHED_IS_ACTIVE());
                values.put(dbHelper.FINVRHED_IS_SYNCED, invrHed.getFINVRHED_IS_SYNCED());
                values.put(dbHelper.FINVRHED_COSTCODE, invrHed.getFINVRHED_COSTCODE());
                values.put(dbHelper.FINVRHED_LOCCODE, invrHed.getFINVRHED_LOCCODE());
                values.put(dbHelper.FINVRHED_REASON_CODE, invrHed.getFINVRHED_REASON_CODE());
                values.put(dbHelper.FINVRHED_TAX_REG, invrHed.getFINVRHED_TAX_REG());
                values.put(dbHelper.FINVRHED_TOTAL_TAX, invrHed.getFINVRHED_TOTAL_TAX());
                values.put(dbHelper.FINVRHED_TOTAL_DIS, invrHed.getFINVRHED_TOTAL_DIS());
                values.put(dbHelper.FINVRHED_LONGITUDE, invrHed.getFINVRHED_LONGITUDE());
                values.put(dbHelper.FINVRHED_LATITUDE, invrHed.getFINVRHED_LATITUDE());
                values.put(dbHelper.FINVRHED_START_TIME, invrHed.getFINVRHED_START_TIME());
                values.put(dbHelper.FINVRHED_END_TIME, invrHed.getFINVRHED_END_TIME());
                values.put(dbHelper.FINVRHED_REPCODE, invrHed.getFINVRHED_REP_CODE());
//                values.put(dbHelper.FINVRHED_RETURN_TYPE, invrHed.getFINVRHED_RETURN_TYPE());
//                values.put(dbHelper.FINVRHED_TOURCODE, invrHed.getFINVRHED_TOURCODE());
//                values.put(dbHelper.FINVRHED_AREACODE, invrHed.getFINVRHED_AREACODE());
//                values.put(dbHelper.FINVRHED_DRIVERCODE, invrHed.getFINVRHED_DRIVERCODE());
//                values.put(dbHelper.FINVRHED_HELPERCODE, invrHed.getFINVRHED_HELPERCODE());
//                values.put(dbHelper.FINVRHED_LORRYCODE, invrHed.getFINVRHED_LORRYCODE());

                int cn = cursor.getCount();
                if (cn > 0) {

                    count = dB.update(dbHelper.TABLE_FINVRHED, values, dbHelper.REFNO + " =?",
                            new String[]{String.valueOf(invrHed.getFINVRHED_REFNO())});
                } else {

                    count = (int) dB.insert(dbHelper.TABLE_FINVRHED, null, values);

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

    public boolean isAnyActive() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        boolean res = false;

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVRHED + " WHERE " + DatabaseHelper.FINVRHED_IS_ACTIVE + "='1'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                res = true;
            else
                res = false;

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return res;

    }

    public ArrayList<FInvRHed> getAllActiveInvrhed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRHed> list = new ArrayList<FInvRHed>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + dbHelper.TABLE_FINVRHED + " Where " + dbHelper.FINVRHED_IS_ACTIVE
                + "='1' and " + dbHelper.FINVRHED_IS_SYNCED + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            FInvRHed invrHed = new FInvRHed();

            // invHed.setFINVHED_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRHED_ID)));
            invrHed.setFINVRHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
            invrHed.setFINVRHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
            invrHed.setFINVRHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ROUTE_CODE)));
            invrHed.setFINVRHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TXNTYPE)));
            invrHed.setFINVRHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADD_MACH)));
            invrHed.setFINVRHED_ADD_USER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADD_USER)));
            invrHed.setFINVRHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_MANUREF)));
            invrHed.setFINVRHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REMARKS)));
            invrHed.setFINVRHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_DEBCODE)));
            invrHed.setFINVRHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_AMT)));
            invrHed.setFINVRHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_IS_SYNCED)));
            invrHed.setFINVRHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_IS_ACTIVE)));
            invrHed.setFINVRHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADD_DATE)));
            invrHed.setFINVRHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_COSTCODE)));
            invrHed.setFINVRHED_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LOCCODE)));
            invrHed.setFINVRHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADDRESS)));
            invrHed.setFINVRHED_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REASON_CODE)));
            invrHed.setFINVRHED_TAX_REG(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TAX_REG)));
            invrHed.setFINVRHED_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_TAX)));
            invrHed.setFINVRHED_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_DIS)));
            invrHed.setFINVRHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LONGITUDE)));
            invrHed.setFINVRHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LATITUDE)));
            invrHed.setFINVRHED_START_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_START_TIME)));
            invrHed.setFINVRHED_END_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_END_TIME)));
            invrHed.setFINVRHED_REP_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REPCODE)));
//            invrHed.setFINVRHED_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_RETURN_TYPE)));
//            invrHed.setFINVRHED_TOURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOURCODE)));
//            invrHed.setFINVRHED_AREACODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_AREACODE)));
//            invrHed.setFINVRHED_DRIVERCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_DRIVERCODE)));
//            invrHed.setFINVRHED_HELPERCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_HELPERCODE)));
//            invrHed.setFINVRHED_LORRYCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LORRYCODE)));

            list.add(invrHed);

        }

        return list;
    }

    public FInvRHed getActiveReturnHed(String RefNo) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        FInvRHed invrHed = new FInvRHed();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + dbHelper.TABLE_FINVRHED + " Where " + dbHelper.FINVRHED_IS_ACTIVE + "='1' and " + dbHelper.FINVRHED_IS_SYNCED + "='0'" + " AND " + DatabaseHelper.REFNO + " = '" + RefNo + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            // invHed.setFINVHED_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRHED_ID)));
            invrHed.setFINVRHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
            invrHed.setFINVRHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
            invrHed.setFINVRHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ROUTE_CODE)));
            invrHed.setFINVRHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TXNTYPE)));
            invrHed.setFINVRHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADD_MACH)));
            invrHed.setFINVRHED_ADD_USER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADD_USER)));
            invrHed.setFINVRHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_MANUREF)));
            invrHed.setFINVRHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REMARKS)));
            invrHed.setFINVRHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_DEBCODE)));
            invrHed.setFINVRHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_AMT)));
            invrHed.setFINVRHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_IS_SYNCED)));
            invrHed.setFINVRHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_IS_ACTIVE)));
            invrHed.setFINVRHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADD_DATE)));
            invrHed.setFINVRHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_COSTCODE)));
            invrHed.setFINVRHED_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LOCCODE)));
            invrHed.setFINVRHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADDRESS)));
            invrHed.setFINVRHED_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REASON_CODE)));
            invrHed.setFINVRHED_TAX_REG(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TAX_REG)));
            invrHed.setFINVRHED_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_TAX)));
            invrHed.setFINVRHED_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_DIS)));
            invrHed.setFINVRHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LONGITUDE)));
            invrHed.setFINVRHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LATITUDE)));
            invrHed.setFINVRHED_START_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_START_TIME)));
            invrHed.setFINVRHED_END_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_END_TIME)));
            invrHed.setFINVRHED_REP_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REPCODE)));
//            invrHed.setFINVRHED_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_RETURN_TYPE)));
//            invrHed.setFINVRHED_TOURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOURCODE)));
//            invrHed.setFINVRHED_AREACODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_AREACODE)));
//            invrHed.setFINVRHED_DRIVERCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_DRIVERCODE)));
//            invrHed.setFINVRHED_HELPERCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_HELPERCODE)));
//            invrHed.setFINVRHED_LORRYCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LORRYCODE)));



        }

        return invrHed;
    }

    public int restData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVRHED + " WHERE "
                    + DatabaseHelper.REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            count = cursor.getCount();

            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_FINVRHED,
                        DatabaseHelper.REFNO + " ='" + refno + "'", null);
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
    public FInvRHed getDetailsforPrint(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        FInvRHed SRHed = new FInvRHed();

        try {
            String selectQuery = "SELECT RefNo,TotalAmt FROM " + DatabaseHelper.TABLE_FINVRHED + " WHERE " + DatabaseHelper.REFNO + " = '" + Refno + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                SRHed.setFINVRHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                SRHed.setFINVRHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_AMT)));

            }
            cursor.close();

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return SRHed;

    }
    public int InactiveStatusUpdate(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVRHED + " WHERE "
                    + DatabaseHelper.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FINVRHED_IS_ACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(DatabaseHelper.TABLE_FINVRHED, values, DatabaseHelper.REFNO + " =?",
                        new String[]{String.valueOf(refno)});
            } else {
                count = (int) dB.insert(DatabaseHelper.TABLE_FINVRHED, null, values);
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

    public FInvRHed getReturnDetailsForPrint(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        FInvRHed REHed = new FInvRHed();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVRHED + " WHERE " + DatabaseHelper.REFNO + " = '" + Refno + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                REHed.setFINVRHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                REHed.setFINVRHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_AMT)));
                REHed.setFINVRHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_DEBCODE)));
                REHed.setFINVRHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REMARKS)));
                REHed.setFINVRHED_TAX_REG(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TAX_REG)));
                REHed.setFINVRHED_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_TAX)));
                REHed.setFINVRHED_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_DIS)));
//                REHed.setFTRANSOHED_TOTQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTRANSOHED_TOTQTY)));
//                REHed.setFTRANSOHED_TOTFREE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTRANSOHED_TOTFREE)));
            }
            cursor.close();

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return REHed;
    }

    public int updateIsSynced(FInvRHed hed) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();

            values.put(dbHelper.FINVRHED_IS_SYNCED, "1");

            if (hed.getFINVRHED_IS_SYNCED().equals("1")) {
                count = dB.update(dbHelper.TABLE_FINVRHED, values, dbHelper.REFNO + " =?", new String[] { String.valueOf(hed.getFINVRHED_REFNO()) });
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

    // Sales Return Upload Method

    public ArrayList<FInvRHed> getAllUnsynced() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRHed> list = new ArrayList<FInvRHed>();


        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + dbHelper.TABLE_FINVRHED
                + " Where " + dbHelper.FINVRHED_IS_ACTIVE + "='0' and " +
                dbHelper.FINVRHED_IS_SYNCED + "='0'";


        Cursor cursor = dB.rawQuery(selectQuery, null);

        //localSP =  context.getSharedPreferences(SETTINGS, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);

        while (cursor.moveToNext()) {

            FInvRHed salesReturnMapper = new FInvRHed();

            salesReturnMapper.setDistDB(localSP.getString("DistDB", "").toString());
            salesReturnMapper.setConsoleDB(localSP.getString("ConsoleDB",
                    "").toString());

            salesReturnMapper.setFINVRHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ID)));
            salesReturnMapper.setFINVRHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
            salesReturnMapper.setFINVRHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADD_DATE)));
            salesReturnMapper.setFINVRHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADD_MACH)));
            salesReturnMapper.setFINVRHED_ADD_USER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADD_USER)));
            salesReturnMapper.setFINVRHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REMARKS)));
            salesReturnMapper.setFINVRHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_AMT)));
            salesReturnMapper.setFINVRHED_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_DIS)));
            salesReturnMapper.setFINVRHED_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TOTAL_TAX)));
            salesReturnMapper.setFINVRHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_COSTCODE)));
            salesReturnMapper.setFINVRHED_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REASON_CODE)));
            salesReturnMapper.setFINVRHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_DEBCODE)));
            salesReturnMapper.setFINVRHED_START_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_START_TIME)));
            salesReturnMapper.setFINVRHED_END_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_END_TIME)));
            salesReturnMapper.setFINVRHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LONGITUDE)));
            salesReturnMapper.setFINVRHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LATITUDE)));
            salesReturnMapper.setFINVRHED_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_LOCCODE)));
            salesReturnMapper.setFINVRHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_MANUREF)));
            salesReturnMapper.setFINVRHED_REP_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_REPCODE)));
            salesReturnMapper.setFINVRHED_TAX_REG(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TAX_REG)));
            salesReturnMapper.setFINVRHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_TXNTYPE)));
            salesReturnMapper.setFINVRHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
            salesReturnMapper.setFINVRHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ADDRESS)));
            salesReturnMapper.setFINVRHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_IS_SYNCED)));
            salesReturnMapper.setFINVRHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_IS_ACTIVE)));
            salesReturnMapper.setFINVRHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRHED_ROUTE_CODE)));



            String RefNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO));

            salesReturnMapper.setFinvrtDets(new SalesReturnDetController(context).getAllInvRDet(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO))));
            //salesReturnMapper.setFinvrtDets(new FInvRDetDS(context).getAllInvRDet(RefNo));
            salesReturnMapper.setTaxDTs(new SalesReturnTaxDTDS(context).getAllTaxDT(RefNo));
            salesReturnMapper.setTaxRGs(new SalesReturnTaxRGDS(context).getAllTaxRG(RefNo));

            list.add(salesReturnMapper);

        }

        return list;
    }
}
