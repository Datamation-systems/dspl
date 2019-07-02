package com.datamation.sfa.presale;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.datamation.sfa.adapter.OrderAdapter;
import com.datamation.sfa.controller.OrderController;
import com.datamation.sfa.helpers.IResponseListener;
import com.datamation.sfa.R;
import com.datamation.sfa.model.Order;
import com.datamation.sfa.utils.UtilityContainer;

import java.util.ArrayList;


public class OrderMainFragment extends Fragment {

    private Toolbar detail_toolbar;
    private FloatingActionButton fab;
    private FragmentActivity myContext;
    private ListView salesOrderLst;
    private ArrayList<Order> ordHedList;
    View view;
    IResponseListener listener;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.frag_sales_order, container, false);

        //set title
//        getActivity().setTitle("SFA");
//
//        //initializations
//        fab = (FloatingActionButton) view.findViewById(R.id.fab);
//
//        salesOrderLst = (ListView) view.findViewById(R.id.sales_lv);
//        fatchData();
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UtilityContainer.mLoadFragment(new SalesManagementFragment(), getActivity());
////                Fragment promosale = new PromoSaleManagement();
////                getFragmentManager().beginTransaction().replace(
////                        R.id.fragmentContainer, promosale)
////                        .commit();
//            }
//        });
//
//        //DISABLED BACK NAVIGATION
//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.i("", "keyCode: " + keyCode);
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    Toast.makeText(getActivity(), "Back Navigationid is disable", Toast.LENGTH_SHORT).show();
//                    return true;
//                } else if ((keyCode == KeyEvent.KEYCODE_HOME)) {
//
//                    getActivity().finish();
//
//                    return true;
//
//                } else {
//                    return false;
//                }
//            }
//        });
//
//
//        this.salesOrderLst.setEmptyView(view.findViewById(android.R.id.empty));


        return view;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        myContext = (FragmentActivity) activity;
//        super.onAttach(activity);
////        try {
////            listener = (IResponseListener) getActivity();
////        } catch (ClassCastException e) {
////            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
////        }
//    }


    public void fatchData() {

//        salesOrderLst.clearTextFilter();
//        ordHedList = new OrderController(getActivity()).getAllOrders();
//        if(ordHedList.size()==0){
//            this.salesOrderLst.setEmptyView(view.findViewById(android.R.id.empty));
//        }else{
//            salesOrderLst.setAdapter(new OrderAdapter(getActivity(), ordHedList));
//        }

    }

}
