package com.example.toy_store_app;

import static com.example.toy_store_app.services.FF.handleAnonymous;
import static com.example.toy_store_app.services.FF.isEditTextEmpty;
import static com.example.toy_store_app.services.FF.log;
import static com.example.toy_store_app.services.FF.logToFireBase;
import static com.example.toy_store_app.services.FF.showPassword;
import static com.example.toy_store_app.services.FF.toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.toy_store_app.adapters.StoreItemListViewAdapter;
import com.example.toy_store_app.firebase.FirebaseAT;
import com.example.toy_store_app.firebase.FirebaseDB;
import com.example.toy_store_app.services.ItemDescription;
import com.example.toy_store_app.services.StoreItem;
import com.example.toy_store_app.services.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Activity show In Store view
 * @author Vyacheslav Gudimov
 */
public class InStoreActivity extends AppCompatActivity {

    private ListView itemsList;
    private ArrayList<StoreItem> inStoreItems;
    private User user;
    private Spinner sortSpinner;
    private ImageButton userInfoBtn;
    private ImageButton cartBtn;
    private NotificationBadge cartBadge;

    /**
     * first function to start as activity starts
     * @param savedInstanceState if has memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_store);
        getSupportActionBar().hide(); //hide title bar

        //initiate activity views
        itemsList = (ListView) findViewById(R.id.inStoreActivity_lv_itemsList);
        sortSpinner = (Spinner) findViewById(R.id.inStoreActivity_sp_sortSpinner);
        userInfoBtn = (ImageButton) findViewById(R.id.inStoreActivity_ib_userInfoBtn);
        cartBtn = (ImageButton) findViewById(R.id.inStoreActivity_ib_cartBtn);
        cartBadge = (NotificationBadge) findViewById(R.id.inStoreActivity_nb_cartBadge);

        //initiate User obj
        user = new User();

        //initiate InStore StoreItem ArrayList
        inStoreItems = new ArrayList<>();

        //initiate spinner button
        sortSpinner.setPrompt("Sort by");
        sortBy();

        //initiate StoreItem's list
        initiateStoreItemList();

        //authenticate user with Firebase and get data
        getUser();

        /**
         * set on ListView StoreItem click listener
         */
        itemsList.setOnItemClickListener((parent, view, position, id) -> {
            //start new dialog to show Item clicked full description
            Dialog dialog = new Dialog(InStoreActivity.this);
            dialog.setContentView(R.layout.dialog_store_item_full_view);

            //initiate dialog views
            TextView itemNameHeader = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemNameHeader);
            TextView itemName = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemName);
            TextView itemAge = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemAge);
            TextView itemColor = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemColor);
            TextView itemMaterial = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemMaterial);
            TextView itemMade = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemMade);
            TextView itemPrice = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemPrice);
            ImageView itemPic = dialog.findViewById(R.id.fullStoreIte_dialog_iv_itemPic);
            Button backBtn = dialog.findViewById(R.id.fullStoreItem_dialog_btn_back);
            Button buyBtn = dialog.findViewById(R.id.fullStoreItem_dialog_btn_buy);

            //set data to views with Item clicked data
            itemNameHeader.setText(inStoreItems.get(position).getItemName());
            itemName.setText("Name:\t" + inStoreItems.get(position).getItemName());
            itemAge.setText("Age:\t" + inStoreItems.get(position).getDescription().getAge() + "+");
            itemColor.setText("Color:\t" + inStoreItems.get(position).getDescription().getColor());
            itemMaterial.setText("Material:\t" + inStoreItems.get(position).getDescription().getMaterial());
            itemMade.setText("Made in:\t" + inStoreItems.get(position).getDescription().getMade());
            itemPrice.setText(String.format("Price:\t%.2f$", inStoreItems.get(position).getPrice()));
            Picasso.get().load(inStoreItems.get(position).getPic()).resize(70,70).into(itemPic);

            //set back button click Listener -> dismiss dialog
            backBtn.setOnClickListener(v -> dialog.dismiss());

            //set buy button click listener -> add StoreItem to User obj cart -> dismiss dialog
            buyBtn.setOnClickListener(v-> {
                user.getOrder().addItemToCart(inStoreItems.get(position));
                FirebaseDB
                        .getDataReference()
                        .child(FirebaseDB.USERS_CHILD)
                        .child(FirebaseAT.getAuth().getUid())
                        .child(FirebaseDB.CART_CHILD)
                        .setValue(user.getOrder());
                updateBadge();
                dialog.dismiss();
            });

            dialog.show();

            //set dialog window width match parent height wrap content
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        });

        /**
         * set cart button click listener -> start UserCart activity
         */
        cartBtn.setOnClickListener(v -> startActivity(new Intent(InStoreActivity.this, UserCartActivity.class)));

        /**
         * set on user button click listener -> start Register activity with extra data -> true if existing user / false if anonymous
         */
        userInfoBtn.setOnClickListener(v -> {
            Intent intent  = new Intent(InStoreActivity.this, RegisterActivity.class);
            //if user has no email == anonymous
            if( FirebaseAT.getAuth().getCurrentUser().getEmail() == null) {
                handleAnonymous(InStoreActivity.this);
            } else {
                intent.putExtra("returnedUser",true);
                startActivity(intent);
            }

//            if (FirebaseAT.getAuth().getCurrentUser().getEmail() == null ) {
//
//                //start new dialog
//                Dialog dialog = new Dialog(InStoreActivity.this);
//                dialog.setContentView(R.layout.dialog_register_or_login);
//
//                //initiate all dialog views
//                Button loginBtn = (Button) dialog.findViewById(R.id.loginOrRegisterDialog_btn_login);
//                Button registerBtn = (Button) dialog.findViewById(R.id.loginOrRegisterDialog_btn_register);
//
//                /**
//                 * set on login button click listener -> start new login dialog
//                 */
//                loginBtn.setOnClickListener(dialogV -> {
//                    //start new dialog
//                    dialog.setContentView(R.layout.dialog_login);
//
//                    //initiate all dialog views
//                    EditText usernameET = (EditText) dialog.findViewById(R.id.loginDialog_et_username);
//                    EditText passwordET = (EditText) dialog.findViewById(R.id.loginDialog_et_password);
//                    CheckBox showPasswordBox = (CheckBox) dialog.findViewById(R.id.loginDialog_cb_password);
//                    Button login = (Button) dialog.findViewById(R.id.loginDialog_btn_login);
//
//                    /**
//                     * login set on click listener -> auth with Firebase
//                     * ->reload activity
//                     * ! username and password fields cannot be empty
//                     */
//                    login.setOnClickListener(nextDialogV -> {
//                        if (!isEditTextEmpty(usernameET) && ! isEditTextEmpty(passwordET)) {
//                            String username = usernameET.getText().toString();
//                            String password = passwordET.getText().toString();
//
//                            FirebaseAT.getAuth().signInWithEmailAndPassword(username, password).addOnSuccessListener(authResult -> {
//                                InStoreActivity.this.onResume();
//
//                                toast(InStoreActivity.this,"Login successfully");
//                                log(InStoreActivity.class,username + ": logged in successfully");
//                                logToFireBase(InStoreActivity.this,username + ": logged in successfully");
//                                dialog.dismiss();
//                            }).addOnFailureListener(authResult -> {
//                                toast(InStoreActivity.this, "login denied");
//                                log(InStoreActivity.class,username + ": login failed");
//                                logToFireBase(InStoreActivity.this,username + ": login failed");
//                            });
//
//                        } else {
//                            toast(InStoreActivity.this,"Please enter username and password");
//                        }
//                    });
//
//                    //set on check reveal password
//                    showPassword(showPasswordBox,passwordET);
//                });
//
//                /**
//                 * set on register btn click listener -> start register activity
//                 */
//                registerBtn.setOnClickListener(dialogV -> {
//                    intent.putExtra("returnedUser", false);
//                    dialog.dismiss();
//                    startActivity(intent);
//                });
//
//                //show dialog
//                dialog.show();
//                //set dialog view width match parent height wrap content
//                Window window = dialog.getWindow();
//                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//
        });

        /**
         * set on SpinnerList item clicked -> sort list by switch case
         */
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortSpinner.getChildAt(0).setClickable(false);
                switch (position) {
                    case 1: //sort by name a-z
                        Collections.sort(inStoreItems, (o1, o2) ->{
                            return o1.getItemName().compareTo(o2.getItemName());
                        });
                        break;
                    case 2: //sort by name z-a
                        Collections.sort(inStoreItems, (o1, o2) -> {
                            return o2.getItemName().compareTo(o1.getItemName());
                        });
                        break;
                    case 3: //sort by price l-h
                        Collections.sort(inStoreItems, (o1, o2) -> {
                            return (int)( o1.getPrice() - o2.getPrice());
                        });
                        break;
                    case 4: //sort by price h-l
                        Collections.sort(inStoreItems, (o1, o2) -> {
                            return (int)( o2.getPrice() - o1.getPrice());
                        });
                        break;
                }
                //refresh ListView with new sort
                refreshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * refresh StoreItem adapter
     */
    private void refreshList() {
        StoreItemListViewAdapter SILVA = new StoreItemListViewAdapter(InStoreActivity.this,R.layout.layout_store_item_list_row, inStoreItems);
        itemsList.setAdapter(SILVA);
    }

    /**
     * authenticate user with Firebase and create User obj from Firebase data
     */
    private void getUser() {
        FirebaseDB.getDataReference().child(FirebaseDB.USERS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    if(data.getKey().equals(FirebaseAT.getAuth().getUid())) {
                        user = data.getValue(User.class);
                        updateBadge();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log(InStoreActivity.class,"fetched user canceled");
                logToFireBase(InStoreActivity.this,"fetched user canceled");
            }
        });
    }

    /**
     * get items from Firebase realTime database
     * create new StoreItem arrayList
     */
    private void initiateStoreItemList() {
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
                refreshList();
                log(InStoreActivity.class,"fetched item list successfully");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log(InStoreActivity.class,"refresh list canceled");
                logToFireBase(InStoreActivity.this,"refresh canceled");
            }
        });
    }

    /**
     * set cart button badge with User obj cart size
     */
    private void updateBadge() {
        cartBadge.setNumber(user.getOrder().getCart().size());
    }

    /**
     * sort by spinner list adapter
     */
    private void sortBy() {
        ArrayAdapter<CharSequence> sortByAdp = ArrayAdapter.createFromResource(this, R.array.sort_by, R.layout.textview_spinner_single_row);
        sortSpinner.setAdapter(sortByAdp);
    }

    /**
     * on resume activity
     * re-get user
     * refresh Store item list
     * update cart badge
     */
    @Override
    protected void onResume() {
        super.onResume();
        getUser();
        refreshList();
        updateBadge();
    }

    /**
     * on restart activity
     * re-get user
     * refresh Store item list
     * update cart badge
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        getUser();
        refreshList();
        updateBadge();
    }
}