package com.example.toy_store_app;

import static com.example.toy_store_app.services.FF.log;
import static com.example.toy_store_app.services.FF.logToFireBase;
import static com.example.toy_store_app.services.FF.toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.toy_store_app.adapters.StoreItemListViewAdapter;
import com.example.toy_store_app.firebase.FirebaseDB;
import com.example.toy_store_app.services.ItemDescription;
import com.example.toy_store_app.services.StoreItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InStoreActivity extends AppCompatActivity {

    private ListView itemsList;
    private ArrayList<StoreItem> inStoreItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_store);

        itemsList = (ListView) findViewById(R.id.adminItemListActivity_lv_storeItemList);
        inStoreItems = new ArrayList<>();

        refreshList();

        itemsList.setOnItemClickListener(((parent, view, position, id) -> {
            Dialog dialog = new Dialog(InStoreActivity.this);
            dialog.setContentView(R.layout.dialog_store_item_full_view);
            TextView itemNameHeader = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemNameHeader);
            TextView itemName = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemName);
            TextView itemAge = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemAge);
            TextView itemColor = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemColor);
            TextView itemMaterial = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemMaterial);
            TextView itemMade = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemMade);
            TextView itemPrice = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemPrice);
            ImageView itemPic = dialog.findViewById(R.id.fullStoreIte_dialog_iv_itemPic);
            Button backBtn = findViewById(R.id.fullStoreItem_dialog_btn_back);
            Button buyBtn = findViewById(R.id.fullStoreItem_dialog_btn_back);

            itemNameHeader.setText(inStoreItems.get(position).getItemName());
            itemName.setText(inStoreItems.get(position).getItemName());
            itemAge.setText(inStoreItems.get(position).getItemName());
            itemColor.setText(inStoreItems.get(position).getItemName());
            itemMaterial.setText(inStoreItems.get(position).getItemName());
            itemMade.setText(inStoreItems.get(position).getItemName());
            itemPrice.setText(inStoreItems.get(position).getItemName());
            Picasso.get().load(inStoreItems.get(position).getPic()).resize(70,70).into(itemPic);

            backBtn.setOnClickListener(v -> dialog.dismiss());
            buyBtn.setOnClickListener(v-> {
                //TODO: add item to cart
                dialog.dismiss();
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }));

    }

    private void refreshList() {
        inStoreItems.clear();
        FirebaseDB.getDataReference().child(FirebaseDB.TOYS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    StoreItem item = new StoreItem();
                    for (DataSnapshot items: data.getChildren()) {
                        item.setDescription(data.child(StoreItem.ITEM_DESCRIPTION).getValue(ItemDescription.class));
                        item.setItemName(data.child(StoreItem.ITEM_NAME).getValue(String.class));
                        item.setPic(data.child(StoreItem.ITEM_PIC).getValue(String.class));
                        item.setPrice(data.child(StoreItem.ITEM_PRICE).getValue(Float.class));
                    }
                    inStoreItems.add(item);
                }
                StoreItemListViewAdapter SIA = new StoreItemListViewAdapter(InStoreActivity.this,R.layout.layout_store_item_list_row, inStoreItems);
                itemsList.setAdapter(SIA);
                log(InStoreActivity.class,"fetched item list successfully");
                logToFireBase(InStoreActivity.this,"fetched item list successfully");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log(InStoreActivity.class,"refresh list canceled");
                logToFireBase(InStoreActivity.this,"refresh canceled");
            }
        });


    }
}