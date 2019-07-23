package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.model.TaxDet;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PreSaleTaxDTDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "PreTaxRG ";

    public PreSaleTaxDTDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int UpdateSalesTaxDT(ArrayList<OrderDetail> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (OrderDetail invDet : list) {

                if (invDet.getFORDERDET_TYPE().equals("SA")) {

                    ArrayList<TaxDet> taxcodelist = new TaxDetController(context).getTaxInfoByComCode(invDet.getFORDERDET_TAXCOMCODE());
                    BigDecimal amt = new BigDecimal(invDet.getFORDERDET_AMT());

                    if (taxcodelist.size() > 0) {

                        for (int i = taxcodelist.size() - 1; i > -1; i--) {

                            BigDecimal tax = new BigDecimal("0");
                            ContentValues values = new ContentValues();

                            values.put(DatabaseHelper.PRETAXDT_ITEMCODE, invDet.getFORDERDET_ITEMCODE());
                            values.put(DatabaseHelper.PRETAXDT_RATE, taxcodelist.get(i).getRATE());
                            values.put(DatabaseHelper.PRETAXDT_REFNO, invDet.getFORDERDET_REFNO());
                            values.put(DatabaseHelper.PRETAXDT_SEQ, taxcodelist.get(i).getSEQ());
                            values.put(DatabaseHelper.PRETAXDT_TAXCODE, taxcodelist.get(i).getTAXCODE());
                            values.put(DatabaseHelper.PRETAXDT_TAXCOMCODE, taxcodelist.get(i).getTAXCOMCODE());
                            values.put(DatabaseHelper.PRETAXDT_TAXPER, taxcodelist.get(i).getTAXVAL());
                            values.put(DatabaseHelper.PRETAXDT_TAXTYPE, taxcodelist.get(i).getTAXTYPE());

                            tax = new BigDecimal(taxcodelist.get(i).getTAXVAL()).multiply(amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN));
                            amt = new BigDecimal(taxcodelist.get(i).getTAXVAL()).add(new BigDecimal("100")).multiply((amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN)));

                            values.put(DatabaseHelper.PRETAXDT_BDETAMT, String.format("%.2f", tax));
                            values.put(DatabaseHelper.PRETAXDT_DETAMT, String.format("%.2f", tax));

                            count = (int) dB.insert(DatabaseHelper.TABLE_PRETAXDT, null, values);

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

//    public ArrayList<TaxDT> getAllTaxDT(String RefNo) {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<TaxDT> list = new ArrayList<TaxDT>();
//        try {
//            String selectQuery = "select * from " + dbHelper.TABLE_PRETAXDT + " WHERE RefNo='" + RefNo + "'";
//
//            Cursor cursor = dB.rawQuery(selectQuery, null);
//
//            while (cursor.moveToNext()) {
//                TaxDT tax = new TaxDT();
//
//                tax.setREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_REFNO)));
//                tax.setTAXCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_TAXCODE)));
//                tax.setBTAXDETAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_BDETAMT)));
//                tax.setTAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_TAXCOMCODE)));
//                tax.setTAXDETAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_DETAMT)));
//                tax.setTAXPER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_TAXPER)));
//                tax.setTAXRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_RATE)));
//                tax.setTAXSEQ(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_SEQ)));
//                tax.setITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_ITEMCODE)));
//
//                list.add(tax);
//            }
//            cursor.close();
//
//        } catch (Exception e) {
//            Log.v("Erorr ", e.toString());
//
//        } finally {
//            dB.close();
//        }
//
//        return list;
//
//    }
//
//    public ArrayList<TaxDT> getTaxDTSummery(String RefNo) {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<TaxDT> list = new ArrayList<TaxDT>();
//        try {
//            String selectQuery = "select TaxType,TaxPer,TaxSeq,SUM(TaxDetAmt) as TotTax FROM " + DatabaseHelper.TABLE_PRETAXDT + " WHERE RefNo='" + RefNo + "' GROUP BY TaxType ORDER BY TaxSeq ASC";
//
//            Cursor cursor = dB.rawQuery(selectQuery, null);
//
//            while (cursor.moveToNext()) {
//                TaxDT tax = new TaxDT();
//
//                tax.setTAXDETAMT(cursor.getString(cursor.getColumnIndex("TotTax")));
//                tax.setTAXPER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_TAXPER)));
//                tax.setTAXSEQ(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_SEQ)));
//                tax.setTAXTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRETAXDT_TAXTYPE)));
//
//                list.add(tax);
//            }
//            cursor.close();
//
//        } catch (Exception e) {
//            Log.v("Erorr ", e.toString());
//
//        } finally {
//            dB.close();
//        }
//
//        return list;
//
//    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void ClearTable(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {

            dB.delete(DatabaseHelper.TABLE_PRETAXDT, DatabaseHelper.PRETAXDT_REFNO + "='" + RefNo + "'", null);
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

    }

}
