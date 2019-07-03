package com.datamation.sfa.model;

import java.util.ArrayList;

public class PRESALE {

    private String ORDER_REFNO;
    private String ORDER_TXN_DATE;
    private String ORDER_MANUAL_NUMBER;
    private String ORDER_DELIVERY_DATE;
    private String ORDER_REMARKS;
    private String ORDER_ROUTE_CODE;
    private String ORDER_DEB_CODE;
    private String ORDER_IS_ACTIVE;
    private String ORDER_REP_CODE;
    private String ORDER_LONGITUDE;
    private String ORDER_LATITUDE;
    private String ORDER_ADD_DATE;
    private String ORDER_ADD_TIME;
    private String ORDER_TOT_AMT;
    private ArrayList<OrderDetail> soDetArrayList;

    public PRESALE() {
    }

    public ArrayList<OrderDetail> getSoDetArrayList() {
        return soDetArrayList;
    }

    public void setSoDetArrayList(ArrayList<OrderDetail> soDetArrayList) {
        this.soDetArrayList = soDetArrayList;
    }

    public String getORDER_TOT_AMT() {
        return ORDER_TOT_AMT;
    }

    public void setORDER_TOT_AMT(String ORDER_TOT_AMT) {
        this.ORDER_TOT_AMT = ORDER_TOT_AMT;
    }

    public String getORDER_ADD_TIME() {
        return ORDER_ADD_TIME;
    }

    public void setORDER_ADD_TIME(String ORDER_ADD_TIME) {
        this.ORDER_ADD_TIME = ORDER_ADD_TIME;
    }

    public String getORDER_ADD_DATE() {
        return ORDER_ADD_DATE;
    }

    public void setORDER_ADD_DATE(String ORDER_ADD_DATE) {
        this.ORDER_ADD_DATE = ORDER_ADD_DATE;
    }

    public String getORDER_IS_ACTIVE() {
        return ORDER_IS_ACTIVE;
    }

    public void setORDER_IS_ACTIVE(String ORDER_IS_ACTIVE) {
        this.ORDER_IS_ACTIVE = ORDER_IS_ACTIVE;
    }

    public String getORDER_REP_CODE() {
        return ORDER_REP_CODE;
    }

    public void setORDER_REP_CODE(String ORDER_REP_CODE) {
        this.ORDER_REP_CODE = ORDER_REP_CODE;
    }

    public String getORDER_LONGITUDE() {
        return ORDER_LONGITUDE;
    }

    public void setORDER_LONGITUDE(String ORDER_LONGITUDE) {
        this.ORDER_LONGITUDE = ORDER_LONGITUDE;
    }

    public String getORDER_LATITUDE() {
        return ORDER_LATITUDE;
    }

    public void setORDER_LATITUDE(String ORDER_LATITUDE) {
        this.ORDER_LATITUDE = ORDER_LATITUDE;
    }

    public String getORDER_REFNO() {
        return ORDER_REFNO;
    }

    public void setORDER_REFNO(String ORDER_REFNO) {
        this.ORDER_REFNO = ORDER_REFNO;
    }

    public String getORDER_TXN_DATE() {
        return ORDER_TXN_DATE;
    }

    public void setORDER_TXN_DATE(String ORDER_TXN_DATE) {
        this.ORDER_TXN_DATE = ORDER_TXN_DATE;
    }

    public String getORDER_MANUAL_NUMBER() {
        return ORDER_MANUAL_NUMBER;
    }

    public void setORDER_MANUAL_NUMBER(String ORDER_MANUAL_NUMBER) {
        this.ORDER_MANUAL_NUMBER = ORDER_MANUAL_NUMBER;
    }

    public String getORDER_DELIVERY_DATE() {
        return ORDER_DELIVERY_DATE;
    }

    public void setORDER_DELIVERY_DATE(String ORDER_DELIVERY_DATE) {
        this.ORDER_DELIVERY_DATE = ORDER_DELIVERY_DATE;
    }

    public String getORDER_REMARKS() {
        return ORDER_REMARKS;
    }

    public void setORDER_REMARKS(String ORDER_REMARKS) {
        this.ORDER_REMARKS = ORDER_REMARKS;
    }

    public String getORDER_ROUTE_CODE() {
        return ORDER_ROUTE_CODE;
    }

    public void setORDER_ROUTE_CODE(String ORDER_ROUTE_CODE) {
        this.ORDER_ROUTE_CODE = ORDER_ROUTE_CODE;
    }

    public String getORDER_DEB_CODE() {
        return ORDER_DEB_CODE;
    }

    public void setORDER_DEB_CODE(String ORDER_DEB_CODE) {
        this.ORDER_DEB_CODE = ORDER_DEB_CODE;
    }
}
