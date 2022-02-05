package com.example.toy_store_app.services;

import java.util.ArrayList;
import java.util.Date;

public class OrderCompleted extends Order{
    private User user;
    private String date;

    public OrderCompleted(ArrayList<StoreItem> cart, User user, String date) {
        super(cart);
        this.user = user;
        this.date = date;
    }

    public OrderCompleted() {
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return user.getName() + ":\n" +
                user.getOrder() + "\n" +
                user.getAddress() + "\n" +
                date;
    }
}
