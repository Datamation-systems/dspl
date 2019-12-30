package com.datamation.sfa.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.adapter.CustomerRegListAdapter;
import com.datamation.sfa.adapter.DistrictAdapter;
import com.datamation.sfa.adapter.RouteAdapter;
import com.datamation.sfa.adapter.TownAdapter;
import com.datamation.sfa.controller.CustomerController;
import com.datamation.sfa.controller.DistrictController;
import com.datamation.sfa.controller.NewCustomerController;
import com.datamation.sfa.controller.ReferenceDetailDownloader;
import com.datamation.sfa.controller.RouteController;
import com.datamation.sfa.controller.TownController;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.District;
import com.datamation.sfa.model.NewCustomer;
import com.datamation.sfa.model.Route;
import com.datamation.sfa.model.Town;
import com.datamation.sfa.settings.GPSTracker;
import com.datamation.sfa.settings.ReferenceNum;
import com.datamation.sfa.utils.NetworkUtil;
import com.datamation.sfa.view.ActivityHome;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.markushi.ui.CircleButton;

public class NewCustomerActivity extends AppCompatActivity {

    public EditText customerCode,
            customerName, editTextCNic, OtherCode, businessRegno, district,
            town, route, addressline1, addressline2, city, mobile, phone, fax, emailaddress,Reg_date;
    public ImageButton btn_Route, btn_District, btn_Town, CustomerbtnSearch;
    private ArrayList<Route> routeArrayList;
    private ArrayList<Town> townArrayList;
    private ArrayList<District> districtArrayList;
    private ArrayList<NewCustomer> newCustomerArrayList;
    ArrayList<Uri> uris = new ArrayList<>();
    SharedPref mSharedPref;
    Switch mySwitch;
    private ReferenceNum referenceNum;
    private String jsonstr;
    private ProgressDialog progressDialog;
    public static SharedPreferences localSP;
    public static final String SETTINGS = "SETTINGS";
    ActivityHome home;
    CircleButton fabSave, fabDiscard;
    int CUSFLG = 1;
    private String nCustomerNo;
    private ImageView customerImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        Toolbar toolbar = (Toolbar)findViewById(R.id.new_cus_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("ABCD");

        mSharedPref = SharedPref.getInstance(this);
        referenceNum = new ReferenceNum(this);
        localSP = this.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);
        fabSave = (CircleButton) findViewById(R.id.new_cus_fab_save) ;
        fabDiscard = (CircleButton) findViewById(R.id.new_cus_fab_discard) ;
        customerCode = (TextInputEditText) findViewById(R.id.etRefNo);
        customerName = (TextInputEditText) findViewById(R.id.etCustomer_Name);
        editTextCNic = (TextInputEditText) findViewById(R.id.etCusNIC);
        Reg_date = (TextInputEditText) findViewById(R.id.etDate);
        businessRegno = (TextInputEditText) findViewById(R.id.etRegNo);
        addressline1 = (TextInputEditText) findViewById(R.id.etAddLine_1);
        addressline2 = (TextInputEditText) findViewById(R.id.etAddLine_2);
        city = (TextInputEditText) findViewById(R.id.etCity);
        mobile = (TextInputEditText) findViewById(R.id.etCusMobile_No);
        phone = (TextInputEditText) findViewById(R.id.etCusPhone_No);
        fax = (TextInputEditText) findViewById(R.id.etFax);
        emailaddress = (TextInputEditText) findViewById(R.id.etEmail);
        route = (TextInputEditText) findViewById(R.id.etRoute);
        district = (TextInputEditText) findViewById(R.id.etDistrict);
        town = (TextInputEditText) findViewById(R.id.etTown);


        customerImg = (ImageView) findViewById(R.id.ivCus_Photo);
        fabSave.setColor(ContextCompat.getColor(this, R.color.main_green_color));
        fabSave.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_save_icon));

        fabDiscard.setColor(ContextCompat.getColor(this, R.color.main_green_color));
        fabDiscard.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_undo_icon));

        btn_Town = (ImageButton) findViewById(R.id.btnTown);
        btn_Route = (ImageButton) findViewById(R.id.btnRoute);
        btn_District = (ImageButton) findViewById(R.id.btnDistrict);
        CustomerbtnSearch = (ImageButton) findViewById(R.id.btnSearch);

        mySwitch = (Switch)findViewById(R.id.switch1);
        //-----------------------------------------------------------------------------------------------------------------

        //show new customer ref no
        if (mySwitch.isChecked())
        {
            CustomerbtnSearch.setEnabled(false);
            fabSave.setEnabled(true);
            fabDiscard.setEnabled(true);
            try {
                nCustomerNo = referenceNum.getCurrentRefNo(getResources().getString(R.string.newCusVal));
                customerCode.setText(nCustomerNo);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            CustomerbtnSearch.setEnabled(true);
            fabSave.setEnabled(false);
            fabDiscard.setEnabled(false);
        }


        btn_District.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog(1);

            }
        });
        btn_Town.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog(2);
            }
        });
        btn_Route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog(3);
            }
        });


        //get old customers
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    referenceNum = new ReferenceNum(getApplicationContext());
                    try {
                        nCustomerNo = referenceNum.getCurrentRefNo(getResources().getString(R.string.newCusVal));
                        customerCode.setText(nCustomerNo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    CUSFLG = 1;
                    customerName.setFocusable(true);
                    customerName.setEnabled(true);
                    customerName.setClickable(true);
                    customerName.setFocusableInTouchMode(true);

                    CustomerController TW = new CustomerController(getApplicationContext());

                    customerName.setEnabled(true);
                    editTextCNic.setEnabled(true);
                    OtherCode.setEnabled(true);
                    businessRegno.setEnabled(true);
                    addressline1.setEnabled(true);
                    addressline2.setEnabled(true);
                    mobile.setEnabled(true);
                    phone.setEnabled(true);
                    fax.setEnabled(true);
                    emailaddress.setEnabled(true);
                    route.setEnabled(true);
                    district.setEnabled(true);
                    town.setEnabled(true);

                    city.setEnabled(true);
                    btn_Town.setEnabled(true);
                    btn_Route.setEnabled(true);
                    btn_District.setEnabled(true);

                    customerName.setText("");
                    editTextCNic.setText("");
                    //OtherCode.setText("");
                    businessRegno.setText("");
                    addressline1.setText("");
                    addressline2.setText("");
                    city.setText("");
                    mobile.setText("");
                    phone.setText("");
                    fax.setText("");
                    emailaddress.setText("");
                    route.setText("");
                    district.setText("");
                    town.setText("");


                } else {
                    //WHEN NEW CUSTOMER MODE OFF
                    customerName.setFocusable(false);
                    customerName.setEnabled(false);
                    customerName.setClickable(false);
                    customerName.setFocusableInTouchMode(false);


                    customerName.setEnabled(false);
                    editTextCNic.setEnabled(false);
                    OtherCode.setEnabled(false);
                    businessRegno.setEnabled(false);
                    addressline1.setEnabled(false);
                    addressline2.setEnabled(false);
                    mobile.setEnabled(false);
                    phone.setEnabled(false);
                    fax.setEnabled(false);
                    emailaddress.setEnabled(false);
                    route.setEnabled(false);
                    district.setEnabled(false);
                    town.setEnabled(false);
                    city.setEnabled(false);
                    btn_Town.setEnabled(false);
                    btn_Route.setEnabled(false);
                    btn_District.setEnabled(false);
                    OtherCode.setText("");
                    CUSFLG = 0;
                    customerCode.setText("");
                }
            }
        });

        //save new customer
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CUSFLG == 1) {

                    // if (customerName.getText().length() != 0 && addressline1.getText().length() != 0 && addressline2.getText().length() != 0 && mobile.getText().length() != 0 && town.getText().length() != 0 && route.getText().length() != 0 && city.getText().length() != 0) {
                    if (customerName.getText().length() != 0 && addressline1.getText().length() != 0 && addressline2.getText().length() != 0 && mobile.getText().length() != 0) {

                        if (!isEmailValid(emailaddress.getText().toString()))
                        {
                            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {

                            //RouteController RO = new RouteController(getApplicationContext());

                            DateFormat Dformat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();

                            //SalRepDS fSalRepDS = new SalRepDS(getActivity());
                            //ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(getApplicationContext());

                            GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                            referenceNum = new ReferenceNum(getApplicationContext());

                            NewCustomer customer = new NewCustomer();
                            customer.setC_REGNUM(businessRegno.getText().toString());
                            customer.setNAME(customerName.getText().toString());
                            customer.setCUSTOMER_NIC(editTextCNic.getText().toString());
                            customer.setCUSTOMER_ID(customerCode.getText().toString());
                            customer.setROUTE_ID(route.getText().toString());
                            customer.setC_TOWN(town.getText().toString());
                            customer.setDISTRICT(district.getText().toString());
                            customer.setADDRESS1(addressline1.getText().toString());
                            customer.setADDRESS2(addressline2.getText().toString());
                            customer.setCITY(city.getText().toString());
                            customer.setMOBILE(mobile.getText().toString());
                            customer.setPHONE(phone.getText().toString());
                            customer.setFAX(fax.getText().toString());
                            customer.setE_MAIL(emailaddress.getText().toString());
                            customer.setC_SYNCSTATE("N");
                            customer.setAddMac("NA");
                            customer.setC_ADDDATE(Dformat.format(date));
                            customer.setC_LATITUDE("" + gpsTracker.getLatitude());
                            customer.setC_LONGITUDE("" + gpsTracker.getLongitude());
                            //customer.setnNumVal(referenceNum.getCurrentRefNo(getResources().getString(R.string.newCusVal)));
                            customer.setnNumVal("1");
                            customer.setTxnDate(Dformat.format(date));
                            customer.setCONSOLE_DB(localSP.getString("Console_DB", "").toString());

                            ArrayList<NewCustomer> cusList = new ArrayList<>();
                            cusList.add(customer);

                            NewCustomerController customerDS = new NewCustomerController(getApplicationContext());
                            int result = customerDS.createOrUpdateCustomer(cusList);

                            if (result > 0) {

                                Toast.makeText(getApplicationContext(), "New Customer saved", Toast.LENGTH_SHORT).show();
                                ClearFiled();

                                //insert current NC number for next num generation
                                referenceNum = new ReferenceNum(getApplicationContext());
                                referenceNum.NumValueUpdate(getResources().getString(R.string.newCusVal));
                                try {
                                    if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                                        //upload to master
                                        // new UploadNewCustomer(getActivity(), AddNewCusRegistration.this, UPLOAD_NEW_CUSTOMER, cusList).execute();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception e){
                                    Log.v("CUSTOMER REG>>>>","ERROR.....");
                                    e.printStackTrace();
                                }
                            }

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please fill all compulsory fields", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        fabDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ClearFiled();
            }
        });

        //get old customer for update record
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    customerName.requestFocus();
                    customerCode.setText(referenceNum.getCurrentRefNo(getResources().getString(R.string.newCusVal)));
                    CustomerbtnSearch.setEnabled(false);

                    editTextCNic.setEnabled(true);
                    customerCode.setEnabled(true);
                    customerName.setEnabled(true);
//                    OtherCode.setEnabled(true);
                    businessRegno.setEnabled(true);
                    btn_District.setEnabled(true);
                    btn_Town.setEnabled(true);
                    btn_Route.setEnabled(true);
                    addressline1.setEnabled(true);
                    addressline2.setEnabled(true);
                    city.setEnabled(true);
                    mobile.setEnabled(true);
                    phone.setEnabled(true);
                    fax.setEnabled(true);
                    emailaddress.setEnabled(true);
                } else {
                    CustomerbtnSearch.setEnabled(true);

                    customerCode.setText("");
                    customerCode.setEnabled(false);
                    customerName.setEnabled(false);
                    editTextCNic.setEnabled(false);
                    businessRegno.setEnabled(false);
                    btn_District.setEnabled(false);
                    btn_Town.setEnabled(false);
                    btn_Route.setEnabled(false);
                    addressline1.setEnabled(false);
                    addressline2.setEnabled(false);
                    city.setEnabled(false);
                    mobile.setEnabled(false);
                    phone.setEnabled(false);
                    fax.setEnabled(false);
                    emailaddress.setEnabled(false);
                }
            }
        });
    }

    public void ClearFiled() {
        customerName.setText("");
        editTextCNic.setText("");
//        OtherCode.setText("");
        businessRegno.setText("");
        district.setText("");
        town.setText("");
        route.setText("");
        addressline1.setText("");
        addressline2.setText("");
        city.setText("");
        mobile.setText("");
        phone.setText("");
        fax.setText("");
        emailaddress.setText("");

    }


    private void exitData() {
        // UtilityContainer.mLoadFragment(new CustomerRegMain(), getActivity());
        android.widget.Toast.makeText(getApplicationContext(), "Success", android.widget.Toast.LENGTH_LONG).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("OnResume", "oo");

    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public void popupDialog(final int Flag) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.details_search_item);
        dialog.setCancelable(true);

        final SearchView search = (SearchView) dialog.findViewById(R.id.et_search);
        final ListView Detlist = (ListView) dialog.findViewById(R.id.lv_product_list);

        final RouteController routeDS = new RouteController(this);
        final DistrictController distDS = new DistrictController(this);
        final TownController townDS = new TownController(this);

        Detlist.clearTextFilter();
        if (Flag == 1)
        {
            districtArrayList = distDS.getAllDistrict();
            Detlist.setAdapter(new DistrictAdapter(this, districtArrayList));
        }
        else if (Flag == 2)
        {
            townArrayList = townDS.getAllTowns();
            Detlist.setAdapter(new TownAdapter(this, townArrayList));
        }
        else if (Flag == 3)
        {
            routeArrayList = routeDS.getRoute();
            Detlist.setAdapter(new RouteAdapter(this, routeArrayList));
        }

        Detlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Flag == 3)
                {
                    route.setText(routeArrayList.get(position).getFROUTE_ROUTECODE());
                }
                else if (Flag == 2)
                {
                    town.setText(townArrayList.get(position).getTownCode());
                }
                else if (Flag == 1)
                {
                    district.setText(districtArrayList.get(position).getDistrictCode());
                }

                dialog.dismiss();
            }
        });


        dialog.show();
    }

}
