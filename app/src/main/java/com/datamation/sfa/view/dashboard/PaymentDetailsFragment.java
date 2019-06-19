package com.datamation.sfa.view.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datamation.sfa.R;

import org.json.JSONException;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by TaZ on 4/8/15.
 * Used to show the user a list of recorded payments.
 */
public class PaymentDetailsFragment extends Fragment  {

    private static final String LOG_TAG = PaymentDetailsFragment.class.getSimpleName();
   // private List<PaymentPinHolder> pinHolders;
    //private PaymentListAdapter adapter;
    private TextView tvDate;

//    private DatabaseHandler dbHandler;
//
//    private List<Outlet> outlets;
//
//    private CalendarDatePickerDialog calendarDatePickerDialog;
    private int mYear, mMonth, mDay;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private NumberFormat numberFormat = NumberFormat.getInstance();

    //    private Calendar /*calendarBegin, calendarEnd, */nowCalendar;
    private long timeInMillis;

    private TextView tvGrossAmountTotal;
    private TextView tvNetAmountTotal;
    private TextView tvOutstandingAmountTotal;
    private TextView tvCashPaymentTotal;
    private TextView tvChequeAmountTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_payment_details, container, false);

        timeInMillis = System.currentTimeMillis();

        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

        tvDate = (TextView) rootView.findViewById(R.id.fragment_payment_details_select_date);

        tvGrossAmountTotal = (TextView) rootView.findViewById(R.id.item_payment_details_tv_gross_amount_total);
        tvNetAmountTotal = (TextView) rootView.findViewById(R.id.item_payment_details_tv_net_amount_total);
        tvOutstandingAmountTotal = (TextView) rootView.findViewById(R.id.item_payment_details_tv_outstanding_amount_total);
        tvCashPaymentTotal = (TextView) rootView.findViewById(R.id.item_payment_details_tv_cash_amount_total);
        tvChequeAmountTotal = (TextView) rootView.findViewById(R.id.item_payment_details_tv_cheque_amount_total);

        StickyListHeadersListView pinnedSectionListView = (StickyListHeadersListView) rootView.findViewById(R.id.fragment_payment_details_pslv);

       // dbHandler = DatabaseHandler.getDbHandler(getActivity());

//        try {
//            outlets = dbHandler.getAllOutlets();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        pinHolders = dbHandler.getTimeFramedPayments(TimeUtils.getDayBeginningTime(timeInMillis),
//                TimeUtils.getDayEndTime(timeInMillis));
//
//        adapter = new PaymentListAdapter(getActivity(), pinHolders);
//        pinnedSectionListView.setAdapter(adapter);

        tvDate.setText(dateFormat.format(new Date(timeInMillis)));

//        calendarDatePickerDialog = new CalendarDatePickerDialog();
//        calendarDatePickerDialog.setOnDateSetListener(new CalendarDatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int month, int day) {
//
//                if (year != mYear || month != mMonth || day != mDay) {
//                    Log.d(LOG_TAG, "Different date selected");
//                    mYear = year;
//                    mMonth = month;
//                    mDay = day;
//
////                    nowCalendar.set(mYear, mMonth, mDay);
//
//                    timeInMillis = TimeUtils.parseIntoTimeInMillis(mYear, mMonth, mDay);
//
//                    tvDate.setText(dateFormat.format(new Date(timeInMillis)));
//
//                    pinHolders = dbHandler.getTimeFramedPayments(TimeUtils.getDayBeginningTime(timeInMillis), TimeUtils.getDayEndTime(timeInMillis));
//                    adapter.setPaymentPinHolders(pinHolders);
//                }
//
//            }
//        });
//
//        tvDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calendarDatePickerDialog.show(getFragmentManager(), "TAG");
//            }
//        });

        return rootView;
    }

    public void refresh() {
     //   if (adapter != null) adapter.notifyDataSetChanged();
    }

//    public void showCalendar() {
//        if (calendarDatePickerDialog != null)
//            calendarDatePickerDialog.show(getFragmentManager(), "TAG");
//    }

//    @Override
//    public void onFragmentVisible(DashboardContainerFragment dashboardContainerFragment) {
//        dashboardContainerFragment.currentFragment = this;
//    }

    private static class HeaderViewHolder {
        TextView pinLabel;
    }

    private static class ViewHolder {
        TextView tvInvoiceDetails;
        TextView tvGrossAmount;
        TextView tvNetAmount;
        TextView tvOutstandingAmount;
        TextView tvCashPayment;
        TextView tvChequeAmount;
    }
//
//    private class PaymentListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
//
//        private LayoutInflater inflater;
//       // private List<PaymentPinHolder> paymentPinHolders;
//
//        private PaymentListAdapter(Context context, List<PaymentPinHolder> paymentPinHolders) {
//            this.paymentPinHolders = paymentPinHolders;
//            this.inflater = LayoutInflater.from(context);
//        }
//
//        @SuppressLint("InflateParams")
//        @Override
//        public View getHeaderView(int position, View view, ViewGroup viewGroup) {
//
//            HeaderViewHolder headerViewHolder;
//            if (view == null) {
//                view = inflater.inflate(R.layout.item_payment_details_header, null, false);
//
//                headerViewHolder = new HeaderViewHolder();
//                headerViewHolder.pinLabel = (TextView) view.findViewById(R.id.item_payment_details_tv_pin_txt);
//
//                view.setTag(headerViewHolder);
//            } else {
//                headerViewHolder = (HeaderViewHolder) view.getTag();
//            }
//
//            String label;
//
//            if (paymentPinHolders.get(position).getType() == PaymentPinHolder.TYPE_DAY) {
//                // Selected day invoice
//                label = "Payments of the date " + dateFormat.format(paymentPinHolders.get(position).getHistoryDetail().getDate());
//            } else {
//                // Earlier invoice
//                label = "Payments of earlier invoices ";
//            }
//
//            headerViewHolder.pinLabel.setText(label);
//
//            return view;
//        }
//
//        @Override
//        public long getHeaderId(int position) {
//            if (paymentPinHolders != null) return paymentPinHolders.get(position).getType();
//            return 0;
//        }
//
//        @Override
//        public int getCount() {
//            if (paymentPinHolders != null) return paymentPinHolders.size();
//            return 0;
//        }
//
//        @Override
//        public PaymentPinHolder getItem(int position) {
//            if (paymentPinHolders != null) return paymentPinHolders.get(position);
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @SuppressLint("InflateParams")
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ViewHolder viewHolder;
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.item_payment_details, null, false);
//
//                viewHolder = new ViewHolder();
//                viewHolder.tvInvoiceDetails = (TextView) convertView.findViewById(R.id.item_payment_details_tv_invoice);
//                viewHolder.tvGrossAmount = (TextView) convertView.findViewById(R.id.item_payment_details_tv_gross_amount);
//                viewHolder.tvNetAmount = (TextView) convertView.findViewById(R.id.item_payment_details_tv_net_amount);
//                viewHolder.tvOutstandingAmount = (TextView) convertView.findViewById(R.id.item_payment_details_tv_outstanding_amount);
//                viewHolder.tvCashPayment = (TextView) convertView.findViewById(R.id.item_payment_details_tv_cash_amount);
//                viewHolder.tvChequeAmount = (TextView) convertView.findViewById(R.id.item_payment_details_tv_cheque_amount);
//
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//
////            viewHolder.rlContainer.setVisibility(View.VISIBLE);
//
//            String invoiceDetails = "";
//
//            Outlet selectedOutlet = null;
//            for (Outlet outlet : outlets) {
//                if (outlet.getOutletId() == paymentPinHolders.get(position).getHistoryDetail().getOutletId()) {
//                    selectedOutlet = outlet;
//                    break;
//                }
//            }
//
//            if (selectedOutlet != null) {
//                invoiceDetails = selectedOutlet.getOutletName();
//            }
//
//            Invoice invoice = paymentPinHolders.get(position).getHistoryDetail().getInvoice();
//
//            if (invoice != null) {
//                if (paymentPinHolders.get(position).getType() == PaymentPinHolder.TYPE_OTHER) {
//                    // Not a selected day invoice. Therefore need to append the date
//                    invoiceDetails += "\n" + dateFormat.format(new Date(invoice.getInvoiceTime()));
//                }
//
//                invoiceDetails += "\n" + String.valueOf(invoice.getInvoiceId());
//
//                if(invoice.getInvoiceType() == Invoice.OPEN_BALANCE) {
//                    invoiceDetails += "*";
//                }
//
//                viewHolder.tvInvoiceDetails.setText(invoiceDetails);
//
//                viewHolder.tvGrossAmount.setText(numberFormat.format(invoice.getTotalAmount()));
//                viewHolder.tvNetAmount.setText(numberFormat.format(invoice.getTotalAmount()
//                        - invoice.getTotalDiscount()
//                        - invoice.getReturnAmount()));
//
//                viewHolder.tvOutstandingAmount.setText(numberFormat.format(invoice.getTotalAmount()
//                        - invoice.getTotalDiscount()
//                        - invoice.getReturnAmount()
//                        - invoice.getTotalPaidAmount()));
//
//                if (invoice.getCashPayments() != null) {
//                    List<CashPayment> cashPayments = invoice.getCashPayments();
//
//                    double total = 0;
//                    for (CashPayment cashPayment : cashPayments) {
//                        total += cashPayment.getPaymentAmount();
//                    }
//
//                    viewHolder.tvCashPayment.setText(numberFormat.format(total));
//
//                } else {
//                    viewHolder.tvCashPayment.setText("");
//                }
//
//                if (invoice.getChequePayments() != null) {
//                    List<Cheque> chequePayments = invoice.getChequePayments();
//
//                    double total = 0;
//                    for (Cheque cheque : chequePayments) {
//                        total += cheque.getAmount();
//                    }
//
//                    viewHolder.tvChequeAmount.setText(numberFormat.format(total));
//
//                } else {
//                    viewHolder.tvChequeAmount.setText("");
//                }
//            }
//
//            return convertView;
//        }
//
//        public void setPaymentPinHolders(List<PaymentPinHolder> pinHolderList) {
//            this.paymentPinHolders = pinHolderList;
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public void notifyDataSetChanged() {
//            super.notifyDataSetChanged();
//
//            double grossAmount = 0;
//            double netAmount = 0;
//            double outstandingAmount = 0;
//            double cashAmount = 0;
//            double chequeAmount = 0;
//
//            for(PaymentPinHolder pinHolder : paymentPinHolders) {
//                Invoice invoice = pinHolder.getHistoryDetail().getInvoice();
//                if(invoice != null) {
//
//                    grossAmount += invoice.getTotalAmount();
//                    netAmount += invoice.getNetAmount();
//                    outstandingAmount += (invoice.getNetAmount() - invoice.getTotalPaidAmount());
//                    cashAmount += invoice.getTotalCashPayments();
//                    chequeAmount += invoice.getTotalChequePayments();
//
//                }
//            }
//
//            tvGrossAmountTotal.setText(numberFormat.format(grossAmount));
//            tvNetAmountTotal.setText(numberFormat.format(netAmount));
//            tvOutstandingAmountTotal.setText(numberFormat.format(outstandingAmount));
//            tvCashPaymentTotal.setText(numberFormat.format(cashAmount));
//            tvChequeAmountTotal.setText(numberFormat.format(chequeAmount));
//
//        }
//    }

}
