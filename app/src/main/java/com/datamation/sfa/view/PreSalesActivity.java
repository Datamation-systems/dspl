package com.datamation.sfa.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.sfa.R;
import com.datamation.sfa.fragment.debtorlist.AllCustomerFragment;
import com.datamation.sfa.fragment.debtorlist.RouteCustomerFragment;
import com.datamation.sfa.helpers.IResponseListener;
import com.datamation.sfa.helpers.PreSalesResponseListener;
import com.datamation.sfa.presale.DetailFragment;
import com.datamation.sfa.presale.HeaderFragment;
import com.datamation.sfa.presale.OrderMainFragment;
import com.datamation.sfa.presale.SalesManagementFragment;

public class PreSalesActivity extends AppCompatActivity implements PreSalesResponseListener{

    private HeaderFragment headerFragment;
    private DetailFragment detailFragment;
    private SalesManagementFragment salesManagementFragment;
    private OrderMainFragment orderMainFragment;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sales);

        Toolbar toolbar = (Toolbar) findViewById(R.id.presale_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("SALES ORDER");

        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.presale_tab_strip);
        viewPager = (ViewPager) findViewById(R.id.presale_viewpager);

        slidingTabStrip.setBackgroundColor(getResources().getColor(R.color.theme_color));
        slidingTabStrip.setTextColor(getResources().getColor(android.R.color.black));
        slidingTabStrip.setIndicatorColor(getResources().getColor(R.color.red_error));
        slidingTabStrip.setDividerColor(getResources().getColor(R.color.half_black));

        PreSalesPagerAdapter adapter = new PreSalesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        slidingTabStrip.setViewPager(viewPager);
    }

    private class PreSalesPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"HEADER", "ORDER DETAILS", "ORDER RETURN", "ORDER SUMMARY"};

        public PreSalesPagerAdapter(FragmentManager fm) {
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
                    if(headerFragment == null) headerFragment = new HeaderFragment();
                    return headerFragment;
                case 1:
                    if(detailFragment == null) detailFragment = new DetailFragment();
                    return detailFragment;
                case 2:
                    if(orderMainFragment == null) orderMainFragment = new OrderMainFragment();
                    return orderMainFragment;
                case 4:
                    if(salesManagementFragment == null) salesManagementFragment = new SalesManagementFragment();
                    return salesManagementFragment;
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
    public void moveBackToCustomer_pre(int index) {

        if (index == 0)
        {
            viewPager.setCurrentItem(0);
        }

        if (index == 1)
        {
            viewPager.setCurrentItem(1);
        }

        if (index == 2)
        {
            viewPager.setCurrentItem(2);
        }

        if (index == 3)
        {
            viewPager.setCurrentItem(3);
        }
    }

    @Override
    public void moveNextToCustomer_pre(int index) {

        if (index == 0)
        {
            viewPager.setCurrentItem(0);
        }

        if (index == 1)
        {
            viewPager.setCurrentItem(1);
        }

        if (index == 2)
        {
            viewPager.setCurrentItem(2);
        }

        if (index == 3)
        {
            viewPager.setCurrentItem(3);
        }
    }
}
