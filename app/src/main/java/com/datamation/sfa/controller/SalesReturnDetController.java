package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.DayNPrdDet;
import com.datamation.sfa.model.FInvRDet;
import com.datamation.sfa.model.FInvRHed;
import com.datamation.sfa.model.OrderDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SalesReturnDetController
{
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "SFA";

    public SalesReturnDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateInvRDet(ArrayList<FInvRDet> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (FInvRDet invrDet : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FINVRDET + " WHERE " + dbHelper.FINVRDET_ID + " = '" + invrDet.getFINVRDET_ID() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(dbHelper.REFNO, invrDet.getFINVRDET_REFNO());
                values.put(dbHelper.TXNDATE, invrDet.getFINVRDET_TXN_DATE());
                values.put(dbHelper.FINVRDET_QTY, invrDet.getFINVRDET_QTY());
                values.put(dbHelper.FINVRDET_BAL_QTY, invrDet.getFINVRDET_BAL_QTY());
                values.put(dbHelper.FINVRDET_ITEMCODE, invrDet.getFINVRDET_ITEMCODE());
                values.put(dbHelper.FINVRDET_TAXCOMCODE, invrDet.getFINVRDET_TAXCOMCODE());
                values.put(dbHelper.FINVRDET_PRILCODE, invrDet.getFINVRDET_PRILCODE());
                values.put(dbHelper.FINVRDET_IS_ACTIVE, invrDet.getFINVRDET_IS_ACTIVE());
                values.put(dbHelper.FINVRDET_COST_PRICE, invrDet.getFINVRDET_COST_PRICE());
                values.put(dbHelper.FINVRDET_SELL_PRICE, invrDet.getFINVRDET_SELL_PRICE());
                values.put(dbHelper.FINVRDET_T_SELL_PRICE, invrDet.getFINVRDET_T_SELL_PRICE());
                values.put(dbHelper.FINVRDET_AMT, invrDet.getFINVRDET_AMT());
                values.put(dbHelper.FINVRDET_DIS_AMT, invrDet.getFINVRDET_DIS_AMT());
                values.put(dbHelper.FINVRDET_TAX_AMT, invrDet.getFINVRDET_TAX_AMT());
                values.put(dbHelper.FINVRDET_TXN_TYPE, invrDet.getFINVRDET_TXN_TYPE());
                values.put(dbHelper.FINVRDET_SEQNO, invrDet.getFINVRDET_SEQNO());
                values.put(dbHelper.FINVRDET_REASON_NAME, invrDet.getFINVRDET_RETURN_REASON());
                values.put(dbHelper.FINVRDET_REASON_CODE, invrDet.getFINVRDET_RETURN_REASON_CODE());
                values.put(dbHelper.FINVRDET_RETURN_TYPE, invrDet.getFINVRDET_RETURN_TYPE());
                //values.put(dbHelper.FINVRDET_FREE_QTY, invrDet.getFr());
               // values.put(dbHelper.FINVRDET_PRICE, invrDet.getFINVRDET_SELL_PRICE());
                values.put(dbHelper.FINVRDET_CHANGED_PRICE, invrDet.getFINVRDET_CHANGED_PRICE());

                int cn = cursor.getCount();
                if (cn > 0) {

                    count = dB.update(dbHelper.TABLE_FINVRDET, values, dbHelper.FINVRDET_ID + " =?", new String[]{String
                            .valueOf(invrDet.getFINVRDET_ID())});

                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FINVRDET, null, values);
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
    public ArrayList<FInvRDet> getAllInvRDetForPrint(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVRDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "' ";

//        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVRDET
//                + " WHERE "
//                + DatabaseHelper.REFNO + " in (select RefNo from FInvRHed where InvRefNo = '" + refno + "')";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            FInvRDet invrDet = new FInvRDet();

            invrDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ID)));
            invrDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_AMT)));
            invrDet.setFINVRDET_COST_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_COST_PRICE)));
            invrDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SELL_PRICE)));
            invrDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_T_SELL_PRICE)));
            invrDet.setFINVRDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_IS_ACTIVE)));
            invrDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
            invrDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
            invrDet.setFINVRDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_BAL_QTY)));
            invrDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
            invrDet.setFINVRDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
            invrDet.setFINVRDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAXCOMCODE)));
            invrDet.setFINVRDET_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRILCODE)));
            invrDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_AMT)));
            invrDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAX_AMT)));
            invrDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TXN_TYPE)));
            invrDet.setFINVRDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SEQNO)));
            invrDet.setFINVRDET_RETURN_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_NAME)));
            invrDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_CODE)));
            invrDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_RETURN_TYPE)));
            invrDet.setFINVRDET_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_CHANGED_PRICE)));
            list.add(invrDet);

        }

        return list;
    }

    public ArrayList<FInvRDet> getAllItemsAddedInCurrentReturn(String refNo)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select ItemCode,Qty,ReasonName from " + DatabaseHelper.TABLE_FINVRDET + " WHERE " + DatabaseHelper.REFNO + "='" + refNo + "' "         ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try
        {
            while (cursor.moveToNext())
            {
                FInvRDet fInvRDet = new FInvRDet();
                fInvRDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
                fInvRDet.setFINVRDET_RETURN_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_NAME)));
                fInvRDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
                list.add(fInvRDet);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.v(TAG + " Exception", ex.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }
    public void UpdateReturnTot(ArrayList<FInvRDet> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double totTax = 0, totalAmt = 0;

        try {

            for (FInvRDet retDet : list) {

                /* Calculate only for SA */
                if (retDet.getFINVRDET_TXN_TYPE().equals("SR")) {

                    //no need to mega heaters.get only total of tax detail amounts - commented 2018-10-23
                    // String sArray[] = new TaxDetDS(context).calculateTaxForward(ordDet.getFINVDET_ITEM_CODE(), Double.parseDouble(ordDet.getFINVDET_AMT()));

                    // totTax += Double.parseDouble(retDet.getFINVDET_TAX_AMT());
                    totalAmt += Double.parseDouble(retDet.getFINVRDET_AMT());

                    //  String updateQuery = "UPDATE finvdet SET taxamt='" + sArray[1] + "', amt='" + sArray[0] + "' WHERE Itemcode='" + ordDet.getFINVDET_ITEM_CODE() + "' AND refno='" + ordDet.getFINVDET_REFNO() + "' AND types='SA'";
                    //  dB.execSQL(updateQuery);
                }
            }
            /* Update Sales order Header TotalTax */
            dB.execSQL("UPDATE FInvRHed SET  TotalAmt = '" + totalAmt + "' WHERE RefNo='" + list.get(0).getFINVRDET_REFNO()+ "'");

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }
    public int mDeleteRetDet(String Itemcode, String RefNo)
    {

        int retVla = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try
        {
            return retVla = dB.delete(DatabaseHelper.TABLE_FINVRDET, DatabaseHelper.REFNO + " ='" + RefNo.trim() + "' AND " + DatabaseHelper.FINVRDET_ITEMCODE + " ='" + Itemcode.trim() + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return retVla;
    }

    public ArrayList<FInvRDet> getAllInvRDetForDirectReturn(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVRDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'" + " AND " + dbHelper.FINVRHED_INV_REFNO + " IS NOT NULL AND " + dbHelper.FINVRHED_ORD_REFNO + " IS  NOT NULL";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            FInvRDet invrDet = new FInvRDet();

            invrDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ID)));
            invrDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_AMT)));
            invrDet.setFINVRDET_COST_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_COST_PRICE)));
            invrDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SELL_PRICE)));
            invrDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_T_SELL_PRICE)));
            invrDet.setFINVRDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_IS_ACTIVE)));
            invrDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
            invrDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
            invrDet.setFINVRDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_BAL_QTY)));
            invrDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
            invrDet.setFINVRDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
            invrDet.setFINVRDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAXCOMCODE)));
            invrDet.setFINVRDET_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRILCODE)));
            invrDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_AMT)));
            invrDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAX_AMT)));
            invrDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TXN_TYPE)));
            invrDet.setFINVRDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SEQNO)));
            invrDet.setFINVRDET_RETURN_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_NAME)));
            invrDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_CODE)));
            invrDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_RETURN_TYPE)));
            //invrDet.setFINVRDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRICE)));
            invrDet.setFINVRDET_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_CHANGED_PRICE)));
            //invrDet.setFINVRDET_DIS_RATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_RATE)));
            list.add(invrDet);
        }

        return list;
    }

    public ArrayList<FInvRDet> getAllInvRDetForInvoice(String refno) { // only for inner return of invoice
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from FInvRDet where RefNo in (select Refno from FInvRHed where IsActive = 1 and InvRefNo IS NOT NULL AND OrdRefNo IS NULL)";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)).equals(refno))
            {
                FInvRDet invrDet = new FInvRDet();

                invrDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ID)));
                invrDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_AMT)));
                invrDet.setFINVRDET_COST_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_COST_PRICE)));
                invrDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SELL_PRICE)));
                invrDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_T_SELL_PRICE)));
                invrDet.setFINVRDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_IS_ACTIVE)));
                invrDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
                invrDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
                invrDet.setFINVRDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_BAL_QTY)));
                invrDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                invrDet.setFINVRDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                invrDet.setFINVRDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAXCOMCODE)));
                invrDet.setFINVRDET_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRILCODE)));
                invrDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_AMT)));
                invrDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAX_AMT)));
                invrDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TXN_TYPE)));
                invrDet.setFINVRDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SEQNO)));
                invrDet.setFINVRDET_RETURN_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_NAME)));
                invrDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_CODE)));
                invrDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_RETURN_TYPE)));
                //invrDet.setFINVRDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRICE)));
                invrDet.setFINVRDET_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_CHANGED_PRICE)));
                //invrDet.setFINVRDET_DIS_RATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_RATE)));
                list.add(invrDet);
            }
        }

        return list;
    }
    public ArrayList<FInvRDet> getAllInvRDet(String refno) { // for upload purpose only
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVRDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)).equals(refno))
            {
                FInvRDet invrDet = new FInvRDet();

                invrDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ID)));
                invrDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_AMT)));
                invrDet.setFINVRDET_COST_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_COST_PRICE)));
                invrDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SELL_PRICE)));
                invrDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_T_SELL_PRICE)));
                invrDet.setFINVRDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_IS_ACTIVE)));
                invrDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
                invrDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
                invrDet.setFINVRDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_BAL_QTY)));
                invrDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                invrDet.setFINVRDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                invrDet.setFINVRDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAXCOMCODE)));
                invrDet.setFINVRDET_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRILCODE)));
                invrDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_AMT)));
                invrDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAX_AMT)));
                invrDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TXN_TYPE)));
                invrDet.setFINVRDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SEQNO)));
                invrDet.setFINVRDET_RETURN_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_NAME)));
                invrDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_CODE)));
                invrDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_RETURN_TYPE)));
                //invrDet.setFINVRDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRICE)));
                invrDet.setFINVRDET_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_CHANGED_PRICE)));
                //invrDet.setFINVRDET_DIS_RATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_RATE)));
                list.add(invrDet);
            }
        }

        return list;
    }

    public ArrayList<FInvRDet> getAllInvRDetForSalesReturn(String refno) { // for direct sales return
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        //String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVRDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";
        String selectQuery = "select * from FInvRDet where RefNo in (select Refno from FInvRHed where IsActive = 1 and IsSync = 0 and OrdRefNo IS NULL AND InvRefNo IS NULL)";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)).equals(refno))
            {
                FInvRDet invrDet = new FInvRDet();

                invrDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ID)));
                invrDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_AMT)));
                invrDet.setFINVRDET_COST_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_COST_PRICE)));
                invrDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SELL_PRICE)));
                invrDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_T_SELL_PRICE)));
                invrDet.setFINVRDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_IS_ACTIVE)));
                invrDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
                invrDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
                invrDet.setFINVRDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_BAL_QTY)));
                invrDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                invrDet.setFINVRDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                invrDet.setFINVRDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAXCOMCODE)));
                invrDet.setFINVRDET_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRILCODE)));
                invrDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_AMT)));
                invrDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAX_AMT)));
                invrDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TXN_TYPE)));
                invrDet.setFINVRDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SEQNO)));
                invrDet.setFINVRDET_RETURN_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_NAME)));
                invrDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_CODE)));
                invrDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_RETURN_TYPE)));
                //invrDet.setFINVRDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRICE)));
                invrDet.setFINVRDET_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_CHANGED_PRICE)));
                //invrDet.setFINVRDET_DIS_RATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_RATE)));
                list.add(invrDet);
            }
        }

        return list;
    }

    public ArrayList<FInvRDet> getAllInvRDetForOrders(String refno) { // only for inner return of sales order
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from FInvRDet where RefNo in (select Refno from FInvRHed where IsActive = 1 and OrdRefNo IS NOT NULL AND InvRefNo IS NULL)";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)).equals(refno))
            {
                FInvRDet invrDet = new FInvRDet();

                invrDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ID)));
                invrDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_AMT)));
                invrDet.setFINVRDET_COST_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_COST_PRICE)));
                invrDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SELL_PRICE)));
                invrDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_T_SELL_PRICE)));
                invrDet.setFINVRDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_IS_ACTIVE)));
                invrDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
                invrDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
                invrDet.setFINVRDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_BAL_QTY)));
                invrDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                invrDet.setFINVRDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                invrDet.setFINVRDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAXCOMCODE)));
                invrDet.setFINVRDET_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRILCODE)));
                invrDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_AMT)));
                invrDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAX_AMT)));
                invrDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TXN_TYPE)));
                invrDet.setFINVRDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SEQNO)));
                invrDet.setFINVRDET_RETURN_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_NAME)));
                invrDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_CODE)));
                invrDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_RETURN_TYPE)));
                //invrDet.setFINVRDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRICE)));
                invrDet.setFINVRDET_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_CHANGED_PRICE)));
                //invrDet.setFINVRDET_DIS_RATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_RATE)));
                list.add(invrDet);
            }
        }

        return list;
    }

    public int updateProductPrice(String itemCode, String price) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FINVRDET_SELL_PRICE, price);
            values.put(DatabaseHelper.FINVRDET_T_SELL_PRICE, price);
            count=(int)dB.update(DatabaseHelper.TABLE_FINVRDET, values, DatabaseHelper.FINVRDET_ITEMCODE + " =?", new String[]{String.valueOf(itemCode)});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
        return  count;
    }

    public int restData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            count = dB.delete(DatabaseHelper.TABLE_FINVRDET, DatabaseHelper.REFNO + " ='" + refno + "'", null);

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

    public int restDataDirectSalesReturnDets(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            count = dB.delete(DatabaseHelper.TABLE_FINVRDET, DatabaseHelper.REFNO + " ='" + refno + "'", null);

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

    public int getItemCount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(RefNo) as RefNoCount FROM " + DatabaseHelper.TABLE_FINVRDET + " WHERE  " + DatabaseHelper.REFNO + "='" + refNo + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                return Integer.parseInt(cursor.getString(cursor.getColumnIndex("RefNoCount")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }
        return 0;

    }

    public int InactiveStatusUpdate(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVRDET + " WHERE " + DatabaseHelper.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FINVRDET_IS_ACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(DatabaseHelper.TABLE_FINVRDET, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(refno)});
            } else {
                count = (int) dB.insert(DatabaseHelper.TABLE_FINVRDET, null, values);
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

    public ArrayList<FInvRDet> getReturnItemsforPrint(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVRDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";

        try {

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                FInvRDet reDet = new FInvRDet();

                reDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_AMT)));
                reDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
                reDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
                reDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TXN_TYPE)));
                reDet.setFINVRDET_REFNO(refno);
                reDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_AMT)));
                //reDet.setFTRANSODET_SCHDISC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTRANSODET_DISVALAMT)));
                reDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SELL_PRICE)));
                reDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_T_SELL_PRICE)));
                reDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_RETURN_TYPE)));
                reDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAX_AMT)));

                list.add(reDet);
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }

    public ArrayList<FInvRDet> getAllActiveNPs() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from " + dbHelper.TABLE_FINVRDET + " WHERE " + dbHelper.FINVRDET_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                FInvRDet invrDet = new FInvRDet();

                invrDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ID)));
                invrDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_AMT)));
                invrDet.setFINVRDET_COST_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_COST_PRICE)));
                invrDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SELL_PRICE)));
                invrDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_T_SELL_PRICE)));
                invrDet.setFINVRDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_IS_ACTIVE)));
                invrDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
                invrDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
                invrDet.setFINVRDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_BAL_QTY)));
                invrDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                invrDet.setFINVRDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                invrDet.setFINVRDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAXCOMCODE)));
                invrDet.setFINVRDET_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRILCODE)));
                invrDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_AMT)));
                invrDet.setFINVRDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAX_AMT)));
                invrDet.setFINVRDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TXN_TYPE)));
                invrDet.setFINVRDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SEQNO)));
                invrDet.setFINVRDET_RETURN_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_NAME)));
                invrDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_REASON_CODE)));
                invrDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_RETURN_TYPE)));
                //invrDet.setFINVRDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_PRICE)));
                invrDet.setFINVRDET_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_CHANGED_PRICE)));

                list.add(invrDet);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }

    public FInvRDet getActiveReturnRefNo()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        FInvRDet ordDet = new FInvRDet();

        String selectQuery = "select * from " + dbHelper.TABLE_FINVRDET + " WHERE " + dbHelper.FINVRDET_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                ordDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_ID)));
                ordDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_REASON_CODE)));
                ordDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_RETURN_TYPE)));
                ordDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_QTY)));
                ordDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_AMT)));


            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return ordDet;
    }

    public boolean isAnyActiveRetuens(String RefNo)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from " + dbHelper.TABLE_FINVRDET + " WHERE " + dbHelper.FINVRDET_IS_ACTIVE + "='" + "1" + "'" + " AND " + dbHelper.REFNO + "='" + RefNo + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            if (cursor.getCount()>0)
            {
                return true;
            }
            else
            {
                return false;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return false;
    }

    public boolean isAnyActiveReturnHedDet(String refNo)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String status = "";

        String selectQuery = "select * from " + dbHelper.TABLE_FINVRHED + " WHERE " + dbHelper.FINVRHED_IS_ACTIVE + "='" + "1" + "'" + " AND " + dbHelper.REFNO + "='" + refNo + "'" + " AND " + dbHelper.FINVRHED_INV_REFNO + " IS NULL AND " + dbHelper.FINVRHED_ORD_REFNO + " IS NULL";  ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            if (cursor.getCount()>0)
            {
                while (cursor.moveToNext())
                {
                    status = cursor.getString(cursor.getColumnIndex(dbHelper.REFNO));
                }

                cursor.close();

//                //String hedQuery = "select * from " + dbHelper.TABLE_FINVRHED + " WHERE " + dbHelper.FINVRHED_INV_REFNO + "='" + null + "'" + " AND " + dbHelper.FINVRHED_ORD_REFNO + "='" + null + "'" + dbHelper.FINVRDET_IS_ACTIVE + "='" + "1" + "'" + " AND " + dbHelper.REFNO + "='" + RefNo + "'";
//                String hedQuery = "select * from FInvRHed where RefNo in (select RefNo from FInvRDet where IsActive = 1) and IsActive = 1 and InvRefNo is Null and OrdRefNo is Null";
//
//                Cursor c = dB.rawQuery(hedQuery, null);
//
//                try
//                {
//                    if(c.getCount()>0)
//                    {
//                        return true;
//                    }
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                finally {
//                    if (c != null)
//                    {
//                        c.close();
//                    }
//                    dB.close();
//                }
            }
            else
            {
                return false;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        if (!status.equals(""))
        {
            return true;
        }

        return false;
    }

    public void UpdateItemTaxInfo(ArrayList<FInvRDet> list, String debtorCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double totTax = 0, totalAmt = 0, bTSellPrice = 0, tSellPrice = 0, totDisc=0, disR=0;

        try {

            for (FInvRDet invRDet : list) {

                /* Calculate only for MR or UR */
                if (invRDet.getFINVRDET_RETURN_TYPE().equals("SA")) {

                    String sArray[] = new TaxDetController(context).calculateTaxForwardFromDebTax(debtorCode, invRDet.getFINVRDET_ITEMCODE(), Double.parseDouble(invRDet.getFINVRDET_AMT()));

                    bTSellPrice = Double.parseDouble(sArray[0])/Double.parseDouble(invRDet.getFINVRDET_QTY());
                    if (Double.parseDouble(invRDet.getFINVRDET_DIS_AMT())>0.0)
                    {
                        tSellPrice = (Double.parseDouble(sArray[0])+ Double.parseDouble(invRDet.getFINVRDET_DIS_AMT()))/Double.parseDouble(invRDet.getFINVRDET_QTY());
                        disR = ((Double.parseDouble(invRDet.getFINVRDET_DIS_AMT()))/(Double.parseDouble(sArray[0])+ Double.parseDouble(invRDet.getFINVRDET_DIS_AMT())))* 100;
                        totDisc += Double.parseDouble(invRDet.getFINVRDET_DIS_AMT());
                    }
                    else
                    {
                        tSellPrice = Double.parseDouble(sArray[0])/Double.parseDouble(invRDet.getFINVRDET_QTY());
                    }

                    totTax += Double.parseDouble(sArray[1]);
                    totalAmt += Double.parseDouble(sArray[0]);

                    String updateQuery = "UPDATE FInvRDet SET TaxAmt='" + sArray[1] + "', Amt='" + sArray[0] + "', DisRate='" + String.valueOf(disR) + "', TSellPrice ='" + tSellPrice + "' where Itemcode ='" + invRDet.getFINVRDET_ITEMCODE() + "' AND refno='" + invRDet.getFINVRDET_REFNO() + "' AND ReturnType!='FR'";
                    dB.execSQL(updateQuery);
                }
            }
            /* Update sales return Header TotalTax */
            dB.execSQL("UPDATE FInvRHed SET TotalTax='" + totTax + "',TotalDis='" + totDisc + "',TotalAmt='" + totalAmt + "' WHERE refno='" + list.get(0).getFINVRDET_REFNO() + "'");

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

    public ArrayList<FInvRDet> getEveryItem(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select Itemcode,RefNo,TaxComCode,SellPrice,Qty,Amt,ReturnType,DisAmt from " + DatabaseHelper.TABLE_FINVRDET + " WHERE RefNo='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                FInvRDet IRDet = new FInvRDet();

                IRDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_ITEMCODE)));
                IRDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                IRDet.setFINVRDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_TAXCOMCODE)));
                IRDet.setFINVRDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_SELL_PRICE)));
                IRDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_QTY)));
                IRDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_AMT)));
                IRDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_RETURN_TYPE)));
                IRDet.setFINVRDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVRDET_DIS_AMT)));

                list.add(IRDet);

            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }

    public ArrayList<FInvRDet> getTodayOrderDets(String refno) {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FInvRDet> list = new ArrayList<FInvRDet>();

        String selectQuery = "select * from FInvRDet WHERE "
                + dbHelper.REFNO + "='" + refno + "' and  txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                FInvRDet retDet = new FInvRDet();

                retDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_ID)));
                retDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_AMT)));
                retDet.setFINVRDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_ITEMCODE)));
                retDet.setFINVRDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_PRILCODE)));
                retDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_QTY)));
                retDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                retDet.setFINVRDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_SELL_PRICE)));
                retDet.setFINVRDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_IS_ACTIVE)));

                list.add(retDet);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }
}
