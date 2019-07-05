package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FInvRDet;

import java.util.ArrayList;

public class SalesReturnDetController
{
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "SFA";

    public SalesReturnDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateInvRDet(ArrayList<FInvRDet> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (FInvRDet invrDet : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_INVRDET + " WHERE " + dbHelper.INVRDET_ID + " = '" + invrDet.getFINVRDET_ID() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(dbHelper.REFNO, invrDet.getFINVRDET_REFNO());
                values.put(dbHelper.INVRDET_TXN_DATE, invrDet.getFINVRDET_TXN_DATE());
                values.put(dbHelper.INVRDET_QTY, invrDet.getFINVRDET_QTY());
                values.put(dbHelper.INVRDET_BAL_QTY, invrDet.getFINVRDET_BAL_QTY());
                values.put(dbHelper.INVRDET_ITEMCODE, invrDet.getFINVRDET_ITEMCODE());
                values.put(dbHelper.INVRDET_TAXCOMCODE, invrDet.getFINVRDET_TAXCOMCODE());
                values.put(dbHelper.INVRDET_PRILCODE, invrDet.getFINVRDET_PRILCODE());
                values.put(dbHelper.INVRDET_IS_ACTIVE, invrDet.getFINVRDET_IS_ACTIVE());
                values.put(dbHelper.INVRDET_COST_PRICE, invrDet.getFINVRDET_COST_PRICE());
                values.put(dbHelper.INVRDET_SELL_PRICE, invrDet.getFINVRDET_SELL_PRICE());
                values.put(dbHelper.INVRDET_T_SELL_PRICE, invrDet.getFINVRDET_T_SELL_PRICE());
                values.put(dbHelper.INVRDET_AMT, invrDet.getFINVRDET_AMT());
                values.put(dbHelper.INVRDET_DIS_AMT, invrDet.getFINVRDET_DIS_AMT());
                values.put(dbHelper.INVRDET_TAX_AMT, invrDet.getFINVRDET_TAX_AMT());
                values.put(dbHelper.INVRDET_TXN_TYPE, invrDet.getFINVRDET_TXN_TYPE());
                values.put(dbHelper.INVRDET_SEQNO, invrDet.getFINVRDET_SEQNO());
                values.put(dbHelper.INVRDET_REASON_NAME, invrDet.getFINVRDET_RETURN_REASON());
                values.put(dbHelper.INVRDET_REASON_CODE, invrDet.getFINVRDET_RETURN_REASON_CODE());
                values.put(dbHelper.INVRDET_RETURN_TYPE, invrDet.getFINVRDET_RETURN_TYPE());
                //values.put(dbHelper.FINVRDET_FREE_QTY, invrDet.getFr());
                values.put(dbHelper.INVRDET_PRICE, invrDet.getFINVRDET_SELL_PRICE());
                values.put(dbHelper.INVRDET_CHANGED_PRICE, invrDet.getFINVRDET_CHANGED_PRICE());

                int cn = cursor.getCount();
                if (cn > 0) {

                    count = dB.update(dbHelper.TABLE_INVRDET, values, dbHelper.INVRDET_ID + " =?", new String[]{String
                            .valueOf(invrDet.getFINVRDET_ID())});

                } else {
                    count = (int) dB.insert(dbHelper.TABLE_INVRDET, null, values);
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
        return count;
    }

    public ArrayList<FInvRDet> getAllInvRDet(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_INVRDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            FInvRDet invrDet = new FInvRDet();

            invrDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_ID)));
            invrDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_AMT)));
            invrDet.setFINVRDET_COST_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_COST_PRICE)));
            invrDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_SELL_PRICE)));
            invrDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_T_SELL_PRICE)));
            invrDet.setFINVRDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_IS_ACTIVE)));
            invrDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_ITEMCODE)));
            invrDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_QTY)));
            invrDet.setFINVRDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_BAL_QTY)));
            invrDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
            invrDet.setFINVRDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_TXN_DATE)));
            invrDet.setFINVRDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_TAXCOMCODE)));
            invrDet.setFINVRDET_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_PRILCODE)));
            invrDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_DIS_AMT)));
            invrDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_TAX_AMT)));
            invrDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_TXN_TYPE)));
            invrDet.setFINVRDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_SEQNO)));
            invrDet.setFINVRDET_RETURN_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_REASON_NAME)));
            invrDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_REASON_CODE)));
            invrDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_RETURN_TYPE)));
            //invrDet.setFINVRDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_PRICE)));
            invrDet.setFINVRDET_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_CHANGED_PRICE)));
            //invrDet.setFINVRDET_DIS_RATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_DIS_RATE)));
            list.add(invrDet);
        }

        return list;
    }

    public int restData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            count = dB.delete(DatabaseHelper.TABLE_INVRDET, DatabaseHelper.REFNO + " ='" + refno + "'", null);

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

    public int getItemCount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(RefNo) as RefNoCount FROM " + DatabaseHelper.TABLE_INVRDET + " WHERE  " + DatabaseHelper.REFNO + "='" + refNo + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                return Integer.parseInt(cursor.getString(cursor.getColumnIndex("RefNoCount")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }
        return 0;

    }

    public int InactiveStatusUpdate(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVRDET + " WHERE " + DatabaseHelper.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FINVRDET_IS_ACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(DatabaseHelper.TABLE_FINVRDET, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(refno)});
            } else {
                count = (int) dB.insert(DatabaseHelper.TABLE_FINVRDET, null, values);
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

    public ArrayList<FInvRDet> getReturnItemsforPrint(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_INVRDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";

        try {

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                FInvRDet reDet = new FInvRDet();

                reDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_AMT)));
                reDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_ITEMCODE)));
                reDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_QTY)));
                reDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_TXN_TYPE)));
                reDet.setFINVRDET_REFNO(refno);
                reDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_DIS_AMT)));
                //reDet.setFTRANSODET_SCHDISC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTRANSODET_DISVALAMT)));
                reDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_SELL_PRICE)));
                reDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_T_SELL_PRICE)));
                reDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_RETURN_TYPE)));
                reDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRDET_TAX_AMT)));

                list.add(reDet);
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
