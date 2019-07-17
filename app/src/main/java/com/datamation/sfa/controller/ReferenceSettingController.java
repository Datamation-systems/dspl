package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.sfa.model.CompanySetting;
import com.datamation.sfa.model.RefSetting;
import com.datamation.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;

public class ReferenceSettingController {
	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "SettingsCode";
	
	public ReferenceSettingController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	@SuppressWarnings("static-access")
	public int createOrUpdateFCompanySetting(ArrayList<CompanySetting> list) {
		int count = 0;
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {

			for (CompanySetting setting : list) {

				Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FCOMPANYSETTING + " WHERE " + DatabaseHelper.FCOMPANYBRANCH_CSETTINGS_CODE + "='" + setting.getFCOMPANYSETTING_SETTINGS_CODE() + "'", null);

				ContentValues values = new ContentValues();

				values.put(DatabaseHelper.FCOMPANYSETTING_SETTINGS_CODE, setting.getFCOMPANYSETTING_SETTINGS_CODE());
				values.put(DatabaseHelper.FCOMPANYSETTING_GRP, setting.getFCOMPANYSETTING_GRP());
				values.put(DatabaseHelper.FCOMPANYSETTING_LOCATION_CHAR, setting.getFCOMPANYSETTING_LOCATION_CHAR());
				values.put(DatabaseHelper.FCOMPANYSETTING_CHAR_VAL, setting.getFCOMPANYSETTING_CHAR_VAL());
				values.put(DatabaseHelper.FCOMPANYSETTING_NUM_VAL, setting.getFCOMPANYSETTING_NUM_VAL());
				values.put(DatabaseHelper.FCOMPANYSETTING_REMARKS, setting.getFCOMPANYSETTING_REMARKS());
				values.put(DatabaseHelper.FCOMPANYSETTING_TYPE, setting.getFCOMPANYSETTING_TYPE());
				values.put(DatabaseHelper.FCOMPANYSETTING_COMPANY_CODE, setting.getFCOMPANYSETTING_COMPANY_CODE());

				if (cursor.getCount() > 0) {
					dB.update(DatabaseHelper.TABLE_FCOMPANYSETTING, values, DatabaseHelper.FCOMPANYBRANCH_CSETTINGS_CODE + "=?", new String[]{setting.getFCOMPANYSETTING_SETTINGS_CODE().toString()});
					Log.v(TAG, "Updated");

				} else {
					count = (int) dB.insert(DatabaseHelper.TABLE_FCOMPANYSETTING, null, values);
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

	public int deleteAll() {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {

			cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FCOMPANYSETTING, null);
			count = cursor.getCount();
			if (count > 0) {
				int success = dB.delete(dbHelper.TABLE_FCOMPANYSETTING, null, null);
				Log.v("Success", success + "");
			}
		} catch (Exception e) {

			Log.v(" Exception", e.toString());

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			dB.close();
		}

		return count;

	}
}
