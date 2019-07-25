package com.datamation.sfa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.datamation.sfa.R;
import com.datamation.sfa.model.ItemFreeIssue;

import java.util.ArrayList;

public class FreeIssueAdapter extends ArrayAdapter<ItemFreeIssue> {
    Context context;
    ArrayList<ItemFreeIssue> list;

    public FreeIssueAdapter(Context context, ArrayList<ItemFreeIssue> list) {

        super(context, R.layout.row_free_items, list);
        this.context = context;
        this.list = list;
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        LayoutInflater inflater = null;
        View row = null;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.row_free_items, parent, false);

        TextView code = (TextView) row.findViewById(R.id.tv_item_code);
        TextView name = (TextView) row.findViewById(R.id.tv_item_name);
        TextView alloc = (TextView) row.findViewById(R.id.tv_alloc);

        code.setText(list.get(position).getItems().getFITEM_ITEM_CODE());
        name.setText(list.get(position).getItems().getFITEM_ITEM_NAME());
        alloc.setText(list.get(position).getAlloc());

        return row;
    }
}

