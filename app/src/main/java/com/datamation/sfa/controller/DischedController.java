package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Discdeb;
import com.datamation.sfa.model.Disched;

import java.util.ArrayList;

public class DischedController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";

    public DischedController(Context context) {

        this.context = context;
        dbHelper = new DatabaseHelper(context);

    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateDisched(ArrayList<Disched> list) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        Cursor cursor_ini = null;

        try {

            cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FDISCHED, null);

            for (Disched disched : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.REFNO, disched.getFDISCHED_REF_NO());
                values.put(dbHelper.TXNDATE, disched.getFDISCHED_TXN_DATE());
                values.put(dbHelper.FDISCHED_DISC_DESC, disched.getFDISCHED_DISC_DESC());
                values.put(dbHelper.FDISCHED_PRIORITY, disched.getFDISCHED_PRIORITY());
                values.put(dbHelper.FDISCHED_DIS_TYPE, disched.getFDISCHED_DIS_TYPE());
                values.put(dbHelper.FDISCHED_V_DATE_F, disched.getFDISCHED_V_DATE_F());
                values.put(dbHelper.FDISCHED_V_DATE_T, disched.getFDISCHED_V_DATE_T());
                values.put(dbHelper.FDISCHED_REMARK, disched.getFDISCHED_REMARK());
                values.put(dbHelper.FDISCHED_ADD_USER, disched.getFDISCHED_ADD_USER());
                values.put(dbHelper.FDISCHED_ADD_DATE, disched.getFDISCHED_ADD_DATE());
                values.put(dbHelper.FDISCHED_ADD_MACH, disched.getFDISCHED_ADD_MACH());
                values.put(dbHelper.FDISCHED_RECORD_ID, disched.getFDISCHED_RECORD_ID());
                values.put(dbHelper.FDISCHED_TIMESTAMP_COLUMN, disched.getFDISCHED_TIMESTAMP_COLUMN());

                if (cursor_ini.getCount() > 0) {
                    String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FDISCHED + " WHERE " + dbHelper.REFNO + "='" + disched.getFDISCHED_REF_NO() + "'";
                    cursor = dB.rawQuery(selectQuery, null);

                    if (cursor.getCount() > 0) {
                        count = (int) dB.update(dbHelper.TABLE_FDISCHED, values, dbHelper.REFNO + "='" + disched.getFDISCHED_REF_NO() + "'", null);
                    } else {
                        count = (int) dB.insert(dbHelper.TABLE_FDISCHED, null, values);
                    }

                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FDISCHED, null, values);
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

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FDISCHED, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FDISCHED, null, null);
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


    public Disched getSchemeByItemCode(String itemCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from fdisched where refno in (select refno from fdiscdet where itemcode='" + itemCode + "') and date('now') between vdatef and vdatet";

        Disched DiscHed = new Disched();
        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                DiscHed.setFDISCHED_REF_NO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                DiscHed.setFDISCHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(dbHelper.TXNDATE)));
                DiscHed.setFDISCHED_DIS_TYPE(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCHED_DIS_TYPE)));
                DiscHed.setFDISCHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCHED_DISC_DESC)));
                DiscHed.setFDISCHED_PRIORITY(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCHED_PRIORITY)));
                DiscHed.setFDISCHED_V_DATE_F(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCHED_V_DATE_F)));
                DiscHed.setFDISCHED_V_DATE_T(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCHED_V_DATE_T)));
                DiscHed.setFDISCHED_REMARK(cursor.getString(cursor.getColumnIndex(dbHelper.FFREEHED_REMARKS)));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return DiscHed;
    }


    public ArrayList<Discdeb> getDebterList(String DiscId) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM FDiscdeb WHERE RefNo ='" + DiscId + "'";

        ArrayList<Discdeb> discDebList = new ArrayList<Discdeb>();

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {
                Discdeb discDeb = new Discdeb();
                discDeb.setFDISCDEB_REF_NO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                discDeb.setFDISCDEB_DEB_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCDEB_DEB_CODE)));
                discDeb.setFDISCDEB_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCDEB_ID)));
                discDeb.setFDISCDEB_RECORD_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCDEB_RECORD_ID)));
                discDeb.setFDISCDEB_TIEMSTAMP_COLUMN(cursor.getString(cursor.getColumnIndex(dbHelper.FDISCDEB_TIEMSTAMP_COLUMN)));
                discDebList.add(discDeb);
            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return discDebList;
    }


}
