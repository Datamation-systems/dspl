package com.datamation.sfa.model;

public class FddbNote {

    private String refNo;
    private String refNo1;
    private String debCode;
    private String txnDate;
    private String amt;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FddbNote() {
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefNo1() {
        return refNo1;
    }

    public void setRefNo1(String refNo1) {
        this.refNo1 = refNo1;
    }

    public String getDebCode() {
        return debCode;
    }

    public void setDebCode(String debCode) {
        this.debCode = debCode;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}
