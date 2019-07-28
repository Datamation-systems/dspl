package com.datamation.sfa.view.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.controller.OrderController;
import com.datamation.sfa.controller.OrderDetailController;
import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.model.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by TaZ on 4/7/15.
 * Used to show the user the list of invoices.
 */
public class OrderDetailsFragment extends Fragment {

    private static final String LOG_TAG = OrderDetailsFragment.class.getSimpleName();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Order> listDataHeader;
    HashMap<Order, List<OrderDetail>> listDataChild;
    //    private DatabaseHandler dbHandler;
//    private CalendarDatePickerDialog calendarDatePickerDialog;
    private int mYear, mMonth, mDay;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private NumberFormat numberFormat = NumberFormat.getInstance();

    //    private Calendar /*calendarBegin, calendarEnd, */nowCalendar;

    String url = "http://203.143.21.121:8080/LankaHDWebServices/LankaHDWebServicesRest.svc/ffreehed/mobile123/lhd"; // Replace with your own url
    //ExpandableListAdapter listAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.promotion, container, false);


        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

//        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        // Creating database to store menu items
        // database = openOrCreateDatabase("Menu.db", MODE_PRIVATE, null);
        // final String q = "Create Table if not exists List (dishName varchar(50), categoryName varchar(50), categoryID varchar(20), itemCode varchar(20), rateOfHalf varchar(10), rateOfFull varchar(10), itemStatus varchar(20), half varchar(20), full varchar(20))";
        // database.execSQL(q);
        // Get data from JSON
        // getMenu();

        // Swipe down to refresh list
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //  getMenu();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);


        final int[] prevExpandPosition = {-1};
        //Lisview on group expand listner... to close other expanded headers...
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                if (prevExpandPosition[0] >= 0) {
                    expListView.collapseGroup(prevExpandPosition[0]);
                }
                prevExpandPosition[0] = i;
            }
        });


        // Listview on child click listener


        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String itemName = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getFORDERDET_ITEMCODE();
                Toast.makeText(getActivity(), "You selected : " + itemName, Toast.LENGTH_SHORT).show();
                Log.e("Child", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getFORDERDET_ITEMCODE());
                return false;
            }
        });
        return rootView;
    }
    //https://github.com/Rishijay/Dynamic-Expandable-ListView
    private void prepareListData() {
        listDataHeader = new OrderController(getActivity()).getTodayOrders();
        listDataChild = new HashMap<Order, List<OrderDetail>>();

        for(Order free : listDataHeader){
            listDataChild.put(free,new OrderDetailController(getActivity()).getTodayOrderDets(free.getORDER_REFNO()));
        }

    }

    public void refresh() {
        //   if (adapter != null) adapter.notifyDataSetChanged();
    }


    private static class HeaderViewHolder {
        TextView pinLabel;
    }

    private static class ViewHolder {
        TextView tvInvoiceDetails;
        TextView tvGrossAmount;
        TextView tvNetAmount;
        TextView tvOutstandingAmount;
        TextView tvCashPayment;
        TextView tvChequeAmount;
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Order> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<Order, List<OrderDetail>> _listDataChild;

        public ExpandableListAdapter(Context context, List<Order> listDataHeader,
                                     HashMap<Order, List<OrderDetail>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final OrderDetail childText = (OrderDetail) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_items, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText.getFORDERDET_ITEMCODE()+" - "+childText.getFORDERDET_QTY()+" - "+childText.getFORDERDET_AMT());
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            Order headerTitle = (Order) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getORDER_REFNO()+" Customer : ("+headerTitle.getORDER_DEBCODE()+")");

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
