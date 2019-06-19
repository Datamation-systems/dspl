package com.datamation.sfa.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.adapter.CustomerAdapter;
import com.datamation.sfa.controller.CustomerController;
import com.datamation.sfa.controller.ItemController;
import com.datamation.sfa.controller.ItemPriceController;
import com.datamation.sfa.controller.ReasonController;
import com.datamation.sfa.controller.ReferenceDetailDownloader;
import com.datamation.sfa.controller.ReferenceSettingController;
import com.datamation.sfa.controller.RouteController;
import com.datamation.sfa.dialog.CustomProgressDialog;
import com.datamation.sfa.helpers.IResponseListener;
import com.datamation.sfa.helpers.NetworkFunctions;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.Item;
import com.datamation.sfa.model.ItemPri;
import com.datamation.sfa.model.Reason;
import com.datamation.sfa.model.RefSetting;
import com.datamation.sfa.model.ReferenceDetail;
import com.datamation.sfa.model.Route;
import com.datamation.sfa.settings.GPSTracker;
import com.datamation.sfa.utils.NetworkUtil;
import com.datamation.sfa.utils.UtilityContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FragmentSecondary extends Fragment {

    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    View view;
    ListView lvCustomers;
    CustomerAdapter customerAdapter;
    SharedPref mSharedPref;
    IResponseListener listener;
    TextView txtCusName, btnTourCus, btnAllCus;
    ImageButton btnCust,btnSearch;
    SweetAlertDialog pDialog;
    Spinner spnTour;
    String routeCode="";
    String tourRefNo;
    String areaCode;
    String locCode;
    String routeName="";
    GPSTracker gpsTracker;
    boolean isTourDebtor = true;
    private static final String TAG = "PreSalesCustomer";
    Activity mactivity;
    ArrayList<Customer> customerList;
    private NetworkFunctions networkFunctions;
    private Customer debtor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_secondary, container, false);
        mSharedPref = new SharedPref(getActivity());
        lvCustomers = (ListView) view.findViewById(R.id.cus_lv);
        gpsTracker = new GPSTracker(getActivity());
        localSP = getActivity().getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);

        btnCust = (ImageButton) view.findViewById(R.id.btnCust);
        txtCusName = (TextView) view.findViewById(R.id.txtSelCust);
        btnTourCus = (TextView) view.findViewById(R.id.btnTourDebtor);
        btnAllCus = (TextView) view.findViewById(R.id.btnAllDebtor);
        btnSearch = (ImageButton) view.findViewById(R.id.btnsearch_cus);
        spnTour = (Spinner) view.findViewById(R.id.spnTour);
        mactivity = getActivity();

        // These two button are only used for pre sales
        btnTourCus.setVisibility(View.VISIBLE);
        btnAllCus.setVisibility(View.VISIBLE);
        btnAllCus.setBackground(getResources().getDrawable(R.drawable.custom_label_disable));
        final boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());

        //tourlist = new TourHedDS(getActivity()).getTourDetails("");

        ArrayList<String> strList = new ArrayList<String>();
        strList.add("Select a Tour to continue ...");

//        for (TourHed hed : tourlist)
//        {
//            strList.add(hed.getTOURHED_REFNO() + " - " + hed.getTOURHED_ID());
//        }

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, strList);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTour.setAdapter(dataAdapter1);

        spnTour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0)
                {
//                    routeCode = tourlist.get(position - 1).getTOURHED_ROUTECODE();
//                    tourRefNo = tourlist.get(position - 1).getTOURHED_REFNO();
//                    areaCode = new TourHedDS(getActivity()).getAreaCode(tourRefNo);

                    Log.d("PRE_SALES_CUSTOMER", "ROUTE_CODE_IS: " + routeCode + ", " + areaCode);

                    lvCustomers.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        btnTourCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnTour.setVisibility(View.VISIBLE);
                btnAllCus.setBackground(getResources().getDrawable(R.drawable.custom_label_disable));
                btnTourCus.setBackground(getResources().getDrawable(R.drawable.custom_label));

                if (connectionStatus == true)
                {
                        try {
                            new getAllCustomer(mSharedPref.getLoginUser().getCode()).execute();

                        } catch (Exception e) {
                            Log.e("## ErrorIn2ndSync ##", e.toString());
                        }

                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                isTourDebtor = true;


            }
        });

        btnAllCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnTour.setVisibility(View.GONE);
                btnTourCus.setBackground(getResources().getDrawable(R.drawable.custom_label_disable));
                btnAllCus.setBackground(getResources().getDrawable(R.drawable.custom_label));

                //to blank the customer list view
                //customerList = new Customer(getActivity()).getRouteCustomersByCodeAndName("", "");
                if (connectionStatus == true)
                {
                    try {
                        //new getAllCustomer(mSharedPref.getLoginUser().getCode()).execute();
                        navigateDebtorDetails();

                    } catch (Exception e) {
                        Log.e("## ErrorIn2ndSync ##", e.toString());
                    }

                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                isTourDebtor = false;
            }
        });

        //-----------------------------------from Reorder Only-----------------------------------------------------------------------------------
        Bundle mBundle = getArguments();

//        if (mBundle != null) {
//            tmpsoHed = (TranSOHed) mBundle.getSerializable("order");
//            if (tmpsoHed != null) {
//                activity.selectedDebtor = new DebtorDS(getActivity()).getSelectedCustomerByCode(tmpsoHed.getFTRANSOHED_DEBCODE());
//                Log.d("<>*Pre sales cus****", "" + tmpsoHed.getFTRANSOHED_REFNO());
//                new SharedPref(getActivity()).setGlobalVal("PrekeyCusCode", activity.selectedDebtor.getFDEBTOR_CODE());
//                mSharedPref.setGlobalVal("PrekeyCustomer", "Y");
//            }
//
//        }
////-----------------------------------------------------------------------------------------------------------------------------------
//        if (activity.selectedSOHed == null) {
//            TranSOHed SOHed = new TranSOHedDS(getActivity()).getActiveSOHed();
//            if (SOHed != null) {
//                if (activity.selectedDebtor == null)
//                {
//                    activity.selectedDebtor = new DebtorDS(getActivity()).getSelectedCustomerByCode(SOHed.getFTRANSOHED_DEBCODE());
//                }
//
//
//            }
//        }

//        if(activity.selectedDebtor!=null)
//            txtCusName.setText(activity.selectedDebtor.getFDEBTOR_NAME());

        /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//        btnCust.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        btnCust.setBackground(getResources().getDrawable(R.drawable.cus_down));
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_UP: {
//                        btnCust.setBackground(getResources().getDrawable(R.drawable.cus));
//                    }
//                    break;
//                }
//                return false;
//            }
//        });

        /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
        btnCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isTourDebtor)
                {
                    if (!spnTour.getSelectedItem().toString().equalsIgnoreCase("Select a Tour to continue ..."))
                    {
                        new AsyncTask<Void, Void, Void>() {

                            protected void onPreExecute() {
                                pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Retrieving customers..");
                                pDialog.setCancelable(false);
                                pDialog.show();
                                super.onPreExecute();
                            }

                            @Override
                            protected Void doInBackground(Void... params) {
                                //customerList = new DebtorDS(getActivity()).getRouteCustomers("", "");
                                //customerList = new Customer(getActivity()).getRouteCustomersByCodeAndName(routeCode, "");
                                customerAdapter = new CustomerAdapter(getActivity(), customerList);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                pDialog.dismiss();
                                //lvCustomers.setAdapter(null);
                                lvCustomers.setAdapter(customerAdapter);
                            }
                        }.execute();
                    }
                    else
                    {
                        tourSelectionDialogBox();
                    }
                }
                else
                {
                    new AsyncTask<Void, Void, Void>() {

                        protected void onPreExecute() {
                            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Retrieving customers..");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            //customerList = new Customer(getActivity()).getRouteCustomers("", "");
                            //customerList = new DebtorDS(getActivity()).getRouteCustomersByCodeAndName(routeCode, "");
                            customerAdapter = new CustomerAdapter(getActivity(), customerList);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            pDialog.dismiss();
                            lvCustomers.setAdapter(customerAdapter);
                        }
                    }.execute();
                }

            }
        });

        /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//        btnSearch.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        btnSearch.setBackground(getResources().getDrawable(R.drawable.search_cus_down));
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_UP: {
//                        btnSearch.setBackground(getResources().getDrawable(R.drawable.search_cus));
//                    }
//                    break;
//                }
//                return false;
//            }
//        });
        /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchDialogBox();

            }
        });
        /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {

                if (!(gpsTracker.canGetLocation())) {
                    gpsTracker.showSettingsAlert();
                }

                /* Check whether automatic date time option checked or not */
                int i = android.provider.Settings.Global.getInt(getActivity().getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
                /* If option is selected */
                if (i > 0) {
                    debtor = customerList.get(position);
                    String status = debtor.getCusStatus();
                    String routeName = debtor.getCusRoute();
                    final String cusCode = debtor.getCusCode();

                    // commented due to less data for NIC and BR for fDebtor .................. Nuwan..... 21.02.2019.......
//                    if (!debtor.getFDEBTOR_NIC().equals("") && !debtor.getFDEBTOR_BIS_REG().equals(""))
//                    {
//                        txtCusName.setText(activity.selectedDebtor.getFDEBTOR_NAME());

                    //Log.d("PRE_SALES", "DEBTOR_CREDIT_DETAILS" + debtor.getFDEBTOR_CRD_LIMIT() + ", " + debtor.getFDEBTOR_CRD_PERIOD());

//                    new SharedPref(getActivity()).setGlobalVal("PrekeyCusCode", debtor.getFDEBTOR_CODE());
//                    routeName = new RouteDS(getActivity()).getRouteNameByCode(debtor.getFDEBTOR_CODE());
//                    new SharedPref(getActivity()).setGlobalVal("PrekeyRouteName", routeName);

                    if (!routeCode.equals("")||!routeName.equals(""))
                    {
                        String limitFlag = debtor.getCusAdd1();
                        String period = debtor.getCusAdd2();
//                        int noOfDays  = new FDDbNoteDS(getActivity()).getOldestFDDBNoteDate(activity.selectedDebtor.getFDEBTOR_CODE());
                        int noOfDays  = 0;
                        int noOfOverDue = noOfDays - Integer.valueOf(period);

                        if (status.equals("A"))
                        {
                            if (limitFlag.equals("Y"))
                            {
                                if (noOfOverDue>0)
                                {
                                    creditDatesExceedDialog(String.valueOf(noOfOverDue));
                                }
                                else{
                                    navigateDebtorDetails();
                                }
                            }else{

                                navigateDebtorDetails();
                            }
                        }
                        else
                        {
                            debtorStatusDialog();
                        }
                    }
                    else
                    {
                        routeValidateDialog();
                    }

                }
                else {
                    Toast.makeText(getActivity(), "Please tick the 'Automatic Date and Time' option to continue..", Toast.LENGTH_LONG).show();
                    /* Show Date/time settings dialog */
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
            }
        });

        /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//        lvCustomers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                debtor = customerList.get(position);
//                CusInfoBox alertBox = new CusInfoBox();
//                Log.d("PRE_SALES_CUSTOMER", "DEBTOR_NAME" + debtor.getFDEBTOR_NAME() + ", " + debtor.getFDEBTOR_CODE());
//                if(debtor!=null)
//                    alertBox.debtorDetailsDialogbox(getActivity(),debtor.getFDEBTOR_CODE(),debtor,true);
//
//                return true;
//            }
//        });

        return view;
    }

    public void creditDatesExceedDialog(String noOfDays) {

        String message = "Selected customer is Over Due with " + noOfDays + " days from the given credit period. Please settle the oldest outstanding.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Over Due Dates");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //UtilityContainer.PreClearSharedPref(getActivity());
                UtilityContainer.mLoadFragment(new FragmentSecondary(), getActivity());

            }
        }).setNegativeButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }

    public void debtorStatusDialog() {

        String message = "Customer is NOT ACTIVE. Can not continue";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Customer Status");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //UtilityContainer.PreClearSharedPref(getActivity());
                UtilityContainer.mLoadFragment(new FragmentSecondary(), getActivity());

            }
        }).setNegativeButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }

    public void routeValidateDialog() {

        String message = "No Route to Selected customer. Can not continue...";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Empty Route ");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //UtilityContainer.PreClearSharedPref(getActivity());
                UtilityContainer.mLoadFragment(new FragmentSecondary(), getActivity());
                //listener.moveBackToCustomer_pre(0);

            }
        }).setNegativeButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }

    public void isValidDebtor() {

        String message = "No NIC and BR to Selected customer. Can not continue...";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Empty NIC / BR ");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //UtilityContainer.PreClearSharedPref(getActivity());
                UtilityContainer.mLoadFragment(new FragmentSecondary(), getActivity());
                //listener.moveBackToCustomer_pre(0);

            }
        }).setNegativeButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }

    public void navigateDebtorDetails() {

        Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
        startActivity(intent);



//        MainActivity activity = (MainActivity) getActivity();
//        debtor = customerList.get(position);
//        activity.selectedDebtor = debtor;
//        Toast.makeText(getActivity(), debtor.getFDEBTOR_NAME() + " selected", Toast.LENGTH_LONG).show();
//        mSharedPref.setGlobalVal("PrekeyCustomer", "Y");
//        mSharedPref.setGlobalVal("preKeyTouRef",tourRefNo);
//        mSharedPref.setGlobalVal("preKeyRouteCode", routeCode);
//        mSharedPref.setGlobalVal("preKeyAreaCode", areaCode);
//        mSharedPref.setGlobalVal("PrekeyRouteName", routeName);
//        if (isTourDebtor)
//        {
//            locCode = new TourHedDS(getActivity()).getCurrentLocCodeFromTourHed().trim();
//        }
//        else
//        {
//            locCode = new SalRepDS(getActivity()).getCurrentLocCode().trim();
//        }
//        mSharedPref.setGlobalVal("PrekeyLocCode", locCode);
//        Log.d("PRE_SALES_CUSTOMER", "LOC_CODE: " + locCode);
//
//        listener.moveBackToCustomer_pre(1);
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    @Override
    public void onAttach(Activity activity) {
        this.mactivity = activity;
        super.onAttach(mactivity);
        super.onAttach(activity);
        try {
            listener = (IResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }


    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void mSearchDialogBox() {

//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        View promptView = layoutInflater.inflate(R.layout.input_dialogbox, null);
//        final EditText enteredText = (EditText) promptView.findViewById(R.id.txtTextbox);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        alertDialogBuilder.setView(promptView);
//
//        alertDialogBuilder.setCancelable(false).setPositiveButton("Search", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int id) {
//                lvCustomers.setAdapter(null);
//                customerList = new DebtorDS(getActivity()).getRouteCustomers("", enteredText.getText().toString());
//                lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));
//            }
//
//        }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//            }
//        });
//
//        alertDialogBuilder.create().show();
    }

    public void tourSelectionDialogBox() {

        String message = "Please select tour to continue...";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Tour Selection");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();

            }
        }).setNegativeButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    // to retreive the customers

    private class getAllCustomer extends AsyncTask<String, Integer, Boolean> {
        int totalRecords=0;
        CustomProgressDialog pdialog;
        private String repcode;

        public getAllCustomer(String repCode){
            this.repcode = repCode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog  = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {

            int totalBytes = 0;

            try {
                if (mSharedPref.getLoginUser()!= null && mSharedPref.isLoggedIn()) {

/*****************Customers**********************************************************************/

                    String outlets = "";
                    try {
                        outlets = networkFunctions.getCustomer(repcode);
                        // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing outlets
                    try {
                        JSONObject customersJSON = new JSONObject(outlets);
                        JSONArray customersJSONArray =customersJSON.getJSONArray("outlets");
                        for (int i = 0; i < customersJSONArray.length(); i++) {
                            customerList.add(Customer.parseOutlet(customersJSONArray.getJSONObject(i)));
                        }


                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
/*****************end Customers**********************************************************************/

                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                // errors.add("Unable to reach the server.");

//                ErrorUtil.logException(LoginActivity.this, "LoginActivity -> Authenticate -> doInBackground # Login",
//                        e, null, BugReport.SEVERITY_LOW);

                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                // errors.add("Received an invalid response from the server.");

//                ErrorUtil.logException(LoginActivity.this, "LoginActivity -> Authenticate -> doInBackground # Login",
//                        e, loginResponse, BugReport.SEVERITY_HIGH);

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            if (result)
            {
                customerAdapter = new CustomerAdapter(getActivity(), customerList);
                lvCustomers.setAdapter(customerAdapter);
            }
//            pdialog.setMessage("Finalizing data");
//            pdialog.setMessage("Download Completed..");
//            if (result) {
//                if (pdialog.isShowing()) {
//                    pdialog.dismiss();
//                }
//                mSharedPref.setLoginStatus(true);
//                Intent intent = new Intent(getActivity(), ActivityHome
//                        .class);
//                startActivity(intent);
//                //finish();
//            } else {
//                if (pdialog.isShowing()) {
//                    pdialog.dismiss();
//                }
//
//            }
        }
    }

}
