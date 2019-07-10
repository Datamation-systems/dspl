package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Discdet;

import java.util.ArrayList;
import java.util.List;

public class DiscdetController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";

    public DiscdetController(Context context) {

        this.context = context;
        dbHelper = new DatabaseHelper(context);

    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateDiscdet(ArrayList<Discdet> list) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        Cursor cursor_ini = null;

        try {

            cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FDISCDET, null);

            for (Discdet discdet : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.REFNO, discdet.getFDISCDET_REF_NO());
                values.put(dbHelper.FDISCDET_ITEM_CODE, discdet.getFDISCDET_ITEM_CODE());
                values.put(dbHelper.FDISCDET_RECORD_ID, discdet.getFDISCDET_RECORD_ID());
                values.put(dbHelper.FDISCHED_TIEMSTAMP_COLUMN, discdet.getFDISCHED_TIEMSTAMP_COLUMN());

                if (cursor_ini.getCount() > 0) {
                    String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FDISCDET + " WHERE " + dbHelper.REFNO + "='" + discdet.getFDISCDET_REF_NO() + "' AND " + dbHelper.FDISCDET_ITEM_CODE + " = '" + discdet.getFDISCDET_ITEM_CODE() + "'";
                    cursor = dB.rawQuery(selectQuery, null);

                    if (cursor.getCount() > 0) {
                        count = (int) dB.update(dbHelper.TABLE_FDISCDET, values, dbHelper.REFNO + "='" + discdet.getFDISCDET_REF_NO() + "' AND " + dbHelper.FDISCDET_ITEM_CODE + " = '" + discdet.getFDISCDET_ITEM_CODE() + "'", null);
                    } else {
                        count = (int) dB.insert(dbHelper.TABLE_FDISCDET, null, values);
                    }

                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FDISCDET, null, values);
                }

            }
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

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FDISCDET, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FDISCDET, null, null);
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

    public List<String> getAssortByItemCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String> list = new ArrayList<String>();

        String selectQuery = "select * from fdiscdet where refno in (select RefNo from fdiscdet where itemcode='" + itemCode + "')";

        String s = null;
        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                list.add(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCDET_ITEM_CODE)));

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
