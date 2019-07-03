package com.datamation.sfa.vansale;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.datamation.sfa.R;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.InvDet;
import com.datamation.sfa.model.InvHed;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VanSalesOrderDetails extends Fragment {

    View view;
    int totPieces = 0;
    int seqno = 0;
    ListView lv_order_det, lvFree;
    ArrayList<InvDet> orderList;
    SharedPref mSharedPref;
    String RefNo, locCode;
  //  MainActivity mainActivity;
    MyReceiver r;
   // ArrayList<Product> productList = null, selectedItemList = null;
    ImageButton ibtProduct, ibtDiscount;
    private  SweetAlertDialog pDialog;
    private InvHed tmpinvHed=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        seqno = 0;
        totPieces = 0;
        view = inflater.inflate(R.layout.sales_management_van_sales_new_details, container, false);
        mSharedPref = new SharedPref(getActivity());
        locCode = new SharedPref(getActivity()).getGlobalVal("KeyLocCode");
       // mainActivity = (MainActivity) getActivity();
//        if(mainActivity.selectedDebtor != null)
//        {
//            if(mainActivity.selectedDebtor.getFDEBTOR_TAX_REG().equals("Y")){
//                RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.VanNumValTax));
//            }else {
//                RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.VanNumValNonTax));
//            }
//        }
//        else
//        {
//            Toast.makeText(mainActivity,"Select a customer to continue...",Toast.LENGTH_SHORT);
//        }

//        lv_order_det = (ListView) view.findViewById(R.id.lvProducts_Inv);
//        lvFree = (ListView) view.findViewById(R.id.lvFreeIssue_Inv);
//
//        ibtDiscount = (ImageButton) view.findViewById(R.id.ibtDisc);
//        ibtProduct = (ImageButton) view.findViewById(R.id.ibtProduct);
        tmpinvHed=new InvHed();


//        ibtDiscount.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                       // ibtDiscount.setBackground(getResources().getDrawable(R.drawable.discount_down));
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_UP: {
//                        ibtDiscount.setBackground(getResources().getDrawable(R.drawable.discount));
//                    }
//                    break;
//                }
//                return false;
//            }
//        });

        //*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*//




        //*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*//*

//        lv_order_det.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
//                /*new InvDetDS(getActivity()).restFreeIssueData(RefNo);
//                new OrdFreeIssueDS(getActivity()).ClearFreeIssues(RefNo);
//                InvDet invDet = orderList.get(position);
//
//                FreeIssue issue = new FreeIssue(getActivity());
//                ArrayList<FreeItemDetails> list = issue.getEligibleFreeItemsByInvoiceItem( orderList.get(position), new SharedPref(getActivity()).getGlobalVal("KeyCostCode"));
//                popEditDialogBox(invDet,list);*/
//
//            }
//        });

        return view;

    }





	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch_Inv:

                break;

            case R.id.btn_add_Inv: {

                if (!lblProduct.getText().toString().equals("")) {

                    if (values >= 0 && !(txtQty.getText().toString().equals("")) && Integer.parseInt(txtQty.getText().toString()) > 0) {

                        InvDet invDet = new InvDet();
                        ArrayList<InvDet> ordList = new ArrayList<>();

                        MainActivity activity = (MainActivity) getActivity();
                        InvHedDS invHedDS = new InvHedDS(getActivity());
                        ArrayList<InvHed> invHedList = new ArrayList<>();
                        invHedList.add(activity.selectedInvHed);

                        if (invHedDS.getActiveInvoiceRef() != null) {

                            String TaxedAmt = new TaxDetDS(getActivity()).calculateTax(selectedItem.getFITEM_ITEM_CODE(), new BigDecimal(lblAmt.getText().toString()));

                            invDet.setFINVDET_ID(index_id + "");
                            invDet.setFINVDET_AMT(String.format("%.2f", Double.parseDouble(lblAmt.getText().toString()) - Double.parseDouble(TaxedAmt)));
                            invDet.setFINVDET_BAL_QTY(txtQty.getText().toString());
                            invDet.setFINVDET_B_AMT(String.format("%.2f", Double.parseDouble(lblAmt.getText().toString()) - Double.parseDouble(TaxedAmt)));
                            invDet.setFINVDET_B_SELL_PRICE(lblPrice.getText().toString());
                            invDet.setFINVDET_BT_SELL_PRICE(lblPrice.getText().toString());
                            invDet.setFINVDET_DIS_AMT(lblDisc.getText().toString());
                            invDet.setFINVDET_DIS_PER("0");
                            invDet.setFINVDET_ITEM_CODE(selectedItem.getFITEM_ITEM_CODE());
                            invDet.setFINVDET_PRIL_CODE(activity.selectedDebtor.getFDEBTOR_PRILLCODE());
                            invDet.setFINVDET_QTY(txtQty.getText().toString());
                            invDet.setFINVDET_PICE_QTY(txtQty.getText().toString());
                            invDet.setFINVDET_TYPE("SA");
                            invDet.setFINVDET_BT_TAX_AMT("");
                            invDet.setFINVDET_RECORD_ID("");
                            invDet.setFINVDET_SELL_PRICE(String.format("%.2f", (Double.parseDouble(lblAmt.getText().toString()) - Double.parseDouble(TaxedAmt)) / Double.parseDouble(invDet.getFINVDET_QTY())));
                            invDet.setFINVDET_B_SELL_PRICE(String.format("%.2f", (Double.parseDouble(lblAmt.getText().toString()) - Double.parseDouble(TaxedAmt)) / Double.parseDouble(invDet.getFINVDET_QTY())));
                            invDet.setFINVDET_SEQNO(new InvDetDS(getActivity()).getLastSequnenceNo(activity.selectedInvHed.getFINVHED_REFNO()));
                            invDet.setFINVDET_TAX_AMT(TaxedAmt);
                            invDet.setFINVDET_TAX_COM_CODE(new ItemsDS(getActivity()).getTaxComCodeByItemCode(selectedItem.getFITEM_ITEM_CODE()));
                            invDet.setFINVDET_T_SELL_PRICE(String.format("%.2f", Double.parseDouble(lblAmt.getText().toString()) / Double.parseDouble(invDet.getFINVDET_QTY())));
                            invDet.setFINVDET_BT_SELL_PRICE(String.format("%.2f", Double.parseDouble(lblAmt.getText().toString()) / Double.parseDouble(invDet.getFINVDET_QTY())));
                            invDet.setFINVDET_REFNO(activity.selectedInvHed.getFINVHED_REFNO());
                            invDet.setFINVDET_COM_DISCPER(new ControlDS(getActivity()).getCompanyDisc() + "");
                            invDet.setFINVDET_BRAND_DISCPER(brandDisPer + "");
                            invDet.setFINVDET_BRAND_DISC(String.format("%.2f", brandDis));
                            invDet.setFINVDET_COMDISC(String.format("%.2f", CompDisc));
                            invDet.setFINVDET_TXN_DATE(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            invDet.setFINVDET_TXN_TYPE("22");
                            invDet.setFINVDET_IS_ACTIVE("1");
                            invDet.setFINVDET_QOH(iQoh + "");
                            invDet.setFINVDET_DISVALAMT("0");
                            invDet.setFINVDET_PRICE(lblPrice.getText().toString());

                            ordList.add(invDet);

                            if (!(btnAdd.getText().equals("EDIT") && hasChanged == false)) {
                                new InvDetDS(getActivity()).createOrUpdateInvDet(ordList);
                            }

                            if (btnAdd.getText().equals("EDIT"))
                                android.widget.Toast.makeText(getActivity(), "Edited successfully !", android.widget.Toast.LENGTH_SHORT).show();
                            else
                                android.widget.Toast.makeText(getActivity(), "Added successfully !", android.widget.Toast.LENGTH_SHORT).show();

                            txtQty.setEnabled(false);
                            clearTextFields();
                            //FetchData();
                            UtilityContainer.hideKeyboard(getActivity());
                        }
                    } else {
                        android.widget.Toast.makeText(getActivity(), "Enter item quantity..!", android.widget.Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;


            default:
                break;
        }
    }*/

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/



	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public double getAvailableTotal(double discVal, ArrayList<InvDet> OrderList) {
        double avQTY = 0;

        for (InvDet mOrdDet : OrderList) {
            avQTY = avQTY + Double.parseDouble(mOrdDet.getFINVDET_DIS_AMT());
            System.out.println(mOrdDet.getFINVDET_DIS_AMT() + " - " + mOrdDet.getFINVDET_ITEM_CODE());
        }

        return (discVal - avQTY);

    }



    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void mToggleTextbox() {

        if (mSharedPref.getGlobalVal("keyCustomer").equals("Y")) {
            ibtProduct.setEnabled(true);
            ibtDiscount.setEnabled(true);

            Bundle mBundle = getArguments();
            if (mBundle != null) {
                tmpinvHed = (InvHed) mBundle.getSerializable("order");
            }

        } else {
            ibtProduct.setEnabled(false);
            ibtDiscount.setEnabled(false);
        }
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

   	/*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_DETAILS"));
    }

	/*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/



	/*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            VanSalesOrderDetails.this.mToggleTextbox();
        }
    }


}