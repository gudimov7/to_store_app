package com.example.toy_store_app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.toy_store_app.R;

import com.example.toy_store_app.services.OrderCompleted;

import java.util.ArrayList;

/**
 * OrderCompleted obj ListView Adapter class extends ArrayAdapter
 * @author Vyacheslav Gudimov
 */
public class OrdersListViewAdapter extends ArrayAdapter <OrderCompleted> {
    private final ArrayList<OrderCompleted> orders;
    private final Context context;
    private final int resource;

    /**
     * implemented super constructor
     * @param context Activity using this adapter
     * @param resource root id for XML row view
     * @param orders ArrayList of OrderCompleted objects
     */
    public OrdersListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrderCompleted> orders) {
        super(context, resource, orders);
        this.context = context;
        this.orders = orders;
        this.resource = resource;
    }

    /**
     * return view as drawn
     * @param position OrderCompleted row position
     * @param convertView view to be drawn
     * @param parent ListView for objects
     * @return view to be drawn
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(context);
        convertView = (View) inflater.inflate(resource, parent, false);

        // initiate inner views
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.singleOrderView_dialog_l_rowLayout);
        TextView orderPosition = (TextView) convertView.findViewById(R.id.layoutPurchaseOrder_tv_position);
        TextView orderBuyerName = (TextView) convertView.findViewById(R.id.layoutPurchaseOrder_tv_buyerName);
        TextView orderPurchaseDate = (TextView) convertView.findViewById(R.id.layoutPurchaseOrder_tv_purchaseDate);
        TextView orderTotalPrice = (TextView) convertView.findViewById(R.id.layoutPurchaseOrder_tv_purchaseTotalPrice);

        //set params to inner views from OrderCompleted obj in position
        layout.setBackgroundColor(Color.RED);
        orderPosition.setText("#" + position +1);
        orderBuyerName.setText(orders.get(position).getUser().getName());
        orderPurchaseDate.setText(orders.get(position).getDate());
        orderTotalPrice.setText(String.format("%.2f",orders.get(position).getTotalPrice()));

        return convertView;
    }
}
