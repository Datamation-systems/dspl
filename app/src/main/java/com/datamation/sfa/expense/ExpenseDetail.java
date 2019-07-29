package com.datamation.sfa.expense;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import com.datamation.sfa.adapter.SalesExpenseDetailAdapter;
import com.datamation.sfa.adapter.SalesExpenseGridDetails;
import com.datamation.sfa.R;
import com.datamation.sfa.controller.ExpenseController;
import com.datamation.sfa.controller.SalRepController;
import com.datamation.sfa.model.Expense;
import com.datamation.sfa.settings.ReferenceNum;

import com.datamation.sfa.controller.DayExpDetController;
import com.datamation.sfa.controller.DayExpHedController;
import com.datamation.sfa.controller.ReasonController;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.DayExpDet;
import com.datamation.sfa.model.DayExpHed;
import com.datamation.sfa.model.Reason;
import com.datamation.sfa.utils.UtilityContainer;
import com.datamation.sfa.view.ActivityHome;
import com.datamation.sfa.view.FragmentTools;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


import java.sql.Ref;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseDetail extends Fragment implements OnClickListener {
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    public static String DebCODE, RESULT;
    View view;
    EditText Txndate, Amount, ExpCode, Remark, RefNo;
    ArrayList<Expense> list2;
    Button btnAdd;
    ListView lv_invent_load;
    ReferenceNum referenceNum;
    ArrayList<DayExpDet> loadlist;
    int seqno = 0;
    String sExpenseCode;
    FloatingActionButton fabPause, fabDiscard, fabSave;
    FloatingActionMenu fam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expense_hed, container, false);
        //setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        getActivity().setTitle("Expence Details");

        seqno = 0;
        fam = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        fabPause = (FloatingActionButton) view.findViewById(R.id.fab2);
        fabDiscard = (FloatingActionButton) view.findViewById(R.id.fab3);
        fabSave = (FloatingActionButton) view.findViewById(R.id.fab1);

        RefNo = (EditText) view.findViewById(R.id._refNo);
        Txndate = (EditText) view.findViewById(R.id._date);
        Remark = (EditText) view.findViewById(R.id._remrk);
        Amount = (EditText) view.findViewById(R.id._amount);
        ExpCode = (EditText) view.findViewById(R.id._expcode);
        Button ReSearch = (Button) view.findViewById(R.id.reason_search);
        referenceNum = new ReferenceNum(getActivity());
        RefNo.setText(referenceNum.getCurrentRefNo(getResources().getString(R.string.ExpenseNumVal)));
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        lv_invent_load = (ListView) view.findViewById(R.id.lv_loading_sum);
        btnAdd.setOnClickListener(this);
        //fatchData();


        ReSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.reason_search:
                        ExpenseDetailsDialogbox("");
                        break;
                    default:
                        break;
                }

            }
        });

		/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

        lv_invent_load.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
                DayExpDet fexpdet = loadlist.get(position);
                deleteOrderDialog(getActivity(), fexpdet.getEXPDET_REFNO().toString(), fexpdet.getEXPDET_EXPCODE().toString());
                return false;
            }
        });

		/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

        lv_invent_load.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {

                clearTextFields();
                btnAdd.setText("UPDATE");
                DayExpDet fnondet = loadlist.get(position);
                Amount.setText(fnondet.getEXPDET_AMOUNT());
                ExpCode.setText(new ReasonController(getActivity()).getReasonByReaCode(fnondet.getEXPDET_EXPCODE()));
                sExpenseCode = fnondet.getEXPDET_EXPCODE();
            }
        });

        currentDate();

        fam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });
        fam.setClosedOnTouchOutside(true);
        fabPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //mPauseExpense();
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
                undoEditingData(getActivity(), RefNo.getText().toString());
            }
        });


        return view;
    }

	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

    private void currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Txndate.setText(dateFormat.format(date));
    }

	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

    public void ExpenseDetailsDialogbox(String searchword) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.nonprod_retailer_search);
        final SearchView search = (SearchView) dialog.findViewById(R.id.rt_search);
        final ListView locList = (ListView) dialog.findViewById(R.id.rt_product_items);
        dialog.setCancelable(true);

        list2 = new ExpenseController(getActivity()).getAllExpense("");
        locList.clearTextFilter();
        locList.setAdapter(new SalesExpenseDetailAdapter(getActivity(), list2));
        locList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpCode.setText(list2.get(position).getFEXPENSE_NAME());
                sExpenseCode = list2.get(position).getFEXPENSE_CODE();
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
                locList.clearTextFilter();
                list2 = new ExpenseController(getActivity()).getAllExpense(newText);
                locList.setAdapter(new SalesExpenseDetailAdapter(getActivity(), list2));
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

                if (Amount.getText().length() > 0 && ExpCode.getText().length() > 0) {

                    DayExpDet expdet = new DayExpDet();
                    DayExpDetController expDetDS = new DayExpDetController(getActivity());
                    ArrayList<DayExpDet> ExpDetList = new ArrayList<DayExpDet>();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();

                    try {

                        seqno++;
                        expdet.setEXPDET_REFNO(RefNo.getText() + "");
                        expdet.setEXPDET_EXPCODE(sExpenseCode);
                        expdet.setEXPDET_AMOUNT(Amount.getText() + "");
                        expdet.setEXPDET_TXNDATE(dateFormat.format(date));

                        ExpDetList.add(expdet);

                        if (expDetDS.createOrUpdateExpenseDet(ExpDetList) > 0) {
                            clearTextFields();
                            Toast.makeText(getActivity(), "Added successfully", Toast.LENGTH_LONG).show();
                            fatchData();

                        } else {
                            Toast.makeText(getActivity(), "record save Unsuccessful", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "Added Not successfully", Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;
        }
    }

	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

    public void fatchData() {
        try {
            DayExpDetController detDS = new DayExpDetController(getActivity());
            //loadlist = detDS.getAllExpDetails(referenceNum.getCurrentRefNo(getResources().getString(R.string.ExpenseNumVal)));
            loadlist = detDS.getAllExpDetails(RefNo.getText().toString());
            lv_invent_load.setAdapter(new SalesExpenseGridDetails(getActivity(), loadlist));
        } catch (NullPointerException e) {
            Log.v("Loading Error", e.toString());
        }
    }

	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

    public void clearTextFields() {
        Remark.setText("");
        Amount.setText("");
        ExpCode.setText("");
        sExpenseCode = "";

    }

	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

    private void saveSummaryDialog(final Context context) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure you want to save this entry?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (new DayExpDetController(getActivity()).getAllExpDetails(RefNo.getText() + "").size() > 0) {

                    DayExpHed exphed = new DayExpHed();
                    ArrayList<DayExpHed> ExpHedList = new ArrayList<DayExpHed>();

                    String refno = RefNo.getText().toString();
                    exphed.setEXPHED_REFNO(RefNo.getText() + "");
                    exphed.setEXPHED_REPCODE(new SalRepController(getActivity()).getCurrentRepCode().trim());
                    exphed.setEXPHED_TXNDATE(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    exphed.setEXPHED_REMARK(Remark.getText() + "");
                    exphed.setEXPHED_ADDDATE(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    exphed.setEXPHED_ACTIVESTATE("0");
                    exphed.setEXPHED_IS_SYNCED("0");
                    exphed.setEXPHED_LATITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Latitude").equals("***") ? "0.00" : SharedPref.getInstance(getActivity()).getGlobalVal("Latitude"));
                    exphed.setEXPHED_LONGITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Longitude").equals("***") ? "0.00" : SharedPref.getInstance(getActivity()).getGlobalVal("Longitude"));
                   // exphed.setEXPHED_ADDRESS(localSP.getString("GPS_Address", "").toString());
                    exphed.setEXPHED_TOTAMT(new DayExpDetController(getActivity()).getTotalExpenseSumReturns(refno));
                    ExpHedList.add(exphed);

                    if (new DayExpHedController(getActivity()).createOrUpdateDayExpHed(ExpHedList) > 0) {
                        referenceNum.nNumValueInsertOrUpdate(getResources().getString(R.string.ExpenseNumVal));
                        Toast.makeText(getActivity(), "Successfully saved Expense. ", Toast.LENGTH_LONG).show();
//                        UtilityContainer.mLoadFragment(new FragmentTools(), getActivity());
                        Intent intent = new Intent(getActivity(), ActivityHome.class);
                        startActivity(intent);
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

	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

    private void deleteOrderDialog(final Context context, final String refno, final String expcode) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Are you sure you want to delete this entry?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                int count = new DayExpDetController(context).DeleteRec(refno, expcode);
                if (count > 0) {
                    Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_LONG).show();

                    fatchData();
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

	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

    private void undoEditingData(final Context context, final String RefNo) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure you want to Undo this entry?");
        final DayExpHedController expHED = new DayExpHedController(getActivity());
        final DayExpDetController expDET = new DayExpDetController(getActivity());
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                ReferenceNum referenceNum = new ReferenceNum(getActivity());

                try {
                    //expHED.undoExpHedByID(referenceNum.getCurrentRefNo(getResources().getString(R.string.ExpenseNumVal))); // FExpHed
                    expHED.undoExpHedByID(RefNo); // FExpHed
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    //expDET.ExpDetByID(referenceNum.getCurrentRefNo(getResources().getString(R.string.ExpenseNumVal))); // FExpDet
                    expDET.ExpDetByID(RefNo); // FExpDet
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                UtilityContainer.mLoadFragment(new FragmentTools(), getActivity());
                Intent intent = new Intent(getActivity(), ActivityHome.class);
                startActivity(intent);
                getActivity().finish();
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

	
	/*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*/

    public void onTaskCompleted(String result) {
        try {
            if (!result.equals("") || !result.equals("No Address")) {


               // activity.selectedexpHed.setEXPHED_ADDRESS(result);

            }
        } catch (Exception e) {
            Log.v("Selected OrdHed", e.toString());
        }
    }
/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void mPauseExpense() {

        if (new DayExpDetController(getActivity()).getExpenceCount(RefNo.getText().toString().trim()) > 0)
        {
            UtilityContainer.mLoadFragment(new FragmentTools(), getActivity());
        }

        else
        {
            Toast.makeText(getActivity(), "Add expenses before pause ...!", Toast.LENGTH_SHORT).show();
        }

    }

}
