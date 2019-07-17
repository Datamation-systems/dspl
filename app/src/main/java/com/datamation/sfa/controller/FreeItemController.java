package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FreeItem;

import java.util.ArrayList;

public class FreeItemController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";

    public FreeItemController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateFreeItem(ArrayList<FreeItem> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        Cursor cursor_ini = null;

        try {

            cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FFREEITEM, null);

            for (FreeItem item : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.FFREEITEM_ITEMCODE, item.getFFREEITEM_ITEMCODE());
                values.put(dbHelper.REFNO, item.getFFREEITEM_REFNO());
                values.put(dbHelper.FFREEITEM_RECORD_ID, item.getFFREEITEM_RECORD_ID());

                if (cursor_ini.getCount() > 0) {
                    String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FFREEITEM + " WHERE " + dbHelper.REFNO + "='" + item.getFFREEITEM_REFNO() + "' AND " + dbHelper.FFREEITEM_ITEMCODE + " = '" + item.getFFREEITEM_ITEMCODE() + "'";
                    cursor = dB.rawQuery(selectQuery, null);

                    if (cursor.getCount() > 0) {
                        count = (int) dB.update(dbHelper.TABLE_FFREEITEM, values, dbHelper.REFNO + "='" + item.getFFREEITEM_REFNO() + "' AND " + dbHelper.FFREEITEM_ITEMCODE + " = '" + item.getFFREEITEM_ITEMCODE() + "'", null);
                    } else {
                        count = (int) dB.insert(dbHelper.TABLE_FFREEITEM, null, values);
                    }

                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FFREEITEM, null, values);
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
        return count;

    }

    @SuppressWarnings("static-access")
    public int deleteAll() {
        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FFREEITEM, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FFREEITEM, null, null);
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

    public ArrayList<FreeItem> getFreeItemsByRefno(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeItem> list = new ArrayList<FreeItem>();

        String selectQuery = "select * from ffreeItem where refno='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            FreeItem item = new FreeItem();

            item.setFFREEITEM_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEITEM_ID)));
            item.setFFREEITEM_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
            item.setFFREEITEM_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEITEM_ITEMCODE)));
            item.setFFREEITEM_RECORD_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEITEM_RECORD_ID)));

            list.add(item);

        }

        return list;
    }
}
