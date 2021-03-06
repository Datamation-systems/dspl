package com.datamation.sfa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datamation.sfa.controller.ItemController;
import com.datamation.sfa.controller.TaxDetController;
import com.datamation.sfa.model.OrderDetail;
import com.datamation.sfa.R;

import java.util.ArrayList;


public class OrderDetailsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    ArrayList<OrderDetail> list;
    Context context;
    String debCode;

    public OrderDetailsAdapter(Context context, ArrayList<OrderDetail> list, String debCode){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.debCode = debCode;
    }
    @Override
    public int getCount() {
        if (list != null) return list.size();
        return 0;
    }
    @Override
    public OrderDetail getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView ==null) {
            viewHolder = new ViewHolder();
            convertView =inflater.inflate(R.layout.row_order_details,parent,false);
            viewHolder.lblItem = (TextView) convertView.findViewById(R.id.row_item);
            viewHolder.lblQty = (TextView) convertView.findViewById(R.id.row_cases);
            viewHolder.lblAMt = (TextView) convertView.findViewById(R.id.row_piece);
            viewHolder.showStatus=(TextView)convertView.findViewById(R.id.row_free_status);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.lblItem.setText(list.get(position).getFORDERDET_ITEMCODE()+ " - " +new ItemController(convertView.getContext()).getItemNameByCode(list.get(position).getFORDERDET_ITEMCODE()));
        viewHolder.lblQty.setText(list.get(position).getFORDERDET_QTY());
        String sArray[] = new TaxDetController(context).calculateTaxForwardFromDebTax(debCode, list.get(position).getFORDERDET_ITEMCODE(), Double.parseDouble(list.get(position).getFORDERDET_AMT()));
        String amt = String.format("%.2f",Double.parseDouble(sArray[0]));
        viewHolder.lblAMt.setText(amt);

        return convertView;
    }
    //12972
    //1067
   // 1003.75
    //1649
    //600
    //990+419.37
    //19215.00

    private  static  class  ViewHolder{
        TextView lblItem;
        TextView lblQty;
        TextView lblAMt;
        TextView showStatus;

    }

}
