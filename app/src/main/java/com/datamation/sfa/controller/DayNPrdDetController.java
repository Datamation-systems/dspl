package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.DayNPrdDet;
import com.datamation.sfa.model.OrderDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayNPrdDetController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "Ebony";

    public DayNPrdDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateNonPrdDet(ArrayList<DayNPrdDet> list) {

        int serverdbID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (DayNPrdDet nondet : list) {
                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.REFNO + " = '" + nondet.getNONPRDDET_REFNO() + "'"
                        //;
                        + " AND " + DatabaseHelper.NONPRDDET_REASON_CODE + " = '" + nondet.getNONPRDDET_REASON_CODE() + "'";
                cursor = dB.rawQuery(selectQuery, null);

                values.put(DatabaseHelper.REFNO, nondet.getNONPRDDET_REFNO());
                values.put(DatabaseHelper.NONPRDDET_REASON, nondet.getNONPRDDET_REASON());
                values.put(DatabaseHelper.NONPRDDET_REASON_CODE, nondet.getNONPRDDET_REASON_CODE());
                values.put(DatabaseHelper.NONPRDDET_TXNDATE, nondet.getNONPRDDET_TXNDATE());
                values.put(DatabaseHelper.NONPRDDET_REPCODE, nondet.getNONPRDDET_REPCODE());
                values.put(DatabaseHelper.NONPRDDET_REMARK, nondet.getNONPRDDET_REMARK());

                int count = cursor.getCount();
                if (count > 0) {
                    serverdbID = (int) dB.update(DatabaseHelper.TABLE_NONPRDDET, values, DatabaseHelper.NONPRDDET_REASON_CODE + " =?", new String[]{String.valueOf(nondet.getNONPRDDET_REFNO())});

                } else {
                    serverdbID = (int) dB.insert(DatabaseHelper.TABLE_NONPRDDET, null, values);
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

    public ArrayList<DayNPrdDet> getTodayNPDets(String refno) {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdDet> list = new ArrayList<DayNPrdDet>();
        String selectQuery = "select * from FDaynPrdDet WHERE " + dbHelper.REFNO + "='" + refno + "' and  TxnDate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                DayNPrdDet npDet = new DayNPrdDet();

                npDet.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REASON)));
                npDet.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REASON_CODE)));
                npDet.setNONPRDDET_REPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REPCODE)));
                npDet.setNONPRDDET_REMARK(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REMARK)));

                list.add(npDet);

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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public ArrayList<DayNPrdDet> getAllnonprdDetails(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdDet> list = new ArrayList<DayNPrdDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            DayNPrdDet fnonset = new DayNPrdDet();

            fnonset.setNONPRDDET_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDDET_TXNDATE)));
            fnonset.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDDET_REASON)));
            fnonset.setNONPRDDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
            fnonset.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDDET_REASON_CODE)));

            list.add(fnonset);
        }

        return list;
    }
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

//    public String getDuplicate(String code, String RefNo) {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.NONPRDDET_DEBCODE + "='" + code + "' AND " + DatabaseHelper.REFNO + "='" + RefNo + "'";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//
//        while (cursor.moveToNext()) {
//            return cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDDET_DEBCODE));
//        }
//
//        return "";
//    }
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public int deleteOrdDetByID(String refNo, String ReasonCode) {

        int count = 0;
        int success = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.REFNO + "='" + refNo + "'" + " AND " + DatabaseHelper.NONPRDDET_REASON_CODE + " ='" + ReasonCode + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                success = dB.delete(DatabaseHelper.TABLE_NONPRDDET, DatabaseHelper.REFNO + "='" + refNo + "'" + " AND " + DatabaseHelper.NONPRDDET_REASON_CODE + " ='" + ReasonCode + "'", null);
                Log.v("FNONPRO_DET Deleted ", success + "");
            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return success;

    }

    public int deleteOrdDetByRefNo(String id) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.REFNO + "='" + id + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_NONPRDDET, DatabaseHelper.REFNO + "='" + id + "'", null);
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
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public int OrdDetByRefno(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_NONPRDDET, DatabaseHelper.REFNO + "='" + RefNo + "'", null);
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
    public int getNonProdCount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(RefNo) as RefNo FROM " + DatabaseHelper.TABLE_NONPRDDET +  " WHERE  " + DatabaseHelper.REFNO + "='" + refNo + "'";
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

    public ArrayList<DayNPrdDet> getAllActiveNPs() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdDet> list = new ArrayList<DayNPrdDet>();

        String selectQuery = "select * from " + dbHelper.TABLE_NONPRDDET + " WHERE " + dbHelper.ORDDET_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                DayNPrdDet ordDet = new DayNPrdDet();

                ordDet.setNONPRDDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_ID)));
                ordDet.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REASON)));
                ordDet.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REASON_CODE)));
                ordDet.setNONPRDDET_REPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REPCODE)));
                ordDet.setNONPRDDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REFNO)));

                list.add(ordDet);

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

    public DayNPrdDet getActiveNPRefNo()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        DayNPrdDet ordDet = new DayNPrdDet();

        String selectQuery = "select * from " + dbHelper.TABLE_NONPRDDET + " WHERE " + dbHelper.ORDDET_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                ordDet.setNONPRDDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_ID)));
                ordDet.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REASON)));
                ordDet.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REASON_CODE)));
                ordDet.setNONPRDDET_REPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REPCODE)));
                ordDet.setNONPRDDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDDET_REFNO)));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return ordDet;
    }

    public boolean isAnyActiveNPs()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from " + dbHelper.TABLE_NONPRDHED + " WHERE " + dbHelper.NONPRDHED_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            if (cursor.getCount()>0)
            {
                return true;
            }
            else
            {
                return false;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return false;
    }
}
