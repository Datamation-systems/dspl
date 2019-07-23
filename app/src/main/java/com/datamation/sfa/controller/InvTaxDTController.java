package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.InvDet;
import com.datamation.sfa.model.InvTaxDt;
import com.datamation.sfa.model.TaxDet;

import java.math.BigDecimal;
import java.util.ArrayList;

public class InvTaxDTController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "InvTaxRG ";

    public InvTaxDTController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int UpdateInvTaxDT(ArrayList<InvDet> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (InvDet invDet : list) {

                if (invDet.getFINVDET_TYPE().equals("SA")) {

                    BigDecimal amt = new BigDecimal(invDet.getFINVDET_AMT());

                    ArrayList<TaxDet> taxcodelist = new TaxDetController(context).getTaxInfoByComCode(invDet.getFINVDET_TAX_COM_CODE());

                    for (int i = taxcodelist.size() - 1; i > -1; i--) {

                        BigDecimal tax = new BigDecimal("0");

                        ContentValues values = new ContentValues();

                        values.put(DatabaseHelper.INVTAXDT_ITEMCODE, invDet.getFINVDET_ITEM_CODE());
                        values.put(DatabaseHelper.INVTAXDT_RATE, taxcodelist.get(i).getRATE());
                        values.put(DatabaseHelper.REFNO, invDet.getFINVDET_REFNO());
                        values.put(DatabaseHelper.INVTAXDT_SEQ, taxcodelist.get(i).getSEQ());
                        values.put(DatabaseHelper.INVTAXDT_TAXCODE, taxcodelist.get(i).getTAXCODE());
                        values.put(DatabaseHelper.INVTAXDT_TAXCOMCODE, taxcodelist.get(i).getTAXCOMCODE());
                        values.put(DatabaseHelper.INVTAXDT_TAXPER, taxcodelist.get(i).getTAXVAL());
                        values.put(DatabaseHelper.INVTAXDT_TAXTYPE, taxcodelist.get(i).getTAXTYPE());

                        tax = new BigDecimal(taxcodelist.get(i).getTAXVAL()).multiply(amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN));
                        amt = new BigDecimal(taxcodelist.get(i).getTAXVAL()).add(new BigDecimal("100")).multiply((amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN)));

                        values.put(DatabaseHelper.INVTAXDT_BDETAMT, String.format("%.2f", tax));
                        values.put(DatabaseHelper.INVTAXDT_DETAMT, String.format("%.2f", tax));

                        count = (int) dB.insert(DatabaseHelper.TABLE_INVTAXDT, null, values);

                    }
                }
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;

    }

    public int UpdatePreTaxDT(ArrayList<InvDet> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (InvDet invDet : list) {

                if (invDet.getFINVDET_TYPE().equals("SA")) {

                    BigDecimal amt = new BigDecimal(invDet.getFINVDET_AMT());

                    ArrayList<TaxDet> taxcodelist = new TaxDetController(context).getTaxInfoByComCode(invDet.getFINVDET_TAX_COM_CODE());

                    for (int i = taxcodelist.size() - 1; i > -1; i--) {

                        BigDecimal tax = new BigDecimal("0");

                        ContentValues values = new ContentValues();

                        values.put(DatabaseHelper.INVTAXDT_ITEMCODE, invDet.getFINVDET_ITEM_CODE());
                        values.put(DatabaseHelper.INVTAXDT_RATE, taxcodelist.get(i).getRATE());
                        values.put(DatabaseHelper.REFNO, invDet.getFINVDET_REFNO());
                        values.put(DatabaseHelper.INVTAXDT_SEQ, taxcodelist.get(i).getSEQ());
                        values.put(DatabaseHelper.INVTAXDT_TAXCODE, taxcodelist.get(i).getTAXCODE());
                        values.put(DatabaseHelper.INVTAXDT_TAXCOMCODE, taxcodelist.get(i).getTAXCOMCODE());
                        values.put(DatabaseHelper.INVTAXDT_TAXPER, taxcodelist.get(i).getTAXVAL());
                        values.put(DatabaseHelper.INVTAXDT_TAXTYPE, taxcodelist.get(i).getTAXTYPE());

                        tax = new BigDecimal(taxcodelist.get(i).getTAXVAL()).multiply(amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN));
                        amt = new BigDecimal(taxcodelist.get(i).getTAXVAL()).add(new BigDecimal("100")).multiply((amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN)));

                        values.put(DatabaseHelper.INVTAXDT_BDETAMT, String.format("%.2f", tax));
                        values.put(DatabaseHelper.INVTAXDT_DETAMT, String.format("%.2f", tax));

                        count = (int) dB.insert(DatabaseHelper.TABLE_INVTAXDT, null, values);

                    }
                }
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;

    }

    public ArrayList<InvTaxDt> getAllTaxDT(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvTaxDt> list = new ArrayList<InvTaxDt>();
        try {
            String selectQuery = "select * from " + DatabaseHelper.TABLE_INVTAXDT + " WHERE RefNo='" + RefNo + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                InvTaxDt tax = new InvTaxDt();

                tax.setREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                tax.setTAXCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_TAXCODE)));
                tax.setBTAXDETAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_BDETAMT)));
                tax.setTAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_TAXCOMCODE)));
                tax.setTAXDETAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_DETAMT)));
                tax.setTAXPER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_TAXPER)));
                tax.setTAXRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_RATE)));
                tax.setTAXSEQ(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_SEQ)));
                tax.setITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_ITEMCODE)));
                tax.setTAXTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_TAXTYPE)));

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

    public ArrayList<InvTaxDt> getTaxDTSummery(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvTaxDt> list = new ArrayList<InvTaxDt>();
        try {
            String selectQuery = "select TaxType,TaxPer,TaxSeq,SUM(TaxDetAmt) as TotTax FROM " + DatabaseHelper.TABLE_INVTAXDT + " WHERE RefNo='" + RefNo + "' GROUP BY TaxType ORDER BY TaxSeq ASC";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                InvTaxDt tax = new InvTaxDt();

                tax.setTAXDETAMT(cursor.getString(cursor.getColumnIndex("TotTax")));
                tax.setTAXPER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_TAXPER)));
                tax.setTAXSEQ(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_SEQ)));
                tax.setTAXTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INVTAXDT_TAXTYPE)));

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

            dB.delete(DatabaseHelper.TABLE_INVTAXDT, DatabaseHelper.REFNO + "='" + RefNo + "'", null);
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

    }

}
