package com.datamation.sfa.model;


public class FreeItemDetails {

    private String refno;
    private String freeIssueSelectedItem;
    private int freeQty;


    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getFreeIssueSelectedItem() {
        return freeIssueSelectedItem;
    }

    public void setFreeIssueSelectedItem(String freeIssueSelectedItem) {
        this.freeIssueSelectedItem = freeIssueSelectedItem;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
    }


}
