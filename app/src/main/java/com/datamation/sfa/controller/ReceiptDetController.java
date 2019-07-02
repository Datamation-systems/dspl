package com.datamation.sfa.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.ReceiptDet;

public class ReceiptDetController {

	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "Kandana";

	public ReceiptDetController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}
	//for new sfa by rashmi

	public ArrayList<ReceiptDet> getTodayPayments() {
		int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
		int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		ArrayList<ReceiptDet> list = new ArrayList<ReceiptDet>();

		try {
			String selectQuery = "select hed.refno, det.refno1, hed.paytype,det.aloamt, fddb.totbal, det.dtxndate from fprecheds hed, fprecdets det," +
		//			" fddbnote fddb where hed.refno = det.refno and det.refno1 = fddb.refno and hed.txndate = '2019-04-12'";
					" fddbnote fddb where hed.refno = det.refno and det.refno1 = fddb.refno and hed.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

			cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				ReceiptDet recDet = new ReceiptDet();

//
				recDet.setFPRECDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_REFNO)));
				recDet.setFPRECDET_REFNO1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REFNO1)));
				recDet.setFPRECDET_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECHED_PAYTYPE)));
				recDet.setFPRECDET_ALOAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_ALOAMT)));
				recDet.setFPRECDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDDBNOTE_TOT_BAL)));
				recDet.setFPRECDET_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_DTXNDATE)));

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

	//------------------------------------------------------------old functions
	public int createOrUpdateRecDet(ArrayList<ReceiptDet> list) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			for (ReceiptDet recDet : list) {

				String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECDET + " WHERE " + DatabaseHelper.FPRECDET_ID + " = '" + recDet.getFPRECDET_ID() + "'";

				cursor = dB.rawQuery(selectQuery, null);

				ContentValues values = new ContentValues();

				values.put(DatabaseHelper.FPRECDET_ALOAMT, recDet.getFPRECDET_ALOAMT());
				values.put(DatabaseHelper.FPRECDET_AMT, recDet.getFPRECDET_AMT());
				values.put(DatabaseHelper.FPRECDET_BAMT, recDet.getFPRECDET_BAMT());
				values.put(DatabaseHelper.FPRECDET_DCURCODE, recDet.getFPRECDET_DCURCODE());
				values.put(DatabaseHelper.FPRECDET_DCURRATE, recDet.getFPRECDET_DCURRATE());
				values.put(DatabaseHelper.FPRECDET_DTXNDATE, recDet.getFPRECDET_DTXNDATE());
				values.put(DatabaseHelper.FPRECDET_DTXNTYPE, recDet.getFPRECDET_DTXNTYPE());
				values.put(DatabaseHelper.FPRECDET_MANUREF, recDet.getFPRECDET_MANUREF());
				values.put(DatabaseHelper.FPRECDET_OCURRATE, recDet.getFPRECDET_OCURRATE());
				values.put(DatabaseHelper.FPRECDET_OVPAYAMT, recDet.getFPRECDET_OVPAYAMT());
				values.put(DatabaseHelper.FPRECDET_OVPAYBAL, recDet.getFPRECDET_OVPAYBAL());
				values.put(DatabaseHelper.FPRECDET_RECORDID, recDet.getFPRECDET_RECORDID());
				values.put(DatabaseHelper.FPRECDET_REFNO, recDet.getFPRECDET_REFNO());
				values.put(DatabaseHelper.FPRECDET_REFNO1, recDet.getFPRECDET_REFNO1());
				values.put(DatabaseHelper.FPRECDET_REPCODE, recDet.getFPRECDET_REPCODE());
				values.put(DatabaseHelper.FPRECDET_SALEREFNO, recDet.getFPRECDET_SALEREFNO());
				values.put(DatabaseHelper.FPRECDET_TIMESTAMP, recDet.getFPRECDET_TIMESTAMP());
				values.put(DatabaseHelper.FPRECDET_TXNDATE, recDet.getFPRECDET_TXNDATE());
				values.put(DatabaseHelper.FPRECDET_TXNTYPE, recDet.getFPRECDET_TXNTYPE());
				values.put(DatabaseHelper.FPRECDET_DEBCODE, recDet.getFPRECDET_DEBCODE());
				values.put(DatabaseHelper.FPRECDET_REFNO2, recDet.getFPRECDET_REFNO2());
				values.put(DatabaseHelper.FPRECDET_ISDELETE, recDet.getFPRECDET_ISDELETE());
				values.put(DatabaseHelper.FPRECDET_REMARK, recDet.getFPRECDET_REMARK());
				
				int cn = cursor.getCount();
				if (cn > 0) {
					count = dB.update(DatabaseHelper.TABLE_FPRECDET, values, DatabaseHelper.FPRECDET_ID + " =?", new String[] { String.valueOf(recDet.getFPRECDET_ID()) });
				} else {
					count = (int) dB.insert(DatabaseHelper.TABLE_FPRECDET, null, values);
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
	create for single receipt save
	 */


	public int createOrUpdateRecDetS(ArrayList<ReceiptDet> list) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			for (ReceiptDet recDet : list) {

				String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECDETS + " WHERE " + DatabaseHelper.FPRECDET_ID + " = '" + recDet.getFPRECDET_ID() + "'";

				cursor = dB.rawQuery(selectQuery, null);

				ContentValues values = new ContentValues();

				values.put(DatabaseHelper.FPRECDET_ALOAMT, recDet.getFPRECDET_ALOAMT());
				values.put(DatabaseHelper.FPRECDET_AMT, recDet.getFPRECDET_AMT());
				values.put(DatabaseHelper.FPRECDET_BAMT, recDet.getFPRECDET_BAMT());
				values.put(DatabaseHelper.FPRECDET_DCURCODE, recDet.getFPRECDET_DCURCODE());
				values.put(DatabaseHelper.FPRECDET_DCURRATE, recDet.getFPRECDET_DCURRATE());
				values.put(DatabaseHelper.FPRECDET_DTXNDATE, recDet.getFPRECDET_DTXNDATE());
				values.put(DatabaseHelper.FPRECDET_DTXNTYPE, recDet.getFPRECDET_DTXNTYPE());
				values.put(DatabaseHelper.FPRECDET_MANUREF, recDet.getFPRECDET_MANUREF());
				values.put(DatabaseHelper.FPRECDET_OCURRATE, recDet.getFPRECDET_OCURRATE());
				values.put(DatabaseHelper.FPRECDET_OVPAYAMT, recDet.getFPRECDET_OVPAYAMT());
				values.put(DatabaseHelper.FPRECDET_OVPAYBAL, recDet.getFPRECDET_OVPAYBAL());
				values.put(DatabaseHelper.FPRECDET_RECORDID, recDet.getFPRECDET_RECORDID());
				values.put(DatabaseHelper.FPRECDET_REFNO, recDet.getFPRECDET_REFNO());
				values.put(DatabaseHelper.FPRECDET_REFNO1, recDet.getFPRECDET_REFNO1());
				values.put(DatabaseHelper.FPRECDET_REPCODE, recDet.getFPRECDET_REPCODE());
				values.put(DatabaseHelper.FPRECDET_SALEREFNO, recDet.getFPRECDET_SALEREFNO());
				values.put(DatabaseHelper.FPRECDET_TIMESTAMP, recDet.getFPRECDET_TIMESTAMP());
				values.put(DatabaseHelper.FPRECDET_TXNDATE, recDet.getFPRECDET_TXNDATE());
				values.put(DatabaseHelper.FPRECDET_TXNTYPE, recDet.getFPRECDET_TXNTYPE());
				values.put(DatabaseHelper.FPRECDET_DEBCODE, recDet.getFPRECDET_DEBCODE());
				values.put(DatabaseHelper.FPRECDET_REFNO2, recDet.getFPRECDET_REFNO2());
				values.put(DatabaseHelper.FPRECDET_ISDELETE, recDet.getFPRECDET_ISDELETE());
				values.put(DatabaseHelper.FPRECDET_REMARK, recDet.getFPRECDET_REMARK());
				int cn = cursor.getCount();
				if (cn > 0) {
					count = dB.update(DatabaseHelper.TABLE_FPRECDETS, values, DatabaseHelper.FPRECDET_ID + " =?", new String[] { String.valueOf(recDet.getFPRECDET_ID()) });
				} else {
					count = (int) dB.insert(DatabaseHelper.TABLE_FPRECDETS, null, values);
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
	
	/*-----------------------------------------------------------------------------------*/
	
	//only can use for single receipt
	public ArrayList<ReceiptDet> GetReceiptByRefno(String Refno) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		ArrayList<ReceiptDet> list = new ArrayList<ReceiptDet>();

		try {
			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECDETS + " WHERE " + DatabaseHelper.FPRECDET_REFNO + " = '" + Refno + "' and " 
					+ DatabaseHelper.FPRECHED_ISDELETE + "='0'";

			cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				ReceiptDet recDet = new ReceiptDet();

				recDet.setFPRECDET_ALOAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_ALOAMT)));
				recDet.setFPRECDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_AMT)));
				recDet.setFPRECDET_BAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_BAMT)));
				recDet.setFPRECDET_DCURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_DCURCODE)));
				recDet.setFPRECDET_DCURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_DCURRATE)));
				recDet.setFPRECDET_DTXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_DTXNDATE)));
				recDet.setFPRECDET_DTXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_DTXNTYPE)));
				recDet.setFPRECDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_ID)));
				recDet.setFPRECDET_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_MANUREF)));
				recDet.setFPRECDET_OCURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_OCURRATE)));
				recDet.setFPRECDET_OVPAYAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_OVPAYAMT)));
				recDet.setFPRECDET_OVPAYBAL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_OVPAYBAL)));
				recDet.setFPRECDET_RECORDID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_RECORDID)));
				recDet.setFPRECDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REFNO)));
				recDet.setFPRECDET_REFNO1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REFNO1)));
				recDet.setFPRECDET_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REPCODE)));
				recDet.setFPRECDET_SALEREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_SALEREFNO)));
				recDet.setFPRECDET_TIMESTAMP(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_TIMESTAMP)));
				recDet.setFPRECDET_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_TXNDATE)));
				recDet.setFPRECDET_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_TXNTYPE)));
				recDet.setFPRECDET_REMARK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REMARK)));
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
	
	public ArrayList<ReceiptDet> GetMReceiptByRefno(String Refno) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		ArrayList<ReceiptDet> list = new ArrayList<ReceiptDet>();

		try {
			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FPRECDET + " WHERE " + DatabaseHelper.FPRECDET_REFNO + " = '" + Refno + "'";

			cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				ReceiptDet recDet = new ReceiptDet();

				recDet.setFPRECDET_ALOAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_ALOAMT)));
				recDet.setFPRECDET_AMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_AMT)));
				recDet.setFPRECDET_BAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_BAMT)));
				recDet.setFPRECDET_DCURCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_DCURCODE)));
				recDet.setFPRECDET_DCURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_DCURRATE)));
				recDet.setFPRECDET_DTXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_DTXNDATE)));
				recDet.setFPRECDET_DTXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_DTXNTYPE)));
				recDet.setFPRECDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_ID)));
				recDet.setFPRECDET_MANUREF(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_MANUREF)));
				recDet.setFPRECDET_OCURRATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_OCURRATE)));
				recDet.setFPRECDET_OVPAYAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_OVPAYAMT)));
				recDet.setFPRECDET_OVPAYBAL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_OVPAYBAL)));
				recDet.setFPRECDET_RECORDID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_RECORDID)));
				recDet.setFPRECDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REFNO)));
				recDet.setFPRECDET_REFNO1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REFNO1)));
				recDet.setFPRECDET_REFNO2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REFNO2)));
				recDet.setFPRECDET_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REPCODE)));
				recDet.setFPRECDET_SALEREFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_SALEREFNO)));
				recDet.setFPRECDET_TIMESTAMP(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_TIMESTAMP)));
				recDet.setFPRECDET_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_TXNDATE)));
				recDet.setFPRECDET_TXNTYPE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_TXNTYPE)));
				recDet.setFPRECDET_REMARK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRECDET_REMARK)));
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
	@SuppressWarnings("static-access")
	public int restDataForMR(String refno) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FPRECDET + " WHERE " + dbHelper.FPRECDET_REFNO2 + " = '" + refno + "'";
			cursor = dB.rawQuery(selectQuery, null);
			int cn = cursor.getCount();

			if (cn > 0) {
				count = dB.delete(dbHelper.TABLE_FPRECDET, dbHelper.FPRECDET_REFNO2 + " ='" + refno + "'", null);
				Log.v("Success Stauts", count + "");
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
	public int UpdateDeleteStatusMR(String refno) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FPRECDET + " WHERE " + dbHelper.FPRECDET_REFNO2 + " = '" + refno + "'";
			cursor = dB.rawQuery(selectQuery, null);
			

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.FPRECDET_ISDELETE, "1");
			
			int cn = cursor.getCount();

			if (cn > 0) {
				count = dB.update(dbHelper.TABLE_FPRECDET, values,dbHelper.FPRECDET_REFNO2 + " ='" + refno + "'", null);
				Log.v("Success Stauts", count + "");
			}
			else {
				count = (int) dB.insert(DatabaseHelper.TABLE_FPRECDET, null, values);
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
	public int UpdateDeleteStatus(String refno) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FPRECDETS + " WHERE " + dbHelper.FPRECDET_REFNO + " = '" + refno + "'";
			cursor = dB.rawQuery(selectQuery, null);
			

			ContentValues values = new ContentValues();

			values.put(DatabaseHelper.FPRECDET_ISDELETE, "1");
			
			int cn = cursor.getCount();

			if (cn > 0) {
				count = dB.update(dbHelper.TABLE_FPRECDETS, values,dbHelper.FPRECDET_REFNO + " ='" + refno + "'", null);
				Log.v("Success Stauts", count + "");
			}
			else {
				count = (int) dB.insert(DatabaseHelper.TABLE_FPRECDETS, null, values);
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
	public String getTotal(String refNo) {

		String sum = null;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;

		try {

			String selectQuery = "SELECT SUM("+DatabaseHelper.FPRECDET_ALOAMT+") FROM " + DatabaseHelper.TABLE_FPRECDET + " WHERE "
					+ DatabaseHelper.FPRECDET_REFNO + " = '" + refNo + "'";

			cursor = dB.rawQuery(selectQuery, null);

			sum = cursor.getString(0);

		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			dB.close();
		}
		return sum;

	}
	
}
