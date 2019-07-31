package com.datamation.sfa.view.dashboard;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.controller.FreeDebController;
import com.datamation.sfa.controller.FreeHedController;
import com.datamation.sfa.controller.ReceiptDetController;
import com.datamation.sfa.model.FreeDeb;
import com.datamation.sfa.model.FreeHed;
import com.datamation.sfa.model.ReceiptDet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by TaZ on 4/8/15.
 * Used to show the user a list of recorded payments.
 */
public class PromotionDetailsFragment extends Fragment  {

    private static final String LOG_TAG = PromotionDetailsFragment.class.getSimpleName();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<FreeHed> listDataHeader;
    HashMap<FreeHed, List<FreeDeb>> listDataChild;
    //    private DatabaseHandler dbHandler;
//    private CalendarDatePickerDialog calendarDatePickerDialog;
    private int mYear, mMonth, mDay;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private NumberFormat numberFormat = NumberFormat.getInstance();

    //    private Calendar /*calendarBegin, calendarEnd, */nowCalendar;

    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.promotion_new, container, false);


        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

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
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String itemName = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getFFREEDEB_DEB_CODE();
                Toast.makeText(getActivity(), "You selected : " + itemName, Toast.LENGTH_SHORT).show();
                Log.e("Child", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getFFREEDEB_DEB_CODE());
                return false;
            }
        });

        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        return rootView;
    }
    //https://github.com/Rishijay/Dynamic-Expandable-ListView
    private void prepareListData() {
        listDataHeader = new FreeHedController(getActivity()).getFreeIssueHeaders();
        listDataChild = new HashMap<FreeHed, List<FreeDeb>>();

for(FreeHed free : listDataHeader){
    listDataChild.put(free,new FreeDebController(getActivity()).getFreeIssueDebtors(free.getFFREEHED_REFNO()));
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
        private List<FreeHed> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<FreeHed, List<FreeDeb>> _listDataChild;

        public ExpandableListAdapter(Context context, List<FreeHed> listDataHeader,
                                     HashMap<FreeHed, List<FreeDeb>> listChildData) {
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

            final FreeDeb childText = (FreeDeb) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_items, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.refno);

            txtListChild.setText(childText.getFFREEDEB_DEB_CODE());
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
            FreeHed headerTitle = (FreeHed) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_new, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.refno);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getFFREEHED_REFNO()+" From : ("+headerTitle.getFFREEHED_VDATEF()+") , To : (" +
                    ""+headerTitle.getFFREEHED_VDATET()+")");

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
