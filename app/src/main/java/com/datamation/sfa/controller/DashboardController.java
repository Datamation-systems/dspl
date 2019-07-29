package com.datamation.sfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.datamation.sfa.helpers.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DashboardController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "DashboardController";

    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";


    public DashboardController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    //current month target
    public Double getRepTarget() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double targetsum =300000.00;

        Cursor cursor = dB.rawQuery("SELECT Target from fTarget where Month = '"+String.format("%02d", curMonth)+"' and Year = '" + curYear +"'", null);

        while (cursor.moveToNext()) {
            targetsum = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Target")));
            return targetsum;
        }

        cursor.close();
        dB.close();
        return targetsum;

    }
    //current month discount
    public Double getTMDiscounts() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        try {


            double discount;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(a.DisAmt)),0)  as totAmt from FOrdDisc a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return discount;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;


    }
    //current month gross
    public Double getMonthAchievement() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double monthAchieve;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(a.Amt)),0)  as totAmt from Forddet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                monthAchieve = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return monthAchieve;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }
    //current month return
    public Double getTMReturn() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double discount;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(a.Amt)),0)  as totAmt from FInvRDet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return discount;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }
    //current month productive
    public int getTMProductiveCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select count(DISTINCT DebCode) from FOrdHed where txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'", null);

        while (cursor.moveToNext()) {

            int result = cursor.getInt(0);

            if (result>0)
                return result;

        }
        cursor.close();
        dB.close();
        return 0;

    }
    //current month non productive
    public int getTMNonPrdCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select count(refno) from FDaynPrdHed where txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'", null);

        while (cursor.moveToNext()) {

            int result = cursor.getInt(0);

            if (result>0)
                return result;

        }
        cursor.close();
        dB.close();
        return 0;

    }

    //previous month target
    public Double getPMRepTarget() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double targetsum =200000.00;

        Cursor cursor = dB.rawQuery("SELECT Target from fTarget where Month = '"+String.format("%02d", curMonth-1)+"' and Year = '" + curYear +"'", null);

        while (cursor.moveToNext()) {
            targetsum = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Target")));
            return targetsum;
        }

        cursor.close();
        dB.close();
        return targetsum;

    }
    //previous month discount
    public Double getPMDiscounts() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        try {


            double discount;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(a.DisAmt)),0)  as totAmt from FOrdDisc a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth-1) + "-_%'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return discount;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;


    }
    //previous month gross
    public Double getPMonthAchievement() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double monthAchieve;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(a.Amt)),0)  as totAmt from Forddet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth-1) + "-_%'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                monthAchieve = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return monthAchieve;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }
    //previous month return
    public Double getPMReturn() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double discount;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(a.Amt)),0)  as totAmt from FInvRDet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth-1) + "-_%'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return discount;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }
    //previous month productive
    public int getPMProductiveCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select count(DISTINCT DebCode) from FOrdHed where txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth-1) + "-_%'", null);

        while (cursor.moveToNext()) {

            int result = cursor.getInt(0);

            if (result>0)
                return result;

        }
        cursor.close();
        dB.close();
        return 0;

    }
    //previous month non productive
    public int getPMNonPrdCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select count(refno) from FDaynPrdHed where txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth-1) + "-_%'", null);

        while (cursor.moveToNext()) {

            int result = cursor.getInt(0);

            if (result>0)
                return result;

        }
        cursor.close();
        dB.close();
        return 0;

    }



    //today productive
    public int getProductiveCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select count(DISTINCT DebCode) from FOrdHed where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'", null);

        while (cursor.moveToNext()) {

            int result = cursor.getInt(0);

            if (result>0)
                return result;

        }
        cursor.close();
        dB.close();
        return 0;

    }
    //today nonproductive
    public int getNonPrdCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select count(refno) from FDaynPrdHed where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'", null);

        while (cursor.moveToNext()) {

            int result = cursor.getInt(0);

            if (result>0)
                return result;

        }
        cursor.close();
        dB.close();
        return 0;

    }
    public String getRoute(String repCode) {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        String curdate = curYear+"-"+ String.format("%02d", curMonth) + "-" + String.format("%02d", curDate);
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select RouteCode from fTourHed where '"+curdate+"' between DateFrom And DateTo and RepCode = '"+repCode+"'", null);

        while (cursor.moveToNext()) {

           String result = cursor.getString(cursor.getColumnIndex("RouteCode"));
            return result;

        }
        cursor.close();
        dB.close();
        return "";

    }
    public int getOutletCount(String route) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select count(DISTINCT DebCode) from fRouteDet where RouteCode = '"+route.trim()+"'", null);

        while (cursor.moveToNext()) {

            int result = cursor.getInt(0);

            if (result>0)
                return result;

        }
        cursor.close();
        dB.close();
        return 0;

    }


public Double getTodayDiscount() {

    if (dB == null) {
        open();
    } else if (!dB.isOpen()) {
        open();
    }

    ArrayList<String[]> list = new ArrayList<String[]>();

    int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
    int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
    int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

    try {


        double discount;


        Cursor cursor;

        cursor = dB.rawQuery("select ifnull((sum(a.DisAmt)),0)  as totAmt from FOrdDisc a where a.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"'", null);
        // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

        while (cursor.moveToNext()) {
            discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
            return discount;
        }


        cursor.close();

    } catch (NumberFormatException e) {
        e.printStackTrace();
    } finally {
        dB.close();
    }

    return 0.0;

}
    public Double getTodayReturn() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double discount;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(a.Amt)),0)  as totAmt from FInvRDet a where a.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" +
                     String.format("%02d", curDate)+"'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return discount;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }

    public Double getTodayCashCollection() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double discount;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(det.amt)),0)  as totAmt from fprecdets  det, fprecheds hed where det.refno = hed.refno and " +

                    "det.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"'" +
                    " and  det.dtxndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"' and hed.paytype = 'CA'", null);
//                    "det.txndate = '2019-04-12'" +
//                    " and  det.dtxndate = '2019-02-13'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return discount;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }
    public Double getTodayChequeCollection() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double discount;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(det.amt)),0)  as totAmt from fprecdets  det, fprecheds hed where det.refno = hed.refno and " +

                    "det.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"'" +
                    " and  det.dtxndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"' and hed.paytype = 'CH'", null);
//                    "det.txndate = '2019-04-12'" +
//                    " and  det.dtxndate = '2019-02-13'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return discount;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }
    public Double getTodayCashPreviousCollection() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double discount;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(det.aloamt)),0)  as totAmt from fprecdets  det, fprecheds hed where det.refno = hed.refno and " +
                    "det.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"'" +
                    " and  det.dtxndate <> '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"' and hed.paytype = 'CA'", null);
//                    "det.txndate = '2019-04-12'" +
//                    " and  det.dtxndate <> '2019-02-13'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return discount;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }
    public Double getTodayChequePreviousCollection() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double discount;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(det.aloamt)),0)  as totAmt from fprecdets  det , fprecheds hed where det.refno = hed.refno and " +
                    "det.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"'" +
                    " and  det.dtxndate <> '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"' and hed.paytype = 'CH'", null);
//                    "det.txndate = '2019-04-12'" +
//                    " and  det.dtxndate <> '2019-02-13'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return discount;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }
    public Double getDailyAchievement() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        try {


            double monthAchieve;


            Cursor cursor;

            cursor = dB.rawQuery("select ifnull((sum(a.Amt)),0)  as totAmt from Forddet a where a.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-"+ String.format("%02d", curDate)+"'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                monthAchieve = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
                return monthAchieve;
            }


            cursor.close();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }

        return 0.0;

    }
}
