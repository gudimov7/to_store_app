package com.example.toy_store_app.services;

public class StoreItem {
    private String itemName, pic;
    private ItemDescription description;
    private float price;

    public StoreItem() {
    }

    public StoreItem(String itemName, ItemDescription description, float price, String pic) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ItemDescription getDescription() {
        return description;
    }

    public void setDescription(ItemDescription description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StoreItem{" +
                "\tname:\t" + itemName + "\n" +
                "\tdescription:\t" + description + "\n" +
                "\tprice:\t" + price + "$\n" +
                "}";
    }
}
