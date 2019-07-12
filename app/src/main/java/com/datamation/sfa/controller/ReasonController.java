package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.sfa.model.Reason;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Reason;

import java.util.ArrayList;

public class ReasonController {

	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "ReasonController";

	public ReasonController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	/*
	 * insert code
	 */
	@SuppressWarnings("static-access")
	public int createOrUpdateReason(ArrayList<Reason> list) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		Cursor cursor_ini = null;
		try {

			cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FREASON, null);

			for (Reason reason : list) {
				ContentValues values = new ContentValues();

				values.put(dbHelper.FREASON_NAME, reason.getReasonName());
				values.put(dbHelper.FREASON_CODE, reason.getReasonCode());
				values.put(dbHelper.FREASON_TYPE, reason.getReasonType());

				if (cursor_ini.moveToFirst()) {
					String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON + " WHERE " + dbHelper.FREASON_CODE + "='" + reason.getReasonCode() + "'";
					cursor = dB.rawQuery(selectQuery, null);

					if (cursor.moveToFirst()) {
						count = (int) dB.update(dbHelper.TABLE_FREASON, values, dbHelper.FREASON_CODE + "='" + reason.getReasonCode() + "'", null);
					} else {
						count = (int) dB.insert(dbHelper.TABLE_FREASON, null, values);
					}

				} else {
					count = (int) dB.insert(dbHelper.TABLE_FREASON, null, values);
				}

			}
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			
			if (cursor_ini != null) {
				cursor_ini.close();
			}
			dB.close();
		}
		return count;

	}

	/*
	 * delete code
	 */
	@SuppressWarnings("static-access")
	public int deleteAll() {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {

			cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FREASON, null);
			count = cursor.getCount();
			if (count > 0) {
				int success = dB.delete(dbHelper.TABLE_FREASON, null, null);
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
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public ArrayList<Reason> getAllReasonsByRtCode(String RTcode) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> list = new ArrayList<Reason>();

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON + " WHERE " + dbHelper.FREASON_REATCODE + "='" + RTcode + "'";

		Cursor cursor = dB.rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {

			Reason res = new Reason();

			res.setReasonCode(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_CODE)));
			res.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_NAME)));

			list.add(res);

		}

		return list;
	}
	public ArrayList<Reason> getAllReasons() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> list = new ArrayList<Reason>();

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON;

		Cursor cursor = dB.rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {

			Reason reason = new Reason();

			reason.setReasonCode(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_CODE)));
			reason.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_NAME)));

			list.add(reason);

		}

		return list;
	}
	public ArrayList<String> getReasonName() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<String> FREASON = new ArrayList<String>();

		//String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON + " WHERE " + dbHelper.FREASON_CODE + "='RT01' OR dbHelper.FREASON_CODE='RT02'";
		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON;

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);
		FREASON.add("Tap to select a Reason");

		while (cursor.moveToNext()) {

			FREASON.add(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_NAME)));

		}

		return FREASON;
	}

	public String getReaCodeByName(String name) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON + " WHERE " + dbHelper.FREASON_NAME + "='" + name + "'";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_CODE));
		}

		return "";
	}

	public ArrayList<Reason> getAllExpense(String excode) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> list = new ArrayList<Reason>();
		String selectQuery = null;
		if(excode.equals(""))
			selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON ;
		else
			selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON + " WHERE " + dbHelper.FREASON_CODE + "='" + excode + "'";

		Cursor cursor = dB.rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {

			Reason expense = new Reason();

			expense.setReasonCode(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_CODE)));
			expense.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_NAME)));

			list.add(expense);

		}

		return list;
	}


	public String getReasonByReaCode(String reaCode) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON + " WHERE " + dbHelper.FREASON_CODE + "='" + reaCode + "'";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_NAME));
		}

		return "";
	}

	public ArrayList<Reason> getAllNonPrdReasons() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> list = new ArrayList<Reason>();

//		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON + " WHERE " + dbHelper.FREASON_TYPE + "='np'";
		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON;

		Cursor cursor = dB.rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {

			Reason FREASON = new Reason();

			FREASON.setReasonCode(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_CODE)));
			FREASON.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_NAME)));

			list.add(FREASON);

		}

		return list;
	}

	public ArrayList<Reason> getDebDetails(String searchword) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> Itemname = new ArrayList<Reason>();

		String selectQuery = "select * from fReason where ReaTcode='RT02' AND ReaCode LIKE '%" + searchword + "%' OR ReaName LIKE '%" + searchword + "%' ";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {
			Reason items = new Reason();

			items.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_NAME)));
			items.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_CODE)));
			Itemname.add(items);
		}

		return Itemname;
	}

	@SuppressWarnings("static-access")
	public String getReaNameByCode(String code) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON + " WHERE " + dbHelper.FREASON_CODE + "='" + code + "'";

		Cursor cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {

			return cursor.getString(cursor.getColumnIndex(dbHelper.FREASON_NAME));

		}

		return "";
	}

}
