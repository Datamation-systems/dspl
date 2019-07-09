package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.DispDet;
import com.datamation.sfa.model.InvDet;

import java.util.ArrayList;

public class DispDetDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "DispHedDS ";

    public DispDetDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int updateDispDet(ArrayList<InvDet> list, String dispRefno) {

        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (InvDet invDet : list) {

                count = 0;

                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FDISPDET_AMT, invDet.getFINVDET_AMT());
                values.put(DatabaseHelper.FDISPDET_COSTPRICE, invDet.getFINVDET_SELL_PRICE());

                if (invDet.getFINVDET_TYPE().equals("SA")) {
                    values.put(DatabaseHelper.FDISPDET_BALFIQTY, invDet.getFINVDET_QTY());
                    values.put(DatabaseHelper.FDISPDET_BALQTY, invDet.getFINVDET_QTY());
                    values.put(DatabaseHelper.FDISPDET_BALSAQTY, invDet.getFINVDET_QTY());
                    values.put(DatabaseHelper.FDISPDET_QTY, invDet.getFINVDET_QTY());
                    values.put(DatabaseHelper.FDISPDET_SAQTY, invDet.getFINVDET_QTY());
                } else
                    values.put(DatabaseHelper.FDISPDET_FIQTY, invDet.getFINVDET_QTY());

                values.put(DatabaseHelper.FDISPDET_ITEMCODE, invDet.getFINVDET_ITEM_CODE());
                values.put(DatabaseHelper.FDISPDET_REFNO, dispRefno);
                values.put(DatabaseHelper.FDISPDET_REFNO1, invDet.getFINVDET_REFNO());
                values.put(DatabaseHelper.FDISPDET_SEQNO, invDet.getFINVDET_SEQNO());
                values.put(DatabaseHelper.FDISPDET_TXNDATE, invDet.getFINVDET_TXN_DATE());
                values.put(DatabaseHelper.FDISPDET_TXNTYPE, "23");

                count = (int) dB.insert(DatabaseHelper.TABLE_FDISPDET, null, values);
            }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
            return 0;
        } finally {
            dB.close();
        }

        return count;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int clearTable(String refNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            count = dB.delete(DatabaseHelper.TABLE_FDISPDET, DatabaseHelper.FDISPDET_REFNO1 + " = '" + refNo + "'", null);

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return count;

    }


    public ArrayList<DispDet> getUploadData(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DispDet> list = new ArrayList<DispDet>();

        Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FDISPDET + " WHERE refno1='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            DispDet dispDet = new DispDet();

            dispDet.setFDISPDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_AMT)));
            dispDet.setFDISPDET_BALFIQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_BALFIQTY)));
            dispDet.setFDISPDET_BALQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_BALQTY)));
            dispDet.setFDISPDET_BALSAQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_BALSAQTY)));
            dispDet.setFDISPDET_COSTPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_COSTPRICE)));
            dispDet.setFDISPDET_FIQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_FIQTY)));
            dispDet.setFDISPDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_ITEMCODE)));
            dispDet.setFDISPDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_QTY)));
            dispDet.setFDISPDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_REFNO)));
            dispDet.setFDISPDET_SAQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_SAQTY)));
            dispDet.setFDISPDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_SEQNO)));
            dispDet.setFDISPDET_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_TXNDATE)));
            dispDet.setFDISPDET_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPDET_TXNTYPE)));
            list.add(dispDet);
        }

        cursor.close();
        dB.close();
        return list;
    }


}
