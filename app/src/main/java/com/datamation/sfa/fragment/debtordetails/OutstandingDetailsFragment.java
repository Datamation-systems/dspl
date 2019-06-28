package com.datamation.sfa.fragment.debtordetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datamation.sfa.R;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.HistoryDetail;
import com.datamation.sfa.model.Invoice;
import com.datamation.sfa.utils.ValueHolder;

import org.json.JSONException;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class OutstandingDetailsFragment extends Fragment
{

    private DatabaseHelper dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outstanding_details, container, false);

        ListView listView = (ListView) view.findViewById(R.id.outlet_details_outstanding_listview);

        //dbHandler = DatabaseHelper.getDbHandler(getActivity());
        SharedPref pref = SharedPref.getInstance(getActivity());

//        try {
////            outlet = dbHandler.getOutletOfId(pref.getSelectedOutletId());
////
////            if(outlet != null) {
////                historyDetails = dbHandler.getOutstandingPayments(outlet.getOutletId());
////                adapter = new OutletOutstandingAdapter(getActivity(), historyDetails);
////                listView.setAdapter(adapter);
////            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return view;
    }

    public void refreshValues() {
//        if(adapter != null && outlet != null) {
//            historyDetails = dbHandler.getOutstandingPayments(outlet.getOutletId());
//            adapter.setHistoryDetails(historyDetails);
//        }
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


    private class OutletOutstandingAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private List<HistoryDetail> historyDetails;

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        private NumberFormat numberFormat = NumberFormat.getInstance();

        private OutletOutstandingAdapter(Context context, List<HistoryDetail> historyDetails) {
            this.inflater = LayoutInflater.from(context);
            this.historyDetails = historyDetails;

            numberFormat.setGroupingUsed(true);
            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(2);

        }

        @Override
        public int getCount() {
            if(historyDetails != null) return historyDetails.size();
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

            if(invoice != null) {

                String invId = String.valueOf(invoice.getInvoiceId());
                if(invoice.getInvoiceType() == Invoice.OPEN_BALANCE) {
                    invId = invId + "*";
                }

                viewHolder.invoiceId.setText(invId);
                viewHolder.invoiceDate.setText(sdf.format(new Date(invoice.getInvoiceTime())));
                viewHolder.invoiceOutstanding.setText(numberFormat.format(invoice.getTotalAmount() - invoice.getTotalDiscount() - invoice.getTotalPaidAmount() - invoice.getReturnAmount()));
                viewHolder.invoiceGross.setText(numberFormat.format(invoice.getTotalAmount()));
                viewHolder.invoiceNet.setText(numberFormat.format(invoice.getTotalAmount() - invoice.getTotalDiscount() - invoice.getReturnAmount()));

                if(invoice.getInvoiceTime() > 0) {
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
            notifyDataSetChanged();
        }

    }
}
