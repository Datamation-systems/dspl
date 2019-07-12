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
import com.datamation.sfa.controller.ReceiptDetController;
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
    private List<ReceiptDet> pinHolders;
    private TextView tvDate;

    //    private DatabaseHandler dbHandler;
//    private CalendarDatePickerDialog calendarDatePickerDialog;
    private int mYear, mMonth, mDay;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private NumberFormat numberFormat = NumberFormat.getInstance();

    //    private Calendar /*calendarBegin, calendarEnd, */nowCalendar;
    public List<String> category_name = new ArrayList<>();
    public List<String> name_of_dish = new ArrayList<>();
    public List<String> item_code = new ArrayList<>();
    public List<String> rate_of_half = new ArrayList<>();
    public List<String> rate_of_full = new ArrayList<>();
    public List<String> item_status = new ArrayList<>();
    public List<String> category_id = new ArrayList<>();
    public List<String> full = new ArrayList<>();
    public List<String> half = new ArrayList<>();
    String url = "http://203.143.21.121:8080/LankaHDWebServices/LankaHDWebServicesRest.svc/ffreehed/mobile123/lhd"; // Replace with your own url
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<String> listitems;
    HashMap<String, List<String>> listDataChild;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.promotion, container, false);


        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

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
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String itemName = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Toast.makeText(getActivity(), "You selected : " + itemName, Toast.LENGTH_SHORT).show();
                Log.e("Child", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                return false;
            }
        });

        return rootView;
    }
    //https://github.com/Rishijay/Dynamic-Expandable-ListView
//    private void getMenu() {
//        StringRequest request = new StringRequest(DownloadManager.Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                progressDialog.dismiss();
//                try {
//                    JSONObject obj = new JSONObject(s);
//                    Log.e("Json response", " " + obj.toString());
//                    int error = obj.getInt("error_code");
//                    Log.e("error code", " " + error);
//                    if (error == 100) {
//                        JSONArray arr = obj.getJSONArray("category_name");
//                        for (int i = 0; i < arr.length(); i++) {
//                            //retrieving each divisional value
//                            String categoryName = arr.getJSONObject(i).getString("category_name");
//                            String name = arr.getJSONObject(i).getString("name_of_dish");
//                            String itemCode = arr.getJSONObject(i).getString("item_code");
//                            String rateOfHalf = arr.getJSONObject(i).getString("rate_of_half");
//                            String rateOfFull = arr.getJSONObject(i).getString("rate_of_full");
//                            String status = arr.getJSONObject(i).getString("item_status");
//                            String categoryID = arr.getJSONObject(i).getString("category_id");
//                            String fullStatus = arr.getJSONObject(i).getString("full");
//                            String halfStatus = arr.getJSONObject(i).getString("half");
//
//                            //adding values division-wise
//                            category_name.add(categoryName);
//                            name_of_dish.add(name);
//                            item_code.add(itemCode);
//                            rate_of_half.add(rateOfHalf);
//                            rate_of_full.add(rateOfFull);
//                            item_status.add(status);
//                            category_id.add(categoryID);
//                            full.add(fullStatus);
//                            half.add(halfStatus);
//
//                            Log.e("List Showing ", name + " " + status);
//                        }
//                        addrows();
//                        // preparing list data
//                        try {
//                            makeHeaderChildData();
//                        } catch (Exception e) {
//                            Log.e("makeHeaderChildData", "Exception " + e.toString());
//                        }
//
//                        listAdapter = new ExpandableListAdapter(MainActivity.this, listDataHeader, listDataChild);
//                        // setting list adapter
//                        expListView.setAdapter(listAdapter);
//                    } else {
//                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    Log.e("Check", "JSONEXCEPTION " + e.toString());
//                    e.printStackTrace();
//                }
//                Log.e("response", s);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e("error response", "Some error occurred!!" + volleyError);
//                progressDialog.dismiss();
//                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//                alert.setTitle("Error");
//                alert.setMessage("Connection error.! Unable to connect to server.");
//                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        return;
//                    }
//                });
//                alert.show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("rest_id", "001");
//                parameters.put("category_name", "");
//                Log.d("Params", parameters.toString());
//                return parameters;
//            }
//        };
//
//        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
//        rQueue.add(request);
//    }
    public void refresh() {
        //   if (adapter != null) adapter.notifyDataSetChanged();
    }

//    public void showCalendar() {
//        if (calendarDatePickerDialog != null)
//            calendarDatePickerDialog.show(getFragmentManager(), "TAG");
//    }

//    @Override
//    public void onFragmentVisible(DashboardContainerFragment dashboardContainerFragment) {
//        dashboardContainerFragment.currentFragment = this;
//    }

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

    public class ExpandableListAdapter extends BaseExpandableListAdapter implements Filterable {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
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

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_items, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
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
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

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


        @Override
        public Filter getFilter() {
            return null;
        }
    }

}
