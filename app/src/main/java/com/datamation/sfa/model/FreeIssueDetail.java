package com.datamation.sfa.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class FreeIssueDetail implements Serializable {

    private long orderId;
    private Item item;
    private int calculatedFreeQty;
    private int freeIssueStructureIndex;
    private int selectedFreeQty;

    public FreeIssueDetail(long orderId, Item item, int calculatedFreeQty, int selectedFreeQty,
                           int freeIssueStructureIndex) {
        this.orderId = orderId;
        this.item = item;
        this.calculatedFreeQty = calculatedFreeQty;
        this.selectedFreeQty = selectedFreeQty;
        this.freeIssueStructureIndex = freeIssueStructureIndex;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCalculatedFreeQty() {
        return calculatedFreeQty;
    }

    public void setCalculatedFreeQty(int calculatedFreeQty) {
        this.calculatedFreeQty = calculatedFreeQty;
    }

    public int getSelectedFreeQty() {
        return selectedFreeQty;
    }

    public void setSelectedFreeQty(int selectedFreeQty) {
        this.selectedFreeQty = selectedFreeQty;
    }

    public int getFreeIssueStructureIndex() {
        return freeIssueStructureIndex;
    }

    public void setFreeIssueStructureIndex(int freeIssueStructureIndex) {
        this.freeIssueStructureIndex = freeIssueStructureIndex;
    }

    @Override
    public String toString() {
        return "[Order ID : " + orderId
                + ", item : " + item
                + ", calculatedFreeQty : " + calculatedFreeQty
                + ", selected : " + selectedFreeQty + "]";
    }

    public JSONObject toJSON() {
        HashMap<String, Object> params = new HashMap<>();

        //params.put("id_item", item.getItemNo());
//        params.put("price", 0);

//        if (item.getCategoryId() == 12 || item.getCategoryId() == 14 || (item.getCategoryId() == 13 && item.getBrandID() != 38)) {
//            params.put("net_after_discount", (order.getDiscountPercentage() * item.getWholesalePrice()) / 100);
//        } else {
//            params.put("net_after_discount", 0);
//        }

//        params.put("net_after_discount", 0);

//        params.put("return", 0);
        params.put("calculated_qty", calculatedFreeQty);
        params.put("selected_qty", selectedFreeQty);
//        params.put("sample", 0);
//        params.put("free", freeQty);
//        params.put("salableReturns", 0);

        return new JSONObject(params);
    }

    public boolean printable() {
        return selectedFreeQty > 0;
    }

}

