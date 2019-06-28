package com.datamation.sfa.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FreeIssue implements Serializable {

//            "eff_id": "1",
//            "multiple_qty": "1",
//            "eff_freeproduct": "1",
//            "free_item": "Siddhalepa -2.5g",
//            "free_qty": "10",
//            "price_category_id": "2"



    private int freeIssueId;
    private int freeIssueItemId;
    private int priceRangeId;
    private double qty;
    private boolean multiplyBYQty;
    private double totqty;




    public FreeIssue(int freeIssueId, int freeIssueItemId, int priceRangeId, double qty, boolean multiplyBYQty) {
        this.freeIssueId = freeIssueId;
        this.freeIssueItemId = freeIssueItemId;
        this.priceRangeId = priceRangeId;
        this.qty = qty;
        this.multiplyBYQty = multiplyBYQty;
    }

    public static FreeIssue parceFreeIssue(JSONObject object) throws JSONException {
        return new FreeIssue(object.getInt("eff_id"),
                object.getInt("eff_freeproduct"),
                object.getInt("price_category_id"),
                object.getDouble("free_qty"),
                object.getInt("multiple_qty")==1);
    }

    public int getFreeIssueId() {
        return freeIssueId;
    }

    public void setFreeIssueId(int freeIssueId) {
        this.freeIssueId = freeIssueId;
    }

    public int getFreeIssueItemId() {
        return freeIssueItemId;
    }

    public void setFreeIssueItemId(int freeIssueItemId) {
        this.freeIssueItemId = freeIssueItemId;
    }

    public int getPriceRangeId() {
        return priceRangeId;
    }

    public void setPriceRangeId(int priceRangeId) {
        this.priceRangeId = priceRangeId;
    }


//48:6 nam 12:?.......   1.5 web aken add wenne.... multiple neththm (request qty aka gannawa adala price range akat/"pieces": "12", aniwa a=balanwa

    /// multiply vidiyat=  unit=12;itemqty=13;   (13/12)*free_qty
//    public int getCalculatedQty(int unit,int itemQty) {
//        if(multiplyBYQty){
//            int count=itemQty/unit;
//            return  count*getQty();;
//        }
//        return  getQty();
//    }


    ///

    public int getCalculatedQty(int unit,int itemQty) {
        if(multiplyBYQty){
            int count=itemQty/unit;
            totqty=count*getQty();

            int countQty = (int) Math.round(totqty);
            return  countQty;
        }
        int countQty=(int)getQty();
        return  countQty;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isMultiplyBYQty() {
        return multiplyBYQty;
    }

    public void setMultiplyBYQty(boolean multiplyBYQty) {
        this.multiplyBYQty = multiplyBYQty;
    }
}
