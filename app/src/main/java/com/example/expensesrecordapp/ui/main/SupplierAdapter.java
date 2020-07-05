package com.example.expensesrecordapp.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.expensesrecordapp.R;
import com.example.expensesrecordapp.model.Material;

import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierHolder> {

    private List<Material> mList;

    public SupplierAdapter(List<Material> mList) {
            this.mList = mList;
    }

    @Override
    public SupplierHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.supplier_item, parent, false);
        return new SupplierHolder(listItem);
    }

    @Override
    public void onBindViewHolder(SupplierHolder supplierHolder, int position) {
        final Material material = mList.get(position);
        supplierHolder.nameCardMaterial.setText(toTitleCase(material.getNameMaterial()));
        supplierHolder.nameCardDate.setText("Date : " + material.getDate());
        supplierHolder.nameCardQuantity.setText("Quantity : " + Integer.toString(material.getQuantity()));
        supplierHolder.nameCardTotal.setText("Total Price : " + Float.toString(material.getCostTotal()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class SupplierHolder extends RecyclerView.ViewHolder{

        public TextView nameCardMaterial;
        public TextView nameCardDate;
        public TextView nameCardQuantity;
        public TextView nameCardTotal;

        public SupplierHolder(View itemView) {
            super(itemView);
            nameCardDate = itemView.findViewById(R.id.nameCardDate1);
            nameCardMaterial = itemView.findViewById(R.id.nameCardMaterial1);
            nameCardQuantity = itemView.findViewById(R.id.nameCardQuantity1);
            nameCardTotal = itemView.findViewById(R.id.nameCardTotal1);        }
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }


}
