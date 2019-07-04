package com.datamation.sfa.presale;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.datamation.sfa.R;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.FInvRDet;

import java.util.ArrayList;


public class OrderReturnFragment extends Fragment {

    View view;
    public static final String SETTINGS = "SETTINGS";
//    public static SharedPreferences localSP;
//    public String RefNo = "";
    Button itemSearch, bAdd, bFreeIssue, reasonSearch;
    EditText lblItemName, txtQty, editTotDisc, lblReason,lblNou;
    TextView lblPrice;
    int totPieces = 0;
    double amount = 0.00,price = 0.00,minPrice = 0.00, maxPrice = 0.00, changedPrice = 0.0;
    double values = 0.00, iQoh;
    //TextView lblNou, lblPrice;
//    Items selectedItem = null;
//    Reason selectedReason = null;
    int seqno = 0, index_id = 0;
    ListView lv_return_det;
    //ArrayList<FInvRDet> returnList;
    //SharedPref mSharedPref;
    boolean hasChanged = false;
    String locCode;
    double brandDisPer;
    Spinner returnType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_return, container, false);

        seqno = 0;
        totPieces = 0;
        //localSP = getActivity().getSharedPreferences(SETTINGS, 0);
        itemSearch = (Button) view.findViewById(R.id.btn_item_search);
        reasonSearch = (Button) view.findViewById(R.id.btn_reason_search);
        bAdd = (Button) view.findViewById(R.id.btn_add);
        bFreeIssue = (Button) view.findViewById(R.id.btn_free);
        lblItemName = (EditText) view.findViewById(R.id.et_item);
        lblReason = (EditText) view.findViewById(R.id.et_reason);

        lv_return_det = (ListView) view.findViewById(R.id.lv_return_det);

        editTotDisc = (EditText) view.findViewById(R.id.et_TotalDisc);
        lblNou = (EditText) view.findViewById(R.id.et_pieces);
        lblPrice = (TextView) view.findViewById(R.id.tv_price);
        txtQty = (EditText) view.findViewById(R.id.tv_unit);
        returnType = (Spinner) view.findViewById(R.id.spinner_return_Type);
//        itemSearch.setOnClickListener(this);
//        reasonSearch.setOnClickListener(this);
//        bAdd.setOnClickListener(this);
//        bFreeIssue.setOnClickListener(this);

        ArrayList<String> strList = new ArrayList<String>();
        strList.add("Select Return type to continue...");
        strList.add("MR-MARKET RETURN");
        strList.add("UR-USABLE RETURN");
        strList.add("RP-REPLACEMENT");

//        txtQty.addTextChangedListener(new TextWatcher() {
//
////            public void afterTextChanged(Editable s) {
////
////                if(! txtQty.getText().equals(null))
////                {
////                    if ((txtQty.length() > 0))
////                    {
////                        totPieces = Integer.parseInt(txtQty.getText().toString());
////                        hasChanged = true;
////                        amount = Double.parseDouble(txtQty.getText().toString())
////                                * Double.parseDouble(lblPrice.getText().toString());
////                    }
////                }
////
////            }
////
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////            }
////
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////            }
//
//        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//        txtQty.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                txtQty.setText("");
//            }
//        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//        lv_return_det.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
////            @Override
////            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//////                FInvRDet returnDet = returnList.get(position);
////////                deleteOrderDialog(getActivity(), "Return Details ", returnDet.getFINVRDET_ITEMCODE(),RefNo);
//////                return true;
////            }
//        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//        lv_return_det.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
////                FInvRDet returnDet = returnList.get(position);
////                //               FetchData();
////                bAdd.setText("EDIT");
//////                selectedItem = new Items();
//////                selectedItem.setFITEM_ITEM_CODE(returnDet.getFINVRDET_ITEMCODE());
//////                index_id = Integer.parseInt(returnDet.getFINVRDET_ID());
//////                lblItemName.setText(new ItemsDS(getActivity()).getItemNameByCode(returnDet.getFINVRDET_ITEMCODE()));
//////                txtQty.setText(returnDet.getFINVRDET_QTY());
//////                lblPrice.setText(new ItemPriDS(getActivity()).getProductPriceByCode(selectedItem.getFITEM_ITEM_CODE(), activity.selectedRetDebtor.getFDEBTOR_PRILLCODE()));
////                hasChanged = false;
////                editTotDisc.setText(returnDet.getFINVRDET_DIS_AMT());
////                lblReason.setText(returnDet.getFINVRDET_RETURN_REASON());
//            }
//        });

        return view;
    }

//    @Override
//    public void onClick(View view)
//    {
//        switch (view.getId()) {
//            case R.id.btn_item_search:
////                clearTextFields();
//                //prodcutDetailsDialogbox();
////                if(activity.selectedDebtor != null)
////                {
////                    new LoardingItemsFromDB().execute();
////                }
////                else
////                {
////                    Toast.makeText(getActivity(), "Select a customer to continue...", Toast.LENGTH_SHORT).show();
////                }
//
//                break;
//            case R.id.btn_reason_search:
////                reasonsDialogbox();
//                break;
//            case R.id.btn_add: {
////                if (!(lblItemName.getText().toString().equals("")) && !((lblReason.getText().toString().equals(""))) && !(returnType.getSelectedItem().toString().contains("Select Return"))) {
//////                    Log.v("ITEM NAME>>>>",lblItemName.getText().toString());
//////                    Log.v("ITEM REASON>>>>",lblReason.getText().toString());
////                    if(TextUtils.isEmpty(txtQty.getText()))
////                    {
////                        txtQty.setText("0");
////                    }
////                    if (Integer.parseInt(txtQty.getText().toString()) > 0) {
//////                        Log.v("TOTAL PEIACES>>>>",totPieces+"");
////                        FInvRDet ReturnDet = new FInvRDet();
////                        ArrayList<FInvRDet> ReturnList = new ArrayList<FInvRDet>();
//
////                        ArrayList<FInvRHed> returnHedList = new ArrayList<FInvRHed>();
////
////                        String TaxedAmt = new TaxDetDS(getActivity()).calculateTax(selectedItem.getFITEM_ITEM_CODE(),
////                                new BigDecimal(amount - Double.parseDouble(editTotDisc.getText().toString())));
////                        FInvRHed hed = new FInvRHed();
////
////                        hed.setFINVRHED_REFNO(RefNo);
////                        hed.setFINVRHED_MANUREF(activity.selectedInvHed.getFINVHED_MANUREF());
////                        hed.setFINVRHED_INV_REFNO(activity.selectedInvHed.getFINVHED_REFNO());
////                        hed.setFINVRHED_REMARKS(activity.selectedInvHed.getFINVHED_REMARKS());
////                        hed.setFINVRHED_ADD_USER(new SalRepDS(getActivity()).getCurrentRepCode());
////                        hed.setFINVRHED_ADD_DATE(currentTime());
////                        hed.setFINVRHED_ADD_MACH(localSP.getString("MAC_Address", "No MAC Address").toString());
////                        hed.setFINVRHED_TXNTYPE("24");
////                        hed.setFINVRHED_TXN_DATE(activity.selectedInvHed.getFINVHED_TXNDATE());
////                        hed.setFINVRHED_IS_ACTIVE("1");
////                        hed.setFINVRHED_IS_SYNCED("0");
////
////                        if (activity.selectedDebtor != null) {
////                            hed.setFINVRHED_DEBCODE(activity.selectedDebtor.getFDEBTOR_CODE());
////                            hed.setFINVRHED_TAX_REG(activity.selectedDebtor.getFDEBTOR_TAX_REG());
////                        }
////
////                        hed.setFINVRHED_LOCCODE(new SalRepDS(getActivity()).getCurrentLocCode());
////                        hed.setFINVRHED_ROUTE_CODE(new SharedPref(getActivity()).getGlobalVal("KeyRouteCode"));
////                        hed.setFINVRHED_COSTCODE("");
////
////                        activity.selectedReturnHed = hed;
////                        SharedPreferencesClass.setLocalSharedPreference(activity, "Return_Start_Time", currentTime());
////
////                        returnHedList.add(activity.selectedReturnHed);
//////                        Log.v("RETURN HED>>>>",activity.selectedInvHed.toString());
////                        if (new FInvRHedDS(getActivity()).createOrUpdateInvRHed(returnHedList) > 0) {
//////                            Log.v("START DET SAVE>>>>",">>>>>>");
////                            seqno++;
////                            ReturnDet.setFINVRDET_ID(index_id + "");
////                            ReturnDet.setFINVRDET_SEQNO(seqno + "");
////                            ReturnDet.setFINVRDET_COST_PRICE(lblPrice.getText().toString());
////                            ReturnDet.setFINVRDET_SELL_PRICE(""+price);
////                            ReturnDet.setFINVRDET_T_SELL_PRICE(""+price);
////                            ReturnDet.setFINVRDET_DIS_AMT(editTotDisc.getText().toString());
////                            ReturnDet.setFINVRDET_AMT(
////                                    String.format("%.2f", Double.parseDouble(txtQty.getText().toString())
////                                            * changedPrice));
////
////                            ReturnDet.setFINVRDET_TAX_AMT(TaxedAmt);
////                            ReturnDet.setFINVRDET_QTY(totPieces + "");
////                            ReturnDet.setFINVRDET_BAL_QTY(totPieces + "");
////                            ReturnDet.setFINVRDET_RETURN_REASON(lblReason.getText().toString());
////                            ReturnDet.setFINVRDET_RETURN_REASON_CODE(new ReasonDS(getActivity()).getReaCodeByName(lblReason.getText().toString()));
////                            ReturnDet.setFINVRDET_REFNO(RefNo);
////                            ReturnDet.setFINVRDET_ITEMCODE(selectedItem.getFITEM_ITEM_CODE());
////                            ReturnDet.setFINVRDET_PRILCODE("");
////                            ReturnDet.setFINVRDET_IS_ACTIVE("1");
////                            ReturnDet.setFINVRDET_TAXCOMCODE(
////                                    new ItemsDS(getActivity()).getTaxComCodeByItemCode(selectedItem.getFITEM_ITEM_CODE()));
////                            ReturnDet.setFINVRDET_TXN_DATE(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
////                            ReturnDet.setFINVRDET_TXN_TYPE("SR");
////                            ReturnDet.setFINVRDET_RETURN_TYPE(returnType.getSelectedItem().toString().split("-")[0]);
////                            ReturnDet.setFINVRDET_CHANGED_PRICE(""+changedPrice);
////
////                            ReturnList.add(ReturnDet);
//////                            Log.v("RETURNDET>>>>",ReturnList.toString());
////                            //	if (!(bAdd.getText().equals("EDIT") && hasChanged == false)) {
////                            new FInvRDetDS(getActivity()).createOrUpdateInvRDet(ReturnList);
////                            //	}
//////                            Log.v("FINISH DET SAVE>>>>",">>>>>>");
////                            if (bAdd.getText().equals("EDIT"))
////                                Toast.makeText(getActivity(), "Edited successfully !", Toast.LENGTH_LONG).show();
////                            else
////                                Toast.makeText(getActivity(), "Added successfully !", Toast.LENGTH_LONG).show();
////                            FetchData();
////                            clearTextFields();
////
////                        }
////                    }else{
////                        Toast.makeText(getActivity(), "Please add quantities ", Toast.LENGTH_LONG).show();
////                    }
////                }else{
////                    Toast.makeText(getActivity(), "Please fill details ", Toast.LENGTH_LONG).show();
////                }
////            }
////            break;
////            default:
////                break;
//            }
//        }
//    }
}
