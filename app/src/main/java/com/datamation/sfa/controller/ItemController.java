package com.datamation.sfa.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.sfa.model.Group;
import com.datamation.sfa.model.Item;
import com.datamation.sfa.model.ItemFreeIssue;
import com.datamation.sfa.model.PreProduct;
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

    public ArrayList<Item> getAllItemForSalesReturn(String newText, String type, String refno, String LocCode, String prillcode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Item> list = new ArrayList<Item>();
        String selectQuery;
//        selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.brandcode, itm.avgprice, loc.QOH, pric.price FROM fitem itm, fitemLoc loc, fitempri pric WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND pric.ItemCode=itm.ItemCode AND pric.prilcode='" + prillcode + "' AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM FTranSODet WHERE " + type + " And RefNo ='" + refno + "') ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        //selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.brandcode, itm.avgprice, loc.QOH, pric.price FROM fitem itm, fitemLoc loc, fitempri pric WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND pric.ItemCode=itm.ItemCode AND pric.prilcode='" + prillcode + "' ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.brandcode, itm.avgprice, loc.QOH, pric.price FROM fitem itm, fitemLoc loc, fitempri pric WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND pric.ItemCode=itm.ItemCode ORDER BY CAST(loc.QOH AS FLOAT) DESC";
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

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FITEM, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_FITEM, null, null);
                Log.v("Success", success + "");
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

    /*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    public String getTaxComCodeByItemCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT TaxComCode FROM " + DatabaseHelper.TABLE_FITEM + " WHERE " + DatabaseHelper.FITEM_ITEM_CODE + "='" + itemCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_TAX_COM_CODE));
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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Item> getAllItem(String newText, String type, String refno, String LocCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Item> list = new ArrayList<Item>();
        String selectQuery;
        selectQuery = "SELECT itm.*, loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM FTranSODet WHERE " + type + " And RefNo ='" + refno + "') ORDER BY CAST(loc.QOH AS FLOAT) DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                /* Get rid of 0 QOH items */
                // if (qoh > 0) {
                Item items = new Item();
                items.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                items.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                // items.setFITEM_NOUCASE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_NOUCASE)));
                items.setFITEM_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                items.setFITEM_BRANDCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_BRAND_CODE)));
                items.setFITEM_AVGPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_AVG_PRICE)));

                list.add(items);
                //    }
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
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Item> getAllItem(String newText, String type, String refno, String LocCode, String prillcode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Item> list = new ArrayList<Item>();
        String selectQuery;
        //Yasith - 2019-01-23 - Original Query - SELECT itm.*, loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM FInvRDet WHERE " + type + " And RefNo ='" + refno + "') ORDER BY CAST(loc.QOH AS FLOAT) DESC
        //Yasith - 2019-01-23 - Changed Query - SELECT itm.*, loc.QOH FROM fitem itm INNER JOIN fitemLoc loc ON itm.ItemCode = loc.ItemCode WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.LocCode='" + LocCode + "' AND  itm.ItemCode not in (SELECT ItemCode FROM FInvRDet WHERE " + type + " And RefNo ='" + refno + "' GROUP BY ItemCode) ORDER BY CAST(loc.QOH AS FLOAT) DESC

        //Yasith - 2019-01-23 - Changed due to item load slowness issue
        if(prillcode.isEmpty() || prillcode.equals(""))
        {
            selectQuery = "SELECT itm.*, loc.QOH,\n" +
                    "IFNULL(pri.Price,0.0) AS Price  \n" +
//                          "IFNULL(pri.MinPrice,0.0) AS MinPrice , \n" +
//                          "IFNULL(pri.MaxPrice,0.0) AS MaxPrice \n" +
                    " FROM fitem itm INNER JOIN fitemLoc loc ON itm.ItemCode = loc.ItemCode \n" +
                    "LEFT JOIN fItemPri pri ON pri.ItemCode = itm.ItemCode \n" +
                    "WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.LocCode='" + LocCode + "' AND pri.Price > 0 AND  itm.ItemCode not in \n" +
                    "(SELECT ItemCode FROM InvRDet WHERE " + type + " And RefNo ='" + refno + "' GROUP BY ItemCode)\n" +
                    "GROUP BY itm.ItemCode\n" +
                    "ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        }
        else
        {
            selectQuery = "SELECT itm.*, loc.QOH, " +
                    "IFNULL(pri.Price,0.0) AS Price   " +
//                          "IFNULL(pri.MinPrice,0.0) AS MinPrice , \n" +
//                          "IFNULL(pri.MaxPrice,0.0) AS MaxPrice \n" +
                    " FROM fitem itm INNER JOIN fitemLoc loc ON itm.ItemCode = loc.ItemCode " +
                    " LEFT JOIN fItemPri pri ON pri.ItemCode = itm.ItemCode " +
                    " WHERE itm.ItemCode || itm.ItemName LIKE '%"+ newText +"%' AND loc.LocCode='"+ LocCode +"' AND pri.Price > 0 AND pri.PrilCode = '"+prillcode+"' AND  itm.ItemCode not in " +
                    "(SELECT ItemCode FROM InvRDet WHERE  "+ type +"  AND RefNo ='"+ refno +"' GROUP BY ItemCode) " +
                    "GROUP BY itm.ItemCode " +
                    "ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        }

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                /* Get rid of 0 QOH items */
                if (qoh > 0) {
                    Item items = new Item();
                    items.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                    items.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                    // items.setFITEM_NOUCASE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_NOUCASE)));
                    items.setFITEM_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                    items.setFITEM_BRANDCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_BRAND_CODE)));
                    String prillCodeFromItems = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_PRIL_CODE));
                    //Yasith - 2019-01-23
//                    items.setFITEM_PRICE(cursor.getString(cursor.getColumnIndex("Price")));
//                    items.setFITEM_MIN_PRICE(cursor.getString(cursor.getColumnIndex("MinPrice")));
//                    items.setFITEM_MAX_PRICE(cursor.getString(cursor.getColumnIndex("MaxPrice")));

                    //if(prillcode.equals(""))
                    //prillcode = prillCodeFromItems;
                    //String price=new ItemPriDS(context).getProductPriceByCode(items.getFITEM_ITEM_CODE(), prillcode);
                    //String minprice=new ItemPriDS(context).getProductMinPriceByCode(items.getFITEM_ITEM_CODE(), prillcode);
                    //String maxprice=new ItemPriDS(context).getProductMaxPriceByCode(items.getFITEM_ITEM_CODE(), prillcode);
                    //if(price.isEmpty()|| price.equalsIgnoreCase("")){
                    //    items.setFITEM_PRICE("0.00");
                    //}else{
                    //    items.setFITEM_PRICE(price);
                    //}
                    //if(minprice.isEmpty()|| minprice.equalsIgnoreCase("")){
                    //    items.setFITEM_MIN_PRICE("0.00");
                    //}else{
                    //    items.setFITEM_MIN_PRICE(minprice);
                    //}
                    //if(maxprice.isEmpty()|| maxprice.equalsIgnoreCase("")){
                    //    items.setFITEM_MAX_PRICE("0.00");
                    //}else{
                    //    items.setFITEM_MAX_PRICE(maxprice);
                    //}
                    list.add(items);
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


    public ArrayList<Item> getAllItem_from_code(String newText, String type, String refno, String LocCode, String ItemCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Item> list = new ArrayList<Item>();
        String selectQuery;
        selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.brandcode, itm.avgprice, loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM FTranSODet WHERE " + type + " And RefNo ='" + refno + "') and itm.ItemCode = '" + ItemCode + "' ORDER BY CAST(loc.QOH AS FLOAT) DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                /* Get rid of 0 QOH items */
                if (qoh > 0) {
                    Item items = new Item();
                    items.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                    items.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                    //    items.setFITEM_NOUCASE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_NOUCASE)));
                    items.setFITEM_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                    items.setFITEM_BRANDCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_BRAND_CODE)));
                    items.setFITEM_AVGPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_AVG_PRICE)));
                    list.add(items);
                }
            }
            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
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
    /*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    public ArrayList<Item> getAllItemForVanSale(String newText, String refno, String LocCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Item> list = new ArrayList<Item>();

        String selectQuery;

        selectQuery = "SELECT itm.* , loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "' AND itm.ItemCode not in (SELECT DISTINCT itemcode FROM finvdet WHERE txntype='22' And refno ='" + refno + "') order by CAST(loc.QOH AS FLOAT) DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                Item items = new Item();

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                /* Get rid of 0 QOH items */
                if (qoh > 0) {

                    items.setFITEM_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ID)));

                    items.setFITEM_AVGPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_AVG_PRICE)));
                    items.setFITEM_BRANDCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_BRAND_CODE)));
                    items.setFITEM_GROUPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_GROUP_CODE)));
                    items.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                    items.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                    items.setFITEM_ITEMSTATUS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_STATUS)));
                    items.setFITEM_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_PRIL_CODE)));
                    items.setFITEM_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_TAX_COM_CODE)));
                    items.setFITEM_TYPECODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_TYPE_CODE)));
                    items.setFITEM_UNITCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_UNIT_CODE)));
                    items.setFITEM_VENPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_VEN_P_CODE)));
//                    items.setFITEM_DISCOUNT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_DISCOUNT)));
//                    items.setFITEM_ISDISCOUNT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_IS_DISCOUNT)));
//                    items.setFITEM_SCATCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_SCAT_CODE)));
//                    items.setFITEM_ISSIZE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_IS_SIZE)));
//                    items.setFITEM_SUBCATCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_SUBCAT_CODE)));
//                    items.setFITEM_COLORCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_COLOR_CODE)));
//                    items.setFITEM_CLASSCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_CLASS_CODE)));

                    items.setFITEM_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));

                    list.add(items);
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

    /*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    public ArrayList<Group> getAllItemGroups() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Group> list = new ArrayList<Group>();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FGROUP;
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {
                Group group = new Group();
                group.setFGROUP_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FGROUP_CODE)));
                group.setFGROUP_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FGROUP_NAME)));
                list.add(group);
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

    /*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-**-*-*-*/

    public String getGroupByCode(String groupCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT GroupName FROM " + DatabaseHelper.TABLE_FGROUP + " WHERE GroupCode='" + groupCode + "'";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FGROUP_NAME));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return null;
    }

    /*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-**-*/

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

    /*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-**-*-*-*/

    public ArrayList<Item> getAllItems(String newText, String LocCode, String GroupCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Item> list = new ArrayList<Item>();

        String selectQuery = "SELECT itm.GroupCode, itm.ItemCode,itm.ItemName, loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.GroupCode = '" + GroupCode + "' AND itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "' order by CAST(loc.QOH AS FLOAT) DESC";

        try {

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                Item items = new Item();

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                if (qoh > 0) {
                    items.setFITEM_GROUPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_GROUP_CODE)));
                    items.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                    items.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                    items.setFITEM_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
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

    /*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-**-*-*-*/

    public ArrayList<StockInfo> getStocks(String newText, String LocCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<StockInfo> list = new ArrayList<StockInfo>();

        //String selectQuery = "SELECT itm.* , loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "' order by loc.QOH DESC";
        String selectQuery = "SELECT itm.* , loc.LocCode, loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND  loc.itemcode=itm.itemcode  order by loc.QOH DESC";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {

                StockInfo items = new StockInfo();
                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                if (qoh > 0) {
                    items.setStock_Itemcode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                    //items.setStock_Itemname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                    items.setStock_Itemname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_LOC_CODE))+" - "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
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

    /*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-**-*-*-*/

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

    public ArrayList<ItemFreeIssue> getAllFreeItemNameByRefno(String code) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<ItemFreeIssue> list = new ArrayList<ItemFreeIssue>();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FITEM + " WHERE " + DatabaseHelper.FITEM_ITEM_CODE + " in (select itemcode from ffreeItem where refno ='" + code + "')";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            ItemFreeIssue issue = new ItemFreeIssue();
            Item items = new Item();

            items.setFITEM_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ID)));
            items.setFITEM_AVGPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_AVG_PRICE)));
            items.setFITEM_BRANDCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_BRAND_CODE)));
            items.setFITEM_GROUPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_GROUP_CODE)));
            items.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
            items.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
            items.setFITEM_ITEMSTATUS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_STATUS)));
            //  items.setFITEM_NOUCASE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_NOUCASE)));
            items.setFITEM_PRILCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_PRIL_CODE)));
            items.setFITEM_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_TAX_COM_CODE)));
            items.setFITEM_TYPECODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_TYPE_CODE)));
            items.setFITEM_UNITCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_UNIT_CODE)));
            items.setFITEM_VENPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_VEN_P_CODE)));
            //items.setFITEM_ADD_MATCH(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_ADDMATCH)));
            //items.setFITEM_ADD_USER(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_ADD_USER)));
            //items.setFITEM_MAP_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_MAP_CODE)));
            //items.setFITEM_MUST_SALE(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_MUST_SALE)));
            //items.setFITEM_ORD_SEQ(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_ORDSEQ)));
            //items.setFITEM_RE_ORDER_LVL(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_RE_ORDER_LVL)));
            //items.setFITEM_RE_ORDER_QTY(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_RE_ORDER_QTY)));
            //items.setFITEM_S_BRAND_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_S_BRAND_CODE)));
            //items.setFITEM_SKU_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_SKU_CODE)));
            //items.setFITEM_SKU_SIZ_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_SKU_SIZ_CODE)));

            issue.setItems(items);
            issue.setAlloc("0");
            list.add(issue);

        }

        return list;
    }



//---------------------------------------------------get Items for VanSales mega------------------------------------------------

    public ArrayList<Product> getAllItemForVanSales(String LocCode, String prillcode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Product> list = new ArrayList<Product>();
        String selectQuery;
        selectQuery = "SELECT itm.* , loc.QOH FROM fitem itm, fitemLoc loc WHERE  loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "' order by CAST(loc.QOH AS Integer) DESC";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                /* Get rid of 0 QOH items */
                if (qoh > 0) {
                    Product product=new Product();
                    product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                    product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                    product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                    product.setFPRODUCT_QTY("0");
                    product.setFPRODUCT_CHANGED_PRICE("0.0");
                    product.setFPRODUCT_TXN_TYPE("SA");


                    String prillCodeFromItems = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_PRIL_CODE));
                    //rashmi - 2018-08-13 for indra.because debtor has no prillcode, it get from fitems
                    if(prillcode.equals(""))
                        prillcode = prillCodeFromItems;
                    String price=new ItemPriController(context).getProductPriceByCode(product.getFPRODUCT_ITEMCODE(), prillcode);
                    // String minprice=new ItemPriDS(context).getProductMinPriceByCode(product.getFPRODUCT_ITEMCODE(), prillcode);
                    // String maxprice=new ItemPriDS(context).getProductMaxPriceByCode(product.getFPRODUCT_ITEMCODE(), prillcode);
                    if(price.isEmpty()|| price.equalsIgnoreCase("")){
                        product.setFPRODUCT_PRICE("0.00");
                    }else{
                        product.setFPRODUCT_PRICE(price);
                    }
//                if(minprice.isEmpty()|| minprice.equalsIgnoreCase("")){
//                    product.setFPRODUCT_MIN_PRICE("0.00");
//                }else{
//                    product.setFPRODUCT_MIN_PRICE(minprice);
//                }
//                if(maxprice.isEmpty()|| maxprice.equalsIgnoreCase("")){
//                    product.setFPRODUCT_MAX_PRICE("0.00");
//                }else{
//                    product.setFPRODUCT_MAX_PRICE(maxprice);
//                }

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
    public String getBrandCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("SELECT brancode FROM fitem WHERE itemcode='" + itemCode + "'", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("brandcode"));

        }
        cursor.close();
        dB.close();

        return "";
    }
//--------------------------------------------get Items for Must Sales----------------#dhanushika#------------------------------------

    public ArrayList<Item> getItemsForMustSales() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        String selectQuery = "select * from fItem where itemStatus='0'";
        Cursor cu = null;
        cu = dB.rawQuery(selectQuery, null);
        ArrayList<Item> itemsArrayList = new ArrayList<Item>();
        try {
            while (cu.moveToNext()) {
                Item items = new Item();
                items.setFITEM_ITEM_CODE(cu.getString(cu.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                items.setFITEM_ITEM_NAME(cu.getString(cu.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                itemsArrayList.add(items);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cu.close();
            dB.close();
        }

        return itemsArrayList;
    }
//---------------------------------------------------get Items for PreSales indra------------------------------------------------

    public ArrayList<PreProduct> getAllItemForPreSales(String newText, String type, String refno, String prillcode ) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<PreProduct> list = new ArrayList<PreProduct>();
        String selectQuery;
        selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.PrilCode, itm.brandcode, itm.avgprice, loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM OrderDetail WHERE type ='" + type + "' And RefNo ='" + refno + "') ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                /* Get rid of 0 QOH items */
                //  if (qoh > 0) {
                PreProduct preProduct=new PreProduct();
                preProduct.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                preProduct.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                String prillCodeFromItems = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_PRIL_CODE));
                //rashmi - 2018-08-13 for indra.because debtor has no prillcode, it get from fitems
                if(prillcode.equals(""))
                    prillcode = prillCodeFromItems;
                String price=new ItemPriController(context).getProductPriceByCode(preProduct.getPREPRODUCT_ITEMCODE(), prillcode);
                if(price.isEmpty()|| price.equalsIgnoreCase("")){
                    preProduct.setPREPRODUCT_PRICE("0.00");
                }else{
                    preProduct.setPREPRODUCT_PRICE(price);
                }

                preProduct.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                preProduct.setPREPRODUCT_QTY("0");
//                    preProduct.setPREPRODUCT_MARKUP(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_MARKUP)));
//                    preProduct.setPREPRODUCT_MARKUP_PER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_MARKUP_PER)));
                //   preProduct.setPREPRODUCT_IS_CHANGE_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_PRICE_CHANGE)));
                //      preProduct.setPREPRODUCT_DISCOUNT("0.0");
                //     preProduct.setPREPRODUCT_LGRNPRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_LGRNPRICE)));
                //    preProduct.setPREPRODUCT_CHANGED_PRICE("0.0");
                list.add(preProduct);
                // }
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
//---------------------------------------------------get Items for PreSales finac new------------------------------------------------

    public ArrayList<PreProduct> getAllItemForPreSalesOld(String newText,String type, String refno, String LocCode,String prillcode ) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<PreProduct> list = new ArrayList<PreProduct>();
        String selectQuery;
        selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.brandcode, itm.avgprice, loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM FTranSODet WHERE " + type + " And RefNo ='" + refno + "') ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                /* Get rid of 0 QOH items */
                //  if (qoh > 0) {
                PreProduct preProduct=new PreProduct();
                preProduct.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                preProduct.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                String price=new ItemPriController(context).getProductPriceByCode(preProduct.getPREPRODUCT_ITEMCODE(), prillcode);
                if(price.isEmpty()|| price.equalsIgnoreCase("")){
                    preProduct.setPREPRODUCT_PRICE("0.00");
                }else{
                    preProduct.setPREPRODUCT_PRICE(price);
                }

                preProduct.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                preProduct.setPREPRODUCT_QTY("0");
                list.add(preProduct);
                // }
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
    /*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    public String getCostPriceItemCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT avgprice FROM " + DatabaseHelper.TABLE_FITEM + " WHERE " + DatabaseHelper.FITEM_ITEM_CODE + "='" + itemCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_AVG_PRICE));
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
    /*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    public String getPrillCodebyItemCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT PrilCode FROM " + DatabaseHelper.TABLE_FITEM + " WHERE " + DatabaseHelper.FITEM_ITEM_CODE + "='" + itemCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_PRIL_CODE));
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
    //-------------------------------------getItems FromC item Code vise--------------------------------------------------------

    public ArrayList<PreProduct> getItemsCodeViseForPreSales(String type, String refno, String LocCode,String prillcode,String ItemCode ) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<PreProduct> list = new ArrayList<PreProduct>();
        String selectQuery;
        selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.brandcode, itm.avgprice, loc.QOH FROM fitem itm, fitemLoc loc WHERE loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM FTranSODet WHERE " + type + " And RefNo ='" + refno + "')and itm.ItemCode = '" + ItemCode + "' ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                /* Get rid of 0 QOH items */
                if (qoh > 0) {
                    PreProduct preProduct=new PreProduct();
                    preProduct.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_CODE)));
                    preProduct.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEM_ITEM_NAME)));
                    preProduct.setPREPRODUCT_PRICE(new ItemPriController(context).getProductPriceByCode(preProduct.getPREPRODUCT_ITEMCODE(), prillcode));
                    preProduct.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FITEMLOC_QOH)));
                    preProduct.setPREPRODUCT_QTY("0");
                    list.add(preProduct);
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

    public String getTaxComCodeByItemCodeBeforeDebTax(String itemCode, String debtorCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT TaxComCode FROM " + dbHelper.TABLE_FITEM + " WHERE " + dbHelper.FITEM_ITEM_CODE + "='" + itemCode + "'";
        Cursor cursor = dB.rawQuery(selectQuery, null);

        if (cursor.getCount()>0) {

            while (cursor.moveToNext())
            {
                return cursor.getString(cursor.getColumnIndex(dbHelper.FITEM_TAX_COM_CODE));
            }
            cursor.close();
        }
        else
        {
            String query = "SELECT taxCode FROM " + DatabaseHelper.TABLE_FDEBTAX + " WHERE " + DatabaseHelper.FDEBTAX_DEBCODE + "='" + debtorCode + "'";
            Cursor cursor1 = dB.rawQuery(query, null);

            while (cursor1.moveToNext())
            {
                return cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.FDEBTAX_TAXCODE));
            }
            cursor1.close();
        }
        dB.close();

        return "";
    }
}
