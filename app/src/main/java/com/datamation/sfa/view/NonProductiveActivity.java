package com.datamation.sfa.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.sfa.R;
import com.datamation.sfa.controller.DayNPrdDetController;
import com.datamation.sfa.controller.SalesReturnDetController;
import com.datamation.sfa.model.Customer;
import com.datamation.sfa.model.FInvRHed;
import com.datamation.sfa.nonproductive.NonProductiveDetail;
import com.datamation.sfa.salesreturn.SalesReturnDetails;
import com.datamation.sfa.salesreturn.SalesReturnHeader;
import com.datamation.sfa.salesreturn.SalesReturnSummary;

public class NonProductiveActivity extends AppCompatActivity {

    ViewPager viewPager;
    private NonProductiveDetail nonProductiveDetail;
    Context context;
    public Customer selectedNonDebtor = null;
    boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_productive);

        Toolbar toolbar = (Toolbar) findViewById(R.id.np_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("NON PRODUCTIVE");
        context = this;

        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.np_tab_strip);
        viewPager = (ViewPager) findViewById(R.id.np_viewpager);

        slidingTabStrip.setBackgroundColor(getResources().getColor(R.color.theme_color));
        slidingTabStrip.setTextColor(getResources().getColor(android.R.color.black));
        slidingTabStrip.setIndicatorColor(getResources().getColor(R.color.red_error));
        slidingTabStrip.setDividerColor(getResources().getColor(R.color.half_black));

        NPPagerAdapter adapter = new NPPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        slidingTabStrip.setViewPager(viewPager);

        status = new DayNPrdDetController(getApplicationContext()).isAnyActiveNPs();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_NP_DETAILS"));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class NPPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"NON PRODUCTIVE DETAILS"};

        public NPPagerAdapter(FragmentManager fm) {
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
                    if(nonProductiveDetail == null) nonProductiveDetail = new NonProductiveDetail();
                    return nonProductiveDetail;

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
//        super.onBackPressed();
    }
}
