package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.DayExpHed;

import java.util.ArrayList;

public class DayExpHedController {
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "Ebony";

    public DayExpHedController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

	/*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*/

    public int createOrUpdateNonPrdHed(ArrayList<DayExpHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (DayExpHed exphed : list) {
                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FDAYEXPHED_REFNO, exphed.getEXPHED_REFNO());
                values.put(DatabaseHelper.FDAYEXPHED_TXNDATE, exphed.getEXPHED_TXNDATE());
                values.put(DatabaseHelper.FDAYEXPHED_REPCODE, exphed.getEXPHED_REPCODE());
                values.put(DatabaseHelper.FDAYEXPHED_REMARKS, exphed.getEXPHED_REMARK());
                values.put(DatabaseHelper.FDAYEXPHED_ADDDATE, exphed.getEXPHED_ADDDATE());
                values.put(DatabaseHelper.FDAYEXPHED_ISSYNC, exphed.getEXPHED_IS_SYNCED());
                values.put(DatabaseHelper.FDAYEXPHED_TOTAMT, exphed.getEXPHED_TOTAMT());
                values.put(DatabaseHelper.FDAYEXPHED_ACTIVESTATE, exphed.getEXPHED_ACTIVESTATE());
                values.put(DatabaseHelper.FDAYEXPHED_LATITUDE, exphed.getEXPHED_LATITUDE());
                values.put(DatabaseHelper.FDAYEXPHED_LONGITUDE, exphed.getEXPHED_LONGITUDE());


                count = (int) dB.insert(DatabaseHelper.TABLE_DAYEXPHED, null, values);

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

	/*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*/

    public ArrayList<DayExpHed> getAllExpHedDetails(String newTExt) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayExpHed> list = new ArrayList<DayExpHed>();
        String selectQuery = "select * from " + DatabaseHelper.TABLE_DAYEXPHED + " WHERE TxnDate LIKE '%" + newTExt + "%'";
        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            DayExpHed fdayexpset = new DayExpHed();
            fdayexpset.setEXPHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_REFNO)));
            fdayexpset.setEXPHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_TXNDATE)));
            fdayexpset.setEXPHED_TOTAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_TOTAMT)));
            list.add(fdayexpset);
        }

        return list;
    }

	/*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*/

    public int undoExpHedByID(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_DAYEXPHED + " WHERE " + DatabaseHelper.FDAYEXPHED_REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_DAYEXPHED, DatabaseHelper.FDAYEXPHED_REFNO + "='" + RefNo + "'", null);

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

	/*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*/

//    public ArrayList<ExpenseMapper> getUnSyncedData() {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<ExpenseMapper> list = new ArrayList<ExpenseMapper>();
//
//        try {
//
//            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_DAYEXPHED + " WHERE " + DatabaseHelper.FDAYEXPHED_ACTIVESTATE + "='0' AND " + DatabaseHelper.FDAYEXPHED_ISSYNC + "='0'";
//            Cursor cursor = dB.rawQuery(selectQuery, null);
//            localSP = context.getSharedPreferences(SETTINGS, 0);
//
//            while (cursor.moveToNext()) {
//
//                ExpenseMapper mapper = new ExpenseMapper();
//                mapper.setNextNumVal(new CompanyBranchDS(context).getCurrentNextNumVal(context.getResources().getString(R.string.ExpenseNumVal)));
//                mapper.setConsoleDB(localSP.getString("Console_DB", "").toString());
//
//                mapper.setEXP_ACTIVESTATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_ACTIVESTATE)));
//                mapper.setEXP_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_ADDDATE)));
//                mapper.setEXP_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_ADDMATCH)));
//                mapper.setEXP_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_ADDRESS)));
//                mapper.setEXP_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_ADDUSER)));
//                // mapper.setEXP_AREACODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_AREACODE)));
//                // mapper.setEXP_DEALCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_DEALCODE)));
//                mapper.setEXP_COSTCODE("000");
//                mapper.setEXP_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_ISSYNC)));
//                mapper.setEXP_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_LATITUDE)));
//                mapper.setEXP_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_LONGITUDE)));
//                mapper.setEXP_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_REFNO)));
//                mapper.setEXP_REMARK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_REMARKS)));
//                mapper.setEXP_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_REPCODE)));
//                mapper.setEXP_REPNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_REPNAME)));
//                mapper.setEXP_TOTAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_TOTAMT)));
//                mapper.setEXP_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_TXNDATE)));
//
//                mapper.setExpnseDetList(new FDayExpDetDS(context).getAllExpDetails(mapper.getEXP_REFNO()));
//                list.add(mapper);
//            }
//
//        } catch (Exception e) {
//            Log.v(TAG + " Exception", e.toString());
//        } finally {
//            dB.close();
//        }
//
//        return list;
//
//    }

	/*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*/

    public int restDataExp(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_DAYEXPHED + " WHERE " + DatabaseHelper.FDAYEXPHED_REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(DatabaseHelper.TABLE_DAYEXPHED, DatabaseHelper.FDAYEXPHED_REFNO + " ='" + refno + "'", null);
                Log.v("Success Stauts", count + "");
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


    public boolean isEntrySynced(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select issync from FDayExpHed where refno ='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            String result = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_ISSYNC));

            if (result.equals("1"))
                return true;

        }
        cursor.close();
        dB.close();
        return false;

    }


}
