package com.datamation.sfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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

	@SuppressWarnings("static-access")
	public int createOrUpdateDebtor(ArrayList<Customer> customers) {
		int serverdbID = 0;
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {

			dB.beginTransaction();

			String sql = "Insert or Replace into " + dbHelper.TABLE_CUSTOMER + " (" + dbHelper.CUSTOMER_CODE + ", "
					+ dbHelper.CUSTOMER_NAME + ", " + dbHelper.CUSTOMER_ADD1 + ", " + dbHelper.CUSTOMER_ADD2 + ", "
					+ dbHelper.CUSTOMER_EMAIL + ", " + dbHelper.CUSTOMER_MOB + ", " + dbHelper.CUSTOMER_ROUTE + ", "
					+ dbHelper.CUSTOMER_STATUS +") values(?,?,?,?,?,?,?,?)";

			SQLiteStatement insert = dB.compileStatement(sql);

			for (Customer customer : customers) {

				insert.bindString(1, customer.getCusCode());
				insert.bindString(2, customer.getCusName());
				insert.bindString(3, customer.getCusAdd1());
				insert.bindString(4, customer.getCusAdd2());
				insert.bindString(5, customer.getCusMob());
				insert.bindString(6, customer.getCusRoute());
				insert.bindString(7, customer.getCusEmail());
				insert.bindString(8, customer.getCusStatus());

				insert.execute();

				serverdbID = 1;
			}

			dB.setTransactionSuccessful();
			Log.w(TAG, "Done");
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.endTransaction();
			dB.close();
		}
		return serverdbID;

	}
	public void InsertOrReplaceDebtor(ArrayList<Debtor> list) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {
			dB.beginTransactionNonExclusive();
			// String sql = "INSERT OR REPLACE INTO " +
			// DatabaseHelper.TABLE_FDEBTOR + "
			// (DebCode,DebName,DebAdd1,DebAdd2,DebAdd3,DebTele,DebMob,DebEMail,CretDate,RemDis,TownCode,AreaCode,DebCatCode,DbGrCode,DebClsCode,Status,DebLylty,DealCode,AddUser,AddDateDEB,AddMach,RecordId,timestamp_column,"
			// +
			// "CrdPeriod,ChkCrdPrd,CrdLimit,ChkCrdLmt,RepCode,RankCode,txndate,TranBatch,DebSumary,OutDis,DebFax,DebWeb,DebCTNam,DebCTAdd1,DebCTAdd2,DebCTAdd3,DebCTTele,DebCTFax,DebCTEmail,"
			// +
			// "DelPersn,DelAdd1,DelAdd2,DelAdd3,DelTele,DelFax,DelEmail,DateOfB,TaxReg,CusDIsPer,PrillCode,CusDisStat,BusRgNo,PostCode,GenRemarks,BranCode,Bank,Branch,AcctNo,CusVatNo)
			// " + " VALUES
			// (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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

	//	public int createOrUpdateTempDebtor(ArrayList<Customer> debtors) {
//		int serverdbID = 0;
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		try {
//
//			dB.beginTransaction();
//
//			String sql = "Insert or Replace into " + dbHelper.TABLE_TEMP_FDEBTOR + " (" + dbHelper.FDEBTOR_CODE + ", "
//					+ dbHelper.FDEBTOR_NAME + ") values(?,?)";
//
//			SQLiteStatement insert = dB.compileStatement(sql);
//
//			for (Customer debtor : debtors) {
//
//				insert.bindString(1, debtor.getFDEBTOR_CODE());
//				insert.bindString(2, debtor.getFDEBTOR_NAME());
//
//				insert.execute();
//
//				serverdbID = 1;
//			}
//
//			dB.setTransactionSuccessful();
//			Log.w(TAG, "Done");
//		} catch (Exception e) {
//
//			Log.v(TAG + " Exception", e.toString());
//
//		} finally {
//			dB.endTransaction();
//			dB.close();
//		}
//		return serverdbID;
//
//	}
//
//	public int deleteAll() {
//
//		int count = 0;
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//		Cursor cursor = null;
//		try {
//
//			cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_CUSTOMER, null);
//			count = cursor.getCount();
//			if (count > 0) {
//				int success = dB.delete(dbHelper.TABLE_CUSTOMER, null, null);
//				Log.v("Success", success + "");
//			}
//		} catch (Exception e) {
//
//			Log.v(TAG + " Exception", e.toString());
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//
//		return count;
//
//	}
//
//	public int deleteAllTemp() {
//
//		int count = 0;
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//		Cursor cursor = null;
//		try {
//
//			cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_TEMP_FDEBTOR, null);
//			count = cursor.getCount();
//			if (count > 0) {
//				int success = dB.delete(dbHelper.TABLE_TEMP_FDEBTOR, null, null);
//				Log.v("Success", success + "");
//			}
//		} catch (Exception e) {
//
//			Log.v(TAG + " Exception", e.toString());
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//
//		return count;
//
//	}
//
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

	public ArrayList<Customer> getAllCustomersForSelectedRepCode(String RepCode) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Customer> list = new ArrayList<Customer>();
		Cursor cursor = null;
		try {
			String selectQuery = "select * from " + dbHelper.TABLE_FDEBTOR + " Where " + dbHelper.FDEBTOR_REP_CODE + "='"
					+ RepCode + "'";

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
				//customer.setCusRoute(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ROUTE_CODE)));
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
	//
//
//	public ArrayList<Customer> getAllSelectedCustomers() {
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//		Cursor cursor = null;
//		try {
//			String selectQuery = "select * from " + dbHelper.TABLE_TEMP_FDEBTOR + " GROUP BY " + dbHelper.FDEBTOR_CODE;
//
//			cursor = dB.rawQuery(selectQuery, null);
//			while (cursor.moveToNext()) {
//
//				Customer aDebtor = new Customer();
//
//				aDebtor.setFDEBTOR_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ID)));
//				aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
//				aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
//
//				list.add(aDebtor);
//
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//		return list;
//	}
//
//	public ArrayList<Customer> getCustomerByCodeAndName(String newText) {
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//		Cursor cursor = null;
//		try {
//			String selectQuery = "select * from " + dbHelper.TABLE_CUSTOMER + " where " + dbHelper.FDEBTOR_NAME
//					+ " like '" + newText + "%'";
//			// Original Menaka 25-05-2016 String selectQuery = "select * from "
//			// +
//			// dbHelper.TABLE_CUSTOMER + " where " + dbHelper.FDEBTOR_CODE + " ||
//			// " +
//			// dbHelper.FDEBTOR_NAME + " like '%" + newText + "%'";
//			cursor = dB.rawQuery(selectQuery, null);
//			while (cursor.moveToNext()) {
//
//				Customer aDebtor = new Customer();
//
//				aDebtor.setFDEBTOR_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ID)));
//				aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
//				aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
//				aDebtor.setFDEBTOR_ADD1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
//				aDebtor.setFDEBTOR_ADD2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
//				aDebtor.setFDEBTOR_ADD3(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD3)));
//				aDebtor.setFDEBTOR_TELE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TELE)));
//				aDebtor.setFDEBTOR_MOB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
//				aDebtor.setFDEBTOR_EMAIL(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
//				aDebtor.setFDEBTOR_CREATEDATE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CREATEDATE)));
//				aDebtor.setFDEBTOR_TOWN_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TOWN_CODE)));
//				aDebtor.setFDEBTOR_AREA_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_AREA_CODE)));
//				// aDebtor.setFDEBTOR_DBGR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_DBGR_CODE)));
//				aDebtor.setFDEBTOR_STATUS(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
//				// aDebtor.setFDEBTOR_ADD_USER(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_USER)));
//				// aDebtor.setFDEBTOR_ADD_DATE_DEB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_DATE_DEB)));
//				// aDebtor.setFDEBTOR_ADD_MACH(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_MACH)));
//				aDebtor.setFDEBTOR_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
//				aDebtor.setFDEBTOR_CHK_CRD_PRD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_PRD)));
//				aDebtor.setFDEBTOR_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
//				aDebtor.setFDEBTOR_CHK_CRD_LIMIT(
//						cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
//				aDebtor.setFDEBTOR_REP_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_REP_CODE)));
//				// aDebtor.setFDEBTOR_RANK_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_RANK_CODE)));
//				aDebtor.setFDEBTOR_SUMMARY(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_SUMMARY)));
//
//				list.add(aDebtor);
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//		return list;
//	}
//
//
//
//	public ArrayList<Customer> getRouteCustomers(String RouteCode) {
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//
//		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FROUTEDET + " RD, " + dbHelper.TABLE_FMDEBTOR
//				+ " D WHERE RD." + dbHelper.FROUTEDET_DEB_CODE + "=D." + dbHelper.FDEBTOR_CODEM + " AND RD."
//				+ dbHelper.FROUTEDET_ROUTE_CODE + "='" + RouteCode + "'";
//
//		Cursor cursor = dB.rawQuery(selectQuery, null);
//		while (cursor.moveToNext()) {
//
//			Customer aDebtor = new Customer();
//
//			aDebtor.setFDEBTOR_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ID)));
//			aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
//			aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
//			aDebtor.setFDEBTOR_ADD1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
//			aDebtor.setFDEBTOR_ADD2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
//			aDebtor.setFDEBTOR_ADD3(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD3)));
//			aDebtor.setFDEBTOR_TELE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TELE)));
//			aDebtor.setFDEBTOR_MOB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
//			aDebtor.setFDEBTOR_EMAIL(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
//			aDebtor.setFDEBTOR_CREATEDATE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CREATEDATE)));
//			aDebtor.setFDEBTOR_TOWN_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TOWN_CODE)));
//			aDebtor.setFDEBTOR_AREA_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_AREA_CODE)));
//			aDebtor.setFDEBTOR_DBGR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_DBGR_CODE)));
//			aDebtor.setFDEBTOR_STATUS(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
//			aDebtor.setFDEBTOR_ADD_USER(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_USER)));
//			aDebtor.setFDEBTOR_ADD_DATE_DEB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_DATE_DEB)));
//			aDebtor.setFDEBTOR_ADD_MACH(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_MACH)));
//			aDebtor.setFDEBTOR_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
//			aDebtor.setFDEBTOR_CHK_CRD_PRD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_PRD)));
//			aDebtor.setFDEBTOR_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
//			aDebtor.setFDEBTOR_CHK_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
//			aDebtor.setFDEBTOR_REP_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_REP_CODE)));
//			aDebtor.setFDEBTOR_RANK_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_RANK_CODE)));
//			aDebtor.setFDEBTOR_SUMMARY(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_SUMMARY)));
//
//			list.add(aDebtor);
//
//		}
//
//		return list;
//	}
//
//	public ArrayList<Customer> getRouteCustomersByCodeAndName(String RouteCode, String newText) {
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//		Cursor cursor = null;
//		try {
//			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FROUTEDET + " RD, " + dbHelper.TABLE_CUSTOMER
//					+ " D WHERE RD." + dbHelper.FROUTEDET_DEB_CODE + "=D." + dbHelper.FDEBTOR_CODE + " AND RD."
//					+ dbHelper.FROUTEDET_ROUTE_CODE + "='" + RouteCode + "' AND D." + dbHelper.FDEBTOR_CODE + " || D."
//					+ dbHelper.FDEBTOR_NAME + " like '%" + newText + "%'";
//			;
//
//			cursor = dB.rawQuery(selectQuery, null);
//
//			while (cursor.moveToNext()) {
//
//				Customer aDebtor = new Customer();
//
//				aDebtor.setFDEBTOR_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ID)));
//				aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
//				aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
//				aDebtor.setFDEBTOR_ADD1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
//				aDebtor.setFDEBTOR_ADD2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
//				aDebtor.setFDEBTOR_ADD3(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD3)));
//				aDebtor.setFDEBTOR_TELE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TELE)));
//				aDebtor.setFDEBTOR_MOB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
//				aDebtor.setFDEBTOR_EMAIL(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
//				aDebtor.setFDEBTOR_CREATEDATE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CREATEDATE)));
//				aDebtor.setFDEBTOR_TOWN_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TOWN_CODE)));
//				aDebtor.setFDEBTOR_AREA_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_AREA_CODE)));
//				aDebtor.setFDEBTOR_DBGR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_DBGR_CODE)));
//				aDebtor.setFDEBTOR_STATUS(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
//				aDebtor.setFDEBTOR_ADD_USER(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_USER)));
//				aDebtor.setFDEBTOR_ADD_DATE_DEB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_DATE_DEB)));
//				aDebtor.setFDEBTOR_ADD_MACH(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_MACH)));
//				aDebtor.setFDEBTOR_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
//				aDebtor.setFDEBTOR_CHK_CRD_PRD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_PRD)));
//				aDebtor.setFDEBTOR_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
//				aDebtor.setFDEBTOR_CHK_CRD_LIMIT(
//						cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
//				aDebtor.setFDEBTOR_REP_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_REP_CODE)));
//				aDebtor.setFDEBTOR_RANK_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_RANK_CODE)));
//				aDebtor.setFDEBTOR_SUMMARY(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_SUMMARY)));
//
//				list.add(aDebtor);
//
//			}
//		} catch (Exception e) {
//
//			Log.v(TAG + " Exception", e.toString());
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//
//		return list;
//	}
//
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
			//	customer.setCusRoute(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ROUTE_CODE)));
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
//
//	@SuppressWarnings("static-access")
//	public String getCustNameByCode(String code) {
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//		Cursor cursor = null;
//		try {
//			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CUSTOMER + " WHERE " + dbHelper.FDEBTOR_CODE + "='"
//					+ code + "'";
//
//			cursor = dB.rawQuery(selectQuery, null);
//
//			while (cursor.moveToNext()) {
//
//				return cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME));
//
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//				dB.close();
//			}
//		}
//		return "";
//	}
//
//	@SuppressWarnings("static-access")
//	public String getTownNameByCode(String code) {
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//		Cursor cursor = null;
//		try {
//			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FTOWN + " WHERE " + dbHelper.FTOWN_CODE
//					+ " IN (SELECT " + dbHelper.FTOWN_CODE + " FROM " + dbHelper.TABLE_CUSTOMER + " WHERE "
//					+ dbHelper.FDEBTOR_CODE + " = '" + code + "')";
//
//			cursor = dB.rawQuery(selectQuery, null);
//
//			while (cursor.moveToNext()) {
//
//				return cursor.getString(cursor.getColumnIndex(dbHelper.FTOWN_NAME));
//
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//				dB.close();
//			}
//		}
//		return "";
//	}
//
//	@SuppressWarnings("static-access")
//	public String getTaxRegStatus(String code) {
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//		Cursor cursor = null;
//		try {
//			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CUSTOMER + " WHERE " + dbHelper.FDEBTOR_CODE + "='"
//					+ code + "'";
//
//			cursor = dB.rawQuery(selectQuery, null);
//
//			while (cursor.moveToNext()) {
//
//				return cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TAXREG));
//
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//				dB.close();
//			}
//		}
//		return "";
//	}
//
//	public ArrayList<Customer> getDebDetails(String searchword) {
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		ArrayList<Customer> Itemname = new ArrayList<Customer>();
//
//		String selectQuery = "select DebName,DebCode from fDebtor where DebCode LIKE '%" + searchword
//				+ "%' OR DebName LIKE '%" + searchword + "%'";
//
//		Cursor cursor = null;
//		try {
//			cursor = dB.rawQuery(selectQuery, null);
//
//			while (cursor.moveToNext()) {
//				Customer items = new Customer();
//
//				items.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
//				items.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
//				Itemname.add(items);
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//				dB.close();
//			}
//		}
//		return Itemname;
//	}
//
//	public String getDebNameByCode(String code) {
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CUSTOMER + " WHERE " + dbHelper.FDEBTOR_CODE + "='" + code
//				+ "'";
//
//		Cursor cursor = null;
//		try {
//			cursor = dB.rawQuery(selectQuery, null);
//
//			while (cursor.moveToNext()) {
//
//				return cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME));
//
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//				dB.close();
//			}
//		}
//		return "";
//
//	}
//
//	public boolean getCheckAllowSelect(String code) {
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CUSTOMER + " WHERE " + dbHelper.FDEBTOR_CODE + "='" + code
//				+ "'";
//
//		Cursor cursor = null;
//		try {
//			cursor = dB.rawQuery(selectQuery, null);
//
//			while (cursor.moveToNext()) {
//				int AllowChange = Integer
//						.parseInt(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_MUSTFREE)));
//
//				if (AllowChange == 1) {
//					return true;
//				} else {
//					return false;
//				}
//
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//				dB.close();
//			}
//		}
//		return false;
//
//	}
//
//	@SuppressWarnings("static-access")
//	public String getRouteNameByCode(String code) {
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//		Cursor cursor = null;
//		try {
//
//			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_ROUTE + " WHERE " + dbHelper.FROUTE_ROUTECODE
//					+ " IN (SELECT " + dbHelper.FROUTEDET_ROUTE_CODE + " FROM " + dbHelper.TABLE_FROUTEDET + " WHERE "
//					+ dbHelper.FROUTEDET_DEB_CODE + " = '" + code + "')";
//
//			cursor = dB.rawQuery(selectQuery, null);
//
//			while (cursor.moveToNext()) {
//
//				return cursor.getString(cursor.getColumnIndex(dbHelper.FROUTE_ROUTE_NAME));
//
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//				dB.close();
//			}
//		}
//		return "";
//	}

}
