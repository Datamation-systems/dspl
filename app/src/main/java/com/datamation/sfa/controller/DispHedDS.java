package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.DispHed;
import com.datamation.sfa.model.InvHed;

import java.util.ArrayList;

public class DispHedDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "DispHedDS";

    public DispHedDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int updateHeader(InvHed invHed, String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        int count = 0;

        try {
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FDISPHED_ADDDATE, invHed.getFINVHED_ADDDATE());
            values.put(DatabaseHelper.FDISPHED_ADDMACH, invHed.getFINVHED_ADDMACH());
            values.put(DatabaseHelper.FDISPHED_ADDUSER, invHed.getFINVHED_ADDUSER());
            values.put(DatabaseHelper.FDISPHED_CONTACT, invHed.getFINVHED_CONTACT());
            values.put(DatabaseHelper.FDISPHED_COSTCODE, invHed.getFINVHED_COSTCODE());
            values.put(DatabaseHelper.FDISPHED_CUSADD1, invHed.getFINVHED_CUSADD1());
            values.put(DatabaseHelper.FDISPHED_CUSADD2, invHed.getFINVHED_CUSADD2());
            values.put(DatabaseHelper.FDISPHED_CUSADD3, invHed.getFINVHED_CUSADD3());
            values.put(DatabaseHelper.FDISPHED_CUSTELE, invHed.getFINVHED_CUSTELE());
            values.put(DatabaseHelper.FDISPHED_DEBCODE, invHed.getFINVHED_DEBCODE());
            values.put(DatabaseHelper.FDISPHED_INVOICE, "1");
            values.put(DatabaseHelper.FDISPHED_LOCCODE, invHed.getFINVHED_LOCCODE());
            values.put(DatabaseHelper.FDISPHED_MANUREF, invHed.getFINVHED_MANUREF());
            values.put(DatabaseHelper.FDISPHED_REFNO, Refno);
            values.put(DatabaseHelper.FDISPHED_REFNO1, invHed.getFINVHED_REFNO());
            values.put(DatabaseHelper.FDISPHED_REMARKS, invHed.getFINVHED_REMARKS());
            values.put(DatabaseHelper.FDISPHED_REPCODE, invHed.getFINVHED_REPCODE());
            values.put(DatabaseHelper.FDISPHED_TOTALAMT, invHed.getFINVHED_TOTALAMT());
            values.put(DatabaseHelper.FDISPHED_TXNDATE, invHed.getFINVHED_TXNDATE());
            values.put(DatabaseHelper.FDISPHED_TXNTYPE, "23");

            count = (int) dB.insert(DatabaseHelper.TABLE_FDISPHED, null, values);

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
            return 0;
        } finally {
            dB.close();
        }

        return count;
    }

    public int clearTable(String refNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            count = dB.delete(DatabaseHelper.TABLE_FDISPHED, DatabaseHelper.FDISPHED_REFNO1 + " = '" + refNo + "'", null);

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return count;

    }

    public ArrayList<DispHed> getUploadData(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DispHed> list = new ArrayList<DispHed>();

        Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FDISPHED + " WHERE refno1='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            DispHed dispHed = new DispHed();

            dispHed.setFDISPHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_ADDDATE)));
            dispHed.setFDISPHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_ADDMACH)));
            dispHed.setFDISPHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_ADDUSER)));
            dispHed.setFDISPHED_CONTACT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_CONTACT)));
            dispHed.setFDISPHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_COSTCODE)));
            dispHed.setFDISPHED_CUSADD1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_CUSADD1)));
            dispHed.setFDISPHED_CUSADD2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_CUSADD2)));
            dispHed.setFDISPHED_CUSADD3(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_CUSADD3)));
            dispHed.setFDISPHED_CUSTELE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_CUSTELE)));
            dispHed.setFDISPHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_DEBCODE)));
            dispHed.setFDISPHED_INVOICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_INVOICE)));
            dispHed.setFDISPHED_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_LOCCODE)));
            dispHed.setFDISPHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_MANUREF)));
            dispHed.setFDISPHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_REFNO)));
            dispHed.setFDISPHED_REFNO1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_REFNO1)));
            dispHed.setFDISPHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_REMARKS)));
            dispHed.setFDISPHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_REPCODE)));
            dispHed.setFDISPHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_TOTALAMT)));
            dispHed.setFDISPHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_TXNDATE)));
            dispHed.setFDISPHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPHED_TXNTYPE)));
            list.add(dispHed);
        }

        cursor.close();
        dB.close();
        return list;

    }

}
