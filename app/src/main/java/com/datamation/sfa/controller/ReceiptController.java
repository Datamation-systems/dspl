package com.datamation.sfa.controller;

import java.util.ArrayList;

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
import com.datamation.sfa.model.ReceiptHed;


public class ReceiptController {

	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "SMACKCERA";

	public static final String SETTINGS = "SETTINGS";
	public static SharedPreferences localSP;

	public ReceiptController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	public int createOrUpdateRecHed(ArrayList<ReceiptHed> list) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			for (ReceiptHed recHed : list) {

				String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECHED + " WHERE "
						+ DatabaseHelper.REFNO + " = '" + recHed.getFPRECHED_REFNO() + "'";

				cursor = dB.rawQuery(selectQuery, null);

				ContentValues values = new ContentValues();

				values.put(DatabaseHelper.FPRECHED_ID, recHed.getFPRECHED_ID());
				values.put(DatabaseHelper.REFNO, recHed.getFPRECHED_REFNO());
				values.put(DatabaseHelper.FPRECHED_REFNO1, recHed.getFPRECHED_REFNO1());
				//values.put(DatabaseHelper.REFNO2, recHed.getFPRECHED_REFNO2());
				values.put(DatabaseHelper.FPRECHED_MANUREF, recHed.getFPRECHED_MANUREF());
				values.put(DatabaseHelper.FPRECHED_SALEREFNO, recHed.getFPRECHED_SALEREFNO());
				values.put(DatabaseHelper.FPRECHED_REPCODE, recHed.getFPRECHED_REPCODE());
				values.put(DatabaseHelper.FPRECHED_TXNTYPE, recHed.getFPRECHED_TXNTYPE());
				values.put(DatabaseHelper.FPRECHED_CHQNO, recHed.getFPRECHED_CHQNO());
				values.put(DatabaseHelper.FPRECHED_CHQDATE, recHed.getFPRECHED_CHQDATE());
				values.put(DatabaseHelper.TXNDATE, recHed.getFPRECHED_TXNDATE());
				values.put(DatabaseHelper.FPRECHED_CURCODE, recHed.getFPRECHED_CURCODE());
				values.put(DatabaseHelper.FPRECHED_CURRATE1, "");

				values.put(DatabaseHelper.FPRECHED_DEBCODE, recHed.getFPRECHED_DEBCODE());
				values.put(DatabaseHelper.FPRECHED_TOTALAMT, recHed.getFPRECHED_TOTALAMT());
				values.put(DatabaseHelper.FPRECHED_BTOTALAMT, recHed.getFPRECHED_BTOTALAMT());
				values.put(DatabaseHelper.FPRECHED_PAYTYPE, recHed.getFPRECHED_PAYTYPE());
				values.put(DatabaseHelper.FPRECHED_PRTCOPY, "");
				values.put(DatabaseHelper.FPRECHED_REMARKS, recHed.getFPRECHED_REMARKS());
				values.put(DatabaseHelper.FPRECHED_ADDUSER, recHed.getFPRECHED_ADDUSER());
				values.put(DatabaseHelper.FPRECHED_ADDMACH, recHed.getFPRECHED_ADDMACH());
				values.put(DatabaseHelper.FPRECHED_ADDDATE, recHed.getFPRECHED_ADDDATE());
				values.put(DatabaseHelper.FPRECHED_RECORDID, "");
				values.put(DatabaseHelper.FPRECHED_TIMESTAMP, "");
				values.put(DatabaseHelper.FPRECHED_CURRATE, recHed.getFPRECHED_CURRATE());
				values.put(DatabaseHelper.FPRECHED_CUSBANK, recHed.getFPRECHED_CUSBANK());
				values.put(DatabaseHelper.FPRECHED_LONGITUDE, recHed.getFPRECHED_LONGITUDE());
				values.put(DatabaseHelper.FPRECHED_LATITUDE, recHed.getFPRECHED_LATITUDE());
				values.put(DatabaseHelper.FPRECHED_ADDRESS, recHed.getFPRECHED_ADDRESS());
				values.put(DatabaseHelper.FPRECHED_START_TIME, recHed.getFPRECHED_START_TIME());
				values.put(DatabaseHelper.FPRECHED_END_TIME, recHed.getFPRECHED_END_TIME());
				values.put(DatabaseHelper.FPRECHED_ISSYNCED, recHed.getFPRECHED_ISSYNCED());
				values.put(DatabaseHelper.FPRECHED_ISACTIVE, recHed.getFPRECHED_ISACTIVE());
				values.put(DatabaseHelper.FPRECHED_ISDELETE, recHed.getFPRECHED_ISDELETE());

				int cn = cursor.getCount();
				if (cn > 0) {
					count = dB.update(DatabaseHelper.TABLE_FPRECHED, values, DatabaseHelper.REFNO + " =?",
							new String[] { String.valueOf(recHed.getFPRECHED_REFNO()) });
				} else {
					count = (int) dB.insert(DatabaseHelper.TABLE_FPRECHED, null, values);
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

	/*
	 * create for store singlr receipt
	 */
	public int createOrUpdateRecHedS(ArrayList<ReceiptHed> list) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			for (ReceiptHed recHed : list) {

				String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECHEDS + " WHERE "
						+ DatabaseHelper.REFNO + " = '" + recHed.getFPRECHED_REFNO() + "'";

				cursor = dB.rawQuery(selectQuery, null);

				ContentValues values = new ContentValues();

				//values.put(DatabaseHelper.FPRECHED_ID, recHed.getFPRECHED_ID());
				values.put(DatabaseHelper.REFNO, recHed.getFPRECHED_REFNO());
				values.put(DatabaseHelper.FPRECDET_REFNO1, recHed.getFPRECHED_REFNO1());
				values.put(DatabaseHelper.FPRECHED_MANUREF, recHed.getFPRECHED_MANUREF());
				values.put(DatabaseHelper.FPRECHED_SALEREFNO, recHed.getFPRECHED_SALEREFNO());
				values.put(DatabaseHelper.FPRECHED_REPCODE, recHed.getFPRECHED_REPCODE());
				values.put(DatabaseHelper.FPRECHED_TXNTYPE, recHed.getFPRECHED_TXNTYPE());
				values.put(DatabaseHelper.FPRECHED_CHQNO, recHed.getFPRECHED_CHQNO());
				values.put(DatabaseHelper.FPRECHED_CHQDATE, recHed.getFPRECHED_CHQDATE());
				values.put(DatabaseHelper.TXNDATE, recHed.getFPRECHED_TXNDATE());
				values.put(DatabaseHelper.FPRECHED_CURCODE, recHed.getFPRECHED_CURCODE());
				values.put(DatabaseHelper.FPRECHED_CURRATE1, "");
				values.put(DatabaseHelper.FPRECHED_DEBCODE, recHed.getFPRECHED_DEBCODE());
				values.put(DatabaseHelper.FPRECHED_TOTALAMT, recHed.getFPRECHED_TOTALAMT());
				values.put(DatabaseHelper.FPRECHED_BTOTALAMT, recHed.getFPRECHED_BTOTALAMT());
				values.put(DatabaseHelper.FPRECHED_PAYTYPE, recHed.getFPRECHED_PAYTYPE());
				values.put(DatabaseHelper.FPRECHED_PRTCOPY, "");
				values.put(DatabaseHelper.FPRECHED_REMARKS, recHed.getFPRECHED_REMARKS());
				values.put(DatabaseHelper.FPRECHED_ADDUSER, recHed.getFPRECHED_ADDUSER());
				values.put(DatabaseHelper.FPRECHED_ADDMACH, recHed.getFPRECHED_ADDMACH());
				values.put(DatabaseHelper.FPRECHED_ADDDATE, recHed.getFPRECHED_ADDDATE());
				values.put(DatabaseHelper.FPRECHED_RECORDID, "");
				values.put(DatabaseHelper.FPRECHED_TIMESTAMP, "");
				values.put(DatabaseHelper.FPRECHED_CURRATE, recHed.getFPRECHED_CURRATE());
				values.put(DatabaseHelper.FPRECHED_CUSBANK, recHed.getFPRECHED_CUSBANK());
				values.put(DatabaseHelper.FPRECHED_LONGITUDE, recHed.getFPRECHED_LONGITUDE());
				values.put(DatabaseHelper.FPRECHED_LATITUDE, recHed.getFPRECHED_LATITUDE());
				values.put(DatabaseHelper.FPRECHED_ADDRESS, recHed.getFPRECHED_ADDRESS());
				values.put(DatabaseHelper.FPRECHED_START_TIME, recHed.getFPRECHED_START_TIME());
				values.put(DatabaseHelper.FPRECHED_END_TIME, recHed.getFPRECHED_END_TIME());
				values.put(DatabaseHelper.FPRECHED_ISSYNCED, recHed.getFPRECHED_ISSYNCED());
				values.put(DatabaseHelper.FPRECHED_ISACTIVE, recHed.getFPRECHED_ISACTIVE());
				values.put(DatabaseHelper.FPRECHED_ISDELETE, recHed.getFPRECHED_ISDELETE());
				values.put(DatabaseHelper.FPRECHED_BANKCODE, recHed.getFPRECHED_BANKCODE());
				values.put(DatabaseHelper.FPRECHED_BRANCHCODE, recHed.getFPRECHED_BRANCHCODE());
				//	values.put(DatabaseHelper.FPRECHED_ADDUSER_NEW, recHed.getFPRECHED_ADDUSER_NEW());

				int cn = cursor.getCount();
				if (cn > 0) {
					count = dB.update(DatabaseHelper.TABLE_FPRECHEDS, values, DatabaseHelper.REFNO + " =?",
							new String[] { String.valueOf(recHed.getFPRECHED_REFNO()) });
				} else {
					count = (int) dB.insert(DatabaseHelper.TABLE_FPRECHEDS, null, values);
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

	/*
	 * create for single receipt
	 */
	public ArrayList<ReceiptHed> getAllCompletedRecHedS(String refno) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
		String selectQuery;

		try {
			if (refno.equals("")) {

				selectQuery = "select * from " + DatabaseHelper.TABLE_FPRECHEDS + " Where "
						+ DatabaseHelper.FPRECHED_ISDELETE + "='0' and "+ DatabaseHelper.FPRECHED_ISACTIVE
						+ "='0' Order by "+DatabaseHelper.FPRECHED_ISSYNCED + "," +DatabaseHelper.REFNO + " DESC";

			} else {

				selectQuery = "select * from " + DatabaseHelper.TABLE_FPRECHEDS + " Where "
						+ DatabaseHelper.FPRECHED_ISDELETE + "='0' and "+ DatabaseHelper.FPRECHED_ISACTIVE + "='0' and " + DatabaseHelper.REFNO + "='" + refno
						+ "' Order by "+DatabaseHelper.FPRECHED_ISSYNCED + "," +DatabaseHelper.REFNO + " DESC";
			}

			Cursor cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				ReceiptHed recHed = new ReceiptHed();

				recHed.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDDATE)));
				recHed.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDMACH)));
				recHed.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDRESS)));
				recHed.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDUSER)));
				recHed.setFPRECHED_BANKCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BANKCODE)));
				recHed.setFPRECHED_BRANCHCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BRANCHCODE)));
				recHed.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BTOTALAMT)));
				recHed.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CHQDATE)));
				recHed.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CHQNO)));
				recHed.setFPRECHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_COST_CODE)));
				recHed.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURCODE)));
				recHed.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURRATE)));
				recHed.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURRATE1)));
				recHed.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CUSBANK)));
				recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_DEBCODE)));
				recHed.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_END_TIME)));
				recHed.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ID)));
				recHed.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ISACTIVE)));
				recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ISSYNCED)));
				recHed.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_LATITUDE)));
				recHed.setFPRECHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_LONGITUDE)));
				recHed.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_MANUREF)));
				recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_PAYTYPE)));
				recHed.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_PRTCOPY)));
				recHed.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_RECORDID)));
				recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
				recHed.setFPRECHED_REFNO1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REFNO1)));
				recHed.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REMARKS)));
				recHed.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REPCODE)));
				recHed.setFPRECHED_SALEREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_SALEREFNO)));
				recHed.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_START_TIME)));
				recHed.setFPRECHED_TIMESTAMP(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TIMESTAMP)));
				recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TOTALAMT)));
				recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
				recHed.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TXNTYPE)));
				//	recHed.setFPRECHED_ADDUSER_NEW(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDUSER_NEW)));

				list.add(recHed);

			}
			cursor.close();
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.close();
		}

		return list;
	}

	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public ArrayList<ReceiptHed> getAllCompletedRecHed(String refno) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
		String selectQuery;

		try {
			if (refno.equals("")) {

				selectQuery = "select * from " + DatabaseHelper.TABLE_FPRECHED + " Where "
						+ DatabaseHelper.FPRECHED_ISDELETE + "='0' and "+ DatabaseHelper.FPRECHED_TOTALAMT
						+ ">'0'";

			} else {

				selectQuery = "select * from " + DatabaseHelper.TABLE_FPRECHED + " Where "
						+ DatabaseHelper.FPRECHED_ISDELETE + "='0' and " + DatabaseHelper.FPRECHED_ISACTIVE
						+ "='0' and " + DatabaseHelper.FPRECHED_TOTALAMT
						+ ">'0' and " + DatabaseHelper.REFNO + "='" + refno + "'";
			}

			Cursor cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				ReceiptHed recHed = new ReceiptHed();

				recHed.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDDATE)));
				recHed.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDMACH)));
				recHed.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDRESS)));
				recHed.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDUSER)));
				recHed.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BTOTALAMT)));
				recHed.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CHQDATE)));
				recHed.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CHQNO)));
				recHed.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURCODE)));
				recHed.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURRATE)));
				recHed.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURRATE1)));
				recHed.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CUSBANK)));
				recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_DEBCODE)));
				recHed.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_END_TIME)));
				recHed.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ID)));
				recHed.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ISACTIVE)));
				recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ISSYNCED)));
				recHed.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_LATITUDE)));
				recHed.setFPRECHED_LONGITUDE(
						cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_LONGITUDE)));
				recHed.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_MANUREF)));
				recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_PAYTYPE)));
				recHed.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_PRTCOPY)));
				recHed.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_RECORDID)));
				recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
				recHed.setFPRECHED_REFNO1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REFNO1)));
				recHed.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REMARKS)));
				recHed.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REPCODE)));
				recHed.setFPRECHED_SALEREFNO(
						cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_SALEREFNO)));
				recHed.setFPRECHED_START_TIME(
						cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_START_TIME)));
				recHed.setFPRECHED_TIMESTAMP(
						cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TIMESTAMP)));
				recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TOTALAMT)));
				recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
				recHed.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TXNTYPE)));
				recHed.setFPRECHED_ISDELETE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ISDELETE)));

				list.add(recHed);

			}
			cursor.close();
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.close();
		}

		return list;
	}

	public ReceiptHed getReceiptByRefno(String refno) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery;

		ReceiptHed recHed = new ReceiptHed();
		try {

			selectQuery = "select * from " + DatabaseHelper.TABLE_FPRECHEDS + " Where " + DatabaseHelper.REFNO
					+ "='" + refno + "'";

			Cursor cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				recHed.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDDATE)));
				recHed.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDMACH)));
				recHed.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDRESS)));
				recHed.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDUSER)));
				recHed.setFPRECHED_BANKCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BANKCODE)));
				recHed.setFPRECHED_BRANCHCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BRANCHCODE)));
				recHed.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BTOTALAMT)));
				recHed.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CHQDATE)));
				recHed.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CHQNO)));
				recHed.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURCODE)));
				recHed.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURRATE)));
				recHed.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURRATE1)));
				recHed.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CUSBANK)));
				recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_DEBCODE)));
				recHed.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_END_TIME)));
				recHed.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ID)));
				recHed.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ISACTIVE)));
				recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ISSYNCED)));
				recHed.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_LATITUDE)));
				recHed.setFPRECHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_LONGITUDE)));
				recHed.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_MANUREF)));
				recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_PAYTYPE)));
				recHed.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_PRTCOPY)));
				recHed.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_RECORDID)));
				recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
				recHed.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REMARKS)));
				recHed.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REPCODE)));
				recHed.setFPRECHED_SALEREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_SALEREFNO)));
				recHed.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_START_TIME)));
				recHed.setFPRECHED_TIMESTAMP(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TIMESTAMP)));
				recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TOTALAMT)));
				recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
				recHed.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TXNTYPE)));
				//	recHed.setFPRECHED_ADDUSER_NEW(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDUSER_NEW)));

			}
			cursor.close();
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.close();
		}

		return recHed;
	}

	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public int CancelReceipt(String Refno) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		int result = 0;
		try {
			result = dB.delete(DatabaseHelper.TABLE_FPRECHED, DatabaseHelper.REFNO + "=?",
					new String[] { Refno });

		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.close();
		}

		return result;
	}

	public int CancelReceiptS(String Refno) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		int result = 0;
		try {
			result = dB.delete(DatabaseHelper.TABLE_FPRECHEDS, DatabaseHelper.REFNO + "=?",
					new String[] { Refno });

		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.close();
		}

		return result;
	}

	/*-*-*-*-*-*-**-*-*--*-*-*-*-*-*-*--**-*-*-*-*-*---*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public int InactiveStatusUpdate(String refno) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECHEDS + " WHERE "
					+ DatabaseHelper.REFNO + " = '" + refno + "'";

			cursor = dB.rawQuery(selectQuery, null);

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.FPRECHED_ISACTIVE, "0");

			int cn = cursor.getCount();

			if (cn > 0) {
				count = dB.update(DatabaseHelper.TABLE_FPRECHEDS, values, DatabaseHelper.REFNO + " =?",
						new String[] { String.valueOf(refno) });
			} else {
				count = (int) dB.insert(DatabaseHelper.TABLE_FPRECHEDS, null, values);
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

	public int InactiveStatusUpdateForMultiREceipt(String refno) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECHED + " WHERE "
					+ DatabaseHelper.FPRECHED_REFNO1 + " = '" + refno + "'";

			cursor = dB.rawQuery(selectQuery, null);

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.FPRECHED_ISACTIVE, "0");

			int cn = cursor.getCount();

			if (cn > 0) {
				count = dB.update(DatabaseHelper.TABLE_FPRECHED, values, DatabaseHelper.FPRECHED_REFNO1 + " =?",
						new String[] { String.valueOf(refno) });
			} else {
				count = (int) dB.insert(DatabaseHelper.TABLE_FPRECHED, null, values);
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

	/***********************************************************************/
	public int DeleteStatusUpdateForMultiREceipt(String refno) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECHED + " WHERE "
					+ DatabaseHelper.FPRECHED_REFNO1 + " = '" + refno + "'";

			cursor = dB.rawQuery(selectQuery, null);

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.FPRECHED_ISDELETE, "1");

			int cn = cursor.getCount();

			if (cn > 0) {
				count = dB.update(DatabaseHelper.TABLE_FPRECHED, values, DatabaseHelper.FPRECHED_REFNO1 + " =?",
						new String[] { String.valueOf(refno) });
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
	/*-*-*-*-*-*-**-*-*--*-*-*-*-*-*-*--**-*-*-*-*-*---*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public int DeleteStatusUpdateForEceipt(String refno) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECHEDS + " WHERE "
					+ DatabaseHelper.REFNO + " = '" + refno + "'";

			cursor = dB.rawQuery(selectQuery, null);

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.FPRECHED_ISDELETE, "1");

			int cn = cursor.getCount();

			if (cn > 0) {
				count = dB.update(DatabaseHelper.TABLE_FPRECHEDS, values, DatabaseHelper.REFNO + " =?",
						new String[] { String.valueOf(refno) });
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
	/********************************************************************************/
	public void UpdateRecHedForMultiReceipt(ReceiptHed recHed, String Refno) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ContentValues values = new ContentValues();

		try {
			values.put(DatabaseHelper.FPRECHED_LATITUDE, recHed.getFPRECHED_LATITUDE());
			values.put(DatabaseHelper.FPRECHED_LONGITUDE, recHed.getFPRECHED_LONGITUDE());
			values.put(DatabaseHelper.FPRECHED_START_TIME, recHed.getFPRECHED_START_TIME());
			values.put(DatabaseHelper.FPRECHED_END_TIME, recHed.getFPRECHED_END_TIME());
			values.put(DatabaseHelper.FPRECHED_ADDRESS, recHed.getFPRECHED_ADDRESS());

			dB.update(DatabaseHelper.TABLE_FPRECHED, values, DatabaseHelper.FPRECHED_REFNO1 + " =?",
					new String[] { String.valueOf(Refno) });

		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.close();
		}
	}

	public void UpdateRecHed(ReceiptHed recHed, String Refno) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ContentValues values = new ContentValues();

		try {
			values.put(DatabaseHelper.FPRECHED_LATITUDE, recHed.getFPRECHED_LATITUDE());
			values.put(DatabaseHelper.FPRECHED_LONGITUDE, recHed.getFPRECHED_LONGITUDE());
			values.put(DatabaseHelper.FPRECHED_START_TIME, recHed.getFPRECHED_START_TIME());
			values.put(DatabaseHelper.FPRECHED_END_TIME, recHed.getFPRECHED_END_TIME());
			values.put(DatabaseHelper.FPRECHED_ADDRESS, recHed.getFPRECHED_ADDRESS());
			values.put(DatabaseHelper.FPRECHED_COST_CODE, recHed.getFPRECHED_COSTCODE());

			dB.update(DatabaseHelper.TABLE_FPRECHEDS, values, DatabaseHelper.REFNO + " =?",
					new String[] { String.valueOf(Refno) });

		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.close();
		}
	}


	/*-*-*-*-*-*-**-*-*--*-*-*-*-*-*-*--**-*-*-*-*-*---*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


	/**
	 *
	 * @return RefNo2ForRecDet
	 */

	@SuppressWarnings("static-access")
	public String getFPRECHED_REFNO2ForRecDet(String code, String status) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FPRECHED + " WHERE " + DatabaseHelper.FPRECHED_DEBCODE
				+ " = '" + code + "' AND " + DatabaseHelper.FPRECHED_ISACTIVE + " = '" + status + "'";

		Cursor cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {

			return cursor.getString(cursor.getColumnIndex(dbHelper.REFNO));

		}

		return "";
	}

	@SuppressWarnings("static-access")
	public String getChequeDate(String refno) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FPRECHEDS + " WHERE " + DatabaseHelper.REFNO
				+ " = '" + refno + "'";

		Cursor cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {

			return cursor.getString(cursor.getColumnIndex(dbHelper.FPRECHED_CHQDATE));

		}

		return "";
	}

	@SuppressWarnings("static-access")
	public String getChequeNo(String refno) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FPRECHEDS + " WHERE " + DatabaseHelper.REFNO
				+ " = '" + refno + "'";

		Cursor cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {

			return cursor.getString(cursor.getColumnIndex(dbHelper.FPRECHED_CHQNO));

		}

		return "";
	}

	public int getHeaderCountForNnumVal(String refno) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECHED + " WHERE "
					+ DatabaseHelper.FPRECDET_REFNO1 + " = '" + refno + "'";

			cursor = dB.rawQuery(selectQuery, null);

			count = cursor.getCount();

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
	public int deleteData(String refno) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FPRECHED + " WHERE " + dbHelper.FPRECDET_REFNO1
					+ " = '" + refno + "'";
			cursor = dB.rawQuery(selectQuery, null);
			int cn = cursor.getCount();

			if (cn > 0) {
				int success = dB.delete(dbHelper.TABLE_FPRECHED, dbHelper.FPRECDET_REFNO1 + " ='" + refno + "'", null);
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

	public void UpdateRecHeadTotalAmount(String refNo) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {
			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECHED + " WHERE "
					+ DatabaseHelper.FPRECHED_REFNO1 + " = '" + refNo + "'";

			cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {
				String refno = cursor.getString(cursor.getColumnIndex(dbHelper.REFNO));

				String query = "Update " + DatabaseHelper.TABLE_FPRECHED
						+ " set TotalAmt = (select sum(AloAmt) Aloamt from fprecdet where refno='" + refno
						+ "' ) where refno='" + refno + "'";
				dB.execSQL(query);
			}


		} catch (Exception e) {
			Log.v(TAG + " Exception", e.toString());
		} finally {
			dB.close();
		}

	}

	public int DeleteHedUnnessary() {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		int result = 0;
		int result2 = 0;
		try {
			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECHED + " WHERE totalamt is null ";


			cursor = dB.rawQuery(selectQuery, null);

			cursor .moveToFirst();
			//int raws = cursor.getCount();

			//cursor.

			while(!cursor.isAfterLast()) {

				String refno = cursor.getString(cursor.getColumnIndex(dbHelper.REFNO));
				result = dB.delete(DatabaseHelper.TABLE_FPRECHED, DatabaseHelper.REFNO + "=?",
						new String[] { refno });

				result2=result2+1;

				cursor.moveToNext();
			}

//			while (cursor.moveToNext()) {
//				String refno = cursor.getString(cursor.getColumnIndex(dbHelper.REFNO));
//				
//				result = dB.delete(DatabaseHelper.TABLE_FPRECHED, DatabaseHelper.REFNO + "=?",
//						new String[] { refno });
//				result2=result2+1;
//				//String query = "Delete FROM " + DatabaseHelper.TABLE_FPRECHED + " where refno='" + refno + "'";
//				//dB.execSQL(query);
//			}


		}
//		try {
//
//				String query = "Delete from " + DatabaseHelper.TABLE_FPRECHED+ " where totalamt is null ";
//				dB.execSQL(query);						
//
//		}
		catch (Exception e) {
			Log.v(TAG + " Exception", e.toString());
		} finally {
			dB.close();
		}
		return result2;
	}

	public int updateIsSyncedReceipt(ReceiptHed mapper) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {
			ContentValues values = new ContentValues();

			values.put(dbHelper.FPRECHED_ISSYNCED, "1");

			if (mapper.getFPRECHED_ISSYNCED().equals("1")) {
				count = dB.update(dbHelper.TABLE_FPRECHED, values, dbHelper.REFNO + " =?",
						new String[] { String.valueOf(mapper.getFPRECHED_REFNO()) });
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

	public ArrayList<ReceiptHed> getAllUnsyncedRecHed() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
		String selectQuery;

		try {
			selectQuery = "select * from " + DatabaseHelper.TABLE_FPRECHED + " Where "
					+ DatabaseHelper.FPRECHED_ISACTIVE + "='0' and " + DatabaseHelper.FPRECHED_ISSYNCED + "='0' and "
					+ DatabaseHelper.FPRECHED_ISDELETE + "='0'";

			localSP = context.getSharedPreferences(SETTINGS,
					0);

			Cursor cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				ReceiptHed mapper = new ReceiptHed();

				mapper.setNextNumVal(new ReferenceController(context)
						.getCurrentNextNumVal(context.getResources().getString(R.string.ReceiptNumVal)));

				mapper.setDistDB(localSP.getString("Dist_DB", "").toString());
				mapper.setConsoleDB(SharedPref.getInstance(context).getDatabase());

//				mapper.setSALEREP_DEALCODE(new SalRepDS(context).getDealCode());
//				mapper.setSALEREP_AREACODE(new SalRepDS(context).getAreaCode());

				mapper.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDDATE)));
				mapper.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDMACH)));
				mapper.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDRESS)));
				mapper.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ADDUSER)));
				mapper.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BTOTALAMT)));
				mapper.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CHQDATE)));
				mapper.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CHQNO)));
				mapper.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURCODE)));
				mapper.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURRATE)));
				mapper.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CURRATE1)));
				mapper.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_CUSBANK)));
				mapper.setFPRECHED_COSTCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_COST_CODE)));
				mapper.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_DEBCODE)));
				mapper.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_END_TIME)));
				mapper.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ID)));
				mapper.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ISACTIVE)));
				mapper.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_ISSYNCED)));
				mapper.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_LATITUDE)));
				mapper.setFPRECHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_LONGITUDE)));
				mapper.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_MANUREF)));
				mapper.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_PAYTYPE)));
				mapper.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_PRTCOPY)));
				mapper.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_RECORDID)));
				mapper.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO)));
				mapper.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REMARKS)));
				mapper.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REPCODE)));
				mapper.setFPRECHED_SALEREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_SALEREFNO)));
				mapper.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_START_TIME)));
				mapper.setFPRECHED_TIMESTAMP(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TIMESTAMP)));
				mapper.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TOTALAMT)));
				mapper.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TXNDATE)));
				mapper.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_TXNTYPE)));
				mapper.setFPRECHED_BANKCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BANKCODE)));
				mapper.setFPRECHED_BRANCHCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_BRANCHCODE)));

				mapper.setRecDetList(new ReceiptDetController(context).GetReceiptByRefno(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFNO))));

				list.add(mapper);

			}
			cursor.close();
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.close();
		}

		return list;
	}
}
