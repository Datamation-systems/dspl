package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.DayNPrdHed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayNPrdHedController {
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "NONPROD DS";

    public DayNPrdHedController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateNonPrdHed(ArrayList<DayNPrdHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (DayNPrdHed nonhed : list) {
                ContentValues values = new ContentValues();

                values.put(dbHelper.REFNO, nonhed.getNONPRDHED_REFNO());
                values.put(dbHelper.NONPRDHED_TXNDATE, nonhed.getNONPRDHED_TXNDATE());
                values.put(dbHelper.NONPRDHED_REPCODE, nonhed.getNONPRDHED_REPCODE());
                values.put(dbHelper.NONPRDHED_REMARK, nonhed.getNONPRDHED_REMARK());
                values.put(dbHelper.NONPRDHED_ADDDATE, nonhed.getNONPRDHED_ADDDATE());
                values.put(dbHelper.NONPRDHED_IS_SYNCED, nonhed.getNONPRDHED_IS_SYNCED());
                values.put(dbHelper.NONPRDHED_LONGITUDE,nonhed.getNONPRDHED_LONGITUDE());
                values.put(dbHelper.NONPRDHED_LATITUDE,nonhed.getNONPRDHED_LATITUDE());
                values.put(dbHelper.NONPRDHED_DEBCODE,nonhed.getNONPRDHED_DEBCODE());
                values.put(dbHelper.NONPRDHED_IS_ACTIVE,nonhed.getNONPRDHED_IS_ACTIVE());

                count = (int) dB.insert(dbHelper.TABLE_NONPRDHED, null, values);

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
//    public boolean validateActiveNonPrd()
//    {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        boolean res = false;
//
//        String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        Cursor cursor = null;
//        try {
//            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDHED + " WHERE " + DatabaseHelper.NONPRDHED_IS_ACTIVE+ "='1'";
//            cursor = dB.rawQuery(selectQuery, null);
//
//            /*Active invoice available*/
//            if (cursor.getCount() > 0) {
//                cursor.moveToFirst();
//
//                String txndate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_TXNDATE));
//
//                /*txndate is equal to current date*/
//                if (txndate.equals(toDay))
//                    res = true;
//                /*if invoice is older, then reset data*/
//                else {
//                    String Refno = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTRANSOHED_REFNO));
//                    restData(Refno);
////                    new InvDetDS(context).restData(Refno);
////                    new OrderDiscDS(context).clearData(Refno);
////                    new OrdFreeIssueDS(context).ClearFreeIssues(Refno);
//                    UtilityContainer.ClearNonSharedPref(context);
//                }
//
//            } else
//                res = false;
//
//        } catch (Exception e) {
//            Log.v(TAG, e.toString());
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//
//        return res;
//
//    }

	/*-*-*-*--*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*--*-*-*-*-*-*-*/

    /**
     * -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
     * *-*-*-*-*-*-*-*-*-*-*-*-
     */

    public boolean restData(String refno) {

        boolean Result = false;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDHED + " WHERE " + DatabaseHelper.REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_IS_SYNCED));
                /* if order already synced, can't delete */
                if (status.equals("1"))
                    Result = false;
                else {
                    int success = dB.delete(DatabaseHelper.TABLE_NONPRDHED, DatabaseHelper.REFNO + " ='" + refno + "'", null);
                    Log.v("Success", success + "");
                    Result = true;
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
        return Result;

    }
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public ArrayList<DayNPrdHed> getAllnonprdHedDetails(String newText) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdHed> list = new ArrayList<DayNPrdHed>();

        String selectQuery = "select * from " + dbHelper.TABLE_NONPRDHED + " WHERE AddDate LIKE '%" + newText + "%'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            DayNPrdHed fnonset = new DayNPrdHed();
            fnonset.setNONPRDHED_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
            fnonset.setNONPRDHED_ADDDATE(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDHED_ADDDATE)));
            fnonset.setNONPRDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDHED_IS_SYNCED)));

            list.add(fnonset);

        }

        return list;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    @SuppressWarnings("static-access")
    public int undoOrdHedByID(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_NONPRDHED + " WHERE " + dbHelper.REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_NONPRDHED, dbHelper.REFNO + "='" + RefNo + "'", null);

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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

//    public ArrayList<NonProdMapper> getUnSyncedData() {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<NonProdMapper> list = new ArrayList<NonProdMapper>();
//
//        try {
//
//            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDHED + " WHERE " + DatabaseHelper.NONPRDHED_IS_SYNCED + "='0'";
//            Cursor cursor = dB.rawQuery(selectQuery, null);
//            localSP = context.getSharedPreferences(SETTINGS, 0);
//
//            while (cursor.moveToNext()) {
//
//                NonProdMapper mapper = new NonProdMapper();
//                mapper.setNextNumVal(new CompanyBranchDS(context).getCurrentNextNumVal(context.getResources().getString(R.string.NonProd)));
//                mapper.setConsoleDB(localSP.getString("Console_DB", "").toString());
//
//                mapper.setNONPRDHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_ADDDATE)));
//                mapper.setNONPRDHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_ADDMACH)));
//                mapper.setNONPRDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_ADDRESS)));
//                mapper.setNONPRDHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_ADDUSER)));
//                mapper.setNONPRDHED_COSTCODE("000");
//                // mapper.setNONPRDHED_DEALCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_DEALCODE)));
//                mapper.setNONPRDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_IS_SYNCED)));
//                mapper.setREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
//                mapper.setNONPRDHED_REMARK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_REMARK)));
//                mapper.setNONPRDHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_REPCODE)));
//                mapper.setNONPRDHED_TRANSBATCH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_TRANSBATCH)));
//                mapper.setNONPRDHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_TXNDATE)));
//                mapper.setNONPRDHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_DEBCODE)));
//                mapper.setNONPRDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_LONGITUDE)));
//                mapper.setNONPRDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_LATITUDE)));
//                mapper.setNonPrdDet(new DayNPrdDetController(context).getAllnonprdDetails(mapper.getREFNO()));
//
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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

//    public int updateIsSynced(NonProdMapper mapper) {
//
//        int count = 0;
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//        Cursor cursor = null;
//
//        try {
//            ContentValues values = new ContentValues();
//            values.put(DatabaseHelper.NONPRDHED_IS_SYNCED, "1");
//            if (mapper.isSynced()) {
//                count = dB.update(DatabaseHelper.TABLE_NONPRDHED, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(mapper.getREFNO())});
//            }
//
//        } catch (Exception e) {
//
//            Log.v(TAG + " Exception", e.toString());
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//        return count;
//
//    }

    public boolean isEntrySynced(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select ISsync from FDaynPrdHed where refno ='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            String result = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_IS_SYNCED));

            if (result.equals("1"))
                return true;

        }
        cursor.close();
        dB.close();
        return false;

    }
    public int getNonPrdCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select count(refno) from FDaynPrdHed where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'", null);

        while (cursor.moveToNext()) {

            int result = cursor.getInt(0);

            if (result>0)
                return result;

        }
        cursor.close();
        dB.close();
        return 0;

    }

}
