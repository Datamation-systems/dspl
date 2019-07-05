package com.datamation.sfa.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.sfa.R;
import com.datamation.sfa.helpers.ReceiptResponseListener;
import com.datamation.sfa.helpers.VanSalesResponseListener;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.ReceiptHed;
import com.datamation.sfa.receipt.ReceiptDetails;
import com.datamation.sfa.receipt.ReceiptHeader;
import com.datamation.sfa.receipt.ReceiptSummary;

public class ReceiptActivity extends AppCompatActivity implements ReceiptResponseListener{
    private ReceiptHeader headerFragment;
    private ReceiptDetails detailFragment;
    private ReceiptSummary salesManagementFragment;

    private ViewPager viewPager;
    public Customer selectedDebtor = null;
    public ReceiptHed selectedRecHed = null;
    public Double ReceivedAmt = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sales);

        Toolbar toolbar = (Toolbar) findViewById(R.id.presale_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("INVOICE");

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

        private final String[] titles = {"HEADER", "INVOICE DETAILS", "INVOICE SUMMARY"};

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
                    if(headerFragment == null) headerFragment = new ReceiptHeader();
                    return headerFragment;
                case 1:
                    if(detailFragment == null) detailFragment = new ReceiptDetails();
                    return detailFragment;
                case 2:
                    if(salesManagementFragment == null) salesManagementFragment = new ReceiptSummary();
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
    public void moveToDetailsRece(int index) {

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


    }

}
