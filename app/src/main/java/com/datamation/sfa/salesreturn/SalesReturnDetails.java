package com.datamation.sfa.salesreturn;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.datamation.sfa.R;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SalesReturnDetails extends Fragment {

    View view;
    Button itemSearch, bAdd, bFreeIssue;
    EditText lblItemName, txtQty, editTotDisc,lblNou;
    TextView lblPrice;
    int totPieces = 0;
    double amount = 0.00, changedPrice = 0.0, price = 0.0;
    double values = 0.00, iQoh;
    // TextView lblNou, lblPrice;
    //Items selectedItem = null;
    //Reason selectedReason = null;
    int seqno = 0, index_id = 0;
    ListView lv_return_det;
    //ArrayList<FInvRDet> returnList;
    //SharedPref mSharedPref;
    boolean hasChanged = false;
    String locCode;
    double brandDisPer;
    Spinner returnType;
    //ArrayList<Items> list = null;
    //ArrayList<Reason> reasonList = null;
    //MainActivity activity;
    SweetAlertDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sales_return_details, container, false);

        seqno = 0;
        totPieces = 0;
//        localSP = getActivity().getSharedPreferences(SETTINGS, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
        //localSP = getActivity().getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);
        itemSearch = (Button) view.findViewById(R.id.btn_item_search);
        //reasonSearch = (Button) view.findViewById(R.id.btn_reason_search);
        bAdd = (Button) view.findViewById(R.id.btn_add);
        bFreeIssue = (Button) view.findViewById(R.id.btn_free);
        lblItemName = (EditText) view.findViewById(R.id.et_item);
        //lblReason = (EditText) view.findViewById(R.id.et_reason);
        //activity = (MainActivity) getActivity();
        lv_return_det = (ListView) view.findViewById(R.id.lv_return_det);

        //RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.VanReturnNumVal));

        editTotDisc = (EditText) view.findViewById(R.id.et_TotalDisc);
        lblNou = (EditText) view.findViewById(R.id.tv_unit);
        lblPrice = (TextView) view.findViewById(R.id.tv_price);
        txtQty = (EditText) view.findViewById(R.id.et_pieces);
        returnType = (Spinner) view.findViewById(R.id.spinner_return_Type);

        // ------------------------------- Load return products ----------------- Nuwan ------------ 28/09/2018 -------------------
        itemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //itemSearch.setEnabled(false);
                //new LoardingReturnProductFromDB().execute();
            }
        });

        // -------------------------------------------------------------------------------------------------------------------------
        //reasonSearch.setOnClickListener(this);
//        bAdd.setOnClickListener(this);
//        bFreeIssue.setOnClickListener(this);
//        lblPrice.setOnClickListener(this);

//            ArrayList<FInvRHed> getReturnHed = new FInvRHedDS(getActivity()).getAllActiveInvrhed();
//
//            if (!getReturnHed.isEmpty()) {
//
//                for (FInvRHed returnHed : getReturnHed) {
//                    activity.selectedReturnHed = returnHed;
//
//                    if (activity.selectedRetDebtor == null) {
//                        DebtorDS debtorDS = new DebtorDS(getActivity());
//                        activity.selectedRetDebtor = debtorDS.getSelectedCustomerByCode(returnHed.getFINVRHED_DEBCODE());
//                    }
//                }
//            }

        ArrayList<String> strList = new ArrayList<String>();
        strList.add("Select Return type to continue ...");
//        strList.add("MR");
//        strList.add("UR");
        strList.add("SA");
        strList.add("FR");

        final ArrayAdapter<String> returnTypeAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.return_spinner_item, strList);
        returnTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        returnType.setAdapter(returnTypeAdapter);
        //FetchData();

        /*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        txtQty.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if ((txtQty.length() > 0)) {
                    totPieces = Integer.parseInt(txtQty.getText().toString());
                    hasChanged = true;
                    amount = Double.parseDouble(txtQty.getText().toString())
                            * Double.parseDouble(lblPrice.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        txtQty.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txtQty.setText("");
            }
        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        lv_return_det.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                FInvRDet returnDet = returnList.get(position);
//                deleteOrderDialog(getActivity(), "Return Details ", returnDet.getFINVRDET_ID());
                return true;
            }
        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        lv_return_det.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
//                FInvRDet returnDet = returnList.get(position);
//                FetchData();
//                bAdd.setText("EDIT");
//                selectedItem = new Items();
//                selectedItem.setFITEM_ITEM_CODE(returnDet.getFINVRDET_ITEMCODE());
//                index_id = Integer.parseInt(returnDet.getFINVRDET_ID());
//                lblItemName.setText(new ItemsDS(getActivity()).getItemNameByCode(returnDet.getFINVRDET_ITEMCODE()));
//                txtQty.setText(returnDet.getFINVRDET_QTY());
////
//                if (returnType.getSelectedItem().toString().equalsIgnoreCase("FR"))
//                {
//                    lblPrice.setText("0.00");
//                }
//                else
//                {
//                    //lblPrice.setText("10000.00");
//                    lblPrice.setText(new ItemPriDS(getActivity()).getProductPriceByCode(selectedItem.getFITEM_ITEM_CODE(), activity.selectedRetDebtor.getFDEBTOR_PRILLCODE()));
//
//                }
//                hasChanged = false;
//                editTotDisc.setText(returnDet.getFINVRDET_DIS_AMT());
//                //lblReason.setText(returnDet.getFINVRDET_RETURN_REASON());
            }
        });
        lblPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CustomKeypadDialogPrice keypadPrice = new CustomKeypadDialogPrice(getActivity(), true, new CustomKeypadDialogPrice.IOnOkClickListener() {
//                    @Override
//                    public void okClicked(double value) {
//                        //price cannot be changed less than gross profit
//                        //changedPrice = price;
//                        //validation removed from return 2019/04/01 - said menaka
//                        // if(minPrice <=value && value <= maxPrice) {
//                        //  save changed price
//                        new FInvRDetDS(getActivity()).updateProductPrice(selectedItem.getFITEM_ITEM_CODE(), String.valueOf(price));
//                        //  value should be set for another variable in preProduct
//                        //  preProduct.setPREPRODUCT_PRICE(String.valueOf(value));
//                        changedPrice = value;
//                        lblPrice.setText(""+changedPrice);
////                            }else{
////                                //changedPrice = price;
////                                Toast.makeText(getActivity(),"Price cannot be change..",Toast.LENGTH_LONG).show();
////                            }
//                    }
//                });
//                keypadPrice.show();
//
//                keypadPrice.setHeader("CHANGE PRICE");
////                if(preProduct.getPREPRODUCT_CHANGED_PRICE().equals("0")){
//                keypadPrice.loadValue(changedPrice);
            }
        });

        return view;
    }
}
