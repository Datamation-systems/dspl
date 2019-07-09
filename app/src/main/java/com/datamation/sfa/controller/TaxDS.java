package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Tax;

import java.util.ArrayList;

public class TaxDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbeHelper;
    private String TAG = "TaxDS";

    public TaxDS(Context context) {
        this.context = context;
        dbeHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbeHelper.getWritableDatabase();
    }

    public int createOrUpdateTaxHed(ArrayList<Tax> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (Tax tax : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FTAX + " WHERE " + DatabaseHelper.FTAX_TAXCODE + " = '" + tax.getTAXCODE() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(DatabaseHelper.FTAX_TAXCODE, tax.getTAXCODE());
                values.put(DatabaseHelper.FTAX_TAXNAME, tax.getTAXNAME());
                values.put(DatabaseHelper.FTAX_TAXPER, tax.getTAXPER());
                values.put(DatabaseHelper.FTAX_TAXREGNO, tax.getTAXREGNO());

                int cn = cursor.getCount();
                if (cn > 0)
                    count = dB.update(DatabaseHelper.TABLE_FTAX, values, DatabaseHelper.FTAX_TAXCODE + " =?", new String[]{String.valueOf(tax.getTAXCODE())});
                else
                    count = (int) dB.insert(DatabaseHelper.TABLE_FTAX, null, values);

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


    public String getTaxRGNo(String Taxcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FTAX + " WHERE " + DatabaseHelper.FTAX_TAXCODE + "='" + Taxcode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTAX_TAXREGNO));

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            //if (cursor != null) {
            //	cursor.close();
            //}
            //dB.close();
        }
        return "";
    }


}
