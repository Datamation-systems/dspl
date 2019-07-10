package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FreeSlab;

import java.util.ArrayList;

public class FreeSlabController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";

    public FreeSlabController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateFreeSlab(ArrayList<FreeSlab> list) {

        int serverdbID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        Cursor cursor_ini = null;

        try {

            cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FFREESLAB, null);
            // if(cursor_ini.getCount()>0){
            // int success = dB.delete(dbHelper.TABLE_FFREESLAB, null, null);
            // Log.v("Success", success+"");
            // }

            for (FreeSlab freeSlab : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.REFNO, freeSlab.getFFREESLAB_REFNO());
                values.put(dbHelper.FFREESLAB_QTY_F, freeSlab.getFFREESLAB_QTY_F());
                values.put(dbHelper.FFREESLAB_QTY_T, freeSlab.getFFREESLAB_QTY_T());
                values.put(dbHelper.FFREESLAB_FITEM_CODE, freeSlab.getFFREESLAB_FITEM_CODE());
                values.put(dbHelper.FFREESLAB_FREE_QTY, freeSlab.getFFREESLAB_FREE_QTY());
                values.put(dbHelper.FFREESLAB_ADD_USER, freeSlab.getFFREESLAB_ADD_USER());
                values.put(dbHelper.FFREESLAB_ADD_DATE, freeSlab.getFFREESLAB_ADD_DATE());
                values.put(dbHelper.FFREESLAB_ADD_MACH, freeSlab.getFFREESLAB_ADD_MACH());
                values.put(dbHelper.FFREESLAB_RECORD_ID, freeSlab.getFFREESLAB_RECORD_ID());
                values.put(dbHelper.FFREESLAB_TIMESTAP_COLUMN, freeSlab.getFFREESLAB_TIMESTAP_COLUMN());
                values.put(dbHelper.FFREESLAB_SEQ_NO, freeSlab.getFFREESLAB_SEQ_NO());

                if (cursor_ini.getCount() > 0) {
                    String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FFREESLAB + " WHERE " + dbHelper.REFNO + "='" + freeSlab.getFFREESLAB_REFNO() + "' AND " + dbHelper.FFREESLAB_SEQ_NO + " = '" + freeSlab.getFFREESLAB_SEQ_NO() + "'";
                    cursor = dB.rawQuery(selectQuery, null);

                    if (cursor.getCount() > 0) {
                        serverdbID = (int) dB.update(dbHelper.TABLE_FFREESLAB, values, dbHelper.REFNO + "='" + freeSlab.getFFREESLAB_REFNO() + "' AND " + dbHelper.FFREESLAB_SEQ_NO + " = '" + freeSlab.getFFREESLAB_SEQ_NO() + "'", null);
                    } else {
                        serverdbID = (int) dB.insert(dbHelper.TABLE_FFREESLAB, null, values);
                    }

                } else {
                    serverdbID = (int) dB.insert(dbHelper.TABLE_FFREESLAB, null, values);
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
        return serverdbID;

    }

    public ArrayList<FreeSlab> getSlabdetails(String refno, int tQty) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeSlab> list = new ArrayList<FreeSlab>();

        String selectQuery = "select * from fFreesLab where refno='" + refno + "' and " + tQty + " between CAST(Qtyf as double) and CAST(Qtyt as double)";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            FreeSlab freeSlab = new FreeSlab();

            freeSlab.setFFREESLAB_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_ID)));
            freeSlab.setFFREESLAB_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
            freeSlab.setFFREESLAB_QTY_F(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_QTY_F)));
            freeSlab.setFFREESLAB_QTY_T(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_QTY_T)));
            freeSlab.setFFREESLAB_FITEM_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_FITEM_CODE)));
            freeSlab.setFFREESLAB_FREE_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_FREE_QTY)));
            freeSlab.setFFREESLAB_ADD_USER(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_ADD_USER)));
            freeSlab.setFFREESLAB_ADD_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_ADD_DATE)));
            freeSlab.setFFREESLAB_ADD_MACH(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_ADD_MACH)));
            freeSlab.setFFREESLAB_RECORD_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_RECORD_ID)));
            freeSlab.setFFREESLAB_TIMESTAP_COLUMN(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_TIMESTAP_COLUMN)));
            freeSlab.setFFREESLAB_SEQ_NO(cursor.getString(cursor.getColumnIndex(dbHelper.FFREESLAB_SEQ_NO)));

            list.add(freeSlab);

        }

        return list;
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

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FFREESLAB, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FFREESLAB, null, null);
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
