package com.datamation.sfa.view.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import com.datamation.sfa.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface//IOnDashboardFragmentInteractionListener
 * to handle interaction events.
 * Use the {@link MainDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainDashboardFragment extends Fragment {

    private static final String LOG_TAG = MainDashboardFragment.class.getSimpleName();

  //  private CalendarDatePickerDialog calendarDatePickerDialog;

    private TextView tvSyncTime;

    private TextView tvTodayHeader;
    private TextView tvTodayGrossSale, tvTodayGrossSalePercentage, tvTodayMarketReturn,
            tvTodayDiscount, tvTodayDiscountPercentage, tvTodayNetSale, tvTodayTarget,
            tvTodayProductive, tvTodayUnproductive;

    private TextView tvThisMonthHeader;
    private TextView tvThisMonthGrossSale, tvThisMonthGrossSalePercentage, tvThisMonthMarketReturn,
            tvThisMonthDiscount, tvThisMonthDiscountPercentage, tvThisMonthNetSale, tvThisMonthTarget,
            tvThisMonthProductive, tvThisMonthUnproductive;

    private TextView tvPrevMonthHeader;
    private TextView tvPrevMonthGrossSale, tvPrevMonthGrossSalePercentage, tvPrevMonthMarketReturn,
            tvPrevMonthDiscount, tvPrevMonthDiscountPercentage, tvPrevMonthNetSale, tvPrevMonthTarget,
            tvPrevMonthProductive, tvPrevMonthUnproductive;

    //    private CardView chartContainer;
    private LineChart cumulativeLineChart;
    private CheckBox cumulativeSwitcher;
//    private boolean firstAttempt;

    private NumberFormat numberFormat = NumberFormat.getInstance();
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aaa", Locale.getDefault());

    //    private SharedPref pref;
    //private NetworkFunctions networkFunctions;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());

    private ArrayList<Double> targetValues;
    private List<Double> achievementValues;

    //private DatabaseHandler databaseHandler;

    private boolean syncedInThisDashboardView = false;

    private int thisDay;

//    private IOnDashboardFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainDashboardFragment.
     */
    public static MainDashboardFragment newInstance() {
//        MainDashboardFragment fragment = new MainDashboardFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return new MainDashboardFragment();
    }

    public MainDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_dashboard, container, false);

        //databaseHandler = DatabaseHandler.getDbHandler(getActivity());

        tvSyncTime = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_sync_time);

        tvTodayHeader = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_header);
        tvTodayGrossSale = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_gross_sale);
        tvTodayGrossSalePercentage = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_gross_sale_percentage);
        tvTodayMarketReturn = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_market_return);
        tvTodayDiscount = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_discount);
        tvTodayDiscountPercentage = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_discount_percentage);
        tvTodayNetSale = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_net_sale);
        tvTodayTarget = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_target);
        tvTodayProductive = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_productive_calls);
        tvTodayUnproductive = (TextView) rootView.findViewById(R.id.dashboard_tv_card_today_unproductive_calls);

        tvThisMonthHeader = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_header);
        tvThisMonthGrossSale = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_gross_sale);
        tvThisMonthGrossSalePercentage = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_gross_sale_percentage);
        tvThisMonthMarketReturn = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_market_return);
        tvThisMonthDiscount = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_discount);
        tvThisMonthDiscountPercentage = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_discount_percentage);
        tvThisMonthNetSale = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_net_sale);
        tvThisMonthTarget = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_target);
        tvThisMonthProductive = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_productive_calls);
        tvThisMonthUnproductive = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_unproductive_calls);

        tvPrevMonthHeader = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_header);
        tvPrevMonthGrossSale = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_gross_sale);
        tvPrevMonthGrossSalePercentage = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_gross_sale_percentage);
        tvPrevMonthMarketReturn = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_market_return);
        tvPrevMonthDiscount = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_discount);
        tvPrevMonthDiscountPercentage = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_discount_percentage);
        tvPrevMonthNetSale = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_net_sale);
        tvPrevMonthTarget = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_target);
        tvPrevMonthProductive = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_productive_calls);
        tvPrevMonthUnproductive = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_unproductive_calls);

//        chartContainer = (CardView) rootView.findViewById(R.id.dashboard_card_target_achievement);
        cumulativeLineChart = (LineChart) rootView.findViewById(R.id.dashboard_linechart_accumulated);
        cumulativeSwitcher = (CheckBox) rootView.findViewById(R.id.dashboard_checkbox_cumulative_switcher);
        targetValues = new ArrayList<Double>();
        targetValues.add(0.0);
        cumulativeLineChart.setDescription("");
        cumulativeLineChart.setDrawGridBackground(false);
        cumulativeLineChart.setPinchZoom(true);

        cumulativeLineChart.setHighlightEnabled(true);
        cumulativeLineChart.setHighlightIndicatorEnabled(false);

        XAxis xAxis = cumulativeLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

//        cumulativeLineChart.setAlpha(0);
//        firstAttempt = true;

        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

//        pref = SharedPref.getInstance(getActivity());
       // networkFunctions = new NetworkFunctions(MainDashboardFragment.this.getActivity());

//        if(NetworkUtil.isNetworkAvailable(getActivity())) {
//            new GetDashboardData(System.currentTimeMillis()).execute();
//        }

        cumulativeSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              //  initiateLineChart(generateLineData(isChecked));
                generateSalesChart(targetValues, achievementValues, isChecked);
            }
        });

//        ImageButton imgbtnCalendar = (ImageButton) rootView.findViewById(R.id.dashboard_toolbar_icon_calendar);
//        calendarDatePickerDialog = new CalendarDatePickerDialog();
//
//        calendarDatePickerDialog.setOnDateSetListener(new CalendarDatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int month, int day) {
////                Calendar calendar = Calendar.getInstance();
////                calendar.set(year, month, day);
//
//                thisDay = day;
//
//                long millis = TimeUtils.parseIntoTimeInMillis(year, month, day);
//                try {
//                    setDashboardDetails(millis);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "Error loading data. Please try again", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

//        try {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            thisDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//           // setDashboardDetails(System.currentTimeMillis());
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(getActivity(), "Error loading data. Please try again", Toast.LENGTH_SHORT).show();
//        }



        return rootView;
    }

    public void showCalendar() {
      //  calendarDatePickerDialog.show(getActivity().getSupportFragmentManager(), "TAG");
    }

    public void sync() {
        if (!syncedInThisDashboardView) {
         //   new GetDashboardData(System.currentTimeMillis()).execute();
        } else {
            AlertDialog alreadySyncedDialog = new AlertDialog.Builder(getActivity())
                    .setMessage("New data already been synced")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            alreadySyncedDialog.setCancelable(false);
            alreadySyncedDialog.show();
        }
    }

//    public void onDateSet(int year, int month, int day) {
//
//    }

//    public void setDashboardDetails(long time) throws JSONException {
//        DashboardDetail dashboardDetail = databaseHandler.getDashboardDetailForMonth(time);
//        List<DayPlanHolder> planHolders =
//                databaseHandler.getDayRouteAmountPlansOfTimeFrame(TimeUtils.getMonthBeginTime(time),
//                        TimeUtils.getMonthEndTime(time));
//

//
//        if(planHolders.size() > 0) {

//            for(DayPlanHolder planHolder : planHolders) {
//                targetValues.add(planHolder.getTargetAmount());
//            }
//        }
//
//        if (dashboardDetail != null) {
//            Log.wtf(LOG_TAG, "LOADED FROM DB");
//            try {
//                JSONObject previousMonth = dashboardDetail.getPreviousMonth();
//                setPreviousMonthDetails(previousMonth);
//                JSONObject thisMonth = dashboardDetail.getThisMonth();
//                setThisMonthDetails(thisMonth);
//
////                Log.d(LOG_TAG, "This Day : " + thisDay);
//                JSONObject todayJSON = dashboardDetail.getDetailsOfDay(thisDay);
//                if (todayJSON != null) setTodayDetails(todayJSON);
//
//                generateSalesChart(targetValues, achievementValues, cumulativeSwitcher.isChecked());
//                cumulativeSwitcher.setEnabled(true);
//
//                Date date = new Date(time);
//
//                tvTodayHeader.setText(dateFormat.format(time));
//                tvThisMonthHeader.setText("This Month (" + monthFormat.format(date) + ")");
//
//                Calendar lastMonthCalendar = Calendar.getInstance();
//                lastMonthCalendar.setTimeInMillis(time);
//                lastMonthCalendar.roll(Calendar.MONTH, false);
//
//                tvPrevMonthHeader.setText(monthFormat.format(new Date(lastMonthCalendar.getTimeInMillis())));
//
//                tvSyncTime.setText("(Accurate as of " + dateTimeFormat.format(new Date(dashboardDetail.getTime())) + ")");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            if (NetworkUtil.isNetworkAvailable(getActivity())) {
//                new GetDashboardData(time).execute();
//                Log.wtf(LOG_TAG, "LOADED FROM WEB");
//            } else {
//                Toast.makeText(getActivity(), "Please switch on internet and try again", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void setPreviousMonthDetails(JSONObject previousMonth) throws JSONException {
//
//        double target = previousMonth.getDouble("target");
//        double grossSales = previousMonth.getDouble("gross_sale");
//        double discount = previousMonth.getDouble("discount");
//        double discountPercentage = previousMonth.getDouble("discount_percentage");
//        double returns = previousMonth.getDouble("returns");
//
//        double netSale = grossSales - discount - returns;
//
//        tvPrevMonthTarget.setText(numberFormat.format(target));
//        tvPrevMonthGrossSale.setText(numberFormat.format(grossSales));
//
//        if (grossSales > 0 && netSale > 0) {
//            tvPrevMonthGrossSalePercentage.setText("(" + numberFormat.format(netSale / grossSales * 100) + "%)");
//        } else {
//            tvPrevMonthGrossSalePercentage.setText("(0.00%)");
//        }
//
//        tvPrevMonthMarketReturn.setText(numberFormat.format(returns));
//        tvPrevMonthDiscount.setText(numberFormat.format(discount));
//        tvPrevMonthDiscountPercentage.setText("(" + numberFormat.format(discountPercentage) + "%)");
//
//        tvPrevMonthNetSale.setText(numberFormat.format(netSale));
//
//        tvPrevMonthProductive.setText(previousMonth.getString("productive_calls"));
//        tvPrevMonthUnproductive.setText(previousMonth.getString("unproductive_cals"));
//
//    }
//
//    private void setThisMonthDetails(JSONObject thisMonth) throws JSONException {
//
//        Log.d(LOG_TAG, "This month : \n" + thisMonth);
//
////        targetValues = new ArrayList<>();
//        achievementValues = new ArrayList<>();
//
//        double monthTarget = 0;
//        double monthGrossSales = 0;
//        double monthDiscount = 0;
//        double monthDiscountPercentage = 0;
//        double monthReturns = 0;
//        double monthNetSale = 0;
//
//        int monthProductive = 0;
//        int monthUnproductive = 0;
//
////        double target = 0;
////        double grossSales = 0;
////        double discount = 0;
////        double discountPercentage = 0;
////        double returns = 0;
//        double netSale = 0;
//
//        int discountPercentageCount = 0;
////
////        int productive = 0;
////        int unproductive = 0;
//
//        boolean targetsNeeded = targetValues.size() == 0;
//
//        if(targetsNeeded) targetValues.add(0.0);
//
//        achievementValues.add(0.0);
//
//        for (int dayIndex = 1; dayIndex < 32; dayIndex++) {
//
//            if (thisMonth.has(String.valueOf(dayIndex))) {
//
//                JSONObject dayJSON = thisMonth.getJSONObject(String.valueOf(dayIndex));
//
//                if(targetsNeeded) targetValues.add(dayJSON.getDouble("target"));
//
//                if(dayIndex <= thisDay) achievementValues.add(dayJSON.getDouble("gross_sale"));
//
//                monthTarget += dayJSON.getDouble("target");
//                monthGrossSales += dayJSON.getDouble("gross_sale");
//                monthDiscount += dayJSON.getDouble("discount");
//                monthReturns += dayJSON.getDouble("returns");
//                monthNetSale += netSale;
//
//                monthProductive += dayJSON.getInt("productive_calls");
//                monthUnproductive += dayJSON.getInt("unproductive_cals");
//
//                if(dayJSON.getDouble("discount_percentage") > 0) {
//                    monthDiscountPercentage += dayJSON.getDouble("discount_percentage");
//                    discountPercentageCount++;
//                }
//
//            } else break;
//        }
//
//        tvThisMonthTarget.setText(numberFormat.format(monthTarget));
//        tvThisMonthGrossSale.setText(numberFormat.format(monthGrossSales));
//
//        tvThisMonthMarketReturn.setText(numberFormat.format(monthReturns));
//        tvThisMonthDiscount.setText(numberFormat.format(monthDiscount));
////        tvThisMonthDiscountPercentage.setText("(" + "0.00" + "%)");
//
//        tvThisMonthNetSale.setText(numberFormat.format(monthNetSale));
//
//        tvThisMonthProductive.setText(String.valueOf(monthProductive));
//        tvThisMonthUnproductive.setText(String.valueOf(monthUnproductive));
//
//        if(monthDiscountPercentage > 0) {
//            tvThisMonthDiscountPercentage.setText("("
//                    + numberFormat.format(monthDiscountPercentage/discountPercentageCount) + "%)");
//        } else {
//            tvThisMonthDiscountPercentage.setText("(0.00%");
//        }
//
//        if(monthTarget > 0) {
//            tvThisMonthGrossSalePercentage.setText("("
//                    + numberFormat.format(monthGrossSales/monthTarget * 100) + "%)");
//        } else {
//            tvThisMonthGrossSalePercentage.setText("N/A");
//        }
//
//    }
//
//    private void setTodayDetails(JSONObject dayJSON) throws JSONException {
//
//        double target;
//        double grossSales;
//        double discount;
//        double discountPercentage;
//        double returns;
//        double netSale;
//
//        int productive;
//        int unproductive;
//
//        target = dayJSON.getDouble("target");
//        grossSales = dayJSON.getDouble("gross_sale");
//        discount = dayJSON.getDouble("discount");
//        discountPercentage = dayJSON.getDouble("discount_percentage");
//        returns = dayJSON.getDouble("returns");
//
//        netSale = grossSales - discount - returns;
//
//        productive = dayJSON.getInt("productive_calls");
//        unproductive = dayJSON.getInt("unproductive_cals");
//
//        tvTodayTarget.setText(numberFormat.format(target));
//        tvTodayGrossSale.setText(numberFormat.format(grossSales));
//
//        if (target > 0) {
//            tvTodayGrossSalePercentage.setText("("
//                    + numberFormat.format(grossSales / target * 100) + "%)");
//        } else {
//            tvTodayGrossSalePercentage.setText("N/A");
//        }
//
//        tvTodayMarketReturn.setText(numberFormat.format(returns));
//        tvTodayDiscount.setText(numberFormat.format(discount));
//        tvTodayDiscountPercentage.setText("(" + numberFormat.format(discountPercentage) + "%)");
//
//        tvTodayNetSale.setText(numberFormat.format(netSale));
//
//        tvTodayProductive.setText(String.valueOf(productive));
//        tvTodayUnproductive.setText(String.valueOf(unproductive));
//    }
//
//    @Override
//    public void onFragmentVisible(DashboardContainerFragment dashboardContainerFragment) {
//        dashboardContainerFragment.currentFragment = this;
//    }
//
//    private class GetDashboardData extends AsyncTask<Void, Void, String> {
//
//        private CustomProgressDialog pDialog;
////        private List<String> errors = new ArrayList<>();
//
//        private long timeInMillis;
//
//        private GetDashboardData(long timeInMillis) {
//            this.timeInMillis = timeInMillis;
////            errors = new ArrayList<>();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new CustomProgressDialog(getActivity());
//            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            pDialog.setMessage("Authenticating...");
//            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            try {
//                long requestTime = TimeUtils.getMonthEndTime(timeInMillis);
//                return networkFunctions.getMonthlyReport(dateFormat.format(new Date(requestTime)));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String response) {
//            super.onPostExecute(response);
//
//            if (pDialog.isShowing()) pDialog.dismiss();
//
//            JSONObject previousMonth = null;
//            JSONObject thisMonth;
//            if (response != null) {
//
//                Log.d(LOG_TAG, "Dashboard Detail : " + response.getBytes().length + " bytes");
//
//                try {
//                    JSONObject responseJSON = new JSONObject(response);
//
//                    try {
//                        previousMonth = responseJSON.getJSONObject("previous_month");
//                        setPreviousMonthDetails(previousMonth);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                        tvPrevMonthTarget.setText(numberFormat.format(0));
//                        tvPrevMonthGrossSale.setText(numberFormat.format(0));
//                        tvPrevMonthGrossSalePercentage.setText("(0.00%)");
//                        tvPrevMonthMarketReturn.setText(numberFormat.format(0));
//                        tvPrevMonthDiscount.setText(numberFormat.format(0));
//                        tvPrevMonthDiscountPercentage.setText("(0.00%)");
//
//                        tvPrevMonthNetSale.setText(numberFormat.format(0));
//
//                        tvPrevMonthProductive.setText("0");
//                        tvPrevMonthUnproductive.setText("0");
//
//                    }
//
//                    try {
//                        thisMonth = responseJSON.getJSONObject("this_month");
//
//                        setThisMonthDetails(thisMonth);
//
//                        DashboardDetail dashboardDetail = new DashboardDetail();
//                        dashboardDetail.setTime(timeInMillis);
//                        dashboardDetail.setPreviousMonth(previousMonth);
//                        dashboardDetail.setThisMonth(thisMonth);
//
//                        Log.d(LOG_TAG, "This Day : " + thisDay);
//                        JSONObject todayJSON = dashboardDetail.getDetailsOfDay(thisDay);
//                        if (todayJSON != null) setTodayDetails(todayJSON);
//
//                        generateSalesChart(targetValues, achievementValues, cumulativeSwitcher.isChecked());
//
//                        databaseHandler.addDashboardDetail(dashboardDetail);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    cumulativeSwitcher.setEnabled(true);
//
//                    Date date = new Date(timeInMillis);
//
//                    tvTodayHeader.setText(dateFormat.format(timeInMillis));
////                    tvThisMonthHeader.setText(monthFormat.format(date));
//                    tvThisMonthHeader.setText("This Month (" + monthFormat.format(date) + ")");
//
//                    Calendar lastMonthCalendar = Calendar.getInstance();
//                    lastMonthCalendar.setTimeInMillis(timeInMillis);
//                    lastMonthCalendar.roll(Calendar.MONTH, false);
//
//                    tvPrevMonthHeader.setText(monthFormat.format(new Date(lastMonthCalendar.getTimeInMillis())));
//
//                    tvSyncTime.setText("(Accurate as of " + dateTimeFormat.format(new Date(timeInMillis)) + ")");
//
//                    syncedInThisDashboardView = true;
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }
//
    private void generateSalesChart(@NonNull List<Double> targetValues, @NonNull List<Double> achievementValues, boolean cumulative) {

        List<Entry> targets = new ArrayList<>();
        List<Entry> achievements = new ArrayList<>();

        Double cumulativeTarget = (double) 0;
        Double cumulativeAchiv = (double) 0;

        for (int dayIndex = 0; dayIndex < targetValues.size(); dayIndex++) {

            if (cumulative) {
                cumulativeTarget += targetValues.get(dayIndex);
                Entry targetEntry = new Entry(cumulativeTarget.floatValue(), dayIndex);
                targets.add(targetEntry);
            } else {
                Entry targetEntry = new Entry(targetValues.get(dayIndex).floatValue(), dayIndex);
                targets.add(targetEntry);
            }

        }

        for (int dayIndex = 0; dayIndex < achievementValues.size(); dayIndex++) {

            if (cumulative) {
                cumulativeAchiv += achievementValues.get(dayIndex);
                Entry achivEntry = new Entry(cumulativeAchiv.floatValue(), dayIndex);
                achievements.add(achivEntry);
            } else {
                Entry achivEntry = new Entry(achievementValues.get(dayIndex).floatValue(), dayIndex);
                achievements.add(achivEntry);
            }

        }

        LineDataSet line1 = new LineDataSet(targets, "Target");
        line1.setDrawCircles(false);
        line1.setDrawCubic(false);
        line1.setDrawValues(false);
        line1.setColor(getResources().getColor(R.color.material_alert_positive_button));
        line1.setDrawFilled(true);
        line1.setFillColor(getResources().getColor(R.color.material_alert_positive_button));
        line1.setFillAlpha(100);

        LineDataSet line2 = new LineDataSet(achievements, "Achievement");
        line2.setDrawCircles(false);
        line2.setDrawCubic(false);
        line2.setDrawValues(false);
        line2.setColor(getResources().getColor(R.color.red_error));
        line2.setDrawFilled(true);
        line2.setFillColor(getResources().getColor(R.color.red_error));
        line2.setFillAlpha(100);

        List<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(line1);
        lineDataSets.add(line2);

        String[] labels = new String[targets.size()];
        for (int targetsIndex = 0; targetsIndex < targets.size(); targetsIndex++) {
            labels[targetsIndex] = String.valueOf(targetsIndex);
        }

        cumulativeLineChart.setData(new LineData(labels, lineDataSets));
        cumulativeLineChart.animateY(1000);
    }

}