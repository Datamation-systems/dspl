package com.datamation.sfa.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Order {
    private final String LOG_TAG = Order.class.getSimpleName();

    public static final int DISCOUNT_MODE_CASH=0;
    public static final int DISCOUNT_MODE_CHEQUE=1;

    private long orderId;
    private long orderTime;
    private int outletId, routeId;
    private double grossAmount;
    private double returnAmount;
    private double eligibleAmount;
    private double discount;
    private double discountPercentage;
    private double marginAmount;
    private double netAmount;
    private int lineAmount;

    private double latitude;
    private double longitude;
    private int locationType;
    private int batteryLevel;
    private List<OrderDetail> orderDetails;
    private List<ReturnDetail> returnDetails;
    private List<FreeIssueDetail> freeIssueDetails;
    //    private List<ItemPromotion> itemPromotions;
    private List<Payment> payments;
    private boolean isSynced = false;
    private int discount_mode;
    private String mobileIP;

//    private boolean discountPercent = false;

    private long nextVisit;

    private double locationAccuracy;
    private String paymentMethodCode;

    private boolean isSalesOrder = false;

    public Order() {
        isSynced = false;
    }

    public Order(long orderId, long orderTime, int outletId, double discount, double latitude,
                 double longitude, float locationAccuracy, int locationType, List<OrderDetail> orderDetails,
                 List<ReturnDetail> returnDetails, /*Payment payment,*/ List<FreeIssueDetail> freeIssueDetails) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.outletId = outletId;
        this.discount = discount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationType = locationType;
        this.orderDetails = orderDetails;
//        this.payment = payment;
        this.returnDetails = returnDetails;
        this.isSynced = false;
        this.freeIssueDetails = freeIssueDetails;
        this.locationAccuracy = locationAccuracy;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public double getEligibleAmount() {
        return eligibleAmount;
    }

    public void setEligibleAmount(double eligibleAmount) {
        this.eligibleAmount = eligibleAmount;
    }

    public double getMarginAmount() {
        return marginAmount;
    }

    public void setMarginAmount(double marginAmount) {
        this.marginAmount = marginAmount;
    }

    public int getLineAmount() {
        return lineAmount;
    }

    public void setLineAmount(int lineAmount) {
        this.lineAmount = lineAmount;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

//    public Payment getPayment() {
//        return payment;
//    }
//
//    public void setPayment(Payment payment) {
//        this.payment = payment;
//    }


    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<ReturnDetail> getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(List<ReturnDetail> returnDetails) {
        if (returnDetails!=null) {
            List<ReturnDetail> temp = new ArrayList<>();
            for(ReturnDetail detail:returnDetails){
                if(detail.getQty()==0){temp.add(detail);}
            }
            returnDetails.removeAll(temp);
        }
        this.returnDetails = returnDetails;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean isSynced) {
        this.isSynced = isSynced;
    }

//    public boolean isDiscountPercent() {
//        return discountPercent;
//    }
//
//    public void setDiscountPercent(boolean discountPercent) {
//        this.discountPercent = discountPercent;
//    }

    public long getNextVisit() {
        return nextVisit;
    }

    public void setNextVisit(long nextVisit) {
        this.nextVisit = nextVisit;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public List<FreeIssueDetail> getFreeIssueDetails() {
        return freeIssueDetails;
    }

    public void setFreeIssueDetails(List<FreeIssueDetail> freeIssueDetails) {
        this.freeIssueDetails = freeIssueDetails;
    }

    public double getLocationAccuracy() {
        return locationAccuracy;
    }

    public void setLocationAccuracy(double locationAccuracy) {
        this.locationAccuracy = locationAccuracy;
    }

    public boolean isSalesOrder() {
        return isSalesOrder;
    }

    public void setIsSalesOrder(boolean isSalesOrder) {
        this.isSalesOrder = isSalesOrder;
    }

    public int getDiscount_mode() {
        return discount_mode;
    }

    public void setDiscount_mode(int discount_mode) {
        this.discount_mode = discount_mode;
    }

    public String getMobileIP() {
        return mobileIP;
    }

    public void setMobileIP(String mobileIP) {
        this.mobileIP = mobileIP;
    }

    public JSONObject getOrderAsJSON(Context context) throws JSONException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        HashMap<String, Object> finalJSONParams = new HashMap<>();

        HashMap<String, Object> invoiceParams = new HashMap<>();
        invoiceParams.put("orderId", String.valueOf(orderId));
        invoiceParams.put("invtype", 0);
        invoiceParams.put("outletid", outletId);
        invoiceParams.put("routeid", routeId);
        invoiceParams.put("lat", latitude);
        invoiceParams.put("lon", longitude);
        invoiceParams.put("bat", batteryLevel);
        invoiceParams.put("discount", discount);
        invoiceParams.put("grossamount", grossAmount);
        invoiceParams.put("returnamount", returnAmount);
        invoiceParams.put("eligibleamount", eligibleAmount);
        invoiceParams.put("marginamount", marginAmount);
        invoiceParams.put("netamount", netAmount);
        invoiceParams.put("lineamount", lineAmount);
        invoiceParams.put("discountpercentage", discountPercentage);
        invoiceParams.put("nextvisitdate", nextVisit);
        invoiceParams.put("locationType", locationType);
        invoiceParams.put("locationAccuracy", locationAccuracy);
        invoiceParams.put("hasFree", hasFree());
        invoiceParams.put("hasSalesReturn", false);
        invoiceParams.put("hasMarketReturn", returnDetails != null && returnDetails.size() > 0);
        invoiceParams.put("paymentMethodCode", paymentMethodCode);
        invoiceParams.put("currentIP",mobileIP);
        try {
            invoiceParams.put("app_version",context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Add the sales order flag to the outgoing JSON
        invoiceParams.put("isSalesOrder", isSalesOrder);

        Date date = new Date(orderTime);

        String combinedDate = sdf.format(date);

        String[] broken = combinedDate.split(" ");

        invoiceParams.put("invDate", broken[0]);
        invoiceParams.put("invtime", broken[1]);

        JSONObject invoiceJSON = new JSONObject(invoiceParams);

        finalJSONParams.put("Invoice", invoiceJSON);

        finalJSONParams.put("posm", new JSONArray());

        JSONArray itemsArray = new JSONArray();

        if (orderDetails != null) {
//            for (int i = 0; i < orderDetails.size(); i++) {
//                JSONObject tmpItemJSON = orderDetails.get(i).getOrderDetailAsJSON(this);
//                if (tmpItemJSON != null) {
//                    itemsArray.put(tmpItemJSON);
//                }
//            }
        }
        finalJSONParams.put("invitems", itemsArray);
        JSONArray returnArray = new JSONArray();
        if (returnDetails != null) {
            for (int i = 0; i < returnDetails.size(); i++) {
                JSONObject tmpItemJSON = returnDetails.get(i).getReturnDetailAsJSON();
                if (tmpItemJSON != null) {
                    returnArray.put(tmpItemJSON);
                }
            }
        }

        finalJSONParams.put("returnitems", returnArray);

        JSONArray freeArray = new JSONArray();
        if (orderDetails != null) {
            for (int i = 0; i < orderDetails.size(); i++) {
//                Log.d("<>","fuck");
//                ItemPromotion freeIssueDetail = orderDetails.get(i).getItemPromotion();
//                if (freeIssueDetail != null) {
////                    Log.d("<>","fuck fuck");
//                    freeArray.put(freeIssueDetail.toJSON(freeIssueDetail));
//                }

            }
        }
        finalJSONParams.put("item_promotions", freeArray);

        if (payments!=null&&payments.size()>0) {
            HashMap<String,Object> pymnts = new HashMap<>();
            JSONArray cashArray = new JSONArray();
            JSONArray cheqArray = new JSONArray();

            for(Payment payment:payments){
                if(payment.isCash()){
                    cashArray.put(payment.getPaymentAsJSON());
                }
                else{
                    cheqArray.put(payment.getPaymentAsJSON());
                }
            }
            pymnts.put("cash",cashArray);
            pymnts.put("cheq",cheqArray);

            finalJSONParams.put("Payment", new JSONObject(pymnts));
        }
        JSONObject finalObject = new JSONObject(finalJSONParams);

        Log.wtf(LOG_TAG, "ORDER JSON\n" + finalObject.toString());

        return new JSONObject(finalJSONParams);
    }

    public double calculateTotalReturns() {

        if (returnDetails != null) {
            double total = 0;
            for (ReturnDetail returnDetail : returnDetails) {
                total += returnDetail.getReturnPrice() * returnDetail.getQty();
            }
            return total;
        }
        return 0;
    }

    public void addFreeIssueDetail(FreeIssueDetail freeIssueDetail) {
        if (freeIssueDetails == null) freeIssueDetails = new ArrayList<>();
        freeIssueDetails.add(freeIssueDetail);
    }

    public boolean hasFree() {
        for(OrderDetail orderDetail:orderDetails){
//            if(orderDetail.getItemPromotion()!=null && (orderDetail.getItemPromotion().getFreeCount()>0)){
//                return true;
//            }
        }
        return false;

    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }
}
