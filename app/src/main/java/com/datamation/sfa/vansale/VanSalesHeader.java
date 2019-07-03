package com.datamation.sfa.vansale;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.datamation.sfa.R;
import com.datamation.sfa.helpers.SharedPref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VanSalesHeader extends Fragment {

    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    View view;
    SharedPref mSharedPref;

    TextView lblCustomerName, outStandingAmt, lastBillAmt;
    EditText lblInvRefno, currnentDate,txtManual,txtRemakrs;
    MyReceiver r;
    Spinner spnPayMethod;
    //EditText spnPayMethods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sales_management_van_sales_header, container, false);
        localSP = getActivity().getSharedPreferences(SETTINGS, 0);
      //  activity = (MainActivity) getActivity();
        mSharedPref = new SharedPref(getActivity());

        lblCustomerName = (TextView) view.findViewById(R.id.customerName);
        outStandingAmt = (TextView) view.findViewById(R.id.lbl_Inv_outstanding_amt);
        lastBillAmt = (TextView) view.findViewById(R.id.lbl_inv_lastbill);
        lblInvRefno = (EditText) view.findViewById(R.id.lbl_InvRefno);
        currnentDate = (EditText) view.findViewById(R.id.lbl_InvDate);
        txtManual = (EditText) view.findViewById(R.id.txt_InvManual);
        txtRemakrs = (EditText) view.findViewById(R.id.txt_InvRemarks);
        spnPayMethod = (Spinner) view.findViewById(R.id.spnnerPayment);
     //   spnPayMethods = (EditText) view.findViewById(R.id.spnnerPayment);
       /*
       comment by dhanushika
       txtRemakrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputDialogbox(txtRemakrs.getText().toString(), "R");
            }
        });

        *//*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*//*

        txtManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputDialogbox(txtManual.getText().toString(), "M");
            }
        });*/

	/*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//        outStandingAmt.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//                View promptView = layoutInflater.inflate(R.layout.customer_debtor, null);
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                alertDialogBuilder.setTitle("Customer outstanding...");
//                alertDialogBuilder.setView(promptView);
//
////                final ListView listView = (ListView) promptView.findViewById(R.id.lvCusDebt);
////                ArrayList<FDDbNote> list = new FDDbNoteDS(getActivity()).getDebtInfo(activity.selectedDebtor.getFDEBTOR_CODE());
////                listView.setAdapter(new CustomerDebtAdapter(getActivity(), list));
//
//                alertDialogBuilder.setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
//        });

        List<String> listPayType = new ArrayList<String>();
        // listPayType.add("-SELECT PAYMENT TYPE-");
        listPayType.add("CASH");
        listPayType.add("CREDIT");
        listPayType.add("OTHER");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, listPayType);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPayMethod.setAdapter(dataAdapter1);

        spnPayMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                new SharedPref(getActivity()).setGlobalVal("KeyPayType", spnPayMethod.getSelectedItem().toString());
                Log.v("PAYMENT TYPE", spnPayMethod.getSelectedItem().toString());
              //  mSaveInvoiceHeader();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        return view;
    }

	/*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//    public void mSaveInvoiceHeader() {
//
//        if (lblInvRefno.getText().length() > 0) {
//
//            InvHed hed = new InvHed();
//            hed.setFINVHED_REFNO(lblInvRefno.getText().toString());
//            hed.setFINVHED_ADDDATE(currnentDate.getText().toString());
//            hed.setFINVHED_MANUREF(txtManual.getText().toString());
//            hed.setFINVHED_REMARKS(txtRemakrs.getText().toString());
//            hed.setFINVHED_ADDMACH(localSP.getString("MAC_Address", "No MAC Address").toString());
//            hed.setFINVHED_ADDUSER(new SalRepDS(getActivity()).getCurrentRepCode());
//            hed.setFINVHED_CURCODE("LKR");
//            hed.setFINVHED_CURRATE("1.00");
//            hed.setFINVHED_REPCODE(new SalRepDS(getActivity()).getCurrentRepCode());
//
//            if (activity.selectedDebtor != null) {
//                hed.setFINVHED_DEBCODE(activity.selectedDebtor.getFDEBTOR_CODE());
//                hed.setFINVHED_CONTACT(activity.selectedDebtor.getFDEBTOR_MOB());
//                hed.setFINVHED_CUSADD1(activity.selectedDebtor.getFDEBTOR_ADD1());
//                hed.setFINVHED_CUSADD2(activity.selectedDebtor.getFDEBTOR_ADD2());
//                hed.setFINVHED_CUSADD3(activity.selectedDebtor.getFDEBTOR_ADD3());
//                hed.setFINVHED_CUSTELE(activity.selectedDebtor.getFDEBTOR_TELE());
//                hed.setFINVHED_TAXREG(activity.selectedDebtor.getFDEBTOR_TAX_REG());
//            }
//
//            hed.setFINVHED_TXNTYPE("22");
//            hed.setFINVHED_TXNDATE(currnentDate.getText().toString());
//            hed.setFINVHED_IS_ACTIVE("1");
//            hed.setFINVHED_IS_SYNCED("0");
//            hed.setFINVHED_TOURCODE(new SharedPref(getActivity()).getGlobalVal("KeyTouRef"));
//           // hed.setFINVHED_AREACODE(new SharedPref(getActivity()).getGlobalVal("KeyAreaCode"));
//            hed.setFINVHED_AREACODE(activity.selectedDebtor.getFDEBTOR_AREA_CODE());
//           // hed.setFINVHED_LOCCODE(new SharedPref(getActivity()).getGlobalVal("KeyLocCode"));
//            hed.setFINVHED_LOCCODE(new SalRepDS(getActivity()).getCurrentLocCode());
//            hed.setFINVHED_ROUTECODE(new SharedPref(getActivity()).getGlobalVal("KeyRouteCode"));
//            hed.setFINVHED_PAYTYPE(new SharedPref(getActivity()).getGlobalVal("KeyPayType"));
//            hed.setFINVHED_COSTCODE("");
//            hed.setFINVHED_START_TIME_SO(currentTime());
//            if(activity.selectedDebtor.getFDEBTOR_TAX_REG().equals("Y")){
//                hed.setFINVHED_SETTING_CODE(getResources().getString(R.string.VanNumValTax));
//            }else{
//                hed.setFINVHED_SETTING_CODE(getResources().getString(R.string.VanNumValNonTax));
//            }
//
//            activity.selectedInvHed = hed;
//            SharedPreferencesClass.setLocalSharedPreference(activity, "Van_Start_Time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
//            ArrayList<InvHed> ordHedList = new ArrayList<>();
//            ordHedList.add(activity.selectedInvHed);
//            new InvHedDS(getActivity()).createOrUpdateInvHed(ordHedList);
//        }
//    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//    public void mRefreshHeader() {
//
//        if (mSharedPref.getGlobalVal("keyCustomer").equals("Y")) {
//
//            lblCustomerName.setText(activity.selectedDebtor.getFDEBTOR_NAME());
//            activity.selectedRetDebtor = activity.selectedDebtor;
//            currnentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//            outStandingAmt.setText(String.format("%,.2f", new FDDbNoteDS(getActivity()).getDebtorBalance(activity.selectedDebtor.getFDEBTOR_CODE())));
//            txtRemakrs.setEnabled(true);
//            txtManual.setEnabled(true);
//
//            /*already a header exist*/
//            if (activity.selectedInvHed != null) {
//                txtManual.setText(activity.selectedInvHed.getFINVHED_MANUREF());
//                txtRemakrs.setText(activity.selectedInvHed.getFINVHED_REMARKS());
//                lblInvRefno.setText(activity.selectedInvHed.getFINVHED_REFNO());
//                mSaveInvoiceHeader();
//            } else { /*No header*/
//                if(activity.selectedDebtor.getFDEBTOR_TAX_REG().equals("Y")) {
//                    lblInvRefno.setText(new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.VanNumValTax)));
//                }else{
//                    lblInvRefno.setText(new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.VanNumValNonTax)));
//                }
//
//                mSaveInvoiceHeader();
//            }
//
//        } else {
//            Toast.makeText(getActivity(), "Select a customer to continue...", Toast.LENGTH_SHORT).show();
//            txtRemakrs.setEnabled(false);
//            txtManual.setEnabled(false);
//        }
//    }

	/*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

   	/*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_HEADER"));
    }

	/*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//    public void mInputDialogbox(String s, final String sType) {
//
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        View promptView = layoutInflater.inflate(R.layout.input_dialogbox, null);
//        final EditText enteredText = (EditText) promptView.findViewById(R.id.txtTextbox);
//        enteredText.setText(s);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        alertDialogBuilder.setView(promptView);
//
//        alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int id) {
//
//                if (sType.equals("R"))
//                    txtRemakrs.setText(enteredText.getText().toString());
//                else
//                    txtManual.setText(enteredText.getText().toString());
//
//               // mSaveInvoiceHeader();
//            }
//        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alertD = alertDialogBuilder.create();
//        alertD.show();
//    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //VanSalesHeader.this.mRefreshHeader();
        }
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/
}
