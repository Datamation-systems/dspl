package com.datamation.sfa.fragment.debtordetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datamation.sfa.R;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.helpers.NetworkFunctions;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.HistoryDetail;
import com.datamation.sfa.model.Invoice;
import com.datamation.sfa.utils.ValueHolder;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoryDetailsFragment_old extends Fragment
{

    private DatabaseHelper dbHandler;
    private SharedPref pref;

    private OutletHistoryAdapter adapter;
    private Customer outlet;
    private NetworkFunctions networkFunctions;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_details, container, false);

        pref = SharedPref.getInstance(getActivity());
        //dbHandler = DatabaseHandler.getDbHandler(getActivity());
        networkFunctions = new NetworkFunctions(HistoryDetailsFragment_old.this.getActivity());

        return view;
    }

//    private class OrderDetailFetcher extends AsyncTask<Void, Void, Boolean> {
//
//        private final long invoiceId;
//        private CustomProgressDialog customProDialog;
//        private Order order = null;
//
//        public OrderDetailFetcher(long invoiceId) {
//            this.invoiceId = invoiceId;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            this.customProDialog = new CustomProgressDialog(getActivity());
//            this.customProDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            this.customProDialog.show();
//            this.customProDialog.setMessage("Fetching order details from the server...");
//
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
////            int locationId = pref.getLoginUser().getLocationId();
//            try {
//                String response = networkFunctions.fetchOrderDetails(invoiceId);
//                JSONObject responseJSON = new JSONObject(response);
//                JSONObject orderHead = responseJSON.getJSONArray("order_head").getJSONObject(0);
//
////                int has_free = orderHead.getInt("has_free");
//                double location_accuracy = 0;
//                if (!orderHead.getString("location_accuracy").isEmpty()) {
//                    location_accuracy = orderHead.getDouble("location_accuracy");
//                }
////                long sales_order_code = orderHead.getLong("sales_order_code");
////                int id_store = orderHead.getInt("id_store");
//                int battery_level = orderHead.getInt("battery_level");
//                double gps_latitude = orderHead.getDouble("gps_latitude");
//                double gps_longitude = orderHead.getDouble("gps_longitude");
////                int id_invoice = orderHead.getInt("id_invoice");
//                String location_type = orderHead.getString("location_type");
//                double discount = Double.parseDouble(orderHead.getString("discount").replace(",", ""));
////                int id_sales_order = orderHead.getInt("id_sales_order");
//                String added_date = orderHead.getString("added_date");
//                String added_time = orderHead.getString("added_time");
//                double market_return = Double.parseDouble(orderHead.getString("market_return").replace(",", ""));
//                double sales_amount = Double.parseDouble(orderHead.getString("sales_amount").replace(",", ""));
////                double return_amount = Double.parseDouble(orderHead.getString("return_amount").replace(",", ""));
//                double total = Double.parseDouble(orderHead.getString("total").replace(",", ""));
////                double outstanding = Double.parseDouble(orderHead.getString("outstanding").replace(",", ""));
//                double marginAmount = Double.parseDouble(orderHead.getString("margin_amount").replace(",", ""));
//                double eligibleAmount = Double.parseDouble(orderHead.getString("eligible_amount").replace(",", ""));
//                double discountPercentage = Double.parseDouble(orderHead.getString("discount_percentage").replace(",", ""));
//
//                /*
//                keyOrderId, order.getOr
//                keyOutletId, order.getO
//                keyLatitude, order.getL
//                keyLongitude, order.get
//                keyLocationType, order.
//                keyLocationAccuracy, or
//                keyOrderDiscount, order
//                keyOrderTime, order.get
//                keyBatteryLevel, order.
//                keyGrossAmount, order.g
//                keyReturnAmount, order.
//                keyEligibleAmount, orde
//                keyMarginAmount, order.
//                keyNetAmount, order.get
//                keyDiscountPercentage,
//                keyNextVisitDate, order
//                flagIsSynced, order.isS
//                flagHasFree, order.hasF
//                */
//
//                order = new Order();
//                order.setOrderId(invoiceId);
//                order.setOutletId(pref.getSelectedOutletId());
//                order.setLatitude(gps_latitude);
//                order.setLongitude(gps_longitude);
//                if (location_type.equals("GPS")) {
//                    order.setLocationType(LocationProvider.LOCATION_TYPE_GPS);
//                } else {
//                    order.setLocationType(LocationProvider.LOCATION_TYPE_NETWORK);
//                }
//                order.setLocationAccuracy(location_accuracy);
//                order.setDiscount(discount);
//
//                long extractedTime = TimeUtils.extractTimeFromInvoiceId(order.getOrderId());
//                if (extractedTime > 0) {
//                    order.setOrderTime(extractedTime);
//                } else {
//                    SimpleDateFormat simpleDateFmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
//                    try {
//                        Date parsedDate = simpleDateFmt.parse(added_date.concat(" ").concat(added_time));
//                        order.setOrderTime(parsedDate.getTime());
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                order.setBatteryLevel(battery_level);
//                order.setGrossAmount(sales_amount);
//                order.setNetAmount(total);
//                order.setMarginAmount(marginAmount);
//                order.setEligibleAmount(eligibleAmount);
//                order.setDiscountPercentage(discountPercentage);
//                order.setReturnAmount(market_return);
//                order.setSynced(true);
//
//                JSONArray salesDetailsJSONArray = responseJSON.getJSONArray("sales_details");
//                List<OrderDetail> orderDetailList = new ArrayList<>();
//                for (int i = 0; i < salesDetailsJSONArray.length(); i++) {
//                    JSONObject saleItemJSONObject = salesDetailsJSONArray.getJSONObject(i);
//                    int itemId = saleItemJSONObject.getInt("iditem");
//                    double itemQty = saleItemJSONObject.getDouble("item_qty");
//                    double price = saleItemJSONObject.getDouble("unit_price");
////                    Item item = dbHandler.getItem(itemId);
////                    item.setSelectedQty(itemQty);
//                    //set item wholesale price
////                    OrderDetail orderDetail = new OrderDetail(invoiceId, item, price);
//                    OrderDetail orderDetail = null;
//
//                    orderDetailList.add(orderDetail);
//                }
//                order.setOrderDetails(orderDetailList);
//
//                JSONArray returnDetailsJSONArray = responseJSON.getJSONArray("market_details");
//                List<ReturnDetail> returnDetailList = new ArrayList<>();
//                for (int i = 0; i < returnDetailsJSONArray.length(); i++) {
//                    JSONObject returnItemJSONObject = returnDetailsJSONArray.getJSONObject(i);
//                    int itemId = returnItemJSONObject.getInt("iditem");
//                    int itemQty = (int) returnItemJSONObject.getDouble("item_qty");
//                    double price = returnItemJSONObject.getDouble("unit_price");
////                    Item item = dbHandler.getItem(itemId);
////                    item.setReturnQty(itemQty);
////                    item.setSelectedReturnPrice(price);//a parameter should comes with the value
//                    Batch batch = new Batch();
//                    batch.setBatchNo("Not found");
//                    //batch.setPrice(item.getSelectedReturnPrice());
//                    //ReturnDetail returnDetail = new ReturnDetail(invoiceId, item, itemQty, batch);
//                    //returnDetailList.add(returnDetail);
//                }
//                order.setReturnDetails(returnDetailList);
//
//                JSONArray freeDetailsJSONArray = responseJSON.getJSONArray("free_item");
//                List<FreeIssueDetail> freeDetailList = new ArrayList<>();
//                for (int i = 0; i < freeDetailsJSONArray.length(); i++) {
//                    JSONObject freeItemJSONObject = freeDetailsJSONArray.getJSONObject(i);
//                    int itemId = freeItemJSONObject.getInt("iditem");
//                    int itemQty = (int) freeItemJSONObject.getDouble("item_qty");
//                    int calculatedQty = (int) freeItemJSONObject.getDouble("calculated_qty");
//                    //Item item = dbHandler.getItem(itemId);
////                    FreeIssueDetail freeIssueDetail = new FreeIssueDetail(invoiceId, item, calculatedQty, itemQty, 0);
////                    freeDetailList.add(freeIssueDetail);
//                }
//                order.setFreeIssueDetails(freeDetailList);
//
//                //291434452441709
//
//                return true;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean finalState) {
//            super.onPostExecute(finalState);
//            if (this.customProDialog.isShowing()) {
//                this.customProDialog.dismiss();
//            }
//            if (finalState) {
////                dbHandler.storeDownloadedOrderDetail(order);
////                Intent intent = new Intent(getActivity(), InvoiceReprintActivity.class);
////                intent.putExtra("order", order);
////                startActivity(intent);
//            }
//        }
//    }

    public void refreshValues() {
        if (adapter != null) {
//            try {
////                outlet = dbHandler.getOutletOfId(pref.getSelectedOutletId());
////                if (outlet != null) adapter.setHistoryDetails(outlet.getOutletHistory());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

    private static class ViewHolder {
        RelativeLayout layout;
        TextView invoiceId;
        TextView invoiceDate;
        TextView invoiceGross;
        TextView invoiceNet;
        TextView invoiceOutstanding;
        TextView invoiceDays;
    }

    private class OutletHistoryAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private List<HistoryDetail> historyDetails;

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        private NumberFormat numberFormat = NumberFormat.getInstance();

        private OutletHistoryAdapter(Context context, List<HistoryDetail> historyDetails) {
//            this.context = context;
            this.historyDetails = historyDetails;
            this.inflater = LayoutInflater.from(context);

            numberFormat.setGroupingUsed(true);
            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(2);

        }

        @Override
        public int getCount() {
            if (historyDetails != null) return historyDetails.size();
            return 0;
        }

        @Override
        public HistoryDetail getItem(int position) {
            return historyDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return historyDetails.get(position).getDate();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_outlet_invoice, parent, false);
                viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.item_payment_invoice_layout);
                viewHolder.invoiceId = (TextView) convertView.findViewById(R.id.item_payment_invoice_id);
                viewHolder.invoiceDate = (TextView) convertView.findViewById(R.id.item_payment_invoice_date);
                viewHolder.invoiceGross = (TextView) convertView.findViewById(R.id.item_payment_invoice_gross);
                viewHolder.invoiceNet = (TextView) convertView.findViewById(R.id.item_payment_invoice_net);
                viewHolder.invoiceOutstanding = (TextView) convertView.findViewById(R.id.item_payment_invoice_outstanding);
                viewHolder.invoiceDays = (TextView) convertView.findViewById(R.id.item_payment_invoice_days);

//                convertView.findViewById(R.id.item_payment_invoice_checkbox).setVisibility(View.INVISIBLE);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            HistoryDetail detail = historyDetails.get(position);
            Invoice invoice = detail.getInvoice();

            if (invoice != null) {

                String invId = String.valueOf(invoice.getInvoiceId());
                if (invoice.getInvoiceType() == Invoice.OPEN_BALANCE) {
                    invId = invId + "*";
                }

                viewHolder.invoiceId.setText(invId);
                viewHolder.invoiceDate.setText(sdf.format(new Date(invoice.getInvoiceTime())));
                viewHolder.invoiceOutstanding.setText(numberFormat.format(invoice.getTotalAmount() - invoice.getTotalDiscount() - invoice.getTotalPaidAmount() - invoice.getReturnAmount()));
                viewHolder.invoiceGross.setText(numberFormat.format(invoice.getTotalAmount()-invoice.getTotalDiscount()));
                viewHolder.invoiceNet.setText(numberFormat.format(invoice.getTotalAmount() - invoice.getTotalDiscount() - invoice.getReturnAmount()));

                if (invoice.getInvoiceTime() > 0) {
                    // Calculate the number of days since
                    int numOfDays = (int) ((System.currentTimeMillis() - invoice.getInvoiceTime()) / ValueHolder.DAY_IN_MILLIS);
                    viewHolder.invoiceDays.setText(String.valueOf(numOfDays));
                } else {
                    viewHolder.invoiceDays.setText("N/A");
                }

            }

            return convertView;
        }

        public void setHistoryDetails(List<HistoryDetail> historyDetails) {
            this.historyDetails = historyDetails;
        }
    }
}
