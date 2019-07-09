package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.TaxHed;

import java.util.ArrayList;

public class TaxHedDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbeHelper;
    private String TAG = "TaxHedDS";

    public TaxHedDS(Context context) {
        this.context = context;
        dbeHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbeHelper.getWritableDatabase();
    }

    public int createOrUpdateTaxHed(ArrayList<TaxHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (TaxHed taxHed : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FTAXHED + " WHERE " + DatabaseHelper.FTAXHED_COMCODE + " = '" + taxHed.getTAXCOMCODE() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(DatabaseHelper.FTAXHED_ACTIVE, taxHed.getACTIVE());
                values.put(DatabaseHelper.FTAXHED_COMCODE, taxHed.getTAXCOMCODE());
                values.put(DatabaseHelper.FTAXHED_COMNAME, taxHed.getTAXCOMNAME());

                int cn = cursor.getCount();
                if (cn > 0)
                    count = dB.update(DatabaseHelper.TABLE_FTAXHED, values, DatabaseHelper.FTAXHED_COMCODE + " =?", new String[]{String.valueOf(taxHed.getTAXCOMCODE())});
                else
                    count = (int) dB.insert(DatabaseHelper.TABLE_FTAXHED, null, values);

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
