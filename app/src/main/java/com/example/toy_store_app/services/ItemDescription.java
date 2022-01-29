package com.example.toy_store_app.services;

public class ItemDescription {
    private String age, color, material, made;

    public ItemDescription() {
    }

    public ItemDescription(String age, String color, String material, String made) {
        this.age = age;
        this.color = color;
        this.material = material;
        this.made = made;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    @Override
    public String toString() {
        return "age:\t" + age + "\n" +
            "color:\t" + color + "\n" +
            "material:\t" + material + "\n" +
            "made:\t" + made + "\n";
    }
}
