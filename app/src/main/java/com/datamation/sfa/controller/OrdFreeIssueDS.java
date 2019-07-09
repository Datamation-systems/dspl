package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.OrdFreeIssue;

import java.util.ArrayList;

public class OrdFreeIssueDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "OrdFreeIssueDS";

    public OrdFreeIssueDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateOrderFreeIssue(OrdFreeIssue ordFreeIssue) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.REFNO, ordFreeIssue.getOrdFreeIssue_RefNo());
            values.put(DatabaseHelper.TXNDATE, ordFreeIssue.getOrdFreeIssue_TxnDate());
            values.put(DatabaseHelper.FORDFREEISS_REFNO1, ordFreeIssue.getOrdFreeIssue_RefNo1());
            values.put(DatabaseHelper.FORDFREEISS_ITEMCODE, ordFreeIssue.getOrdFreeIssue_ItemCode());
            values.put(DatabaseHelper.FORDFREEISS_QTY, ordFreeIssue.getOrdFreeIssue_Qty());

            dB.insert(DatabaseHelper.TABLE_FORDFREEISS, null, values);
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {

            dB.close();
        }

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void ClearFreeIssues(String RefNo) {

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FORDFREEISS + " WHERE " + DatabaseHelper.REFNO + " = '" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(DatabaseHelper.TABLE_FORDFREEISS, DatabaseHelper.REFNO + " = '" + RefNo + "'", null);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }

    public void RemoveFreeIssue(String RefNo, String itemCode) {

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FORDFREEISS + " WHERE " + DatabaseHelper.REFNO + " = '" + RefNo + "' AND itemcode='" + itemCode + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(DatabaseHelper.TABLE_FORDFREEISS, DatabaseHelper.REFNO + " = '" + RefNo + "' AND itemcode='" + itemCode + "'", null);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<OrdFreeIssue> getAllFreeIssues(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrdFreeIssue> list = new ArrayList<OrdFreeIssue>();

        try {
            Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FORDFREEISS + " WHERE RefNo1='" + Refno + "'", null);

            while (cursor.moveToNext()) {

                OrdFreeIssue freeIssue = new OrdFreeIssue();

                freeIssue.setOrdFreeIssue_ItemCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDFREEISS_ITEMCODE)));
                freeIssue.setOrdFreeIssue_Qty(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDFREEISS_QTY)));
                freeIssue.setOrdFreeIssue_RefNo1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                freeIssue.setOrdFreeIssue_RefNo(cursor.getString(cursor.getColumnIndex("RefNo1")));
                freeIssue.setOrdFreeIssue_TxnDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                list.add(freeIssue);
            }
            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

}
