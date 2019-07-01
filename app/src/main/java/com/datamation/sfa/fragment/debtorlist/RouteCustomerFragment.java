package com.datamation.sfa.fragment.debtorlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.adapter.CustomerAdapter;
import com.datamation.sfa.controller.CustomerController;
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

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;


public class RouteCustomerFragment extends Fragment {

    View view;
    ListView lvCustomers;
    SharedPref mSharedPref;
    private NetworkFunctions networkFunctions;
    ArrayList<Customer> customerList;
    CustomerAdapter customerAdapter;
    GPSTracker gpsTracker;
    private Customer debtor;
    String routeCode="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_route_customer, container, false);

        lvCustomers = (ListView)view.findViewById(R.id.route_cus_lv);
        mSharedPref = new SharedPref(getActivity());
        gpsTracker = new GPSTracker(getActivity());
        networkFunctions = new NetworkFunctions(getActivity());

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

                    if (isValidateCustomer(debtor))
                    {
                        Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                        intent.putExtra("CUSTOMER_CODE", "");
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Please tick the 'Automatic Date and Time' option to continue..", Toast.LENGTH_LONG).show();
                    /* Show Date/time settings dialog */
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
            }
        });

        //String rCode = new SalRepController(getActivity()).getCurrentRepCode().trim();

        new getRouteCustomer("SR01").execute();

        return view;
    }

    private class getRouteCustomer extends AsyncTask<String, Integer, Boolean> {
        int totalRecords=0;
        CustomProgressDialog pdialog;
        private String repcode;

        public getRouteCustomer(String repCode){
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

            try
            {
                customerList = new CustomerController(getActivity()).getAllCustomersByRoute(repcode);

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
        if (customer.getCusRoute().equals(""))
        {
            errorDialog("Route Error", "Selected debtor has no route to continue...");
            return false;
        }
        else if (customer.getCusStatus().equals("I"))
        {
            errorDialog("Status Error", "Selected debtor is inactive to continue...");
            return false;
        }
        else if (customer.getCreditPeriod().equals("N"))
        {
            errorDialog("Period Error", "Credit period expired for Selected debtor...");
            return false;
        }
        else if (Double.parseDouble(customer.getCreditLimit())>0.00)
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
