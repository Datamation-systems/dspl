package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.OrderDisc;

import java.util.ArrayList;

public class OrderDiscDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi orderDisc";

    public OrderDiscDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateOrdDisc(ArrayList<OrderDisc> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (OrderDisc orderDisc : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FORDDISC + " WHERE " + dbHelper.REFNO + " = '" + orderDisc.getRefNo() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(dbHelper.REFNO, orderDisc.getRefNo());
                values.put(dbHelper.FORDDISC_TXNDATE, orderDisc.getTxnDate());
                values.put(dbHelper.FORDDISC_REFNO1, orderDisc.getRefNo1());
                values.put(dbHelper.FORDDISC_ITEMCODE, orderDisc.getItemCode());
                values.put(dbHelper.FORDDISC_DISAMT, orderDisc.getDisAmt());
                values.put(dbHelper.FORDDISC_DISPER, orderDisc.getDisPer());

                int cn = cursor.getCount();
                if (cn > 0) {

                    count = dB.update(dbHelper.TABLE_FORDDISC, values, dbHelper.REFNO + " =?", new String[]{String.valueOf(orderDisc.getRefNo())});

                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FORDDISC, null, values);
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

	/*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*--*-Update order discount table-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateOrderDiscount(OrderDisc orderDisc, String DiscRef, String DiscPer) {

		/* Remove record using order discount ref no & item code */
        RemoveOrderDiscRecord(orderDisc.getRefNo(), orderDisc.getItemCode());

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.REFNO, orderDisc.getRefNo());
            values.put(DatabaseHelper.FORDDISC_TXNDATE, orderDisc.getTxnDate());
            values.put(DatabaseHelper.FORDDISC_REFNO1, DiscRef);
            values.put(DatabaseHelper.FORDDISC_ITEMCODE, orderDisc.getItemCode());
            values.put(DatabaseHelper.FORDDISC_DISAMT, orderDisc.getDisAmt());
            values.put(DatabaseHelper.FORDDISC_DISPER, DiscPer);
            dB.insert(DatabaseHelper.TABLE_FORDDISC, null, values);

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {

            dB.close();
        }

    }

	/*-*-*-*-*-*-*-*-*Check record availability using RefNo and Itemcode in FORDDISC*-*-*-*-*-*-*-*-*-*-*-*/

    public boolean isRecordAvailable(String RefNo, String ItemCode) {

        int count = 0;
        boolean Result = false;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FORDDISC + " WHERE " + DatabaseHelper.REFNO + " = '" + RefNo + "' AND " + DatabaseHelper.FORDDISC_ITEMCODE + " = '" + ItemCode + "'";
            cursor = dB.rawQuery(selectQuery, null);
            count = cursor.getCount();

            if (count > 0) {
                Result = true;
            } else {
                Result = false;
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

	/*-*-*-*-*-*-*-*- Remove particular record from Order Discount table -*-*-* *-*-*-*-*-*/

    public void RemoveOrderDiscRecord(String RefNo, String ItemCode) {

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FORDDISC + " WHERE " + dbHelper.REFNO + " = '" + RefNo + "' AND " + dbHelper.FORDDISC_ITEMCODE + " = '" + ItemCode + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(dbHelper.TABLE_FORDDISC, dbHelper.REFNO + " = '" + RefNo + "' AND " + dbHelper.FORDDISC_ITEMCODE + " = '" + ItemCode + "'", null);
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


    public void clearData(String RefNo) {

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FORDDISC + " WHERE " + dbHelper.REFNO + " = '" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(dbHelper.TABLE_FORDDISC, dbHelper.REFNO + " = '" + RefNo + "'", null);
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

	

	/* Delete all records */

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FORDDISC, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FORDDISC, null, null);
                Log.v("Success", success + "");
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

	/*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**--*--*-*-*-*-*-*-*-*-*-*-*-*-**/

    public ArrayList<OrderDisc> getAllOrderDiscs(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDisc> list = new ArrayList<OrderDisc>();
        try {

            Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FORDDISC + " WHERE RefNo='" + Refno + "'", null);

            while (cursor.moveToNext()) {
                OrderDisc orderDisc = new OrderDisc();

                orderDisc.setDisAmt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDISC_DISAMT)));
                orderDisc.setDisPer(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDISC_DISPER)));
                orderDisc.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDISC_ITEMCODE)));
                orderDisc.setRefNo(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                orderDisc.setRefNo1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDISC_REFNO1)));
                orderDisc.setTxnDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDISC_TXNDATE)));
                list.add(orderDisc);
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
