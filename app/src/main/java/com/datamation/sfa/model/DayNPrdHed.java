package com.datamation.sfa.model;

import java.io.Serializable;

public class DayNPrdHed implements Serializable
{

    private String ConsoleDB;
    private String DistDB;
    private String NextNumVal;

    private String NONPRDHED_REFNO;
    private String NONPRDHED_TXNDATE;
    private String NONPRDHED_DEALCODE;
    private String NONPRDHED_REPCODE;
    private String NONPRDHED_REMARK;
    private String NONPRDHED_COSTCODE;
    private String NONPRDHED_ADDUSER;
    private String NONPRDHED_ADDDATE;
    private String NONPRDHED_ADDMACH;
    private String NONPRDHED_TRANSBATCH;
    private String NONPRDHED_IS_SYNCED;
    private String NONPRDHED_ADDRESS;
    private String NONPRDHED_LONGITUDE;
    private String NONPRDHED_LATITUDE;
    private String NONPRDHED_DEBCODE;
    private String NONPRDHED_IS_ACTIVE;
    private String NONPRDHED_REASON;

    public String getNONPRDHED_REASON() {
        return NONPRDHED_REASON;
    }

    public void setNONPRDHED_REASON(String NONPRDHED_REASON) {
        this.NONPRDHED_REASON = NONPRDHED_REASON;
    }

    public String getNextNumVal() {
        return NextNumVal;
    }

    public void setNextNumVal(String nextNumVal) {
        NextNumVal = nextNumVal;
    }

    public String getConsoleDB() {
        return ConsoleDB;
    }

    public void setConsoleDB(String consoleDB) {
        ConsoleDB = consoleDB;
    }

    public String getDistDB() {
        return DistDB;
    }

    public void setDistDB(String distDB) {
        DistDB = distDB;
    }

    public String getNONPRDHED_LONGITUDE() {
        return NONPRDHED_LONGITUDE;
    }

    public void setNONPRDHED_LONGITUDE(String NONPRDHED_LONGITUDE) {
        this.NONPRDHED_LONGITUDE = NONPRDHED_LONGITUDE;
    }

    public String getNONPRDHED_LATITUDE() {
        return NONPRDHED_LATITUDE;
    }

    public void setNONPRDHED_LATITUDE(String NONPRDHED_LATITUDE) {
        this.NONPRDHED_LATITUDE = NONPRDHED_LATITUDE;
    }

    public String getNONPRDHED_DEBCODE() {
        return NONPRDHED_DEBCODE;
    }

    public void setNONPRDHED_DEBCODE(String NONPRDHED_DEBCODE) {
        this.NONPRDHED_DEBCODE = NONPRDHED_DEBCODE;
    }

    public String getNONPRDHED_IS_ACTIVE() {
        return NONPRDHED_IS_ACTIVE;
    }

    public void setNONPRDHED_IS_ACTIVE(String NONPRDHED_IS_ACTIVE) {
        this.NONPRDHED_IS_ACTIVE = NONPRDHED_IS_ACTIVE;
    }

    public String getNONPRDHED_ADDRESS() {
        return NONPRDHED_ADDRESS;
    }

    public void setNONPRDHED_ADDRESS(String nONPRDHED_ADDRESS) {
        NONPRDHED_ADDRESS = nONPRDHED_ADDRESS;
    }

    public String getNONPRDHED_REFNO() {
        return NONPRDHED_REFNO;
    }

    public void setNONPRDHED_REFNO(String nONPRDHED_REFNO) {
        NONPRDHED_REFNO = nONPRDHED_REFNO;
    }

    public String getNONPRDHED_TXNDATE() {
        return NONPRDHED_TXNDATE;
    }

    public void setNONPRDHED_TXNDATE(String nONPRDHED_TXNDAET) {
        NONPRDHED_TXNDATE = nONPRDHED_TXNDAET;
    }

    public String getNONPRDHED_DEALCODE() {
        return NONPRDHED_DEALCODE;
    }

    public void setNONPRDHED_DEALCODE(String nONPRDHED_DEALCODE) {
        NONPRDHED_DEALCODE = nONPRDHED_DEALCODE;
    }

    public String getNONPRDHED_REPCODE() {
        return NONPRDHED_REPCODE;
    }

    public void setNONPRDHED_REPCODE(String nONPRDHED_REPCODE) {
        NONPRDHED_REPCODE = nONPRDHED_REPCODE;
    }

    public String getNONPRDHED_REMARK() {
        return NONPRDHED_REMARK;
    }

    public void setNONPRDHED_REMARK(String nONPRDHED_REMARK) {
        NONPRDHED_REMARK = nONPRDHED_REMARK;
    }

    public String getNONPRDHED_COSTCODE() {
        return NONPRDHED_COSTCODE;
    }

    public void setNONPRDHED_COSTCODE(String nONPRDHED_COSTCODE) {
        NONPRDHED_COSTCODE = nONPRDHED_COSTCODE;
    }

    public String getNONPRDHED_ADDUSER() {
        return NONPRDHED_ADDUSER;
    }

    public void setNONPRDHED_ADDUSER(String nONPRDHED_ADDUSER) {
        NONPRDHED_ADDUSER = nONPRDHED_ADDUSER;
    }

    public String getNONPRDHED_ADDDATE() {
        return NONPRDHED_ADDDATE;
    }

    public void setNONPRDHED_ADDDATE(String nONPRDHED_ADDDATE) {
        NONPRDHED_ADDDATE = nONPRDHED_ADDDATE;
    }

    public String getNONPRDHED_ADDMACH() {
        return NONPRDHED_ADDMACH;
    }

    public void setNONPRDHED_ADDMACH(String nONPRDHED_ADDMACH) {
        NONPRDHED_ADDMACH = nONPRDHED_ADDMACH;
    }

    public String getNONPRDHED_TRANSBATCH() {
        return NONPRDHED_TRANSBATCH;
    }

    public void setNONPRDHED_TRANSBATCH(String nONPRDHED_TRANSBATCH) {
        NONPRDHED_TRANSBATCH = nONPRDHED_TRANSBATCH;
    }

    public String getNONPRDHED_IS_SYNCED() {
        return NONPRDHED_IS_SYNCED;
    }

    public void setNONPRDHED_IS_SYNCED(String nONPRDHED_IS_SYNCED) {
        NONPRDHED_IS_SYNCED = nONPRDHED_IS_SYNCED;
    }

}
