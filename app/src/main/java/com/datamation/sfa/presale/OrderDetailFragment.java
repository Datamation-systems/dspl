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
import com.datamation.sfa.adapter.NewProduct_Adapter;
import com.datamation.sfa.adapter.PreProduct_Adapter;
import com.datamation.sfa.controller.InvDetDS;
import com.datamation.sfa.controller.ProductDS;
import com.datamation.sfa.controller.SalRepController;
import com.datamation.sfa.helpers.PreSalesResponseListener;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.model.PRESALE;
import com.datamation.sfa.model.Product;

import com.datamation.sfa.R;
import com.datamation.sfa.settings.ReferenceNum;
import com.datamation.sfa.view.PreSalesActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderDetailFragment extends Fragment{

    int totPieces = 0;
    int seqno = 0;
    ListView lv_order_det, lvFree;
    ArrayList<Product> productList = null, selectedItemList = null;
    ImageButton ibtProduct, ibtDiscount;
    private  SweetAlertDialog pDialog;
    private static final String TAG = "OrderDetailFragment";
    public View view;
    public SharedPref mSharedPref;
    private ListView lv_pre_order_detlv, lv_pre_Free;
    private  String RefNo, locCoe;
    private  MyReceiver r;
    private PRESALE tmpsoHed=null;  //from re oder creation
    PreSalesResponseListener preSalesResponseListener;
    int count = 0;
    private double totAmt = 0.0;
    private Customer debtor;
    PreSalesActivity mainActivity;


    public OrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sales_management_pre_sales_details_new, container, false);
        seqno = 0;
        totPieces = 0;
        lv_order_det = (ListView) view.findViewById(R.id.lvProducts_Inv);
        lvFree = (ListView) view.findViewById(R.id.lvFreeIssue_Inv);
        ibtDiscount = (ImageButton) view.findViewById(R.id.ibtDisc);
        ibtProduct = (ImageButton) view.findViewById(R.id.ibtProduct);
        mainActivity = (PreSalesActivity) getActivity();
        if(mainActivity.selectedDebtor != null)
            RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.VanNumVal));

        tmpsoHed=new PRESALE();
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
                new InvDetDS(getActivity()).restFreeIssueData(RefNo);
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

    public class LoardingProductFromDB extends AsyncTask<Object, Object, ArrayList<Product>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Fetch Data Please Wait.");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Product> doInBackground(Object... objects) {

            if (new ProductDS(getActivity()).tableHasRecords()) {
                //productList = new ProductDS(getActivity()).getAllItems("");
                productList = new ProductDS(getActivity()).getAllItems("","SA");//rashmi 2018-10-26
            } else {
                new ProductDS(getActivity()).insertIntoProductAsBulk(new SalRepController(getActivity()).getCurrentLocCode().trim(), new SalRepController(getActivity()).getCurrentPriLCode().trim());

                if(tmpsoHed!=null) {

                    ArrayList<OrderDetail> orderDetailArrayList = tmpsoHed.getSoDetArrayList();
                    if (orderDetailArrayList != null) {
                        for (int i = 0; i < orderDetailArrayList.size(); i++) {
                            String tmpItemName = orderDetailArrayList.get(i).getORDDET_ITEMCODE();
                            String tmpQty = orderDetailArrayList.get(i).getORDDET_QTY();
                            //Update Qty in  fProducts_pre table
                            int count = new ProductDS(getActivity()).updateProductQtyfor(tmpItemName, tmpQty);
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
            productList = new ProductDS(getActivity()).getAllItems("","SA");//rashmi -2018-10-26
            return productList;
        }


        @Override
        protected void onPostExecute(ArrayList<Product> products) {
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
//        try {
//            orderList = new InvDetDS(getActivity()).getAllInvDet(mainActivity.selectedInvHed.getFINVHED_REFNO());
//            ArrayList<InvDet> freeList = new InvDetDS(getActivity()).getAllFreeIssue(mainActivity.selectedInvHed.getFINVHED_REFNO());
//            lv_order_det.setAdapter(new InvDetAdapter(getActivity(), orderList));//2019-07-07 till error free
//            lvFree.setAdapter(new FreeItemsAdapter(getActivity(), freeList));
//        } catch (NullPointerException e) {
//            Log.v("SA Error", e.toString());
//        }
    }

    public void ProductDialogBox() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.product_dialog_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final ListView lvProducts = (ListView) promptView.findViewById(R.id.lv_product_list);
        final SearchView search = (SearchView) promptView.findViewById(R.id.et_search);

        lvProducts.clearTextFilter();
        productList = new ProductDS(getActivity()).getAllItems("","SA");
        lvProducts.setAdapter(new PreProduct_Adapter(getActivity(), productList));

        alertDialogBuilder.setCancelable(false).setNegativeButton("DONE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                selectedItemList = new ProductDS(getActivity()).getSelectedItems("SA");
                //updateInvoiceDet(selectedItemList);
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
                productList = new ProductDS(getActivity()).getAllItems(query,"SA");//Rashmi 2018-10-26
                lvProducts.setAdapter(new NewProduct_Adapter(getActivity(), productList));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                productList.clear();
                productList = new ProductDS(getActivity()).getAllItems(newText,"SA");//rashmi-2018-10-26
                lvProducts.setAdapter(new NewProduct_Adapter(getActivity(), productList));
                return true;
            }
        });
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void newDeleteOrderDialog(final int position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Confirm Deletion !");
        alertDialogBuilder.setMessage("Do you want to delete this item ?");
        alertDialogBuilder.setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //new ProductDS(getActivity()).updateProductQty(orderList.get(position).getFINVDET_ITEM_CODE(), "0");
                //new InvDetDS(getActivity()).mDeleteProduct(mainActivity.selectedInvHed.getFINVHED_REFNO(), orderList.get(position).getFINVDET_ITEM_CODE());
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




}
