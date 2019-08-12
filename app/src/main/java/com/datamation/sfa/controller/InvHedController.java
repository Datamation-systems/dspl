package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.R;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.InvHed;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvHedController {

    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "INVHED";

    public InvHedController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateInvHed(ArrayList<InvHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (InvHed invHed : list) {

                String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE " + DatabaseHelper.REFNO + " = '" + invHed.getFINVHED_REFNO() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.REFNO, invHed.getFINVHED_REFNO());
                values.put(DatabaseHelper.FINVHED_ADDDATE, invHed.getFINVHED_ADDDATE());
                values.put(DatabaseHelper.FINVHED_ADDMACH, invHed.getFINVHED_ADDMACH());
                values.put(DatabaseHelper.FINVHED_ADDUSER, invHed.getFINVHED_ADDUSER());
                values.put(DatabaseHelper.FINVHED_COSTCODE, invHed.getFINVHED_COSTCODE());
                values.put(DatabaseHelper.FINVHED_CURCODE, invHed.getFINVHED_CURCODE());
                values.put(DatabaseHelper.FINVHED_CURRATE, invHed.getFINVHED_CURRATE());
                values.put(DatabaseHelper.FINVHED_DEBCODE, invHed.getFINVHED_DEBCODE());
                values.put(DatabaseHelper.FINVHED_START_TIME_SO, invHed.getFINVHED_START_TIME_SO());
                values.put(DatabaseHelper.FINVHED_END_TIME_SO, invHed.getFINVHED_END_TIME_SO());
                values.put(DatabaseHelper.FINVHED_LONGITUDE, invHed.getFINVHED_LONGITUDE());
                values.put(DatabaseHelper.FINVHED_LATITUDE, invHed.getFINVHED_LATITUDE());
                values.put(DatabaseHelper.FINVHED_LOCCODE, invHed.getFINVHED_LOCCODE());
                values.put(DatabaseHelper.FINVHED_MANUREF, invHed.getFINVHED_MANUREF());
                values.put(DatabaseHelper.FINVHED_REMARKS, invHed.getFINVHED_REMARKS());
                values.put(DatabaseHelper.FINVHED_REPCODE, invHed.getFINVHED_REPCODE());
                values.put(DatabaseHelper.FINVHED_TAXREG, invHed.getFINVHED_TAXREG());
                values.put(DatabaseHelper.FINVHED_TOTALAMT, invHed.getFINVHED_TOTALAMT());
                values.put(DatabaseHelper.FINVHED_TOTALDIS, invHed.getFINVHED_TOTALDIS());
                values.put(DatabaseHelper.FINVHED_TOTALTAX, invHed.getFINVHED_TOTALTAX());
                values.put(DatabaseHelper.FINVHED_TXNTYPE, invHed.getFINVHED_TXNTYPE());
                values.put(DatabaseHelper.TXNDATE, invHed.getFINVHED_TXNDATE());
                // values.put(DatabaseHelper.FINVHED_ADDRESS, invHed.getFINVHED_ADDRESS());
                values.put(DatabaseHelper.FINVHED_IS_SYNCED, "0");
                values.put(DatabaseHelper.FINVHED_IS_ACTIVE, invHed.getFINVHED_IS_ACTIVE());
                values.put(DatabaseHelper.FINVHED_REFNO1, invHed.getFINVHED_REFNO1());
                // values.put(DatabaseHelper.FINVHED_AREACODE, invHed.getFINVHED_AREACODE());
                values.put(DatabaseHelper.FINVHED_ROUTECODE, invHed.getFINVHED_ROUTECODE());
                values.put(DatabaseHelper.FINVHED_TOURCODE, invHed.getFINVHED_TOURCODE());
                values.put(DatabaseHelper.FINVHED_PAYTYPE, invHed.getFINVHED_PAYTYPE());
                values.put(DatabaseHelper.FINVHED_SETTING_CODE, invHed.getFINVHED_SETTING_CODE());

                int cn = cursor.getCount();
                if (cn > 0) {
                    count = dB.update(DatabaseHelper.TABLE_FINVHED, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(invHed.getFINVHED_REFNO())});
                } else {
                    count = (int) dB.insert(DatabaseHelper.TABLE_FINVHED, null, values);
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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public ArrayList<InvHed> getTodayOrders() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<InvHed> list = new ArrayList<InvHed>();

        try {
            //String selectQuery = "select DebCode, RefNo from fordHed " +
            String selectQuery = "select DebCode, RefNo, isSynced, TxnDate, TotalAmt from finvhed " +
                    //			" fddbnote fddb where hed.refno = det.refno and det.FPRECDET_REFNO1 = fddb.refno and hed.txndate = '2019-04-12'";
                    "  where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                InvHed recDet = new InvHed();

//
                recDet.setFINVHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                recDet.setFINVHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
                recDet.setFINVHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_SYNCED)));
                recDet.setFINVHED_TXNTYPE("Invoice");
                recDet.setFINVHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                recDet.setFINVHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALAMT)));
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
    public InvHed getActiveInvhed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        InvHed invHed = new InvHed();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVHED + " Where " + DatabaseHelper.FINVHED_IS_ACTIVE + "='1' and " + DatabaseHelper.FINVHED_IS_SYNCED + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    invHed.setFINVHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ID)));
                    invHed.setFINVHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                    invHed.setFINVHED_REFNO1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REFNO1)));
                    invHed.setFINVHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDDATE)));
                    invHed.setFINVHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDMACH)));
                    invHed.setFINVHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDUSER)));
                    invHed.setFINVHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_COSTCODE)));
                    invHed.setFINVHED_CURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURCODE)));
                    invHed.setFINVHED_CURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURRATE)));
                    invHed.setFINVHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
                    invHed.setFINVHED_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LOCCODE)));
                    invHed.setFINVHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_MANUREF)));
                    invHed.setFINVHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REMARKS)));
                    invHed.setFINVHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REPCODE)));
                    invHed.setFINVHED_TAXREG(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TAXREG)));
                    invHed.setFINVHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALAMT)));
                    invHed.setFINVHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALDIS)));
                    invHed.setFINVHED_TOTALTAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALTAX)));
                    invHed.setFINVHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TXNTYPE)));
                    invHed.setFINVHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                    invHed.setFINVHED_SETTING_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_SETTING_CODE)));

//                    invHed.setFINVHED_CONTACT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CONTACT)));
//                    invHed.setFINVHED_CUSADD1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD1)));
//                    invHed.setFINVHED_CUSADD2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD2)));
//                    invHed.setFINVHED_CUSADD3(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD3)));
//                    invHed.setFINVHED_CUSTELE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSTELE)));

                    //invHed.setFINVHED_ROUTECODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ROUTECODE)));
                  //  invHed.setFINVHED_TOURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOURCODE)));
                  ///  invHed.setFINVHED_AREACODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_AREACODE)));
                  //invHed.setFINVHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_PAYTYPE)));
                  //  invHed.setFINVHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_START_TIME_SO)));
                    return invHed;
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

        return null;
    }


    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
//
//    public ArrayList<VanSalesMapper> getAllUnsynced() {
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<VanSalesMapper> list = new ArrayList<VanSalesMapper>();
//
//        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVHED + " Where " + DatabaseHelper.FINVHED_IS_ACTIVE + "='0' AND " + DatabaseHelper.FINVHED_IS_SYNCED + "='0'";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//        localSP = context.getSharedPreferences(SETTINGS, 0);
//
//        while (cursor.moveToNext()) {
//
//            VanSalesMapper vanSalesMapper = new VanSalesMapper();
//            if(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TAXREG)).equals("Y")) {
//                vanSalesMapper.setNextNumVal(new CompanyBranchDS(context).getCurrentNextNumVal(context.getResources().getString(R.string.VanNumValTax)));
//            }else {
//                vanSalesMapper.setNextNumVal(new CompanyBranchDS(context).getCurrentNextNumVal(context.getResources().getString(R.string.VanNumValNonTax)));
//            }
//            vanSalesMapper.setDistDB(localSP.getString("Dist_DB", "").toString());
//            vanSalesMapper.setConsoleDB(localSP.getString("Console_DB", "").toString());
//
//            vanSalesMapper.setFINVHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ID)));
//            vanSalesMapper.setREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
//            vanSalesMapper.setFINVHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDDATE)));
//            vanSalesMapper.setFINVHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDMACH)));
//            vanSalesMapper.setFINVHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDUSER)));
//            vanSalesMapper.setFINVHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_COSTCODE)));
//            vanSalesMapper.setFINVHED_CURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURCODE)));
//            vanSalesMapper.setFINVHED_CURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURRATE)));
//            vanSalesMapper.setFINVHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
//            vanSalesMapper.setFINVHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_START_TIME_SO)));
//            vanSalesMapper.setFINVHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_END_TIME_SO)));
//            vanSalesMapper.setFINVHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LONGITUDE)));
//            vanSalesMapper.setFINVHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LATITUDE)));
//            vanSalesMapper.setFINVHED_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LOCCODE)));
//            vanSalesMapper.setFINVHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_MANUREF)));
//            vanSalesMapper.setFINVHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REMARKS)));
//            vanSalesMapper.setFINVHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REPCODE)));
//            vanSalesMapper.setFINVHED_TAXREG(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TAXREG)));
//            vanSalesMapper.setFINVHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALAMT)));
//            vanSalesMapper.setFINVHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALDIS)));
//            vanSalesMapper.setFINVHED_TOTALTAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALTAX)));
//            vanSalesMapper.setFINVHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TXNTYPE)));
//            vanSalesMapper.setFINVHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TXNDATE)));
//            vanSalesMapper.setFINVHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDRESS)));
//            vanSalesMapper.setFINVHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_SYNCED)));
//            vanSalesMapper.setFINVHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_ACTIVE)));
//            vanSalesMapper.setFINVHED_CONTACT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CONTACT)));
//            vanSalesMapper.setFINVHED_CUSADD1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD1)));
//            vanSalesMapper.setFINVHED_CUSADD2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD2)));
//            vanSalesMapper.setFINVHED_CUSADD3(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD3)));
//            vanSalesMapper.setFINVHED_CUSTELE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSTELE)));
//            vanSalesMapper.setFINVHED_TOURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOURCODE)));
//            vanSalesMapper.setFINVHED_ROUTECODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ROUTECODE)));
//            vanSalesMapper.setFINVHED_AREACODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_AREACODE)));
//            vanSalesMapper.setFINVHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_PAYTYPE)));
//            vanSalesMapper.setFINVHED_SETTING_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_SETTING_CODE)));
//
//            String RefNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO));
//
//            vanSalesMapper.setInvDets(new InvDetDS(context).getAllInvDet(RefNo));
//            vanSalesMapper.setInvTaxDTs(new InvTaxDTDS(context).getAllTaxDT(RefNo));
//            vanSalesMapper.setInvTaxRGs(new InvTaxRGDS(context).getAllTaxRG(RefNo));
//            vanSalesMapper.setOrderDiscs(new OrderDiscDS(context).getAllOrderDiscs(RefNo));
//            vanSalesMapper.setFreeIssues(new OrdFreeIssueDS(context).getAllFreeIssues(RefNo));
//            vanSalesMapper.setStkIsses(new StkIssDS(context).getUploadData(RefNo));
//            vanSalesMapper.setDispHeds(new DispHedDS(context).getUploadData(RefNo));
//            vanSalesMapper.setDispDets(new DispDetDS(context).getUploadData(RefNo));
//            vanSalesMapper.setDispIsses(new DispIssDS(context).getUploadData(RefNo));
//
//            list.add(vanSalesMapper);
//
//        }
//
//        return list;
//    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
//
    public int updateIsSynced(InvHed mapper) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FINVHED_IS_SYNCED, "1");

            if (mapper.getFINVHED_IS_SYNCED().equals("1")) {
                count = dB.update(DatabaseHelper.TABLE_FINVHED, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(mapper.getFINVHED_REFNO())});
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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int InactiveStatusUpdate(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE " + DatabaseHelper.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FINVHED_IS_ACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(DatabaseHelper.TABLE_FINVHED, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(refno)});
            } else {
                count = (int) dB.insert(DatabaseHelper.TABLE_FINVHED, null, values);
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

    /*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public InvHed getDetailsforPrint(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        InvHed SOHed = new InvHed();

        try {
            String selectQuery = "SELECT TxnDate,DebCode,Remarks,routecode,tourcode,TotalAmt,TotalDis FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE " + DatabaseHelper.REFNO + " = '" + Refno + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                SOHed.setFINVHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                SOHed.setFINVHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
                SOHed.setFINVHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REMARKS)));
                SOHed.setFINVHED_TOURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOURCODE)));
                SOHed.setFINVHED_ROUTECODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ROUTECODE)));
                SOHed.setFINVHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALAMT)));
                SOHed.setFINVHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALDIS)));
            }
            cursor.close();

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return SOHed;

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Reset invoice headers-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String restData(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        String locCode = "";

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE " + DatabaseHelper.REFNO + "='" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                String status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_SYNCED));
                /* if invoice already synced, can't delete */
                if (status.equals("1")) {
                    locCode = "";
                } else {
                    locCode = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LOCCODE));
                    int success = dB.delete(DatabaseHelper.TABLE_FINVHED, DatabaseHelper.REFNO + "='" + refno + "'", null);
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
        return locCode;
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<InvHed> getAllUnsyncedInvHed(String newText, String uploaded) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvHed> list = new ArrayList<InvHed>();
        Cursor cursor = null;
        try {

            String selectQuery;

            if (uploaded.equals("U"))
                selectQuery = "select * from finvHed sa, fdebtor cu where sa.isActive='0' AND sa.isSynced ='1' and sa.DebCode=cu.DebCode and sa.RefNo || sa.AddDate || cu.DebName  like '%" + newText + "%'";
            else
                selectQuery = "select * from finvHed sa, fdebtor cu where sa.isActive='0'AND sa.isSynced ='0' and sa.DebCode=cu.DebCode and sa.RefNo || sa.AddDate || cu.DebName  like '%" + newText + "%'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                InvHed invHed = new InvHed();

                invHed.setFINVHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ID)));
                invHed.setFINVHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                invHed.setFINVHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDDATE)));
                invHed.setFINVHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDMACH)));
                invHed.setFINVHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDUSER)));
                invHed.setFINVHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_COSTCODE)));
                invHed.setFINVHED_CURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURCODE)));
                invHed.setFINVHED_CURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURRATE)));
                invHed.setFINVHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
                invHed.setFINVHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_START_TIME_SO)));
                invHed.setFINVHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_END_TIME_SO)));
                invHed.setFINVHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LONGITUDE)));
                invHed.setFINVHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LATITUDE)));
                invHed.setFINVHED_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LOCCODE)));
                invHed.setFINVHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_MANUREF)));
                invHed.setFINVHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REMARKS)));
                invHed.setFINVHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REPCODE)));
                invHed.setFINVHED_TAXREG(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TAXREG)));
                invHed.setFINVHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALAMT)));
                invHed.setFINVHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALDIS)));
                invHed.setFINVHED_TOTALTAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALTAX)));
                invHed.setFINVHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TXNTYPE)));
                invHed.setFINVHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                invHed.setFINVHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDRESS)));
                invHed.setFINVHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_SYNCED)));
                invHed.setFINVHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_ACTIVE)));
                invHed.setFINVHED_CONTACT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CONTACT)));
                invHed.setFINVHED_CUSADD1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD1)));
                invHed.setFINVHED_CUSADD2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD2)));
                invHed.setFINVHED_CUSADD3(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD3)));
                invHed.setFINVHED_CUSTELE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSTELE)));
                invHed.setFINVHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_PAYTYPE)));

                list.add(invHed);

            }

        } catch (Exception e) {
            Log.v("Erorr :- ", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }
    public ArrayList<InvHed> getAllUnsyncedInvHed() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvHed> list = new ArrayList<InvHed>();
        Cursor cursor = null;
        try {

            String selectQuery;

                selectQuery = "select * from finvHed sa where sa.isActive='0' AND sa.isSynced ='0'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                InvHed invHed = new InvHed();

                invHed.setFINVHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ID)));
                invHed.setFINVHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                invHed.setFINVHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDDATE)));
                invHed.setFINVHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDMACH)));
                invHed.setFINVHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDUSER)));
                invHed.setFINVHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_COSTCODE)));
                invHed.setFINVHED_CURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURCODE)));
                invHed.setFINVHED_CURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURRATE)));
                invHed.setFINVHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
                invHed.setFINVHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_START_TIME_SO)));
                invHed.setFINVHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_END_TIME_SO)));
                invHed.setFINVHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LONGITUDE)));
                invHed.setFINVHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LATITUDE)));
                invHed.setFINVHED_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LOCCODE)));
                invHed.setFINVHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_MANUREF)));
                invHed.setFINVHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REMARKS)));
                invHed.setFINVHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REPCODE)));
                invHed.setFINVHED_TAXREG(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TAXREG)));
                invHed.setFINVHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALAMT)));
                invHed.setFINVHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALDIS)));
                invHed.setFINVHED_TOTALTAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALTAX)));
                invHed.setFINVHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TXNTYPE)));
                invHed.setFINVHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                invHed.setFINVHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDRESS)));
                invHed.setFINVHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_SYNCED)));
                invHed.setFINVHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_ACTIVE)));
                invHed.setFINVHED_CONTACT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CONTACT)));
                invHed.setFINVHED_CUSADD1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD1)));
                invHed.setFINVHED_CUSADD2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD2)));
                invHed.setFINVHED_CUSADD3(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD3)));
                invHed.setFINVHED_CUSTELE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSTELE)));
                invHed.setFINVHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_PAYTYPE)));

                list.add(invHed);

            }

        } catch (Exception e) {
            Log.v("Erorr :- ", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }

    /* Check if all records are synced */

    public boolean isAllSynced() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        boolean res = false;

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE " + DatabaseHelper.FINVHED_IS_SYNCED + "='0'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                res = false;
            else
                res = true;

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return res;

    }

    /*-*-*--*-*-*-*---*-*-*-*-*-*-*-*-*--*-*--*-*-*-*-*-*-*-*-*-*-*---*-*-*--*/

//    public boolean validateActiveInvoices() {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        boolean res = false;
//
//        String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        Cursor cursor = null;
//        try {
//            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE " + DatabaseHelper.FINVHED_IS_ACTIVE + "='1'";
//            cursor = dB.rawQuery(selectQuery, null);
//
//            /*Active invoice available*/
//            if (cursor.getCount() > 0) {
//                cursor.moveToFirst();
//
//                String txndate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TXNDATE));
//
//                /*txndate is equal to current date*/
//                if (txndate.equals(toDay))
//                    res = true;
//                /*if invoice is older, then reset data*/
//                else {
//                    String Refno = cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO));
//                    restData(Refno);
//                    new InvDetDS(context).restData(Refno);
//                    new OrderDiscDS(context).clearData(Refno);
//                    new OrdFreeIssueDS(context).ClearFreeIssues(Refno);
//                    UtilityContainer.ClearVanSharedPref(context);
//                }
//
//            } else
//                res = false;
//
//        } catch (Exception e) {
//            Log.v(TAG, e.toString());
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//
//        return res;
//
//    }


    public String getActiveInvoiceRef()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String refNo = "";

        String selectQuery = "select * from " + dbHelper.TABLE_FINVHED + " WHERE " + dbHelper.FINVHED_IS_ACTIVE + "= '1'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            if (cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    refNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO));
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

        return refNo;
    }
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public double getLastBillAmount() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT TotalAmt FROM " + DatabaseHelper.TABLE_FINVHED;
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                cursor.moveToLast();
                return Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALAMT)));

            } else
                return 0.00;

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return 0.00;

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Entry> getMonthlySales(int iMonth) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Entry> list = new ArrayList<Entry>();

        try {

            int j = 0;
            for (int i = (iMonth - 2); i < (iMonth + 1); i++) {

                int iYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
                int temp;
                if (i == 0) {
                    temp = 12;
                    iYear -= 1;
                } else if (i == -1) {
                    temp = 11;
                    iYear -= 1;
                } else {
                    temp = i;
                }

                String selectQuery = "SELECT SUM(totalamt) as totsum FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE  txndate LIKE '" + iYear + "-" + String.format("%02d", temp) + "-_%'";
                Cursor cursor = dB.rawQuery(selectQuery, null);

                while (cursor.moveToNext()) {

                    if (cursor.getString(cursor.getColumnIndex("totsum")) == null)
                        list.add(new Entry(0, j++));
                    else
                        list.add(new Entry(Float.parseFloat(cursor.getString(cursor.getColumnIndex("totsum"))) / 1000, j++));
                }
                cursor.close();
            }

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return list;
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getRefnoByDebcode(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FINVHED + " WHERE " + dbHelper.REFNO + "='" + refno + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(dbHelper.FINVHED_DEBCODE));
        }
        return "";

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getMonthlySales(String costCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double invSum = 0, retSum = 0;
        try {

            int iYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
            int iMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));

            String selectQuery = "SELECT SUM(totalamt) as totsum FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE CostCode='" + costCode + "' AND txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'";
            Cursor curInvHed = dB.rawQuery(selectQuery, null);

            while (curInvHed.moveToNext())
                invSum = Double.parseDouble(curInvHed.getString(curInvHed.getColumnIndex("totsum")));

            String selectQuery1 = "SELECT SUM(totalamt) as totsum FROM " + DatabaseHelper.TABLE_FINVRHED + " WHERE CostCode='" + costCode + "' AND txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'";
            Cursor curRetHed = dB.rawQuery(selectQuery1, null);

            while (curRetHed.moveToNext())
                retSum = Double.parseDouble(curRetHed.getString(curRetHed.getColumnIndex("totsum")));

            curInvHed.close();
            curRetHed.close();

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return String.format("%,.2f", invSum - retSum);
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getMonthlyVisits(String costCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        int invVisit = 0, retVisit = 0;
        try {

            int iYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
            int iMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));

            String selectQuery = "SELECT count(*) as totvisit FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE CostCode='" + costCode + "' AND txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'";
            Cursor curInvHed = dB.rawQuery(selectQuery, null);

            while (curInvHed.moveToNext())
                invVisit = curInvHed.getInt(curInvHed.getColumnIndex("totvisit"));

            String selectQuery1 = "SELECT count(*) as totvisit FROM " + DatabaseHelper.TABLE_FINVRHED + " WHERE CostCode='" + costCode + "' AND txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'";
            Cursor curRetHed = dB.rawQuery(selectQuery1, null);

            while (curRetHed.moveToNext())
                retVisit = curRetHed.getInt(curRetHed.getColumnIndex("totvisit"));

            curInvHed.close();
            curRetHed.close();

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return String.valueOf(invVisit + retVisit);
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getCurrentMonthlySales(String costCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        int invVisit = 0;
        try {

            int iYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
            int iMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));

            String selectQuery = "SELECT count(*) as totvisit FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE CostCode='" + costCode + "' AND txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'";
            Cursor curInvHed = dB.rawQuery(selectQuery, null);

            while (curInvHed.moveToNext())
                invVisit = curInvHed.getInt(curInvHed.getColumnIndex("totvisit"));

            curInvHed.close();

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return String.valueOf(invVisit);
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getTodaySales(String costCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double invSum = 0, retSum = 0;
        try {

            String sDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String selectQuery = "SELECT SUM(totalamt) as totsum FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE CostCode='" + costCode + "' AND txndate='" + sDate + "'";
            Cursor curInvHed = dB.rawQuery(selectQuery, null);

            while (curInvHed.moveToNext())
                invSum = Double.parseDouble(curInvHed.getString(curInvHed.getColumnIndex("totsum")));

            String selectQuery1 = "SELECT SUM(totalamt) as totsum FROM " + DatabaseHelper.TABLE_FINVRHED + " WHERE CostCode='" + costCode + "' AND txndate='" + sDate + "'";
            Cursor curRetHed = dB.rawQuery(selectQuery1, null);

            while (curRetHed.moveToNext())
                retSum = Double.parseDouble(curRetHed.getString(curRetHed.getColumnIndex("totsum")));

            curInvHed.close();
            curRetHed.close();

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return String.format("%,.2f", invSum - retSum);
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getTodaySalesVisit(String costCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        int invSum = 0, retSum = 0;
        try {

            String sDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String selectQuery = "SELECT count(*) as totvisit FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE CostCode='" + costCode + "' AND txndate='" + sDate + "'";
            Cursor curInvHed = dB.rawQuery(selectQuery, null);

            while (curInvHed.moveToNext())
                invSum = curInvHed.getInt(curInvHed.getColumnIndex("totvisit"));

            String selectQuery1 = "SELECT count(*) as totvisit FROM " + DatabaseHelper.TABLE_FINVRHED + " WHERE CostCode='" + costCode + "' AND txndate='" + sDate + "'";
            Cursor curRetHed = dB.rawQuery(selectQuery1, null);

            while (curRetHed.moveToNext())
                retSum = curRetHed.getInt(curRetHed.getColumnIndex("totvisit"));

            curInvHed.close();
            curRetHed.close();

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return String.valueOf(invSum + retSum);
    }

    public String getTodayProductivity(String costCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        int invSum = 0;

        try {

            String sDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String selectQuery = "SELECT count(*) as totvisit FROM " + DatabaseHelper.TABLE_FINVHED + " WHERE CostCode='" + costCode + "' AND txndate='" + sDate + "'";
            Cursor curInvHed = dB.rawQuery(selectQuery, null);

            while (curInvHed.moveToNext())
                invSum = curInvHed.getInt(curInvHed.getColumnIndex("totvisit"));

            curInvHed.close();

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return String.valueOf(invSum);
    }

    public ArrayList<InvHed> getAllUnsynced() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<InvHed> list = new ArrayList<InvHed>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FINVHED + " Where " + DatabaseHelper.FINVHED_IS_ACTIVE + "='0' AND " + DatabaseHelper.FINVHED_IS_SYNCED + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        localSP = context.getSharedPreferences(SETTINGS, 0);

        while (cursor.moveToNext()) {

            InvHed vanSalesMapper = new InvHed();

            vanSalesMapper.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.VanNumVal)));

            vanSalesMapper.setDistDB(localSP.getString("Dist_DB", "").toString());
            vanSalesMapper.setConsoleDB(SharedPref.getInstance(context).getDatabase());

            vanSalesMapper.setFINVHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ID)));
            vanSalesMapper.setFINVHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
            vanSalesMapper.setFINVHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDDATE)));
            vanSalesMapper.setFINVHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDMACH)));
            vanSalesMapper.setFINVHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDUSER)));
            vanSalesMapper.setFINVHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_COSTCODE)));
            vanSalesMapper.setFINVHED_CURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURCODE)));
            vanSalesMapper.setFINVHED_CURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CURRATE)));
            vanSalesMapper.setFINVHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_DEBCODE)));
            vanSalesMapper.setFINVHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_START_TIME_SO)));
            vanSalesMapper.setFINVHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_END_TIME_SO)));
            vanSalesMapper.setFINVHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LONGITUDE)));
            vanSalesMapper.setFINVHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LATITUDE)));
            vanSalesMapper.setFINVHED_LOCCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_LOCCODE)));
            vanSalesMapper.setFINVHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_MANUREF)));
            vanSalesMapper.setFINVHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REMARKS)));
            vanSalesMapper.setFINVHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_REPCODE)));
            vanSalesMapper.setFINVHED_TAXREG(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TAXREG)));
            vanSalesMapper.setFINVHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALAMT)));
            vanSalesMapper.setFINVHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALDIS)));
            vanSalesMapper.setFINVHED_TOTALTAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOTALTAX)));
            vanSalesMapper.setFINVHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TXNTYPE)));
            vanSalesMapper.setFINVHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
            vanSalesMapper.setFINVHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ADDRESS)));
            vanSalesMapper.setFINVHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_SYNCED)));
            vanSalesMapper.setFINVHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_IS_ACTIVE)));
            vanSalesMapper.setFINVHED_CONTACT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CONTACT)));
            vanSalesMapper.setFINVHED_CUSADD1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD1)));
            vanSalesMapper.setFINVHED_CUSADD2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD2)));
            vanSalesMapper.setFINVHED_CUSADD3(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSADD3)));
            vanSalesMapper.setFINVHED_CUSTELE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_CUSTELE)));
            vanSalesMapper.setFINVHED_TOURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_TOURCODE)));
            vanSalesMapper.setFINVHED_ROUTECODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_ROUTECODE)));
            vanSalesMapper.setFINVHED_AREACODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_AREACODE)));
            vanSalesMapper.setFINVHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_PAYTYPE)));
            vanSalesMapper.setFINVHED_SETTING_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FINVHED_SETTING_CODE)));


            String RefNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO));

            vanSalesMapper.setInvDets(new InvDetController(context).getAllInvDet(RefNo));
            vanSalesMapper.setInvTaxDTs(new InvTaxDTController(context).getAllTaxDT(RefNo));
            vanSalesMapper.setInvTaxRGs(new InvTaxRGController(context).getAllTaxRG(RefNo));
            vanSalesMapper.setOrderDiscs(new OrderDiscController(context).getAllOrderDiscs(RefNo));
            vanSalesMapper.setFreeIssues(new OrdFreeIssueController(context).getAllFreeIssues(RefNo));
            vanSalesMapper.setStkIsses(new StkIssController(context).getUploadData(RefNo));
            vanSalesMapper.setDispHeds(new DispHedController(context).getUploadData(RefNo));
            vanSalesMapper.setDispDets(new DispDetController(context).getUploadData(RefNo));
            vanSalesMapper.setDispIsses(new DispIssController(context).getUploadData(RefNo));


            list.add(vanSalesMapper);

        }

        return list;
    }
}
