package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.FddbNote;
import com.datamation.sfa.model.ReceiptDet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OutstandingController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "FDDbNoteDS ";

    public OutstandingController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateFDDbNote(ArrayList<FddbNote> list) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            for (FddbNote fdDbNote : list) {

                cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FDDBNOTE + " WHERE " + DatabaseHelper.REFNO + "='" + fdDbNote.getFDDBNOTE_REFNO() + "'", null);

                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FDDBNOTE_RECORD_ID, fdDbNote.getFDDBNOTE_RECORD_ID());
                values.put(DatabaseHelper.REFNO, fdDbNote.getFDDBNOTE_REFNO());
                values.put(DatabaseHelper.FDDBNOTE_REF_INV, fdDbNote.getFDDBNOTE_REF_INV());
                values.put(DatabaseHelper.FDDBNOTE_SALE_REF_NO, fdDbNote.getFDDBNOTE_SALE_REF_NO());
                values.put(DatabaseHelper.FDDBNOTE_MANU_REF, fdDbNote.getFDDBNOTE_MANU_REF());
                values.put(DatabaseHelper.FDDBNOTE_TXN_TYPE, fdDbNote.getFDDBNOTE_TXN_TYPE());
                values.put(DatabaseHelper.TXNDATE, fdDbNote.getFDDBNOTE_TXN_DATE());
                values.put(DatabaseHelper.FDDBNOTE_CUR_CODE, fdDbNote.getFDDBNOTE_CUR_CODE());
                values.put(DatabaseHelper.FDDBNOTE_CUR_RATE, fdDbNote.getFDDBNOTE_CUR_RATE());
                values.put(DatabaseHelper.FDDBNOTE_DEB_CODE, fdDbNote.getFDDBNOTE_DEB_CODE());
                values.put(DatabaseHelper.FDDBNOTE_REP_CODE, fdDbNote.getFDDBNOTE_REP_CODE());
                values.put(DatabaseHelper.FDDBNOTE_TAX_COM_CODE, fdDbNote.getFDDBNOTE_TAX_COM_CODE());
                values.put(DatabaseHelper.FDDBNOTE_TAX_AMT, fdDbNote.getFDDBNOTE_TAX_AMT());
                values.put(DatabaseHelper.FDDBNOTE_B_TAX_AMT, fdDbNote.getFDDBNOTE_B_TAX_AMT());
                values.put(DatabaseHelper.FDDBNOTE_AMT, fdDbNote.getFDDBNOTE_AMT());
                values.put(DatabaseHelper.FDDBNOTE_B_AMT, fdDbNote.getFDDBNOTE_B_AMT());
                values.put(DatabaseHelper.FDDBNOTE_TOT_BAL, fdDbNote.getFDDBNOTE_TOT_BAL());
                values.put(DatabaseHelper.FDDBNOTE_TOT_BAL1, fdDbNote.getFDDBNOTE_TOT_BAL1());
                values.put(DatabaseHelper.FDDBNOTE_OV_PAY_AMT, fdDbNote.getFDDBNOTE_OV_PAY_AMT());
                values.put(DatabaseHelper.FDDBNOTE_REMARKS, fdDbNote.getFDDBNOTE_REMARKS());
                values.put(DatabaseHelper.FDDBNOTE_CR_ACC, fdDbNote.getFDDBNOTE_CR_ACC());
                values.put(DatabaseHelper.FDDBNOTE_PRT_COPY, fdDbNote.getFDDBNOTE_PRT_COPY());
                values.put(DatabaseHelper.FDDBNOTE_GL_POST, fdDbNote.getFDDBNOTE_GL_POST());
                values.put(DatabaseHelper.FDDBNOTE_GL_BATCH, fdDbNote.getFDDBNOTE_GL_BATCH());
                values.put(DatabaseHelper.FDDBNOTE_ADD_USER, fdDbNote.getFDDBNOTE_ADD_USER());
                values.put(DatabaseHelper.FDDBNOTE_ADD_DATE, fdDbNote.getFDDBNOTE_ADD_DATE());
                values.put(DatabaseHelper.FDDBNOTE_ADD_MACH, fdDbNote.getFDDBNOTE_ADD_MACH());
                values.put(DatabaseHelper.FDDBNOTE_REFNO1, fdDbNote.getFDDBNOTE_REFNO1());
                values.put(DatabaseHelper.FDDBNOTE_ENTER_AMT, fdDbNote.getFDDBNOTE_ENTER_AMT());

                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_FDDBNOTE, values, DatabaseHelper.REFNO + "=?", new String[]{fdDbNote.getFDDBNOTE_REFNO().toString()});
                } else {
                    count = (int) dB.insert(DatabaseHelper.TABLE_FDDBNOTE, null, values);
                }

            }

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;
    }

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int deleteAll() {
        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FDDBNOTE, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_FDDBNOTE, null, null);
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

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public double getDebtorBalance(String DebCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double totbal = 0, totbal1 = 0;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT TotBal,TotBal1 FROM " + DatabaseHelper.TABLE_FDDBNOTE + " WHERE DebCode ='" + DebCode + "'";
            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                totbal = totbal + Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TOT_BAL)));
                totbal1 = totbal1 + Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TOT_BAL1)));
            }

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return totbal - totbal1;

    }

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<FddbNote> getDebtInfo(String DebCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FddbNote> list = new ArrayList<FddbNote>();
        try {
            String selectQuery;

            if (DebCode.equals(""))
                selectQuery = "SELECT refno,totbal,totbal1,txndate FROM " + DatabaseHelper.TABLE_FDDBNOTE;
            else
                selectQuery = "SELECT refno,totbal,totbal1,txndate FROM " + DatabaseHelper.TABLE_FDDBNOTE + " WHERE DebCode ='" + DebCode + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                FddbNote dbNote = new FddbNote();
                dbNote.setFDDBNOTE_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                dbNote.setFDDBNOTE_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                dbNote.setFDDBNOTE_TOT_BAL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TOT_BAL)));
                dbNote.setFDDBNOTE_TOT_BAL1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TOT_BAL1)));
                list.add(dbNote);
            }

            cursor.close();

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return list;

    }

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public String getHighestPaymentDue(String DebCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT txndate FROM " + DatabaseHelper.TABLE_FDDBNOTE + " WHERE DebCode ='" + DebCode + "' ORDER BY txndate ASC";
            cursor = dB.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE));

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            cursor.close();
            dB.close();
        }

        return null;
    }

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<String[]> getCustomerCreditInfo(String routeCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        String selectQuery = "select sum(f.totbal) as totbal, sum(f.totbal1) as totbal1,f.txndate,f.debcode,d.debcode,d.debname,t.townname,t.towncode from ftown t,fddbnote  f, fdebtor  d where f.debcode=d.debcode and t.towncode=d.towncode  and d.debcode in (select debcode from froutedet where routecode='" + routeCode + "') group by f.debcode,d.debcode,d.debname,t.townname,t.towncode";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            String[] arr = new String[6];

            arr[0] = cursor.getString(cursor.getColumnIndex("DebName"));
            arr[1] = cursor.getString(cursor.getColumnIndex("TownName"));
            arr[2] = cursor.getString(cursor.getColumnIndex("totbal"));
            arr[3] = cursor.getString(cursor.getColumnIndex("totbal1"));
            arr[4] = cursor.getString(cursor.getColumnIndex("TxnDate"));
            arr[5] = cursor.getString(cursor.getColumnIndex("DebCode"));

            if (Daybetween(arr[4]) > 60) {
                list.add(arr);
            }

        }

        cursor.close();
        dB.close();
        return list;

    }

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int Daybetween(String sDate) {

        long lDate1 = 0;
        long lDate2 = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf1.format(new Date());

            Calendar c1 = Calendar.getInstance();
            Date Date1 = sdf1.parse(currentDate);
            c1.setTime(Date1);

            Calendar c2 = Calendar.getInstance();
            Date Date2 = sdf.parse(sDate);
            c2.setTime(Date2);

            lDate1 = c1.getTimeInMillis();
            lDate2 = c2.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) ((lDate1 - lDate2) / (24 * 60 * 60 * 1000));
    }

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<FddbNote> getCreditBreakup(String debCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        ArrayList<FddbNote> list = new ArrayList<FddbNote>();

        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FDDBNOTE + " WHERE " + DatabaseHelper.FDDBNOTE_DEB_CODE + "='" + debCode + "'", null);

            while (cursor.moveToNext()) {

                FddbNote fdDbNote = new FddbNote();

                fdDbNote.setFDDBNOTE_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                fdDbNote.setFDDBNOTE_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                fdDbNote.setFDDBNOTE_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_AMT)));
                fdDbNote.setFDDBNOTE_TOT_BAL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TOT_BAL)));
                fdDbNote.setFDDBNOTE_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                fdDbNote.setFDDBNOTE_ADD_DATE(Daybetween(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE))) + "");
                fdDbNote.setFDDBNOTE_B_AMT(String.format("%,.2f", Double.parseDouble(fdDbNote.getFDDBNOTE_AMT()) - Double.parseDouble(fdDbNote.getFDDBNOTE_TOT_BAL())));
                list.add(fdDbNote);
            }

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;

    }

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<FddbNote> getAllRecords(String debcode, boolean isSummery) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FddbNote> list = new ArrayList<FddbNote>();
        try {

            String selectQuery;

            if (isSummery)
                selectQuery = "select * from " + DatabaseHelper.TABLE_FDDBNOTE + " WHERE " + " debcode='" + debcode + "' AND EnterAmt<>'' AND CAST(TotBal AS INT) > 0.0 Order By " + DatabaseHelper.TXNDATE;
            else
                selectQuery = "select * from " + DatabaseHelper.TABLE_FDDBNOTE + " WHERE " + " debcode='" + debcode + "' AND CAST(TotBal AS INT) > 0.0 Order By " + DatabaseHelper.TXNDATE;

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                FddbNote fdDbNote = new FddbNote();

                fdDbNote.setFDDBNOTE_ADD_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_ADD_DATE)));
                fdDbNote.setFDDBNOTE_ADD_MACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_ADD_MACH)));
                fdDbNote.setFDDBNOTE_ADD_USER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_ADD_USER)));
                fdDbNote.setFDDBNOTE_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_AMT)));
                fdDbNote.setFDDBNOTE_B_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_B_AMT)));
                fdDbNote.setFDDBNOTE_B_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_B_TAX_AMT)));
                fdDbNote.setFDDBNOTE_CR_ACC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_CR_ACC)));
                fdDbNote.setFDDBNOTE_CUR_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_CUR_CODE)));
                fdDbNote.setFDDBNOTE_CUR_RATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_CUR_RATE)));
                fdDbNote.setFDDBNOTE_DEB_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_DEB_CODE)));
                fdDbNote.setFDDBNOTE_ENTER_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_ENTER_AMT)));
                fdDbNote.setFDDBNOTE_GL_BATCH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_GL_BATCH)));
                fdDbNote.setFDDBNOTE_GL_POST(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_GL_POST)));
                fdDbNote.setFDDBNOTE_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_ID)));
                fdDbNote.setFDDBNOTE_MANU_REF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_MANU_REF)));
                fdDbNote.setFDDBNOTE_OV_PAY_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_OV_PAY_AMT)));
                fdDbNote.setFDDBNOTE_PRT_COPY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_PRT_COPY)));
                fdDbNote.setFDDBNOTE_RECORD_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_RECORD_ID)));
                fdDbNote.setFDDBNOTE_REF_INV(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_REF_INV)));
                fdDbNote.setFDDBNOTE_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
                fdDbNote.setFDDBNOTE_REFNO1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_REFNO1)));
                fdDbNote.setFDDBNOTE_REP_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_REP_CODE)));
                fdDbNote.setFDDBNOTE_SALE_REF_NO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_SALE_REF_NO)));
                fdDbNote.setFDDBNOTE_TAX_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TAX_AMT)));
                fdDbNote.setFDDBNOTE_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TAX_COM_CODE)));
                fdDbNote.setFDDBNOTE_TOT_BAL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TOT_BAL)));
                fdDbNote.setFDDBNOTE_TOT_BAL1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TOT_BAL1)));
                fdDbNote.setFDDBNOTE_TXN_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
                fdDbNote.setFDDBNOTE_TXN_TYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TXN_TYPE)));
                fdDbNote.setFDDBNOTE_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_REMARKS)));
                fdDbNote.setFDDBNOTE_REPNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_REPNAME)));
                // fdDbNote.setFDDBNOTE_ENTREMARK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_ENT_REMARK)));

                list.add(fdDbNote);

            }
            cursor.close();
        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return list;
    }

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int ClearFddbNoteData() {

        int result = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FDDBNOTE_ENTER_AMT, "");
            result = dB.update(DatabaseHelper.TABLE_FDDBNOTE, values, null, null);
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }
        return result;
    }

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateFddbNoteBalance(ArrayList<FddbNote> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            for (FddbNote fddb : list) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.FDDBNOTE_TOT_BAL, Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT()));
                values.put(DatabaseHelper.FDDBNOTE_ENTER_AMT, "");
                values.put(DatabaseHelper.FDDBNOTE_REMARKS, "");
                dB.update(DatabaseHelper.TABLE_FDDBNOTE, values, DatabaseHelper.REFNO + "=?", new String[] { fddb.getFDDBNOTE_REFNO().toString() });
            }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }


    public void UpdateFddbNoteBalanceForReceipt(ArrayList<ReceiptDet> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            for (ReceiptDet recDet : list) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.FDDBNOTE_TOT_BAL, Double.parseDouble(recDet.getFPRECDET_BAMT()) + Double.parseDouble(recDet.getFPRECDET_ALOAMT()));
                values.put(DatabaseHelper.FDDBNOTE_ENTER_AMT, "");
                dB.update(DatabaseHelper.TABLE_FDDBNOTE, values, DatabaseHelper.REFNO + "=?", new String[] { recDet.getFPRECDET_REFNO1().toString() });
            }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

}
