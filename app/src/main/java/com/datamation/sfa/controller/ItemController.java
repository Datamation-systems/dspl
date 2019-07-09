package com.datamation.sfa.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.sfa.model.Item;
import com.datamation.sfa.model.Product;
import com.datamation.sfa.model.StockInfo;
import com.datamation.sfa.model.tempOrderDet;
import com.datamation.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;


public class ItemController {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "ItemController";

    public static SharedPreferences localSP;

    public ItemController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void InsertOrReplaceItems(ArrayList<Item> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_FITEM + " (AvgPrice,BrandCode,GroupCode,ItemCode,ItemName,ItemStatus,PrilCode,VenPcode,NouCase,ReOrderLvl,ReOrderQty,UnitCode,TypeCode,TaxComCode) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Item items : list) {

                stmt.bindString(1, items.getFITEM_AVGPRICE());
                stmt.bindString(2, items.getFITEM_BRANDCODE());
                stmt.bindString(3, items.getFITEM_GROUPCODE());
                stmt.bindString(4, items.getFITEM_ITEM_CODE());
                stmt.bindString(5, items.getFITEM_ITEM_NAME());
                stmt.bindString(6, items.getFITEM_ITEMSTATUS());
                stmt.bindString(7, items.getFITEM_PRILCODE());
                stmt.bindString(8, items.getFITEM_NOUCASE());
                stmt.bindString(9, items.getFITEM_TYPECODE());
                stmt.bindString(10, items.getFITEM_UNITCODE());
                stmt.bindString(11, items.getFITEM_VENPCODE());
                stmt.bindString(12, items.getFITEM_REORDER_LVL());
                stmt.bindString(13, items.getFITEM_REORDER_QTY());
                stmt.bindString(14, items.getFITEM_TAXCOMCODE());

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

    public String getItemNameByCode(String code) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FITEM + " WHERE " + DatabaseHelper.FITEM_ITEM_CODE + "='" + code + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME));

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return "";
    }

    public ArrayList<Product> getAllItems(String LocCode, String prillcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Product> list = new ArrayList<>();
        String selectQuery;
        selectQuery = "SELECT itm.* , loc.QOH FROM fitem itm, fitemLoc loc WHERE  loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "' order by CAST(loc.QOH AS Integer) DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                Product product = new Product();
                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));

                /* Get rid of 0 QOH items */
                if (qoh > 0) {
                    product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                    product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                    product.setFPRODUCT_PRICE(new ItemPriceController(context).getProductPriceByCode(product.getFPRODUCT_ITEMCODE(), prillcode));
                    product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                    product.setFPRODUCT_QTY("0");
                    list.add(product);
                }
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

    public ArrayList<Item> getAllItemForSalesReturn(String newText, String type, String refno, String LocCode, String prillcode ) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Item> list = new ArrayList<Item>();
        String selectQuery;
//        selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.brandcode, itm.avgprice, loc.QOH, pric.price FROM fitem itm, fitemLoc loc, fitempri pric WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND pric.ItemCode=itm.ItemCode AND pric.prilcode='" + prillcode + "' AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM FTranSODet WHERE " + type + " And RefNo ='" + refno + "') ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.brandcode, itm.avgprice, loc.QOH, pric.price FROM fitem itm, fitemLoc loc, fitempri pric WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND pric.ItemCode=itm.ItemCode AND pric.prilcode='" + prillcode + "' ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {
                Item items=new Item();
                items.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                items.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                items.setFITEM_AVGPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMPRI_PRICE)));
                items.setFITEM_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                list.add(items);
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

//    public ArrayList<Item> findAllItems(String key) {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//        Cursor cursor = null;
//        ArrayList<Item> list = new ArrayList<Item>();
//        try {
//
//
//            String searchsql = "";
//            searchsql = "SELECT * FROM " + DatabaseHelper.TABLE_ITEMS + " WHERE ItemName LIKE '" + key + "%'";
//            cursor = dB.rawQuery(searchsql, null);
//
//
//            while (cursor.moveToNext()) {
//
//                Item item = new Item();
//                item.setITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_CODE)));
//                item.setITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_NAME)));
//                list.add(item);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            cursor.close();
//            dB.close();
//        }
//
//        return list;
//    }

    public ArrayList<StockInfo> getStocks(String newText, String LocCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<StockInfo> list = new ArrayList<StockInfo>();

        String selectQuery = "SELECT itm.* , loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "' order by loc.QOH DESC";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {

                StockInfo items = new StockInfo();
                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                if (qoh > 0) {
                    items.setStock_Itemcode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                    items.setStock_Itemname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                    items.setStock_Qoh(((int) qoh) + "");
                    list.add(items);
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }
        return list;
    }

    public String getTotalStockQOH(String LocCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT SUM(loc.QOH) as totqty FROM fitem itm, fitemLoc loc WHERE loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "'";

        try {

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex("totqty"));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }

        return null;
    }
}
