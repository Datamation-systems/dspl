package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FInvRDet;
import com.datamation.sfa.model.TaxDet;
import com.datamation.sfa.model.TaxRG;

import java.util.ArrayList;

public class SalesReturnTaxRGDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "SalesReturnTaxRGDS ";

    public SalesReturnTaxRGDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int UpdateReturnTaxRG(ArrayList<FInvRDet> list, String debtorCode) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            Cursor cursor = null;
            for (FInvRDet invRDet : list) {

                if (invRDet.getFINVRDET_RETURN_TYPE().equals("MR") || invRDet.getFINVRDET_RETURN_TYPE().equals("UR")) {

                    ArrayList<TaxDet> taxcodelist = new TaxDetController(context).getTaxInfoByComCode(invRDet.getFINVRDET_TAXCOMCODE());

                    for (TaxDet taxDet : taxcodelist) {

                        String s = "SELECT * FROM " + DatabaseHelper.TABLE_INVRTAXRG + " WHERE " + DatabaseHelper.INVRTAXRG_REFNO + "='" + invRDet.getFINVRDET_REFNO() + "' AND " + DatabaseHelper.INVRTAXRG_TAXCODE + "='" + taxDet.getTAXCODE() + "'";

                        cursor = dB.rawQuery(s, null);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.INVRTAXRG_REFNO, invRDet.getFINVRDET_REFNO());
                        values.put(DatabaseHelper.PRETAXRG_RGNO, new TaxController(context).getTaxRGNo(taxDet.getTAXCODE()));
                        //values.put(DatabaseHelper.INVRTAXRG_RGNO, new FDebTaxDS(context).getTaxRegNo(debtorCode));
                        values.put(DatabaseHelper.INVRTAXRG_TAXCODE, taxDet.getTAXCODE());

                        if (cursor.getCount() <= 0)
                            count = (int) dB.insert(DatabaseHelper.TABLE_INVRTAXRG, null, values);

                    }
                }
            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;

    }

    public ArrayList<TaxRG> getAllTaxRG(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<TaxRG> list = new ArrayList<TaxRG>();
        try {
            String selectQuery = "select * from " + DatabaseHelper.TABLE_INVRTAXRG + " WHERE RefNo='" + RefNo + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                TaxRG tax = new TaxRG();

                tax.setREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXRG_REFNO)));
                tax.setTAXCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXRG_TAXCODE)));
                tax.setRGNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXRG_RGNO)));
                list.add(tax);
            }
            cursor.close();

        } catch (Exception e) {
            Log.v("Erorr ", e.toString());

        } finally {
            dB.close();
        }

        return list;

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void ClearTable(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {

            dB.delete(DatabaseHelper.TABLE_INVRTAXRG, DatabaseHelper.INVRTAXRG_REFNO + "='" + RefNo + "'", null);
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

    }

}
