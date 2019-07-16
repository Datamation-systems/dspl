package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Route;
import com.datamation.sfa.model.Town;

import java.util.ArrayList;

public class TownController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "RouteDS";

    public TownController(Context context) {
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


                values.put(dbHelper.ROUTE_NAME, route.getFROUTE_ROUTE_NAME());
                values.put(dbHelper.ROUTE_CODE, route.getFROUTE_ROUTECODE());


                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_ROUTE, values, DatabaseHelper.ROUTE_CODE + "=?", new String[]{route.getFROUTE_ROUTECODE().toString()});
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

    public ArrayList<Town> getAllTowns() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Town> list = new ArrayList<Town>();

        String selectQuery = "select * from fRoute";
        Cursor cursor=null;
        try {
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Town town = new Town();
                town.setTownCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_CODE)));
                town.setTownName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_NAME)));

                list.add(town);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
