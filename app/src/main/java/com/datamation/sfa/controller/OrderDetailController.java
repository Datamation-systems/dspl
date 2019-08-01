package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.model.FInvRDet;
import com.datamation.sfa.model.InvDet;
import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.OrderDisc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rashmi
 */

public class OrderDetailController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "Order Detail";

    public OrderDetailController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {

        dB = dbHelper.getWritableDatabase();

    }

    @SuppressWarnings("static-access")
    public int createOrUpdateOrdDet(ArrayList<OrderDetail> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (OrderDetail ordDet : list) {

                Cursor cursor = null;
                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FORDDET + " WHERE " + dbHelper.FORDDET_ID
                        + " = '" + ordDet.getFORDERDET_ID() + "'";

                cursor = dB.rawQuery(selectQuery, null);

//                values.put(dbHelper.ORDDET_AMT, ordDet.getFORDERDET_AMT());
//                values.put(dbHelper.ORDDET_ITEM_CODE, ordDet.getFORDERDET_ITEMCODE());
//                values.put(dbHelper.ORDDET_PRIL_CODE, ordDet.getFORDERDET_PRILCODE());
//                values.put(dbHelper.ORDDET_QTY, ordDet.getFORDERDET_QTY());
//                values.put(dbHelper.REFNO, ordDet.getFORDERDET_REFNO());
//                values.put(dbHelper.ORDDET_PRICE, ordDet.getFORDERDET_PRICE());
//                values.put(dbHelper.ORDDET_IS_ACTIVE, ordDet.getFORDERDET_IS_ACTIVE());
//                values.put(dbHelper.ORDDET_ITEM_NAME, ordDet.getFORDERDET_ITEMNAME());
//                values.put(dbHelper.ORDDET_BAL_QTY, ordDet.getFORDERDET_BALQTY());
//                values.put(dbHelper.ORDDET_BAMT, ordDet.getFORDERDET_BAMT());
//                values.put(dbHelper.ORDDET_BDIS_AMT, ordDet.getFORDERDET_BDISAMT());
//                values.put(dbHelper.ORDDET_BPDIS_AMT, ordDet.getFORDERDET_BPDISAMT());
//                values.put(dbHelper.ORDDET_BTAX_AMT, ordDet.getFORDERDET_BTAXAMT());
//                values.put(dbHelper.ORDDET_TAX_AMT, ordDet.getFORDERDET_TAXAMT());
//                values.put(dbHelper.ORDDET_DIS_AMT, ordDet.getFORDERDET_DISAMT());
//                values.put(dbHelper.ORDDET_SCHDISPER, ordDet.getFORDERDET_SCHDISPER());
//                values.put(dbHelper.ORDDET_BRAND_DISPER, ordDet.getFORDERDET_BRAND_DISPER());
//                values.put(dbHelper.ORDDET_BRAND_DISC, ordDet.getFORDERDET_BRAND_DISC());
//                values.put(dbHelper.ORDDET_COMP_DISC, ordDet.getFORDERDET_COMP_DISC());
//                values.put(dbHelper.ORDDET_COST_PRICE, ordDet.getFORDERDET_COSTPRICE());
//                values.put(dbHelper.ORDDET_PIECE_QTY, ordDet.getFORDERDET_PICE_QTY());
//                values.put(dbHelper.ORDDET_SELL_PRICE, ordDet.getFORDERDET_SELLPRICE());
//                values.put(dbHelper.ORDDET_BSELL_PRICE, ordDet.getFORDERDET_BSELLPRICE());
//                values.put(dbHelper.ORDDET_SEQ_NO, ordDet.getFORDERDET_SEQNO());
//                values.put(dbHelper.ORDDET_TAX_COM_CODE, ordDet.getFORDERDET_TAXCOMCODE());
//                values.put(dbHelper.ORDDET_TSELL_PRICE, ordDet.getFORDERDET_TSELLPRICE());
//                values.put(dbHelper.ORDDET_BTSELL_PRICE, ordDet.getFORDERDET_BTSELLPRICE());
//                values.put(dbHelper.ORDDET_TXN_TYPE, ordDet.getFORDERDET_TXNTYPE());
//                values.put(dbHelper.ORDDET_LOC_CODE, ordDet.getFORDERDET_LOCCODE());
//                values.put(dbHelper.ORDDET_TXN_DATE, ordDet.getFORDERDET_TXNDATE());
//                values.put(dbHelper.ORDDET_RECORD_ID, ordDet.getFORDERDET_RECORDID());
//                values.put(dbHelper.ORDDET_PDIS_AMT, ordDet.getFORDERDET_PDISAMT());
//                values.put(dbHelper.ORDDET_IS_SYNCED, ordDet.getFORDERDET_IS_SYNCED());
//                values.put(dbHelper.ORDDET_QOH, ordDet.getFORDERDET_QOH());
//                values.put(dbHelper.ORDDET_TYPE, ordDet.getFORDERDET_TYPE());
//                values.put(dbHelper.ORDDET_SCHDISC, ordDet.getFORDERDET_SCHDISC());
//                values.put(dbHelper.ORDDET_DIS_TYPE, ordDet.getFORDERDET_DISCTYPE());
//                values.put(dbHelper.ORDDET_QTY_SLAB_DISC, ordDet.getFORDERDET_QTY_SLAB_DISC());
//                values.put(dbHelper.ORDDET_ORG_PRICE, ordDet.getFORDERDET_ORG_PRICE());
//                values.put(dbHelper.ORDDET_DIS_FLAG, ordDet.getFORDERDET_DISFLAG());
//
                // commented due to table changed

                values.put(dbHelper.FORDDET_AMT, ordDet.getFORDERDET_AMT());
                values.put(dbHelper.FORDDET_ITEM_CODE, ordDet.getFORDERDET_ITEMCODE());
                values.put(dbHelper.FORDDET_PRIL_CODE, ordDet.getFORDERDET_PRILCODE());
                values.put(dbHelper.FORDDET_QTY, ordDet.getFORDERDET_QTY());
                values.put(dbHelper.REFNO, ordDet.getFORDERDET_REFNO());
                values.put(dbHelper.FORDDET_SELL_PRICE, ordDet.getFORDERDET_PRICE());
                values.put(dbHelper.FORDDET_IS_ACTIVE, ordDet.getFORDERDET_IS_ACTIVE());
                values.put(dbHelper.FORDDET_ITEMNAME, ordDet.getFORDERDET_ITEMNAME());
                values.put(dbHelper.FORDDET_BAL_QTY, ordDet.getFORDERDET_BALQTY());
                values.put(dbHelper.FORDDET_B_AMT, ordDet.getFORDERDET_BAMT());
                values.put(dbHelper.FORDDET_B_DIS_AMT, ordDet.getFORDERDET_BDISAMT());
                values.put(dbHelper.FORDDET_BP_DIS_AMT, ordDet.getFORDERDET_BPDISAMT());
                values.put(dbHelper.FORDDET_BT_TAX_AMT, ordDet.getFORDERDET_BTAXAMT());
                values.put(dbHelper.FORDDET_TAX_AMT, ordDet.getFORDERDET_TAXAMT());
                values.put(dbHelper.FORDDET_DIS_AMT, ordDet.getFORDERDET_DISAMT());
                values.put(dbHelper.FORDDET_DIS_PER, ordDet.getFORDERDET_SCHDISPER());
                values.put(dbHelper.FORDDET_PICE_QTY, ordDet.getFORDERDET_PICE_QTY());
                values.put(dbHelper.FORDDET_SELL_PRICE, ordDet.getFORDERDET_SELLPRICE());
                values.put(dbHelper.FORDDET_B_SELL_PRICE, ordDet.getFORDERDET_BSELLPRICE());
                values.put(dbHelper.FORDDET_SEQNO, ordDet.getFORDERDET_SEQNO());
                values.put(dbHelper.FORDDET_TAX_COM_CODE, ordDet.getFORDERDET_TAXCOMCODE());
                values.put(dbHelper.FORDDET_T_SELL_PRICE, ordDet.getFORDERDET_TSELLPRICE());
                values.put(dbHelper.FORDDET_BT_SELL_PRICE, ordDet.getFORDERDET_BTSELLPRICE());
                values.put(dbHelper.FORDDET_TXN_TYPE, ordDet.getFORDERDET_TXNTYPE());
                values.put(dbHelper.TXNDATE, ordDet.getFORDERDET_TXNDATE());
                values.put(dbHelper.FORDDET_RECORD_ID, ordDet.getFORDERDET_RECORDID());
                values.put(dbHelper.FORDDET_P_DIS_AMT, ordDet.getFORDERDET_PDISAMT());
                values.put(dbHelper.FORDDET_TYPE, ordDet.getFORDERDET_TYPE());

                int cn = cursor.getCount();

                if (cn > 0) {
                    count = dB.update(dbHelper.TABLE_FORDDET, values, dbHelper.FORDDET_ID + " =?", new String[] { String.valueOf(ordDet.getFORDERDET_ID()) });
                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FORDDET, null, values);
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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public ArrayList<OrderDetail> getTodayOrders() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        try {
            String selectQuery = "select det.ItemCode, det.Amt,det.Qty from OrderDetail det" +
                    //			" fddbnote fddb where hed.refno = det.refno and det.FPRECDET_REFNO1 = fddb.refno and hed.txndate = '2019-04-12'";
                    "  where det.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                OrderDetail recDet = new OrderDetail();

//
                recDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                recDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_AMT)));
                recDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
               // recDet.setFORDERDET_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
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
    public int getItemCountForUpload(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(oh.RefNo) as RefNo FROM " + DatabaseHelper.TABLE_ORDER_DETAIL + " od, OrderHeader oh WHERE  od." + DatabaseHelper.REFNO + "='" + refNo + "' and oh.isSynced = '0' and od.refNo = oh.refNo";
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
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public int getItemCountForSave(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(oh.RefNo) as RefNo FROM " + DatabaseHelper.TABLE_ORDER_DETAIL + " od, OrderHeader oh WHERE  od." + DatabaseHelper.REFNO + "='" + refNo + "' and od."+DatabaseHelper.ORDDET_IS_ACTIVE + " = '1' and oh.isSynced = '0' and od.refNo = oh.refNo";
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
    // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

    @SuppressWarnings("static-access")
    public int restData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FORDDET + " WHERE " + dbHelper.REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(dbHelper.TABLE_FORDDET, dbHelper.REFNO + " ='" + refno + "'", null);
                Log.v("Success Stauts", count + "");
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

    @SuppressWarnings("static-access")
    public int restFreeIssueData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FORDDET + dbHelper.REFNO + " = '" + refno + "' AND " + dbHelper.FORDDET_TYPE + " = 'FI'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(dbHelper.TABLE_FORDDET, dbHelper.REFNO + " = '" + refno + "' AND " + dbHelper.FORDDET_TYPE + " = 'FI'", null);
                Log.v("Success Stauts", count + "");
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

	/*-*-**-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<OrderDetail> getAllOrderDetails(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + dbHelper.TABLE_FORDDET + " WHERE "
                + dbHelper.REFNO + "='" + refno + "' and "
                + dbHelper.FORDDET_TYPE + "='" + "SA" + "' and "
                + dbHelper.FORDDET_IS_ACTIVE +" = '1'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

//                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
//                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
//                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
//                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
//                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
//                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
//                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
//                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));
//                ordDet.setFORDERDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_NAME)));

                //commented due to table changed

                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ID)));
                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_AMT)));
                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ITEM_CODE)));
                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_PRIL_CODE)));
                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_QTY)));
                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_SELL_PRICE)));
                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_IS_ACTIVE)));
                ordDet.setFORDERDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ITEMNAME)));
                ordDet.setFORDERDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_TAX_COM_CODE)));

                list.add(ordDet);

            }
            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

    public ArrayList<OrderDetail> getAllOrderDetailsForTaxUpdate(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + dbHelper.TABLE_FORDDET + " WHERE "
                + dbHelper.REFNO + "='" + refno + "' and "
                + dbHelper.FORDDET_TYPE + "='" + "SA" + "' and "
                + dbHelper.FORDDET_IS_ACTIVE +" = '0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

//                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
//                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
//                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
//                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
//                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
//                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
//                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
//                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));
//                ordDet.setFORDERDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_NAME)));

                //commented due to table changed

                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ID)));
                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_AMT)));
                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ITEM_CODE)));
                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_PRIL_CODE)));
                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_QTY)));
                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_SELL_PRICE)));
                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_IS_ACTIVE)));
                ordDet.setFORDERDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ITEMNAME)));
                ordDet.setFORDERDET_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_TAX_COM_CODE)));
                ordDet.setFORDERDET_TYPE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_TYPE)));

                list.add(ordDet);

            }
            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

    public ArrayList<OrderDetail> getAllOrderDetailOthers(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();
        //
        // String selectQuery = "select * from "+dbHelper.TABLE_ORDER_DETAIL
        // +" WHERE "+dbHelper.FORDDET_TXN_TYPE+"!='22' AND "+dbHelper.REFNO+"='"+refno+"'";

        String selectQuery = "select * from " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " +
                 dbHelper.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));
                ordDet.setFORDERDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_NAME)));


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

    public ArrayList<OrderDetail> getSAForFreeIssueCalc(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + dbHelper.TABLE_FORDDET + " WHERE "
               + dbHelper.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ID)));
                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_AMT)));
                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ITEM_CODE)));
                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_PRIL_CODE)));
                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_QTY)));
                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_SELL_PRICE)));
                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_IS_ACTIVE)));

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

    public ArrayList<OrderDetail> getTodayOrderDets(String refno) {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

       // String selectQuery = "select * from " + dbHelper.TABLE_ORDER_DETAIL + " WHERE "
        String selectQuery = "select * from FOrddet WHERE "
                + dbHelper.REFNO + "='" + refno + "' and  txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

//                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
//                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
//                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
//                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
//                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
//                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
//                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
//                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));

                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ID)));
                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_AMT)));
                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ITEM_CODE)));
                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_PRIL_CODE)));
                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_QTY)));
                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_SELL_PRICE)));
                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_IS_ACTIVE)));

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
    @SuppressWarnings("static-access")
    public int InactiveStatusUpdate(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FORDDET + " WHERE " + dbHelper.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(dbHelper.FORDDET_IS_ACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(dbHelper.TABLE_FORDDET, values, dbHelper.REFNO + " =?", new String[] { String.valueOf(refno) });
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

    @SuppressWarnings("static-access")
    public int deleteOrdDetByID(String id) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.ORDDET_ID + "='" + id + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_ORDER_DETAIL, dbHelper.ORDDET_ID + "='" + id + "'", null);
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


    @SuppressWarnings("static-access")
    public int deleteOrdDetByItemCode(String itemcode, String Refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FORDDET + " WHERE " + dbHelper.REFNO + "='" + Refno + "' AND "+dbHelper.ORDDET_ITEM_CODE + "='"+itemcode+"'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FORDDET, dbHelper.REFNO + "='" + Refno + "' AND "+dbHelper.ORDDET_ITEM_CODE + "='"+itemcode+"'", null);
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

    public String getGrossValue(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        @SuppressWarnings("static-access")
        String selectQuery = "SELECT SUM(" +
                dbHelper.ORDDET_QTY + " * " + dbHelper.ORDDET_PRICE + ") AS 'Gross Value'  FROM " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.REFNO + "='" + refno + "'";
        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("Gross Value")) != null)
                    return cursor.getString(cursor.getColumnIndex("Gross Value"));
                else
                    return "0.00";
            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return "0.00";
    }

    public ArrayList<OrderDetail> getAllActives(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + dbHelper.TABLE_FORDDET + " WHERE " + dbHelper.REFNO
                + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

//                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
//                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
//                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
//                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
//                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
//                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
//                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
//                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));

                //commented due to table changed

                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ID)));
                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_AMT)));
                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ITEM_CODE)));
                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_PRIL_CODE)));
                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_QTY)));
                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_SELL_PRICE)));
                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_IS_ACTIVE)));

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

    public ArrayList<OrderDetail> getAllActiveOrders() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.ORDDET_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));

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

    public OrderDetail getActiveRefNo() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        OrderDetail ordDet = new OrderDetail();

        String selectQuery = "select * from " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.ORDDET_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {



                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFORDERDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));

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

    public boolean isAnyActiveOrders()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from " + dbHelper.TABLE_FORDDET + " WHERE " + dbHelper.FORDDET_IS_ACTIVE + "='" + "1" + "'";

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

    public ArrayList<OrderDetail> getAllUnSync(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + dbHelper.TABLE_FORDDET + " WHERE " + dbHelper.REFNO
                + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

                ordDet.setFORDERDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ID)));
                ordDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_AMT)));
                ordDet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_ITEM_CODE)));
                ordDet.setFORDERDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_PRIL_CODE)));
                ordDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_QTY)));
                ordDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setFORDERDET_SELLPRICE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_SELL_PRICE)));
                ordDet.setFORDERDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.FORDDET_IS_ACTIVE)));



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

    @SuppressWarnings("static-access")
//    public int DeleteOldOrders(String DateFrom, String DateTo) {
//
//        int count = 0;
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//        Cursor cursor = null;
//
//        try {
//
//            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " +
//                    dbHelper.ORDDET_TXN_DATE + " BETWEEN '"+ DateFrom + "' AND '" + DateTo + "'";
//            cursor = dB.rawQuery(selectQuery, null);
//            int cn = cursor.getCount();
//
//            if (cn > 0) {
//                int success = dB.delete(dbHelper.TABLE_ORDER_DETAIL, dbHelper.FORDDET_TXN_DATE + " BETWEEN '"+ DateFrom + "' AND '" + DateTo + "'", null);
//                Log.v("Success", success + "");
//            }
//
//        } catch (Exception e) {
//            Log.v(TAG + " Exception", e.toString());
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//        return count;
//
//    }

    public int OrdDetByRefno(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ORDER_DETAIL + " WHERE " + DatabaseHelper.REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_ORDER_DETAIL, DatabaseHelper.REFNO + "='" + RefNo + "'", null);
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
    public void mDeleteRecords(String RefNo, String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(DatabaseHelper.TABLE_FORDDET, DatabaseHelper.REFNO + " ='" + RefNo + "'" + " AND " + DatabaseHelper.FORDDET_ITEM_CODE + " ='" + itemCode + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }

    public void deleteRecords(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(DatabaseHelper.TABLE_FORDDET, DatabaseHelper.REFNO + " ='" + RefNo + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }

    public String getLastSequnenceNo(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT Max(seqno) as seqno FROM " + dbHelper.TABLE_FORDDET + " WHERE " + dbHelper.REFNO + "='" + RefNo + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);
            cursor.moveToFirst();

            return (cursor.getInt(cursor.getColumnIndex("seqno")) + 1) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        } finally {
            dB.close();
        }
    }

    public int getItemCount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(RefNo) as RefNo FROM " + DatabaseHelper.TABLE_FORDDET + " WHERE  " + DatabaseHelper.REFNO + "='" + refNo + "'";
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

    public ArrayList<OrderDetail> getAllItemsAddedInCurrentSale(String refNo)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select ItemCode,Qty,Amt from " + DatabaseHelper.TABLE_FORDDET + " WHERE " + DatabaseHelper.REFNO + "='" + refNo + "' "         ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try
        {
            while (cursor.moveToNext())
            {
                OrderDetail orderDetail = new OrderDetail();
//                orderDetail.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_ITEM_CODE)));
//                orderDetail.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_QTY)));
//                orderDetail.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_AMT)));

                // commented due to table changed
                orderDetail.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_ITEM_CODE)));
                orderDetail.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_QTY)));
                orderDetail.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_AMT)));
                list.add(orderDetail);
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

    public ArrayList<OrderDetail> getAllItemsForPrint(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select Itemcode,Qty,Amt,TSellPrice,Types from " + DatabaseHelper.TABLE_FORDDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";

        // commented due to table changed
        //String selectQuery = "select Itemcode,Qty,Amt,TSellPrice,Type from " + DatabaseHelper.TABLE_ORDER_DETAIL + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";

        try {

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                OrderDetail order = new OrderDetail();

//                order.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_AMT)));
//                order.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_ITEM_CODE)));
//                order.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_QTY)));
//                order.setFORDERDET_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_TYPE)));
//                order.setFORDERDET_TSELLPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_TSELL_PRICE)));

                // commented due to changed table

                order.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_AMT)));
                order.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_ITEM_CODE)));
                order.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_QTY)));
                order.setFORDERDET_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_TYPE)));
                order.setFORDERDET_TSELLPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_T_SELL_PRICE)));

                //order.setFINVDET_DISVALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_DISVALAMT)));
                order.setFORDERDET_REFNO(refno);

                list.add(order);
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

    public FInvRDet getActiveReturnDet() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        FInvRDet invrDet = new FInvRDet();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + dbHelper.TABLE_FINVRDET + " Where " + dbHelper.FINVRDET_IS_ACTIVE
                + "='1' ";

//        String selectQuery = "select * from " + dbHelper.TABLE_FINVRDET + " Where " + dbHelper.FINVRDET_IS_ACTIVE
//                + "='1' and " + dbHelper.FINVRHED_IS_SYNCED + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            invrDet.setFINVRDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_ID)));
            invrDet.setFINVRDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
            invrDet.setFINVRDET_RETURN_REASON_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_REASON_CODE)));
            invrDet.setFINVRDET_RETURN_TYPE(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_RETURN_TYPE)));
            invrDet.setFINVRDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_QTY)));
            invrDet.setFINVRDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.FINVRDET_AMT)));
        }

        return invrDet;
    }

    public void UpdateItemTaxInfoWithDiscount(ArrayList<OrderDetail> list, String debtorCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double totTax = 0, totalAmt = 0, sellPrice = 0, tSellPrice = 0, totDisc=0, disR=0;

        try {

            for (OrderDetail soDet : list) {

                /* Calculate only for MR or UR */
                if (soDet.getFORDERDET_TYPE().equals("SA")) {

                    String sArray[] = new TaxDetController(context).calculateTaxForwardFromDebTax(debtorCode, soDet.getFORDERDET_ITEMCODE(), Double.parseDouble(soDet.getFORDERDET_AMT()));

                    sellPrice = Double.parseDouble(sArray[0])/Double.parseDouble(soDet.getFORDERDET_QTY());
                    if (Double.parseDouble(soDet.getFORDERDET_SCHDISC())>0.0)
                    {
                        tSellPrice = (Double.parseDouble(sArray[0])+ Double.parseDouble(soDet.getFORDERDET_SCHDISC()))/Double.parseDouble(soDet.getFORDERDET_QTY());
                        disR = ((Double.parseDouble(soDet.getFORDERDET_SCHDISC()))/(Double.parseDouble(sArray[0])+ Double.parseDouble(soDet.getFORDERDET_SCHDISC())))* 100;
                        totDisc += Double.parseDouble(soDet.getFORDERDET_SCHDISC());
                    }
                    else
                    {
                        tSellPrice = Double.parseDouble(sArray[0])/Double.parseDouble(soDet.getFORDERDET_QTY());
                    }

                    totTax += Double.parseDouble(sArray[1]);
                    totalAmt += Double.parseDouble(sArray[0]);

                    String updateQuery = "UPDATE FOrddet SET TaxAmt='" + sArray[1] + "', Amt='" + sArray[0] + "', BAmt='" + sArray[0] + "', DisAmt='" + String.valueOf(disR) + "', TSellPrice='" + tSellPrice + "', BTSellPrice ='" + tSellPrice + "' where ItemCode ='" + soDet.getFORDERDET_ITEMCODE() + "' AND refno='" + soDet.getFORDERDET_REFNO() + "' AND Type!='FI'";
                    dB.execSQL(updateQuery);
                }
            }
            /* Update sales return Header TotalTax */
            dB.execSQL("UPDATE FOrdHed SET TotalTax='" + totTax + "',TotalDis='" + totDisc + "',TotalAmt='" + totalAmt + "' WHERE refno='" + list.get(0).getFORDERDET_REFNO() + "'");

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

    public void updateDiscountSO(OrderDetail ordDet, double discount, String discType, String debCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            OrderDisc orderDisc = new OrderDisc();
            orderDisc.setRefNo(ordDet.getFORDERDET_REFNO());
            orderDisc.setTxnDate(ordDet.getFORDERDET_TXNDATE());
            orderDisc.setItemCode(ordDet.getFORDERDET_ITEMCODE());
            orderDisc.setDisAmt(String.format("%.2f", discount));
            String disFlag = "1";

            new OrderDiscController(context).UpdateOrderDiscount(orderDisc, ordDet.getFORDERDET_DISC_REF(), ordDet.getFORDERDET_SCHDISPER());
            String amt = String.format(String.format("%.2f", (Double.parseDouble(ordDet.getFORDERDET_AMT()) + Double.parseDouble(ordDet.getFORDERDET_SCHDISC())) - discount));
            String forwardAmt[] = new TaxDetController(context).calculateTaxForwardFromDebTax(debCode, ordDet.getFORDERDET_ITEMCODE(),Double.parseDouble(ordDet.getFORDERDET_AMT()));
            String forwardAmtWithoutDis = String.valueOf(Double.parseDouble(forwardAmt[0]) - discount);
            String reverseAmtWithoutDis = new TaxDetController(context).calculateReverseTaxFromDebTax(debCode, ordDet.getFORDERDET_ITEMCODE(),new BigDecimal(forwardAmtWithoutDis));
            String forwardSellPrice = String.valueOf((Double.parseDouble(reverseAmtWithoutDis)+ discount)/Double.parseDouble(ordDet.getFORDERDET_QTY()));
            String updateQuery = "UPDATE FOrddet SET " +
                    DatabaseHelper.FORDDET_SCH_DISPER + "='" +
                    ordDet.getFORDERDET_SCHDISPER() + "'," + DatabaseHelper.FORDDET_DIS_VAL_AMT + " ='" + String.format("%.2f", discount) + "'," + DatabaseHelper.FORDDET_AMT + "='" + reverseAmtWithoutDis + "'," + DatabaseHelper.FORDDET_DISFLAG + "='" + disFlag + "'," + DatabaseHelper.FORDDET_SELL_PRICE + "='" + forwardSellPrice + "'," + DatabaseHelper.FORDDET_BT_SELL_PRICE + "='" + forwardSellPrice + "'," + DatabaseHelper.FORDDET_B_AMT + "='" + reverseAmtWithoutDis + "'," + DatabaseHelper.FORDDET_DISCTYPE + "='" + discType + "' WHERE Itemcode ='" + ordDet.getFORDERDET_ITEMCODE() + "' AND type='SA'";
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

    public void updateDiscount(OrderDetail ordDet, double discount, String discType) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            OrderDisc orderDisc = new OrderDisc();
            orderDisc.setRefNo(ordDet.getFORDERDET_REFNO());
            orderDisc.setTxnDate(ordDet.getFORDERDET_TXNDATE());
            orderDisc.setItemCode(ordDet.getFORDERDET_ITEMCODE());
            orderDisc.setDisAmt(String.format("%.2f", discount));

            new OrderDiscController(context).UpdateOrderDiscount(orderDisc, ordDet.getFORDERDET_DISC_REF(), ordDet.getFORDERDET_SCHDISPER());
            String amt = String.format(String.format("%.2f", (Double.parseDouble(ordDet.getFORDERDET_AMT()) + Double.parseDouble(ordDet.getFORDERDET_SCHDISC())) - discount));

            String updateQuery = "UPDATE FTranSODet SET " +
                    DatabaseHelper.FORDDET_SCH_DISPER + "='" + ordDet.getFORDERDET_SCHDISPER() + "',"
                    + DatabaseHelper.FORDDET_DIS_VAL_AMT + " ='" + String.format("%.2f", discount) + "',"
                    + DatabaseHelper.FORDDET_AMT + "='" + amt + "',"
                    + DatabaseHelper.FORDDET_B_AMT + "='" + amt + "',"
                    + DatabaseHelper.FORDDET_DISCTYPE + "='" + discType + "' WHERE Itemcode ='" + ordDet.getFORDERDET_ITEMCODE() + "' AND type='SA'";
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

    public ArrayList<OrderDetail> getAllFreeIssue(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FORDDET + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "' AND " + DatabaseHelper.FORDDET_TYPE  + "='FI'" ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {

            while (cursor.moveToNext()) {

                OrderDetail tranSODet=new OrderDetail();
                tranSODet.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_ITEM_CODE)));
                tranSODet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FORDDET_QTY)));
                list.add(tranSODet);
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
}
