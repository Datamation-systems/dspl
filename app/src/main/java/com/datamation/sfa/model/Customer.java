package com.datamation.sfa.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.datamation.sfa.helpers.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Customer {

	public Customer() {

	}

	private String cusCode;
	private String cusName;
	private String cusAdd1;
	private String cusAdd2;
	private String cusMob;
	private String cusRoute;
	private String cusStatus;
	private String cusEmail;
	private boolean selectedOnList;
	private String creditLimit;
	private String creditStatus;
	private String creditPeriod;

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getCreditPeriod() {
		return creditPeriod;
	}

	public void setCreditPeriod(String creditPeriod) {
		this.creditPeriod = creditPeriod;
	}

	public String getCusCode() {
		return cusCode;
	}

	public void setCusCode(String cusCode) {
		this.cusCode = cusCode;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getCusAdd1() {
		return cusAdd1;
	}

	public void setCusAdd1(String cusAdd1) {
		this.cusAdd1 = cusAdd1;
	}

	public String getCusAdd2() {
		return cusAdd2;
	}

	public void setCusAdd2(String cusAdd2) {
		this.cusAdd2 = cusAdd2;
	}

	public String getCusMob() {
		return cusMob;
	}

	public void setCusMob(String cusMob) {
		this.cusMob = cusMob;
	}

	public String getCusRoute() {
		return cusRoute;
	}

	public void setCusRoute(String cusRoute) {
		this.cusRoute = cusRoute;
	}

	public String getCusStatus() {
		return cusStatus;
	}

	public void setCusStatus(String cusStatus) {
		this.cusStatus = cusStatus;
	}

	public String getCusEmail() {
		return cusEmail;
	}

	public void setCusEmail(String cusEmail) {
		this.cusEmail = cusEmail;
	}

	public boolean isSelectedOnList() {
		return selectedOnList;
	}
	
	public void setSelectedOnList(boolean selectedOnList) {
		this.selectedOnList = selectedOnList;
	}

	public static Customer parseOutlet(JSONObject instance) throws JSONException {

		if (instance != null) {
			Customer outlet = new Customer();
			String outletIdString;
			outlet.setCusCode(instance.getString("cuscode"));
			outlet.setCusName(instance.getString("cusname"));
			outlet.setCusRoute(instance.getString("routecode"));
			outlet.setCusAdd1(instance.getString("address"));
			//outlet.setCusAdd2(instance.getString("addressline2"));
			outlet.setCusMob(instance.getString("mobile"));
			outlet.setCusEmail(instance.getString("email"));
			outlet.setCusStatus(instance.getString("status"));
			return outlet;
		}

		return null;
	}

//	public ArrayList<Customer> getRouteCustomersByCodeAndName(String RouteCode, String newText) {
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//
//		return list;
//	}
//
//	public ArrayList<Customer> getRouteCustomers(String RouteCode, String newText) {
//
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//
//		return list;
//	}
		
}
