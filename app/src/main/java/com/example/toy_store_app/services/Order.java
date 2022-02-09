package com.example.toy_store_app.services;

import java.util.ArrayList;

/**
 * User obj order class
 * @author Vyacheslav Gudimov
 */
public class Order {
    private ArrayList <StoreItem> cart;
    private float totalPrice;

    /**
     * full constructor
     * @param cart Array list with item cart
     */
    public Order(ArrayList<StoreItem> cart) {
        this();
        this.cart = cart;
        setTotalPrice();
    }

    /**
     * empty constructor
     * only initiate cart array list
     */
    public Order() {
        cart = new ArrayList<>();
        setTotalPrice();
    }

    /**
     * return private cart value
     * @return ArrayList cart value
     */
    public ArrayList<StoreItem> getCart() {
        return cart;
    }

    /**
     * set private cart value
     * @param cart ArrayList to be set
     */
    public void setCart(ArrayList<StoreItem> cart) {
        this.cart = cart;
        setTotalPrice();
    }

    /**
     * return private totalPrice value
     * @return float totalPrice cart value
     */
    public float getTotalPrice() {
        this.setTotalPrice();
        return totalPrice;
    }

    /**
     * set private totalPrice value
     * calculate total price for each item in cart
     * and sets in private totalPrice param
     */
    private boolean setTotalPrice(){
        totalPrice = 0;
        if (cart != null) {
            for (StoreItem item: cart)
                this.totalPrice += item.getPrice();
            return true;
        }
        return false;
    }

    /**
     * add new item to cart arrayList
     * @param item StoreItem to add in cart
     */
    public void addItemToCart(StoreItem item) {
        if (cart == null) {
            cart = new ArrayList<>();
        }
        cart.add(item);
    }

    /**
     * remove StoreItem from cart if exist
     * @param item StoreItem to be removed
     * @return true if item removed successfully
     */
    public boolean removeItemFromCart(StoreItem item) {
        if (cart != null && cart.contains(item)) {
            cart.remove(item);
            return true;
        }
        return false;
    }
}
