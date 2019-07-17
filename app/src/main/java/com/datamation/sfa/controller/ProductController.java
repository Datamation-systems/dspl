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
import com.datamation.sfa.model.Product;

import java.util.ArrayList;

/**
 * Created by Himas on 7/25/2017.
 */

public class ProductController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;

    public ProductController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }


    public void insertOrUpdateProducts(ArrayList<Product> list) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_FPRODUCT + " (itemcode,itemname,price,qoh,qty,minPrice,maxPrice,ChangedPrice,TxnType) VALUES (?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Product items : list) {

                stmt.bindString(1, items.getFPRODUCT_ITEMCODE());
                stmt.bindString(2, items.getFPRODUCT_ITEMNAME());
                stmt.bindString(3, items.getFPRODUCT_PRICE());
                stmt.bindString(4, items.getFPRODUCT_QOH());
                stmt.bindString(5, items.getFPRODUCT_QTY());
                stmt.bindString(6, items.getFPRODUCT_MIN_PRICE());
                stmt.bindString(7, items.getFPRODUCT_MAX_PRICE());
                stmt.bindString(8, items.getFPRODUCT_CHANGED_PRICE());
                stmt.bindString(9, items.getFPRODUCT_TXN_TYPE());

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


    public boolean tableHasRecords() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT, null);

            if (cursor.getCount() > 0)
                result = true;
            else
                result = false;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();

        }

        return result;

    }

    public ArrayList<Product> getAllItems(String newText) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Product> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT + " WHERE itemcode || itemname LIKE '%" + newText + "%'", null);

            while (cursor.moveToNext()) {
                Product product = new Product();
                product.setFPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ID)));
                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMCODE)));
                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMNAME)));
                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE)));
                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QOH)));
                product.setFPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QTY)));
                product.setFPRODUCT_MAX_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_MAX_PRICE)));
                product.setFPRODUCT_MIN_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_MIN_PRICE)));
                product.setFPRODUCT_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_CHANGED_PRICE)));

                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }
    //2018-10-26 create by rashmi -because mega has inner return detail in invoice
    public ArrayList<Product> getAllItems(String newText,String txntype) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Product> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT + " WHERE itemcode || itemname LIKE '%" + newText + "%' and TxnType = '"+txntype+"' ORDER BY QOH DESC", null);

            while (cursor.moveToNext()) {
                Product product = new Product();
                product.setFPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ID)));
                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMCODE)));
                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMNAME)));
                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE)));
                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QOH)));
                product.setFPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QTY)));
//                product.setFPRODUCT_MAX_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_MAX_PRICE)));
//                product.setFPRODUCT_MIN_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_MIN_PRICE)));
//                product.setFPRODUCT_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_CHANGED_PRICE)));
                product.setFPRODUCT_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_TXNTYPE)));


                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }


    public void updateProductQty(String itemCode, String qty) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FPRODUCT_QTY, qty);
            dB.update(DatabaseHelper.TABLE_FPRODUCT, values, DatabaseHelper.FPRODUCT_ITEMCODE + " =?", new String[]{String.valueOf(itemCode)});

        } catch (Exception e) {
            Log.v(" Exception", e.toString());
        } finally {
            dB.close();
        }
    }



    public int updateProductQtyfor(String itemCode, String qty) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FPRODUCT_QTY, qty);
            count=(int)dB.update(DatabaseHelper.TABLE_FPRODUCT, values, DatabaseHelper.FPRODUCT_ITEMCODE + " =?", new String[]{String.valueOf(itemCode)});

        } catch (Exception e) {
        e.printStackTrace();
        } finally {
            dB.close();
        }
        return  count;
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
            values.put(DatabaseHelper.FPRODUCT_CHANGED_PRICE, price);
            count=(int)dB.update(DatabaseHelper.TABLE_FPRODUCT, values, DatabaseHelper.FPRODUCT_ITEMCODE + " =?", new String[]{String.valueOf(itemCode)});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
        return  count;
    }

    public ArrayList<Product> getSelectedItems() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Product> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT + " WHERE  qty<>'0'", null);

            while (cursor.moveToNext()) {
                Product product = new Product();
                product.setFPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ID)));
                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMCODE)));
                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMNAME)));
                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE)));
                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QOH)));
                product.setFPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QTY)));
                product.setFPRODUCT_MAX_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_MAX_PRICE)));
                product.setFPRODUCT_MIN_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_MIN_PRICE)));
                product.setFPRODUCT_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_CHANGED_PRICE)));
                product.setFPRODUCT_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_TXNTYPE)));

                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }
    public ArrayList<Product> getSelectedItems(String txntype) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Product> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT + " WHERE  qty<>'0' and TxnType = '"+txntype+"' ", null);

            while (cursor.moveToNext()) {
                Product product = new Product();
                product.setFPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ID)));
                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMCODE)));
                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMNAME)));
                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE)));
                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QOH)));
                product.setFPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QTY)));
                product.setFPRODUCT_MAX_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_MAX_PRICE)));
                product.setFPRODUCT_MIN_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_MIN_PRICE)));
                product.setFPRODUCT_CHANGED_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_CHANGED_PRICE)));
                product.setFPRODUCT_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_TXNTYPE)));

                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

         return list;
    }


    public ArrayList<InvDet> getSelectedItemsForInvoice(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<InvDet> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT + " WHERE  qty<>'0'", null);

            while (cursor.moveToNext()) {
                InvDet invDet = new InvDet();
                invDet.setFINVDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ID)));
                invDet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMCODE)));
                invDet.setFINVDET_REFNO(Refno);
                invDet.setFINVDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE)));
                invDet.setFINVDET_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QOH)));
                invDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QTY)));
                list.add(invDet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*/

    public void mClearTables() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(DatabaseHelper.TABLE_FPRODUCT, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }


    public void insertIntoProductAsBulk(String LocCode, String prillcode)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try
        {

            if(prillcode.equals(null) || prillcode.isEmpty())
            {
                String insertQuery1;
                insertQuery1 = "INSERT INTO fProducts (itemcode,itemname,price,qoh,ChangedPrice,TxnType,qty)\n" +
                        "SELECT \n" +
                        "itm.ItemCode AS ItemCode , \n" +
                        "itm.ItemName AS ItemName ,  \n" +
                        "IFNULL(pri.Price,0.0) AS Price , \n" +
//                        "IFNULL(pri.MinPrice,0.0) AS MinPrice , \n" +//commented on 2019-07-08 because price is 0.0
//                        "IFNULL(pri.MaxPrice,0.0) AS MaxPrice ,\n" +//commented on 2019-07-08 because price is 0.0
                        "loc.QOH AS QOH , \n" +
                        "\"0.0\" AS ChangedPrice , \n" +
                        "\"SA\" AS TxnType , \n" +
                        "\"0\" AS Qty \n" +
                        "FROM fItem itm\n" +
                        "INNER JOIN fItemLoc loc ON loc.ItemCode = itm.ItemCode \n" +
                        "LEFT JOIN fItemPri pri ON pri.ItemCode = itm.ItemCode \n" +
                        "AND pri.PrilCode = itm.PrilCode\n" +
                        "WHERE loc.LocCode = '"+LocCode+"'\n" +
                     //   "AND pri.Price > 0\n" +//commented on 2019-07-08 because price is 0.0
                        "GROUP BY itm.ItemCode ORDER BY QOH DESC";

                dB.execSQL(insertQuery1);
            }
            else
            {
                String insertQuery2;
                insertQuery2 = "INSERT INTO fProducts (itemcode,itemname,price,qoh,ChangedPrice,TxnType,qty)\n" +
                        "SELECT \n" +
                        "itm.ItemCode AS ItemCode , itm.ItemName AS ItemName ,  \n" +
                        "IFNULL(pri.Price,0.0) AS Price , " +
//                        "IFNULL(pri.MinPrice,0.0) AS MinPrice , \n" +//commented on 2019-07-08 because price is 0.0
//                        "IFNULL(pri.MaxPrice,0.0) AS MaxPrice ,\n" +//commented on 2019-07-08 because price is 0.0
                        "loc.QOH AS QOH , \"0.0\" AS ChangedPrice , \"SA\" AS TxnType , \"0\" AS Qty FROM fItem itm\n" +
                        "INNER JOIN fItemLoc loc ON loc.ItemCode = itm.ItemCode \n" +
                        "LEFT JOIN fItemPri pri ON pri.ItemCode = itm.ItemCode \n" +
                        "AND pri.PrilCode = '"+prillcode+"'\n" +
                        "WHERE loc.LocCode = '"+LocCode+"'\n" +
                    //    "AND pri.Price > 0\n" +//commented on 2019-07-08 because price is 0.0
                        "GROUP BY itm.ItemCode ORDER BY QOH DESC";

                dB.execSQL(insertQuery2);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if(dB.isOpen())
            {
                dB.close();
            }
        }
    }

    public void insertIntoProductAsBulkForPre(String LocCode, String prillcode)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try
        {

            if(prillcode.equals(null) || prillcode.isEmpty())
            {
                String insertQuery1;
                insertQuery1 = "INSERT INTO fProducts (itemcode,itemname,price,qoh,ChangedPrice,TxnType,qty)\n" +
                        "SELECT \n" +
                        "itm.ItemCode AS ItemCode , \n" +
                        "itm.ItemName AS ItemName ,  \n" +
                        "IFNULL(pri.Price,0.0) AS Price , \n" +
//                        "IFNULL(pri.MinPrice,0.0) AS MinPrice , \n" +//commented on 2019-07-08 because price is 0.0
//                        "IFNULL(pri.MaxPrice,0.0) AS MaxPrice ,\n" +//commented on 2019-07-08 because price is 0.0
                        "loc.QOH AS QOH , \n" +
                        "\"0.0\" AS ChangedPrice , \n" +
                        "\"SA\" AS TxnType , \n" +
                        "\"0\" AS Qty \n" +
                        "FROM fItem itm\n" +
                        "INNER JOIN fItemLoc loc ON loc.ItemCode = itm.ItemCode \n" +
                        "LEFT JOIN fItemPri pri ON pri.ItemCode = itm.ItemCode \n" +
                        "AND pri.PrilCode = itm.PrilCode\n" +
                        "WHERE loc.LocCode = '"+LocCode+"'\n" +
                        //   "AND pri.Price > 0\n" +//commented on 2019-07-08 because price is 0.0
                        "GROUP BY itm.ItemCode ORDER BY QOH DESC";

                dB.execSQL(insertQuery1);
            }
            else
            {
                String insertQuery2;
                insertQuery2 = "INSERT INTO fProducts (itemcode,itemname,price,qoh,ChangedPrice,TxnType,qty)\n" +
                        "SELECT \n" +
                        "itm.ItemCode AS ItemCode , itm.ItemName AS ItemName ,  \n" +
                        "IFNULL(pri.Price,0.0) AS Price , " +
//                        "IFNULL(pri.MinPrice,0.0) AS MinPrice , \n" +//commented on 2019-07-08 because price is 0.0
//                        "IFNULL(pri.MaxPrice,0.0) AS MaxPrice ,\n" +//commented on 2019-07-08 because price is 0.0
                        "loc.QOH AS QOH , \"0.0\" AS ChangedPrice , \"SA\" AS TxnType , \"0\" AS Qty FROM fItem itm\n" +
                        "INNER JOIN fItemLoc loc ON loc.ItemCode = itm.ItemCode \n" +
                        "LEFT JOIN fItemPri pri ON pri.ItemCode = itm.ItemCode \n" +
                        "AND pri.PrilCode = '"+prillcode+"'\n" +
                        "WHERE loc.LocCode = '"+LocCode+"'\n" +
                        //    "AND pri.Price > 0\n" +//commented on 2019-07-08 because price is 0.0
                        "GROUP BY itm.ItemCode ORDER BY QOH DESC";

                dB.execSQL(insertQuery2);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if(dB.isOpen())
            {
                dB.close();
            }
        }
    }


}
