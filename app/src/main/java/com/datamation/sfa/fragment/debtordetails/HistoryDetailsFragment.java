package com.datamation.sfa.fragment.debtordetails;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datamation.sfa.R;
import com.datamation.sfa.adapter.Last3InvoiceAdapter;
import com.datamation.sfa.controller.FInvhedL3DS;
import com.datamation.sfa.dialog.CustomProgressDialog;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.helpers.DateTimeFormatings;
import com.datamation.sfa.helpers.NetworkFunctions;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Batch;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.FInvhedL3;
import com.datamation.sfa.model.FreeIssueDetail;
import com.datamation.sfa.model.HistoryDetail;
import com.datamation.sfa.model.Invoice;
import com.datamation.sfa.model.Last3Invoice;
import com.datamation.sfa.model.Order;
import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.model.ReturnDetail;
import com.datamation.sfa.utils.LocationProvider;
import com.datamation.sfa.utils.TimeUtils;
import com.datamation.sfa.utils.ValueHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoryDetailsFragment extends Fragment
{

    private Last3InvoiceAdapter l3Adapter;
    private RecyclerView rv;
    private TextView tvInvoice1,tvInvoice2,tvInvoice3;
    SharedPref pref;
    NetworkFunctions networkFunctions;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_last_three_invoices, container, false);

        rv = view.findViewById(R.id.rv_report);

        tvInvoice1 = view.findViewById(R.id.tvInvoice1);
        tvInvoice2 = view.findViewById(R.id.tvInvoice2);
        tvInvoice3 = view.findViewById(R.id.tvInvoice3);

        rv.setNestedScrollingEnabled(false);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Last3Invoice> last3InvList =  new FInvhedL3DS(getActivity()).getLast3InvoiceDetails();
        ArrayList<FInvhedL3> FInvhedL3ArrayList = new FInvhedL3DS(getActivity()).getLast3InvoiceHed();

        for (int i = 0; i < FInvhedL3ArrayList.size(); i++) {
            if(i==0)
                tvInvoice1.setText(FInvhedL3ArrayList.get(i).getFINVHEDL3_REF_NO1()+" - "+new DateTimeFormatings().getDateFormat(FInvhedL3ArrayList.get(i).getFINVHEDL3_TXN_DATE()));
            else if(i==1)
                tvInvoice2.setText(FInvhedL3ArrayList.get(i).getFINVHEDL3_REF_NO1()+" - "+new DateTimeFormatings().getDateFormat(FInvhedL3ArrayList.get(i).getFINVHEDL3_TXN_DATE()));
            else if(i==2)
                tvInvoice3.setText(FInvhedL3ArrayList.get(i).getFINVHEDL3_REF_NO1()+" - "+new DateTimeFormatings().getDateFormat(FInvhedL3ArrayList.get(i).getFINVHEDL3_TXN_DATE()));
        }

        l3Adapter = new Last3InvoiceAdapter(last3InvList);
        rv.setAdapter(l3Adapter);

        return view;
    }

}
