package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.SalRep;

import java.util.ArrayList;

public class SalRepController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "FSALREP";

    public SalRepController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int createOrUpdateSalRep(ArrayList<SalRep> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (SalRep rep : list) {

                Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FSALREP + " WHERE " + DatabaseHelper.FSALREP_REPCODE + "='" + rep.getREPCODE() + "'", null);

                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FSALREP_ADDMACH, rep.getADDMACH());
                values.put(DatabaseHelper.FSALREP_ADDUSER, rep.getADDUSER());
                values.put(DatabaseHelper.FSALREP_EMAIL, rep.getEMAIL());
                values.put(DatabaseHelper.FSALREP_ID, rep.getId());
                values.put(DatabaseHelper.FSALREP_MACID, rep.getMACID());
                values.put(DatabaseHelper.FSALREP_MOBILE, rep.getMOBILE());
                values.put(DatabaseHelper.FSALREP_NAME, rep.getNAME());
                values.put(DatabaseHelper.FSALREP_PASSWORD, rep.getPASSWORD());
                values.put(DatabaseHelper.FSALREP_PREFIX, rep.getPREFIX());
                values.put(DatabaseHelper.FSALREP_RECORDID, rep.getRECORDID());
                values.put(DatabaseHelper.FSALREP_REPCODE, rep.getREPCODE());
                values.put(DatabaseHelper.FSALREP_REPID, rep.getREPID());
                values.put(DatabaseHelper.FSALREP_STATUS, rep.getSTATUS());
                values.put(DatabaseHelper.FSALREP_TELE, rep.getTELE());
                values.put(DatabaseHelper.FSALREP_LOCCODE, rep.getLOCCODE());


                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_FSALREP, values, DatabaseHelper.FSALREP_REPCODE + "=?", new String[]{rep.getREPCODE()});
                    Log.v("FSALREP : ", "Updated");
                } else {
                    count = (int) dB.insert(DatabaseHelper.TABLE_FSALREP, null, values);
                    Log.v("FSALREP : ", "Inserted " + count);
                }

                cursor.close();
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getCurrentRepCode() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + DatabaseHelper.FSALREP_REPCODE + " FROM " + DatabaseHelper.TABLE_FSALREP;

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSALREP_REPCODE));


            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public SalRep getSaleRep(String Repcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectRep = "SELECT * FROM fSalRep WHERE RepCode='" + Repcode + "'";
        Cursor curRep = null;
        curRep = dB.rawQuery(selectRep, null);
        SalRep newRep = new SalRep();

        try {
            while (curRep.moveToNext()) {

                newRep.setADDMACH(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_ADDMACH)));
                newRep.setADDUSER(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_ADDUSER)));
                newRep.setEMAIL(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_EMAIL)));
                newRep.setId(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_ID)));
                newRep.setMACID(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_MACID)));
                newRep.setMOBILE(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_MOBILE)));
                newRep.setNAME(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_NAME)));
                newRep.setPASSWORD(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_PASSWORD)));
                newRep.setPREFIX(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_PREFIX)));
                newRep.setRECORDID(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_RECORDID)));
                newRep.setREPCODE(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_REPCODE)));
                newRep.setREPID(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_REPID)));
                newRep.setSTATUS(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_STATUS)));
                newRep.setTELE(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_TELE)));

            }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            curRep.close();
            dB.close();
        }

        return newRep;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<SalRep> getSaleRepDetails() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectRep = "SELECT * FROM fSalRep";

        Cursor curRep = null;
        curRep = dB.rawQuery(selectRep, null);
        ArrayList<SalRep> salreplist = new ArrayList<SalRep>();
        try {
            while (curRep.moveToNext()) {

                SalRep newRep = new SalRep();

                newRep.setADDMACH(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_ADDMACH)));
                newRep.setADDUSER(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_ADDUSER)));
                newRep.setEMAIL(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_EMAIL)));
                newRep.setId(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_ID)));
                newRep.setMACID(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_MACID)));
                newRep.setMOBILE(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_MOBILE)));
                newRep.setNAME(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_NAME)));
                newRep.setPASSWORD(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_PASSWORD)));
                newRep.setPREFIX(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_PREFIX)));
                newRep.setRECORDID(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_RECORDID)));
                newRep.setREPCODE(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_REPCODE)));
                newRep.setREPID(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_REPID)));
                newRep.setSTATUS(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_STATUS)));
                newRep.setTELE(curRep.getString(curRep.getColumnIndex(DatabaseHelper.FSALREP_TELE)));

                salreplist.add(newRep);

            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            curRep.close();
            dB.close();
        }
        return salreplist;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getCurrentLocCode() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + DatabaseHelper.FSALREP_LOCCODE + " FROM " + DatabaseHelper.TABLE_FSALREP;

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FSALREP_LOCCODE));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }


}
