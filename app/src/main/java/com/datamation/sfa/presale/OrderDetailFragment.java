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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.adapter.FreeIssueAdapter;
import com.datamation.sfa.adapter.OrderDetailsAdapter;
import com.datamation.sfa.adapter.OrderFreeItemAdapter;
import com.datamation.sfa.adapter.PreOrderAdapter;
import com.datamation.sfa.controller.ItemController;
import com.datamation.sfa.controller.ItemPriController;
import com.datamation.sfa.controller.OrdFreeIssueController;
import com.datamation.sfa.controller.OrderDetailController;
import com.datamation.sfa.controller.PreProductController;
import com.datamation.sfa.controller.SalRepController;
import com.datamation.sfa.controller.TaxDetController;
import com.datamation.sfa.helpers.PreSalesResponseListener;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.FreeIssue;
import com.datamation.sfa.model.FreeItemDetails;
import com.datamation.sfa.model.ItemFreeIssue;
import com.datamation.sfa.model.OrdFreeIssue;
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
                calculateFreeIssue(mSharedPref.getSelectedDebCode());
            }
        });

        lv_order_det.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new OrderDetailController(getActivity()).restFreeIssueData(RefNo);
                newDeleteOrderDialog(position);
                return true;
            }
        });

        lv_order_det.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id)
            {
                new OrderDetailController(getActivity()).restFreeIssueData(RefNo);
                new OrdFreeIssueController(getActivity()).ClearFreeIssues(RefNo);
                OrderDetail orderDet=orderList.get(position);
                mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");
                FreeIssue issue = new FreeIssue(getActivity());
                ArrayList<FreeItemDetails> list=issue.getEligibleFreeItemsBySalesItem(orderList.get(position),"");
                popEditDialogBox(orderDet,list);
            }
        });

        return view;
    }

    public void popEditDialogBox(final OrderDetail tranSODet, ArrayList<FreeItemDetails>itemDetailsArrayList) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Enter Quantity");
        alertDialogBuilder.setView(promptView);

        final EditText txtInputBox = (EditText) promptView.findViewById(R.id.txtInputBox);
        final TextView lblQoh = (TextView) promptView.findViewById(R.id.lblQOH);

        final TextView itemName = (TextView) promptView.findViewById(R.id.tv_free_issue_item_name);
        final TextView freeQty = (TextView) promptView.findViewById(R.id.tv_free_qty);

        lblQoh.setText(tranSODet.getFORDERDET_QOH());
        txtInputBox.setText(tranSODet.getFORDERDET_QTY());
        txtInputBox.selectAll();

        if(itemDetailsArrayList==null){
            freeQty.setVisibility(View.GONE);
            itemName.setVisibility(View.GONE);
        }else{
            for(FreeItemDetails itemDetails :itemDetailsArrayList){
                freeQty.setText("Free Quantity : " + itemDetails.getFreeQty());
                itemName.setText("Product : " + new ItemController(getActivity()).getItemNameByCode(itemDetails.getFreeIssueSelectedItem()));
            }
        }


        txtInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (txtInputBox.length() > 0) {
                    int enteredQty = Integer.parseInt(txtInputBox.getText().toString());
                    if (tranSODet.getFORDERDET_QOH()!=null)
                    {
                        int newQty = enteredQty - (Integer.parseInt(tranSODet.getFORDERDET_QOH()));
                        lblQoh.setText(String.valueOf(newQty));
                    }
                    else
                    {
                        lblQoh.setText(String.valueOf(enteredQty));
                    }


                    // commented due to error set text
                    //lblQoh.setText((int) Double.parseDouble(tranSODet.getFORDERDET_QOH()) - enteredQty + "");


                 /*   int enteredQty = Integer.parseInt(txtInputBox.getText().toString());

                    if (enteredQty > Double.parseDouble(tranSODet.getFTRANSODET_QOH())) {
                        Toast.makeText(getActivity(), "Quantity exceeds QOH !", Toast.LENGTH_SHORT).show();
                        txtInputBox.setText("0");
                        txtInputBox.selectAll();
                    } else
                        ;*/
                } else {
                    txtInputBox.setText("0");
                    txtInputBox.selectAll();
                }
            }
        });

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                if (Integer.parseInt(txtInputBox.getText().toString()) > 0) {
                    new PreProductController(getActivity()).updateProductQty(tranSODet.getFORDERDET_ITEMCODE(), txtInputBox.getText().toString());
                    new OrderDetailController(getActivity()).deleteOrdDetByItemCode(tranSODet.getFORDERDET_ITEMCODE(), RefNo);
                    mUpdatePrsSales(tranSODet.getFORDERDET_ID(), tranSODet.getFORDERDET_ITEMCODE(), txtInputBox.getText().toString(), tranSODet.getFORDERDET_PRICE(), tranSODet.getFORDERDET_SEQNO(), tranSODet.getFORDERDET_QOH());

                } else
                    Toast.makeText(getActivity(), "Enter Qty above Zero !", Toast.LENGTH_SHORT).show();

                showData();
            }

        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }

    public void calculateFreeIssue(String debCode) {
        /* GET CURRENT ORDER DETAILS FROM TABLE */
        ArrayList<OrderDetail> dets = new OrderDetailController(getActivity()).getSAForFreeIssueCalc(RefNo);
        /* CLEAR ORDERDET TABLE RECORD IF FREE ITEMS ARE ALREADY ADDED. */
        new OrderDetailController(getActivity()).restFreeIssueData(RefNo);
        /* Clear free issues in OrdFreeIss */
        new OrdFreeIssueController(getActivity()).ClearFreeIssues(RefNo);

        FreeIssue issue = new FreeIssue(getActivity());

        /* Get discounts for assorted items */
        ArrayList<ArrayList<OrderDetail>> metaOrdList = issue.SortDiscount(dets, debCode);

        Log.d("PRE_SALES_ORDER_DETAILS", "LIST_SIZE: " + metaOrdList.size());

        /* Iterate through for discounts for items */
        for (ArrayList<OrderDetail> OrderList : metaOrdList) {

            double totAmt = 0;
            String discPer,discType,discRef;
            double freeVal = Double.parseDouble(OrderList.get(0).getFORDERDET_BAMT());
            if(OrderList.get(0).getFORDERDET_SCHDISPER() != null)
                discPer = OrderList.get(0).getFORDERDET_SCHDISPER();
            else
                discPer = "";
            if(OrderList.get(0).getFORDERDET_DISCTYPE() != null)
                discType = OrderList.get(0).getFORDERDET_DISCTYPE();
            else
                discType = "";
            if(OrderList.get(0).getFORDERDET_DISC_REF() != null)
                discRef = OrderList.get(0).getFORDERDET_DISC_REF();
            else
                discRef = "";


            OrderList.get(0).setFORDERDET_BAMT("0");

            for (OrderDetail det : OrderList)
                totAmt += Double.parseDouble(det.getFORDERDET_BSELLPRICE()) * (Double.parseDouble(det.getFORDERDET_QTY()));
            // commented cue to getFTRANSODET_PRICE() is not set
            //totAmt += Double.parseDouble(det.getFTRANSODET_PRICE()) * (Double.parseDouble(det.getFTRANSODET_QTY()));

            for (OrderDetail det : OrderList) {
                det.setFORDERDET_SCHDISPER(discPer);
                det.setFORDERDET_DISCTYPE(discType);
                det.setFORDERDET_DISC_REF(discRef);

                double disc;
                /*
                 * For value, calculate amount portion & for percentage ,
                 * calculate percentage portion
                 */
                disc = (freeVal / totAmt) * Double.parseDouble(det.getFORDERDET_BSELLPRICE()) * (Double.parseDouble(det.getFORDERDET_QTY()));

                //commented due to
                // disc = (Double.parseDouble(det.getFTRANSODET_AMT()) / 100) * disc not correct

                /* Calculate discount amount from disc percentage portion */
//					if (discType != null)
//                    {
//                        if (discType.equals("P"))
////                            disc = (Double.parseDouble(det.getFTRANSODET_AMT()) / 100) * disc;
//                            disc = (Double.parseDouble(det.getFTRANSODET_AMT()) / 100);
//                    }

                new OrderDetailController(getActivity()).updateDiscount(det, disc, det.getFORDERDET_DISCTYPE());
            }
        }

        // GET ARRAY OF FREE ITEMS BY PASSING IN ORDER DETAILS
        //ArrayList<FreeItemDetails> list = issue.getFreeItemsBySalesItem(dets);
        ArrayList<FreeItemDetails> list = issue.getFreeItemsBySalesItem(dets, "");
        //ArrayList<FreeItemDetails> list = issue.getFreeItemsBySalesItem(dets, new SharedPref(getActivity()).getGlobalVal("preKeyCostCode"));
        // PASS EACH ITEM IN TO DIALOG BOX FOR USER SELECTION
        //       if(count == 1) {
        // Log.v("Click count before loop", ">>>"+count);
        for (FreeItemDetails freeItemDetails : list) {
            freeIssueDialogBox(freeItemDetails);

        }
            Log.v("Click count after loop", ">>>"+count);
//        }

    }
    //------------------------------------------------------------show ---------------Free issue Dailog box--------------------------------------------------------------------------

    private boolean freeIssueDialogBox(final FreeItemDetails itemDetails) {

        final ArrayList<ItemFreeIssue> itemFreeIssues;
        final String FIRefNo = itemDetails.getRefno();
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.free_issues_items_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Free Issue Schemes");
        alertDialogBuilder.setView(promptView);

        final ListView listView = (ListView) promptView.findViewById(R.id.lv_free_issue);
        final TextView itemName = (TextView) promptView.findViewById(R.id.tv_free_issue_item_name);
        final TextView freeQty = (TextView) promptView.findViewById(R.id.tv_free_qty);

        freeQty.setText("Free Quantity : " + itemDetails.getFreeQty());
        itemName.setText("Product : " + new ItemController(getActivity()).getItemNameByCode(itemDetails.getFreeIssueSelectedItem()));

        final ItemController itemsDS = new ItemController(getActivity());
        itemFreeIssues = itemsDS.getAllFreeItemNameByRefno(itemDetails.getRefno());
        listView.setAdapter(new FreeIssueAdapter(getActivity(), itemFreeIssues));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            int avaliableQty = 0;

            @Override
            public void onItemClick(AdapterView<?> parent, View view2, final int position, long id) {

                if (itemDetails.getFreeQty() > 0) {

                    ItemFreeIssue freeIssue = itemFreeIssues.get(position);
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

                    View promptView = layoutInflater.inflate(R.layout.set_free_issue_dialog, null);

                    final TextView leftQty = (TextView) promptView.findViewById(R.id.tv_free_item_left_qty);
                    final EditText enteredQty = (EditText) promptView.findViewById(R.id.et_free_qty);

                    leftQty.setText("Free Items Left = " + itemDetails.getFreeQty());

                    enteredQty.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s) {

                            if (enteredQty.getText().toString().equals("")) {

                                leftQty.setText("Free Items Left = " + itemDetails.getFreeQty());

                            } else {
                                avaliableQty = itemDetails.getFreeQty() - Integer.parseInt(enteredQty.getText().toString());

                                if (avaliableQty < 0) {
                                    enteredQty.setText("");
                                    leftQty.setText("Free Items Left = " + itemDetails.getFreeQty());
                                    Toast.makeText(getActivity(), "You don't have enough sufficient quantity to order", Toast.LENGTH_SHORT).show();
                                } else {
                                    leftQty.setText("Free Items Left = " + avaliableQty);
                                }
                            }
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                    });

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(freeIssue.getItems().getFITEM_ITEM_NAME());
                    alertDialogBuilder.setView(promptView);

                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            itemDetails.setFreeQty(avaliableQty);
                            freeQty.setText("Free Qty you have : " + itemDetails.getFreeQty());

                            itemFreeIssues.get(position).setAlloc(enteredQty.getText().toString());
                            listView.clearTextFilter();
                            listView.setAdapter(new FreeIssueAdapter(getActivity(), itemFreeIssues));
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertD = alertDialogBuilder.create();
                    alertD.show();
                } else {
                    Toast.makeText(getActivity(), "You don't have enough sufficient quantity to order", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                for (ItemFreeIssue itemFreeIssue : itemFreeIssues) {

                    if (Integer.parseInt(itemFreeIssue.getAlloc()) > 0) {

                        seqno++;
                        OrderDetail ordDet = new OrderDetail();
                        OrderDetailController detDS = new OrderDetailController(getActivity());
                        ArrayList<OrderDetail> ordList = new ArrayList<OrderDetail>();

                        ordDet.setFORDERDET_ID("0");
                        ordDet.setFORDERDET_AMT("0");
                        ordDet.setFORDERDET_BALQTY(itemFreeIssue.getAlloc());
                        ordDet.setFORDERDET_BAMT("0");
                        ordDet.setFORDERDET_BDISAMT("0");
                        ordDet.setFORDERDET_BPDISAMT("0");
                        String unitPrice = new ItemPriController(getActivity()).getProductPriceByCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE(), mSharedPref.getSelectedDebtorPrilCode());
                        ordDet.setFORDERDET_BSELLPRICE("0");
                        ordDet.setFORDERDET_BTSELLPRICE("0.00");
                        ordDet.setFORDERDET_DISAMT("0");
                        ordDet.setFORDERDET_SCHDISPER("0");
                        ordDet.setFORDERDET_FREEQTY("0");
                        ordDet.setFORDERDET_ITEMCODE(itemFreeIssue.getItems().getFITEM_ITEM_CODE());
                        ordDet.setFORDERDET_PDISAMT("0");
                        ordDet.setFORDERDET_PRILCODE(mSharedPref.getSelectedDebtorPrilCode());
                        ordDet.setFORDERDET_QTY(itemFreeIssue.getAlloc());
                        ordDet.setFORDERDET_PICE_QTY(itemFreeIssue.getAlloc());
                        ordDet.setFORDERDET_TYPE("FI");
                        ordDet.setFORDERDET_RECORDID("");
                        ordDet.setFORDERDET_REFNO(RefNo);
                        ordDet.setFORDERDET_SELLPRICE("0");
                        ordDet.setFORDERDET_SEQNO(seqno + "");
                        ordDet.setFORDERDET_TAXAMT("0");
                        ordDet.setFORDERDET_TAXCOMCODE(new ItemController(getActivity()).getTaxComCodeByItemCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));
                        //ordDet.setFTRANSODET_TAXCOMCODE(new ItemsDS(getActivity()).getTaxComCodeByItemCodeFromDebTax(itemFreeIssue.getItems().getFITEM_ITEM_CODE(), mainActivity.selectedDebtor.getFDEBTOR_CODE()));
                        ordDet.setFORDERDET_TIMESTAMP_COLUMN("");
                        ordDet.setFORDERDET_TSELLPRICE("0.00");
                        ordDet.setFORDERDET_TXNDATE(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        ordDet.setFORDERDET_TXNTYPE("27");
                        ordDet.setFORDERDET_IS_ACTIVE("1");
                        ordDet.setFORDERDET_LOCCODE(mSharedPref.getGlobalVal("PrekeyLocCode").trim());
                        ordDet.setFORDERDET_COSTPRICE(new ItemController(getActivity()).getCostPriceItemCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));
                        ordDet.setFORDERDET_BTAXAMT("0");
                        ordDet.setFORDERDET_IS_SYNCED("0");
                        ordDet.setFORDERDET_QOH("0");
                        ordDet.setFORDERDET_SCHDISC("0");
                        ordDet.setFORDERDET_BRAND_DISC("0");
                        ordDet.setFORDERDET_BRAND_DISPER("0");
                        ordDet.setFORDERDET_COMP_DISC("0");
                        ordDet.setFORDERDET_COMP_DISPER("0");
                        ordDet.setFORDERDET_DISCTYPE("");
                        ordDet.setFORDERDET_PRICE("0.00");
                        ordDet.setFORDERDET_ORG_PRICE("0");
                        ordDet.setFORDERDET_DISFLAG("0");

                        /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*OrdFreeIssue table update*-*-*-*-*-*-*-*-*-*-*-*-*-*/

                        OrdFreeIssue ordFreeIssue = new OrdFreeIssue();
                        ordFreeIssue.setOrdFreeIssue_ItemCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE());
                        ordFreeIssue.setOrdFreeIssue_Qty(itemFreeIssue.getAlloc());
                        ordFreeIssue.setOrdFreeIssue_RefNo(FIRefNo);
                        ordFreeIssue.setOrdFreeIssue_RefNo1(RefNo);
                        ordFreeIssue.setOrdFreeIssue_TxnDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        new OrdFreeIssueController(getActivity()).UpdateOrderFreeIssue(ordFreeIssue);

                        /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*/

                        ordList.add(ordDet);

                        if (detDS.createOrUpdateOrdDet(ordList) > 0) {
                            Toast.makeText(getActivity(), "Added successfully", Toast.LENGTH_SHORT).show();
                            //showData();

                            lvFree.setAdapter(null);
                            ArrayList<OrderDetail> freeList=new OrderDetailController(getActivity()).getAllFreeIssue(RefNo);
                            lvFree.setAdapter(new OrderFreeItemAdapter(getActivity(), freeList));
                        }
                    }
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();
        return true;
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

        try
        {
            lv_order_det.setAdapter(null);
            orderList = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
            lv_order_det.setAdapter(new OrderDetailsAdapter(getActivity(), orderList, mSharedPref.getSelectedDebCode()));//2019-07-07 till error free

            lvFree.setAdapter(null);
            ArrayList<OrderDetail> freeList=new OrderDetailController(getActivity()).getAllFreeIssue(RefNo);
            lvFree.setAdapter(new OrderFreeItemAdapter(getActivity(), freeList));

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
        productList.clear();
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
