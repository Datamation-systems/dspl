package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.DayExpDet;
import com.datamation.sfa.model.DayNPrdDet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayExpDetController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "Ebony";

    public DayExpDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    /*
     * insert code
     */
    @SuppressWarnings("static-access")
    public int createOrUpdateExpenseDet(ArrayList<DayExpDet> list) {

        // int count =0;
        int serverdbID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (DayExpDet expdet : list) {
                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_DAYEXPDET + " WHERE " + dbHelper.REFNO + " = '" + expdet.getEXPDET_REFNO() + "'" + " AND " + dbHelper.DAYEXPDET_EXPCODE + " = '" + expdet.getEXPDET_EXPCODE() + "'";
                cursor = dB.rawQuery(selectQuery, null);

                values.put(dbHelper.REFNO, expdet.getEXPDET_REFNO());
                values.put(dbHelper.DAYEXPDET_EXPCODE, expdet.getEXPDET_EXPCODE());
                values.put(dbHelper.DAYEXPDET_AMT, expdet.getEXPDET_AMOUNT());
                values.put(dbHelper.DAYEXPDET_TXNDATE, expdet.getEXPDET_TXNDATE());

                int count = cursor.getCount();
                if (count > 0) {
                    serverdbID = dB.update(dbHelper.TABLE_DAYEXPDET, values, dbHelper.REFNO + " =?" + " AND " + dbHelper.DAYEXPDET_EXPCODE + " =?", new String[]{String.valueOf(expdet.getEXPDET_REFNO()), String.valueOf(expdet.getEXPDET_EXPCODE())});

                } else {
                    serverdbID = (int) dB.insert(dbHelper.TABLE_DAYEXPDET, null, values);
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
        return serverdbID;

    }

    // Load Detail records of FdayExpdet for selected Reference number.
    public ArrayList<DayExpDet> getAllExpDetails(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayExpDet> list = new ArrayList<DayExpDet>();

        String selectQuery = "select * from " + dbHelper.TABLE_DAYEXPDET + " WHERE " + dbHelper.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            DayExpDet fdayexpset = new DayExpDet();

            fdayexpset.setEXPDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
            fdayexpset.setEXPDET_TXNDATE(cursor.getString(cursor.getColumnIndex(dbHelper.DAYEXPDET_TXNDATE)));
            fdayexpset.setEXPDET_EXPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.DAYEXPDET_EXPCODE)));
            fdayexpset.setEXPDET_AMOUNT(cursor.getString(cursor.getColumnIndex(dbHelper.DAYEXPDET_AMT)));

            list.add(fdayexpset);

        }

        return list;
    }

    public String getDuplicate(String code, String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_DAYEXPDET + " WHERE " + dbHelper.DAYEXPDET_EXPCODE + "='" + code + "' AND " + dbHelper.REFNO + "='" + RefNo + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(dbHelper.DAYEXPDET_EXPCODE));

        }

        return "";
    }

    @SuppressWarnings("static-access")
    public int deleteOrdDetByID(String id) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_DAYEXPDET + " WHERE " + dbHelper.DAYEXPDET_ID + "='" + id + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_DAYEXPDET, dbHelper.DAYEXPDET_ID + "='" + id + "'", null);
                Log.v("FExpDet Deleted ", success + "");
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
    public int getExpenceCount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(RefNo) as RefNo FROM " + dbHelper.TABLE_DAYEXPDET +  " WHERE  " + dbHelper.REFNO + "='" + refNo + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                return Integer.parseInt(cursor.getString(cursor.getColumnIndex("RefNo")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }
        return 0;

    }
    @SuppressWarnings("static-access")
    public int getAllExpDetail(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_DAYEXPDET + " WHERE " + dbHelper.REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_DAYEXPDET, dbHelper.REFNO + "='" + RefNo + "'", null);
                Log.v("FtranDet Deleted ", success + "");
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
    public int ExpDetByID(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_DAYEXPDET + " WHERE " + dbHelper.REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_DAYEXPDET, dbHelper.REFNO + "='" + RefNo + "'", null);
                Log.v("FtranDet Deleted ", success + "");
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

    public String getTotalExpenseSumReturns(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        // String selectQuery = "SELECT * FROM " + dbHelper.TABLE_REASON
        // +" WHERE "+dbHelper.REASON_NAME+"='"+name+"'";
        String selectQuery = "select sum(FD.Amt) as TotSum from DayExpDet FD where FD.RefNo = '" + refno + "'";
        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("TotSum")) != null)
                return cursor.getString(cursor.getColumnIndex("TotSum"));
            else
                return "0";

        }

        return "0";
    }

    // Delete Record from Fdayexpdet
    @SuppressWarnings("static-access")
    public int DeleteRec(String RefNo, String expcode) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_DAYEXPDET + " WHERE " + dbHelper.REFNO + "='" + RefNo + "' AND " + dbHelper.DAYEXPDET_EXPCODE + "='" + expcode + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_DAYEXPDET, dbHelper.REFNO + "='" + RefNo + "' AND " + dbHelper.DAYEXPDET_EXPCODE + "='" + expcode + "' ", null);
                Log.v("FtranDet Deleted ", success + "");
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
    public int restDataExp(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_DAYEXPDET + " WHERE " + dbHelper.REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(dbHelper.TABLE_DAYEXPDET, dbHelper.REFNO + " ='" + refno + "'", null);
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

    public ArrayList<DayExpDet> getTodayDEDets(String refno) {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayExpDet> list = new ArrayList<DayExpDet>();

        String selectQuery = "select * from DayExpDet WHERE " + dbHelper.REFNO + "='" + refno + "' and  TxnDate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                DayExpDet deDet = new DayExpDet();

                deDet.setEXPDET_EXPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.DAYEXPDET_EXPCODE)));
                deDet.setEXPDET_AMOUNT(cursor.getString(cursor.getColumnIndex(dbHelper.DAYEXPDET_AMT)));

                list.add(deDet);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }

}
