package com.datamation.sfa.presale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class OrderSummaryFragment extends Fragment {

    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    View view;
    TextView lblNetVal, lblDisc, lblSchDisc, lblLineDisc, lblGross, lblQty;
    //SharedPref mSharedPref;
    String RefNo = null;
    //TranSOHed Header;
    //ArrayList<TranSODet> OrdDetList;
    Activity activity;
    int freeQty;
    FloatingActionButton fabPause, fabDiscard, fabSave;
    FloatingActionMenu fam;
//    MyReceiver r;
//    MainActivity mainActivity;
    private static final String TAG = "PreSalesOrderSummary";
//    ArrayList<PreProduct> PreproductList = null, selectedItemList = null;
//    private TranSOHed tmpsoHed=null;
//    SweetAlertDialog pDialog;
    boolean isPresalePending = false;
//    GPSTracker gpsTracker;
//    IResponseListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_summary, container, false);

        localSP = getActivity().getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);
//        RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
//        mSharedPref = new SharedPref(getActivity());

        fam = (FloatingActionMenu) view.findViewById(R.id.fab_menu);

        fabPause = (FloatingActionButton) view.findViewById(R.id.fab2);
        fabDiscard = (FloatingActionButton) view.findViewById(R.id.fab3);
        fabSave = (FloatingActionButton) view.findViewById(R.id.fab1);

        lblNetVal = (TextView) view.findViewById(R.id.lblNetVal);
        lblDisc = (TextView) view.findViewById(R.id.lblDisc);
        lblSchDisc = (TextView) view.findViewById(R.id.lblSchDisc);
        lblLineDisc = (TextView) view.findViewById(R.id.lblLineDisc);
        lblGross = (TextView) view.findViewById(R.id.lblGross);
        lblQty = (TextView) view.findViewById(R.id.lblQty);
//        mainActivity = (MainActivity) getActivity();
//        tmpsoHed=new TranSOHed();

//        if (mainActivity.selectedSOHed == null) {
//            TranSOHed SOHed = new TranSOHedDS(getActivity()).getActiveSOHed();
//            if (SOHed != null) {
//                if (mainActivity.selectedDebtor == null)
//                    mainActivity.selectedDebtor = new DebtorDS(getActivity()).getSelectedCustomerByCode(SOHed.getFTRANSOHED_DEBCODE());
//            }
//        }

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });
        fam.setClosedOnTouchOutside(true);
        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPauseOrder();
            }
        });

        //Log.d(TAG, "TOTAL_IS: " + lblNetVal.getText().toString().trim() + " LIMIT_IS: " + mainActivity.selectedDebtor.getFDEBTOR_CRD_LIMIT());

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

//        if (new TranSOHedDS(activity).validateActivePreSales())
//        {
//            isPresalePending = true;
//        }
//        else
//        {
//            isPresalePending = false;
//        }

//        if (mSharedPref.getGlobalVal("preKeyIsFreeClicked").equals("0") || mSharedPref.getGlobalVal("preKeyIsFreeClicked")== null)
//        {
//            //listener.moveBackToCustomer_pre(2);
//            Toast.makeText(getActivity(), "Please tap on Free Issue Button", Toast.LENGTH_LONG).show();
//        }

        return view;
    }

    public void refreshData() {

//        if (mSharedPref.getGlobalVal("preKeyIsFreeClicked").equals("0"))
//        {
//            listener.moveBackToCustomer_pre(2);
//            Toast.makeText(getActivity(), "Please tap on Free Issue Button", Toast.LENGTH_LONG).show();
//        }
//
//        double ftotAmt = 0, fTotLineDisc = 0, fTotSchDisc = 0, fTotQtySlabDisc = 0;
//        int ftotQty = 0, fTotFree = 0;
//        String itemCode = "";
//
//        OrdDetList = new TranSODetDS(getActivity()).getAllOrderDetails(RefNo);
//        Header = new TranSOHedDS(getActivity()).getActiveSOHed();
//
//        for (TranSODet ordDet : OrdDetList) {
//            ftotAmt += Double.parseDouble(ordDet.getFTRANSODET_AMT());
//            itemCode = ordDet.getFTRANSODET_ITEMCODE();
//
//            if (ordDet.getFTRANSODET_TYPE().equals("SA"))
//                ftotQty += Integer.parseInt(ordDet.getFTRANSODET_QTY());
//            else
//                fTotFree += Integer.parseInt(ordDet.getFTRANSODET_QTY());
//
//            fTotLineDisc += Double.parseDouble(ordDet.getFTRANSODET_DISAMT());
//            fTotSchDisc += Double.parseDouble(ordDet.getFTRANSODET_SCHDISC());
//            fTotQtySlabDisc += Double.parseDouble(ordDet.getFTRANSODET_QTY_SLAB_DISC());
//        }

//        freeQty = fTotFree;
//        lblQty.setText(String.valueOf(ftotQty + fTotFree));
//        double totAmt = ftotAmt + fTotSchDisc + fTotLineDisc + fTotQtySlabDisc;
////        String grossArray[] = new TaxDetDS(getActivity()).calculateTaxForwardFromDebTax(mSharedPref.getGlobalVal("PrekeyCusCode"), itemCode, totAmt );
////        String NetArray[] = new TaxDetDS(getActivity()).calculateTaxForwardFromDebTax(mSharedPref.getGlobalVal("PrekeyCusCode"), itemCode, ftotAmt );
////        String disArray[] = new TaxDetDS(getActivity()).calculateTaxForwardFromDebTax(mSharedPref.getGlobalVal("PrekeyCusCode"), itemCode, fTotSchDisc + fTotQtySlabDisc );
////        String totDisArray[] = new TaxDetDS(getActivity()).calculateTaxForwardFromDebTax(mSharedPref.getGlobalVal("PrekeyCusCode"), itemCode, fTotSchDisc + fTotLineDisc + fTotQtySlabDisc );
//////        lblGross.setText(String.format("%.2f", ftotAmt + fTotSchDisc + fTotLineDisc + fTotQtySlabDisc));
//////        lblDisc.setText(String.format("%.2f", fTotSchDisc + fTotLineDisc + fTotQtySlabDisc ));
//////        lblNetVal.setText(String.format("%.2f", ftotAmt));
//////        lblSchDisc.setText(String.format("%.2f", fTotSchDisc + fTotQtySlabDisc));
////        lblGross.setText(String.format("%.2f", Double.parseDouble(grossArray[0])));
////        lblDisc.setText(String.format("%.2f", Double.parseDouble(totDisArray[0]) ));
////        lblNetVal.setText(String.format("%.2f", Double.parseDouble(NetArray[0])));
////        lblSchDisc.setText(String.format("%.2f", Double.parseDouble(disArray[0])));
////        lblLineDisc.setText(String.format("%.2f", fTotLineDisc));
//        ftotAmt = 0;
//        fTotSchDisc = 0;
//        fTotLineDisc = 0;
//        fTotQtySlabDisc = 0;
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- Save final Sales order to database-*-*-*-**-*-*-*-*-*-*-*-*-*/

    public void undoEditingData() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Do you want to discard the order?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

//                MainActivity activity = (MainActivity) getActivity();
//                boolean result = new TranSOHedDS(getActivity()).restData(RefNo);
//
//                if (result) {
//                    new TranSODetDS(getActivity()).restData(RefNo);
//                    new PreProductDS(getActivity()).mClearTables();
//                    new OrderDiscDS(getActivity()).clearData(RefNo);
//                    new OrdFreeIssueDS(getActivity()).ClearFreeIssues(RefNo);
//                    new PreSaleTaxDTDS(getActivity()).ClearTable(RefNo);
//                    new PreSaleTaxRGDS(getActivity()).ClearTable(RefNo);
//                }
//                activity.cusPosition = 0;
//                activity.selectedDebtor = null;
//                activity.selectedSOHed = null;
//                Toast.makeText(getActivity(), "Order discarded successfully..!", Toast.LENGTH_LONG).show();
//                UtilityContainer.PreClearSharedPref(getActivity());
//                UtilityContainer.mLoadFragment(new PreSalesInvoice(), activity);

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void saveSummaryDialog() {

        //gpsTracker = new GPSTracker(getActivity());

//        if (!(gpsTracker.canGetLocation()))
//        {
//            gpsTracker.showSettingsAlert();
//        }
//        else if (new TranSODetDS(getActivity()).getItemCount(RefNo) > 0) {
//
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//            alertDialogBuilder.setMessage("Do you want to save the order ?");
//            alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//            alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                public void onClick(final DialogInterface dialog, int id) {
//
////                    TranSOHed mainHead = new TranSOHed();
////                    ArrayList<TranSOHed> ordHedList = new ArrayList<TranSOHed>();
////
////                    if (Header != null) {
////
////                        mainHead.setFTRANSOHED_REFNO(RefNo);
////                        mainHead.setFTRANSOHED_DEBCODE(Header.getFTRANSOHED_DEBCODE());
////                        mainHead.setFTRANSOHED_TXNDELDATE(Header.getFTRANSOHED_TXNDELDATE());
////                        mainHead.setFTRANSOHED_MANUREF(Header.getFTRANSOHED_MANUREF());
////                        mainHead.setFTRANSOHED_REMARKS(Header.getFTRANSOHED_REMARKS());
////                        mainHead.setFTRANSOHED_ADDMACH(Header.getFTRANSOHED_ADDMACH());
////                        mainHead.setFTRANSOHED_CURCODE(Header.getFTRANSOHED_CURCODE());
////                        mainHead.setFTRANSOHED_CURRATE(Header.getFTRANSOHED_CURRATE());
////                        mainHead.setFTRANSOHED_REPCODE(Header.getFTRANSOHED_REPCODE());
////                        mainHead.setFTRANSOHED_CONTACT(Header.getFTRANSOHED_CONTACT());
////                        mainHead.setFTRANSOHED_CUSADD1(Header.getFTRANSOHED_CUSADD1());
////                        mainHead.setFTRANSOHED_CUSADD2(Header.getFTRANSOHED_CUSADD2());
////                        mainHead.setFTRANSOHED_CUSADD3(Header.getFTRANSOHED_CUSADD3());
////                        mainHead.setFTRANSOHED_CUSTELE(Header.getFTRANSOHED_CUSTELE());
////                        mainHead.setFTRANSOHED_TXNTYPE(Header.getFTRANSOHED_TXNTYPE());
////                        mainHead.setFTRANSOHED_TXNDATE(Header.getFTRANSOHED_TXNDATE());
////                        mainHead.setFTRANSOHED_TAXREG(Header.getFTRANSOHED_TAXREG());
////                        //mainHead.setFTRANSOHED_TAXREG(new FDebTaxDS(getActivity()).getTaxRegNo(mainActivity.selectedDebtor.getFDEBTOR_CODE()));
////                        mainHead.setFTRANSOHED_TOURCODE(Header.getFTRANSOHED_TOURCODE());
////                        mainHead.setFTRANSOHED_LOCCODE(Header.getFTRANSOHED_LOCCODE());
////                        mainHead.setFTRANSOHED_AREACODE(Header.getFTRANSOHED_AREACODE());
////                        mainHead.setFTRANSOHED_ROUTECODE(Header.getFTRANSOHED_ROUTECODE());
////                        mainHead.setFTRANSOHED_COSTCODE(Header.getFTRANSOHED_COSTCODE());
////                        mainHead.setFTRANSOHED_IS_ACTIVE("0");
////                        mainHead.setfTRANSOHED_IS_SYNCED("0");
////                        mainHead.setFTRANSOHED_REFNO1(Header.getFTRANSOHED_REFNO1());
////
////                        Log.d("PRE_SALES_SUMMARY", "TOTAL_TAX_IS: " + mainHead.getFTRANSOHED_TOTALTAX());
////                    }
////
////                    mainHead.setFTRANSOHED_BPTOTALDIS("0");
////                    mainHead.setFTRANSOHED_BTOTALAMT("0");
////                    mainHead.setFTRANSOHED_BTOTALDIS("0");
////                    mainHead.setFTRANSOHED_BTOTALTAX("0");
////
////
////                    mainHead.setFTRANSOHED_LATITUDE(mSharedPref.getGlobalVal("Latitude").equals("")?"0.0":mSharedPref.getGlobalVal("Latitude"));
////                    mainHead.setFTRANSOHED_LONGITUDE(mSharedPref.getGlobalVal("Longitude").equals("")?"0.0":mSharedPref.getGlobalVal("Longitude"));
////                    mainHead.setFTRANSOHED_TOTALTAX(Header.getFTRANSOHED_TOTALTAX());
////                    //mainHead.setFTRANSOHED_TOTALTAX("");
////                    mainHead.setFTRANSOHED_TOTALDIS(lblDisc.getText().toString());
////                    mainHead.setFTRANSOHED_TOTALAMT(lblNetVal.getText().toString());
////                    mainHead.setFTRANSOHED_PTOTALDIS("0");
////                    mainHead.setFTRANSOHED_START_TIMESO(localSP.getString("SO_Start_Time", "").toString());
////                    mainHead.setFTRANSOHED_END_TIMESO(currentTime());
////                    mainHead.setFTRANSOHED_TOTQTY(lblQty.getText() + "");
////                    mainHead.setFTRANSOHED_TOTFREE(freeQty + "");
////                    ordHedList.add(mainHead);
//
////                    Log.d("PRE_SALES_SUMMARY", "LOC_CODE_IS: " + mainHead.getFTRANSOHED_LOCCODE());
//
////                    if (new TranSOHedDS(getActivity()).createOrUpdateTranSOHedDS(ordHedList) > 0) {
////
////                        new TranSODetDS(getActivity()).InactiveStatusUpdate(RefNo);
////                        new TranSOHedDS(getActivity()).InactiveStatusUpdate(RefNo);
////                        new PreProductDS(getActivity()).mClearTables();
////                        MainActivity activity = (MainActivity) getActivity();
////                        activity.cusPosition = 0;
////
////                        activity.selectedSOHed = null;
////
////                        UpdateTaxDetails(RefNo);
////                        new ItemLocDS(getActivity()).UpdateInvoiceQOH(RefNo, "-", mSharedPref.getGlobalVal("PrekeyLocCode").trim());
////                        activity.selectedDebtor = null;
////                        new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.NumVal));
////                        Toast.makeText(getActivity(), "Order saved successfully !", Toast.LENGTH_LONG).show();
////                        new SalesPrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Sales Order", RefNo, false);
////                        UtilityContainer.PreClearSharedPref(getActivity());
////                        UtilityContainer.mLoadFragment(new PreSalesInvoice(), activity);
////                    } else {
////                        Toast.makeText(getActivity(), "Order failed !", Toast.LENGTH_LONG).show();
////                    }
//                }
//            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alertD = alertDialogBuilder.create();
//            alertD.show();
//        }else{
//            Toast.makeText(getActivity(), "Order det failed !", Toast.LENGTH_LONG).show();
//            saveValidationDialogBox();
//        }
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateTaxDetails(String refNo) {

//        ArrayList<TranSODet> list = new TranSODetDS(activity).getEveryItem(refNo);
//        new TranSODetDS(activity).UpdateItemTaxInfoWithDiscount(list, mainActivity.selectedDebtor.getFDEBTOR_CODE());
//        new PreSaleTaxRGDS(activity).UpdateSalesTaxRG(list, mainActivity.selectedDebtor.getFDEBTOR_CODE());
//        new PreSaleTaxDTDS(activity).UpdateSalesTaxDT(list);
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);

        try {
            //listener = (IResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void mPauseOrder() {

//        if (new TranSODetDS(getActivity()).getItemCount(RefNo) > 0)
//            UtilityContainer.mLoadFragment(new IconPallet(), activity);
//        else
//            Toast.makeText(activity, "Add items before pause ...!", Toast.LENGTH_SHORT).show();
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onResume() {
        super.onResume();
        //r = new MyReceiver();
        //LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_SUMMARY"));
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //PreSalesSummary.this.refreshData();
        }
    }

    public void saveValidationDialogBox() {

//        String message = "Please add products for New Sales Order.";
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        alertDialogBuilder.setTitle("Sales Order");
//        alertDialogBuilder.setMessage(message);
//        alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
//                dialog.cancel();
//
//                if (fam.isOpened()) {
//                    fam.close(true);
//                }
//                if (isPresalePending) {
//                    PreSales preSales = new PreSales();
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("Active", true);
//                    preSales.setArguments(bundle);
//                    UtilityContainer.mLoadFragment(preSales, activity);
//                }
//            }
//        }).setNegativeButton("", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//
//            }
//        });
//
//        AlertDialog alertD = alertDialogBuilder.create();
//        alertD.show();

    }


}
