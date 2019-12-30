package com.datamation.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.util.Log;

import com.datamation.sfa.model.Customer;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.Debtor;
import com.datamation.sfa.model.FddbNote;
import com.datamation.sfa.model.HistoryDetail;
import com.datamation.sfa.model.Invoice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.datamation.sfa.helpers.DatabaseHelper.FDDBNOTE_DEB_CODE;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_ADD1;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_ADD2;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_CHK_CRD_LIMIT;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_CODE;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_CRD_LIMIT;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_CRD_PERIOD;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_EMAIL;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_MOB;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_NAME;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_PRILLCODE;
import static com.datamation.sfa.helpers.DatabaseHelper.FDEBTOR_STATUS;

public class CustomerController {

	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "CustomerController";

	public CustomerController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	public void InsertOrReplaceDebtor(ArrayList<Debtor> list) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {
			dB.beginTransactionNonExclusive();
				String sql = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_FDEBTOR + " (DebCode,DebName,DebAdd1,DebAdd2,DebAdd3,DebTele,DebMob,DebEMail,TownCode,AreaCode,DbGrCode,Status,CrdPeriod,ChkCrdPrd,CrdLimit,ChkCrdLmt,RepCode,PrillCode,TaxReg,RankCode,NIC,BisRegNo) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			SQLiteStatement stmt = dB.compileStatement(sql);

			for (Debtor debtor : list) {

				stmt.bindString(1, debtor.getFDEBTOR_CODE());
				stmt.bindString(2, debtor.getFDEBTOR_NAME());
				stmt.bindString(3, debtor.getFDEBTOR_ADD1());
				stmt.bindString(4, debtor.getFDEBTOR_ADD2());
				stmt.bindString(5, debtor.getFDEBTOR_ADD3());
				stmt.bindString(6, debtor.getFDEBTOR_TELE());
				stmt.bindString(7, debtor.getFDEBTOR_MOB());
				stmt.bindString(8, debtor.getFDEBTOR_EMAIL());
				stmt.bindString(9, debtor.getFDEBTOR_TOWN_CODE());
				stmt.bindString(10, debtor.getFDEBTOR_AREA_CODE());
				stmt.bindString(11, debtor.getFDEBTOR_DBGR_CODE());
				stmt.bindString(12, debtor.getFDEBTOR_STATUS());
				stmt.bindString(13, debtor.getFDEBTOR_CRD_PERIOD());
				stmt.bindString(14, debtor.getFDEBTOR_CHK_CRD_PRD());
				stmt.bindString(15, debtor.getFDEBTOR_CRD_LIMIT());
				stmt.bindString(16, debtor.getFDEBTOR_CHK_CRD_LIMIT());
				stmt.bindString(17, debtor.getFDEBTOR_REPCODE());
				stmt.bindString(18, debtor.getFDEBTOR_PRILLCODE());
				stmt.bindString(19, debtor.getFDEBTOR_TAX_REG());
				stmt.bindString(20, debtor.getFDEBTOR_RANK_CODE());
				stmt.bindString(21, debtor.getFDEBTOR_NIC());
				stmt.bindString(22, debtor.getFDEBTOR_BIS_REG());
				// stmt.bindString(15, debtor.getFDEBTOR_DEB_CLS_CODE());
				// stmt.bindString(10, debtor.getFDEBTOR_REM_DIS());
				// stmt.bindString(13, debtor.getFDEBTOR_DEB_CAT_CODE());
				// stmt.bindString(9, debtor.getFDEBTOR_CREATEDATE());
				// stmt.bindString(17, debtor.getFDEBTOR_LYLTY());
				// stmt.bindString(18, debtor.getFDEBTOR_DEAL_CODE());
				// stmt.bindString(19, debtor.getFDEBTOR_ADD_USER());
				// stmt.bindString(20, debtor.getFDEBTOR_ADD_DATE_DEB());
				// stmt.bindString(21, debtor.getFDEBTOR_ADD_MACH());
				// stmt.bindString(22, debtor.getFDEBTOR_RECORD_ID());
				// stmt.bindString(23, debtor.getFDEBTOR_TIME_STAMP());
				// stmt.bindString(30, debtor.getFDEBTOR_TRAN_DATE());
				// stmt.bindString(31, debtor.getFDEBTOR_TRAN_BATCH());
				// stmt.bindString(32, debtor.getFDEBTOR_SUMMARY());
				// stmt.bindString(33, debtor.getFDEBTOR_OUT_DIS());
				// stmt.bindString(34, debtor.getFDEBTOR_DEB_FAX());
				// stmt.bindString(35, debtor.getFDEBTOR_DEB_WEB());
				// stmt.bindString(36, debtor.getFDEBTOR_DEBCT_NAM());
				// stmt.bindString(37, debtor.getFDEBTOR_DEBCT_ADD1());
				// stmt.bindString(38, debtor.getFDEBTOR_DEBCT_ADD2());
				// stmt.bindString(39, debtor.getFDEBTOR_DEBCT_ADD3());
				// stmt.bindString(40, debtor.getFDEBTOR_DEBCT_TELE());
				// stmt.bindString(41, debtor.getFDEBTOR_DEBCT_FAX());
				// stmt.bindString(42, debtor.getFDEBTOR_DEBCT_EMAIL());
				// stmt.bindString(43, debtor.getFDEBTOR_DEL_PERSN());
				// stmt.bindString(44, debtor.getFDEBTOR_DEL_ADD1());
				// stmt.bindString(45, debtor.getFDEBTOR_DEL_ADD2());
				// stmt.bindString(46, debtor.getFDEBTOR_DEL_ADD3());
				// stmt.bindString(47, debtor.getFDEBTOR_DEL_TELE());
				// stmt.bindString(48, debtor.getFDEBTOR_DEL_FAX());
				// stmt.bindString(49, debtor.getFDEBTOR_DEL_EMAIL());
				// stmt.bindString(50, debtor.getFDEBTOR_DATE_OFB());
				// stmt.bindString(52, debtor.getFDEBTOR_CUSDISPER());
				// stmt.bindString(54, debtor.getFDEBTOR_CUSDISSTAT());
				// stmt.bindString(55, debtor.getFDEBTOR_BUS_RGNO());
				// stmt.bindString(56, debtor.getFDEBTOR_POSTCODE());
				// stmt.bindString(57, debtor.getFDEBTOR_GEN_REMARKS());
				// stmt.bindString(58, debtor.getFDEBTOR_BRANCODE());
				// stmt.bindString(59, debtor.getFDEBTOR_BANK());
				// stmt.bindString(60, debtor.getFDEBTOR_BRANCH());
				// stmt.bindString(61, debtor.getFDEBTOR_ACCTNO());
				// stmt.bindString(62, debtor.getFDEBTOR_CUS_VATNO());

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
	public ArrayList<FddbNote>getOutStandingList(String debCode)
	{

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<FddbNote> list = new ArrayList<FddbNote>();
		Cursor cursor = null;
		try {
			String selectQuery = "select * from " + dbHelper.TABLE_FDDBNOTE + " WHERE " + FDDBNOTE_DEB_CODE + "='"
					+ debCode + "'";

			cursor = dB.rawQuery(selectQuery, null);
			while (cursor.moveToNext()) {

				FddbNote fddbNote = new FddbNote();
//
				fddbNote.setRefNo(cursor.getString(cursor.getColumnIndex(dbHelper.REFNO)));
				fddbNote.setTxnDate(cursor.getString(cursor.getColumnIndex(dbHelper.TXNDATE)));
				fddbNote.setAmt(cursor.getString(cursor.getColumnIndex(dbHelper.FDDBNOTE_AMT)));

				list.add(fddbNote);

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

	public ArrayList<Customer> getAllCustomers() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Customer> list = new ArrayList<Customer>();
		Cursor cursor = null;
		try {
			String selectQuery = "select * from " + dbHelper.TABLE_FDEBTOR;

			cursor = dB.rawQuery(selectQuery, null);
			while (cursor.moveToNext()) {

				Customer customer = new Customer();

				customer.setCusCode(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
				customer.setCusName(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
				customer.setCusAdd1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
				customer.setCusAdd2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
				customer.setCusMob(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
//				customer.setCusRoute(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_RO)));
				customer.setCusEmail(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
				customer.setCusStatus(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
				customer.setCreditLimit(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
				customer.setCreditPeriod(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
				customer.setCreditStatus(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
				customer.setCusPrilCode(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_PRILLCODE)));

				list.add(customer);

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

	public ArrayList<Customer> getAllGpsCustomerForSelectedRepcode(String RepCode ,Double lan , Double lon ,String key) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		double debLat = 0.0;
		double debLon = 0.0;

		ArrayList<Customer> list = new ArrayList<>();
		Cursor cursor = null;

		try {
			String selectQuery = "select * from " + dbHelper.TABLE_FDEBTOR + " where " + dbHelper.FDEBTOR_REP_CODE + " = '" + RepCode + "' and DebCode || DebName LIKE '%" + key + "%'";

			cursor = dB.rawQuery(selectQuery, null);
			while ((cursor.moveToNext())) {
				Customer customer = new Customer();

				if ((cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LATITUDE)) != null) && (cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LONGITUDE)) != null)) {

						debLat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LATITUDE)));
						debLon = Double.parseDouble(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LONGITUDE)));

						float[] results = new float[1];
						Location.distanceBetween(lan,lon,debLat,debLon,results);
						float distanceInMeteres = results[0];
						boolean isWithin100m = distanceInMeteres < 150 ;

						if(isWithin100m)
						{
							customer.setCusCode(cursor.getString(cursor.getColumnIndex(FDEBTOR_CODE)));
							customer.setCusName(cursor.getString((cursor.getColumnIndex(FDEBTOR_NAME))));
							customer.setCusAdd1(cursor.getString(cursor.getColumnIndex(FDEBTOR_ADD1)));
							customer.setCusAdd2(cursor.getString(cursor.getColumnIndex(FDEBTOR_ADD2)));
							customer.setCusMob(cursor.getString(cursor.getColumnIndex(FDEBTOR_MOB)));
							customer.setCusEmail(cursor.getString(cursor.getColumnIndex(FDEBTOR_EMAIL)));
							customer.setCusStatus(cursor.getString(cursor.getColumnIndex(FDEBTOR_STATUS)));
							customer.setCreditLimit(cursor.getString(cursor.getColumnIndex(FDEBTOR_CRD_LIMIT)));
							customer.setCreditPeriod(cursor.getString(cursor.getColumnIndex(FDEBTOR_CRD_PERIOD)));
							customer.setCreditStatus(cursor.getString(cursor.getColumnIndex(FDEBTOR_CHK_CRD_LIMIT)));
							customer.setCusPrilCode(cursor.getString(cursor.getColumnIndex(FDEBTOR_PRILLCODE)));
							list.add(customer);

						}
				}
			}
		}catch (Exception e)
		{
				e.printStackTrace();
		}
		finally {

				if(cursor != null)
				{
					cursor.close();
				}
				dB.close();
		}
		return list;
	}





	public ArrayList<Customer> getAllCustomersForSelectedRepCode(String RepCode , String key) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String nullCoordi = "";
		nullCoordi = "0.000000";

		ArrayList<Customer> list = new ArrayList<Customer>();
		Cursor cursor = null;

		try {
			String selectQuery = "select * from " + dbHelper.TABLE_FDEBTOR + " Where " + dbHelper.FDEBTOR_REP_CODE + "='"
					+ RepCode +  "' and DebCode || DebName LIKE '%" + key + "%'";

			cursor = dB.rawQuery(selectQuery, null);
			while (cursor.moveToNext()) {

				Customer customer = new Customer();

				customer.setCusCode(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
				customer.setCusName(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
				customer.setCusAdd1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
				customer.setCusAdd2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
				customer.setCusMob(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
//				customer.setCusRoute(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_RO)));
				customer.setCusEmail(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
				customer.setCusStatus(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
				customer.setCreditLimit(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
				customer.setCreditPeriod(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
				customer.setCreditStatus(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
				customer.setLatitude(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LATITUDE)));
				customer.setLongitude(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LONGITUDE)));
				customer.setCusPrilCode(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_PRILLCODE)));

				list.add(customer);

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

	public ArrayList<Customer> getAllCustomersByRoute(String repCode)
	{

		int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
		int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

		String curdate = curYear+"-"+ String.format("%02d", curMonth) + "-" + String.format("%02d", curDate);
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Customer> list = new ArrayList<Customer>();
		Cursor cursor = null;
		try {

			cursor = dB.rawQuery("select * from fDebtor where DebCode in (select Debcode from fRouteDet where RouteCode in (select RouteCode from fTourHed where '"+curdate+"' between DateFrom And DateTo and RepCode = '"+repCode+"'));", null);

			while (cursor.moveToNext()) {

				Customer customer = new Customer();

				customer.setCusCode(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
				customer.setCusName(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
				customer.setCusAdd1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
				customer.setCusAdd2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
				customer.setCusMob(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
				customer.setCusEmail(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
				customer.setCusStatus(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
				customer.setCreditLimit(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
				customer.setCreditPeriod(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
				customer.setCreditStatus(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
				customer.setCusPrilCode(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_PRILLCODE)));

				list.add(customer);

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

	public Customer getSelectedCustomerByCode(String code) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		Cursor cursor = null;
		try {
			String selectQuery = "select * from " + dbHelper.TABLE_FDEBTOR + " Where " + dbHelper.FDEBTOR_CODE + "='"
					+ code + "'";

			cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				Customer customer = new Customer();

				customer.setCusName(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
				customer.setCusCode(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
				customer.setCusAdd1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
				customer.setCusAdd2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
				customer.setCusMob(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
				customer.setCusStatus(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));

				return customer;

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

	public int updateCustomerLocationByCurrentCordinates(String debcode , Double lan , Double lon)
	{
		int count = 0;

		if(dB == null) {
			open();
		}
		else if(dB.isOpen()) {
			open();
		}

		try {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.FDEBTOR_LATITUDE , lan);
			values.put(DatabaseHelper.FDEBTOR_LONGITUDE, lon);
			count = (int) dB.update(DatabaseHelper.TABLE_FDEBTOR, values, DatabaseHelper.FDEBTOR_CODE+ " =?", new String[]{String.valueOf(debcode)});

		}
		catch (Exception e) {
				e.printStackTrace();
		}
		finally {
				dB.close();
		}

		return count;

	}

	public ArrayList<Customer> getGPSCustomersByRoute(Double lat, Double lon, String key) {

		int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
		int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

		String curdate = curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate);
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		Double debLat = 0.0;
		Double debLon = 0.0;

		ArrayList<Customer> list = new ArrayList<Customer>();
		Cursor cursor = null;
		try {

			//cursor = dB.rawQuery("select * from fDebtor where DebCode in (select Debcode from fRouteDet where RouteCode in (select RouteCode from fTourHed where '"+curdate+"' between DateFrom And DateTo and RepCode = '"+repCode+"'));", null);
			cursor = dB.rawQuery("select * from fDebtor where DebCode in (select Debcode from fRouteDet where RouteCode in (select RouteCode from FItenrDet where TxnDate = '" + curdate + "')) and DebCode || DebName LIKE '%" + key + "%'", null);

			while (cursor.moveToNext()) {

				Customer customer = new Customer();

				if ((cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LATITUDE)) != null) && (cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LATITUDE)) != null)) {
					debLat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LATITUDE)));
					debLon = Double.parseDouble(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LONGITUDE)));

					float[] results = new float[1];
					Location.distanceBetween(lat, lon, debLat, debLon, results);
					float distanceInMeters = results[0];
					boolean isWithin100m = distanceInMeters < 150;

					if (isWithin100m) {
						customer.setCusCode(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
						customer.setCusName(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
						customer.setCusAdd1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
						customer.setCusAdd2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
						customer.setCusMob(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
						customer.setCusEmail(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
						customer.setCusStatus(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
						customer.setCreditLimit(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
						customer.setCreditPeriod(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
						customer.setCreditStatus(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
						customer.setCusPrilCode(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_PRILLCODE)));
						customer.setLatitude(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LATITUDE)));
						customer.setLongitude(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_LONGITUDE)));

						list.add(customer);
					}
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

}
