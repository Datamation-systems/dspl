package com.datamation.sfa.presale;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.PRESALE;
import com.datamation.sfa.model.tempOrderDet;

import com.datamation.sfa.R;
import com.datamation.sfa.settings.ReferenceNum;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderDetailFragment extends Fragment{

    private static final String TAG = "OrderDetailFragment";
    public View view;
    public SharedPref mSharedPref;
    private ListView lv_pre_order_detlv, lv_pre_Free;
    private ImageButton pre_Product_btn, pre_Discount_btn;
    private  String RefNo;
    //public MainActivity mainActivity;
    int seqno = 0, index_id = 0;
    ArrayList<tempOrderDet> PreproductList = null, selectedItemList = null;
    //ArrayList<TranSODet> orderList;
    SweetAlertDialog pDialog;
    //private  MyReceiver r;
    private PRESALE tmpsoHed=null;  //from re oder creation
    //MainActivity activity;
    //ArrayList<FItTourDisc>ftdList = null;
    //IResponseListener listener;
    Activity mactivity;
    int count = 0;
    private double totAmt = 0.0;
    private Customer debtor;


    public OrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sales_management_pre_sales_details_new, container, false);
        lv_pre_order_detlv = (ListView) view.findViewById(R.id.lvProducts_Pre);
        lv_pre_Free = (ListView) view.findViewById(R.id.lvFreeIssue_Pre);
        pre_Product_btn = (ImageButton) view.findViewById(R.id.btnPreSalesProduct);
        pre_Discount_btn = (ImageButton) view.findViewById(R.id.btnPreSalesDisc);
        seqno = 0;
        mSharedPref = new SharedPref(getActivity());
        debtor = new Customer();
        RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
        mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");

        // load pre sales products to temporary preSales table and load them
        pre_Product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new LoadingPreProductFromDB().execute();
                mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");
                count = 0;
            }
        });

        pre_Product_btn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        pre_Product_btn.setBackground(getResources().getDrawable(R.drawable.product_down));
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_UP: {
//                        pre_Product_btn.setBackground(getResources().getDrawable(R.drawable.product));
//                    }
//                    break;
//                }
                return false;
            }
        });
//------------------------------------------------------------------------------------------------------------------------------------
        lv_pre_order_detlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                new TranSODetDS(getActivity()).restFreeIssueData(RefNo);
//                new OrdFreeIssueDS(getActivity()).ClearFreeIssues(RefNo);
//                TranSODet tranSODet=orderList.get(position);
//                //String price = new PreProductDS(getActivity()).getPriceByItemCode(tranSODet.getFTRANSODET_ITEMCODE());
//                //tranSODet.setFTRANSODET_PRICE(price);
//                mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");
//                FreeIssue issue = new FreeIssue(getActivity());
//                ArrayList<FreeItemDetails> list=issue.getEligibleFreeItemsBySalesItem(orderList.get(position),new SharedPref(getActivity()).getGlobalVal("KeyCostCode"));
//                popEditDialogBox(tranSODet,list);

            }
        });
        //---------------------------------------------------------------------------------------------------------------------------------
        lv_pre_order_detlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                new TranSODetDS(getActivity()).restFreeIssueData(RefNo);
//                new OrdFreeIssueDS(getActivity()).ClearFreeIssues(RefNo);
//                mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");
//                newDeleteOrderDialog(position);
                return true;
            }
        });

        //----------------------------------------------discountFreeissue-------------------------------------------------------------------------

        pre_Discount_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        pre_Discount_btn.setBackground(getResources().getDrawable(R.drawable.discount_down));
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_UP: {
//                        pre_Discount_btn.setBackground(getResources().getDrawable(R.drawable.discount));
//                    }
//                    break;
//                }
                return false;
            }
        });

        //-----------------------------------------pre Discount calculation-----------------------------------------------------

        pre_Discount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                count++;
//                mSharedPref.setGlobalVal("preKeyIsFreeClicked", ""+count);
//                if(mSharedPref.getGlobalVal("preKeyIsFreeClicked").equals("1")) {
//
//                    calculateFreeIssue(mainActivity.selectedDebtor.getFDEBTOR_CODE());
//                }else{
//                    Log.v("Freeclick Count", mSharedPref.getGlobalVal("preKeyIsFreeClicked"));
//                }
            }
        });


        return view;
    }


}
