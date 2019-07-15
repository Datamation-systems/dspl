package com.datamation.sfa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.datamation.sfa.R;
import com.datamation.sfa.model.OrderDetail;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PrintPreSaleItemAdapter extends ArrayAdapter<OrderDetail> {
    Context context;
    ArrayList<OrderDetail> list;
    String refno;
    BigDecimal disc;

    public PrintPreSaleItemAdapter(Context context, ArrayList<OrderDetail> list) {

        super(context, R.layout.row_printitems_listview, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {

        LayoutInflater inflater = null;
        View row = null;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.row_printitems_listview, parent, false);

        TextView itemname = (TextView) row.findViewById(R.id.printitemnameVan);
        TextView pieceqty = (TextView) row.findViewById(R.id.printpiecesQtyVan);
        TextView amount = (TextView) row.findViewById(R.id.printamountVan);
        TextView printindex = (TextView) row.findViewById(R.id.printindexVan);
        TextView Disc = (TextView) row.findViewById(R.id.printVanDisc);
        TextView mrp = (TextView) row.findViewById(R.id.printVanMRP);

        itemname.setText(list.get(position).getFORDERDET_ITEMCODE());
        pieceqty.setText(list.get(position).getFORDERDET_QTY());
        mrp.setText(list.get(position).getFORDERDET_TSELLPRICE());
        Disc.setText(list.get(position).getFORDERDET_BPDISAMT());
        amount.setText(list.get(position).getFORDERDET_AMT());

        position = position + 1;
        String pos = Integer.toString(position);
        printindex.setText(pos + ". ");

        return row;
    }
}
