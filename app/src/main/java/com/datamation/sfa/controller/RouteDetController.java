package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.RouteDet;

import java.util.ArrayList;

public class RouteDetController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";

    public RouteDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateFRouteDet(ArrayList<RouteDet> list) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            for (RouteDet routeDet : list) {

                cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FROUTEDET + " WHERE " + dbHelper.FROUTEDET_DEB_CODE + "='" + routeDet.getFROUTEDET_DEB_CODE() + "' AND " + dbHelper.FROUTEDET_ROUTE_CODE + "='" + routeDet.getFROUTEDET_ROUTE_CODE() + "'", null);

                ContentValues values = new ContentValues();

                values.put(dbHelper.FROUTEDET_DEB_CODE, routeDet.getFROUTEDET_DEB_CODE());
                values.put(dbHelper.FROUTEDET_ROUTE_CODE, routeDet.getFROUTEDET_ROUTE_CODE());

                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_FROUTEDET, values, dbHelper.FROUTEDET_DEB_CODE + "=? AND " + dbHelper.FROUTEDET_ROUTE_CODE + "=?", new String[]{routeDet.getFROUTEDET_DEB_CODE().toString(), routeDet.getFROUTEDET_ROUTE_CODE().toString()});
                    Log.v("TABLE_FREASON : ", "Updated");
                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FROUTEDET, null, values);
                    Log.v("TABLE_FREASON : ", "Inserted " + count);
                }

            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;
    }

    public void InsertOrReplaceRouteDet(ArrayList<RouteDet> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
//            int reccount = 0;
        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_FROUTEDET + " ("+dbHelper.FROUTEDET_DEB_CODE+","+dbHelper.FROUTEDET_ROUTE_CODE+") VALUES (?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (RouteDet routeDet : list) {

                stmt.bindString(1, routeDet.getFROUTEDET_DEB_CODE());
                stmt.bindString(2, routeDet.getFROUTEDET_ROUTE_CODE());


                stmt.execute();
                stmt.clearBindings();
//                reccount += 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dB.setTransactionSuccessful();
            dB.endTransaction();
            dB.close();
        }
//        return reccount;
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
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

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FROUTEDET, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FROUTEDET, null, null);
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

    public String getRouteCodeByDebCode(String debCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FROUTEDET +  " WHERE "  + dbHelper.FROUTEDET_DEB_CODE + "='" + debCode + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(dbHelper.FROUTEDET_ROUTE_CODE));

        }

        return "";
    }
}
