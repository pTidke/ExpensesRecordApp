package com.example.expensesrecordapp.model;

import java.util.Iterator;

public class Material{

    private String nameMaterial;
    private int quantity;
    private float price;
    private String nameSupplier;
    private String date;
    private float costTotal;

    public Material() {
    }

    public Material(String nameMaterial, int quantity, float price, String nameSupplier, String date, float costTotal) {
        this.nameMaterial = nameMaterial;
        this.quantity = quantity;
        this.price = price;
        this.nameSupplier = nameSupplier;
        this.date = date;
        this.costTotal = costTotal;
    }

    public String getNameMaterial() {
        return nameMaterial;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public String getNameSupplier() {
        return nameSupplier;
    }

    public String getDate() {
        return date;
    }

    public float getCostTotal() {
        return costTotal;
    }
}
