package com.example.toy_store_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.toy_store_app.R;

import com.example.toy_store_app.services.OrderCompleted;

import java.util.ArrayList;

public class OrdersListViewAdapter extends ArrayAdapter <OrderCompleted> {
    private final ArrayList<OrderCompleted> orders;
    private final Context context;
    private final int resource;

    public OrdersListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrderCompleted> orders) {
        super(context, resource, orders);
        this.context = context;
        this.orders = orders;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(context);
        convertView = (View) inflater.inflate(resource, parent, false);

        TextView orderPosition = (TextView) convertView.findViewById(R.id.layoutPurchaseOrder_tv_position);
        TextView orderBuyerName = (TextView) convertView.findViewById(R.id.layoutPurchaseOrder_tv_buyerName);
        TextView orderPurchaseDate = (TextView) convertView.findViewById(R.id.layoutPurchaseOrder_tv_purchaseDate);
        TextView orderTotalPrice = (TextView) convertView.findViewById(R.id.layoutPurchaseOrder_tv_purchaseTotalPrice);

        orderPosition.setText("#" + position +1);
        orderBuyerName.setText(orders.get(position).getUser().getName());
        orderPurchaseDate.setText(orders.get(position).getDate());
        orderTotalPrice.setText(String.format("%.2f",orders.get(position).getTotalPrice()));

        return convertView;
    }
}
