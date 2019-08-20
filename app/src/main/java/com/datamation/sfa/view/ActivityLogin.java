package com.datamation.sfa.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.controller.BankController;
import com.datamation.sfa.controller.CompanyDetailsController;
import com.datamation.sfa.controller.CustomerController;
//import com.datamation.sfa.controller.ItemController;
import com.datamation.sfa.controller.DebItemPriController;
import com.datamation.sfa.controller.DiscdebController;
import com.datamation.sfa.controller.DiscdetController;
import com.datamation.sfa.controller.DischedController;
import com.datamation.sfa.controller.DiscslabController;
import com.datamation.sfa.controller.ExpenseController;
import com.datamation.sfa.controller.FInvhedL3DS;
import com.datamation.sfa.controller.FreeDebController;
import com.datamation.sfa.controller.FreeDetController;
import com.datamation.sfa.controller.FreeHedController;
import com.datamation.sfa.controller.FreeItemController;
import com.datamation.sfa.controller.FreeMslabController;
import com.datamation.sfa.controller.FreeSlabController;
import com.datamation.sfa.controller.ItemController;
import com.datamation.sfa.controller.ItemLocController;
import com.datamation.sfa.controller.ItemPriceController;
import com.datamation.sfa.controller.LocationsController;
import com.datamation.sfa.controller.OutstandingController;
import com.datamation.sfa.controller.ReasonController;
import com.datamation.sfa.controller.ReferenceDetailDownloader;
import com.datamation.sfa.controller.ReferenceSettingController;
import com.datamation.sfa.controller.RouteController;
import com.datamation.sfa.controller.RouteDetController;
import com.datamation.sfa.controller.STKInController;
import com.datamation.sfa.controller.SalRepController;
import com.datamation.sfa.controller.TaxController;
import com.datamation.sfa.controller.TaxDetController;
import com.datamation.sfa.controller.TaxHedController;
import com.datamation.sfa.controller.TourController;
import com.datamation.sfa.dialog.CustomProgressDialog;
import com.datamation.sfa.helpers.NetworkFunctions;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Bank;
import com.datamation.sfa.model.CompanyBranch;
import com.datamation.sfa.model.CompanySetting;
import com.datamation.sfa.model.Control;
import com.datamation.sfa.model.DebItemPri;
import com.datamation.sfa.model.Debtor;
import com.datamation.sfa.model.Discdeb;
import com.datamation.sfa.model.Discdet;
import com.datamation.sfa.model.Disched;
import com.datamation.sfa.model.Discslab;
import com.datamation.sfa.model.Expense;
import com.datamation.sfa.model.FInvhedL3;
import com.datamation.sfa.model.FddbNote;
import com.datamation.sfa.model.FreeDeb;
import com.datamation.sfa.model.FreeDet;
import com.datamation.sfa.model.FreeHed;
import com.datamation.sfa.model.FreeItem;
import com.datamation.sfa.model.FreeMslab;
import com.datamation.sfa.model.FreeSlab;
import com.datamation.sfa.model.Item;
import com.datamation.sfa.model.ItemLoc;
import com.datamation.sfa.model.ItemPri;
import com.datamation.sfa.model.Locations;
import com.datamation.sfa.model.Reason;
import com.datamation.sfa.model.ReferenceDetail;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.RefSetting;
import com.datamation.sfa.model.Route;
import com.datamation.sfa.model.RouteDet;
import com.datamation.sfa.model.SalRep;
import com.datamation.sfa.model.StkIn;
import com.datamation.sfa.model.Tax;
import com.datamation.sfa.model.TaxDet;
import com.datamation.sfa.model.TaxHed;
import com.datamation.sfa.model.TourHed;
import com.datamation.sfa.model.User;
import com.datamation.sfa.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    EditText username, password;
    TextView txtver;
    SharedPref pref;
    User loggedUser;
    NetworkFunctions networkFunctions;
    private static String spURL = "";
    int tap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        networkFunctions = new NetworkFunctions(this);
        pref = SharedPref.getInstance(this);
        username = (EditText) findViewById(R.id.editText1);
        password = (EditText) findViewById(R.id.editText2);
        Button login = (Button) findViewById(R.id.btnlogin);
        txtver = (TextView) findViewById(R.id.textVer);
        txtver.setText("Version " + getVersionCode());
        loggedUser = pref.getLoginUser();

        login.setOnClickListener(this);

        txtver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tap += 1;
               // StartTimer(3000);
                if (tap >= 7) {
                    validateDialog();
                }
            }
        });

    }
    private void validateDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.ip_connection_dailog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setView(promptView);
        final EditText input = (EditText) promptView.findViewById(R.id.txt_Enter_url);

        input.setText(pref.getBaseURL().substring(7));
//        DBList = (Spinner) promptView.findViewById(R.id.spinner2);
//
//        //dbName =(TextView) promptView.findViewById(R.id.txtDatabase);
//        DBList =(Spinner)promptView.findViewById(R.id.spinner2);
//        DBList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                DBNAME = DBList.getSelectedItem().toString();
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        Button btn_validate = (Button)promptView.findViewById(R.id.btn_validate);
        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spURL = input.getText().toString().trim();
                String URL = "http://" + input.getText().toString().trim();
                if (Patterns.WEB_URL.matcher(URL).matches())
                {
//                    if (NetworkUtil.isNetworkAvailable(ActivitySplash.this))
//                    {
                    pref.setBaseURL(spURL);
                    Toast.makeText(ActivityLogin.this, "URL config success."+spURL, Toast.LENGTH_LONG).show();


//                    }
//                    else
//                    {
//                        Snackbar snackbar = Snackbar.make(v, R.string.txt_msg, Snackbar.LENGTH_LONG);
//                        View snackbarLayout = snackbar.getView();
//                        snackbarLayout.setBackgroundColor(Color.RED);
//                        TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
//                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_signal_wifi_off_black_24dp, 0, 0, 0);
//                        textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.body_size));
//                        textView.setTextColor(Color.WHITE);
//                        snackbar.show();
//
//                        // Toast.makeText(finac_payroll_splash.this, "No Internet Connection", Toast.LENGTH_LONG).show();
//                        reCallActivity();
//                    }
                } else {
                    Toast.makeText(ActivityLogin.this, "Invalid URL Entered. Please Enter Valid URL.", Toast.LENGTH_LONG).show();
                    reCallActivity();
                }
            }
        });



        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String URL = "http://" + input.getText().toString().trim();
                pref.setBaseURL(URL);
                if(URL.length()!=0)
                {
                    //   pref.setDBNAME(DBNAME);
//                    if (Patterns.WEB_URL.matcher(URL).matches()&& URL.length()== 26)
                    if (Patterns.WEB_URL.matcher(URL).matches())
                    {
                        if (NetworkUtil.isNetworkAvailable(ActivityLogin.this))
                        {
                            pref.setBaseURL(URL);
                            new Validate(pref.getMacAddress().trim(),URL).execute();
                            //TODO: validate uname pwd with server details
//                            String debtorURL = getResources().getString(R.string.ConnURL) + "/fSalrep/mobile123/"+pref.getDBNAME() +"/"+ pref.getMacAddress().replace(":", "");
//                            // String URL = getResources().getString(R.string.ConnectionURL) + "/femployee/mobile123/" + databaseName + "/" + UIdStr.toString() + "/" + UserNameStr.toString();
//                            new Downloader(SplashActivity.this, SplashActivity.this, FSALREP, URL, debtorURL).execute();
                            //me tika wenna one username pwd server yawala validate unata passe..

                            //pref.setFirstTimeLaunch(true);


                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(promptView, R.string.txt_msg, Snackbar.LENGTH_LONG);
                            View snackbarLayout = snackbar.getView();
                            snackbarLayout.setBackgroundColor(Color.RED);
                            TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_signal_wifi_off_black_24dp, 0, 0, 0);
                            textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.body_size));
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                            reCallActivity();
                        }

                    } else {
                        Toast.makeText(ActivityLogin.this, "Invalid URL Entered. Please Enter Valid URL.", Toast.LENGTH_LONG).show();
                        reCallActivity();
                    }

                }else
                {
                    Toast.makeText(ActivityLogin.this, "Please fill informations", Toast.LENGTH_LONG).show();
                    validateDialog();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                ActivityLogin.this.finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    public void reCallActivity(){
        Intent mainActivity = new Intent(ActivityLogin.this, ActivityLogin.class);
        startActivity(mainActivity);
    }
    public void StartTimer(int timeout) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tap = 0;
            }
        }, timeout);

    }
    public String getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0";

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin: {
                if(pref.isLoggedIn()){
                    Intent intent = new Intent(ActivityLogin
                            .this, ActivityHome
                            .class);
                    startActivity(intent);
                    finish();
                }

               else if(!(username.getText().toString().equals("")) && !(password.getText().toString().equals(""))) {
                    //temparary for datamation
                    new Authenticate(SharedPref.getInstance(this).getLoginUser().getCode()).execute();
//                    Intent intent = new Intent(ActivityLogin
//                            .this, ActivityHome
//                            .class);
//                    startActivity(intent);
//                    finish();




//                    String decrepted = getMD5HashVal(password.getText().toString());
//                    String logged = loggedUser.getPassword();
//                   if(!(username.getText().toString().equals(loggedUser.getUserName())) || !(decrepted.equals(loggedUser.getPassword()))){
//                       Toast.makeText(this,"Invalid Username or Password",Toast.LENGTH_LONG).show();
//
//                    }else{
//                       Toast.makeText(this,"Username and Password are correct",Toast.LENGTH_LONG).show();
//
//                       new Authenticate(username.getText().toString(), password.getText().toString(), loggedUser.getCode()).execute();
//                    }
                }else
                    Toast.makeText(this,"Please fill the fields",Toast.LENGTH_LONG).show();


            }
            break;

            default:
                break;
        }
    }


    //--
//    private void LoginValidation() {
//        SalRepDS ds = new SalRepDS(getApplicationContext());
//        ArrayList<SalRep> list = ds.getSaleRepDetails();
//        for (SalRep salRep : list) {
//
//            if (salRep.getREPCODE().equals(username.getText().toString().toUpperCase()) && salRep.getNAME().equals(password.getText().toString().toUpperCase())) {
//
//                StartApp();
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Invalid username or password.", Toast.LENGTH_LONG).show();
//
//            }
//        }
//    }

    private class Authenticate extends AsyncTask<String, Integer, Boolean> {
        int totalRecords=0;
        CustomProgressDialog pdialog;
        private String uname,pwd,repcode;

//        public Authenticate(String uname, String pwd, String repCode){
//            this.uname = uname;
//            this.pwd = pwd;
//            this.repcode = repCode;
//            this.pdialog = new CustomProgressDialog(ActivityLogin.this);
//        }
        public Authenticate(String repCode){
            this.repcode = repCode;
            this.pdialog = new CustomProgressDialog(ActivityLogin.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog  = new CustomProgressDialog(ActivityLogin.this);
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
//                if ((username.getText().toString().equals(uname)) && (password.getText().toString().equals(pwd))
//                        ) {

/*****************Customers**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Authenticated\nDownloading company control data...");
                        }
                    });

                    String controls = "";
                    try {
                        controls = networkFunctions.getCompanyDetails(repcode);
                       // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (Company details)...");
                        }
                    });

                    // Processing outlets
                    try {
                        JSONObject controlJSON = new JSONObject(controls);
                        JSONArray controlJSONArray = controlJSON.getJSONArray("fControlResult");
                        ArrayList<Control> controlList = new ArrayList<Control>();
                        CompanyDetailsController companyController = new CompanyDetailsController(ActivityLogin.this);
                        for (int i = 0; i < controlJSONArray.length(); i++) {
                            controlList.add(Control.parseControlDetails(controlJSONArray.getJSONObject(i)));
                        }
                        companyController.createOrUpdateFControl(controlList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
/*****************end Customers**********************************************************************/

/*****************Customers**********************************************************************/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Control downloaded\nDownloading Customers...");
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
                    JSONArray customersJSONArray =customersJSON.getJSONArray("FdebtorResult");
                    ArrayList<Debtor> customerList = new ArrayList<Debtor>();
                    CustomerController customerController = new CustomerController(ActivityLogin.this);
                    for (int i = 0; i < customersJSONArray.length(); i++) {
                        customerList.add(Debtor.parseOutlet(customersJSONArray.getJSONObject(i)));
                    }
                    customerController.InsertOrReplaceDebtor(customerList);

                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Customers downloaded\nDownloading Company Settings...");
                    }
                });
                /*****************end Customers**********************************************************************/
                /*****************Settings*****************************************************************************/

                String comSettings = "";
                try {
                    comSettings = networkFunctions.getReferenceSettings();
                    // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
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

                // Processing company settings
                try {
                    JSONObject settingJSON = new JSONObject(comSettings);
                    JSONArray settingsJSONArray =settingJSON.getJSONArray("fCompanySettingResult");
                    ArrayList<CompanySetting> settingList = new ArrayList<CompanySetting>();
                    ReferenceSettingController settingController = new ReferenceSettingController(ActivityLogin.this);
                    for (int i = 0; i < settingsJSONArray.length(); i++) {
                        settingList.add(CompanySetting.parseSettings(settingsJSONArray.getJSONObject(i)));
                    }
                    settingController.createOrUpdateFCompanySetting(settingList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }

                /*****************end Settings**********************************************************************/
/*****************Branches*****************************************************************************/

                String comBranches = "";
                try {
                    comBranches = networkFunctions.getReferences(repcode);
                    // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
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

                // Processing company settings
                try {
                    JSONObject settingJSON = new JSONObject(comBranches);
                    JSONArray settingsJSONArray =settingJSON.getJSONArray("FCompanyBranchResult");
                    ArrayList<CompanyBranch> settingList = new ArrayList<CompanyBranch>();
                    ReferenceDetailDownloader settingController = new ReferenceDetailDownloader(ActivityLogin.this);
                    for (int i = 0; i < settingsJSONArray.length(); i++) {
                        settingList.add(CompanyBranch.parseSettings(settingsJSONArray.getJSONObject(i)));
                    }
                    settingController.createOrUpdateFCompanyBranch(settingList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }

                /*****************end Settings**********************************************************************/
                /*****************Item Loc*****************************************************************************/

                String itemLocs = "";
                try {
                    itemLocs = networkFunctions.getItemLocations(repcode);
                    // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (item location details)...");
                    }
                });

                // Processing itemLocations
                try {
                    JSONObject itemLocJSON = new JSONObject(itemLocs);
                    JSONArray settingsJSONArray =itemLocJSON.getJSONArray("fItemLocResult");
                    ArrayList<ItemLoc> itemLocList = new ArrayList<ItemLoc>();
                    ItemLocController locController = new ItemLocController(ActivityLogin.this);
                    for (int i = 0; i < settingsJSONArray.length(); i++) {
                        itemLocList.add(ItemLoc.parseItemLocs(settingsJSONArray.getJSONObject(i)));
                    }
                    locController.InsertOrReplaceItemLoc(itemLocList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }

                /*****************end Item Loc**********************************************************************/
                /*****************Locations*****************************************************************************/

                String locations = "";
                try {
                    locations = networkFunctions.getLocations(repcode);
                    // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (location details)...");
                    }
                });

                // Processing itemLocations
                try {
                    JSONObject locJSON = new JSONObject(locations);
                    JSONArray locJSONArray =locJSON.getJSONArray("fLocationsResult");
                    ArrayList<Locations> locList = new ArrayList<Locations>();
                    LocationsController locController = new LocationsController(ActivityLogin.this);
                    for (int i = 0; i < locJSONArray.length(); i++) {
                        locList.add(Locations.parseLocs(locJSONArray.getJSONObject(i)));
                    }
                    locController.createOrUpdateFLocations(locList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************Tourhed*****************************************************************************/

                String tours = "";
                try {
                    tours = networkFunctions.getTourHed(repcode);
                    // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (tour details)...");
                    }
                });

                // Processing itemLocations
                try {
                    JSONObject tourJSON = new JSONObject(tours);
                    JSONArray tourJSONArray =tourJSON.getJSONArray("FTourHedResult");
                    ArrayList<TourHed> tourList = new ArrayList<TourHed>();
                    TourController tourController = new TourController(ActivityLogin.this);
                    for (int i = 0; i < tourJSONArray.length(); i++) {
                        tourList.add(TourHed.parseTours(tourJSONArray.getJSONObject(i)));
                    }
                    tourController.createOrUpdateTourHed(tourList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end tourhed**********************************************************************/
                /*****************ItemPrice*****************************************************************************/

                String itemPrices = "";
                try {
                    itemPrices = networkFunctions.getItemPrices(repcode);
                    // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (item price details)...");
                    }
                });

                // Processing itemLocations
                try {
                    JSONObject itemPriceJSON = new JSONObject(itemPrices);
                    JSONArray itemPriceJSONArray =itemPriceJSON.getJSONArray("fItemPriResult");
                    ArrayList<ItemPri> itemPriceList = new ArrayList<ItemPri>();
                    ItemPriceController priceController = new ItemPriceController(ActivityLogin.this);
                    for (int i = 0; i < itemPriceJSONArray.length(); i++) {
                        itemPriceList.add(ItemPri.parseItemPrices(itemPriceJSONArray.getJSONObject(i)));
                    }
                    priceController.InsertOrReplaceItemPri(itemPriceList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end item prices**********************************************************************/
                /*****************Item*****************************************************************************/

                String item = "";
                try {
                    item = networkFunctions.getItems(repcode);
                    // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (item price details)...");
                    }
                });

                // Processing item price
                try {
                    JSONObject itemJSON = new JSONObject(item);
                    JSONArray itemJSONArray =itemJSON.getJSONArray("fItemsResult");
                    ArrayList<Item> itemList = new ArrayList<Item>();
                    ItemController itemController = new ItemController(ActivityLogin.this);
                    for (int i = 0; i < itemJSONArray.length(); i++) {
                        itemList.add(Item.parseItem(itemJSONArray.getJSONObject(i)));
                    }
                    itemController.InsertOrReplaceItems(itemList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end item **********************************************************************/
                /*****************fddbnote*****************************************************************************/

                String fddbnote = "";
                try {
                    fddbnote = networkFunctions.getFddbNotes(repcode);
                    // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (item price details)...");
                    }
                });

                // Processing fddbnote
                try {
                    JSONObject fddbnoteJSON = new JSONObject(fddbnote);
                    JSONArray fddbnoteJSONArray =fddbnoteJSON.getJSONArray("fDdbNoteWithConditionResult");
                    ArrayList<FddbNote> fddbnoteList = new ArrayList<FddbNote>();
                    OutstandingController outstandingController = new OutstandingController(ActivityLogin.this);
                    for (int i = 0; i < fddbnoteJSONArray.length(); i++) {
                        fddbnoteList.add(FddbNote.parseFddbnote(fddbnoteJSONArray.getJSONObject(i)));
                    }
                    outstandingController.createOrUpdateFDDbNote(fddbnoteList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************reasons**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Outstanding details downloaded\nDownloading reasons...");
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
                        JSONArray reasonJSONArray =reasonJSON.getJSONArray("fReasonResult");
                        ArrayList<Reason> reasonList = new ArrayList<Reason>();
                        ReasonController reasonController = new ReasonController(ActivityLogin.this);
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
                /*****************expenses**********************************************************************/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Reasons downloaded\nDownloading expense details...");
                    }
                });

                String expenses = "";
                try {
                    expenses = networkFunctions.getExpenses();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (expenses)...");
                    }
                });

                // Processing expense
                try {
                    JSONObject expenseJSON = new JSONObject(expenses);
                    JSONArray expenseJSONArray =expenseJSON.getJSONArray("fExpenseResult");
                    ArrayList<Expense> expensesList = new ArrayList<Expense>();
                    ExpenseController expenseController = new ExpenseController(ActivityLogin.this);
                    for (int i = 0; i < expenseJSONArray.length(); i++) {
                        expensesList.add(Expense.parseExpense(expenseJSONArray.getJSONObject(i)));
                    }
                    expenseController.createOrUpdateFExpense(expensesList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end expenses**********************************************************************/
                /*****************Route**********************************************************************/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Expenses downloaded\nDownloading route details...");
                    }
                });

                String route = "";
                try {
                    route = networkFunctions.getRoutes(repcode);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (routes)...");
                    }
                });

                // Processing route
                try {
                    JSONObject routeJSON = new JSONObject(route);
                    JSONArray routeJSONArray =routeJSON.getJSONArray("fRouteResult");
                    ArrayList<Route> routeList = new ArrayList<Route>();
                    RouteController routeController = new RouteController(ActivityLogin.this);
                    for (int i = 0; i < routeJSONArray.length(); i++) {
                        routeList.add(Route.parseRoute(routeJSONArray.getJSONObject(i)));
                    }
                    routeController.createOrUpdateFRoute(routeList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end route**********************************************************************/

                /*****************Route det**********************************************************************/

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        pdialog.setMessage("Expenses downloaded\nDownloading route details...");
//                    }
//                });

                String routedet = "";
                try {
                    routedet = networkFunctions.getRouteDets(repcode);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (routes)...");
                    }
                });

                // Processing route
                try {
                    JSONObject routeJSON = new JSONObject(routedet);
                    JSONArray routeJSONArray =routeJSON.getJSONArray("fRouteDetResult");
                    ArrayList<RouteDet> routeList = new ArrayList<RouteDet>();
                    RouteDetController routeController = new RouteDetController(ActivityLogin.this);
                    for (int i = 0; i < routeJSONArray.length(); i++) {
                        routeList.add(RouteDet.parseRoute(routeJSONArray.getJSONObject(i)));
                    }
                    routeController.InsertOrReplaceRouteDet(routeList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end route det**********************************************************************/
                /*****************Banks**********************************************************************/
                String banks = "";
                try {
                    banks = networkFunctions.getBanks();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (banks)...");
                    }
                });

                // Processing route
                try {
                    JSONObject bankJSON = new JSONObject(banks);
                    JSONArray bankJSONArray =bankJSON.getJSONArray("fbankResult");
                    ArrayList<Bank> bankList = new ArrayList<Bank>();
                    BankController bankController = new BankController(ActivityLogin.this);
                    for (int i = 0; i < bankJSONArray.length(); i++) {
                        bankList.add(Bank.parseBank(bankJSONArray.getJSONObject(i)));
                    }
                    bankController.createOrUpdateBank(bankList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end banks**********************************************************************/
                /*****************Tax**********************************************************************/
                String tax = "";
                try {
                    tax = networkFunctions.getTax();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (tax)...");
                    }
                });

                // Processing route
                try {
                    JSONObject taxJSON = new JSONObject(tax);
                    JSONArray taxJSONArray =taxJSON.getJSONArray("fTaxResult");
                    ArrayList<Tax> taxList = new ArrayList<Tax>();
                    TaxController taxController = new TaxController(ActivityLogin.this);
                    for (int i = 0; i < taxJSONArray.length(); i++) {
                        taxList.add(Tax.parseTax(taxJSONArray.getJSONObject(i)));
                    }
                    taxController.createOrUpdateTaxHed(taxList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end Tax**********************************************************************/
                /*****************TaxHed**********************************************************************/
                String taxHed = "";
                try {
                    taxHed = networkFunctions.getTaxHed();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (taxHed)...");
                    }
                });

                // Processing taxhed
                try {
                    JSONObject taxJSON = new JSONObject(taxHed);
                    JSONArray taxJSONArray =taxJSON.getJSONArray("fTaxHedResult");
                    ArrayList<TaxHed> taxList = new ArrayList<TaxHed>();
                    TaxHedController taxController = new TaxHedController(ActivityLogin.this);
                    for (int i = 0; i < taxJSONArray.length(); i++) {
                        taxList.add(TaxHed.parseTaxHed(taxJSONArray.getJSONObject(i)));
                    }
                    taxController.createOrUpdateTaxHed(taxList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end Tax**********************************************************************/
                /*****************TaxDet**********************************************************************/
                String taxDet = "";
                try {
                    taxDet = networkFunctions.getTaxDet();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (taxDet)...");
                    }
                });

                // Processing route
                try {
                    JSONObject taxJSON = new JSONObject(taxDet);
                    JSONArray taxJSONArray =taxJSON.getJSONArray("fTaxDetResult");
                    ArrayList<TaxDet> taxList = new ArrayList<TaxDet>();
                    TaxDetController taxController = new TaxDetController(ActivityLogin.this);
                    for (int i = 0; i < taxJSONArray.length(); i++) {
                        taxList.add(TaxDet.parseTaxDet(taxJSONArray.getJSONObject(i)));
                    }
                    taxController.createOrUpdateTaxDet(taxList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end Tax**********************************************************************/
                /*****************Freeslab**********************************************************************/
                String freeslab = "";
                try {
                    freeslab = networkFunctions.getFreeSlab();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (free)...");
                    }
                });

                // Processing freeslab
                try {
                    JSONObject freeslabJSON = new JSONObject(freeslab);
                    JSONArray freeslabJSONArray =freeslabJSON.getJSONArray("FfreeslabResult");
                    ArrayList<FreeSlab> freeslabList = new ArrayList<FreeSlab>();
                    FreeSlabController freeslabController = new FreeSlabController(ActivityLogin.this);
                    for (int i = 0; i < freeslabJSONArray.length(); i++) {
                        freeslabList.add(FreeSlab.parseFreeSlab(freeslabJSONArray.getJSONObject(i)));
                    }
                    freeslabController.createOrUpdateFreeSlab(freeslabList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end freeSlab**********************************************************************/
                /*****************Freemslab**********************************************************************/
                String freeMslab = "";
                try {
                    freeMslab = networkFunctions.getFreeMslab();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (free)...");
                    }
                });

                // Processing freeMslab
                try {
                    JSONObject freeMslabJSON = new JSONObject(freeMslab);
                    JSONArray taxJSONArray =freeMslabJSON.getJSONArray("fFreeMslabResult");
                    ArrayList<FreeMslab> freeMslabList = new ArrayList<FreeMslab>();
                    FreeMslabController freeMslabController = new FreeMslabController(ActivityLogin.this);
                    for (int i = 0; i < taxJSONArray.length(); i++) {
                        freeMslabList.add(FreeMslab.parseFreeMslab(taxJSONArray.getJSONObject(i)));
                    }
                    freeMslabController.createOrUpdateFreeMslab(freeMslabList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end freeMSlab**********************************************************************/

                /*****************FreeHed**********************************************************************/
                String freehed = "";
                try {
                    freehed = networkFunctions.getFreeHed(repcode);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (free)...");
                    }
                });

                // Processing freehed
                try {
                    JSONObject freeHedJSON = new JSONObject(freehed);
                    JSONArray freeHedJSONArray =freeHedJSON.getJSONArray("FfreehedResult");
                    ArrayList<FreeHed> freeHedList = new ArrayList<FreeHed>();
                    FreeHedController freeHedController = new FreeHedController(ActivityLogin.this);
                    for (int i = 0; i < freeHedJSONArray.length(); i++) {
                        freeHedList.add(FreeHed.parseFreeHed(freeHedJSONArray.getJSONObject(i)));
                    }
                    freeHedController.createOrUpdateFreeHed(freeHedList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end freeHed**********************************************************************/
                /*****************Freedet**********************************************************************/
                String freedet = "";
                try {
                    freedet = networkFunctions.getFreeDet();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (free)...");
                    }
                });

                // Processing freeMslab
                try {
                    JSONObject freedetJSON = new JSONObject(freedet);
                    JSONArray freedetJSONArray =freedetJSON.getJSONArray("FfreedetResult");
                    ArrayList<FreeDet> freedetList = new ArrayList<FreeDet>();
                    FreeDetController freedetController = new FreeDetController(ActivityLogin.this);
                    for (int i = 0; i < freedetJSONArray.length(); i++) {
                        freedetList.add(FreeDet.parseFreeDet(freedetJSONArray.getJSONObject(i)));
                    }
                    freedetController.createOrUpdateFreeDet(freedetList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end freedet**********************************************************************/
                /*****************Freeslab**********************************************************************/
                String freedeb = "";
                try {
                    freedeb = networkFunctions.getFreeDebs();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (free)...");
                    }
                });

                // Processing freedeb
                try {
                    JSONObject freedebJSON = new JSONObject(freedeb);
                    JSONArray freedebJSONArray =freedebJSON.getJSONArray("FfreedebResult");
                    ArrayList<FreeDeb> freedebList = new ArrayList<FreeDeb>();
                    FreeDebController freedebController = new FreeDebController(ActivityLogin.this);
                    for (int i = 0; i < freedebJSONArray.length(); i++) {
                        freedebList.add(FreeDeb.parseFreeDeb(freedebJSONArray.getJSONObject(i)));
                    }
                    freedebController.createOrUpdateFreeDeb(freedebList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end freeSlab**********************************************************************/
                /*****************Freeslab**********************************************************************/
                String freeitem = "";
                try {
                    freeitem = networkFunctions.getFreeItems();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (free)...");
                    }
                });

                // Processing freeMslab
                try {
                    JSONObject freeitemJSON = new JSONObject(freeitem);
                    JSONArray freeitemJSONArray =freeitemJSON.getJSONArray("fFreeItemResult");
                    ArrayList<FreeItem> freeitemList = new ArrayList<FreeItem>();
                    FreeItemController freeitemController = new FreeItemController(ActivityLogin.this);
                    for (int i = 0; i < freeitemJSONArray.length(); i++) {
                        freeitemList.add(FreeItem.parseFreeItem(freeitemJSONArray.getJSONObject(i)));
                    }
                    freeitemController.createOrUpdateFreeItem(freeitemList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end freeSlab**********************************************************************/
                /*****************STKin**********************************************************************/
                String stkin = "";
                try {
                    stkin = networkFunctions.getStkIn(repcode);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (stock)...");
                    }
                });

                // Processing stkin
                try {
                    JSONObject stkInJSON = new JSONObject(stkin);
                    JSONArray stkInJSONArray =stkInJSON.getJSONArray("FStkInResult");
                    ArrayList<StkIn> stkInList = new ArrayList<StkIn>();
                    STKInController stkInListController = new STKInController(ActivityLogin.this);
                    for (int i = 0; i < stkInJSONArray.length(); i++) {
                        stkInList.add(StkIn.parseStkIn(stkInJSONArray.getJSONObject(i)));
                    }
                    stkInListController.createUpdateSTKIn(stkInList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end stkin**********************************************************************/
                /*****************debitem**********************************************************************/
                String debItem = "";
                try {
                    debItem = networkFunctions.getDebItemPrices();
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

                // Processing stkin
                try {
                    JSONObject debItemPriJSON = new JSONObject(debItem);
                    JSONArray debItemPriJSONArray =debItemPriJSON.getJSONArray("fDebItemPriResult");
                    ArrayList<DebItemPri> debItemPriList = new ArrayList<DebItemPri>();
                    DebItemPriController debItemPriController = new DebItemPriController(ActivityLogin.this);
                    for (int i = 0; i < debItemPriJSONArray.length(); i++) {
                        debItemPriList.add(DebItemPri.parseDebPri(debItemPriJSONArray.getJSONObject(i)));
                    }
                    debItemPriController.createOrUpdateDebItemPri(debItemPriList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end debitem**********************************************************************/

                /*****************discdeb**********************************************************************/
                String debdisc = "";
                try {
                    debdisc = networkFunctions.getDiscDeb(repcode);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (discount)...");
                    }
                });

                // Processing discdeb
                try {
                    JSONObject discdebPriJSON = new JSONObject(debdisc);
                    JSONArray discdebJSONArray = discdebPriJSON.getJSONArray("FdiscdebResult");
                    ArrayList<Discdeb> discdebList = new ArrayList<Discdeb>();
                    DiscdebController discdebController = new DiscdebController(ActivityLogin.this);
                    for (int i = 0; i < discdebJSONArray.length(); i++) {
                        discdebList.add(Discdeb.parseDiscDeb(discdebJSONArray.getJSONObject(i)));
                    }
                    discdebController.createOrUpdateDiscdeb(discdebList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end discdeb**********************************************************************/
                /*****************discdet**********************************************************************/
                String discdet = "";
                try {
                    discdet = networkFunctions.getDiscDet();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (discount)...");
                    }
                });

                // Processing discdeb
                try {
                    JSONObject discdetJSON = new JSONObject(discdet);
                    JSONArray discdetJSONArray = discdetJSON.getJSONArray("FdiscdetResult");
                    ArrayList<Discdet> discdetList = new ArrayList<Discdet>();
                    DiscdetController discdetController = new DiscdetController(ActivityLogin.this);
                    for (int i = 0; i < discdetJSONArray.length(); i++) {
                        discdetList.add(Discdet.parseDiscDet(discdetJSONArray.getJSONObject(i)));
                    }
                    discdetController.createOrUpdateDiscdet(discdetList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end discdet**********************************************************************/
                /*****************discshed**********************************************************************/
                String disched = "";
                try {
                    disched = networkFunctions.getDiscHed(repcode);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (discount)...");
                    }
                });

                // Processing discdeb
                try {
                    JSONObject dischedJSON = new JSONObject(disched);
                    JSONArray dischedJSONArray = dischedJSON.getJSONArray("FDischedResult");
                    ArrayList<Disched> dischedList = new ArrayList<Disched>();
                    DischedController dischedController = new DischedController(ActivityLogin.this);
                    for (int i = 0; i < dischedJSONArray.length(); i++) {
                        dischedList.add(Disched.parseDisched(dischedJSONArray.getJSONObject(i)));
                    }
                    dischedController.createOrUpdateDisched(dischedList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end discdet**********************************************************************/
                /*****************discslab**********************************************************************/
                String discslab = "";
                try {
                    discslab = networkFunctions.getDiscSlab();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (discount)...");
                    }
                });

                // Processing discslab
                try {
                    JSONObject discslabJSON = new JSONObject(discslab);
                    JSONArray discslabJSONArray = discslabJSON.getJSONArray("FdiscslabResult");
                    ArrayList<Discslab> discslabList = new ArrayList<Discslab>();
                    DiscslabController discslabController = new DiscslabController(ActivityLogin.this);
                    for (int i = 0; i < discslabJSONArray.length(); i++) {
                        discslabList.add(Discslab.parseDiscslab(discslabJSONArray.getJSONObject(i)));
                    }
                    discslabController.createOrUpdateDiscslab(discslabList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end discslab**********************************************************************/
                /*****************last 3 invoice heds**********************************************************************/
                String last3InvHeds = "";
                try {
                    last3InvHeds = networkFunctions.getLastThreeInvHed(repcode);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (invoices)...");
                    }
                });

                // Processing lastinvoiceheds
                try {
                    JSONObject invoiceHedJSON = new JSONObject(last3InvHeds);
                    JSONArray invoiceHedJSONJSONArray = invoiceHedJSON.getJSONArray("RepLastThreeInvHedResult");
                    ArrayList<FInvhedL3> invoiceHedList = new ArrayList<FInvhedL3>();
                    FInvhedL3DS invoiceHedController = new FInvhedL3DS(ActivityLogin.this);
                    for (int i = 0; i < invoiceHedJSONJSONArray.length(); i++) {
                        invoiceHedList.add(FInvhedL3.parseInvoiceHeds(invoiceHedJSONJSONArray.getJSONObject(i)));
                    }
                    invoiceHedController.createOrUpdateFinvHedL3(invoiceHedList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end lastinvoiceheds**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Processing downloaded data (invoices)...");
                    }
                });

                // Processing lastinvoiceheds
                try {
                    JSONObject invoiceHedJSON = new JSONObject(discslab);
                    JSONArray invoiceHedJSONJSONArray = invoiceHedJSON.getJSONArray("RepLastThreeInvHedResult");
                    ArrayList<FInvhedL3> invoiceHedList = new ArrayList<FInvhedL3>();
                    FInvhedL3DS invoiceHedController = new FInvhedL3DS(ActivityLogin.this);
                    for (int i = 0; i < invoiceHedJSONJSONArray.length(); i++) {
                        invoiceHedList.add(FInvhedL3.parseInvoiceHeds(invoiceHedJSONJSONArray.getJSONObject(i)));
                    }
                    invoiceHedController.createOrUpdateFinvHedL3(invoiceHedList);
                } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                    throw e;
                }
                /*****************end lastinvoiceheds**********************************************************************/
                    return true;
//        } else {
//            //errors.add("Please enter correct username and password");
//            return false;
//        }
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
                Intent intent = new Intent(ActivityLogin
                        .this, ActivityHome
                        .class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ActivityLogin.this,"Invalid Response from server",Toast.LENGTH_LONG);
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
        }
    }

    public static String getMD5HashVal(String strToBeEncrypted) {
        String encryptedString = null;
        byte[] bytesToBeEncrypted;

        try {
            // convert string to bytes using a encoding scheme
            bytesToBeEncrypted = strToBeEncrypted.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theDigest = md.digest(bytesToBeEncrypted);
            // convert each byte to a hexadecimal digit
            Formatter formatter = new Formatter();
            for (byte b : theDigest) {
                formatter.format("%02x", b);
            }
            encryptedString = formatter.toString().toLowerCase();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedString;
    }
    private class Validate extends AsyncTask<String, Integer, Boolean> {
        int totalRecords=0;
        CustomProgressDialog pdialog;
        private String macId,url;

        public Validate(String macId,String url){
            this.macId = macId;
            this.url = url;
            this.pdialog = new CustomProgressDialog(ActivityLogin.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Validating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {

            try {
                int recordCount = 0;
                int totalBytes  = 0;
                String validateResponse = null;
                JSONObject validateJSON;
                try {
                    validateResponse = networkFunctions.validate(ActivityLogin.this,macId,pref.getBaseURL());
                    Log.d("validateResponse",validateResponse);
                    validateJSON = new JSONObject(validateResponse);


                    if (validateJSON != null) {
                        pref = SharedPref.getInstance(ActivityLogin.this);
                        //dbHandler.clearTables();
                        // Login successful. Proceed to download other items

                        JSONArray repArray = validateJSON.getJSONArray("fSalRepResult");
                        ArrayList<SalRep> salRepList = new ArrayList<>();
                        for (int i = 0; i<repArray.length(); i++){
                            JSONObject expenseJSON = repArray.getJSONObject(i);
                            salRepList.add(SalRep.parseUser(expenseJSON));
                        }
                        new SalRepController(getApplicationContext()).createOrUpdateSalRep(salRepList);
                        User user = User.parseUser(repArray.getJSONObject(0));
                        networkFunctions.setUser(user);
                        pref.storeLoginUser(user);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pdialog.setMessage("Authenticated...");
                            }
                        });

                        return true;
                    }else{
                        Toast.makeText(ActivityLogin.this,"Invalid response from server when getting sales rep data",Toast.LENGTH_SHORT).show();
                        return false;
                    }

                } catch (IOException e) {
                    Log.e("networkFunctions ->","IOException -> "+e.toString());
                    throw e;
                } catch (JSONException e) {
                    Log.e("networkFunctions ->","JSONException -> "+e.toString());
                    throw e;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//            pDialog.setMessage("Prefetching data..." + progress[0] + "/" + totalRecords);
//
//        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pdialog.isShowing())
                pdialog.cancel();
            // pdialog.cancel();
            if(result){
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                pref.setValidateStatus(true);
                //tryAgain.setVisibility(View.INVISIBLE);
                //set user details to shared prefferences
                //Intent mainActivity = new Intent(ActivitySplash.this, SettingsActivity.class);
                // .................. Nuwan ....... commented due to run home activity .............. 19/06/2019
                Intent loginActivity = new Intent(ActivityLogin.this, ActivityLogin.class);
                //  Intent loginActivity = new Intent(ActivitySplash.this, ActivityHome.class);
                // ..............................................................................................
//
                startActivity(loginActivity);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Invalid response from server", Toast.LENGTH_LONG).show();
                //tryAgain.setVisibility(View.VISIBLE);
//temerary set for new SFA
                // .................. Nuwan ....... commented due to run home activity .............. 19/06/2019
                //Intent loginActivity = new Intent(ActivitySplash.this, ActivityLogin.class);
//                Intent loginActivity = new Intent(ActivitySplash.this, ActivityHome.class);
//                // ..............................................................................................
//                startActivity(loginActivity);
//                finish();

            }
        }
    }
}
