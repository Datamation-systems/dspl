package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FInvRHed;

import java.util.ArrayList;

public class SalesReturnController
{
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "SFA";

    public SalesReturnController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateInvRHed(ArrayList<FInvRHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (FInvRHed invrHed : list) {

                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FINVRHED + " WHERE " + dbHelper.FINVRHED_REFNO
                        + " = '" + invrHed.getFINVRHED_REFNO() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(dbHelper.FINVRHED_REFNO, invrHed.getFINVRHED_REFNO());
                values.put(dbHelper.FINVRHED_TXNDATE, invrHed.getFINVRHED_TXN_DATE());
                values.put(dbHelper.FINVRHED_REMARKS, invrHed.getFINVRHED_REMARKS());
                values.put(dbHelper.FINVRHED_DEBCODE, invrHed.getFINVRHED_DEBCODE());
                values.put(dbHelper.FINVRHED_TOTAL_AMT, invrHed.getFINVRHED_TOTAL_AMT());
                values.put(dbHelper.FINVRHED_ADD_DATE, invrHed.getFINVRHED_ADD_DATE());
                values.put(dbHelper.FINVRHED_ADD_MACH, invrHed.getFINVRHED_ADD_MACH());
                values.put(dbHelper.FINVRHED_ADD_USER, invrHed.getFINVRHED_ADD_USER());
                values.put(dbHelper.FINVRHED_MANUREF, invrHed.getFINVRHED_MANUREF());
                values.put(dbHelper.FINVRHED_IS_ACTIVE, invrHed.getFINVRHED_IS_ACTIVE());
                values.put(dbHelper.FINVRHED_IS_SYNCED, invrHed.getFINVRHED_IS_SYNCED());
                values.put(dbHelper.FINVRHED_COSTCODE, invrHed.getFINVRHED_COSTCODE());
                values.put(dbHelper.FINVRHED_LOCCODE, invrHed.getFINVRHED_LOCCODE());
                //values.put(dbHelper.FINVRHED_REASON_CODE, invrHed.getFINVRHED_REASON_CODE());
                //values.put(dbHelper.FINVRHED_TAX_REG, invrHed.getFINVRHED_TAX_REG());
//                values.put(dbHelper.FINVRHED_TOTAL_TAX, invrHed.getFINVRHED_TOTAL_TAX());
//                values.put(dbHelper.FINVRHED_TOTAL_DIS, invrHed.getFINVRHED_TOTAL_DIS());
//                //values.put(dbHelper.FINVRHED_LONGITUDE, invrHed.getFINVRHED_LONGITUDE());
//                values.put(dbHelper.FINVRHED_LATITUDE, invrHed.getFINVRHED_LATITUDE());
//                values.put(dbHelper.FINVRHED_START_TIME, invrHed.getFINVRHED_START_TIME());
//                values.put(dbHelper.FINVRHED_END_TIME, invrHed.getFINVRHED_END_TIME());
//                values.put(dbHelper.FINVRHED_REPCODE, invrHed.getFINVRHED_REP_CODE());
//                values.put(dbHelper.FINVRHED_RETURN_TYPE, invrHed.getFINVRHED_RETURN_TYPE());
//                values.put(dbHelper.FINVRHED_TOURCODE, invrHed.getFINVRHED_TOURCODE());
//                values.put(dbHelper.FINVRHED_AREACODE, invrHed.getFINVRHED_AREACODE());
//                values.put(dbHelper.FINVRHED_DRIVERCODE, invrHed.getFINVRHED_DRIVERCODE());
//                values.put(dbHelper.FINVRHED_HELPERCODE, invrHed.getFINVRHED_HELPERCODE());
//                values.put(dbHelper.FINVRHED_LORRYCODE, invrHed.getFINVRHED_LORRYCODE());

                int cn = cursor.getCount();
                if (cn > 0) {

                    count = dB.update(dbHelper.TABLE_FINVRHED, values, dbHelper.FINVRHED_REFNO + " =?",
                            new String[]{String.valueOf(invrHed.getFINVRHED_REFNO())});
                } else {

                    count = (int) dB.insert(dbHelper.TABLE_FINVRHED, null, values);

                }

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
