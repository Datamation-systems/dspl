package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FreeHed;

import java.util.ArrayList;

public class FreeHedDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";

    public FreeHedDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateFreeHed(ArrayList<FreeHed> list) {

        int returnID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        Cursor cursor_ini = null;

        try {
            cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FFREESLAB, null);

            for (FreeHed freehed : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.REFNO, freehed.getFFREEHED_REFNO());
                values.put(dbHelper.TXNDATE, freehed.getFFREEHED_TXNDATE());
                values.put(dbHelper.FFREEHED_DISC_DESC, freehed.getFFREEHED_DISC_DESC());
                values.put(dbHelper.FFREEHED_PRIORITY, freehed.getFFREEHED_PRIORITY());
                values.put(dbHelper.FFREEHED_VDATEF, freehed.getFFREEHED_VDATEF());
                values.put(dbHelper.FFREEHED_VDATET, freehed.getFFREEHED_VDATET());
                values.put(dbHelper.FFREEHED_REMARKS, freehed.getFFREEHED_REMARKS());
                values.put(dbHelper.FFREEHED_RECORD_ID, freehed.getFFREEHED_RECORD_ID());
                values.put(dbHelper.FFREEHED_ITEM_QTY, freehed.getFFREEHED_ITEM_QTY());
                values.put(dbHelper.FFREEHED_FREE_IT_QTY, freehed.getFFREEHED_FREE_IT_QTY());
                values.put(dbHelper.FFREEHED_FTYPE, freehed.getFFREEHED_FTYPE());
               // values.put(dbHelper.FFREEHED_COSTCODE, freehed.getFFREEHED_COSTCODE());

                if (cursor_ini.getCount() > 0) {
                    String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FFREESLAB + " WHERE " + dbHelper.REFNO + "='" + freehed.getFFREEHED_REFNO() + "'";
                    cursor = dB.rawQuery(selectQuery, null);

                    if (cursor.getCount() > 0) {
                        returnID = (int) dB.update(dbHelper.TABLE_FFREESLAB, values, dbHelper.REFNO + "='" + freehed.getFFREEHED_REFNO() + "'", null);
                    } else {
                        returnID = (int) dB.insert(dbHelper.TABLE_FFREEHED, null, values);
                    }

                } else {
                    returnID = (int) dB.insert(dbHelper.TABLE_FFREEHED, null, values);
                }

            }
        } catch (Exception e) {

            Log.v("Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (cursor_ini != null) {
                cursor_ini.close();
            }
            dB.close();
        }
        return returnID;

    }

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FFREEHED, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FFREEHED, null, null);
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

    public ArrayList<FreeHed> getFreeIssueItemDetailByRefno(String itemCode, String costCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

       // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
    // inoshi--Mine**CostCode change//
        String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "')  AND date('now') between vdatef and vdatet";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
        while (cursor.moveToNext()) {

            FreeHed freeHed = new FreeHed();

            freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
            freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(dbHelper.TXNDATE)));
            freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_DISC_DESC)));
            freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_PRIORITY)));
            freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_VDATEF)));
            freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_VDATET)));
            freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_REMARKS)));
            freeHed.setFFREEHED_RECORD_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_RECORD_ID)));
            freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_ITEM_QTY)));
            freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_FREE_IT_QTY)));
            freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_FTYPE)));
            //freeHed.setFFREEHED_COSTCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_COSTCODE)));

            list.add(freeHed);
        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }

    public FreeHed getFreeIssueItemSchema(String itemCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        FreeHed freeHed = new FreeHed();
        String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "')  AND date('now') between vdatef and vdatet";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
        while (cursor.moveToNext()) {



            freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
            freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(dbHelper.TXNDATE)));
            freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_DISC_DESC)));
            freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_PRIORITY)));
            freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_VDATEF)));
            freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_VDATET)));
            freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_REMARKS)));
            freeHed.setFFREEHED_RECORD_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_RECORD_ID)));
            freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_ITEM_QTY)));
            freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_FREE_IT_QTY)));
            freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_FTYPE)));
            //freeHed.setFFREEHED_COSTCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_COSTCODE)));
        }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return freeHed;
    }

    public String getRefoByItemCode(String itemCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

        String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') and date('now') between vdatef and vdatet";

        String s = null;
        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                FreeHed freeHed = new FreeHed();

                s = cursor.getString(cursor.getColumnIndex(dbHelper.REFNO));
                list.add(freeHed);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return s;
    }

}
