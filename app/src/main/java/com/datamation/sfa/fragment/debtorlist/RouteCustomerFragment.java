package com.datamation.sfa.fragment.debtorlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.adapter.CustomerAdapter;
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
                                    //creditDatesExceedDialog(String.valueOf(noOfOverDue));
                                }
                                else{
                                    Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                                    intent.putExtra("CUSTOMER_CODE", "");
                                    startActivity(intent);
                                }
                            }else{

                                Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                                intent.putExtra("CUSTOMER_CODE", "");
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            //debtorStatusDialog();
                        }
                    }
                    else
                    {
                        //routeValidateDialog();
                    }

                }
                else {
                    Toast.makeText(getActivity(), "Please tick the 'Automatic Date and Time' option to continue..", Toast.LENGTH_LONG).show();
                    /* Show Date/time settings dialog */
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
            }
        });

        new getRouteCustomer("").execute();

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
            pdialog.setMessage("Finalizing data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
                mSharedPref.setLoginStatus(true);
//                Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
//                startActivity(intent);
            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
        }
    }


}
