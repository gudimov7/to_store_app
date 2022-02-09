package com.example.toy_store_app;

import static com.example.toy_store_app.services.FF.composeEmail;
import static com.example.toy_store_app.services.FF.log;
import static com.example.toy_store_app.services.FF.logToFireBase;
import static com.example.toy_store_app.services.FF.toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.toy_store_app.adapters.OrdersListViewAdapter;
import com.example.toy_store_app.adapters.StoreItemListViewAdapter;
import com.example.toy_store_app.firebase.FirebaseDB;
import com.example.toy_store_app.services.OrderCompleted;
import com.example.toy_store_app.services.StoreItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Admin Activity show all Users obj InStore purchases
 * @author Vyacheslav Gudimov
 */
public class AdminPurchaseDashActivity extends AppCompatActivity {
    private ArrayList <OrderCompleted> orders;
    private ListView purchaseList;
    private ListView singleOrderList;

    /**
     * first function to start as activity starts
     * @param savedInstanceState if has memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_purchase_dash);
        getSupportActionBar().hide();

        //initiate all activity views
        purchaseList = findViewById(R.id.adminPurchaseDashActivity_lv_purchasesList);

        //get all orders from Firebase
        getOrders();

        //set on ListView OrderCompleted click listener
        purchaseList.setOnItemClickListener((parent, view, position, id) -> {
            //start new dialog
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_purchases_single_order_view);

            //initiate dialog views
            TextView buyerName = (TextView) dialog.findViewById(R.id.singleOrderView_dialog_tv_buyerName);
            singleOrderList = (ListView) dialog.findViewById(R.id.singleOrderView_dialog_lv_buyerList);
            Button backBtn = (Button) dialog.findViewById(R.id.singleOrderView_dialog_btn_backBtn);
            Button sendBtn = (Button) dialog.findViewById(R.id.singleOrderView_dialog_btn_sendBtn);

            //set views data with Item clicked in position
            singleOrderRefreshList(orders.get(position).getCart());
            buyerName.setText(orders.get(position).getUser().getName());

            /**
             * back button click listener -> dismiss dialog
             */
            backBtn.setOnClickListener(v -> dialog.dismiss());

            /**
             * send button click listener -> send email to client with "order on its way" message
             */
            sendBtn.setOnClickListener(v -> {
                //send email to buyer
                String mailText = "Your order its on its way\n";
                for(StoreItem item: orders.get(position).getCart())
                    mailText = mailText.concat( item.toString() + "\n");
                mailText = mailText.concat(String.format("Total price %.2f $\n", orders.get(position).getTotalPrice()));

                composeEmail(this,new String[]{orders.get(position).getUser().getEmail()}, mailText);

                toast(this,orders.get(position).getUser().getId() + " : order sent");
                log(AdminPurchaseDashActivity.class, orders.get(position).getUser().getId() + " : order sent");
                logToFireBase(this,orders.get(position).getUser().getId() + " : order sent");


                refreshList();
                dialog.dismiss();
            });

            dialog.show();
            //set dialog view width match parent height wrap content
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        });

    }

    /**
     * refresh OrderCompleted adapter
     */
    private void refreshList() {
        OrdersListViewAdapter OLVA = new OrdersListViewAdapter(AdminPurchaseDashActivity.this,R.layout.layout_purchases_dash_list_row, orders);
        purchaseList.setAdapter(OLVA);
    }

    /**
     * refresh in dialog full order view StoreItem adapter
     * @param cart
     */
    private void singleOrderRefreshList(ArrayList<StoreItem> cart) {
        StoreItemListViewAdapter SILVA = new StoreItemListViewAdapter(
                AdminPurchaseDashActivity.this,
                R.layout.layout_store_item_list_row,
                cart
        );
        singleOrderList.setAdapter(SILVA);
    }

    /**
     * get order from Firebase database in new orders ArrayList
     */
    private void getOrders() {
        orders = new ArrayList<>();
        FirebaseDB.getDataReference().child(FirebaseDB.ORDER_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    for (DataSnapshot dataChild: data.getChildren()) {
                        orders.add(dataChild.getValue(OrderCompleted.class));
                    }
                }

                refreshList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log(AdminPurchaseDashActivity.class,"Fetching orders list canceled");
                logToFireBase(AdminPurchaseDashActivity.this,"Fetching orders list canceled");
            }
        });
    }
}