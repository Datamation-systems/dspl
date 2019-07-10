package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FreeDeb;

import java.util.ArrayList;

public class FreeDebController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";


    public FreeDebController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateFreeDeb(ArrayList<FreeDeb> list) {

        int serverdbID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (FreeDeb freeDeb : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.REFNO, freeDeb.getFFREEDEB_REFNO());
                values.put(dbHelper.FFREEDEB_DEB_CODE, freeDeb.getFFREEDEB_DEB_CODE());
                values.put(dbHelper.FFREEDEB_ADD_USER, freeDeb.getFFREEDEB_ADD_USER());
                values.put(dbHelper.FFREEDEB_ADD_DATE, freeDeb.getFFREEDEB_ADD_DATE());
                values.put(dbHelper.FFREEDEB_ADD_MACH, freeDeb.getFFREEDEB_ADD_MACH());
                values.put(dbHelper.FFREEDEB_RECORD_ID, freeDeb.getFFREEDEB_RECORD_ID());
                values.put(dbHelper.FFREEDEB_TIMESTAMP_COLUMN, freeDeb.getFFREEDEB_TIMESTAMP_COLUMN());

                serverdbID = (int) dB.insert(dbHelper.TABLE_FFREEDEB, null, values);

            }
        } catch (Exception e) {

            Log.v("Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return serverdbID;

    }

    public int getRefnoByDebCount(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT count(*) FROM " + dbHelper.TABLE_FFREEDEB + " WHERE " + dbHelper.REFNO + "='" + refno + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getInt(0);

        }
        return 0;

    }

    public int isValidDebForFreeIssue(String refno, String currDeb) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT count(*) FROM " + dbHelper.TABLE_FFREEDEB + " WHERE " + dbHelper.REFNO + "='" + refno + "' AND " + dbHelper.FFREEDEB_DEB_CODE + "='" + currDeb + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getInt(0);

        }
        return 0;

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

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FFREEDEB, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FFREEDEB, null, null);
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
}
