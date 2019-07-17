package com.datamation.sfa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.datamation.sfa.R;
import com.datamation.sfa.model.Reason;

import java.util.ArrayList;

public class SalesExpenseDetailAdapter extends ArrayAdapter<Reason> {
    Context context;
    ArrayList<Reason> list;

    public SalesExpenseDetailAdapter(Context context, ArrayList<Reason> list) {
        super(context, R.layout.row_sales_expense_detail, list);
        this.context = context;
        this.list = list;

    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        LayoutInflater inflater = null;
        View row = null;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.row_sales_expense_detail, parent, false);

        TextView Itemname = (TextView) row.findViewById(R.id.row_sales_expensename);
        TextView Itemcode = (TextView) row.findViewById(R.id.row_sales_expensecode);

        Itemname.setText(list.get(position).getFREASON_NAME());
        Itemcode.setText(list.get(position).getFREASON_CODE());

        return row;
    }
}
