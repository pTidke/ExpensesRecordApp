package com.example.expensesrecordapp.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Supplier {
    private float payment;
    private List<Material> materials;

    public Supplier() {
    }

    public Supplier(float payment, List<Material> materials) {
        this.payment = payment;
        this.materials = materials;
    }

    public float getGrandTotal(){
        Iterator<Material> crunchyIterator = materials.iterator();
        float sum = 0;
        while (crunchyIterator.hasNext()) {
            sum += crunchyIterator.next().getCostTotal();
        }
        return sum;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }
    public void setMaterialsInList(Material material) {
        this.materials.add( material );
    }


    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public float getPayment() {
        return payment;
    }

    public List<Material> getMaterials() {
        return materials;
    }
}
