package com.datamation.sfa.view.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.datamation.sfa.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;



import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private HorizontalBarChart cumulativeLineChart;
    private NumberFormat numberFormat = NumberFormat.getInstance();
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aaa", Locale.getDefault());

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());

    private ArrayList<Double> targetValues;
    private List<Double> achievementValues;

    BarChart chart ;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_dashboard, container, false);

        cumulativeLineChart = (HorizontalBarChart) rootView.findViewById(R.id.dashboard_hBarChart);
        targetValues = new ArrayList<Double>();
        cumulativeLineChart.setDescription("");
        cumulativeLineChart.setDrawGridBackground(false);
        cumulativeLineChart.setPinchZoom(true);

        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

        chart = rootView.findViewById(R.id.barChart);
        ArrayList monthTvA = new ArrayList();

        chart.setDescription("");
        //chart.set
        monthTvA.add(new BarEntry(945f, 0));
        monthTvA.add(new BarEntry(1040f, 1));
        monthTvA.add(new BarEntry(1133f, 2));


        ArrayList titl = new ArrayList();
        titl.add("Target");
        titl.add("Achievement");
        titl.add("Balance");



        BarDataSet bardataset = new BarDataSet(monthTvA, "values");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        bardataset.setColors(new int[]{ContextCompat.getColor(getActivity(), R.color.blue_btn_bg_color),
//                ContextCompat.getColor(getActivity(), R.color.cardviews_colorsN),
//                ContextCompat.getColor(getActivity(), R.color.background),
//                ContextCompat.getColor(getActivity(), R.color.blue_btn_bg_color)});
        chart.animateY(2000);
        chart.setDrawGridBackground(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
       // chart.xAxis.isEnabled = false;
      //chart.getXAxis().setEnabled(false);

    //chart.getAxisLeft().setDrawAxisLine(false);
        BarData data = new BarData(titl, bardataset);
        bardataset.setBarSpacePercent(35f);
        chart.setData(data);

        // horizontal barchart
        BarData data1 = new BarData(getXAxisValues(),getDataSet());
        cumulativeLineChart.setData(data1);
        cumulativeLineChart.animateXY(2000, 2000);
        cumulativeLineChart.setDrawGridBackground(false);
        cumulativeLineChart.setDrawBorders(false);
        cumulativeLineChart.setDrawValueAboveBar(false);
        cumulativeLineChart.getAxisLeft().setDrawGridLines(false);
        cumulativeLineChart.getAxisLeft().setEnabled(false);
        cumulativeLineChart.getAxisRight().setEnabled(false);
        cumulativeLineChart.invalidate();

        // pie chart
        PieChart pieChart = rootView.findViewById(R.id.piechart);
        ArrayList pieChartValues = new ArrayList();

        pieChart.setDescription("");

        pieChartValues.add(new Entry(2945f, 0));
        pieChartValues.add(new Entry(1133f, 1));


        PieDataSet dataSet = new PieDataSet(pieChartValues, "(-values-)");

        ArrayList title = new ArrayList();

        title.add("Target");
        title.add("Achievement");


        PieData dataPie = new PieData(title, dataSet);
        pieChart.setData(dataPie);
       // dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setColors(new int[]{ContextCompat.getColor(getActivity(), R.color.red_error),
                ContextCompat.getColor(getActivity(),R.color.achievecolor )});
        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(3000, 3000);
        return rootView;
    }

    private BarDataSet getDataSet() {

        ArrayList<BarEntry> entries = new ArrayList();
        entries.add(new BarEntry(80f, 0));
        entries.add(new BarEntry(50f, 1));
        entries.add(new BarEntry(60f, 2));


        BarDataSet dataset = new BarDataSet(entries,"count");
        dataset.setColors(new int[]{ContextCompat.getColor(getActivity(), R.color.error_stroke_color),
                ContextCompat.getColor(getActivity(), R.color.main_green_stroke_color),
                ContextCompat.getColor(getActivity(), R.color.theme_color_dark)});
      //  dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        return dataset;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> labels = new ArrayList();
        labels.add("visit");
        labels.add("not visit");
        labels.add("nonproductive");
        return labels;
    }


}