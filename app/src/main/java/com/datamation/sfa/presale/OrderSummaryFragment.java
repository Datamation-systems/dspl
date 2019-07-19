package com.datamation.sfa.presale;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.adapter.InvDetAdapter;
import com.datamation.sfa.adapter.OrderDetailsAdapter;
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
import com.datamation.sfa.controller.OrderController;
import com.datamation.sfa.controller.OrderDetailController;
import com.datamation.sfa.controller.PreProductController;
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
import com.datamation.sfa.model.Order;
import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.model.PRESALE;
import com.datamation.sfa.model.Product;
import com.datamation.sfa.model.StkIn;
import com.datamation.sfa.settings.ReferenceNum;
import com.datamation.sfa.utils.UtilityContainer;
import com.datamation.sfa.vansale.VanSalesSummary;
import com.datamation.sfa.view.DebtorDetailsActivity;
import com.datamation.sfa.view.PreSalesActivity;
import com.datamation.sfa.view.VanSalesActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class OrderSummaryFragment extends Fragment {

    public static final String SETTINGS = "PreSalesSummary";
    public static SharedPreferences localSP;
    View view;
    TextView lblGross, lblReturnQty, lblReturn, lblNetVal, lblReplacements, lblQty;
    SharedPref mSharedPref;
    String RefNo = null, ReturnRefNo = null;
    ArrayList<OrderDetail> list;
    ArrayList<FInvRDet> returnList;
    String locCode;
    FloatingActionButton fabPause, fabDiscard, fabSave;
    FloatingActionMenu fam;
    MyReceiver r;
    int iTotFreeQty = 0;
    private  SweetAlertDialog pDialog;
    PreSalesActivity mainActivity;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_summary, container, false);

        mSharedPref = new SharedPref(getActivity());
        mainActivity = (PreSalesActivity)getActivity();
        RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
        ReturnRefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.PreReturnNumVal));
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

//        if (new OrderDetailController(getActivity()).isAnyActiveOrders())
//        {
//            RefNo = new OrderDetailController(getActivity()).getActiveRefNo().getFORDERDET_REFNO();
//        }
//        else
//        {
//            RefNo = mainActivity.selectedPreHed.getORDER_REFNO();
//        }

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

    public void undoEditingData() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Do you want to discard the order with return ?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                int result = new OrderController(getActivity()).restData(RefNo);
                int resultReturn = new SalesReturnController(getActivity()).restData(ReturnRefNo);

                if (result>0) {
                    new OrderDetailController(getActivity()).restData(RefNo);
                    new PreProductController(getActivity()).mClearTables();
                }
                if(resultReturn != 0){
                    new SalesReturnDetController(getActivity()).restData(ReturnRefNo);
                }

                //    activity.cusPosition = 0;
                mainActivity.selectedDebtor = null;
                mainActivity.selectedRetDebtor = null;
                mainActivity.selectedPreHed = null;
                mainActivity.selectedReturnHed = null;
                Toast.makeText(getActivity(), "Order and return details discarded successfully..!", Toast.LENGTH_SHORT).show();
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

        RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
        ReturnRefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.PreReturnNumVal));

        int ftotQty = 0, fTotFree = 0, returnQty = 0, replacements = 0;
        double ftotAmt = 0, fTotLineDisc = 0, fTotSchDisc = 0, totalReturn = 0;

        locCode = new SharedPref(getActivity()).getGlobalVal("KeyLocCode");

        list = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
        returnList = new SalesReturnDetController(getActivity()).getAllInvRDet(ReturnRefNo);

        for (OrderDetail ordDet : list) {
            ftotAmt += Double.parseDouble(ordDet.getFORDERDET_AMT());

//            if (ordDet.getFORDERDET_TYPE().equals("SA"))
                ftotQty += Integer.parseInt(ordDet.getFORDERDET_QTY());
            //else
                //fTotFree += Integer.parseInt(ordDet.getFORDERDET_QTY());

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
//        lblGross.setText(String.format("%.2f", ftotAmt + fTotSchDisc + fTotLineDisc));
//        lblReturn.setText(String.format("%.2f", totalReturn));
//        lblNetVal.setText(String.format("%.2f", ftotAmt-totalReturn));

        lblGross.setText(String.format("%.2f", ftotAmt));
        lblReturn.setText(String.format("%.2f", totalReturn));
        lblNetVal.setText(String.format("%.2f", ftotAmt-totalReturn));

        lblReturnQty.setText(String.valueOf(returnQty));
        lblReplacements.setText(String.valueOf(replacements));


    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/
//
    public void saveSummaryDialog() {

        if (new OrderDetailController(getActivity()).getItemCount(RefNo) > 0)
        {

            if (new SalesReturnDetController(getActivity()).getItemCount(ReturnRefNo) > 0) {

                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.sales_management_van_sales_summary_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Do you want to save the invoice with return details ?");
                alertDialogBuilder.setView(promptView);

                final ListView lvProducts_Invoice = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Inv);
                final ListView lvProducts_Return = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Ret);

                ArrayList<OrderDetail> orderItemList = null;
                ArrayList<FInvRDet> returnItemList = null;

                orderItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo);
                returnItemList = new SalesReturnDetController(getActivity()).getAllItemsAddedInCurrentReturn(ReturnRefNo);
                lvProducts_Invoice.setAdapter(new OrderDetailsAdapter(getActivity(), orderItemList));
                lvProducts_Return.setAdapter(new ReturnDetailsAdapter(getActivity(), returnItemList));

                alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int id) {

                        PRESALE ordHed = new PRESALE();
                        ArrayList<PRESALE> ordHedList = new ArrayList<PRESALE>();

                        PRESALE presale = new OrderController(getActivity()).getAllActiveOrdHed();

                        ordHed.setORDER_REFNO(RefNo);
                        ordHed.setORDER_DEBCODE(presale.getORDER_DEBCODE());
                        ordHed.setORDER_ADDDATE(presale.getORDER_ADDDATE());
                        ordHed.setORDER_MANUREF(presale.getORDER_MANUREF());
                        ordHed.setORDER_REMARKS(presale.getORDER_REMARKS());
                        ordHed.setORDER_ADDMACH(presale.getORDER_ADDMACH());
                        ordHed.setORDER_ADDUSER(presale.getORDER_ADDUSER());
                        ordHed.setORDER_CURCODE(presale.getORDER_CURCODE());
                        ordHed.setORDER_CURRATE(presale.getORDER_CURRATE());
                        ordHed.setORDER_LOCCODE(presale.getORDER_LOCCODE());
                        ordHed.setORDER_CUSTELE(presale.getORDER_CUSTELE());
                        ordHed.setORDER_CONTACT(presale.getORDER_CONTACT());
                        ordHed.setORDER_CUSADD1(presale.getORDER_CUSADD1());
                        ordHed.setORDER_CUSADD2(presale.getORDER_CUSADD2());
                        ordHed.setORDER_CUSADD3(presale.getORDER_CUSADD3());
                        ordHed.setORDER_TXNTYPE(presale.getORDER_TXNTYPE());
                        ordHed.setORDER_IS_ACTIVE(presale.getORDER_IS_ACTIVE());
                        ordHed.setORDER_IS_SYNCED(presale.getORDER_IS_SYNCED());
                        ordHed.setORDER_LOCCODE(presale.getORDER_LOCCODE());
                        ordHed.setORDER_AREACODE(presale.getORDER_AREACODE());
                        ordHed.setORDER_ROUTECODE(presale.getORDER_ROUTECODE());
                        ordHed.setORDER_COSTCODE(presale.getORDER_COSTCODE());
                        ordHed.setORDER_TAXREG(presale.getORDER_TAXREG());
                        ordHed.setORDER_TOURCODE(presale.getORDER_TOURCODE());
                        ordHed.setORDER_BPTOTALDIS("0");
                        ordHed.setORDER_BTOTALAMT("0");
                        ordHed.setORDER_BTOTALDIS("0");
                        ordHed.setORDER_BTOTALTAX("0");
                        ordHed.setORDER_LATITUDE(mSharedPref.getGlobalVal("Latitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Latitude"));
                        ordHed.setORDER_LONGITUDE(mSharedPref.getGlobalVal("Longitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Longitude"));
                        //ordHed.setORDER_ADDRESS(localSP.getString("GPS_Address", "").toString());
                        ordHed.setORDER_TOTALTAX("0");
                        ordHed.setORDER_TOTALDIS("0.0");
                        ordHed.setORDER_TOTALAMT(lblNetVal.getText().toString());
                        ordHed.setORDER_TXNDATE(presale.getORDER_TXNDATE());
                        ordHed.setORDER_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
                        ordHed.setORDER_REFNO1("");
                        ordHed.setORDER_TOTQTY(lblQty.getText().toString());
                        ordHed.setORDER_TOTFREEQTY(iTotFreeQty + "");
                        ordHed.setORDER_SETTING_CODE(presale.getORDER_SETTING_CODE());

                        ordHedList.add(ordHed);

                        if (new OrderController(getActivity()).createOrUpdateOrdHed(ordHedList) > 0) {
                            new PreProductController(getActivity()).mClearTables();
                            new OrderController(getActivity()).InactiveStatusUpdate(RefNo);
                            new OrderDetailController(getActivity()).InactiveStatusUpdate(RefNo);

                            final PreSalesActivity activity = (PreSalesActivity) getActivity();

                            new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.NumVal));

                            FInvRHed mainHead = new FInvRHed();
                            ArrayList<FInvRHed> returnHedList = new ArrayList<FInvRHed>();
                            ArrayList<FInvRHed> HedList = new SalesReturnController(getActivity()).getAllActiveInvrhed();

                            if (!HedList.isEmpty()) {

                                mainHead.setFINVRHED_REFNO(ReturnRefNo);
                                mainHead.setFINVRHED_DEBCODE(ordHed.getORDER_DEBCODE());
                                mainHead.setFINVRHED_ADD_DATE(ordHed.getORDER_ADDDATE());
                                mainHead.setFINVRHED_MANUREF(ordHed.getORDER_MANUREF());
                                mainHead.setFINVRHED_REMARKS(ordHed.getORDER_REMARKS());
                                mainHead.setFINVRHED_ADD_MACH(ordHed.getORDER_ADDMACH());
                                mainHead.setFINVRHED_ADD_USER(ordHed.getORDER_ADDUSER());
                                mainHead.setFINVRHED_TXN_DATE(HedList.get(0).getFINVRHED_TXN_DATE());
                                mainHead.setFINVRHED_ROUTE_CODE(ordHed.getORDER_ROUTECODE());
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
                                new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.PreReturnNumVal));
                                Toast.makeText(getActivity(), "Order Return saved successfully !", Toast.LENGTH_LONG).show();
                                UtilityContainer.ClearReturnSharedPref(getActivity());

                            } else {
                                Toast.makeText(getActivity(), "Order Return failed !", Toast.LENGTH_LONG).show();
                            }

                            UpdateTaxDetails(RefNo);
                            //UpdateQOH_FIFO();
                            new ItemLocController(getActivity()).UpdateOrderQOH(RefNo, "-", locCode);
                            new ItemLocController(getActivity()).UpdateOrderQOHInReturn(RefNo, "+", locCode);
//                            updateDispTables(sHed);
                            new VanSalePrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(),"PRINT PREVIEW", RefNo, ReturnRefNo,true);

                            Toast.makeText(getActivity(), "Order saved successfully..!", Toast.LENGTH_SHORT).show();
                            activity.selectedRetDebtor = null;
                            activity.selectedReturnHed = null;
                            activity.selectedPreHed = null;

//                            Intent intent = new Intent(getActivity(),DebtorDetailsActivity.class);
//                            startActivity(intent);
//                            getActivity().finish();

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
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.sales_management_van_sales_summary_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Do you want to save the invoice ?");
                alertDialogBuilder.setView(promptView);

                final ListView lvProducts_Invoice = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Inv);
                ArrayList<OrderDetail> orderItemList = null;
                orderItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo);
                lvProducts_Invoice.setAdapter(new OrderDetailsAdapter(getActivity(), orderItemList));

                alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int id) {

                        PRESALE ordHed = new PRESALE();
                        ArrayList<PRESALE> ordHedList = new ArrayList<PRESALE>();
                        PRESALE presale = new OrderController(getActivity()).getAllActiveOrdHed();

                        ordHed.setORDER_REFNO(RefNo);
                        ordHed.setORDER_DEBCODE(presale.getORDER_DEBCODE());
                        ordHed.setORDER_ADDDATE(presale.getORDER_ADDDATE());
                        ordHed.setORDER_MANUREF(presale.getORDER_MANUREF());
                        ordHed.setORDER_REMARKS(presale.getORDER_REMARKS());
                        ordHed.setORDER_ADDMACH(presale.getORDER_ADDMACH());
                        ordHed.setORDER_ADDUSER(presale.getORDER_ADDUSER());
                        ordHed.setORDER_CURCODE(presale.getORDER_CURCODE());
                        ordHed.setORDER_CURRATE(presale.getORDER_CURRATE());
                        ordHed.setORDER_LOCCODE(presale.getORDER_LOCCODE());
                        ordHed.setORDER_CUSTELE(presale.getORDER_CUSTELE());
                        ordHed.setORDER_CONTACT(presale.getORDER_CONTACT());
                        ordHed.setORDER_CUSADD1(presale.getORDER_CUSADD1());
                        ordHed.setORDER_CUSADD2(presale.getORDER_CUSADD2());
                        ordHed.setORDER_CUSADD3(presale.getORDER_CUSADD3());
                        ordHed.setORDER_TXNTYPE(presale.getORDER_TXNTYPE());
                        ordHed.setORDER_IS_ACTIVE(presale.getORDER_IS_ACTIVE());
                        ordHed.setORDER_IS_SYNCED(presale.getORDER_IS_SYNCED());
                        ordHed.setORDER_LOCCODE(presale.getORDER_LOCCODE());
                        ordHed.setORDER_AREACODE(presale.getORDER_AREACODE());
                        ordHed.setORDER_ROUTECODE(presale.getORDER_ROUTECODE());
                        ordHed.setORDER_COSTCODE(presale.getORDER_COSTCODE());
                        ordHed.setORDER_TAXREG(presale.getORDER_TAXREG());
                        ordHed.setORDER_TOURCODE(presale.getORDER_TOURCODE());
                        ordHed.setORDER_BPTOTALDIS("0");
                        ordHed.setORDER_BTOTALAMT("0");
                        ordHed.setORDER_BTOTALDIS("0");
                        ordHed.setORDER_BTOTALTAX("0");
                        ordHed.setORDER_LATITUDE(mSharedPref.getGlobalVal("Latitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Latitude"));
                        ordHed.setORDER_LONGITUDE(mSharedPref.getGlobalVal("Longitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Longitude"));
                        //ordHed.setORDER_ADDRESS(localSP.getString("GPS_Address", "").toString());
                        ordHed.setORDER_TOTALTAX("0");
                        ordHed.setORDER_TOTALDIS("0.0");
                        ordHed.setORDER_TOTALAMT(lblNetVal.getText().toString());
                        ordHed.setORDER_TXNDATE(presale.getORDER_TXNDATE());
                        ordHed.setORDER_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
                        ordHed.setORDER_REFNO1("");
                        ordHed.setORDER_TOTQTY(lblQty.getText().toString());
                        ordHed.setORDER_TOTFREEQTY(iTotFreeQty + "");
                        ordHed.setORDER_SETTING_CODE(presale.getORDER_SETTING_CODE());

                        ordHedList.add(ordHed);

                        if (new OrderController(getActivity()).createOrUpdateOrdHed(ordHedList) > 0) {
                            new ProductController(getActivity()).mClearTables();
                            new OrderController(getActivity()).InactiveStatusUpdate(RefNo);
                            new OrderDetailController(getActivity()).InactiveStatusUpdate(RefNo);

                            final PreSalesActivity activity = (PreSalesActivity) getActivity();
                            //new ReferenceNum(getActivity()).nNumValueInsertOrUpdate(getResources().getString(R.string.NumVal));

                            /*-*-*-*-*-*-*-*-*-*-QOH update-*-*-*-*-*-*-*-*-*/

                            UpdateTaxDetails(RefNo);
                            //UpdateQOH_FIFO();
                            new ItemLocController(getActivity()).UpdateOrderQOH(RefNo, "-", locCode);
                            //updateDispTables(sHed);
                            //new VanSalePrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Print preview", RefNo,false);

                            //if(a == 1)
                            //{
                            new VanSalePrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Print preview", RefNo,"",true);
                            Toast.makeText(getActivity(), "Order saved successfully..!", Toast.LENGTH_SHORT).show();
                            //  UtilityContainer.ClearVanSharedPref(getActivity());
                            //   activity.cusPosition = 0;
                            activity.selectedDebtor = null;
                            activity.selectedRetDebtor = null;
                            // activity.selectedRecHed = null;
                            activity.selectedPreHed = null;
//                            Intent intent = new Intent(getActivity(),DebtorDetailsActivity.class);
//                            startActivity(intent);
//                            getActivity().finish();
                            //}

                            //new LoardingPrintView();

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
            Toast.makeText(getActivity(), "Add items before save ...!", Toast.LENGTH_SHORT).show();


    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateTaxDetails(String refNo) {
//        ArrayList<InvDet> list = new InvDetController(getActivity()).getAllInvDet(refNo);
//        new InvDetController(getActivity()).UpdateItemTaxInfo(list);
//        new InvTaxRGController(getActivity()).UpdateInvTaxRG(list);
//        new InvTaxDTController(getActivity()).UpdateInvTaxDT(list);
    }
    public void UpdateReturnTotal(String refNo) {
//        ArrayList<FInvRDet> list = new SalesReturnDetController(getActivity()).getAllInvRDet(refNo);
//        new SalesReturnDetController(getActivity()).UpdateReturnTot(list);

    }
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        this.mainActivity = activity;
//    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

//    private void UpdateQOH_FIFO() {
//
//        ArrayList<InvDet> list = new InvDetController(getActivity()).getAllInvDet(RefNo);
//
//        /*-*-*-*-*-*-*-*-*-*-*-*-each itemcode has multiple sizecodes*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*/
//        for (InvDet item : list) {
//
//            int Qty = (int) Double.parseDouble(item.getFINVDET_QTY());
//
//            ArrayList<StkIn> GRNList = new STKInController(getActivity()).getAscendingGRNList(item.getFINVDET_ITEM_CODE(), locCode);
//
//            /*-*-*-*-*-*-*-*-*-*-multiple GRN for each sizecode-*-*-*-*-*-*-*-*-*-*-*/
//            for (StkIn size : GRNList) {
//                int balQty = (int) Double.parseDouble(size.getBALQTY());
//
//                if (balQty > 0) {
//                    if (Qty > balQty) {
//                        Qty = Qty - balQty;
//                        size.setBALQTY("0");
//                        new StkIssController(getActivity()).InsertSTKIssData(size, RefNo, String.valueOf(balQty), locCode);
//
//                    } else {
//                        size.setBALQTY(String.valueOf(balQty - Qty));
//                        new StkIssController(getActivity()).InsertSTKIssData(size, RefNo, String.valueOf(Qty), locCode);
//                        break;
//                    }
//                }
//            }
//            new STKInController(getActivity()).UpdateBalQtyByFIFO(GRNList);
//        }
//    }
////
    public void mPauseinvoice() {

        if (new OrderDetailController(getActivity()).getItemCount(RefNo) > 0) {
            Intent intnt = new Intent(getActivity(),DebtorDetailsActivity.class);
            startActivity(intnt);
            getActivity().finish();
        } else
            Toast.makeText(getActivity(), "Add items before pause ...!", Toast.LENGTH_SHORT).show();
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_PRE_SUMMARY"));
    }

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
            mRefreshData();
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
                ArrayList<StkIn> GRNList = new STKInController(getActivity()).getAscendingGRNList(item.getFINVDET_ITEM_CODE(), locCode);

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    

}
