package com.datamation.sfa.fragment.debtorlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.adapter.CustomerAdapter;
import com.datamation.sfa.controller.CustomerController;
import com.datamation.sfa.controller.RouteDetController;
import com.datamation.sfa.controller.SalRepController;
import com.datamation.sfa.dialog.CustomProgressDialog;
import com.datamation.sfa.helpers.NetworkFunctions;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.settings.GPSTracker;
import com.datamation.sfa.view.DebtorDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class AllCustomerFragment extends Fragment {

    View view;
    ListView lvCustomers;
    SharedPref mSharedPref;
    private NetworkFunctions networkFunctions;
    ArrayList<Customer> customerList;
    CustomerAdapter customerAdapter;
    GPSTracker gpsTracker;
    private Customer debtor;
    Switch mySwitch;
    SearchView mSearchDeb;
    String routeCode="";
    double longi = 0.0;
    double lati = 0.0;
    private boolean isGPSSwitched;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_all_customer, container, false);

        lvCustomers = (ListView)view.findViewById(R.id.all_cus_lv);
        mySwitch = (Switch) view.findViewById(R.id.gps_switch);
        mSearchDeb = (SearchView) view.findViewById(R.id.et_all_deb_search);
        mSharedPref = new SharedPref(getActivity());
        gpsTracker = new GPSTracker(getActivity());

        if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            mySwitch.setText("GPS Mode");
        }
        else
        {
            mySwitch.setText("GPS OFF");
            mySwitch.setText("GPS ON");
        }

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

                    if(isGPSSwitched)
                    {
                        mSharedPref.setGPSDebtor("AY");
                    }
                    else
                    {
                        mSharedPref.setGPSDebtor("AN");
                    }

                    if (isValidateCustomer(debtor))
                    {
                        Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                        intent.putExtra("outlet",debtor);
                        mSharedPref.setSelectedDebCode(debtor.getCusCode());
                        mSharedPref.setSelectedDebName(debtor.getCusName());
                        mSharedPref.setSelectedDebRouteCode(new RouteDetController(getActivity()).getRouteCodeByDebCode(debtor.getCusCode()));
                        mSharedPref.setSelectedDebtorPrilCode(debtor.getCusPrilCode());
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Please tick the 'Automatic Date and Time' option to continue..", Toast.LENGTH_LONG).show();
                    /* Show Date/time settings dialog */
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
            }
        });

        isGPSSwitched = false;

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b) {
                    gpsTracker = new GPSTracker(getActivity());

                    if (gpsTracker.canGetLocation()) {
                        isGPSSwitched = true;
                        Log.d("Latitude", mSharedPref.getGlobalVal("Latitude") + ", " + mSharedPref.getGlobalVal("Longitude"));

                        if (!mSharedPref.getGlobalVal("Latitude").equals("") && !mSharedPref.getGlobalVal("Longitude").equals("")) {
                            lati = Double.parseDouble(mSharedPref.getGlobalVal("Latitude"));
                            longi = Double.parseDouble(mSharedPref.getGlobalVal("Longitude"));

                            Log.d("Latitude", "Latitude");
                            Log.d("Longitude","Longitude");

                            new getAllGPSCustomer().execute();
                        } else {
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                            //Log.d("gpsTracker","true");
                        }

                    } else {

                        gpsTracker.showSettingsAlert();
                    }

                    mSearchDeb.setQuery("",false);
                    mSearchDeb.clearFocus();


                } else {
                        isGPSSwitched = false;
                        //Log.d("gpsTracker","false");

                        lati = 0.0;
                        longi = 0.0;

                        new getAllCustomer("").execute();
                        mSearchDeb.setQuery("",false);
                        mSearchDeb.clearFocus();
                    }
                }

        });

        mSearchDeb.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!isGPSSwitched)
                {
                    customerList.clear();
                    customerList = new CustomerController(getActivity()).getAllCustomersForSelectedRepCode(new SalRepController(getActivity()).getCurrentRepCode(),query);
                    lvCustomers.setAdapter(new CustomerAdapter(getActivity(),customerList));
                }
                else {
                    customerList.clear();
                    customerList = new CustomerController(getActivity()).getAllGpsCustomerForSelectedRepcode(new SalRepController(getActivity()).getCurrentRepCode(),lati ,longi , query);
                    lvCustomers.setAdapter(new CustomerAdapter(getActivity(),customerList));
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(!isGPSSwitched)
                {
                    customerList.clear();
                    customerList = new CustomerController(getActivity()).getAllCustomersForSelectedRepCode(new SalRepController(getActivity()).getCurrentRepCode(), newText);
                    lvCustomers.setAdapter(new CustomerAdapter(getActivity(),customerList));
                }
                else {
                    customerList.clear();
                    customerList = new CustomerController(getActivity()).getAllGpsCustomerForSelectedRepcode(new SalRepController(getActivity()).getCurrentRepCode(),lati ,longi , newText);
                    lvCustomers.setAdapter(new CustomerAdapter(getActivity(),customerList));
                }

                return true;
            }
        });

       new getAllCustomer("").execute();

        return view;
    }

    private class  getAllGPSCustomer extends  AsyncTask<String, Integer ,Boolean>{

        int totalRecords = 0;
        CustomProgressDialog pDialog;

        public getAllGPSCustomer() {
           this.pDialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new CustomProgressDialog(getActivity());
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setMessage("Authenticating");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try
            {
                customerList = new CustomerController(getActivity()).getAllGpsCustomerForSelectedRepcode(new SalRepController(getActivity()).getCurrentRepCode(),lati,longi,"");
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();

                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            if(result)
            {
                customerAdapter = new CustomerAdapter(getActivity(), customerList);
                lvCustomers.setAdapter(customerAdapter);

                pDialog.setMessage("Finalizing Data");
                pDialog.setMessage("Download Completed");

                if(pDialog.isShowing())
                {
                    pDialog.dismiss();
                }
                mSharedPref.setLoginStatus(true);
            }
            else
            {
                if(pDialog.isShowing())
                {
                    pDialog.dismiss();
                }
            }
        }
    }

    private class getAllCustomer extends AsyncTask<String, Integer, Boolean> {
        int totalRecords=0;
        CustomProgressDialog pdialog;
        private String keyWord;

        public getAllCustomer(String keyWord){
            this.keyWord = keyWord;
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

            try
            {
                customerList = new CustomerController(getActivity()).getAllCustomersForSelectedRepCode(new SalRepController(getActivity()).getCurrentRepCode(), "");

                return true;

            } catch (Exception e) {
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
            pdialog.setMessage("Finalizing data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
                mSharedPref.setLoginStatus(true);
            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
        }
    }

    public boolean isValidateCustomer(Customer customer)
    {
//        if (customer.getCusRoute().equals(""))
//        {
//            errorDialog("Route Error", "Selected debtor has no route to continue...");
//            return false;
//        }
//        else
        if (customer.getCusStatus().equals("I"))
        {
            errorDialog("Status Error", "Selected debtor is inactive to continue...");
            return false;
        }
        else if (customer.getCreditPeriod().equals("N"))
        {
            errorDialog("Period Error", "Credit period expired for Selected debtor...");
            return false;
        }
        else if (Double.parseDouble(customer.getCreditLimit())==0.00)
        {
            errorDialog("Limit Error", "Credit limit exceed for Selected debtor...");
            return false;
        }
        else if (customer.getCreditStatus().equals("N"))
        {
            errorDialog("Credit Status Error", "Credit status not valid for Selected debtor...");
            return false;
        }
        else
        {
            return true;
        }
    }

    public void errorDialog(String title, String msg)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.info);


        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
