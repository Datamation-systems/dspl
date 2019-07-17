package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Route;

import java.util.ArrayList;

public class RouteController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "RouteDS";

    public RouteController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    /*
     * insert code
     */
    @SuppressWarnings("static-access")
    public int createOrUpdateFRoute(ArrayList<Route> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (Route route : list) {

                cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FROUTE + " WHERE " + DatabaseHelper.FROUTE_ROUTECODE + "='" + route.getFROUTE_ROUTECODE() + "'", null);

                ContentValues values = new ContentValues();

                values.put(dbHelper.FROUTE_REPCODE, route.getFROUTE_REPCODE());
                values.put(dbHelper.FROUTE_ROUTECODE, route.getFROUTE_ROUTECODE());
                values.put(dbHelper.FROUTE_ROUTE_NAME, route.getFROUTE_ROUTE_NAME());
                values.put(dbHelper.FROUTE_RECORDID, route.getFROUTE_RECORDID());
                values.put(dbHelper.FROUTE_ADDDATE, route.getFROUTE_ADDDATE());
                values.put(dbHelper.FROUTE_ADD_MACH, route.getFROUTE_ADD_MACH());
                values.put(dbHelper.FROUTE_ADD_USER, route.getFROUTE_ADD_USER());
                values.put(dbHelper.FROUTE_AREACODE, route.getFROUTE_AREACODE());
                values.put(dbHelper.FROUTE_DEALCODE, route.getFROUTE_DEALCODE());
                values.put(dbHelper.FROUTE_FREQNO, route.getFROUTE_FREQNO());
                values.put(dbHelper.FROUTE_KM, route.getFROUTE_KM());
                values.put(dbHelper.FROUTE_MINPROCALL, route.getFROUTE_MINPROCALL());
                values.put(dbHelper.FROUTE_RDALORATE, route.getFROUTE_RDALORATE());
                values.put(dbHelper.FROUTE_RDTARGET, route.getFROUTE_RDTARGET());
                values.put(dbHelper.FROUTE_REMARKS, route.getFROUTE_REMARKS());
                values.put(dbHelper.FROUTE_STATUS, route.getFROUTE_STATUS());
                values.put(dbHelper.FROUTE_TONNAGE, route.getFROUTE_TONNAGE());

                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_FROUTE, values, DatabaseHelper.FROUTE_ROUTECODE + "=?", new String[]{route.getFROUTE_ROUTECODE().toString()});
                    Log.v("TABLE_FROUTE : ", "Updated");
                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FROUTE, null, values);
                    Log.v("TABLE_FROUTE : ", "Inserted " + count);
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

    /*
     * delete code
     */
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

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_ROUTE, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_ROUTE, null, null);
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




    @SuppressWarnings("static-access")
    public String getRouteNameByCode(String code) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FROUTE +  " WHERE "  + dbHelper.FROUTE_ROUTECODE + "='" + code + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(dbHelper.FROUTE_ROUTE_NAME));

        }

        return "";
    }

    //----------------------------getAllRoute---------------------------------------
    public ArrayList<Route> getRoute() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Route> list = new ArrayList<Route>();

        String selectQuery = "select * from fRoute";
        Cursor cursor=null;
        try {
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Route route = new Route();
                route.setFROUTE_ROUTECODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_CODE)));
                route.setFROUTE_ROUTE_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_NAME)));

                list.add(route);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
