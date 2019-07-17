package com.datamation.sfa.presale;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.sfa.adapter.CustomerDebtAdapter;
import com.datamation.sfa.controller.OrderController;
import com.datamation.sfa.controller.OutstandingController;
import com.datamation.sfa.controller.RouteController;
import com.datamation.sfa.controller.SalRepController;
import com.datamation.sfa.helpers.PreSalesResponseListener;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.FddbNote;
import com.datamation.sfa.model.PRESALE;
import com.datamation.sfa.utils.LocationProvider;
import com.datamation.sfa.view.ActivityHome;
import com.datamation.sfa.R;
import com.datamation.sfa.settings.ReferenceNum;
import com.datamation.sfa.view.PreSalesActivity;
//import com.bit.sfa.Settings.SharedPreferencesClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderHeaderFragment extends Fragment{

    View view;
    private FloatingActionButton next;
    //public static EditText ordno, date, mNo, deldate, remarks;
    public String LOG_TAG = "OrderHeaderFragment";
    TextView lblCustomerName, outStandingAmt, lastBillAmt,lblPreRefno;
    EditText  currnentDate,txtManual,txtRemakrs, txtRoute;
    MyReceiver r;
    SharedPref pref;
    PreSalesResponseListener preSalesResponseListener;
    PreSalesActivity activity;

    public OrderHeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_promo_sale_header, container, false);
        activity = (PreSalesActivity)getActivity();
        next = (FloatingActionButton) view.findViewById(R.id.fab);
        pref = SharedPref.getInstance(getActivity());

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy"); //change this
        String formattedDate = simpleDateFormat.format(d);
        ActivityHome home = new ActivityHome();
        ReferenceNum referenceNum = new ReferenceNum(getActivity());
     //   localSP = new SharedPreferencesClass();

        next = (FloatingActionButton) view.findViewById(R.id.fab);

        lblCustomerName = (TextView) view.findViewById(R.id.customerName);
        outStandingAmt = (TextView) view.findViewById(R.id.lbl_Inv_outstanding_amt);
        lastBillAmt = (TextView) view.findViewById(R.id.lbl_inv_lastbill);
        lblPreRefno = (TextView) view.findViewById(R.id.invoice_no);
        currnentDate = (EditText) view.findViewById(R.id.lbl_InvDate);
        txtManual = (EditText) view.findViewById(R.id.txt_InvManual);
        txtRemakrs = (EditText) view.findViewById(R.id.txt_InvRemarks);
        txtRoute = (EditText)view.findViewById(R.id.txt_route);

        activity.selectedRetDebtor = activity.selectedDebtor;

        lblCustomerName.setText(pref.getSelectedDebName());
        txtRoute.setText(new RouteController(getActivity()).getRouteNameByCode(pref.getSelectedDebRouteCode()));
        currnentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        outStandingAmt.setText(String.format("%,.2f", new OutstandingController(getActivity()).getDebtorBalance(pref.getSelectedDebCode())));
        txtRemakrs.setEnabled(true);
        txtManual.setEnabled(true);

        /*already a header exist*/
        if (activity.selectedPreHed != null) {
            txtManual.setText(activity.selectedPreHed.getORDER_MANUREF());
            txtRemakrs.setText(activity.selectedPreHed.getORDER_REMARKS());
            lblPreRefno.setText(activity.selectedPreHed.getORDER_REFNO());
            //mSaveInvoiceHeader();
        } else { /*No header*/

            lblPreRefno.setText(new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));
            //lblPreRefno.setText("/0001");
        }

        outStandingAmt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.customer_debtor, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Customer outstanding...");
                alertDialogBuilder.setView(promptView);

                final ListView listView = (ListView) promptView.findViewById(R.id.lvCusDebt);
                ArrayList<FddbNote> list = new OutstandingController(getActivity()).getDebtInfo(SharedPref.getInstance(getActivity()).getSelectedDebCode());
                listView.setAdapter(new CustomerDebtAdapter(getActivity(), list));

                alertDialogBuilder.setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lblCustomerName.getText().toString().equals("")|| lblPreRefno.getText().toString().equals("")||txtRoute.getText().toString().equals(""))
                {
                    preSalesResponseListener.moveBackToCustomer_pre(0);
                    Toast.makeText(getActivity(), "Can not proceed without Route...", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //preSalesResponseListener.moveNextToCustomer_pre(1);
                    SaveSalesHeader();
                }

            }
        });

        return view;
    }

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public void SaveSalesHeader() {

        if (lblPreRefno.getText().length() > 0)
        {
            PRESALE hed =new PRESALE();
            hed.setORDER_REFNO(lblPreRefno.getText().toString());
            hed.setORDER_DEBCODE(pref.getSelectedDebCode());
            hed.setORDER_TXNDATE(currnentDate.getText().toString());
            //hed.setORDER_DELIVERY_DATE(deldate.getText().toString());
            hed.setORDER_ROUTECODE(pref.getSelectedDebRouteCode());
            hed.setORDER_MANUREF(txtManual.getText().toString());
            hed.setORDER_REMARKS(txtRemakrs.getText().toString());
            hed.setORDER_IS_ACTIVE("1");
            hed.setORDER_ADDDATE(currnentDate.getText().toString());
            hed.setORDER_ADDTIME(currentTime().split(" ")[1]);
            hed.setORDER_REPCODE(new SalRepController(getActivity()).getCurrentRepCode().trim());
            hed.setORDER_LONGITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("startLongitude"));
            hed.setORDER_LATITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("startLatitude"));

            activity.selectedPreHed = hed;

            ArrayList<PRESALE> ordHedList=new ArrayList<PRESALE>();
            OrderController ordHedDS =new OrderController(getActivity());
            ordHedList.add(hed);

            if (ordHedDS.createOrUpdateOrdHed(ordHedList)>0)
            {
                preSalesResponseListener.moveNextToCustomer_pre(1);
                Toast.makeText(getActivity(),"Order Header Saved...", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void mRefreshHeader() {

        if (SharedPref.getInstance(getActivity()).getGlobalVal("PrekeyCustomer").equals("Y")) {

        currnentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtRoute.setText(new RouteController(getActivity()).getRouteNameByCode(pref.getSelectedDebRouteCode()));
        //deldate.setEnabled(true);
        txtRemakrs.setEnabled(true);
        txtManual.setEnabled(true);
        lblCustomerName.setText(SharedPref.getInstance(getActivity()).getGlobalVal("PrekeyCusName"));
        lblPreRefno.setText(new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));
       // String debCode= new SharedPref(getActivity()).getGlobalVal("PrekeyCusCode");

        if (activity.selectedPreHed != null) {
            //if (home.selectedDebtor == null)
               // home.selectedDebtor = new FmDebtorDS(getActivity()).getSelectedCustomerByCode(home.selectedOrdHed.getFORDHED_DEB_CODE());

//                cusName.setText(home.selectedDebtor.getCusName());
//                ordno.setText(home.selectedOrdHed.getORDHED_REFNO());
//                deldate.setText(home.selectedOrdHed.getORDHED_DELV_DATE());
//                mNo.setText(home.selectedOrdHed.getORDHED_MANU_REF());
//                remarks.setText(home.selectedOrdHed.getORDHED_REMARKS());

        } else {

            lblPreRefno.setText(new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));
            //deldate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            SaveSalesHeader();
        }

        } else {
            Toast.makeText(getActivity(), "Select a customer to continue...", Toast.LENGTH_SHORT).show();
            txtRemakrs.setEnabled(false);
            txtManual.setEnabled(false);
        }

    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    public void onResume() {
        super.onResume();
        r = new OrderHeaderFragment.MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_HEADER"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            OrderHeaderFragment.this.mRefreshHeader();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            preSalesResponseListener = (PreSalesResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }
    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
}
