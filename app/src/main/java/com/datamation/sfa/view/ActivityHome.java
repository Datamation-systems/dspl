package com.datamation.sfa.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.controller.CustomerController;
//import com.datamation.sfa.controller.ItemController;
import com.datamation.sfa.controller.ItemPriceController;
import com.datamation.sfa.controller.NewCustomerController;
import com.datamation.sfa.controller.ReasonController;
import com.datamation.sfa.customer.CustomerRegMain;
import com.datamation.sfa.dialog.CustomProgressDialog;
import com.datamation.sfa.helpers.IResponseListener;
import com.datamation.sfa.controller.OrderController;
import com.datamation.sfa.helpers.NetworkFunctions;
import com.datamation.sfa.helpers.SQLiteBackUp;

import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.ItemPri;
import com.datamation.sfa.controller.RouteController;
import com.datamation.sfa.model.Order;
import com.datamation.sfa.model.Reason;
import com.datamation.sfa.model.Route;
import com.datamation.sfa.model.User;
//import com.datamation.sfa.presale.OrderMainFragment;
import com.datamation.sfa.R;

import com.datamation.sfa.settings.ContentItem;
import com.datamation.sfa.settings.ImportActivity;
import com.datamation.sfa.adapter.ListViewDataAdapter;
import com.datamation.sfa.settings.ReferenceNum;
import com.datamation.sfa.settings.UserSessionManager;
import com.datamation.sfa.utils.NetworkUtil;
import com.datamation.sfa.utils.UtilityContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityHome extends AppCompatActivity implements IResponseListener{

    private TextView mTextMessage;
    public static final String SETTINGS = "SETTINGS";
    // web service connection URL (SVC)
    //debtor
    public Customer selectedDebtor = null;
    public boolean FreeTapped = false;
    //fordhed
    public Order selectedOrdHed = null;
    //ftranHed
    public int cusPosition = 0;
    public int gpsseq = 0;
    private Context context = this;
    public String TAG = "ActivityHome.class";
    NetworkFunctions networkFunctions;
    public static String SAcustomer,SAroute;
    List<String> resultList;
    SharedPref pref;
    User loggedUser;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    changeFragment(0);

                    return true;
                case R.id.navigation_sales:
                    SharedPref sharedPref = SharedPref.getInstance(context);
                    if (sharedPref.getGlobalVal("dayStart").equalsIgnoreCase("Y")) {
                        Intent intent = new Intent(getApplicationContext(), DebtorListActivity.class);
                        startActivity(intent);


                        Log.d("newcus clicked", "position2");
                    } else {
                        UtilityContainer.mLoadFragment(new FragmentMarkAttendance(), ActivityHome.this);
                    }

                    return true;
                case R.id.navigation_tools:
                    UtilityContainer.mLoadFragment(new FragmentTools(), ActivityHome.this);

                    return true;
                case R.id.navigation_logout:
                    Logout();
                    //logoutMessage("Do you want to log out?", "Logout");
                    return true;
            }
            return false;
        }
    };
    public static BottomNavigationView navigation;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pref = SharedPref.getInstance(this);
        networkFunctions = new NetworkFunctions(this);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        resultList = new ArrayList<>();
        loggedUser = pref.getLoginUser();

        //set home frgament
        changeFragment(0);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void Logout() {

        final Dialog Ldialog = new Dialog(ActivityHome.this);
        Ldialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Ldialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Ldialog.setContentView(R.layout.logout);

        Ldialog.show();
        //logout
        Ldialog.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSessionManager sessionManager = new UserSessionManager(context);
                sessionManager.Logout();
                finish();

            }
        });

    }

    public void logoutMessage(String message, String title) {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.info);


        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    public void managementTools() {
        final Dialog dialog = new Dialog(ActivityHome.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.management_tools);

        //sync
        dialog.findViewById(R.id.Sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncMasterDataDialog(context);
            }
        });

        //upload
        dialog.findViewById(R.id.Upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        dialog.findViewById(R.id.btn_deposit_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //UtilityContainer.mLoadFragment(new ExpenseMain(), ActivityHome.this);
                dialog.hide();
            }
        });
        dialog.findViewById(R.id.btn_sales_exec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilityContainer.mLoadFragment(new FragmentSecondary(), ActivityHome.this);
                dialog.hide();
            }
        });
        dialog.findViewById(R.id.btn_stock_inquery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilityContainer.mLoadFragment(new FragmentSecondary(), ActivityHome.this);
                dialog.hide();
            }
        });


        dialog.show();

    }



    private void syncMasterDataDialog(final Context context) {
       // final String sp_url = localSP.getString("URL", "").toString();
        // String spConsole_DB = localSP.getString("Console_DB", "").toString();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Are you sure, Do you want to Sync Master Data?");

        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @SuppressWarnings("unchecked")
            public void onClick(DialogInterface dialog, int id) {
//                if (localSP.getString("MAC_Address", "No MAC Address").equals("No MAC Address")) {
//                    GetMacAddress macAddress = new GetMacAddress();
//                    SharedPreferencesClass.setLocalSharedPreference(context, "MAC_Address",
//                            macAddress.getMacAddress(context));
//                }


                boolean connectionStatus = NetworkUtil.isNetworkAvailable(ActivityHome.this);
                if (connectionStatus == true) {

                    if (isAllUploaded()) {
                        dialog.dismiss();
                        try {
                            new secondarySync(pref.getLoginUser().getCode()).execute();

                        } catch (Exception e) {
                            Log.e("## ErrorIn2ndSync ##", e.toString());
                        }
                    } else {
                        Toast.makeText(context, "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();
    }

    private boolean isAllUploaded() {
        Boolean allUpload = false;
        OrderController hedDS = new OrderController(context);

        ArrayList<Order> ordHedList = hedDS.getAllUnSyncOrdHed();

        if (ordHedList.isEmpty()) {
            allUpload = true;
        } else {
            allUpload = false;
        }

        return allUpload;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
////            case R.id.item1:
////                settingsMenu();
////                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }



    public void ViewRepProfile() {
        final Dialog repDialog = new Dialog(context);
        repDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        repDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        repDialog.setCancelable(false);
        repDialog.setCanceledOnTouchOutside(false);
        repDialog.setContentView(R.layout.rep_profile);

        //initializations

//
        TextView repname = (TextView) repDialog.findViewById(R.id.repname);
        TextView repcode = (TextView) repDialog.findViewById(R.id.repcode);
        TextView repPrefix = (TextView) repDialog.findViewById(R.id.repPrefix);
       // TextView locCode = (TextView) repDialog.findViewById(R.id.target);
        TextView email= (TextView) repDialog.findViewById(R.id.email);
        TextView dealCode= (TextView) repDialog.findViewById(R.id.Dealcode);
        TextView areaCode = (TextView) repDialog.findViewById(R.id.areaCode);
      //  areaCode.setText(loggedUser.getRoute());

        repname.setText(loggedUser.getName());
        repcode.setText(loggedUser.getCode());
        repPrefix.setText(loggedUser.getPrefix());
        //locCode.setText("0.0");
        email.setText(loggedUser.getEmail());
        dealCode.setText(loggedUser.getDealcode());

        //close
        repDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repDialog.dismiss();
            }
        });

        repDialog.show();
    }

    public void viewRouteInfo() {
        final Dialog repDialog = new Dialog(context);
        repDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        repDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        repDialog.setCancelable(false);
        repDialog.setCanceledOnTouchOutside(false);
        repDialog.setContentView(R.layout.rep_route_profile);

        //initializations
        RouteController routeDS = new RouteController(context);
      //  String routes = routeDS.getRouteNameByCode(loggedUser.getRoute());

        TextView routeName = (TextView) repDialog.findViewById(R.id.routeName);
       // routeName.setText(routes);
        TextView routeCode = (TextView) repDialog.findViewById(R.id.routeCode);
     //   routeCode.setText(loggedUser.getRoute());


        //close
        repDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repDialog.dismiss();
            }
        });

        repDialog.show();
    }

    private void sqliteDatabaseDialogbox(final Context context, String title) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View promptView = layoutInflater.inflate(R.layout.settings_sqlite_database_layout, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setView(promptView);

        final Button b_backups = (Button) promptView.findViewById(R.id.b_backups);
        final Button b_restore = (Button) promptView.findViewById(R.id.b_restore);

        b_backups.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SQLiteBackUp backUp = new SQLiteBackUp(getApplicationContext());
                backUp.exportDB();
            }
        });

        b_restore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                LayoutInflater layoutInflater = LayoutInflater.from(context);

                View promptView = layoutInflater.inflate(R.layout.settings_sqlite_password_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptView);

                final EditText password = (EditText) promptView.findViewById(R.id.et_password);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (password.getText().toString().toString().equals("admin@rs")) {
                                    Intent myIntent = new Intent(context, ImportActivity.class);
                                    startActivity(myIntent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Password.", Toast.LENGTH_LONG).show();
                                    //dialog.cancel();
                                }

                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();


                                    }
                                });

                AlertDialog alertD = alertDialogBuilder.create();

                alertD.show();
                /*Intent myIntent = new Intent(context,ImportActivity.class);
                startActivity(myIntent);*/

            }
        });

        alertDialogBuilder.setCancelable(false).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();
    }


    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    /**
     * To load fragments for sample
     *
     * @param position menu index
     */
    private void changeFragment(int position) {



        if (position == 0) {
         //   newFragment = new FragHome();
            Log.d(">>>>>>", "position0");
            UtilityContainer.mLoadFragment(new FragmentHome(), ActivityHome.this);
        } else if (position == 1) {
            Log.d(">>>>>>", "position1");
//            if (pref.getGlobalVal("dayStart").equalsIgnoreCase("Y")) {
//                UtilityContainer.mLoadFragment(new OrderMainFragment(), ActivityHome.this);
//                Log.d("Presale clicked", "position1");
//            } else {
//                Toast.makeText(context, "Please add the Day start entry first", Toast.LENGTH_SHORT).show();
//            }
            //UtilityContainer.mLoadFragment(new OrderMainFragment(), ActivityHome.this);
        } else if (position == 2) {
            Log.d(">>>>>>", "position2");
            UtilityContainer.mLoadFragment( new CustomerRegMain(), ActivityHome.this);
         //   newFragment = new CustomerRegMain();
        } else if (position == 3) {
            Log.d(">>>>>>", "position3");
                //UtilityContainer.mLoadFragment(new NonProductiveMain(), ActivityHome.this);

        } else if (position == 4) {
            Log.d(">>>>>>", "position4");
           // newFragment = new FragDayInfo();
            UtilityContainer.mLoadFragment(new FragmentMarkAttendance(), ActivityHome.this);
        } else if (position == 5) {
            Log.d(">>>>>>", "position5");
            //UtilityContainer.mLoadFragment(new ExpenseMain(), ActivityHome.this);

        }

//
//        getFragmentManager().beginTransaction().replace(
//                R.id.fragmentContainer, newFragment)
//                .commit();
    }


    public void mUploadResult(String message) {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityHome.this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("Upload Summary");

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                //UtilityContainer.mLoadFragment(new SalesManagementFragment(), ActivityHome.this);
                dialog.cancel();
            }
        });
        android.app.AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }
    public void bottomNav(Boolean cmd) {

        if (cmd == true) {

            navigation.setVisibility(View.GONE);
        } else {
            navigation.setVisibility(View.VISIBLE);
        }
    }

    public static void PrinterDialogbox(final Context context) {


        View promptView = LayoutInflater.from(context).inflate(R.layout.settings_printer_layout, null);
        final EditText serverURL = (EditText) promptView.findViewById(R.id.et_mac_address);

        String printer_mac_shared_pref = "";
        printer_mac_shared_pref = SharedPref.getInstance(context).getGlobalVal("printer_mac_address");

        if(!TextUtils.isEmpty(printer_mac_shared_pref))
        {
            serverURL.setText(printer_mac_shared_pref);
            Toast.makeText(context, "MAC Address Already Exists", Toast.LENGTH_LONG).show();
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(promptView)
                .setTitle("Printer MAC Address")
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                Button bOk = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button bClose = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);



                bOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (serverURL.length() > 0) {

                            if (validate(serverURL.getText().toString().toUpperCase())) {
                                //SharedPreferencesClass.setLocalSharedPreference(context, "printer_mac_address", serverURL.getText().toString().toUpperCase());
                                SharedPref.getInstance(context).setGlobalVal("printer_mac_address", serverURL.getText().toString().toUpperCase());
                                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(context, "Enter Valid MAC Address", Toast.LENGTH_LONG).show();
                            }
                        } else
                            Toast.makeText(context, "Type in the MAC Address", Toast.LENGTH_LONG).show();
                    }
                });

                bClose.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }
    //
    public static boolean validate(String mac) {
        Pattern p = Pattern.compile("^([a-fA-F0-9]{2}[:-]){5}[a-fA-F0-9]{2}$");
        Matcher m = p.matcher(mac);
        return m.find();
    }

    @Override
    public void moveNextFragment_Pre() {
//        FragmentManager manager = getSupportFragmentManager();
//         SalesManagementFragment frag = (SalesManagementFragment) manager.findFragmentByTag(SalesManagementFragment.class.getSimpleName());
//        frag.mMoveToHeader();
    }

    @Override
    public void moveNextFragment_NonProd() {
//        FragmentManager manager = getSupportFragmentManager();
//        NonProductiveManage frag = (NonProductiveManage) manager.findFragmentByTag(NonProductiveManage.class.getSimpleName());
//        frag.mMoveToHeader();
    }

    private class secondarySync extends AsyncTask<String, Integer, Boolean> {
        int totalRecords=0;
        CustomProgressDialog pdialog;
        private String repcode;

        public secondarySync(String repCode){
            this.repcode = repCode;
            this.pdialog = new CustomProgressDialog(ActivityHome.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog  = new CustomProgressDialog(ActivityHome.this);
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            pDialog.setMessage("Validating...");
//            pDialog.show();
//            pdialog = new SpotsDialog(getApplicationContext());
//            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {


            int totalBytes = 0;

            try {
                if (pref.getLoginUser()!= null && pref.isLoggedIn()) {

/*****************Customers**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Authenticated\nDownloading Customers...");
                        }
                    });

                    String outlets = "";
                    try {
                        outlets = networkFunctions.getCustomer(repcode);
                        // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (customer details)...");
                        }
                    });

                    // Processing outlets
                    try {
                        JSONObject customersJSON = new JSONObject(outlets);
                        JSONArray customersJSONArray =customersJSON.getJSONArray("outlets");
                        ArrayList<Customer> customerList = new ArrayList<Customer>();
                        CustomerController customerController = new CustomerController(ActivityHome.this);
                        for (int i = 0; i < customersJSONArray.length(); i++) {
                            customerList.add(Customer.parseOutlet(customersJSONArray.getJSONObject(i)));
                        }
                       // customerController.createOrUpdateDebtor(customerList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
/*****************end Customers**********************************************************************/
/*****************routes*****************************************************************************/

                    //routes
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Customer downloaded\nDownloading Routes...");
                        }
                    });

                    String routes = "";
                    try {
                        routes = networkFunctions.getRoutes(repcode);
                        // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (route details)...");
                        }
                    });

                    // Processing outlets
                    try {
                        JSONObject routesJSON = new JSONObject(routes);
                        JSONArray routesJSONArray =routesJSON.getJSONArray("routes");
                        ArrayList<Route> routeList = new ArrayList<Route>();
                        RouteController routeController = new RouteController(ActivityHome.this);
                        for (int i = 0; i < routesJSONArray.length(); i++) {
                            routeList.add(Route.parseRoute(routesJSONArray.getJSONObject(i)));
                        }
                        routeController.createOrUpdateFRoute(routeList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
/*****************end routes**********************************************************************/
/*****************references**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Routes downloaded\nDownloading References...");
                        }
                    });

                    String references = "";
                    try {
                        references = networkFunctions.getReferences(repcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (reference details)...");
                        }
                    });

                    // Processing references
//                    try {
//                        JSONObject refJSON = new JSONObject(references);
//                        JSONArray refJSONArray =refJSON.getJSONArray("references");
//                        ArrayList<ReferenceDetail> refList = new ArrayList<ReferenceDetail>();
//                        ReferenceDetailDownloader refController = new ReferenceDetailDownloader(ActivityHome.this);
//                        for (int i = 0; i < refJSONArray.length(); i++) {
//                            refList.add(ReferenceDetail.parseRef(refJSONArray.getJSONObject(i)));
//                        }
//                        refController.createOrUpdateFCompanyBranch(refList);
//                    } catch (JSONException | NumberFormatException e) {
//
////                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
////                                e, routes, BugReport.SEVERITY_HIGH);
//
//                        throw e;
//                    }
/*****************ennd references**********************************************************************/
/*****************reference settings**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Reference detail downloaded\nDownloading reference settings...");
                        }
                    });

                    String settings = "";
                    try {
                        settings = networkFunctions.getReferenceSettings();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (setting details)...");
                        }
                    });

                    // Processing outlets
//                    try {
//                        JSONObject settingJSON = new JSONObject(settings);
//                        JSONArray settingJSONArray =settingJSON.getJSONArray("refSettings");
//                        ArrayList<RefSetting> settingList = new ArrayList<RefSetting>();
//                        ReferenceSettingController settingController = new ReferenceSettingController(ActivityHome.this);
//                        for (int i = 0; i < settingJSONArray.length(); i++) {
//                            settingList.add(RefSetting.parseSetting(settingJSONArray.getJSONObject(i)));
//                        }
//                        settingController.createOrUpdateReferenceSetting(settingList);
//                    } catch (JSONException | NumberFormatException e) {
//
////                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
////                                e, routes, BugReport.SEVERITY_HIGH);
//
//                        throw e;
//                    }
                    /*****************end reference settings**********************************************************************/
                    /*****************reasons**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Reference Setting details downloaded\nDownloading reasons...");
                        }
                    });

                    String reasons = "";
                    try {
                        reasons = networkFunctions.getReasons();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (reasons)...");
                        }
                    });

                    // Processing reasons
                    try {
                        JSONObject reasonJSON = new JSONObject(reasons);
                        JSONArray reasonJSONArray =reasonJSON.getJSONArray("reasons");
                        ArrayList<Reason> reasonList = new ArrayList<Reason>();
                        ReasonController reasonController = new ReasonController(ActivityHome.this);
                        for (int i = 0; i < reasonJSONArray.length(); i++) {
                            reasonList.add(Reason.parseReason(reasonJSONArray.getJSONObject(i)));
                        }
                        reasonController.createOrUpdateReason(reasonList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end reasons**********************************************************************/
                    /*****************reasons**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Reasons downloaded\nDownloading items...");
                        }
                    });

                    String items = "";
                    try {
                        items = networkFunctions.getItems();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (items)...");
                        }
                    });

                    // Processing items
//                    try {
//                        JSONObject itemJSON = new JSONObject(items);
//                        JSONArray itemJSONArray =itemJSON.getJSONArray("items");
//                        ArrayList<Item> itemList = new ArrayList<Item>();
//                        ItemController itemController = new ItemController(ActivityHome.this);
//                        for (int i = 0; i < itemJSONArray.length(); i++) {
//                            itemList.add(Item.parseItem(itemJSONArray.getJSONObject(i)));
//                        }
//                        itemController.InsertItems(itemList);
//                    } catch (JSONException | NumberFormatException e) {
//
////                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
////                                e, routes, BugReport.SEVERITY_HIGH);
//
//                        throw e;
//                    }
                    /*****************end items**********************************************************************/
                    /*****************prices**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Items downloaded\nDownloading prices...");
                        }
                    });

                    String prices = "";
                    try {
                        prices = networkFunctions.getPrices();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (prices)...");
                        }
                    });

                    // Processing reasons
                    try {
                        JSONObject priceJSON = new JSONObject(prices);
                        JSONArray priceJSONArray =priceJSON.getJSONArray("itemprices");
                        ArrayList<ItemPri> priceList = new ArrayList<ItemPri>();
                        ItemPriceController priceController = new ItemPriceController(ActivityHome.this);
                        for (int i = 0; i < priceJSONArray.length(); i++) {
                            //priceList.add(ItemPri.parsePrices(priceJSONArray.getJSONObject(i)));
                        }
                      //  priceController.createOrUpdateItemPri(priceList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end prices**********************************************************************/

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

            pdialog.setMessage("Finalizing data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
                pref.setLoginStatus(true);
                Intent intent = new Intent(ActivityHome
                        .this, ActivityHome
                        .class);
                startActivity(intent);
                finish();
            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
        }
    }
}
