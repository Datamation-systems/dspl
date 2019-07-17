package com.datamation.sfa.vansale;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.datamation.sfa.R;
import com.datamation.sfa.adapter.InvDetAdapter;
import com.datamation.sfa.adapter.ReturnDetailsAdapter;
import com.datamation.sfa.controller.DebItemPriController;
import com.datamation.sfa.controller.DispDetController;
import com.datamation.sfa.controller.DispHedController;
import com.datamation.sfa.controller.DispIssController;
import com.datamation.sfa.controller.InvDetController;
import com.datamation.sfa.controller.InvHedController;
import com.datamation.sfa.controller.InvTaxDTController;
import com.datamation.sfa.controller.InvTaxRGController;
import com.datamation.sfa.controller.ItemController;
import com.datamation.sfa.controller.ItemLocController;
import com.datamation.sfa.controller.ProductController;
import com.datamation.sfa.controller.STKInController;
import com.datamation.sfa.controller.SalRepController;
import com.datamation.sfa.controller.SalesReturnController;
import com.datamation.sfa.controller.SalesReturnDetController;
import com.datamation.sfa.controller.StkIssController;
import com.datamation.sfa.controller.TaxDetController;
import com.datamation.sfa.dialog.VanSalePrintPreviewAlertBox;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.FInvRDet;
import com.datamation.sfa.model.FInvRHed;
import com.datamation.sfa.model.InvDet;
import com.datamation.sfa.model.InvHed;
import com.datamation.sfa.model.Product;
import com.datamation.sfa.model.StkIn;
import com.datamation.sfa.settings.ReferenceNum;
import com.datamation.sfa.utils.UtilityContainer;
import com.datamation.sfa.view.DebtorDetailsActivity;
import com.datamation.sfa.view.VanSalesActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VanSalesSummary extends Fragment {

    public static final String SETTINGS = "VanSalesSummary";
    public static SharedPreferences localSP;
    View view;
    TextView lblGross, lblReturnQty, lblReturn, lblNetVal, lblReplacements, lblQty;
    SharedPref mSharedPref;
    String RefNo = null,ReturnRefNo = null;
    ArrayList<InvDet> list;
    ArrayList<FInvRDet> returnList;
    Activity activity;
    String locCode;
    FloatingActionButton fabPause, fabDiscard, fabSave;
    FloatingActionMenu fam;
    MyReceiver r;
    int iTotFreeQty = 0;
    private  SweetAlertDialog pDialog;

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        return true;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*Cancel order*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sales_management_van_sales_summary, container, false);

        mSharedPref = new SharedPref(getActivity());
        RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.VanNumVal));
        ReturnRefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.VanReturnNumVal));
        fabPause = (FloatingActionButton) view.findViewById(R.id.fab2);
        fabDiscard = (FloatingActionButton) view.findViewById(R.id.fab3);
        fabSave = (FloatingActionButton) view.findViewById(R.id.fab1);
        fam = (FloatingActionMenu) view.findViewById(R.id.fab_menu);

        lblNetVal = (TextView) view.findViewById(R.id.lblNetVal_Inv);
        lblReturn = (TextView) view.findViewById(R.id.lbl_return_tot);
        lblReturnQty = (TextView) view.findViewById(R.id.lblReturnQty);
        lblReplacements = (TextView) view.findViewById(R.id.lblReplacement);
        lblGross = (TextView) view.findViewById(R.id.lblGross_Inv);
        lblQty = (TextView) view.findViewById(R.id.lblQty_Inv);

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });

        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPauseinvoice();
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveSummaryDialog();

        }
       });

        fabDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoEditingData();
            }
        });

        return view;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*Clear Shared preference-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void undoEditingData() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Do you want to discard the invoice with return ?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                VanSalesActivity activity = (VanSalesActivity) getActivity();
                String result = new InvHedController(getActivity()).restData(RefNo);
                int resultReturn = new SalesReturnController(getActivity()).restData(ReturnRefNo);

                if (!result.equals("")) {
                    new InvDetController(getActivity()).restData(RefNo);
                    new ProductController(getActivity()).mClearTables();
                }
                if(resultReturn != 0){
                    new SalesReturnDetController(getActivity()).restData(ReturnRefNo);
                }

            //    activity.cusPosition = 0;
                activity.selectedDebtor = null;
                activity.selectedRetDebtor = null;
                activity.selectedInvHed = null;
                activity.selectedReturnHed = null;
                Toast.makeText(getActivity(), "Invoice and return details discarded successfully..!", Toast.LENGTH_SHORT).show();
               // UtilityContainer.ClearVanSharedPref(getActivity());
                UtilityContainer.ClearReturnSharedPref(getActivity());

                Intent intnt = new Intent(getActivity(),DebtorDetailsActivity.class);
                startActivity(intnt);
                getActivity().finish();

            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Save primary & secondary invoice-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*/

    public void mRefreshData() {

        int ftotQty = 0, fTotFree = 0, returnQty = 0, replacements = 0;
        double ftotAmt = 0, fTotLineDisc = 0, fTotSchDisc = 0, totalReturn = 0;

        locCode = new SharedPref(getActivity()).getGlobalVal("KeyLocCode");

        list = new InvDetController(getActivity()).getAllInvDet(RefNo);
        returnList = new SalesReturnDetController(getActivity()).getAllInvRDet(ReturnRefNo);

        for (InvDet ordDet : list) {
            ftotAmt += Double.parseDouble(ordDet.getFINVDET_AMT());

            if (ordDet.getFINVDET_TYPE().equals("SA"))
                ftotQty += Integer.parseInt(ordDet.getFINVDET_QTY());
            else
                fTotFree += Integer.parseInt(ordDet.getFINVDET_QTY());

        //    fTotLineDisc += Double.parseDouble(ordDet.getFINVDET_DIS_AMT());
        //    fTotSchDisc += Double.parseDouble(ordDet.getFINVDET_DISVALAMT());
        }
        for (FInvRDet returnDet : returnList){
            if(!returnDet.getFINVRDET_RETURN_TYPE().equals("RP")) {
                totalReturn += Double.parseDouble(returnDet.getFINVRDET_AMT());
                returnQty += Double.parseDouble(returnDet.getFINVRDET_QTY());
            }else{
                replacements += Double.parseDouble(returnDet.getFINVRDET_QTY());
            }
        }

        iTotFreeQty = fTotFree;
        lblQty.setText(String.valueOf(ftotQty + fTotFree));
        lblGross.setText(String.format("%.2f", ftotAmt + fTotSchDisc + fTotLineDisc));
        lblReturn.setText(String.format("%.2f", totalReturn));
        lblNetVal.setText(String.format("%.2f", ftotAmt-totalReturn));
        lblReturnQty.setText(String.valueOf(returnQty));
        lblReplacements.setText(String.valueOf(replacements));


    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/
//
    public void saveSummaryDialog() {

        if (new InvDetController(getActivity()).getItemCount(RefNo) > 0)
        {

            if (new SalesReturnDetController(getActivity()).getItemCount(ReturnRefNo) > 0) {
                //save both invoice and return
                //Changed By Yasith - 2019-01-29
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.sales_management_van_sales_summary_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Do you want to save the invoice with return details ?");
                alertDialogBuilder.setView(promptView);

                final ListView lvProducts_Invoice = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Inv);
                final ListView lvProducts_Return = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Ret);

                ArrayList<InvDet> invoiceItemList = null;
                ArrayList<FInvRDet> returnItemList = null;

                invoiceItemList = new InvDetController(getActivity()).getAllItemsAddedInCurrentSale(RefNo);
                returnItemList = new SalesReturnDetController(getActivity()).getAllItemsAddedInCurrentReturn(ReturnRefNo);
                lvProducts_Invoice.setAdapter(new InvDetAdapter(getActivity(), invoiceItemList));
                lvProducts_Return.setAdapter(new ReturnDetailsAdapter(getActivity(), returnItemList));

                alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int id) {

                        InvHed sHed = new InvHed();
                        ArrayList<InvHed> invHedList = new ArrayList<InvHed>();

                        InvHed invHed = new InvHedController(getActivity()).getActiveInvhed();

                        sHed.setFINVHED_REFNO(RefNo);
                        sHed.setFINVHED_DEBCODE(invHed.getFINVHED_DEBCODE());
                        sHed.setFINVHED_ADDDATE(invHed.getFINVHED_ADDDATE());
                        sHed.setFINVHED_MANUREF(invHed.getFINVHED_MANUREF());
                        sHed.setFINVHED_REMARKS(invHed.getFINVHED_REMARKS());
                        sHed.setFINVHED_ADDMACH(invHed.getFINVHED_ADDMACH());
                        sHed.setFINVHED_ADDUSER(invHed.getFINVHED_ADDUSER());
                        sHed.setFINVHED_CURCODE(invHed.getFINVHED_CURCODE());
                        sHed.setFINVHED_CURRATE(invHed.getFINVHED_CURRATE());
                        sHed.setFINVHED_LOCCODE(invHed.getFINVHED_LOCCODE());

                        sHed.setFINVHED_CUSTELE(invHed.getFINVHED_CUSTELE());
                        sHed.setFINVHED_CONTACT(invHed.getFINVHED_CONTACT());
                        sHed.setFINVHED_CUSADD1(invHed.getFINVHED_CUSADD1());
                        sHed.setFINVHED_CUSADD2(invHed.getFINVHED_CUSADD2());
                        sHed.setFINVHED_CUSADD3(invHed.getFINVHED_CUSADD3());
                        sHed.setFINVHED_TXNTYPE(invHed.getFINVHED_TXNTYPE());
                        sHed.setFINVHED_IS_ACTIVE(invHed.getFINVHED_IS_ACTIVE());
                        sHed.setFINVHED_IS_SYNCED(invHed.getFINVHED_IS_SYNCED());
                        sHed.setFINVHED_LOCCODE(invHed.getFINVHED_LOCCODE());
                        sHed.setFINVHED_AREACODE(invHed.getFINVHED_AREACODE());
                        sHed.setFINVHED_ROUTECODE(invHed.getFINVHED_ROUTECODE());
                        sHed.setFINVHED_COSTCODE(invHed.getFINVHED_COSTCODE());
                        sHed.setFINVHED_TAXREG(invHed.getFINVHED_TAXREG());
                        sHed.setFINVHED_TOURCODE(invHed.getFINVHED_TOURCODE());
                        sHed.setFINVHED_TOURCODE(invHed.getFINVHED_START_TIME_SO());

                        sHed.setFINVHED_BPTOTALDIS("0");
                        sHed.setFINVHED_BTOTALAMT("0");
                        sHed.setFINVHED_BTOTALDIS("0");
                        sHed.setFINVHED_BTOTALTAX("0");
                        sHed.setFINVHED_END_TIME_SO(currentTime());
                        sHed.setFINVHED_START_TIME_SO(invHed.getFINVHED_START_TIME_SO());
                        sHed.setFINVHED_LATITUDE(mSharedPref.getGlobalVal("Latitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Latitude"));
                        sHed.setFINVHED_LONGITUDE(mSharedPref.getGlobalVal("Longitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Longitude"));
                       // sHed.setFINVHED_ADDRESS(localSP.getString("GPS_Address", "").toString());
                        sHed.setFINVHED_TOTALTAX("0");
                        sHed.setFINVHED_TOTALDIS("0.0");
                        sHed.setFINVHED_TOTALAMT(lblNetVal.getText().toString());
                        sHed.setFINVHED_TXNDATE(invHed.getFINVHED_TXNDATE());
                        sHed.setFINVHED_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
                        sHed.setFINVHED_REFNO1("");
                        sHed.setFINVHED_TOTQTY(lblQty.getText().toString());
                        sHed.setFINVHED_TOTFREEQTY(iTotFreeQty + "");
                        sHed.setFINVHED_SETTING_CODE(invHed.getFINVHED_SETTING_CODE());

                        invHedList.add(sHed);

                        if (new InvHedController(getActivity()).createOrUpdateInvHed(invHedList) > 0) {
                            new ProductController(getActivity()).mClearTables();
                            new InvHedController(getActivity()).InactiveStatusUpdate(RefNo);
                            new InvDetController(getActivity()).InactiveStatusUpdate(RefNo);

                            final VanSalesActivity activity = (VanSalesActivity) getActivity();

                                new ReferenceNum(getActivity()).nNumValueInsertOrUpdate(getResources().getString(R.string.VanNumVal));

                            FInvRHed mainHead = new FInvRHed();
                            ArrayList<FInvRHed> returnHedList = new ArrayList<FInvRHed>();
                            ArrayList<FInvRHed> HedList = new SalesReturnController(getActivity()).getAllActiveInvrhed();
                            if (!HedList.isEmpty()) {

                                mainHead.setFINVRHED_REFNO(ReturnRefNo);
                                mainHead.setFINVRHED_DEBCODE(invHed.getFINVHED_DEBCODE());
                                mainHead.setFINVRHED_ADD_DATE(invHed.getFINVHED_ADDDATE());
                                mainHead.setFINVRHED_MANUREF(invHed.getFINVHED_MANUREF());
                                mainHead.setFINVRHED_REMARKS(invHed.getFINVHED_REMARKS());
                                mainHead.setFINVRHED_ADD_MACH(invHed.getFINVHED_ADDMACH());
                                mainHead.setFINVRHED_ADD_USER(invHed.getFINVHED_ADDUSER());
                                mainHead.setFINVRHED_TXN_DATE(HedList.get(0).getFINVRHED_TXN_DATE());
                                mainHead.setFINVRHED_ROUTE_CODE(invHed.getFINVHED_ROUTECODE());
                                mainHead.setFINVRHED_TOTAL_AMT(HedList.get(0).getFINVRHED_TOTAL_AMT());
                                mainHead.setFINVRHED_TXNTYPE(HedList.get(0).getFINVRHED_TXNTYPE());
                                mainHead.setFINVRHED_ADDRESS(HedList.get(0).getFINVRHED_ADDRESS());
                                mainHead.setFINVRHED_REASON_CODE(HedList.get(0).getFINVRHED_REASON_CODE());
                                mainHead.setFINVRHED_COSTCODE(HedList.get(0).getFINVRHED_COSTCODE());
                                mainHead.setFINVRHED_LOCCODE(HedList.get(0).getFINVRHED_LOCCODE());
                                mainHead.setFINVRHED_TAX_REG(HedList.get(0).getFINVRHED_TAX_REG());
                                mainHead.setFINVRHED_TOTAL_TAX(HedList.get(0).getFINVRHED_TOTAL_TAX());
                                mainHead.setFINVRHED_TOTAL_DIS(HedList.get(0).getFINVRHED_TOTAL_DIS());
                                mainHead.setFINVRHED_LONGITUDE(HedList.get(0).getFINVRHED_LONGITUDE());
                                mainHead.setFINVRHED_LATITUDE(HedList.get(0).getFINVRHED_LATITUDE());
                                mainHead.setFINVRHED_START_TIME(HedList.get(0).getFINVRHED_START_TIME());
                                mainHead.setFINVRHED_END_TIME(HedList.get(0).getFINVRHED_END_TIME());
                                mainHead.setFINVRHED_INV_REFNO(RefNo);//HedList.get(0).getFINVRHED_INV_REFNO()
                                mainHead.setFINVRHED_IS_ACTIVE("0");
                                mainHead.setFINVRHED_IS_SYNCED("0");
                            }

                            returnHedList.add(mainHead);
                            //Log.v("Ref No :",mainHead.getFINVRHED_INV_REFNO().toString());

                            if (new SalesReturnController(getActivity()).createOrUpdateInvRHed(returnHedList) > 0) {
                                UpdateReturnTotal(ReturnRefNo);
                                new SalesReturnDetController(getActivity()).InactiveStatusUpdate(ReturnRefNo);
                                new SalesReturnController(getActivity()).InactiveStatusUpdate(ReturnRefNo);

                                activity.selectedReturnHed = null;
                                new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.VanReturnNumVal));
                                Toast.makeText(getActivity(), "Return saved successfully !", Toast.LENGTH_LONG).show();
                                UtilityContainer.ClearReturnSharedPref(getActivity());
                               // UtilityContainer.mLoadFragment(new SalesReturnHistory(), activity);

//						new SalesPrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Sales return",
//								RefNo);
                            } else {
                                Toast.makeText(getActivity(), "Return failed !", Toast.LENGTH_LONG)
                                        .show();
                            }


                            /*-*-*-*-*-*-*-*-*-*-QOH update-*-*-*-*-*-*-*-*-*/

                            UpdateTaxDetails(RefNo);
                            UpdateQOH_FIFO();
                            new ItemLocController(getActivity()).UpdateInvoiceQOH(RefNo, "-", locCode);
                            new ItemLocController(getActivity()).UpdateInvoiceQOHInReturn(RefNo, "+", locCode);
                            updateDispTables(sHed);
                            int a = new VanSalePrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Print preview", RefNo,"",false);

                            Toast.makeText(getActivity(), "Invoice saved successfully..!", Toast.LENGTH_SHORT).show();
                        //    UtilityContainer.ClearVanSharedPref(getActivity());
                        //    activity.cusPosition = 0;
                            activity.selectedDebtor = null;
                            activity.selectedRetDebtor = null;
                            activity.selectedReturnHed = null;
                            activity.selectedInvHed = null;
                         //   loadFragment(new VanSaleInvoice());
                            Intent intent = new Intent(getActivity(),DebtorDetailsActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                            Toast.makeText(getActivity(), "Failed..", Toast.LENGTH_SHORT).show();
                        }

                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();
            }
            else
            {
                // save only invoice
                //Changed By Yasith - 2019-01-29
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.sales_management_van_sales_summary_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Do you want to save the invoice ?");
                alertDialogBuilder.setView(promptView);

                final ListView lvProducts_Invoice = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Inv);
                ArrayList<InvDet> invoiceItemList = null;
                invoiceItemList = new InvDetController(getActivity()).getAllItemsAddedInCurrentSale(RefNo);
                lvProducts_Invoice.setAdapter(new InvDetAdapter(getActivity(), invoiceItemList));

            alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(final DialogInterface dialog, int id) {


                    ArrayList<InvHed> invHedList = new ArrayList<InvHed>();

                    InvHed invHed = new InvHedController(getActivity()).getActiveInvhed();
                    InvHed sHed = new InvHed();
/**
 hed.setFINVHED_ADDDATE(currnentDate.getText().toString());
 hed.setFINVHED_MANUREF(txtManual.getText().toString());
 hed.setFINVHED_REMARKS(txtRemakrs.getText().toString());
 hed.setFINVHED_ADDMACH(localSP.getString("MAC_Address", "No MAC Address").toString());
 hed.setFINVHED_ADDUSER(new SalRepController(getActivity()).getCurrentRepCode());
 hed.setFINVHED_CURCODE("LKR");
 hed.setFINVHED_CURRATE("1.00");
 hed.setFINVHED_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());

 if (activity.selectedDebtor != null) {
 hed.setFINVHED_DEBCODE(activity.selectedDebtor.getCusCode());
 hed.setFINVHED_CONTACT(activity.selectedDebtor.getCusMob());
 hed.setFINVHED_CUSADD1(activity.selectedDebtor.getCusAdd1());
 hed.setFINVHED_CUSADD2(activity.selectedDebtor.getCusAdd2());
 hed.setFINVHED_CUSADD3(activity.selectedDebtor.getCusAdd1());
 //  hed.setFINVHED_CUSTELE(activity.selectedDebtor.getCus);
 //    hed.setFINVHED_TAXREG(activity.selectedDebtor.getFDEBTOR_TAX_REG());
 }else{
 activity.selectedDebtor  = new CustomerController(getActivity()).getSelectedCustomerByCode(new SharedPref(getActivity()).getSelectedDebCode());
 hed.setFINVHED_DEBCODE(new SharedPref(getActivity()).getSelectedDebCode());
 hed.setFINVHED_CONTACT(activity.selectedDebtor.getCusMob());
 hed.setFINVHED_CUSADD1(activity.selectedDebtor.getCusAdd1());
 hed.setFINVHED_CUSADD2(activity.selectedDebtor.getCusAdd2());
 hed.setFINVHED_CUSADD3(activity.selectedDebtor.getCusAdd1());
 }

 hed.setFINVHED_TXNTYPE("22");
 hed.setFINVHED_TXNDATE(currnentDate.getText().toString());
 hed.setFINVHED_IS_ACTIVE("1");
 hed.setFINVHED_IS_SYNCED("0");
 hed.setFINVHED_TOURCODE(new SharedPref(getActivity()).getGlobalVal("KeyTouRef"));
 // hed.setFINVHED_AREACODE(new SharedPref(getActivity()).getGlobalVal("KeyAreaCode"));
 hed.setFINVHED_AREACODE(SharedPref.getInstance(getActivity()).getSelectedDebName());
 // hed.setFINVHED_LOCCODE(new SharedPref(getActivity()).getGlobalVal("KeyLocCode"));
 hed.setFINVHED_LOCCODE(new SalRepController(getActivity()).getCurrentLocCode());
 hed.setFINVHED_ROUTECODE(new SharedPref(getActivity()).getSelectedDebRouteCode());
 hed.setFINVHED_PAYTYPE(new SharedPref(getActivity()).getGlobalVal("KeyPayType"));
 hed.setFINVHED_COSTCODE("");
 hed.setFINVHED_START_TIME_SO(currentTime());
 hed.setFINVHED_SETTING_CODE(getResources().getString(R.string.VanNumVal));**/
                    sHed.setFINVHED_REFNO(RefNo);
                    //sHed.setFINVHED_DEBCODE(invHed.getFINVHED_DEBCODE());
                    sHed.setFINVHED_ADDDATE(invHed.getFINVHED_ADDDATE());
                    sHed.setFINVHED_MANUREF(invHed.getFINVHED_MANUREF());
                    sHed.setFINVHED_REMARKS(invHed.getFINVHED_REMARKS());
                    sHed.setFINVHED_ADDMACH(invHed.getFINVHED_ADDMACH());
                    sHed.setFINVHED_ADDUSER(invHed.getFINVHED_ADDUSER());
                    sHed.setFINVHED_CURCODE(invHed.getFINVHED_CURCODE());
                    sHed.setFINVHED_CURRATE(invHed.getFINVHED_CURRATE());
                    sHed.setFINVHED_LOCCODE(invHed.getFINVHED_LOCCODE());

                    sHed.setFINVHED_CUSTELE(invHed.getFINVHED_CUSTELE());
                    sHed.setFINVHED_CONTACT(invHed.getFINVHED_CONTACT());
                    sHed.setFINVHED_CUSADD1(invHed.getFINVHED_CUSADD1());
                    sHed.setFINVHED_CUSADD2(invHed.getFINVHED_CUSADD2());
                    sHed.setFINVHED_CUSADD3(invHed.getFINVHED_CUSADD3());
                    sHed.setFINVHED_TXNTYPE(invHed.getFINVHED_TXNTYPE());
                    sHed.setFINVHED_IS_ACTIVE(invHed.getFINVHED_IS_ACTIVE());
                    sHed.setFINVHED_IS_SYNCED(invHed.getFINVHED_IS_SYNCED());
                    sHed.setFINVHED_LOCCODE(invHed.getFINVHED_LOCCODE());
                    sHed.setFINVHED_AREACODE(invHed.getFINVHED_AREACODE());
                    sHed.setFINVHED_ROUTECODE(invHed.getFINVHED_ROUTECODE());
                    sHed.setFINVHED_COSTCODE(invHed.getFINVHED_COSTCODE());
                    sHed.setFINVHED_TAXREG(invHed.getFINVHED_TAXREG());
                    sHed.setFINVHED_TOURCODE(invHed.getFINVHED_TOURCODE());
                    sHed.setFINVHED_START_TIME_SO(invHed.getFINVHED_START_TIME_SO());

                    sHed.setFINVHED_BPTOTALDIS("0");
                    sHed.setFINVHED_BTOTALAMT("0");
                    sHed.setFINVHED_BTOTALDIS("0");
                    sHed.setFINVHED_BTOTALTAX("0");
                    sHed.setFINVHED_END_TIME_SO(currentTime());
                  //  sHed.setFINVHED_START_TIME_SO(localSP.getString("Van_Start_Time", "").toString());
                    sHed.setFINVHED_LATITUDE(mSharedPref.getGlobalVal("Latitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Latitude"));
                    sHed.setFINVHED_LONGITUDE(mSharedPref.getGlobalVal("Longitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Longitude"));
                   // sHed.setFINVHED_ADDRESS(localSP.getString("GPS_Address", "").toString());
                    sHed.setFINVHED_TOTALTAX("0");
                    sHed.setFINVHED_TOTALDIS("0.0");
                    sHed.setFINVHED_TOTALAMT(lblNetVal.getText().toString());
                    sHed.setFINVHED_TXNDATE(invHed.getFINVHED_TXNDATE());
                    sHed.setFINVHED_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
                    sHed.setFINVHED_REFNO1("");
                    sHed.setFINVHED_TOTQTY(lblQty.getText().toString());
                    sHed.setFINVHED_TOTFREEQTY(iTotFreeQty + "");
                    sHed.setFINVHED_SETTING_CODE(invHed.getFINVHED_SETTING_CODE());

                    invHedList.add(sHed);

                    if (new InvHedController(getActivity()).createOrUpdateInvHed(invHedList) > 0) {
                        new ProductController(getActivity()).mClearTables();
                        new InvHedController(getActivity()).InactiveStatusUpdate(RefNo);
                        new InvDetController(getActivity()).InactiveStatusUpdate(RefNo);

                        final VanSalesActivity activity = (VanSalesActivity) getActivity();
                        new ReferenceNum(getActivity()).nNumValueInsertOrUpdate(getResources().getString(R.string.VanNumVal));

                        /*-*-*-*-*-*-*-*-*-*-QOH update-*-*-*-*-*-*-*-*-*/

                        UpdateTaxDetails(RefNo);
                        UpdateQOH_FIFO();
                        new ItemLocController(getActivity()).UpdateInvoiceQOH(RefNo, "-", locCode);
                        updateDispTables(sHed);
                        int a = new VanSalePrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Print preview", RefNo, "",false);

                        //if(a == 1)
                        //{
                            Toast.makeText(getActivity(), "Invoice saved successfully..!", Toast.LENGTH_SHORT).show();
                          //  UtilityContainer.ClearVanSharedPref(getActivity());
                         //   activity.cusPosition = 0;
                            activity.selectedDebtor = null;
                            activity.selectedRetDebtor = null;
                           // activity.selectedRecHed = null;
                            activity.selectedInvHed = null;
                            Intent intent = new Intent(getActivity(),DebtorDetailsActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        //}

                    } else {
                        Toast.makeText(getActivity(), "Failed..", Toast.LENGTH_SHORT).show();
                    }

                }

            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alertD = alertDialogBuilder.create();
            alertD.show();
        }

        } else
            Toast.makeText(activity, "Add items before save ...!", Toast.LENGTH_SHORT).show();


    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateTaxDetails(String refNo) {
        ArrayList<InvDet> list = new InvDetController(activity).getAllInvDet(refNo);
        new InvDetController(activity).UpdateItemTaxInfo(list);
        new InvTaxRGController(activity).UpdateInvTaxRG(list);
        new InvTaxDTController(activity).UpdateInvTaxDT(list);
    }
    public void UpdateReturnTotal(String refNo) {
        ArrayList<FInvRDet> list = new SalesReturnDetController(activity).getAllInvRDet(refNo);
        new SalesReturnDetController(activity).UpdateReturnTot(list);

    }
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    private void UpdateQOH_FIFO() {

        ArrayList<InvDet> list = new InvDetController(getActivity()).getAllInvDet(RefNo);

		/*-*-*-*-*-*-*-*-*-*-*-*-each itemcode has multiple sizecodes*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*/
        for (InvDet item : list) {

            int Qty = (int) Double.parseDouble(item.getFINVDET_QTY());

            ArrayList<StkIn> GRNList = new STKInController(activity).getAscendingGRNList(item.getFINVDET_ITEM_CODE(), locCode);

			/*-*-*-*-*-*-*-*-*-*-multiple GRN for each sizecode-*-*-*-*-*-*-*-*-*-*-*/
            for (StkIn size : GRNList) {
                int balQty = (int) Double.parseDouble(size.getBALQTY());

                if (balQty > 0) {
                    if (Qty > balQty) {
                        Qty = Qty - balQty;
                        size.setBALQTY("0");
                        new StkIssController(activity).InsertSTKIssData(size, RefNo, String.valueOf(balQty), locCode);

                    } else {
                        size.setBALQTY(String.valueOf(balQty - Qty));
                        new StkIssController(activity).InsertSTKIssData(size, RefNo, String.valueOf(Qty), locCode);
                        break;
                    }
                }
            }
            new STKInController(activity).UpdateBalQtyByFIFO(GRNList);
        }
    }
//
//	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/
//
    public void updateDispTables(InvHed invHed) {

        String dispREfno = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.DispVal));

        int res = new DispHedController(getActivity()).updateHeader(invHed, dispREfno);

        if (res > 0) {
            ArrayList<InvDet> list = new InvDetController(getActivity()).getAllInvDet(invHed.getFINVHED_REFNO());
            new DispDetController(getActivity()).updateDispDet(list, dispREfno);
            new DispIssController(getActivity()).updateDispIss(new StkIssController(getActivity()).getUploadData(invHed.getFINVHED_REFNO()), dispREfno);
            new ReferenceNum(getActivity()).nNumValueInsertOrUpdate(getResources().getString(R.string.DispVal));
        }
    }

//	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/
//
    public void mPauseinvoice() {

        if (new InvDetController(getActivity()).getItemCount(RefNo) > 0) {
            Intent intnt = new Intent(getActivity(),DebtorDetailsActivity.class);
            startActivity(intnt);
            getActivity().finish();
        } else
            Toast.makeText(activity, "Add items before pause ...!", Toast.LENGTH_SHORT).show();
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_SUMMARY"));
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

//    public void loadFragment(Fragment fragment) {
//
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
//        ft.replace(R.id.main_container, fragment);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.commit();
//
//    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void updateInvoice() {

        ArrayList<Product> list = new ProductController(getActivity()).getSelectedItems();

        int i = 0;

        for (Product product : list) {

            VanSalesActivity activity = (VanSalesActivity) getActivity();
            double totAmt = Double.parseDouble(product.getFPRODUCT_PRICE()) * Double.parseDouble(product.getFPRODUCT_QTY());
            String TaxedAmt = new TaxDetController(getActivity()).calculateTax(product.getFPRODUCT_ITEMCODE(), new BigDecimal(totAmt));

            double brandDiscPer = new DebItemPriController(getActivity()).getBrandDiscount(new ItemController(getActivity()).getBrandCode(product.getFPRODUCT_ITEMCODE()), SharedPref.getInstance(getActivity()).getSelectedDebCode());
         //   double compDiscPer = new ControlDS(getActivity()).getCompanyDisc();

//            double Disc = (totAmt / 100) * compDiscPer;
//            double compDisc = Disc;
//            totAmt -= Disc;

//            Disc = (totAmt / 100) * brandDiscPer;
//            double brandDisc = Disc;
//            totAmt -= Disc;

            InvDet invDet = new InvDet();

            invDet.setFINVDET_ID(String.valueOf(i++));
            invDet.setFINVDET_AMT(String.format("%.2f", totAmt - Double.parseDouble(TaxedAmt)));
            invDet.setFINVDET_BAL_QTY(product.getFPRODUCT_QTY());
            invDet.setFINVDET_B_AMT(invDet.getFINVDET_AMT());
            invDet.setFINVDET_B_SELL_PRICE(product.getFPRODUCT_PRICE());
            invDet.setFINVDET_BT_SELL_PRICE(product.getFPRODUCT_PRICE());
        //    invDet.setFINVDET_DIS_AMT(String.format("%.2f", compDisc + brandDisc));
            invDet.setFINVDET_DIS_PER("0");
            invDet.setFINVDET_ITEM_CODE(product.getFPRODUCT_ITEMCODE());
            invDet.setFINVDET_PRIL_CODE(SharedPref.getInstance(getActivity()).getSelectedDebtorPrilCode());
            invDet.setFINVDET_QTY(product.getFPRODUCT_QTY());
            invDet.setFINVDET_PICE_QTY(product.getFPRODUCT_QTY());
            invDet.setFINVDET_TYPE("SA");
            invDet.setFINVDET_BT_TAX_AMT("");
            invDet.setFINVDET_RECORD_ID("");


        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            VanSalesSummary.this.mRefreshData();
        }
    }


    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/
    public void updateQtyInItemLocTblItemWise()
    {
        try
        {
            ArrayList<InvDet> list = new InvDetController(getActivity()).getAllInvDet(RefNo);

		/*-*-*-*-*-*-*-*-*-*-*-*-each itemcode has multiple sizecodes*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*/
            for (InvDet item : list)
            {
                int Qty = (int) Double.parseDouble(item.getFINVDET_QTY());
                ArrayList<StkIn> GRNList = new STKInController(activity).getAscendingGRNList(item.getFINVDET_ITEM_CODE(), locCode);

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    //------------------------------------------------------------------------------------------------------------------------------------
    public class LoardingPrintView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            new VanSalePrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Print preview", RefNo, "",false);
            return null;

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            final VanSalesActivity activity = (VanSalesActivity) getActivity();
            Toast.makeText(getActivity(), "Invoice saved successfully..!", Toast.LENGTH_SHORT).show();
           // UtilityContainer.ClearVanSharedPref(getActivity());

          //  activity.selectedDebtor = null;
          //  activity.selectedRetDebtor = null;
            //activity.selectedRecHed = null;
            activity.selectedInvHed = null;
            Intent intnt = new Intent(getActivity(),DebtorDetailsActivity.class);
            startActivity(intnt);
            getActivity().finish();
          //  loadFragment(new VanSaleInvoice());

        }
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


}
