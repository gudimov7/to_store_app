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

/**
 * StoreItem obj ListView Adapter class extends ArrayAdapter
 * @author Vyacheslav Gudimov
 */
public class StoreItemListViewAdapter extends ArrayAdapter<StoreItem> {
    private final ArrayList<StoreItem> storeItems;
    private final Context context;
    private final int resource;

    /**
     * implemented super constructor
     * @param context Activity using this adapter
     * @param resource root id for XML row view
     * @param storeItems ArrayList of storeItems objects
     */
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

        // initiate inner views
        ImageView itemPicIV = (ImageView) view.findViewById(R.id.layoutStoreItem_iv_itemPic);
        TextView itemNameTV = (TextView) view.findViewById(R.id.layoutStoreItem_tv_itemName);
        TextView itemPriceTV = (TextView) view.findViewById(R.id.layoutStoreItem_tv_itemPrice);

        //set params to inner views from OrderCompleted obj in position
        Picasso.get().load(storeItems.get(position).getPic()).into(itemPicIV);
        itemNameTV.setText("Name:\t" +storeItems.get(position).getItemName());
        itemPriceTV.setText(String.format("Price: %.2f$",storeItems.get(position).getPrice()));

        return view;
    }
}
