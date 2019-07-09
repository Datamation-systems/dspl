package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.StkIn;

import java.util.ArrayList;

public class STKInDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbeHelper;

    public STKInDS(Context context) {
        this.context = context;
        dbeHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbeHelper.getWritableDatabase();
    }

    public void createUpdateSTKIn(ArrayList<StkIn> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (StkIn stkIn : list) {

                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FSTKIN_COSTPRICE, stkIn.getCOSTPRICE());
                values.put(DatabaseHelper.FSTKIN_INQTY, stkIn.getINQTY());
                values.put(DatabaseHelper.FSTKIN_ITEMCODE, stkIn.getITEMCODE());
                values.put(DatabaseHelper.FSTKIN_LOCCODE, stkIn.getLOCCODE());
                values.put(DatabaseHelper.FSTKIN_BALQTY, stkIn.getBALQTY());
                values.put(DatabaseHelper.FSTKIN_OTHCOST, stkIn.getOTHCOST());
                values.put(DatabaseHelper.FSTKIN_REFNO, stkIn.getREFNO());
                values.put(DatabaseHelper.FSTKIN_STKREC_DATE, stkIn.getSTKRecDate());
                values.put(DatabaseHelper.FSTKIN_STKRECNO, stkIn.getSTKRECNO());
                values.put(DatabaseHelper.FSTKIN_TXNDATE, stkIn.getTXNDATE());
                values.put(DatabaseHelper.FSTKIN_TXNTYPE, stkIn.getTXNTYPE());

                dB.insert(DatabaseHelper.TABLE_FSTKIN, null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            dB.close();
        }

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*/

    public int resetData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FSTKIN + " WHERE " + DatabaseHelper.FSTKIN_REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(DatabaseHelper.TABLE_FSTKIN, DatabaseHelper.FSTKIN_REFNO + " ='" + refno + "'", null);
                Log.v("Success Stauts", count + "");
            }
            cursor.close();
        } catch (Exception e) {
            Log.v(" Exception", e.toString());
        } finally {
            dB.close();
        }
        return count;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*/

    public ArrayList<StkIn> UnsyncedSizeInRecords(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<StkIn> list = new ArrayList<StkIn>();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FSTKIN + " WHERE " + DatabaseHelper.FSTKIN_REFNO + " = '" + refno + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                StkIn stkin = new StkIn();

                stkin.setBALQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_BALQTY)));
                stkin.setCOSTPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_COSTPRICE)));
                stkin.setINQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_INQTY)));
                stkin.setITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_ITEMCODE)));
                stkin.setLOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_LOCCODE)));
                stkin.setREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_REFNO)));
                stkin.setSTKRecDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_STKREC_DATE)));
                stkin.setSTKRECNO(cursor.getString(cursor.getColumnIndex("StkRecNo")));
                stkin.setTXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_TXNDATE)));
                stkin.setTXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_TXNTYPE)));
                stkin.setOTHCOST(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_OTHCOST)));
                list.add(stkin);

            }
            cursor.close();
        } catch (Exception e) {
            Log.v(" Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

    public void deleteAll() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        dB.delete(DatabaseHelper.TABLE_FSTKIN, null, null);

        dB.close();

    }

    public ArrayList<StkIn> getAscendingGRNList(String ItemCode, String LocCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<StkIn> GRNList = new ArrayList<StkIn>();

        try {

            String selectQuery = "SELECT *  FROM fstkin  WHERE ItemCode ='" + ItemCode + "' AND loccode='" + LocCode + "' ORDER BY StkRecDate ASC";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                StkIn stkin = new StkIn();

                stkin.setBALQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_BALQTY)));
                stkin.setCOSTPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_COSTPRICE)));
                stkin.setINQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_INQTY)));
                stkin.setITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_ITEMCODE)));
                stkin.setLOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_LOCCODE)));
                stkin.setREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_REFNO)));
                stkin.setSTKRecDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_STKREC_DATE)));
                stkin.setSTKRECNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_STKRECNO)));
                stkin.setTXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_TXNDATE)));
                stkin.setTXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_TXNTYPE)));
                stkin.setOTHCOST(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_OTHCOST)));
                stkin.setID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKIN_ID)));

                GRNList.add(stkin);

            }
            cursor.close();

        } catch (Exception e) {
            Log.v("StkIn", e.toString());
        } finally {
            dB.close();
        }

        return GRNList;

    }

    public int UpdateBalQtyByFIFO(ArrayList<StkIn> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (StkIn stkIn : list) {

                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FSTKIN_COSTPRICE, stkIn.getCOSTPRICE());
                values.put(DatabaseHelper.FSTKIN_INQTY, stkIn.getINQTY());
                values.put(DatabaseHelper.FSTKIN_ITEMCODE, stkIn.getITEMCODE());
                values.put(DatabaseHelper.FSTKIN_LOCCODE, stkIn.getLOCCODE());
                values.put(DatabaseHelper.FSTKIN_BALQTY, stkIn.getBALQTY());
                values.put(DatabaseHelper.FSTKIN_OTHCOST, "");
                values.put(DatabaseHelper.FSTKIN_REFNO, stkIn.getREFNO());
                values.put(DatabaseHelper.FSTKIN_STKREC_DATE, stkIn.getSTKRecDate());
                values.put(DatabaseHelper.FSTKIN_STKRECNO, stkIn.getSTKRECNO());
                values.put(DatabaseHelper.FSTKIN_TXNDATE, stkIn.getTXNDATE());
                values.put(DatabaseHelper.FSTKIN_TXNTYPE, stkIn.getTXNTYPE());

                dB.update(DatabaseHelper.TABLE_FSTKIN, values, DatabaseHelper.FSTKIN_ID + "=?", new String[]{stkIn.getID().toString()});

            }
        } catch (Exception e) {

            Log.v("STKIN" + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;

    }

}
