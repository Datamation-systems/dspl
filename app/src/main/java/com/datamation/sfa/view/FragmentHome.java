package com.datamation.sfa.view;




import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.datamation.sfa.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

//        Pie pie = AnyChart.pie();
//        Pie bar = AnyChart.pie();
//
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("John", 10000));
//        data.add(new ValueDataEntry("Jake", 12000));
//        data.add(new ValueDataEntry("Peter", 18000));
//        bar.setData(data);
//        List<DataEntry> data1 = new ArrayList<>();
//        data1.add(new ValueDataEntry("John", 10000));
//        data1.add(new ValueDataEntry("Jake", 12000));
//        data1.add(new ValueDataEntry("Peter", 18000));
//        pie.setData(data1);
//        AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.any_chart_view);
//        AnyChartView anyChartView2 = (AnyChartView) view.findViewById(R.id.any_chart_view2);
        //anyChartView.setChart(bar);
        //anyChartView2.setChart(pie);

        return view;
    }

}
