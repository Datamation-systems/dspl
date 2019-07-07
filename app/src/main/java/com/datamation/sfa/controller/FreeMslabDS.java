package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FreeMslab;

import java.util.ArrayList;

public class FreeMslabDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";


    public FreeMslabDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateFreeMslab(ArrayList<FreeMslab> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        Cursor cursor_ini = null;

        try {

            cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FFREEMSLAB, null);

            for (FreeMslab freeMslab : list) {
                ContentValues values = new ContentValues();

                values.put(dbHelper.REFNO, freeMslab.getFFREEMSLAB_REFNO());
                values.put(dbHelper.FFREEMSLAB_QTY_F, freeMslab.getFFREEMSLAB_QTY_F());
                values.put(dbHelper.FFREEMSLAB_QTY_T, freeMslab.getFFREEMSLAB_QTY_T());
                values.put(dbHelper.FFREEMSLAB_ITEM_QTY, freeMslab.getFFREEMSLAB_ITEM_QTY());
                values.put(dbHelper.FFREEMSLAB_FREE_IT_QTY, freeMslab.getFFREEMSLAB_FREE_IT_QTY());
                values.put(dbHelper.FFREEMSLAB_ADD_USER, freeMslab.getFFREEMSLAB_ADD_USER());
                values.put(dbHelper.FFREEMSLAB_ADD_DATE, freeMslab.getFFREEMSLAB_ADD_DATE());
                values.put(dbHelper.FFREEMSLAB_ADD_MACH, freeMslab.getFFREEMSLAB_ADD_MACH());
                values.put(dbHelper.FFREEMSLAB_RECORD_ID, freeMslab.getFFREEMSLAB_RECORD_ID());
                values.put(dbHelper.FFREEMSLAB_TIMESTAMP_COLUMN, freeMslab.getFFREEMSLAB_TIMESTAMP_COLUMN());
                values.put(dbHelper.FFREEMSLAB_SEQ_NO, freeMslab.getFFREEMSLAB_SEQ_NO());

                if (cursor_ini.getCount() > 0) {
                    String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FFREEMSLAB + " WHERE " + dbHelper.REFNO + "='" + freeMslab.getFFREEMSLAB_REFNO() + "' AND " + dbHelper.FFREEMSLAB_SEQ_NO + " = '" + freeMslab.getFFREEMSLAB_SEQ_NO() + "'";
                    cursor = dB.rawQuery(selectQuery, null);

                    if (cursor.getCount() > 0) {
                        count = (int) dB.update(dbHelper.TABLE_FFREEMSLAB, values, dbHelper.REFNO + "='" + freeMslab.getFFREEMSLAB_REFNO() + "' AND " + dbHelper.FFREEMSLAB_SEQ_NO + " = '" + freeMslab.getFFREEMSLAB_SEQ_NO() + "'", null);
                    } else {
                        count = (int) dB.insert(dbHelper.TABLE_FFREEMSLAB, null, values);
                    }

                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FFREEMSLAB, null, values);
                }


            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

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


    public ArrayList<FreeMslab> getMixDetails(String refno, int tQty) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeMslab> list = new ArrayList<FreeMslab>();

        String selectQuery = "select * from ffreeMslab where refno='" + refno + "' and " + tQty + " between CAST(Qtyf as double) and CAST(Qtyt as double)";


        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            FreeMslab freeMslab = new FreeMslab();

            freeMslab.setFFREEMSLAB_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_ID)));
            freeMslab.setFFREEMSLAB_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
            freeMslab.setFFREEMSLAB_QTY_F(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_QTY_F)));
            freeMslab.setFFREEMSLAB_QTY_T(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_QTY_T)));
            freeMslab.setFFREEMSLAB_ITEM_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_ITEM_QTY)));
            freeMslab.setFFREEMSLAB_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_FREE_IT_QTY)));
            freeMslab.setFFREEMSLAB_ADD_USER(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_ADD_USER)));
            freeMslab.setFFREEMSLAB_ADD_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_ADD_DATE)));
            freeMslab.setFFREEMSLAB_ADD_MACH(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_ADD_MACH)));
            freeMslab.setFFREEMSLAB_RECORD_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_RECORD_ID)));
            freeMslab.setFFREEMSLAB_TIMESTAMP_COLUMN(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_TIMESTAMP_COLUMN)));
            freeMslab.setFFREEMSLAB_SEQ_NO(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEMSLAB_SEQ_NO)));

            list.add(freeMslab);

        }

        return list;
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
            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FFREEMSLAB, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FFREEMSLAB, null, null);
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
