package com.example.toy_store_app.services;

/**
 * User obj address class
 * @author Vyacheslav Gudimov
 */
public class Address {
    private String street, city, country;

    /**
     * Empty constructor
     */
    public Address() {
    }

    /**
     * full constructor
     * @param street String street name
     * @param city String city name
     * @param country String country name
     */
    public Address(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }

    /**
     * return private street value
     * @return String Street value
     */
    public String getStreet() {
        return street;
    }

    /**
     * set new private street value
     * @param street String street value to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * * return private city value
     * @return String city value
     */
    public String getCity() {
        return city;
    }

    /**
     * set new private city value
     * @param city String city value to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * return private country value
     * @return String country value
     */
    public String getCountry() {
        return country;
    }

    /**
     * set new private country value
     * @param country String country value to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * override Object to string function for address class
     * @return Address all params multiple lines
     */
    @Override
    public String toString() {
        return "Address{\n" +
                "\tstreet:\t" + street + "\n" +
                "\tcity:\t" + city + "\n" +
                "\tcountry:\t" + country + "\n" +
                "}";
    }
}
