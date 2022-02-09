package com.example.toy_store_app.services;

import static com.example.toy_store_app.services.FF.calendarDate;

import java.util.ArrayList;

/**
 * OrderCompleted class extends Order class by adding User obj
 * @author Vyacheslav Gudimov
 */
public class OrderCompleted extends Order{
    private User user;
    private String date;

    /**
     * full constructor
     * @param cart cart ArrayList
     * @param user User obj
     */
    public OrderCompleted(ArrayList<StoreItem> cart, User user, String date) {
        super(cart);
        this.user = user;
        this.date = calendarDate();
    }

    /**
     * empty constructor
     */
    public OrderCompleted() {
        super();
    }

    /**
     * return private user value
     * @return User obj user value
     */
    public User getUser() {
        return user;
    }

    /**
     * set private user value
     * @param user User obj to be set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * return private date value
     * @return String date value
     */
    public String getDate() {
        return date;
    }

    /**
     * set private date value
     */
    public void setDate() {
        this.date = calendarDate();
    }

    /**
     * override Object to string function for OrderCompleted class
     * @return OrderCompleted all params multiple lines
     */
    @Override
    public String toString() {
        return user.getName() + ":\n" +
                user.getOrder() + "\n" +
                user.getAddress() + "\n" +
                date;
    }
}
