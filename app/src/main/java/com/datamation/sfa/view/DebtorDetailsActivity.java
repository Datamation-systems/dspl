package com.datamation.sfa.view;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import at.markushi.ui.CircleButton;
import com.astuetz.PagerSlidingTabStrip;
import com.datamation.sfa.customer.CustomerRegMain;
import com.datamation.sfa.expense.ExpenseMain;
import com.datamation.sfa.fragment.debtordetails.HistoryDetailsFragment;
import com.datamation.sfa.fragment.debtorlist.AllCustomerFragment;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.fragment.debtordetails.CompetitorDetailsFragment;
import com.datamation.sfa.fragment.debtordetails.OutstandingDetailsFragment;
import com.datamation.sfa.fragment.debtordetails.PersonalDetailsFragment;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.User;

public class DebtorDetailsActivity extends AppCompatActivity {

    //private CircleButton floatingActionsMenu;
    private CircleButton fabInvoice, fabUnproductive, fabReturnNote, fabSalesOrder, fabVansale;
    //private TextView labelInvoice, labelUnproductive, labelReturnNote, labelSalesOrder, labelVanSale;

    private Customer outlet;
    private Context context;

    private View overlay;

    private boolean famOpen = false;

    private DatabaseHelper dbHandler;
    private SharedPref sharedPref;

    private boolean invoiced = false;

    private PersonalDetailsFragment personalDetailsFragment;
    private OutstandingDetailsFragment outstandingDetailsFragment;
    private HistoryDetailsFragment historyDetailsFragment;
    private CompetitorDetailsFragment competitorDetailsFragment;

    private LocationManager locManager;

    private int[] famDisplayIntervals = {0, 100, 200, 300, 400, 500, 600, 700, 800, 900};

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtor_details);

        dbHandler = new DatabaseHelper(this);
        sharedPref = SharedPref.getInstance(DebtorDetailsActivity.this);

        user = sharedPref.getLoginUser();
        context = this;

        locManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        outlet = new Customer();
        outlet.setCusName("ABCD DISTRIBUTORS");

        Toolbar toolbar = (Toolbar) findViewById(R.id.outlet_details_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText(sharedPref.getSelectedDebName());


        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.outlet_details_tab_strip);
        ViewPager viewPager = (ViewPager) findViewById(R.id.outlet_details_viewpager);

        fabVansale = (CircleButton)findViewById(R.id.outlet_details_fab_van_sale);
        fabInvoice = (CircleButton)findViewById(R.id.outlet_details_fab_receipt);
        fabUnproductive = (CircleButton)findViewById(R.id.outlet_details_fab_non_productive);
        fabReturnNote = (CircleButton)findViewById(R.id.outlet_details_fab_sales_return);
        fabSalesOrder = (CircleButton) findViewById(R.id.outlet_details_fab_pre_sale);

//        labelVanSale = (TextView)findViewById(R.id.outlet_details_label_vansale);
//        labelInvoice = (TextView)findViewById(R.id.outlet_details_label_invoice);
//        labelUnproductive = (TextView)findViewById(R.id.outlet_details_label_non_productive);
//        labelReturnNote = (TextView)findViewById(R.id.outlet_details_label_sales_return);
//        labelSalesOrder = (TextView)findViewById(R.id.outlet_details_label_pre_sale);

        // Setting the expanded options button colors
//        fabSalesOrder.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.blueColor));
//        fabInvoice.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.blueColor));
//        fabUnproductive.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.blueColor));
//        fabReturnNote.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.blueColor));
//        fabVansale.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.blueColor));

        // Setting the expanded options button drawables
        fabSalesOrder.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.circle_ic_sales));
        fabInvoice.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.circle_ic_receipt));
        fabUnproductive.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.circle_ic_nonprod));
        fabReturnNote.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.circle_ic_return));
        fabVansale.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.circle_ic_expensive));

        // The overlay when showing expanding the menu
        overlay = findViewById(R.id.outlet_details_view_overlay);
        overlay.setAlpha(1);

        openFAM();

        // Collapsing the FAM when pressed on the overlay
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(famOpen){
                    closeFAM();
                }
            }
        });

        OutletDetailsPagerAdapter adapter = new OutletDetailsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);

        slidingTabStrip.setViewPager(viewPager);
        slidingTabStrip.setIndicatorColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.colorPrimaryDark));

        fabUnproductive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeFAM();

                // Only proceed if location service is available
                if (locationServiceEnabled()) {
                    Intent intent = new Intent(DebtorDetailsActivity.this, NonProductiveActivity.class);
                    intent.putExtra("outlet", "");
                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service to proceed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeFAM();

                // Only proceed if location service is available
                if(locationServiceEnabled())
                {
                    Toast.makeText(DebtorDetailsActivity.this, "Please wait. This may take a while", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DebtorDetailsActivity.this, VanSalesActivity.class);
                    intent.putExtra("outlet", "");
                    intent.putExtra("sales_order", false);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fabReturnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFAM();

                // Only proceed if location service is available
                if(locationServiceEnabled()){
                    Toast.makeText(DebtorDetailsActivity.this, "Please wait. This may take a while", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DebtorDetailsActivity.this, SalesReturnActivity.class);
                    intent.putExtra("outlet", "");
                    intent.putExtra("sales_return", false);
                    startActivity(intent);
                } else {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only proceed if location service is available
                if(locationServiceEnabled())
                {
                    Toast.makeText(DebtorDetailsActivity.this, "Please wait. This may take a while", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DebtorDetailsActivity.this, PreSalesActivity.class);
                    intent.putExtra("outlet", "");
                    intent.putExtra("sales_order", false);
                    startActivity(intent);
                }
                else
                    {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean locationServiceEnabled() {
        boolean gpsActive;
        boolean networkActive;

        try {
            gpsActive = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            gpsActive = false;
        }

        try {
            networkActive = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            networkActive = false;
        }

        return networkActive || gpsActive;
    }

    /**
     * Function to open the FAM with custom flowing animation
     */
    private void openFAM() {
        //ViewCompat.animate(floatingActionsMenu).rotation(135).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(0);
//        ViewPropertyAnimator.animate(floatingActionsMenu).rotation(135).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(0);
        ViewCompat.animate(overlay).alpha(1).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

                fabSalesOrder.setVisibility(View.VISIBLE);
                fabInvoice.setVisibility(View.VISIBLE);
                fabReturnNote.setVisibility(View.VISIBLE);
                fabUnproductive.setVisibility(View.VISIBLE);
                fabVansale.setVisibility(View.VISIBLE);

//                labelUnproductive.setVisibility(View.VISIBLE);
//                labelSalesOrder.setVisibility(View.VISIBLE);
//                labelInvoice.setVisibility(View.VISIBLE);
//                labelReturnNote.setVisibility(View.VISIBLE);
//                labelVanSale.setVisibility(View.VISIBLE);

                overlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(View view) {

            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });

        int index = 0;

        if(!invoiced) {
        }

        famOpen = true;
    }

    /**
     * Function to close the FAM with custom flowing animation
     */
    private void closeFAM() {
//        if(invoiced){
//            ViewCompat.animate(floatingActionsMenu).rotation(0).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(400);
//        } else {
//            ViewCompat.animate(floatingActionsMenu).rotation(0).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(500);
//        }

        ViewCompat.animate(overlay).alpha(0).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                overlay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });

        int index = 0;

        famOpen = false;
    }


    private class OutletDetailsPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"OUTSTANDING", "PERSONNEL", "HISTORY", "COMPETITORS"};

        public OutletDetailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    if(outstandingDetailsFragment == null) outstandingDetailsFragment = new OutstandingDetailsFragment();
                    return outstandingDetailsFragment;
                case 1:
                    if(personalDetailsFragment == null) personalDetailsFragment = new PersonalDetailsFragment();
                    return personalDetailsFragment;
                case 2:
                    if(historyDetailsFragment == null) historyDetailsFragment = new HistoryDetailsFragment();
                    return historyDetailsFragment;
                case 3:
                    if(competitorDetailsFragment == null) competitorDetailsFragment = new CompetitorDetailsFragment();
                    return competitorDetailsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

    @Override
    public void onBackPressed() {
        if(famOpen) {
            closeFAM();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}





