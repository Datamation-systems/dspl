package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.District;
import com.datamation.sfa.model.Route;

import java.util.ArrayList;

public class DistrictController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "RouteDS";

    public DistrictController(Context context) {
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
    public int createOrUpdateRoute(ArrayList<Route> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (Route route : list) {

                cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ROUTE, null);

                ContentValues values = new ContentValues();


                values.put(dbHelper.ROUTE_NAME, route.getRouteName());
                values.put(dbHelper.ROUTE_CODE, route.getRouteCode());


                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_ROUTE, values, DatabaseHelper.ROUTE_CODE + "=?", new String[]{route.getRouteCode().toString()});
                    Log.v("TABLE_ROUTE : ", "Updated");
                } else {
                    count = (int) dB.insert(dbHelper.TABLE_ROUTE, null, values);
                    Log.v("TABLE_ROUTE : ", "Inserted " + count);
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

    public ArrayList<District> getAllDistrict() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<District> list = new ArrayList<District>();

        String selectQuery = "select * from fRoute";
        Cursor cursor=null;
        try {
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                District district = new District();
                district.setDistrictCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_CODE)));
                district.setDistrictName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_NAME)));

                list.add(district);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
