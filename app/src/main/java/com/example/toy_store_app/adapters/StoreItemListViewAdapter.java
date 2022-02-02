package com.example.toy_store_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.toy_store_app.R;
import com.example.toy_store_app.services.StoreItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StoreItemListViewAdapter extends ArrayAdapter<StoreItem> {
    private ArrayList<StoreItem> storeItems;
    private Context context;
    private int resource;

    public StoreItemListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<StoreItem> storeItems) {
        super(context, resource, storeItems);
        this.context = context;
        this.storeItems = storeItems;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(context);
        View view = (View) inflater.inflate(resource, null);

        ImageView itemPicIV = (ImageView) view.findViewById(R.id.layoutStoreItem_iv_itemPic);
        TextView itemNameTV = (TextView) view.findViewById(R.id.layoutStoreItem_tv_itemName);
        TextView itemPriceTV = (TextView) view.findViewById(R.id.layoutStoreItem_tv_itemPrice);

        Picasso.get().load(storeItems.get(position).getPic()).into(itemPicIV);
        itemNameTV.setText("Name:\t" +storeItems.get(position).getItemName());
        itemPriceTV.setText(String.format("Price: %.2f$",storeItems.get(position).getPrice()));

        return view;
    }
}
