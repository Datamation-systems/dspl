package com.datamation.sfa.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.sfa.R;
import com.datamation.sfa.customer.AddNewCusRegistration;
import com.datamation.sfa.customer.NewCustomerActivity;
import com.datamation.sfa.fragment.debtordetails.CompetitorDetailsFragment;
import com.datamation.sfa.fragment.debtordetails.HistoryDetailsFragment;
import com.datamation.sfa.fragment.debtordetails.OutstandingDetailsFragment;
import com.datamation.sfa.fragment.debtordetails.PersonalDetailsFragment;
import com.datamation.sfa.fragment.debtorlist.AllCustomerFragment;
import com.datamation.sfa.fragment.debtorlist.RouteCustomerFragment;
import com.datamation.sfa.utils.UtilityContainer;

import at.markushi.ui.CircleButton;

public class DebtorListActivity extends AppCompatActivity {

    private CircleButton fabNewCust;

    private RouteCustomerFragment routeCustomerFragment;
    private AllCustomerFragment allCustomerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtor_list);

        fabNewCust = (CircleButton)findViewById(R.id.outlet_details_fab_add_new_cus);
        fabNewCust.setColor(ContextCompat.getColor(this, R.color.main_green_color));
        fabNewCust.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fab_add));

        Toolbar toolbar = (Toolbar) findViewById(R.id.debtor_list_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("DEBTOR LIST");

        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.debtor_list_tab_strip);
        ViewPager viewPager = (ViewPager) findViewById(R.id.debtor_list_viewpager);

        slidingTabStrip.setBackgroundColor(getResources().getColor(R.color.theme_color));
        slidingTabStrip.setTextColor(getResources().getColor(android.R.color.black));
        slidingTabStrip.setIndicatorColor(getResources().getColor(R.color.red_error));
        slidingTabStrip.setDividerColor(getResources().getColor(R.color.half_black));

        DebtorListActivity.DebtorListPagerAdapter adapter = new DebtorListActivity.DebtorListPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        slidingTabStrip.setViewPager(viewPager);

        fabNewCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), NewCustomerActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private class DebtorListPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"ROUTE DEBTORS", "ALL DEBTORS"};

        public DebtorListPagerAdapter(FragmentManager fm) {
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
                    if(routeCustomerFragment == null) routeCustomerFragment = new RouteCustomerFragment();
                    return routeCustomerFragment;
                case 1:
                    if(allCustomerFragment == null) allCustomerFragment = new AllCustomerFragment();
                    return allCustomerFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
