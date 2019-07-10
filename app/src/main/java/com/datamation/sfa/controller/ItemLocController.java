package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.InvDet;
import com.datamation.sfa.model.ItemLoc;

import java.util.ArrayList;

public class ItemLocController
{
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "Ebony";

    public ItemLocController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void InsertOrReplaceItemLoc(ArrayList<ItemLoc> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_FITEMLOC + " (ItemCode,LocCode,QOH,RecordId) VALUES (?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (ItemLoc itemLoc : list) {

                stmt.bindString(1, itemLoc.getFITEMLOC_ITEM_CODE());
                stmt.bindString(2, itemLoc.getFITEMLOC_LOC_CODE());
                stmt.bindString(3, itemLoc.getFITEMLOC_QOH());
                stmt.bindString(4, itemLoc.getFITEMLOC_RECORD_ID());

                stmt.execute();
                stmt.clearBindings();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dB.setTransactionSuccessful();
            dB.endTransaction();
            dB.close();
        }

    }

    public int deleteAllItemLoc() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FITEMLOC, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_FITEMLOC, null, null);
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
    public void UpdateInvoiceQOH(String RefNo, String Task, String locCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ArrayList<InvDet> list = new InvDetController(context).getAllItemsforPrint(RefNo);

            for (InvDet item : list) {

                int qoh = 0;

                Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FITEMLOC + " WHERE " + DatabaseHelper.FITEMLOC_ITEM_CODE + "='" + item.getFINVDET_ITEM_CODE() + "' AND " + DatabaseHelper.FITEMLOC_LOC_CODE + "='" + locCode + "'", null);
                int Qty = Integer.parseInt(item.getFINVDET_QTY());

                if (cursor.getCount() > 0) {

                    while (cursor.moveToNext()) {
                        qoh = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                    }

                    ContentValues values = new ContentValues();

                    if (Task.equals("+")) {
                        values.put(DatabaseHelper.FITEMLOC_QOH, String.valueOf(qoh + Qty));
                    } else {
                        values.put(DatabaseHelper.FITEMLOC_QOH, String.valueOf(qoh - Qty));
                    }

                    dB.update(DatabaseHelper.TABLE_FITEMLOC, values, DatabaseHelper.FITEMLOC_ITEM_CODE + "=? AND " + DatabaseHelper.FITEMLOC_LOC_CODE + "=?", new String[]{item.getFINVDET_ITEM_CODE(), locCode});

                }

                cursor.close();

            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

}
