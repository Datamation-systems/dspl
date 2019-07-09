package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Control;

import java.util.ArrayList;

public class ControlDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "ControlDS";

    public ControlDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateFControl(ArrayList<Control> coList) {
        int serverdbID = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (Control control : coList) {
                Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FCONTROL, null);

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.FCONTROL_COM_NAME, control.getFCONTROL_COM_NAME());
                values.put(DatabaseHelper.FCONTROL_COM_ADD1, control.getFCONTROL_COM_ADD1());
                values.put(DatabaseHelper.FCONTROL_COM_ADD2, control.getFCONTROL_COM_ADD2());
                values.put(DatabaseHelper.FCONTROL_COM_ADD3, control.getFCONTROL_COM_ADD3());
                values.put(DatabaseHelper.FCONTROL_COM_TEL1, control.getFCONTROL_COM_TEL1());
                values.put(DatabaseHelper.FCONTROL_COM_TEL2, control.getFCONTROL_COM_TEL2());
                values.put(DatabaseHelper.FCONTROL_COM_FAX, control.getFCONTROL_COM_FAX());
                values.put(DatabaseHelper.FCONTROL_COM_EMAIL, control.getFCONTROL_COM_EMAIL());
                values.put(DatabaseHelper.FCONTROL_COM_WEB, control.getFCONTROL_COM_WEB());
                values.put(DatabaseHelper.FCONTROL_FYEAR, control.getFCONTROL_FYEAR());
                values.put(DatabaseHelper.FCONTROL_TYEAR, control.getFCONTROL_TYEAR());
                values.put(DatabaseHelper.FCONTROL_COM_REGNO, control.getFCONTROL_COM_REGNO());
                values.put(DatabaseHelper.FCONTROL_FTXN, control.getFCONTROL_FTXN());
                values.put(DatabaseHelper.FCONTROL_TTXN, control.getFCONTROL_TTXN());
                values.put(DatabaseHelper.FCONTROL_CRYSTALPATH, control.getFCONTROL_CRYSTALPATH());
                values.put(DatabaseHelper.FCONTROL_VATCMTAXNO, control.getFCONTROL_VATCMTAXNO());
                values.put(DatabaseHelper.FCONTROL_NBTCMTAXNO, control.getFCONTROL_NBTCMTAXNO());
                values.put(DatabaseHelper.FCONTROL_SYSTYPE, control.getFCONTROL_SYSTYPE());
                values.put(DatabaseHelper.FCONTROL_DEALCODE, control.getFCONTROL_DEALCODE());
                values.put(DatabaseHelper.FCONTROL_BASECUR, control.getFCONTROL_BASECUR());
                values.put(DatabaseHelper.FCONTROL_BALGCRLM, control.getFCONTROL_BALGCRLM());
                values.put(DatabaseHelper.FCONTROL_CONAGE1, control.getFCONTROL_CONAGE1());
                values.put(DatabaseHelper.FCONTROL_CONAGE2, control.getFCONTROL_CONAGE2());
                values.put(DatabaseHelper.FCONTROL_CONAGE3, control.getFCONTROL_CONAGE3());
                values.put(DatabaseHelper.FCONTROL_CONAGE4, control.getFCONTROL_CONAGE4());
                values.put(DatabaseHelper.FCONTROL_CONAGE5, control.getFCONTROL_CONAGE5());
                values.put(DatabaseHelper.FCONTROL_CONAGE6, control.getFCONTROL_CONAGE6());
                values.put(DatabaseHelper.FCONTROL_CONAGE7, control.getFCONTROL_CONAGE7());
                values.put(DatabaseHelper.FCONTROL_CONAGE8, control.getFCONTROL_CONAGE8());
                values.put(DatabaseHelper.FCONTROL_CONAGE9, control.getFCONTROL_CONAGE9());
                values.put(DatabaseHelper.FCONTROL_CONAGE10, control.getFCONTROL_CONAGE10());
                values.put(DatabaseHelper.FCONTROL_CONAGE11, control.getFCONTROL_CONAGE11());
                values.put(DatabaseHelper.FCONTROL_CONAGE12, control.getFCONTROL_CONAGE12());
                values.put(DatabaseHelper.FCONTROL_CONAGE13, control.getFCONTROL_CONAGE13());
            //    values.put(DatabaseHelper.FCONTROL_COMDISPER, control.getFCONTROL_COMDISPER());
                values.put(DatabaseHelper.FCONTROL_CONAGE14, control.getFCONTROL_CONAGE14());

                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_FCONTROL, values, null, new String[]{});
                    Log.v(TAG, " Updated");
                } else {
                    serverdbID = (int) dB.insert(DatabaseHelper.TABLE_FCONTROL, null, values);
                    Log.v(TAG, " Inserted " + serverdbID);
                }

                cursor.close();
            }

        } catch (Exception e) {
            Log.v(TAG, e.toString());
        } finally {
            dB.close();
        }
        return serverdbID;

    }

	/*-*-*-*-*-**-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Control> getAllControl() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Control> list = new ArrayList<Control>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FCONTROL;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                Control aControl = new Control();

                aControl.setFCONTROL_COM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COM_NAME)));
                aControl.setFCONTROL_COM_ADD1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COM_ADD1)));
                aControl.setFCONTROL_COM_ADD2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COM_ADD2)));
                aControl.setFCONTROL_COM_ADD3(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COM_ADD3)));
                aControl.setFCONTROL_COM_TEL1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COM_TEL1)));
                aControl.setFCONTROL_COM_TEL2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COM_TEL2)));
                aControl.setFCONTROL_COM_FAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COM_FAX)));
                aControl.setFCONTROL_COM_EMAIL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COM_EMAIL)));
                aControl.setFCONTROL_COM_WEB(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COM_WEB)));
                aControl.setFCONTROL_DEALCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_DEALCODE)));
                aControl.setFCONTROL_VATCMTAXNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FCONTROL_VATCMTAXNO)));

                list.add(aControl);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();

        }

        return list;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int getSysType() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FCONTROL;

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FCONTROL_SYSTYPE));

        }
        cursor.close();
        dB.close();

        return 0;

    }


//    public double getCompanyDisc() {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        String selectQuery = "SELECT comdisper FROM " + DatabaseHelper.TABLE_FCONTROL;
//
//        Cursor cursor = null;
//        cursor = dB.rawQuery(selectQuery, null);
//
//        while (cursor.moveToNext()) {
//
//            return cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.FCONTROL_COMDISPER));
//
//        }
//        cursor.close();
//        dB.close();
//
//        return 0;
//
//    }
//--------------------------------getFlag status--------------------------------------------------------------

    public int getCurrentStatus() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + DatabaseHelper.FCONTROL_CONAGE1 + " FROM " + DatabaseHelper.TABLE_FCONTROL;

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FCONTROL_CONAGE1));

            }
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            cursor.close();
            dB.close();

        }

        return 0;
    }

}
