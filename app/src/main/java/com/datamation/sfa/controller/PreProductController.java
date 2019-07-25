package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.PreProduct;

import java.util.ArrayList;

public class PreProductController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;

    public PreProductController(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void insertOrUpdatePreProducts(ArrayList<PreProduct> list) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_FPRODUCT_PRE + " (itemcode_pre,itemname_pre,price_pre,qoh_pre,qty_pre) VALUES (?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (PreProduct items : list) {

                stmt.bindString(1, items.getPREPRODUCT_ITEMCODE());
                stmt.bindString(2, items.getPREPRODUCT_ITEMNAME());
                stmt.bindString(3, items.getPREPRODUCT_PRICE());
                stmt.bindString(4, items.getPREPRODUCT_QOH());
                stmt.bindString(5, items.getPREPRODUCT_QTY());

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
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT_PRE, null);

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

    public ArrayList<PreProduct> getAllItems(String newText) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<PreProduct> list = new ArrayList<>();
        try {
            //cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT_PRE + " WHERE itemcode || itemname LIKE '%" + newText + "%' and TxnType = '"+txntype+"' ORDER BY QOH DESC", null);
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT_PRE + " WHERE itemcode_pre || itemname_pre LIKE '%" + newText + "%' group by itemcode_pre", null);

            while (cursor.moveToNext()) {
                PreProduct product = new PreProduct();
                product.setPREPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ID_PRE)));
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMCODE_PRE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMNAME_PRE)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE_PRE)));
                product.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QOH_PRE)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QTY_PRE)));
//                product.setPREPRODUCT_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_TXNTYPE)));
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

    public ArrayList<PreProduct> getItemsCodeVise(String newText, String ItemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<PreProduct> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT_PRE + " WHERE itemcode_pre || itemname_pre LIKE '%" + newText + "%'and itemcode_pre = '" + ItemCode + "'", null);

            while (cursor.moveToNext()) {
                PreProduct product = new PreProduct();
                product.setPREPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ID_PRE)));
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMCODE_PRE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMNAME_PRE)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE_PRE)));
                product.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QOH_PRE)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QTY_PRE)));
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

    public String getPriceByItemCode(String ItemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT_PRE + " WHERE itemcode_pre = '" + ItemCode + "'", null);

            while (cursor.moveToNext())
            {
                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE_PRE));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return null;
    }

    public void updateProductQty(String itemCode, String qty) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FPRODUCT_QTY_PRE, qty);
            dB.update(DatabaseHelper.TABLE_FPRODUCT_PRE, values, DatabaseHelper.FPRODUCT_ITEMCODE_PRE + " =?", new String[]{String.valueOf(itemCode)});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }

    public int updateProductQtyFor(String itemCode, String qty) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FPRODUCT_QTY_PRE, qty);
            count=(int)  dB.update(DatabaseHelper.TABLE_FPRODUCT_PRE, values, DatabaseHelper.FPRODUCT_ITEMCODE_PRE + " =?", new String[]{String.valueOf(itemCode)});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
        return count;
    }

    public ArrayList<PreProduct> getSelectedItems() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<PreProduct> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT_PRE + " WHERE  qty_pre<>'0'", null);

            while (cursor.moveToNext()) {
                PreProduct product = new PreProduct();
                product.setPREPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ID_PRE)));
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMCODE_PRE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMNAME_PRE)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE_PRE)));
                product.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QOH_PRE)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QTY_PRE)));

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

    public void mClearTables() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(DatabaseHelper.TABLE_FPRODUCT_PRE, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
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
                insertQuery1 = "INSERT INTO fProducts_pre (itemcode_pre,itemname_pre,price_pre,qoh_pre,qty_pre)\n" +
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
                insertQuery2 = "INSERT INTO fProducts_pre (itemcode_pre,itemname_pre,price_pre,qoh_pre,qty_pre)\n" +
                        "SELECT \n" +
                        "itm.ItemCode AS ItemCode , itm.ItemName AS ItemName ,  \n" +
                        "IFNULL(pri.Price,0.0) AS Price , " +
//                        "IFNULL(pri.MinPrice,0.0) AS MinPrice , \n" +//commented on 2019-07-08 because price is 0.0
//                        "IFNULL(pri.MaxPrice,0.0) AS MaxPrice ,\n" +//commented on 2019-07-08 because price is 0.0
                        "loc.QOH AS QOH , \"0\" AS Qty FROM fItem itm\n" +
//                        "loc.QOH AS QOH , \"0.0\" AS ChangedPrice , \"SA\" AS TxnType , \"0\" AS Qty FROM fItem itm\n" +
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
