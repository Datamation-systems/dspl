package com.datamation.sfa.view;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.sfa.R;
import at.markushi.ui.CircleButton;
import com.astuetz.PagerSlidingTabStrip;
import com.datamation.sfa.fragment.debtordetails.HistoryDetailsFragment;
import com.datamation.sfa.helpers.SharedPref;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.fragment.debtordetails.CompetitorDetailsFragment;
import com.datamation.sfa.fragment.debtordetails.OutstandingDetailsFragment;
import com.datamation.sfa.fragment.debtordetails.PersonalDetailsFragment;
import com.datamation.sfa.helpers.DatabaseHelper;
import com.datamation.sfa.model.User;


public class DebtorDetailsActivity extends AppCompatActivity {

    private CircleButton floatingActionsMenu;
    private CircleButton fabInvoice, fabUnproductive, fabReturnNote, fabPayment, fabSalesOrder;
    private TextView labelInvoice, labelUnproductive, labelReturnNote, labelPayment, labelSalesOrder;

    private Customer outlet;

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

        locManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        outlet = new Customer();
        outlet.setCusName("ABCD DISTRIBUTORS");

//        Intent dataIntent = getIntent();
//        if(dataIntent.hasExtra("outlet")){
////            outlet = (Customer) dataIntent.getExtras().get("outlet");
//            outlet.setCusName("ABCD");
//            if(outlet == null) {
//                Toast.makeText(DebtorDetailsActivity.this, "Error receiving the outlet. Please try again.", Toast.LENGTH_SHORT).show();
//                onBackPressed();
//
//            }
//        } else {
//            Toast.makeText(DebtorDetailsActivity.this, "Error receiving the outlet. Please try again.", Toast.LENGTH_SHORT).show();
//            onBackPressed();
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.outlet_details_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText(outlet.getCusName());

        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.outlet_details_tab_strip);
        ViewPager viewPager = (ViewPager) findViewById(R.id.outlet_details_viewpager);

        floatingActionsMenu = (CircleButton)findViewById(R.id.outlet_details_floating_action_menu);
        floatingActionsMenu.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.colorPrimary));
        floatingActionsMenu.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.fab_add));

        fabInvoice = (CircleButton)findViewById(R.id.outlet_details_fab_invoice);
        fabUnproductive = (CircleButton)findViewById(R.id.outlet_details_fab_unproductive);
        fabReturnNote = (CircleButton)findViewById(R.id.outlet_details_fab_merchandising);
        fabPayment = (CircleButton)findViewById(R.id.outlet_details_fab_payment);
        fabSalesOrder = (CircleButton) findViewById(R.id.outlet_details_fab_sales_order);

        labelInvoice = (TextView)findViewById(R.id.outlet_details_label_invoice);
        labelUnproductive = (TextView)findViewById(R.id.outlet_details_label_unproductive);
        labelReturnNote = (TextView)findViewById(R.id.outlet_details_label_merchandising);
        labelPayment = (TextView)findViewById(R.id.outlet_details_label_payment);
        labelSalesOrder = (TextView)findViewById(R.id.outlet_details_label_sales_order);

        // Make the label sized small so they can be scaled up.
//        labelSalesOrder.setScaleX(0);
//        labelSalesOrder.setScaleY(0);
//
//        labelInvoice.setScaleX(0);
//        labelInvoice.setScaleY(0);
//
//        labelUnproductive.setScaleX(0);
//        labelUnproductive.setScaleY(0);
//
//        labelReturnNote.setScaleX(0);
//        labelReturnNote.setScaleY(0);
//
//        labelPayment.setScaleX(0);
//        labelPayment.setScaleY(0);

        // Set the visibility to visible to the animation will show
//        labelInvoice.setVisibility(View.VISIBLE);
//        labelUnproductive.setVisibility(View.VISIBLE);
//        labelReturnNote.setVisibility(View.VISIBLE);
//        labelPayment.setVisibility(View.VISIBLE);

        // Setting the expanded options button colors
        fabSalesOrder.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.md_divider_white));
        fabInvoice.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.md_divider_white));
        fabUnproductive.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.md_divider_white));
        fabReturnNote.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.md_divider_white));
        fabPayment.setColor(ContextCompat.getColor(DebtorDetailsActivity.this, R.color.md_divider_white));

        // Setting the expanded options button drawables
        fabSalesOrder.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.fab_add));
        fabInvoice.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.fab_add));
        fabUnproductive.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.fab_add));
        fabReturnNote.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.fab_add));
        fabPayment.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.fab_add));

        // Scale down the invisible FAB options
//        fabSalesOrder.setScaleX(0);
//        fabSalesOrder.setScaleY(0);
//
//        fabInvoice.setScaleX(0);
//        fabInvoice.setScaleY(0);
//
//        fabUnproductive.setScaleX(0);
//        fabUnproductive.setScaleY(0);
//
//        fabReturnNote.setScaleX(0);
//        fabReturnNote.setScaleY(0);
//
//        fabPayment.setScaleX(0);
//        fabPayment.setScaleY(0);

        // Make the invisible FABs visible to show the expand animation
//        fabInvoice.setVisibility(View.VISIBLE);
//        fabUnproductive.setVisibility(View.VISIBLE);
//        fabReturnNote.setVisibility(View.VISIBLE);
//        fabPayment.setVisibility(View.VISIBLE);

        // Hide the unproductive call FAB if an invoice is created
//        if(visitStatus == VisitDetail.STATUS_INVOICED) {
//            fabUnproductive.setVisibility(View.GONE);
//            labelUnproductive.setVisibility(View.GONE);
//        }

        // Setting the FAM drawable
        floatingActionsMenu.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.fab_add));

        // The overlay when showing expanding the menu
        overlay = findViewById(R.id.outlet_details_view_overlay);
        overlay.setAlpha(0);

        // Expanding/Collapsing the FAM when pressed on the FAM
        floatingActionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(famOpen) {
                    closeFAM();
                } else {
                    openFAM();
                }
            }
        });

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
                    Intent intent = new Intent(DebtorDetailsActivity.this, DebtorDetailsActivity.class);
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
                if(locationServiceEnabled()){
                    Toast.makeText(DebtorDetailsActivity.this, "Please wait. This may take a while", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DebtorDetailsActivity.this, DebtorDetailsActivity.class);
                    intent.putExtra("outlet", "");
                    intent.putExtra("sales_order", false);
                    startActivity(intent);
//                    finish();
                } else {
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
                    Intent intent = new Intent(DebtorDetailsActivity.this, DebtorDetailsActivity.class);
                    intent.putExtra("outlet", "");
//                    intent.putExtra("sales_order", false);
                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fabPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeFAM();

                // Only proceed if location service is available
                if(locationServiceEnabled()){
                    Intent intent = new Intent(DebtorDetailsActivity.this, DebtorDetailsActivity.class);
                    intent.putExtra("outlet", "");
                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        fabSalesOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Only proceed if location service is available
//                if(locationServiceEnabled()){
//                    Toast.makeText(DealerDetailsActivity.this, "Please wait. This may take a while", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(DealerDetailsActivity.this, NewSalesOrderActivity.class);
//                    intent.putExtra("outlet", outlet);
//                    intent.putExtra("sales_order", true);
//                    startActivity(intent);
////                    finish();
//                } else {
//                    Toast.makeText(DealerDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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
        ViewCompat.animate(floatingActionsMenu).rotation(135).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(0);
//        ViewPropertyAnimator.animate(floatingActionsMenu).rotation(135).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(0);
        ViewCompat.animate(overlay).alpha(1).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                // Set the visibility to visible so the animation will be visible

//                fabReturnNote.setVisibility(View.VISIBLE);
//                fabPayment.setVisibility(View.VISIBLE);
//
//                labelReturnNote.setVisibility(View.VISIBLE);
//                labelPayment.setVisibility(View.VISIBLE);

                fabSalesOrder.setVisibility(View.VISIBLE);
                fabPayment.setVisibility(View.VISIBLE);
                //fabUnproductive.setVisibility(View.VISIBLE);
                labelSalesOrder.setVisibility(View.VISIBLE);
                labelPayment.setVisibility(View.VISIBLE);
                //labelUnproductive.setVisibility(View.VISIBLE);

                overlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(View view) {

            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });

        // Scale in the FABs one by oneViewCompat.animate(fabPayment).scaleX(1).scaleY(1).setDuration(200).setStartDelay(0);

        int index = 0;

//        ViewCompat.animate(labelPayment).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//        ViewCompat.animate(fabPayment).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//        index++;
//
//        ViewCompat.animate(fabReturnNote).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//        ViewCompat.animate(labelReturnNote).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//        index++;

        if(!invoiced) {
//            fabUnproductive.setVisibility(View.VISIBLE);
//            labelUnproductive.setVisibility(View.VISIBLE);
//
//
//            ViewCompat.animate(fabUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            ViewCompat.animate(labelUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            index++;
        }

//        if(user.getSalesType() == User.TYPE_INVOICE || user.getSalesType() == User.TYPE_BOTH) {
//            fabInvoice.setVisibility(View.VISIBLE);
//            labelInvoice.setVisibility(View.VISIBLE);
//            ViewCompat.animate(fabInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            ViewCompat.animate(labelInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            index++;
//        }

//        if(user.getSalesType() == User.TYPE_SALES_ORDER || user.getSalesType() == User.TYPE_BOTH) {
//            fabSalesOrder.setVisibility(View.VISIBLE);
//            labelSalesOrder.setVisibility(View.VISIBLE);
//            ViewCompat.animate(fabSalesOrder).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            ViewCompat.animate(labelSalesOrder).scaleX(1).scaleY(1).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//        }

//        if(invoiced){
//            ViewCompat.animate(fabReturnNote).scaleX(1).scaleY(1).setDuration(200).setStartDelay(100);
//            ViewCompat.animate(labelReturnNote).scaleX(1).scaleY(1).setDuration(200).setStartDelay(100);

//            ViewCompat.animate(fabUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);
//            ViewCompat.animate(labelUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);

//            ViewCompat.animate(fabInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);
//            ViewCompat.animate(labelInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);

//            ViewCompat.animate(fabSalesOrder).scaleX(1).scaleY(1).setDuration(200).setStartDelay(300);
//            ViewCompat.animate(labelSalesOrder).scaleX(1).scaleY(1).setDuration(200).setStartDelay(300);
//        } else {
//            ViewCompat.animate(fabReturnNote).scaleX(1).scaleY(1).setDuration(200).setStartDelay(100);
//            ViewCompat.animate(labelReturnNote).scaleX(1).scaleY(1).setDuration(200).setStartDelay(100);

//            ViewCompat.animate(fabUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);
//            ViewCompat.animate(labelUnproductive).scaleX(1).scaleY(1).setDuration(200).setStartDelay(200);

//            ViewCompat.animate(fabInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(300);
//            ViewCompat.animate(labelInvoice).scaleX(1).scaleY(1).setDuration(200).setStartDelay(300);

//            ViewCompat.animate(fabSalesOrder).scaleX(1).scaleY(1).setDuration(200).setStartDelay(400);
//            ViewCompat.animate(labelSalesOrder).scaleX(1).scaleY(1).setDuration(200).setStartDelay(400);
//        }

        famOpen = true;
    }

    /**
     * Function to close the FAM with custom flowing animation
     */
    private void closeFAM() {
        if(invoiced){
            ViewCompat.animate(floatingActionsMenu).rotation(0).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(400);
        } else {
            ViewCompat.animate(floatingActionsMenu).rotation(0).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(500);
        }

        ViewCompat.animate(overlay).alpha(0).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                overlay.setVisibility(View.GONE);
                // Set the visibility to visible to the animation will show
//                labelSalesOrder.setVisibility(View.GONE);
//                labelInvoice.setVisibility(View.GONE);
//                labelUnproductive.setVisibility(View.GONE);
//                labelReturnNote.setVisibility(View.GONE);
//                labelPayment.setVisibility(View.GONE);

//                fabSalesOrder.setVisibility(View.GONE);
//                fabInvoice.setVisibility(View.GONE);
//                fabUnproductive.setVisibility(View.GONE);
//                fabReturnNote.setVisibility(View.GONE);
//                fabPayment.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });

        int index = 0;

        // Scale out the FABs one by one
//        if(user.getSalesType() == User.TYPE_SALES_ORDER || user.getSalesType() == User.TYPE_BOTH) {
//            ViewCompat.animate(fabSalesOrder).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            ViewCompat.animate(labelSalesOrder).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            index++;
//        }
//
//        if(user.getSalesType() == User.TYPE_INVOICE || user.getSalesType() == User.TYPE_BOTH) {
//            ViewCompat.animate(fabInvoice).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            ViewCompat.animate(labelInvoice).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            index++;
//        }

//        if(!invoiced) {
//            ViewCompat.animate(fabUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            ViewCompat.animate(labelUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//            index++;
//        }
//
//        ViewCompat.animate(fabReturnNote).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//        ViewCompat.animate(labelReturnNote).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//        index++;
//
//        ViewCompat.animate(fabPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);
//        ViewCompat.animate(labelPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(famDisplayIntervals[index]);

//        ViewCompat.animate(fabInvoice).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);
//        ViewCompat.animate(labelInvoice).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);

//        if(invoiced) {
//            ViewCompat.animate(fabUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);
//            ViewCompat.animate(labelUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(100);

//            ViewCompat.animate(fabReturnNote).scaleX(0).scaleY(0).setDuration(200).setStartDelay(200);
//            ViewCompat.animate(labelReturnNote).scaleX(0).scaleY(0).setDuration(200).setStartDelay(200);

//            ViewCompat.animate(fabPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(300);
//            ViewCompat.animate(labelPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(300);
//        } else {
//            ViewCompat.animate(fabUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(200);
//            ViewCompat.animate(labelUnproductive).scaleX(0).scaleY(0).setDuration(200).setStartDelay(200);

//            ViewCompat.animate(fabReturnNote).scaleX(0).scaleY(0).setDuration(200).setStartDelay(300);
//            ViewCompat.animate(labelReturnNote).scaleX(0).scaleY(0).setDuration(200).setStartDelay(300);

//            ViewCompat.animate(fabPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(400);
//            ViewCompat.animate(labelPayment).scaleX(0).scaleY(0).setDuration(200).setStartDelay(400);
//        }


        famOpen = false;
    }


    private class OutletDetailsPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"PERSONAL", "OUTSTANDING", "HISTORY", "COMPETITORS"};

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
                    if(personalDetailsFragment == null) personalDetailsFragment = new PersonalDetailsFragment();
                    return personalDetailsFragment;
                case 1:
                    if(outstandingDetailsFragment == null) outstandingDetailsFragment = new OutstandingDetailsFragment();
                    return outstandingDetailsFragment;
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
//            startActivity(new Intent(DealerDetailsActivity.this, WelcomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if(outlet != null) {
//            List<Integer> visitStatuses = dbHandler.getVisitStatusOfOutlet(sharedPref.getLocalSessionId(), outlet.getOutletId());
//            if(visitStatuses != null) {
//                invoiced = false;
//                for(int i=0; i< visitStatuses.size(); i++) {
//                    if(visitStatuses.get(i) == VisitDetail.STATUS_INVOICED){
//                        invoiced = true;
//                        break;
//                    }
//                }
//
//                if(invoiced) {
//                    fabUnproductive.setVisibility(View.GONE);
//                    labelUnproductive.setVisibility(View.GONE);
//                }
//            }
//
//        }

//        if(outstandingDetailsFragment != null) {
//            outstandingDetailsFragment.refreshValues();
//        }
//
//        if(historyDetailsFragment != null) {
//            historyDetailsFragment.refreshValues();
//        }

//        if(sharedPref.getTransferToDealerList(true)){
//            startActivity(new Intent(DealerDetailsActivity.this, SelectDealerActivity.class));
//            finish();
//        }

    }
}





