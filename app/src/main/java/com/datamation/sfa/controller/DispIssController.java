package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.DispIss;
import com.datamation.sfa.model.StkIss;

import java.util.ArrayList;

public class DispIssController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "DispHedDS";

    public DispIssController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int updateDispIss(ArrayList<StkIss> list, String disRefno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (StkIss iss : list) {

                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FDISPISS_AMT, String.format("%.2f", Double.parseDouble(iss.getQTY()) * Double.parseDouble(iss.getCOSTPRICE())));
                values.put(DatabaseHelper.FDISPISS_BALQTY, iss.getQTY());
                values.put(DatabaseHelper.FDISPISS_COSTPRICE, iss.getCOSTPRICE());
                values.put(DatabaseHelper.FDISPISS_QTY, iss.getQTY());
                values.put(DatabaseHelper.FDISPISS_ITEMCODE, iss.getITEMCODE());
                values.put(DatabaseHelper.FDISPISS_OTHCOST, "0");
                values.put(DatabaseHelper.REFNO, disRefno);
                values.put(DatabaseHelper.FDISPISS_STKRECNO, iss.getSTKRECNO());
                values.put(DatabaseHelper.FDISPISS_STKRECDATE, iss.getSTKRECDATE());
                values.put(DatabaseHelper.FDISPISS_LOCCODE, iss.getLOCCODE());
                values.put(DatabaseHelper.FDISPISS_STKTXNDATE, iss.getSTKTXNDATE());
                values.put(DatabaseHelper.FDISPISS_STKTXNTYPE, iss.getSTKTXNTYPE());
                values.put(DatabaseHelper.FDISPISS_STKTXNNO, iss.getSTKTXNNO());
                values.put(DatabaseHelper.TXNDATE, iss.getTXN_DATE());
                values.put(DatabaseHelper.FDISPISS_REFNO1, iss.getREFNO());

                count = (int) dB.insert(DatabaseHelper.TABLE_FDISPISS, null, values);

            }

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

            count = dB.delete(DatabaseHelper.TABLE_FDISPISS, DatabaseHelper.FDISPISS_REFNO1 + " = '" + refNo + "'", null);

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return count;

    }


    public ArrayList<DispIss> getUploadData(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DispIss> list = new ArrayList<DispIss>();

        Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FDISPISS + " WHERE RefNo1='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            DispIss dispIss = new DispIss();

            dispIss.setFDISPISS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_AMT)));
            dispIss.setFDISPISS_BALQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_BALQTY)));
            dispIss.setFDISPISS_COSTPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_COSTPRICE)));
            dispIss.setFDISPISS_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_ITEMCODE)));
            dispIss.setFDISPISS_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_LOCCODE)));
            dispIss.setFDISPISS_OTHCOST(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_OTHCOST)));
            dispIss.setFDISPISS_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_QTY)));
            dispIss.setFDISPISS_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
            dispIss.setFDISPISS_STKRECDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_STKRECDATE)));
            dispIss.setFDISPISS_STKRECNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_STKRECNO)));
            dispIss.setFDISPISS_STKTXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_STKTXNDATE)));
            dispIss.setFDISPISS_STKTXNNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_STKTXNNO)));
            dispIss.setFDISPISS_STKTXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDISPISS_STKTXNTYPE)));
            dispIss.setFDISPISS_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));

            list.add(dispIss);
        }

        cursor.close();
        dB.close();
        return list;
    }


}
