package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.InvDet;
import com.datamation.sfa.model.OrderDisc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvDetController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbeHelper;
    private String TAG = "InvDet DS";

    public InvDetController(Context context) {
        this.context = context;
        dbeHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbeHelper.getWritableDatabase();
    }

    public int createOrUpdateInvDet(ArrayList<InvDet> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (InvDet invDet : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.FINVDET_ID
                        + " = '" + invDet.getFINVDET_ID() + "'";
                cursor = dB.rawQuery(selectQuery, null);

              //  values.put(DatabaseHelper.FINVDET_ID, invDet.getFINVDET_ID());
                values.put(DatabaseHelper.FINVDET_AMT, invDet.getFINVDET_AMT());
                values.put(DatabaseHelper.FINVDET_BAL_QTY, invDet.getFINVDET_BAL_QTY());
                values.put(DatabaseHelper.FINVDET_B_AMT, invDet.getFINVDET_B_AMT());
                values.put(DatabaseHelper.FINVDET_B_SELL_PRICE, invDet.getFINVDET_B_SELL_PRICE());
                values.put(DatabaseHelper.FINVDET_BT_TAX_AMT, invDet.getFINVDET_TAX_AMT());
                values.put(DatabaseHelper.FINVDET_BT_SELL_PRICE, invDet.getFINVDET_BT_SELL_PRICE());
                values.put(DatabaseHelper.FINVDET_DIS_AMT, invDet.getFINVDET_DIS_AMT());
                values.put(DatabaseHelper.FINVDET_DIS_PER, invDet.getFINVDET_DIS_PER());
                values.put(DatabaseHelper.FINVDET_ITEM_CODE, invDet.getFINVDET_ITEM_CODE());
                values.put(DatabaseHelper.FINVDET_PRIL_CODE, invDet.getFINVDET_PRIL_CODE());
                values.put(DatabaseHelper.FINVDET_QTY, invDet.getFINVDET_QTY());
                values.put(DatabaseHelper.FINVDET_PICE_QTY, invDet.getFINVDET_PICE_QTY());
                values.put(DatabaseHelper.FINVDET_TYPE, invDet.getFINVDET_TYPE());
                values.put(DatabaseHelper.FINVDET_RECORD_ID, invDet.getFINVDET_RECORD_ID());
                values.put(DatabaseHelper.REFNO, invDet.getFINVDET_REFNO());
                values.put(DatabaseHelper.FINVDET_SELL_PRICE, invDet.getFINVDET_SELL_PRICE());
                values.put(DatabaseHelper.FINVDET_SEQNO, invDet.getFINVDET_SEQNO());
                values.put(DatabaseHelper.FINVDET_TAX_AMT, invDet.getFINVDET_TAX_AMT());
                values.put(DatabaseHelper.FINVDET_TAX_COM_CODE, invDet.getFINVDET_TAX_COM_CODE());
                values.put(DatabaseHelper.FINVDET_T_SELL_PRICE, invDet.getFINVDET_T_SELL_PRICE());
                values.put(DatabaseHelper.TXNDATE, invDet.getFINVDET_TXN_DATE());
                values.put(DatabaseHelper.FINVDET_TXN_TYPE, invDet.getFINVDET_TXN_TYPE());
                values.put(DatabaseHelper.FINVDET_IS_ACTIVE, invDet.getFINVDET_IS_ACTIVE());

              //  values.put(DatabaseHelper.FINVDET_BRAND_DISC, invDet.getFINVDET_BRAND_DISC());
//                values.put(DatabaseHelper.FINVDET_COMDISPER, invDet.getFINVDET_COM_DISCPER());
//                values.put(DatabaseHelper.FINVDET_BRAND_DISPER, invDet.getFINVDET_BRAND_DISCPER());
//                values.put(DatabaseHelper.FINVDET_COMPDISC, invDet.getFINVDET_COMDISC());
//                values.put(DatabaseHelper.FINVDET_DISVALAMT, invDet.getFINVDET_DISVALAMT());
//                values.put(DatabaseHelper.FINVDET_QOH, invDet.getFINVDET_QOH());
//                values.put(DatabaseHelper.FINVDET_PRICE, invDet.getFINVDET_PRICE());
//                values.put(DatabaseHelper.FINVDET_CHANGED_PRICE, invDet.getFINVDET_CHANGED_PRICE());

                int cn = cursor.getCount();
                if (cn > 0) {

                    count = dB.update(DatabaseHelper.TABLE_FINVDET, values, DatabaseHelper.FINVDET_ID + " =?", new String[]{String.valueOf(invDet.getFINVDET_ID())});

                } else {
                    count = (int) dB.insert(DatabaseHelper.TABLE_FINVDET, null, values);
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

    public ArrayList<InvDet> getTodayOrderDets(String refno) {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvDet> list = new ArrayList<InvDet>();

        // String selectQuery = "select * from " + dbHelper.TABLE_ORDER_DETAIL + " WHERE "
        String selectQuery = "select * from finvdet WHERE "
                + DatabaseHelper.REFNO + "='" + refno + "' and  txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                InvDet ordDet = new InvDet();

//                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
//                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
//                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
//                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
//                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
//                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
//                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
//                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));

                ordDet.setFINVDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ID)));
                ordDet.setFINVDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_AMT)));
                ordDet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ITEM_CODE)));
                ordDet.setFINVDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_PRIL_CODE)));
                ordDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                ordDet.setFINVDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                ordDet.setFINVDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_SELL_PRICE)));
                ordDet.setFINVDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_IS_ACTIVE)));

                list.add(ordDet);

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
    public ArrayList<InvDet> getTodayInvoices() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<InvDet> list = new ArrayList<InvDet>();

        try {
            String selectQuery = "select hed.DebCode, det.RefNo, ifnull((sum(det.Amt)),0) as totAmt, ifnull((sum(det.Qty)),0) as totQty from finvHed hed, finvDet det" +
                    //			" fddbnote fddb where hed.refno = det.refno and det.FPRECDET_REFNO1 = fddb.refno and hed.txndate = '2019-04-12'";
                   // "  where hed.refno = det.refno and hed.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";
                    "  where  hed.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                InvDet recDet = new InvDet();

//
                recDet.setFINVDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                recDet.setFINVDET_AMT(cursor.getString(cursor.getColumnIndex("totAmt")));
                recDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex("totQty")));
                recDet.setFINVDET_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
              //TODO :set  discount, free

                list.add(recDet);
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
    public boolean isAnyActiveOrders()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.FINVDET_IS_ACTIVE + "='" + "1" + "'";

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
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<InvDet> getAllItemsforPrint(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        ArrayList<InvDet> list = new ArrayList<InvDet>();

        String selectQuery = "select itemcode,qty,amt,TSellPrice,types,disvalamt from " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";

        try {

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                InvDet invdet = new InvDet();

                invdet.setFINVDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_AMT)));
                invdet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ITEM_CODE)));
                invdet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                invdet.setFINVDET_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TYPE)));
                invdet.setFINVDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_T_SELL_PRICE)));
                invdet.setFINVDET_DISVALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DISVALAMT)));
                invdet.setFINVDET_REFNO(refno);
                list.add(invdet);
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
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public ArrayList<InvDet> getDetailforPrint(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        ArrayList<InvDet> list = new ArrayList<InvDet>();

        String selectQuery = "select a.itemcode,a.qty,a.amt,a.TSellPrice,a.types,a.disvalamt from " + DatabaseHelper.TABLE_FINVDET + " a WHERE a." + DatabaseHelper.REFNO + "='" + refno + "'";

        try {

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                InvDet invdet = new InvDet();

                invdet.setFINVDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_AMT)));
                invdet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ITEM_CODE)));
                invdet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                invdet.setFINVDET_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TYPE)));
                invdet.setFINVDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_T_SELL_PRICE)));
                invdet.setFINVDET_DISVALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DISVALAMT)));
                invdet.setFINVDET_REFNO(refno);
                list.add(invdet);
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
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<InvDet> getAllInvDet(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvDet> list = new ArrayList<InvDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "' AND types='SA'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {

            while (cursor.moveToNext()) {

                InvDet invDet = new InvDet();

             //   invDet.setFINVDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ID)));
                invDet.setFINVDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_AMT)));
                invDet.setFINVDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BAL_QTY)));
                invDet.setFINVDET_B_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_B_AMT)));
                invDet.setFINVDET_B_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_B_SELL_PRICE)));
                invDet.setFINVDET_BT_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BT_TAX_AMT)));
                invDet.setFINVDET_BT_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BT_SELL_PRICE)));
                invDet.setFINVDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DIS_AMT)));
                invDet.setFINVDET_DIS_PER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DIS_PER)));
                invDet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ITEM_CODE)));
                invDet.setFINVDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_PRIL_CODE)));
                invDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                invDet.setFINVDET_PICE_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_PICE_QTY)));
                invDet.setFINVDET_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TYPE)));
                invDet.setFINVDET_RECORD_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_RECORD_ID)));
                invDet.setFINVDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                invDet.setFINVDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_SELL_PRICE)));
                invDet.setFINVDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_SEQNO)));
                invDet.setFINVDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TAX_AMT)));
                invDet.setFINVDET_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TAX_COM_CODE)));
                invDet.setFINVDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_T_SELL_PRICE)));
                invDet.setFINVDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                invDet.setFINVDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TXN_TYPE)));
                invDet.setFINVDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_IS_ACTIVE)));

//                invDet.setFINVDET_DISVALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DISVALAMT)));
//                invDet.setFINVDET_COM_DISCPER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_COMDISPER)));
//                invDet.setFINVDET_BRAND_DISCPER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BRAND_DISPER)));
//                invDet.setFINVDET_COMDISC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_COMPDISC)));
//                invDet.setFINVDET_BRAND_DISC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BRAND_DISC)));
//                invDet.setFINVDET_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QOH)));
//                invDet.setFINVDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_PRICE)));
//                invDet.setFINVDET_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_CHANGED_PRICE)));

                list.add(invDet);

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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int deleteInvDetByID(String id) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.FINVDET_ID + "='" + id + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_FINVDET, DatabaseHelper.FINVDET_ID + "='" + id + "'", null);
                Log.v("OrdDet Deleted ", success + "");
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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<InvDet> getAllActiveVanDet(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvDet> list = new ArrayList<InvDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.FINVDET_TYPE + " in ('MR','UR','FD') AND " + DatabaseHelper.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                InvDet invDet = new InvDet();

                invDet.setFINVDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ID)));
                invDet.setFINVDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_AMT)));
                invDet.setFINVDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BAL_QTY)));
                invDet.setFINVDET_B_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_B_AMT)));
                invDet.setFINVDET_B_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_B_SELL_PRICE)));
                invDet.setFINVDET_BT_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BT_TAX_AMT)));
                invDet.setFINVDET_BT_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BT_SELL_PRICE)));
                invDet.setFINVDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DIS_AMT)));
                invDet.setFINVDET_DIS_PER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DIS_PER)));
                invDet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ITEM_CODE)));
                invDet.setFINVDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_PRIL_CODE)));
                invDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                invDet.setFINVDET_PICE_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_PICE_QTY)));
                invDet.setFINVDET_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TYPE)));
                invDet.setFINVDET_RECORD_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_RECORD_ID)));
                invDet.setFINVDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                invDet.setFINVDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_SELL_PRICE)));
                invDet.setFINVDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_SEQNO)));
                invDet.setFINVDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TAX_AMT)));
                invDet.setFINVDET_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TAX_COM_CODE)));
                invDet.setFINVDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_T_SELL_PRICE)));
                invDet.setFINVDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                invDet.setFINVDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TXN_TYPE)));
                invDet.setFINVDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_IS_ACTIVE)));

                invDet.setFINVDET_DISVALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DISVALAMT)));
                invDet.setFINVDET_COM_DISCPER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_COMDISPER)));
                invDet.setFINVDET_BRAND_DISCPER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BRAND_DISPER)));
                invDet.setFINVDET_COMDISC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_COMPDISC)));
                invDet.setFINVDET_BRAND_DISC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BRAND_DISC)));
                invDet.setFINVDET_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QOH)));

                list.add(invDet);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public double getTaxedSellprice(String refno, String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT SellPrice,TaxAmt,Qty  FROM " + DatabaseHelper.TABLE_FINVDET + " WHERE Itemcode='" + itemCode + "' AND RefNo='" + refno + "'";

        try {
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                double sellprice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_SELL_PRICE)));
                double qty = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                double taxamt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TAX_AMT)));

                return sellprice - (taxamt / qty);

            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }

        return 0.00;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*Get total amount for ref no-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getTotalForRefNo(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String tot = null;
        String selectQuery = "SELECT SUM(CAST(" + DatabaseHelper.FINVDET_AMT + " AS double)) AS 'total_amt'  FROM " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";
        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {

                if (cursor.getString(cursor.getColumnIndex("total_amt")) != null)
                    tot = cursor.getString(cursor.getColumnIndex("total_amt"));

            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return tot;
    }

	/*-*-*--*-*-*-*-*-*-*-*-*-change active status*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-**/

    public int InactiveStatusUpdate(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FINVDET_IS_ACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(DatabaseHelper.TABLE_FINVDET, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(refno)});
            } else {
                count = (int) dB.insert(DatabaseHelper.TABLE_FINVDET, null, values);
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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Reset Invoice details*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int restData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                count = dB.delete(DatabaseHelper.TABLE_FINVDET, DatabaseHelper.REFNO + " ='" + refno + "'", null);
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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int getItemCount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(RefNo) as RefNo FROM " + DatabaseHelper.TABLE_FINVDET + " WHERE  " + DatabaseHelper.REFNO + "='" + refNo + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                return Integer.parseInt(cursor.getString(cursor.getColumnIndex("RefNo")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }
        return 0;

    }

    public int restFreeIssueData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM finvDet WHERE " + DatabaseHelper.REFNO + " = '" + refno + "' AND " + DatabaseHelper.FINVDET_TYPE + " = 'FI'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(DatabaseHelper.TABLE_FINVDET, DatabaseHelper.REFNO + " = '" + refno + "' AND " + DatabaseHelper.FINVDET_TYPE + " = 'FI'", null);
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

    public ArrayList<InvDet> getSAForFreeIssueCalc(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvDet> list = new ArrayList<InvDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.FINVDET_TXN_TYPE + "='22' AND " + DatabaseHelper.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                InvDet ordDet = new InvDet();

                ordDet.setFINVDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ID)));
                ordDet.setFINVDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_AMT)));
                ordDet.setFINVDET_B_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_B_AMT)));
                ordDet.setFINVDET_B_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_B_SELL_PRICE)));
                ordDet.setFINVDET_BT_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_BT_SELL_PRICE)));
                ordDet.setFINVDET_DIS_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DIS_AMT)));
                ordDet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ITEM_CODE)));
                ordDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                ordDet.setFINVDET_PICE_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_PICE_QTY)));
                ordDet.setFINVDET_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TYPE)));
                ordDet.setFINVDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                ordDet.setFINVDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_SELL_PRICE)));
                ordDet.setFINVDET_SEQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_SEQNO)));
                ordDet.setFINVDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TAX_AMT)));
                ordDet.setFINVDET_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TAX_COM_CODE)));
                ordDet.setFINVDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_T_SELL_PRICE)));
                ordDet.setFINVDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                ordDet.setFINVDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_IS_ACTIVE)));
                ordDet.setFINVDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_TXN_TYPE)));
                ordDet.setFINVDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_PRIL_CODE)));
                ordDet.setFINVDET_DISVALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DISVALAMT)));
                ordDet.setFINVDET_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_PRICE)));

                list.add(ordDet);

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

    public void updateDiscount(InvDet invDet, double discount, String discType) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            OrderDisc orderDisc = new OrderDisc();
            orderDisc.setRefNo(invDet.getFINVDET_REFNO());
            orderDisc.setTxnDate(invDet.getFINVDET_TXN_DATE());
            orderDisc.setItemCode(invDet.getFINVDET_ITEM_CODE());
            orderDisc.setDisAmt(String.format("%.2f", discount));

            new OrderDiscController(context).UpdateOrderDiscount(orderDisc, invDet.getFINVDET_DISC_REF(), invDet.getFINVDET_DISVALAMT());
            String amt = String.format(String.format("%.2f", (Double.parseDouble(invDet.getFINVDET_AMT()) + Double.parseDouble(invDet.getFINVDET_DISVALAMT())) - discount));
            String updateQuery = "UPDATE finvdet SET SchDisPer='" + invDet.getFINVDET_SCHDISPER() + "', DisValAmt='" + String.format("%.2f", discount) + "', amt='" + amt + "' where Itemcode ='" + invDet.getFINVDET_ITEM_CODE() + "' AND types='SA'";
            dB.execSQL(updateQuery);

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }
    public void updateDiscountInvoice(InvDet ordDet, double discount, String discType, String debCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            OrderDisc orderDisc = new OrderDisc();
            orderDisc.setRefNo(ordDet.getFINVDET_REFNO());
            orderDisc.setTxnDate(ordDet.getFINVDET_TXN_DATE());
            orderDisc.setItemCode(ordDet.getFINVDET_ITEM_CODE());
            orderDisc.setDisAmt(String.format("%.2f", discount));
            String disFlag = "1";

            new OrderDiscController(context).UpdateOrderDiscount(orderDisc, ordDet.getFINVDET_DISC_REF(), ordDet.getFINVDET_SCHDISPER());
            String amt = String.format(String.format("%.2f", (Double.parseDouble(ordDet.getFINVDET_AMT()) + Double.parseDouble(ordDet.getFINVDET_DIS_AMT())) - discount));
            String forwardAmt[] = new TaxDetController(context).calculateTaxForwardFromDebTax(debCode, ordDet.getFINVDET_ITEM_CODE(),Double.parseDouble(ordDet.getFINVDET_AMT()));
            String forwardAmtWithoutDis = String.valueOf(Double.parseDouble(forwardAmt[0]) - discount);
            String reverseAmtWithoutDis = new TaxDetController(context).calculateReverseTaxFromDebTax(debCode, ordDet.getFINVDET_ITEM_CODE(),new BigDecimal(forwardAmtWithoutDis));
            String forwardSellPrice = String.valueOf((Double.parseDouble(reverseAmtWithoutDis)+ discount)/Double.parseDouble(ordDet.getFINVDET_QTY()));
            String updateQuery = "UPDATE FOrddet SET " +
                    DatabaseHelper.FORDDET_SCH_DISPER + "='" +
                    ordDet.getFINVDET_SCHDISPER() + "'," + DatabaseHelper.FORDDET_DIS_VAL_AMT + " ='" + String.format("%.2f", discount) + "'," + DatabaseHelper.FORDDET_AMT + "='" + reverseAmtWithoutDis + "'," + DatabaseHelper.FORDDET_DISFLAG + "='" + disFlag + "'," + DatabaseHelper.FORDDET_SELL_PRICE + "='" + forwardSellPrice + "'," + DatabaseHelper.FORDDET_BT_SELL_PRICE + "='" + forwardSellPrice + "'," + DatabaseHelper.FORDDET_B_AMT + "='" + reverseAmtWithoutDis + "'," + DatabaseHelper.FORDDET_DISCTYPE + "='" + discType + "' WHERE Itemcode ='" + ordDet.getFINVDET_ITEM_CODE() + "' AND type='SA'";
            dB.execSQL(updateQuery);

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }
    public void UpdateArrayDiscount(ArrayList<InvDet> orderList) {

        String DiscRef = orderList.get(0).getFINVDET_DISC_REF();
        String DiscPer = orderList.get(0).getFINVDET_SCHDISPER();

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            for (InvDet ordDet : orderList) {

                OrderDisc orderDisc = new OrderDisc();
                orderDisc.setRefNo(ordDet.getFINVDET_REFNO());
                orderDisc.setTxnDate(ordDet.getFINVDET_TXN_DATE());
                orderDisc.setRefNo1(ordDet.getFINVDET_DISC_REF());
                orderDisc.setItemCode(ordDet.getFINVDET_ITEM_CODE());
                orderDisc.setDisAmt(ordDet.getFINVDET_DIS_AMT());
                orderDisc.setDisPer(ordDet.getFINVDET_SCHDISPER());

                new OrderDiscController(context).UpdateOrderDiscount(orderDisc, DiscRef, DiscPer);
                String updateQuery = "UPDATE ftransodet SET SchDisPer='" + ordDet.getFINVDET_SCHDISPER() + "', DisValAmt='" + ordDet.getFINVDET_DIS_AMT() + "' where Itemcode ='" + ordDet.getFINVDET_ITEM_CODE() + "'";
                dB.execSQL(updateQuery);

            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
    }
//change by rashmi -2018-10-23 for mega heaters
    public void UpdateItemTaxInfo(ArrayList<InvDet> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double totTax = 0, totalAmt = 0;

        try {

            for (InvDet ordDet : list) {

				/* Calculate only for SA */
                if (ordDet.getFINVDET_TYPE().equals("SA")) {

                    //no need to mega heaters.get only total of tax detail amounts - commented 2018-10-23
                   // String sArray[] = new TaxDetDS(context).calculateTaxForward(ordDet.getFINVDET_ITEM_CODE(), Double.parseDouble(ordDet.getFINVDET_AMT()));

                    totTax += Double.parseDouble(ordDet.getFINVDET_TAX_AMT());
                    totalAmt += Double.parseDouble(ordDet.getFINVDET_AMT());

                  //  String updateQuery = "UPDATE finvdet SET taxamt='" + sArray[1] + "', amt='" + sArray[0] + "' WHERE Itemcode='" + ordDet.getFINVDET_ITEM_CODE() + "' AND refno='" + ordDet.getFINVDET_REFNO() + "' AND types='SA'";
                  //  dB.execSQL(updateQuery);
                }
            }
            /* Update Sales order Header TotalTax */
            dB.execSQL("UPDATE finvhed SET totaltax='" + totTax + "', totalamt='" + totalAmt + "' WHERE refno='" + list.get(0).getFINVDET_REFNO() + "'");

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

//    public String getLastSequnenceNo(String RefNo) {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//        try {
//            String selectQuery = "SELECT Max(seqno) as seqno FROM " + DatabaseHelper.TABLE_FTRANSODET + " WHERE " + DatabaseHelper.FTRANSODET_REFNO + "='" + RefNo + "'";
//            Cursor cursor = dB.rawQuery(selectQuery, null);
//            cursor.moveToFirst();
//
//            return (cursor.getInt(cursor.getColumnIndex("seqno")) + 1) + "";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "1";
//        } finally {
//            dB.close();
//        }
//    }


    public void mDeleteRecords(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(DatabaseHelper.TABLE_FINVDET, DatabaseHelper.REFNO + " ='" + RefNo + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }


    public void mDeleteProduct(String RefNo, String Itemcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(DatabaseHelper.TABLE_FINVDET, DatabaseHelper.REFNO + " ='" + RefNo + "' AND " + DatabaseHelper.FINVDET_ITEM_CODE + " ='" + Itemcode + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }


    public void mUpdateProduct(String RefNo, String Itemcode, String Qty) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FINVDET_QTY, Qty);
            values.put(DatabaseHelper.FINVDET_PICE_QTY, Qty);

            dB.update(DatabaseHelper.TABLE_FINVDET, values, DatabaseHelper.REFNO + " =? AND " + DatabaseHelper.FINVDET_ITEM_CODE + "=?" , new String[]{RefNo,Itemcode});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }




    public ArrayList<InvDet> getAllFreeIssue(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvDet> list = new ArrayList<InvDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "' AND " + DatabaseHelper.FINVDET_TYPE  + "='FI'"         ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {

            while (cursor.moveToNext()) {

                InvDet invDet = new InvDet();
                invDet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ITEM_CODE)));
                invDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                list.add(invDet);

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


    // Added By Yasith - 2019-01-29
    public ArrayList<InvDet> getAllItemsAddedInCurrentSale(String refNo)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvDet> list = new ArrayList<InvDet>();

        String selectQuery = "select ItemCode,Qty,Amt from " + DatabaseHelper.TABLE_FINVDET + " WHERE " + DatabaseHelper.REFNO + "='" + refNo + "' "         ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try
        {
            while (cursor.moveToNext())
            {
                InvDet invDet = new InvDet();
                invDet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_ITEM_CODE)));
                invDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                invDet.setFINVDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_AMT)));
                list.add(invDet);
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
}