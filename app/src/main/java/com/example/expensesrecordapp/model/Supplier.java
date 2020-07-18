package com.example.expensesrecordapp.model;

public class Supplier {

    private float grandTotal;
    private float payment;

    private Supplier() {
    }

    private Supplier(float grandTotal, float payment){
        this.grandTotal = grandTotal;
        this.payment = payment;
    }

    private float getGrandTotal(){
        return grandTotal;
    }

    private float getPayment(){
        return payment;
    }

}
