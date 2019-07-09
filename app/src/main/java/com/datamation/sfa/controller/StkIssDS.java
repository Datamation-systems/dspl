package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.StkIn;
import com.datamation.sfa.model.StkIss;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StkIssDS {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbeHelper;
    private String TAG = "SizeIssDS";

    public StkIssDS(Context context) {
        this.context = context;
        dbeHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbeHelper.getWritableDatabase();
    }

    public void InsertSTKIssData(StkIn size, String RefNo, String Qty, String LocCode) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FSTKISS_ITEMCODE, size.getITEMCODE());
            values.put(DatabaseHelper.FSTKISS_REFNO, RefNo);
            values.put(DatabaseHelper.FSTKISS_QTY, Qty);
            values.put(DatabaseHelper.FSTKISS_STKRECNO, size.getSTKRECNO());
            values.put(DatabaseHelper.FSTKISS_STKRECDATE, size.getSTKRecDate());
            values.put(DatabaseHelper.FSTKISS_LOCCODE, LocCode);
            values.put(DatabaseHelper.FSTKISS_STKTXNDATE, size.getTXNDATE());
            values.put(DatabaseHelper.FSTKISS_STKTXNTYPE, size.getTXNTYPE());
            values.put(DatabaseHelper.FSTKISS_STKTXNNO, size.getREFNO());
            values.put(DatabaseHelper.FSTKISS_COSTPRICE, size.getCOSTPRICE());

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            values.put(DatabaseHelper.FSTKISS_TXNDATE, dateFormat.format(date));

            count = (int) dB.insert(DatabaseHelper.TABLE_FSTKISS, null, values);
            Log.v(TAG, "Inserted " + count);

        } catch (Exception e) {
            Log.v("Exception", e.toString());
        } finally {
            dB.close();
        }

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*/

    public void resetData(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double balQty = 0;

        try {
            Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FSTKISS + " WHERE RefNo='" + Refno + "'", null);

            while (cursor.moveToNext()) {

                String itemCode = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_ITEMCODE));
                String StkTxnNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_STKTXNNO));
                String StkRecNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_STKRECNO));
                String locCode = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_LOCCODE));
                double Qty = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_QTY)));

                Cursor curStkIn = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FSTKIN + " WHERE RefNo='" + StkTxnNo + "' AND ItemCode='" + itemCode + "'  AND STKRecNo='" + StkRecNo + "' AND LocCode='" + locCode + "'", null);

                while (curStkIn.moveToNext()) {
                    balQty = Double.parseDouble(curStkIn.getString(curStkIn.getColumnIndex(DatabaseHelper.FSTKIN_BALQTY)));
                }

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.FSTKIN_BALQTY, String.valueOf((int) (Qty + balQty)));
                /* reset balqty values */
                dB.update(DatabaseHelper.TABLE_FSTKIN, values, DatabaseHelper.FSTKIN_REFNO + " =? AND " + DatabaseHelper.FSTKIN_STKRECNO + "=? AND " + DatabaseHelper.FSTKIN_ITEMCODE + "=? AND " + DatabaseHelper.FSTKIN_LOCCODE + "=?", new String[]{StkTxnNo, StkRecNo, itemCode, locCode});

                curStkIn.close();
            }
            cursor.close();
            ClearTable(Refno);

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<StkIss> getUploadData(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<StkIss> list = new ArrayList<StkIss>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FSTKISS + " WHERE " + DatabaseHelper.FSTKISS_REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                StkIss sizeIss = new StkIss();
                sizeIss.setCOSTPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_COSTPRICE)));
                sizeIss.setID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_ID)));
                sizeIss.setITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_ITEMCODE)));
                sizeIss.setLOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_LOCCODE)));
                sizeIss.setQTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_QTY)));
                sizeIss.setREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_REFNO)));
                sizeIss.setSTKRECDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_STKRECDATE)));
                sizeIss.setSTKRECNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_STKRECNO)));
                sizeIss.setSTKTXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_STKTXNDATE)));
                sizeIss.setSTKTXNNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_STKTXNNO)));
                sizeIss.setSTKTXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_STKTXNTYPE)));
                sizeIss.setTXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSTKISS_TXNDATE)));

                list.add(sizeIss);

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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void ClearTable(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {

            dB.delete(DatabaseHelper.TABLE_FSTKISS, DatabaseHelper.FSTKISS_REFNO + "='" + RefNo + "'", null);
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

    }

}
