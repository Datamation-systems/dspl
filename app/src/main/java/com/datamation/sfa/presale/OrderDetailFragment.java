package com.datamation.sfa.presale;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.datamation.sfa.adapter.OrderDetailsAdapter;
import com.datamation.sfa.adapter.PreOrderAdapter;
import com.datamation.sfa.controller.ItemController;
import com.datamation.sfa.controller.OrderDetailController;
import com.datamation.sfa.controller.PreProductController;
import com.datamation.sfa.controller.SalRepController;
import com.datamation.sfa.controller.TaxDetController;
import com.datamation.sfa.helpers.PreSalesResponseListener;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.model.Order;
import com.datamation.sfa.model.PreProduct;

import com.datamation.sfa.R;
import com.datamation.sfa.settings.ReferenceNum;
import com.datamation.sfa.view.PreSalesActivity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderDetailFragment extends Fragment{

    int totPieces = 0;
    int seqno = 0;
    ListView lv_order_det, lvFree;
    ArrayList<PreProduct> productList = null, selectedItemList = null;
    ImageButton ibtProduct, ibtDiscount;
    SweetAlertDialog pDialog;
    private static final String TAG = "OrderDetailFragment";
    public View view;
    public SharedPref mSharedPref;
    private  String RefNo, locCoe;
    private  MyReceiver r;
    private Order tmpsoHed=null;  //from re oder creation
    PreSalesResponseListener preSalesResponseListener;
    int count = 0;
    private double totAmt = 0.0;
    private Customer debtor;
    PreSalesActivity mainActivity;
    ArrayList<OrderDetail> orderList;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sales_management_pre_sales_details_new, container, false);
        seqno = 0;
        totPieces = 0;
        mSharedPref =SharedPref.getInstance(getActivity());
        lv_order_det = (ListView) view.findViewById(R.id.lvProducts_pre);
        lvFree = (ListView) view.findViewById(R.id.lvFreeIssue_Inv);
        ibtDiscount = (ImageButton) view.findViewById(R.id.ibtDisc);
        ibtProduct = (ImageButton) view.findViewById(R.id.ibtProduct);
        mainActivity = (PreSalesActivity)getActivity();
//        if(mainActivity.selectedDebtor != null)
        RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));

//        RefNo = mainActivity.selectedPreHed.getORDER_REFNO();
        tmpsoHed = new Order();
        showData();

        ibtProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoardingProductFromDB().execute();
            }
        });

        ibtDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calculateFreeIssue();
            }
        });

        lv_order_det.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //new InvDetController(getActivity()).restFreeIssueData(RefNo);
                newDeleteOrderDialog(position);
                return true;
            }
        });

        lv_order_det.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {

            }
        });

        return view;
    }

    public class LoardingProductFromDB extends AsyncTask<Object, Object, ArrayList<PreProduct>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Fetch Data Please Wait.");
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected ArrayList<PreProduct> doInBackground(Object... objects) {

            if (new PreProductController(getActivity()).tableHasRecords()) {
                productList = new PreProductController(getActivity()).getAllItems("");
            } else {
                //productList =new ItemController(getActivity()).getAllItemForPreSales("","",RefNo, mSharedPref.getSelectedDebtorPrilCode());
                new PreProductController(getActivity()).insertIntoProductAsBulkForPre(new SalRepController(getActivity()).getCurrentLocCode().trim(), mSharedPref.getSelectedDebtorPrilCode());
                //productList = new PreProductController(getActivity()).getAllItems("");

                if(tmpsoHed!=null) {

                    ArrayList<OrderDetail> orderDetailArrayList = tmpsoHed.getSoDetArrayList();
                    if (orderDetailArrayList != null) {
                        for (int i = 0; i < orderDetailArrayList.size(); i++) {
                            String tmpItemName = orderDetailArrayList.get(i).getFORDERDET_ITEMCODE();
                            String tmpQty = orderDetailArrayList.get(i).getFORDERDET_QTY();
                            //Update Qty in  fProducts_pre table
                            int count = new PreProductController(getActivity()).updateProductQtyFor(tmpItemName, tmpQty);
                            if (count > 0) {

                                Log.d("InsertOrUpdate", "success");
                            } else {
                                Log.d("InsertOrUpdate", "Failed");
                            }

                        }
                    }
                }
                //----------------------------------------------------------------------------
            }
            productList = new PreProductController(getActivity()).getAllItems("");//rashmi -2018-10-26
            return productList;
        }


        @Override
        protected void onPostExecute(ArrayList<PreProduct> products) {
            super.onPostExecute(products);

            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            ProductDialogBox();
        }
    }

    public void mToggleTextbox()
    {
        showData();
    }

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

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            OrderDetailFragment.this.mToggleTextbox();
        }
    }

    public void showData() {

        try {

//            if (new OrderDetailController(getActivity()).isAnyActiveOrders())
//            {
//                lv_order_det.setAdapter(null);
//                orderList = new OrderDetailController(getActivity()).getAllActiveOrders();
//                //ArrayList<OrderDetail> freeList = new OrderDetailController(getActivity()).getSAForFreeIssueCalc(mainActivity.selectedPreHed.getORDER_REFNO());
//                lv_order_det.setAdapter(new OrderDetailsAdapter(getActivity(), orderList));//2019-07-07 till error free
//                //lvFree.setAdapter(new PreSalesFreeItemAdapter(getActivity(), freeList));
//            }
//            else
//            {
                lv_order_det.setAdapter(null);
                orderList = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
                //ArrayList<OrderDetail> freeList = new OrderDetailController(getActivity()).getSAForFreeIssueCalc(mainActivity.selectedPreHed.getORDER_REFNO());
                lv_order_det.setAdapter(new OrderDetailsAdapter(getActivity(), orderList));//2019-07-07 till error free
                //lvFree.setAdapter(new PreSalesFreeItemAdapter(getActivity(), freeList));
            //}

        } catch (NullPointerException e) {
            Log.v("SA Error", e.toString());
        }
    }

    public void ProductDialogBox() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.product_dialog_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final ListView lvProducts = (ListView) promptView.findViewById(R.id.lv_product_list);
        final SearchView search = (SearchView) promptView.findViewById(R.id.et_search);

        lvProducts.clearTextFilter();
        productList = new PreProductController(getActivity()).getAllItems("");
        lvProducts.setAdapter(new PreOrderAdapter(getActivity(), productList));

        alertDialogBuilder.setCancelable(false).setNegativeButton("DONE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                selectedItemList = new PreProductController(getActivity()).getSelectedItems();
                updateOrderDet(selectedItemList);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                productList = new PreProductController(getActivity()).getAllItems(query);//Rashmi 2018-10-26
                lvProducts.setAdapter(new PreOrderAdapter(getActivity(), productList));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                productList.clear();
                productList = new PreProductController(getActivity()).getAllItems(newText);//rashmi-2018-10-26
                lvProducts.setAdapter(new PreOrderAdapter(getActivity(), productList));
                return true;
            }
        });
    }

    public void updateOrderDet(final ArrayList<PreProduct> list) {


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
//                pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
//                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//                pDialog.setTitleText("Updating products...");
//                pDialog.setCancelable(false);
//                pDialog.show();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                int i = 0;
                RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
                new OrderDetailController(getActivity()).deleteRecords(RefNo);

                for (PreProduct product : list) {
                    i++;
                    mUpdatePrsSales("0",product.getPREPRODUCT_ITEMCODE(), product.getPREPRODUCT_QTY(), product.getPREPRODUCT_PRICE(), i + "", product.getPREPRODUCT_QOH());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                if(pDialog.isShowing()){
//                    pDialog.dismiss();
//                }

                showData();
            }

        }.execute();
    }

    public void newDeleteOrderDialog(final int position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Confirm Deletion !");
        alertDialogBuilder.setMessage("Do you want to delete this item ?");
        alertDialogBuilder.setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                new PreProductController(getActivity()).updateProductQty(orderList.get(position).getFORDERDET_ITEMCODE(), "0");
                new OrderDetailController(getActivity()).mDeleteRecords(RefNo, orderList.get(position).getFORDERDET_ITEMCODE());
                android.widget.Toast.makeText(getActivity(), "Deleted successfully!", android.widget.Toast.LENGTH_SHORT).show();
                showData();

            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    public void mUpdatePrsSales(String id, String itemCode, String Qty, String price, String seqno, String qoh)
    {
        OrderDetail SODet = new OrderDetail();
        ArrayList<OrderDetail> SOList = new ArrayList<OrderDetail>();

        String reverseUnitPrice = "";
        double amt = 0.0;
        reverseUnitPrice = new TaxDetController(getActivity()).calculateReverseTaxFromDebTax(mSharedPref.getSelectedDebCode(),itemCode, new BigDecimal(price));
        amt = Double.parseDouble(reverseUnitPrice) * Double.parseDouble(Qty);
        String TaxedAmt = "0.00";

        SODet.setFORDERDET_AMT(String.valueOf(amt));
        SODet.setFORDERDET_ITEMCODE(itemCode);
        SODet.setFORDERDET_PRILCODE(mSharedPref.getSelectedDebtorPrilCode());
        SODet.setFORDERDET_QTY(Qty);
        SODet.setFORDERDET_REFNO(RefNo);
        SODet.setFORDERDET_PRICE("0.00");
        SODet.setFORDERDET_IS_ACTIVE("1");
        SODet.setFORDERDET_BALQTY(Qty);
        SODet.setFORDERDET_BAMT(String.valueOf(amt));
        SODet.setFORDERDET_BDISAMT("0");
        SODet.setFORDERDET_BPDISAMT("0");
        SODet.setFORDERDET_BTAXAMT(TaxedAmt);
        SODet.setFORDERDET_TAXAMT(TaxedAmt);
        SODet.setFORDERDET_DISAMT("0");
        SODet.setFORDERDET_SCHDISPER("0");
        //SODet.setFORDERDET_COMP_DISPER(new ControlDS(getActivity()).getCompanyDisc() + "");
        SODet.setFORDERDET_BRAND_DISPER("0");
        SODet.setFORDERDET_BRAND_DISC("0");
        SODet.setFORDERDET_COMP_DISC("0");
        SODet.setFORDERDET_COSTPRICE(new ItemController(getActivity()).getCostPriceItemCode(itemCode));
        SODet.setFORDERDET_PICE_QTY(Qty);
        SODet.setFORDERDET_SELLPRICE(String.valueOf((amt ) / Double.parseDouble(SODet.getFORDERDET_QTY())));
        SODet.setFORDERDET_BSELLPRICE(String.valueOf((amt ) / Double.parseDouble(SODet.getFORDERDET_QTY())));
        SODet.setFORDERDET_SEQNO(new OrderDetailController(getActivity()).getLastSequnenceNo(RefNo));
        SODet.setFORDERDET_TAXCOMCODE(new ItemController(getActivity()).getTaxComCodeByItemCodeBeforeDebTax(itemCode, mSharedPref.getSelectedDebCode()));
        SODet.setFORDERDET_BTSELLPRICE(String.valueOf((amt + Double.parseDouble(TaxedAmt)) / Double.parseDouble(SODet.getFORDERDET_QTY())));
        SODet.setFORDERDET_TSELLPRICE(String.valueOf((amt + Double.parseDouble(TaxedAmt)) / Double.parseDouble(SODet.getFORDERDET_QTY())));
        SODet.setFORDERDET_TXNTYPE("21");
        SODet.setFORDERDET_LOCCODE(new SalRepController(getActivity()).getCurrentLocCode());
        SODet.setFORDERDET_TXNDATE(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        SODet.setFORDERDET_RECORDID("");
        SODet.setFORDERDET_PDISAMT("0");
        SODet.setFORDERDET_IS_SYNCED("0");
        SODet.setFORDERDET_QOH(qoh);
        SODet.setFORDERDET_TYPE("SA");
        SODet.setFORDERDET_SCHDISC("0");
        SODet.setFORDERDET_DISCTYPE("");
        SODet.setFORDERDET_QTY_SLAB_DISC("0");
        SODet.setFORDERDET_ORG_PRICE(price);
        SODet.setFORDERDET_DISFLAG("0");
        SOList.add(SODet);

        if (new OrderDetailController(getActivity()).createOrUpdateOrdDet(SOList)>0)
        {
            Log.d("ORDER_DETAILS", "Order det saved successfully...");
        }
        else
        {
            Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
        }
    }
}
