package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.model.InvDet;
import com.datamation.sfa.model.Order;
import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.helpers.DatabaseHelper;

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

                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.ORDDET_ID
                        + " = '" + ordDet.getFORDERDET_ID() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(dbHelper.ORDDET_AMT, ordDet.getFORDERDET_AMT());
                values.put(dbHelper.ORDDET_ITEM_CODE, ordDet.getFORDERDET_ITEMCODE());
                values.put(dbHelper.ORDDET_PRIL_CODE, ordDet.getFORDERDET_PRILCODE());
                values.put(dbHelper.ORDDET_QTY, ordDet.getFORDERDET_QTY());
                values.put(dbHelper.REFNO, ordDet.getFORDERDET_REFNO());
                values.put(dbHelper.ORDDET_PRICE, ordDet.getFORDERDET_PRICE());
                values.put(dbHelper.ORDDET_IS_ACTIVE, ordDet.getFORDERDET_IS_ACTIVE());
                values.put(dbHelper.ORDDET_ITEM_NAME, ordDet.getFORDERDET_ITEMNAME());
                values.put(dbHelper.ORDDET_BAL_QTY, ordDet.getFORDERDET_BALQTY());
                values.put(dbHelper.ORDDET_BAMT, ordDet.getFORDERDET_BAMT());
                values.put(dbHelper.ORDDET_BDIS_AMT, ordDet.getFORDERDET_BDISAMT());
                values.put(dbHelper.ORDDET_BPDIS_AMT, ordDet.getFORDERDET_BPDISAMT());
                values.put(dbHelper.ORDDET_BTAX_AMT, ordDet.getFORDERDET_BTAXAMT());
                values.put(dbHelper.ORDDET_TAX_AMT, ordDet.getFORDERDET_TAXAMT());
                values.put(dbHelper.ORDDET_DIS_AMT, ordDet.getFORDERDET_DISAMT());
                values.put(dbHelper.ORDDET_SCHDISPER, ordDet.getFORDERDET_SCHDISPER());
                values.put(dbHelper.ORDDET_BRAND_DISPER, ordDet.getFORDERDET_BRAND_DISPER());
                values.put(dbHelper.ORDDET_BRAND_DISC, ordDet.getFORDERDET_BRAND_DISC());
                values.put(dbHelper.ORDDET_COMP_DISC, ordDet.getFORDERDET_COMP_DISC());
                values.put(dbHelper.ORDDET_COST_PRICE, ordDet.getFORDERDET_COSTPRICE());
                values.put(dbHelper.ORDDET_PIECE_QTY, ordDet.getFORDERDET_PICE_QTY());
                values.put(dbHelper.ORDDET_SELL_PRICE, ordDet.getFORDERDET_SELLPRICE());
                values.put(dbHelper.ORDDET_BSELL_PRICE, ordDet.getFORDERDET_BSELLPRICE());
                values.put(dbHelper.ORDDET_SEQ_NO, ordDet.getFORDERDET_SEQNO());
                values.put(dbHelper.ORDDET_TAX_COM_CODE, ordDet.getFORDERDET_TAXCOMCODE());
                values.put(dbHelper.ORDDET_TSELL_PRICE, ordDet.getFORDERDET_TSELLPRICE());
                values.put(dbHelper.ORDDET_BTSELL_PRICE, ordDet.getFORDERDET_BTSELLPRICE());
                values.put(dbHelper.ORDDET_TXN_TYPE, ordDet.getFORDERDET_TXNTYPE());
                values.put(dbHelper.ORDDET_LOC_CODE, ordDet.getFORDERDET_LOCCODE());
                values.put(dbHelper.ORDDET_TXN_DATE, ordDet.getFORDERDET_TXNDATE());
                values.put(dbHelper.ORDDET_RECORD_ID, ordDet.getFORDERDET_RECORDID());
                values.put(dbHelper.ORDDET_PDIS_AMT, ordDet.getFORDERDET_PDISAMT());
                values.put(dbHelper.ORDDET_IS_SYNCED, ordDet.getFORDERDET_IS_SYNCED());
                values.put(dbHelper.ORDDET_QOH, ordDet.getFORDERDET_QOH());
                values.put(dbHelper.ORDDET_TYPE, ordDet.getFORDERDET_TYPE());
                values.put(dbHelper.ORDDET_SCHDISC, ordDet.getFORDERDET_SCHDISC());
                values.put(dbHelper.ORDDET_DIS_TYPE, ordDet.getFORDERDET_DISCTYPE());
                values.put(dbHelper.ORDDET_QTY_SLAB_DISC, ordDet.getFORDERDET_QTY_SLAB_DISC());
                values.put(dbHelper.ORDDET_ORG_PRICE, ordDet.getFORDERDET_ORG_PRICE());
                values.put(dbHelper.ORDDET_DIS_FLAG, ordDet.getFORDERDET_DISFLAG());

                int cn = cursor.getCount();

                if (cn > 0) {
                    count = dB.update(dbHelper.TABLE_ORDER_DETAIL, values, dbHelper.ORDDET_ID + " =?", new String[] { String.valueOf(ordDet.getFORDERDET_ID()) });
                } else {
                    count = (int) dB.insert(dbHelper.TABLE_ORDER_DETAIL, null, values);
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
            String selectQuery = "select hed.DebCode, det.RefNo, hed.TotalAmt,det.Qty from finvHed hed, finvDet det" +
                    //			" fddbnote fddb where hed.refno = det.refno and det.FPRECDET_REFNO1 = fddb.refno and hed.txndate = '2019-04-12'";
                    "  where hed.refno = det.refno and hed.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                OrderDetail recDet = new OrderDetail();

//
                recDet.setFORDERDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                recDet.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_AMT)));
                recDet.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVDET_QTY)));
                recDet.setFORDERDET_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
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

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(dbHelper.TABLE_ORDER_DETAIL, dbHelper.REFNO + " ='" + refno + "'", null);
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

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(dbHelper.TABLE_ORDER_DETAIL, dbHelper.REFNO + " = '" + refno + "'", null);
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

        String selectQuery = "select * from " + dbHelper.TABLE_ORDER_DETAIL + " WHERE "
                +
                dbHelper.REFNO + "='" + refno + "' and "+dbHelper.ORDDET_IS_ACTIVE +" = '1'";

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

        String selectQuery = "select * from " + dbHelper.TABLE_ORDER_DETAIL + " WHERE "
               + dbHelper.REFNO + "='" + refno + "'";

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

            String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(dbHelper.ORDDET_IS_ACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(dbHelper.TABLE_ORDER_DETAIL, values, dbHelper.REFNO + " =?", new String[] { String.valueOf(refno) });
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

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.REFNO + "='" + Refno + "' AND "+dbHelper.ORDDET_ITEM_CODE + "='"+itemcode+"'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_ORDER_DETAIL, dbHelper.REFNO + "='" + Refno + "' AND "+dbHelper.ORDDET_ITEM_CODE + "='"+itemcode+"'", null);
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

        String selectQuery = "select * from " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.REFNO
                + "='" + refno + "'";

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
    public ArrayList<OrderDetail> getAllUnSync(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.REFNO
                + "='" + refno + "'";

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
            dB.delete(DatabaseHelper.TABLE_ORDER_DETAIL, DatabaseHelper.REFNO + " ='" + RefNo + "'" + " AND " + DatabaseHelper.ORDDET_ITEM_CODE + " ='" + itemCode + "'", null);

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
            dB.delete(DatabaseHelper.TABLE_ORDER_DETAIL, DatabaseHelper.REFNO + " ='" + RefNo + "'", null);

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
            String selectQuery = "SELECT Max(seqno) as seqno FROM " + dbHelper.TABLE_ORDER_DETAIL + " WHERE " + dbHelper.REFNO + "='" + RefNo + "'";
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
            String selectQuery = "SELECT count(RefNo) as RefNo FROM " + DatabaseHelper.TABLE_ORDER_DETAIL + " WHERE  " + DatabaseHelper.REFNO + "='" + refNo + "'";
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

        String selectQuery = "select ItemCode,Qty,Amt from " + DatabaseHelper.TABLE_ORDER_DETAIL + " WHERE " + DatabaseHelper.REFNO + "='" + refNo + "' "         ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try
        {
            while (cursor.moveToNext())
            {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setFORDERDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_ITEM_CODE)));
                orderDetail.setFORDERDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_QTY)));
                orderDetail.setFORDERDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ORDDET_AMT)));
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
}
