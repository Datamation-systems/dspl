package com.datamation.sfa.vansale;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.datamation.sfa.R;
import com.datamation.sfa.dialog.CustomKeypadDialog;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.InvDet;
import com.datamation.sfa.model.InvHed;
import com.datamation.sfa.model.Item;
import com.datamation.sfa.model.ItemNew;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    private static class ViewHolder {

        TextView categoryIndicator;

        ImageView icon;
        RelativeLayout rl;
        TextView name, packaging, displayPrice, /*qtySuggested, */
                qtyAvailable, price, qtySelected, flavour;
        TextView freeQty;
        ImageView plus;
        TextView freeIssueOverflow;

        // Flavoured layout additional views
        RelativeLayout rl2;
        ImageView icon2;
        TextView /*qtySuggested2, */qtyAvailable2, price2, qtySelected2, flavour2, details2, displayPrice2;
        TextView freeQty2;
        ImageView plus2;
        TextView freeIssueOverflow2;

        RelativeLayout rl3;
        ImageView icon3;
        TextView /*qtySuggested3, */qtyAvailable3, price3, qtySelected3, flavour3, details3, displayPrice3;
        TextView freeQty3;
        ImageView plus3;
        TextView freeIssueOverflow3;

        RelativeLayout rl4;
        ImageView icon4;
        TextView /*qtySuggested4, */qtyAvailable4, price4, qtySelected4, flavour4, details4, displayPrice4;
        TextView freeQty4;
        ImageView plus4;
        TextView freeIssueOverflow4;

        RelativeLayout rl5;
        ImageView icon5;
        TextView /*qtySuggested5,*/ qtyAvailable5, price5, qtySelected5, flavour5, details5, displayPrice5;
        TextView freeQty5;
        ImageView plus5;
        TextView freeIssueOverflow5;
    }

    private static class ItemsAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        private List<ItemNew> allItems;
        private int mode = 0; // 0 : View All, 1 : View Categorized, 2 : Line Items, 3 : Return Items
        private int categoryId = 0;
        private boolean adding = true;
//        private int discountItemCount = 0;

        private int lastCategoryId = 0;

        public ItemsAdapter(Context context, List<ItemNew> allItems) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.allItems = allItems;



        }

        @Override
        public int getCount() {
            if (allItems != null) {
                return allItems.size();
            }
            return 0;
        }

        @Override
        public ItemNew getItem(int position) {
            return allItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // Think of a better name for this shit.
        public void setIsAdding(boolean isAdding) {
            adding = isAdding;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            final ItemNew item = allItems.get(position);


            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_with_flavour, parent, false);

                viewHolder.categoryIndicator = (TextView) convertView.findViewById(R.id.item_order_tv_category_indicator);

                viewHolder.rl = (RelativeLayout) convertView.findViewById(R.id.item_order_rl1);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.item_order_imageview_icon);
                viewHolder.name = (TextView) convertView.findViewById(R.id.item_order_tv_name);
                viewHolder.packaging = (TextView) convertView.findViewById(R.id.item_order_tv_additional_details);
                viewHolder.qtyAvailable = (TextView) convertView.findViewById(R.id.item_order_tv_available_qty);
                viewHolder.price = (TextView) convertView.findViewById(R.id.item_order_tv_selected_price);
                viewHolder.qtySelected = (TextView) convertView.findViewById(R.id.item_order_tv_selected_qty);
                viewHolder.plus = (ImageView) convertView.findViewById(R.id.item_order_plus_imageview);
                viewHolder.flavour = (TextView) convertView.findViewById(R.id.item_order_tv_flavour);
                viewHolder.displayPrice = (TextView) convertView.findViewById(R.id.item_order_tv_display_price);
                viewHolder.freeQty = (TextView) convertView.findViewById(R.id.item_order_tv_free_qty);
                viewHolder.freeIssueOverflow = (TextView) convertView.findViewById(R.id.item_order_tv_free_overflow);

                viewHolder.rl2 = (RelativeLayout) convertView.findViewById(R.id.item_order_rl2);
                viewHolder.icon2 = (ImageView) convertView.findViewById(R.id.item_order_imageview_product_image2);
                viewHolder.qtySelected2 = (TextView) convertView.findViewById(R.id.item_order_tv_selected_qty2);
                viewHolder.qtyAvailable2 = (TextView) convertView.findViewById(R.id.item_order_tv_available_qty2);
                viewHolder.price2 = (TextView) convertView.findViewById(R.id.item_order_tv_selected_price2);
                viewHolder.flavour2 = (TextView) convertView.findViewById(R.id.item_order_tv_flavour2);
                viewHolder.plus2 = (ImageView) convertView.findViewById(R.id.item_order_plus_imageview2);
                viewHolder.details2 = (TextView) convertView.findViewById(R.id.item_order_tv_additional_details2);
                viewHolder.displayPrice2 = (TextView) convertView.findViewById(R.id.item_order_tv_display_price2);
                viewHolder.freeQty2 = (TextView) convertView.findViewById(R.id.item_order_tv_free_qty2);
                viewHolder.freeIssueOverflow2 = (TextView) convertView.findViewById(R.id.item_order_tv_free_overflow2);

                viewHolder.rl3 = (RelativeLayout) convertView.findViewById(R.id.item_order_rl3);
                viewHolder.icon3 = (ImageView) convertView.findViewById(R.id.item_order_imageview_product_image3);
                viewHolder.qtySelected3 = (TextView) convertView.findViewById(R.id.item_order_tv_selected_qty3);
                viewHolder.qtyAvailable3 = (TextView) convertView.findViewById(R.id.item_order_tv_available_qty3);
                viewHolder.price3 = (TextView) convertView.findViewById(R.id.item_order_tv_selected_price3);
                viewHolder.flavour3 = (TextView) convertView.findViewById(R.id.item_order_tv_flavour3);
                viewHolder.plus3 = (ImageView) convertView.findViewById(R.id.item_order_plus_imageview3);
                viewHolder.details3 = (TextView) convertView.findViewById(R.id.item_order_tv_additional_details3);
                viewHolder.displayPrice3 = (TextView) convertView.findViewById(R.id.item_order_tv_display_price3);
                viewHolder.freeQty3 = (TextView) convertView.findViewById(R.id.item_order_tv_free_qty3);
                viewHolder.freeIssueOverflow3 = (TextView) convertView.findViewById(R.id.item_order_tv_free_overflow3);

                viewHolder.rl4 = (RelativeLayout) convertView.findViewById(R.id.item_order_rl4);
                viewHolder.icon4 = (ImageView) convertView.findViewById(R.id.item_order_imageview_product_image4);
                viewHolder.qtySelected4 = (TextView) convertView.findViewById(R.id.item_order_tv_selected_qty4);
                viewHolder.qtyAvailable4 = (TextView) convertView.findViewById(R.id.item_order_tv_available_qty4);
                viewHolder.price4 = (TextView) convertView.findViewById(R.id.item_order_tv_selected_price4);
                viewHolder.flavour4 = (TextView) convertView.findViewById(R.id.item_order_tv_flavour4);
                viewHolder.plus4 = (ImageView) convertView.findViewById(R.id.item_order_plus_imageview4);
                viewHolder.details4 = (TextView) convertView.findViewById(R.id.item_order_tv_additional_details4);
                viewHolder.displayPrice4 = (TextView) convertView.findViewById(R.id.item_order_tv_display_price4);
                viewHolder.freeQty4 = (TextView) convertView.findViewById(R.id.item_order_tv_free_qty4);
                viewHolder.freeIssueOverflow4 = (TextView) convertView.findViewById(R.id.item_order_tv_free_overflow4);

                viewHolder.rl5 = (RelativeLayout) convertView.findViewById(R.id.item_order_rl5);
                viewHolder.icon5 = (ImageView) convertView.findViewById(R.id.item_order_imageview_product_image5);
                viewHolder.qtySelected5 = (TextView) convertView.findViewById(R.id.item_order_tv_selected_qty5);
                viewHolder.qtyAvailable5 = (TextView) convertView.findViewById(R.id.item_order_tv_available_qty5);
                viewHolder.price5 = (TextView) convertView.findViewById(R.id.item_order_tv_selected_price5);
                viewHolder.flavour5 = (TextView) convertView.findViewById(R.id.item_order_tv_flavour5);
                viewHolder.plus5 = (ImageView) convertView.findViewById(R.id.item_order_plus_imageview5);
                viewHolder.details5 = (TextView) convertView.findViewById(R.id.item_order_tv_additional_details5);
                viewHolder.displayPrice5 = (TextView) convertView.findViewById(R.id.item_order_tv_display_price5);
                viewHolder.freeQty5 = (TextView) convertView.findViewById(R.id.item_order_tv_free_qty5);
                viewHolder.freeIssueOverflow5 = (TextView) convertView.findViewById(R.id.item_order_tv_free_overflow5);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.rl2.setVisibility(View.GONE);
            viewHolder.rl3.setVisibility(View.GONE);
            viewHolder.rl4.setVisibility(View.GONE);
            viewHolder.rl5.setVisibility(View.GONE);




//                Log.d(LOG_TAG, item.toString() + " is not flavoured");

//                Log.d(LOG_TAG, item.toString());

//                if(mode == 0 && item.getCategoryId() != lastCategoryId) {
//                    // Start of a new category
//                    for(ItemCategory category : categories) {
//                        if(item.getCategoryId() == category.getCategoryId()) {
//                            viewHolder.categoryIndicator.setText(category.getCategoryName());
//                            viewHolder.categoryIndicator.setVisibility(View.VISIBLE);
//                            lastCategoryId = category.getCategoryId();
//                        }
//                    }
//                } else {
//                    viewHolder.categoryIndicator.setVisibility(View.GONE);
//                }

//                File image = new File(Environment.getExternalStorageDirectory(), "/Chelcey/Products/" + item.getImageUri());
//                if (image.exists()) {
//                    Uri uri = Uri.fromFile(image);
//                    viewHolder.icon.setVisibility(View.VISIBLE);
//                    Picasso.with(context).load(uri).resize(96, 96).centerInside().into(viewHolder.icon);
//                } else {
//                    viewHolder.icon.setImageDrawable(null);
//                    viewHolder.icon.setVisibility(View.INVISIBLE);
//                }

                viewHolder.freeQty.setVisibility(View.GONE);
//                for (FreeIssueStructure freeIssueStructure : freeIssueStructures) {
//                    if (freeIssueStructure.isItemEligibleForFree(item.getItemNo())) {
//                        int freeQty = freeIssueStructure.getSelectedQtyOfItem(item.getItemNo());
//
//                        if (freeIssueStructure.getBalanceFreeQty() > 0 && freeIssueStructure.getBalanceFreeItemId() == item.getItemNo()) {
//                            // Show overflow view
//                            viewHolder.freeIssueOverflow.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.freeIssueOverflow.setVisibility(View.GONE);
//                        }
//
//                        if (freeQty > 0) {
//                            Log.d(LOG_TAG, item.toString() + " available for free " + freeQty);
//                            viewHolder.freeQty.setText("Free : " + freeQty);
//                            viewHolder.freeQty.setVisibility(View.VISIBLE);
//                        }
//
//                        break;
//                    }
//                }
                NumberFormat format = NumberFormat.getInstance();
                viewHolder.name.setText(item.getItemName() + " (" + format.format(item.getConsumerPrice()) + ")");
                viewHolder.packaging.setText(item.getPackaging());
                viewHolder.displayPrice.setText(format.format(item.getWholesalePrice()) + "/" + item.getUnit());

//                viewHolder.qtySuggested.setText("0");
                viewHolder.qtyAvailable.setText(format.format(item.getStockQty()));

                viewHolder.flavour.setVisibility(View.INVISIBLE);

                if (item.getSelectedQty() == 0) {
                    viewHolder.qtySelected.setText("");
                } else {
                    viewHolder.qtySelected.setText(String.valueOf(item.getSelectedQty()));
                }

                viewHolder.price.setText(format.format(item.getWholesalePrice() * item.getSelectedQty()));

                if (adding) {
                    viewHolder.plus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_plus));
                } else {
                    viewHolder.plus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_minus));
                }

                viewHolder.qtySelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        CustomKeypadDialog keypad = new CustomKeypadDialog(context) {
//                            @Override
//                            public double onOkClicked() {
//                                double value = super.onOkClicked();
//                                if (value > item.getStockQty()) {
//                                    Toast.makeText(context, "Exceeds available stock", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    item.setSelectedQty(value);
//                                }
//                                calculateHeaderDetails();
//                                notifyDataSetChanged();
//                                return value;
//                            }
//                        };

                        CustomKeypadDialog keypad = new CustomKeypadDialog(context, true, new CustomKeypadDialog.IOnOkClickListener() {
                            @Override
                            public void okClicked(double value) {
                                if (value > item.getStockQty()) {
                                    Toast.makeText(context, "Exceeds available stock", Toast.LENGTH_SHORT).show();
                                } else {
                                    item.setSelectedQty(value);
                                }

                                notifyDataSetChanged();
                            }
                        });

                        keypad.show();

                        keypad.setHeader("SELECT QUANTITY");
                        keypad.loadValue(item.getSelectedQty());

                    }
                });
                // plus button
                viewHolder.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        freeIssueType2DetailList = null;
//                        changed = true;
                        if (adding) {
                            double newQty = item.getSelectedQty() ;
                            if (newQty > item.getStockQty()) {
                                Toast.makeText(context, "Exceeds available stock", Toast.LENGTH_SHORT).show();
                            } else {
                                item.setSelectedQty(newQty);
//                                if(item.getItemNo()==186){
//                                    if(item.getSelectedQty()>12){
//                                        int free = (int) (item.getSelectedQty()/12);
//                                        item.setFreeIssue(free);
//                                    }
//                                }
//                                if(item.getItemNo()==187){
//                                    if(item.getSelectedQty()>12){
//                                        int free = (int) (item.getSelectedQty()/12);
//                                        item.setFreeIssue(free);
//                                    }
//                                }
                                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            }
                        } else {
                            double newQty = item.getSelectedQty() ;
                            if (newQty < 0) {
                                item.setSelectedQty(0);
                            } else {
                                item.setSelectedQty(newQty);
                            }
                        }
                        notifyDataSetChanged();


                        Log.d("", "Item ID : " + item.getItemNo());
                    }
                });



            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {

//            if (mode == 0) {
//                this.categoryId = 0;
//                this.mode = 0;
//                this.itemsOfCategory = null;
//                selectedItems = null;
//            } else

//            else if (mode == 3) {
////                this.itemsOfCategory = null;
////                this.selectedItems = null;
//            } else if (mode == 4) {
//
//            }

            super.notifyDataSetChanged();
        }











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