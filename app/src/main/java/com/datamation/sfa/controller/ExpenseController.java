package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Expense;


import java.util.ArrayList;

public class ExpenseController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "ExpenseDS ";

    public ExpenseController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateFExpense(ArrayList<Expense> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (Expense expense : list) {

                Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FEXPENSE + " WHERE " + DatabaseHelper.FEXPENSE_CODE + "='" + expense.getFEXPENSE_CODE() + "'", null);

                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FEXPENSE_CODE, expense.getFEXPENSE_CODE());
                values.put(DatabaseHelper.FEXPENSE_GRP_CODE, expense.getFEXPENSE_GRP_CODE());
                values.put(DatabaseHelper.FEXPENSE_NAME, expense.getFEXPENSE_NAME());
                values.put(DatabaseHelper.FEXPENSE_RECORDID, expense.getFEXPENSE_RECORDID());
                values.put(DatabaseHelper.FEXPENSE_ADD_MACH, expense.getFEXPENSE_ADD_MACH());
                values.put(DatabaseHelper.FEXPENSE_STATUS, expense.getFEXPENSE_STATUS());
                values.put(DatabaseHelper.FEXPENSE_ADD_USER, expense.getFEXPENSE_ADD_USER());
                values.put(DatabaseHelper.FEXPENSE_ADD_DATE, expense.getFEXPENSE_ADD_DATE());

                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_FEXPENSE, values, DatabaseHelper.FEXPENSE_CODE + "=?", new String[]{expense.getFEXPENSE_CODE().toString()});
                    Log.v(TAG, "Updated");
                } else {
                    count = (int) dB.insert(DatabaseHelper.TABLE_FEXPENSE, null, values);
                    Log.v(TAG, "Inserted " + count);
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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FEXPENSE, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_FEXPENSE, null, null);
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

    public ArrayList<Expense> getAllExpense(String searchword) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Expense> list = new ArrayList<Expense>();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FEXPENSE + " WHERE ExpName LIKE '%" + searchword + "%' ";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            Expense expense = new Expense();

            expense.setFEXPENSE_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FEXPENSE_CODE)));
            expense.setFEXPENSE_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FEXPENSE_NAME)));

            list.add(expense);

        }

        return list;
    }


    public String getReasonByCode(String sCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }


        String selectQuery = "SELECT ExpName FROM " + DatabaseHelper.TABLE_FEXPENSE + " WHERE ExpCode='" + sCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FEXPENSE_NAME));
        }

        cursor.close();
        dB.close();

        return null;

    }


}
