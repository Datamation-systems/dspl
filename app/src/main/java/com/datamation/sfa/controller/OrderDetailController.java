package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;

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
                        + " = '" + ordDet.getORDDET_ID() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(dbHelper.ORDDET_AMT, ordDet.getORDDET_AMT());
                values.put(dbHelper.ORDDET_ITEM_CODE, ordDet.getORDDET_ITEMCODE());
                values.put(dbHelper.ORDDET_PRIL_CODE, ordDet.getORDDET_PRILCODE());
                values.put(dbHelper.ORDDET_QTY, ordDet.getORDDET_QTY());
                values.put(dbHelper.REFNO, ordDet.getORDDET_REFNO());
                values.put(dbHelper.ORDDET_PRICE, ordDet.getORDDET_PRICE());
                values.put(dbHelper.ORDDET_IS_ACTIVE, ordDet.getORDDET_IS_ACTIVE());
                values.put(dbHelper.ORDDET_ITEMNAME, ordDet.getORDDET_ITEMNAME());


                int cn = cursor.getCount();

                if (cn > 0) {
                    count = dB.update(dbHelper.TABLE_ORDER_DETAIL, values, dbHelper.ORDDET_ID + " =?", new String[] { String.valueOf(ordDet.getORDDET_ID()) });
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

                ordDet.setORDDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
                ordDet.setORDDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
                ordDet.setORDDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
                ordDet.setORDDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
                ordDet.setORDDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
                ordDet.setORDDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setORDDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
                ordDet.setORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));
                ordDet.setORDDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEMNAME)));

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

                ordDet.setORDDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
                ordDet.setORDDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
                ordDet.setORDDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
                ordDet.setORDDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEMNAME)));
                ordDet.setORDDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
                ordDet.setORDDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
                ordDet.setORDDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setORDDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
                ordDet.setORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));

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

                ordDet.setORDDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
                ordDet.setORDDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
                ordDet.setORDDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
                ordDet.setORDDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
                ordDet.setORDDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
                ordDet.setORDDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setORDDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
                ordDet.setORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));

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

                ordDet.setORDDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
                ordDet.setORDDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
                ordDet.setORDDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
                ordDet.setORDDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
                ordDet.setORDDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
                ordDet.setORDDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setORDDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
                ordDet.setORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));



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

                ordDet.setORDDET_ID(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ID)));
                ordDet.setORDDET_AMT(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_AMT)));
                ordDet.setORDDET_ITEMCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_ITEM_CODE)));
                ordDet.setORDDET_PRILCODE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRIL_CODE)));
                ordDet.setORDDET_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_QTY)));
                ordDet.setORDDET_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
                ordDet.setORDDET_PRICE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_PRICE)));
                ordDet.setORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(dbHelper.ORDDET_IS_ACTIVE)));



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
    public void mDeleteRecords(String RefNo) {

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


}
