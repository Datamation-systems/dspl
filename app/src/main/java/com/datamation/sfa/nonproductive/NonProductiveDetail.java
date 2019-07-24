package com.datamation.sfa.nonproductive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import com.datamation.sfa.adapter.NonPrdDetails;
import com.datamation.sfa.adapter.NonProductiveReasonAdapter;
import com.datamation.sfa.controller.CustomerController;
import com.datamation.sfa.controller.DayNPrdDetController;
import com.datamation.sfa.controller.DayNPrdHedController;
import com.datamation.sfa.controller.ReasonController;
import com.datamation.sfa.controller.SalRepController;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.DayNPrdDet;
import com.datamation.sfa.model.DayNPrdHed;
import com.datamation.sfa.model.Reason;
import com.datamation.sfa.settings.ReferenceNum;
import com.datamation.sfa.utils.UtilityContainer;
import com.datamation.sfa.view.ActivityHome;
import com.datamation.sfa.view.DebtorDetailsActivity;
import com.datamation.sfa.view.NonProductiveActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*created by rashmi*/
public class NonProductiveDetail extends Fragment implements OnClickListener{
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    View view;
    EditText  Retailer, Remark, RefNo;
    Button ReSearch;
    TextView Txndate,TxtReason;
    ArrayList<Reason> list2;
    Button btnAdd;
    ListView lv_invent_load;
    ReferenceNum referenceNum;
    ArrayList<DayNPrdDet> loadlist;
    String sRefno;
    FloatingActionMenu fam;
    FloatingActionButton fabPause, fabDiscard, fabSave;
    SharedPref mSharedPref;
    Activity thisActivity;
    NonProductiveActivity mainActivity;
    private Customer outlet;

    MyReceiver r;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.non_productive_details, container, false);

        //final ActivityHome activity = (ActivityHome) getActivity();
        thisActivity = getActivity();
        mainActivity = (NonProductiveActivity)getActivity();
        lv_invent_load = (ListView) view.findViewById(R.id.lv_loading_sum);
        RefNo = (EditText) view.findViewById(R.id._refNo);
        Txndate = (TextView) view.findViewById(R.id._date);
        Remark = (EditText) view.findViewById(R.id._remrk);
        Retailer = (EditText) view.findViewById(R.id._retailer);
        TxtReason = (TextView) view.findViewById(R.id._reason);
        ReSearch = (Button) view.findViewById(R.id.reason_search);
        referenceNum = new ReferenceNum(getActivity());

        btnAdd = (Button) view.findViewById(R.id.btn_add);
        fam = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        fabPause = (FloatingActionButton) view.findViewById(R.id.fab2);
        fabDiscard = (FloatingActionButton) view.findViewById(R.id.fab3);
        fabSave = (FloatingActionButton) view.findViewById(R.id.fab1);

        mSharedPref = SharedPref.getInstance(getActivity());
        RefNo.setText(referenceNum.getCurrentRefNo(getResources().getString(R.string.nonprdVal)));
        Retailer.setText(mSharedPref.getSelectedDebName());

        btnAdd.setOnClickListener(this);
        ReSearch.setOnClickListener(this);

        fam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });

        fabSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSummaryDialog(getActivity());
            }
        });

        fabDiscard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                undoEditingData(getActivity());
            }
        });



		/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        lv_invent_load.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //new InvDetController(getActivity()).restFreeIssueData(RefNo);
                deleteNonProDialog(position);
                return true;
            }
        });

		/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        lv_invent_load.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {

                clearTextFields();
                btnAdd.setText("UPDATE");

                DayNPrdDet fnondet = loadlist.get(position);
                CustomerController customer = new CustomerController(getActivity());
//                Customer cus = customer.getSelectedCustomerByCode(fnondet.getNonprddet_d)
//                Retailer.setText(new CustomerController.getS(fnondet.getNONPRDDET_DEBCODE()));
//                DebCODE = fnondet.getNONPRDDET_DEBCODE().toString();
                TxtReason.setText(fnondet.getNONPRDDET_REASON());

            }
        });

        currentDate();
        return view;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private void currentDate() {
        Txndate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


    public void ReasonDetailsDialogbox(String searchword) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.nonprod_retailer_search);
        final SearchView search = (SearchView) dialog.findViewById(R.id.rt_search);
        final ListView locList = (ListView) dialog.findViewById(R.id.rt_product_items);
        dialog.setCancelable(true);

        list2 = new ReasonController(getActivity()).getAllNonPrdReasons();
        locList.clearTextFilter();
        locList.setAdapter(new NonProductiveReasonAdapter(getActivity(), list2));
        locList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TxtReason.setText(list2.get(position).getFREASON_NAME());
                dialog.dismiss();
            }
        });

        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ReasonController locds = new ReasonController(getActivity());
                locList.clearTextFilter();
                list2 = locds.getDebDetails(newText);
                locList.setAdapter(new NonProductiveReasonAdapter(getActivity(), list2));
                return false;
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {

            case R.id.btn_add:

                btnAdd.setText("ADD");
                if (Retailer.getText().length() != 0 && TxtReason.getText().length() != 0) {

                    DayNPrdDet nondet = new DayNPrdDet();
                    DayNPrdDetController nonDetDS = new DayNPrdDetController(getActivity());
                    ArrayList<DayNPrdDet> NONDetList = new ArrayList<DayNPrdDet>();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();

                    try {
                        nondet.setNONPRDDET_REFNO(RefNo.getText() + "");
                        nondet.setNONPRDDET_REPCODE(new SalRepController(getActivity()).getCurrentRepCode().trim());
                        nondet.setNONPRDDET_TXNDATE(dateFormat.format(date));
                        nondet.setNONPRDDET_REASON(TxtReason.getText() + "");
//                        nondet.setNONPRDDET_LONGITUDE(new SharedPref(getActivity()).getGlobalVal("Longitude"));
//                        nondet.setNONPRDDET_LATITUDE(new SharedPref(getActivity()).getGlobalVal("Latitude"));

                        nondet.setNONPRDDET_REASON_CODE(new ReasonController(getActivity()).getReaCodeByName(TxtReason.getText() + ""));
                        nondet.setNONPRDDET_IS_SYNCED("0");
                        NONDetList.add(nondet);

                        if (nonDetDS.createOrUpdateNonPrdDet(NONDetList) > 0) {
                            clearTextFields();
                            Toast.makeText(getActivity(), "Added successfully", Toast.LENGTH_LONG).show();
                            fatchData(RefNo.getText().toString().trim() + "");

                        } else {
                            Toast.makeText(getActivity(), "Addition unsuccessfully", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "Added Not successfully", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.reason_search:
                ReasonDetailsDialogbox("");
                break;
            default:
                break;
        }
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void fatchData(String sRefno) {

        try {

            DayNPrdDetController detDS = new DayNPrdDetController(getActivity());
            loadlist = detDS.getAllnonprdDetails(sRefno);
            lv_invent_load.setAdapter(new NonPrdDetails(getActivity(), loadlist));
        } catch (NullPointerException e) {
            Log.v("Loading Error", e.toString());
        }
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void clearTextFields() {
        Remark.setText("");
       // Retailer.setText("");
        TxtReason.setText("");
      //  DebCODE = "";

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private void saveSummaryDialog(final Context context) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Are you sure you want to save this entry?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //ActivityHome activity = (ActivityHome) getActivity();
                DayNPrdDetController deb = new DayNPrdDetController(getActivity());
                if (deb.getAllnonprdDetails(RefNo.getText() + "").size() > 0) {
                    // new AddressAyncTask(getActivity(),this).execute();

                    DayNPrdHed nonhed = new DayNPrdHed();
                    ArrayList<DayNPrdHed> NONHedList = new ArrayList<DayNPrdHed>();
                    DayNPrdHedController nonHedDS = new DayNPrdHedController(getActivity());

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();

                    nonhed.setNONPRDHED_REFNO(RefNo.getText() + "");
                    nonhed.setNONPRDHED_REPCODE(new SalRepController(getActivity()).getCurrentRepCode().trim());
                    nonhed.setNONPRDHED_TXNDATE(dateFormat.format(date));
                    nonhed.setNONPRDHED_TRANSBATCH("");
                    nonhed.setNONPRDHED_REMARK(Remark.getText() + "");
                    nonhed.setNONPRDHED_ADDDATE(dateFormat.format(date));
                    //nonhed.setNONPRDHED_ADDMACH(localSP.getString("MAC_Address", "No MAC Address").toString());
                    nonhed.setNONPRDHED_LONGITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Longitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Longitude"));
                    nonhed.setNONPRDHED_LATITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Latitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Latitude"));
                    nonhed.setNONPRDHED_ADDUSER(new SalRepController(getActivity()).getCurrentRepCode().trim());
                    nonhed.setNONPRDHED_COSTCODE("");
                    nonhed.setNONPRDHED_DEALCODE("");
                    nonhed.setNONPRDHED_IS_SYNCED("0");
                    nonhed.setNONPRDHED_DEBCODE(SharedPref.getInstance(getActivity()).getGlobalVal("NonkeyCusCode"));
                    //nonhed.setNONPRDHED_ADDRESS(localSP.getString("GPS_Address", "").toString());
                    NONHedList.add(nonhed);



                    if (nonHedDS.createOrUpdateNonPrdHed(NONHedList) > 0) {
                        new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.nonprdVal));

                        outlet = new CustomerController(context).getSelectedCustomerByCode(mSharedPref.getSelectedDebCode());
//                        activity.cusPosition = 0;
//                        activity.selectedDebtor = null;
                       // UtilityContainer.ClearNonSharedPref(getActivity());
                        Toast.makeText(getActivity(), "Non Productive saved successfully !", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                        intent.putExtra("outlet",outlet);
                        startActivity(intent);
                        getActivity().finish();
                    }

                } else {
                    Toast.makeText(getActivity(), "No Data For Save", Toast.LENGTH_LONG).show();
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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//    private void deleteOrderDialog(final Context context, final String _id) {
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        // alertDialogBuilder.setTitle(title);
//        alertDialogBuilder.setMessage("Are you sure you want to delete this entry?");
//        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
//                int count = new DayNPrdDetController(context).deleteOrdDetByID(_id);
//                if (count > 0) {
//                    Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_LONG).show();
//                    fatchData(_id);
//                    clearTextFields();
//                }
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//
//            }
//        });
//
//        AlertDialog alertD = alertDialogBuilder.create();
//
//        alertD.show();
//    }

    private void deleteNonProDialog(final int position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Are you sure you want to delete this entry?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                int count = new DayNPrdDetController(getActivity()).deleteOrdDetByID(loadlist.get(position).getNONPRDDET_REFNO(), loadlist.get(position).getNONPRDDET_REASON_CODE());
                if (count > 0) {
                    Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_LONG).show();
                    fatchData(loadlist.get(position).getNONPRDDET_REFNO());
                    clearTextFields();
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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private void undoEditingData(final Context context) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure you want to Undo this entry?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                try {
                    new DayNPrdHedController(getActivity()).undoOrdHedByID(RefNo.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    new DayNPrdDetController(getActivity()).OrdDetByRefno(RefNo.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //activity.cusPosition = 0;
                mainActivity.selectedNonDebtor = null;
                Intent intnt = new Intent(getActivity(),DebtorDetailsActivity.class);
                startActivity(intnt);
                getActivity().finish();
                //UtilityContainer.ClearNonSharedPref(getActivity());
                Toast.makeText(getActivity(), "Undo Success", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();

            }
        });

        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    @Override
    public void onResume() {
        super.onResume();
        r = new NonProductiveDetail.MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_NP_DETAILS"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    public void mRefreshHeader() {
        mSharedPref = SharedPref.getInstance(getActivity());
        Log.d("TEST_NON_PRO", "123456");
        RefNo.setText(referenceNum.getCurrentRefNo(getResources().getString(R.string.nonprdVal)));
        CustomerController debtor = new CustomerController(getActivity());
        //Log.d("DEBTOR CHECK",debtor.getCustNameByCode(new SharedPref(getActivity()).getGlobalVal("NonkeyCusCode")));
        Customer cus = debtor.getSelectedCustomerByCode(SharedPref.getInstance(getActivity()).getGlobalVal("NonkeyCusCode"));
        if (mSharedPref.getGlobalVal("NonkeyCustomer").equals("Y")) {
            //RefNo.setText(sRefno);
            btnAdd.setEnabled(true);
            ReSearch.setEnabled(true);
            Remark.setEnabled(true);

            Retailer.setText(cus.getCusName());


        } else {
            Toast.makeText(getActivity(), "Select a customer to continue...", Toast.LENGTH_SHORT).show();
            RefNo.setEnabled(false);
            Retailer.setEnabled(false);
            btnAdd.setEnabled(false);
            //btnDeliDate.setEnabled(false);
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NonProductiveDetail.this.mRefreshHeader();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        this.thisActivity = activity;
        super.onAttach(activity);
    }

}
