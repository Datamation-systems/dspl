package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FInvRDet;
import com.datamation.sfa.model.TaxDT;
import com.datamation.sfa.model.TaxDet;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SalesReturnTaxDTDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "SalesReturnTaxRG ";

    public SalesReturnTaxDTDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int UpdateReturnTaxDT(ArrayList<FInvRDet> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (FInvRDet invRDet : list) {

                if (invRDet.getFINVRDET_RETURN_TYPE().equals("MR")|| invRDet.getFINVRDET_RETURN_TYPE().equals("UR")) {

                    ArrayList<TaxDet> taxcodelist = new TaxDetController(context).getTaxInfoByComCode(invRDet.getFINVRDET_TAXCOMCODE());
                    BigDecimal amt = new BigDecimal(invRDet.getFINVRDET_AMT());

                    if (taxcodelist.size() > 0) {

                        for (int i = taxcodelist.size() - 1; i > -1; i--) {

                            BigDecimal tax = new BigDecimal("0");
                            ContentValues values = new ContentValues();

                            values.put(DatabaseHelper.INVRTAXDT_ITEMCODE, invRDet.getFINVRDET_ITEMCODE());
                            values.put(DatabaseHelper.INVRTAXDT_RATE, taxcodelist.get(i).getRATE());
                            values.put(DatabaseHelper.INVRTAXDT_REFNO, invRDet.getFINVRDET_REFNO());
                            values.put(DatabaseHelper.INVRTAXDT_SEQ, taxcodelist.get(i).getSEQ());
                            values.put(DatabaseHelper.INVRTAXDT_TAXCODE, taxcodelist.get(i).getTAXCODE());
                            values.put(DatabaseHelper.INVRTAXDT_TAXCOMCODE, taxcodelist.get(i).getTAXCOMCODE());
                            values.put(DatabaseHelper.INVRTAXDT_TAXPER, taxcodelist.get(i).getTAXVAL());
                            values.put(DatabaseHelper.INVRTAXDT_TAXTYPE, taxcodelist.get(i).getTAXTYPE());

                            tax = new BigDecimal(taxcodelist.get(i).getTAXVAL()).multiply(amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN));
                            amt = new BigDecimal(taxcodelist.get(i).getTAXVAL()).add(new BigDecimal("100")).multiply((amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN)));

                            values.put(DatabaseHelper.INVRTAXDT_BDETAMT, String.format("%.2f", tax));
                            values.put(DatabaseHelper.INVRTAXDT_DETAMT, String.format("%.2f", tax));

                            count = (int) dB.insert(DatabaseHelper.TABLE_INVRTAXDT, null, values);

                        }
                    }
                }
            }
        } catch (

                Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;

    }

    public ArrayList<TaxDT> getAllTaxDT(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<TaxDT> list = new ArrayList<TaxDT>();
        try {
            String selectQuery = "select * from " + dbHelper.TABLE_INVRTAXDT + " WHERE RefNo='" + RefNo + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                TaxDT tax = new TaxDT();

                tax.setREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXDT_REFNO)));
                tax.setTAXCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXDT_TAXCODE)));
                tax.setBTAXDETAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXDT_BDETAMT)));
                tax.setTAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXDT_TAXCOMCODE)));
                tax.setTAXDETAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXDT_DETAMT)));
                tax.setTAXPER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXDT_TAXPER)));
                tax.setTAXRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXDT_RATE)));
                tax.setTAXSEQ(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXDT_SEQ)));
                tax.setITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVRTAXDT_ITEMCODE)));

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

            dB.delete(DatabaseHelper.TABLE_INVRTAXDT, DatabaseHelper.INVRTAXDT_REFNO + "='" + RefNo + "'", null);
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

    }

}
