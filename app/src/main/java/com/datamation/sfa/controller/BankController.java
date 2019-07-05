package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Bank;

import java.util.ArrayList;

public class BankController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "BankDS ";

    public BankController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateBank(ArrayList<Bank> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (Bank bank : list) {

                Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FBANK + " WHERE " + DatabaseHelper.FBANK_BANK_CODE + "='" + bank.getFBANK_BANK_CODE() + "'", null);

                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FBANK_BANK_CODE, bank.getFBANK_BANK_CODE());
                values.put(DatabaseHelper.FBANK_BANK_NAME, bank.getFBANK_BANK_NAME());
                values.put(DatabaseHelper.FBANK_BANK_ACC_NO, bank.getFBANK_BANK_ACC_NO());
                values.put(DatabaseHelper.FBANK_BRANCH, bank.getFBANK_BRANCH());
                values.put(DatabaseHelper.FBANK_ADD1, bank.getFBANK_ADD1());
                values.put(DatabaseHelper.FBANK_ADD2, bank.getFBANK_ADD2());
                values.put(DatabaseHelper.FBANK_ADD_DATE, bank.getFBANK_ADD_DATE());
                values.put(DatabaseHelper.FBANK_ADD_MACH, bank.getFBANK_ADD_MACH());
                values.put(DatabaseHelper.FBANK_ADD_USER, bank.getFBANK_ADD_USER());

                if (cursor.getCount() > 0) {
                    dB.update(DatabaseHelper.TABLE_FBANK, values, DatabaseHelper.FBANK_BANK_CODE + "=?", new String[]{bank.getFBANK_BANK_CODE().toString()});
                    Log.v(TAG, "Updated");
                } else {
                    count = (int) dB.insert(DatabaseHelper.TABLE_FBANK, null, values);
                    Log.v(TAG, "Inserted " + count);
                }
                cursor.close();
            }

        } catch (Exception e) {
            Log.v(TAG, e.toString());
        } finally {
            dB.close();
        }
        return count;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FBANK, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_FBANK, null, null);
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
    /*-----------------------------------------------------------------------*/
    public ArrayList<Bank> getBanks() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Bank> list = new ArrayList<Bank>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_FBANK;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                Bank bank = new Bank();

                bank.setFBANK_BANK_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FBANK_BANK_CODE)));
                bank.setFBANK_BANK_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FBANK_BANK_NAME))+" - "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.FBANK_BRANCH)));


                list.add(bank);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();

        }

        return list;
    }


    public String getBankCodeAndBranchCodeByBankName(String bankName,String branchName)
    {
        String bankCode = null;

        if (dB == null)
        {
            open();
        }
        else if (!dB.isOpen())
        {
            open();
        }


        try
        {
            String selectQuery = "SELECT bankcode FROM '"+DatabaseHelper.TABLE_FBANK+"' WHERE BankName = '"+bankName.trim()+"' AND Branch = '"+branchName.trim()+"' ";
            Cursor cursor = dB.rawQuery(selectQuery,null);
            cursor.moveToFirst();

            if(cursor.getCount() > 0)
            {
                bankCode = cursor.getString(cursor.getColumnIndex("bankcode"));

                if(!bankCode.isEmpty())
                {
                    return bankCode;
                }
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

        return bankCode;
    }

}
