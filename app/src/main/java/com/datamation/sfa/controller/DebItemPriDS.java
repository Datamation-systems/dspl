package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.DebItemPri;

import java.util.ArrayList;

public class DebItemPriDS {


    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "DebItemPriDS";

    public DebItemPriDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }


    public int createOrUpdateDebItemPri(ArrayList<DebItemPri> list) {

        int serverdbID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (DebItemPri hed : list) {

                Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_DEBITEMPRI + " WHERE " + DatabaseHelper.DEBITEMPRI_BRANDCODE + "='" + hed.getBRANDCODE() + "'", null);
                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.DEBITEMPRI_BRANDCODE, hed.getBRANDCODE());
                values.put(DatabaseHelper.DEBITEMPRI_DEBCODE, hed.getDEBCODE());
                values.put(DatabaseHelper.DEBITEMPRI_DISPER, hed.getDISPER());

                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_DEBITEMPRI, values, DatabaseHelper.DEBITEMPRI_BRANDCODE + "=?", new String[]{hed.getBRANDCODE().toString()});
                } else {
                    dB.insert(DatabaseHelper.TABLE_DEBITEMPRI, null, values);
                }

                cursor.close();
            }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return serverdbID;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public double getBrandDiscount(String brand, String debcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String s = "SELECT disper FROM " + DatabaseHelper.TABLE_DEBITEMPRI + " WHERE " + DatabaseHelper.DEBITEMPRI_BRANDCODE + "='" + brand + "' AND " + DatabaseHelper.DEBITEMPRI_DEBCODE + "='" + debcode + "'";
        Cursor cursor = dB.rawQuery(s, null);

        while (cursor.moveToNext()) {
            return cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.DEBITEMPRI_DISPER));

        }
        cursor.close();
        dB.close();
        return 0.0;
    }


}
