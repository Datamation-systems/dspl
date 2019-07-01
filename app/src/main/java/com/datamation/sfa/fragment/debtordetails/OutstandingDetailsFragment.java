package com.datamation.sfa.fragment.debtordetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datamation.sfa.R;
import com.datamation.sfa.controller.CustomerController;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.FddbNote;
import com.datamation.sfa.model.HistoryDetail;
import com.datamation.sfa.model.Invoice;
import com.datamation.sfa.utils.ValueHolder;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.text.format.DateUtils.DAY_IN_MILLIS;


public class OutstandingDetailsFragment extends Fragment
{

    private DatabaseHelper dbHandler;
    private String debtorCode;
    OutletOutstandingAdapter arrayAdapter;
    ArrayList<FddbNote>historyDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outstanding_details, container, false);

        ListView listView = (ListView) view.findViewById(R.id.outlet_details_outstanding_listview);
        SharedPref pref = SharedPref.getInstance(getActivity());

        try {

            debtorCode = pref.getSelectedDebCode();
            //debtorCode = "9";

            if(debtorCode != null) {
                historyDetail = new CustomerController(getActivity()).getOutStandingList(debtorCode);
                arrayAdapter = new OutletOutstandingAdapter(getActivity(), historyDetail);
                listView.setAdapter(arrayAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
        com.datamation.sfa.utils.CustomFont invoiceId;
        com.datamation.sfa.utils.CustomFont invoiceDate;
        com.datamation.sfa.utils.CustomFont invoiceAmt;
        com.datamation.sfa.utils.CustomFont invoiceBalance;
        com.datamation.sfa.utils.CustomFont invoiceDays;
    }


    private class OutletOutstandingAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        private List<FddbNote> fddbNotes;

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        private NumberFormat numberFormat = NumberFormat.getInstance();

        private OutletOutstandingAdapter(Context context, List<FddbNote> historyDetails) {
            this.inflater = LayoutInflater.from(context);
            this.fddbNotes = historyDetails;

        }

        @Override
        public int getCount() {
            if(fddbNotes != null) return fddbNotes.size();
            return 0;
        }

        @Override
        public FddbNote getItem(int position) {
            return fddbNotes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return fddbNotes.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row_fddnote_details, parent, false);
                //viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.item_payment_invoice_layout);
                viewHolder.invoiceId = (com.datamation.sfa.utils.CustomFont) convertView.findViewById(R.id.row_refno);
                viewHolder.invoiceDate = (com.datamation.sfa.utils.CustomFont) convertView.findViewById(R.id.row_txndate);
                viewHolder.invoiceAmt = (com.datamation.sfa.utils.CustomFont) convertView.findViewById(R.id.row_dueAmt);
                viewHolder.invoiceBalance = (com.datamation.sfa.utils.CustomFont) convertView.findViewById(R.id.row_Amt);
                viewHolder.invoiceDays = (com.datamation.sfa.utils.CustomFont) convertView.findViewById(R.id.days);

//                convertView.findViewById(R.id.item_payment_invoice_checkbox).setVisibility(View.INVISIBLE);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

//            HistoryDetail detail = historyDetails.get(position);
//            Invoice invoice = detail.getInvoice();

            if(fddbNotes != null) {

                viewHolder.invoiceId.setText(fddbNotes.get(position).getRefNo());
                viewHolder.invoiceDate.setText(fddbNotes.get(position).getTxnDate());
                viewHolder.invoiceAmt.setText(fddbNotes.get(position).getAmt());
                viewHolder.invoiceBalance.setText(fddbNotes.get(position).getAmt());

                Date date;
                long txn = 0;
                try {
                    date = (Date)formatter.parse(fddbNotes.get(position).getTxnDate());
                    System.out.println("receipt date is " +date.getTime());
                    txn = date.getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int numOfDays =   (int) ((System.currentTimeMillis()  - txn) / DAY_IN_MILLIS);
                //viewHolder.lblDays.setText(""+numOfDays);
                viewHolder.invoiceDays.setText(""+numOfDays);

                }

            return convertView;
        }

    }

//    private class OutletOutstandingAdapter extends BaseAdapter {
//
//        private LayoutInflater inflater;
//
//        private List<HistoryDetail> historyDetails;
//
//        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        private NumberFormat numberFormat = NumberFormat.getInstance();
//
//        private OutletOutstandingAdapter(Context context, List<HistoryDetail> historyDetails) {
//            this.inflater = LayoutInflater.from(context);
//            this.historyDetails = historyDetails;
//
//            numberFormat.setGroupingUsed(true);
//            numberFormat.setMinimumFractionDigits(2);
//            numberFormat.setMaximumFractionDigits(2);
//
//        }
//
//        @Override
//        public int getCount() {
//            if(historyDetails != null) return historyDetails.size();
//            return 0;
//        }
//
//        @Override
//        public HistoryDetail getItem(int position) {
//            return historyDetails.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return historyDetails.get(position).getDate();
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ViewHolder viewHolder;
//
//            if (convertView == null) {
//                viewHolder = new ViewHolder();
//                convertView = inflater.inflate(R.layout.item_outlet_invoice, parent, false);
//                viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.item_payment_invoice_layout);
//                viewHolder.invoiceId = (TextView) convertView.findViewById(R.id.item_payment_invoice_id);
//                viewHolder.invoiceDate = (TextView) convertView.findViewById(R.id.item_payment_invoice_date);
//                viewHolder.invoiceGross = (TextView) convertView.findViewById(R.id.item_payment_invoice_gross);
//                viewHolder.invoiceNet = (TextView) convertView.findViewById(R.id.item_payment_invoice_net);
//                viewHolder.invoiceOutstanding = (TextView) convertView.findViewById(R.id.item_payment_invoice_outstanding);
//                viewHolder.invoiceDays = (TextView) convertView.findViewById(R.id.item_payment_invoice_days);
//
////                convertView.findViewById(R.id.item_payment_invoice_checkbox).setVisibility(View.INVISIBLE);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//
//            HistoryDetail detail = historyDetails.get(position);
//            Invoice invoice = detail.getInvoice();
//
//            if(invoice != null) {
//
//                String invId = String.valueOf(invoice.getInvoiceId());
//                if(invoice.getInvoiceType() == Invoice.OPEN_BALANCE) {
//                    invId = invId + "*";
//                }
//
//                viewHolder.invoiceId.setText(invId);
//                viewHolder.invoiceDate.setText(sdf.format(new Date(invoice.getInvoiceTime())));
//                viewHolder.invoiceOutstanding.setText(numberFormat.format(invoice.getTotalAmount() - invoice.getTotalDiscount() - invoice.getTotalPaidAmount() - invoice.getReturnAmount()));
//                viewHolder.invoiceGross.setText(numberFormat.format(invoice.getTotalAmount()));
//                viewHolder.invoiceNet.setText(numberFormat.format(invoice.getTotalAmount() - invoice.getTotalDiscount() - invoice.getReturnAmount()));
//
//                if(invoice.getInvoiceTime() > 0) {
//                    // Calculate the number of days since
//                    int numOfDays = (int) ((System.currentTimeMillis() - invoice.getInvoiceTime()) / ValueHolder.DAY_IN_MILLIS);
//                    viewHolder.invoiceDays.setText(String.valueOf(numOfDays));
//                } else {
//                    viewHolder.invoiceDays.setText("N/A");
//                }
//            }
//
//            return convertView;
//        }
//
//        public void setHistoryDetails(List<HistoryDetail> historyDetails) {
//            this.historyDetails = historyDetails;
//            notifyDataSetChanged();
//        }
//
//    }
}
