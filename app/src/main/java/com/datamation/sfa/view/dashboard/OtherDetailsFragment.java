package com.datamation.sfa.view.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.controller.DayExpDetController;
import com.datamation.sfa.controller.DayExpHedController;
import com.datamation.sfa.controller.DayNPrdDetController;
import com.datamation.sfa.controller.DayNPrdHedController;
import com.datamation.sfa.controller.OrderController;
import com.datamation.sfa.controller.OrderDetailController;
import com.datamation.sfa.controller.ReasonController;
import com.datamation.sfa.model.DayExpDet;
import com.datamation.sfa.model.DayExpHed;
import com.datamation.sfa.model.DayNPrdDet;
import com.datamation.sfa.model.DayNPrdHed;
import com.datamation.sfa.model.Order;
import com.datamation.sfa.model.OrderDetail;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class OtherDetailsFragment extends Fragment {

    View view;
    private Spinner spnOther;
    ExpandableNPListAdapter listNPAdapter;
    ExpandableListView expListNPView;
    List<DayNPrdHed> listNPDataHeader;
    HashMap<DayNPrdHed, List<DayNPrdDet>> listNPDataChild;
    private NumberFormat numberFormat = NumberFormat.getInstance();

    ExpandableDEListAdapter listDEAdapter;
    List<DayExpHed> listDEDataHeader;
    HashMap<DayExpHed, List<DayExpDet>> listDEDataChild;

    //SwipeRefreshLayout mSwipeRefreshLayout;
    //private LinearLayout noDataLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_other_details, container, false);

        spnOther = (Spinner)view.findViewById(R.id.spnOtherTrans);

        //noDataLayout = (LinearLayout)view.findViewById(R.id.no_item_layout);

        ArrayList<String> otherList = new ArrayList<String>();
        otherList.add("Non Productive");
        otherList.add("Daily Expense");

        final ArrayAdapter<String> otherAdapter = new ArrayAdapter<String>(getActivity(),R.layout.reason_spinner_item, otherList);
        otherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOther.setAdapter(otherAdapter);

        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //  getMenu();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });

        expListNPView = (ExpandableListView) view.findViewById(R.id.lvExp);

        final int[] prevExpandPosition = {-1};
        //Lisview on group expand listner... to close other expanded headers...
        expListNPView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                if (prevExpandPosition[0] >= 0) {
                    expListNPView.collapseGroup(prevExpandPosition[0]);
                }
                prevExpandPosition[0] = i;
            }
        });

        expListNPView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String itemName = listNPDataChild.get(listNPDataHeader.get(groupPosition)).get(childPosition).getNONPRDDET_REASON_CODE();
                Toast.makeText(getActivity(), "You selected : " + itemName, Toast.LENGTH_SHORT).show();
                Log.e("Child", listNPDataChild.get(listNPDataHeader.get(groupPosition)).get(childPosition).getNONPRDDET_REASON_CODE());
                return false;
            }
        });

        spnOther.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position==0)
                {
                    expListNPView.clearTextFilter();
                    prepareNPListData();
                }
                else
                {
                    expListNPView.clearTextFilter();
                    prepareDEListData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return view;
    }

    private void prepareNPListData()
    {
        listNPDataHeader = new DayNPrdHedController(getActivity()).getTodayNPHeds();

        if (listNPDataHeader.size()== 0)
        {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();
            //noDataLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            listNPDataChild = new HashMap<DayNPrdHed, List<DayNPrdDet>>();

            for(DayNPrdHed free : listNPDataHeader)
            {
                listNPDataChild.put(free,new DayNPrdDetController(getActivity()).getTodayNPDets(free.getNONPRDHED_REFNO()));
            }

            listNPAdapter = new ExpandableNPListAdapter(getActivity(), listNPDataHeader, listNPDataChild);
            expListNPView.setAdapter(listNPAdapter);
        }

    }

    private void prepareDEListData()
    {
        listDEDataHeader = new DayExpHedController(getActivity()).getTodayDEHeds();

        if (listDEDataHeader.size()== 0)
        {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();
            //noDataLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            listDEDataChild = new HashMap<DayExpHed, List<DayExpDet>>();

            for(DayExpHed free : listDEDataHeader)
            {
                listDEDataChild.put(free,new DayExpDetController(getActivity()).getTodayDEDets(free.getEXPHED_REFNO()));
            }

            listDEAdapter = new ExpandableDEListAdapter(getActivity(), listDEDataHeader, listDEDataChild);
            expListNPView.setAdapter(listDEAdapter);
        }

    }

    public class ExpandableNPListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<DayNPrdHed> _listDataNPHeader; // header titles
        // child data in format of header title, child title
        private HashMap<DayNPrdHed, List<DayNPrdDet>> _listDataNPChild;

        public ExpandableNPListAdapter(Context _context, List<DayNPrdHed> _listDataNPHeader, HashMap<DayNPrdHed, List<DayNPrdDet>> _listDataNPChild) {
            this._context = _context;
            this._listDataNPHeader = _listDataNPHeader;
            this._listDataNPChild = _listDataNPChild;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataNPChild.get(this._listDataNPHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final DayNPrdDet childText = (DayNPrdDet) getChild(groupPosition, childPosition);

            if (convertView == null)
            {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_items_1, null);
            }

            TextView reason = (TextView) convertView.findViewById(R.id.reason);
            TextView remarks = (TextView) convertView.findViewById(R.id.remark);

            reason.setText(childText.getNONPRDDET_REASON());
            remarks.setText(childText.getNONPRDDET_REMARK());

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataNPChild.get(this._listDataNPHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataNPHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataNPHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent)
        {
            DayNPrdHed headerTitle = (DayNPrdHed)getGroup(groupPosition);

            if (convertView == null)
            {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_1, null);
            }

            TextView lblrefNo = (TextView) convertView.findViewById(R.id.refno);
            TextView lbldebCode = (TextView) convertView.findViewById(R.id.debcode);
            TextView lbldate = (TextView) convertView.findViewById(R.id.date);
            TextView lblreason = (TextView) convertView.findViewById(R.id.total);
            TextView lblstatus = (TextView) convertView.findViewById(R.id.status);

            lblrefNo.setTypeface(null, Typeface.BOLD);
            lblrefNo.setText(headerTitle.getNONPRDHED_REFNO());
            lbldate.setText(headerTitle.getNONPRDHED_TXNDATE());
            lbldebCode.setText(headerTitle.getNONPRDHED_DEBCODE());
            lblstatus.setText(headerTitle.getNONPRDHED_IS_SYNCED());

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

    public class ExpandableDEListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<DayExpHed> _listDataDEHeader; // header titles
        // child data in format of header title, child title
        private HashMap<DayExpHed, List<DayExpDet>> _listDataDEChild;

        public ExpandableDEListAdapter(Context _context, List<DayExpHed> _listDataDEHeader, HashMap<DayExpHed, List<DayExpDet>> _listDataDEChild) {
            this._context = _context;
            this._listDataDEHeader = _listDataDEHeader;
            this._listDataDEChild = _listDataDEChild;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataDEChild.get(this._listDataDEHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final DayExpDet childText = (DayExpDet) getChild(groupPosition, childPosition);

            if (convertView == null)
            {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_items_1, null);
            }

            TextView reason = (TextView) convertView.findViewById(R.id.reason);
            TextView remarks = (TextView) convertView.findViewById(R.id.remark);

            reason.setText(childText.getEXPDET_EXPCODE());
            remarks.setText(childText.getEXPDET_AMOUNT());

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataDEChild.get(this._listDataDEHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataDEHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataDEHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent)
        {
            DayExpHed headerTitle = (DayExpHed)getGroup(groupPosition);

            if (convertView == null)
            {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_1, null);
            }

            TextView lblrefNo = (TextView) convertView.findViewById(R.id.refno);
            TextView lbldebCode = (TextView) convertView.findViewById(R.id.debcode);
            TextView lbldate = (TextView) convertView.findViewById(R.id.date);
            TextView lblreason = (TextView) convertView.findViewById(R.id.total);
            TextView lblstatus = (TextView) convertView.findViewById(R.id.status);

            lblrefNo.setTypeface(null, Typeface.BOLD);
            lblrefNo.setText(headerTitle.getEXPHED_REFNO());
            lbldate.setText(headerTitle.getEXPHED_TXNDATE());
            lbldebCode.setText(headerTitle.getEXPHED_REPCODE());
            lblstatus.setText(headerTitle.getEXPHED_IS_SYNCED());

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
