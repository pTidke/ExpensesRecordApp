package com.example.expensesrecordapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.expensesrecordapp.model.Material;
import com.example.expensesrecordapp.model.Work;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirstFrag extends Fragment implements View.OnClickListener {

    private static final String TAG = "Expenses";
    private MaterialButton datePicker, btnAddMaterial, btnSaveWorkData;
    private TextInputEditText nameWork, nameMaterial, nameSupplier, quantity, price, totalMaterials;
    private TextView totalPrice;
    private NestedScrollView nestedScrollView;
    private LinearLayout linerLayout1, linerLayout2, linerLayout3;
    private TextInputLayout ti1, ti2, ti3, ti4, ti5;
    private Calendar calendar;
    private String mWork, mMaterial, mSupplier, date;
    private int materialTotal, materialQuantity;
    private float materialPrice;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference works = db.collection("Works");

    List<Material> materialsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        nestedScrollView = (NestedScrollView) inflater.inflate(R.layout.fragment_first, container, false);
        initViews();
        return  nestedScrollView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        calendar = Calendar.getInstance();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.datePicker){
            DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), (view1, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
                datePicker.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }

        if(view.getId() == R.id.btnAddMaterial) {

            try {
                mWork = nameWork.getText().toString().toLowerCase();
                mMaterial = nameMaterial.getText().toString().toLowerCase();
                mSupplier = nameSupplier.getText().toString().toLowerCase();
                materialQuantity = Integer.parseInt(quantity.getText().toString());
                materialPrice = Float.parseFloat(price.getText().toString());
                String date = datePicker.getText().toString();

                Material m = new Material(mMaterial, materialQuantity, materialPrice, mSupplier, date, materialPrice * materialQuantity);

                works.document(mWork).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            Work w = document.toObject(Work.class);
                            materialsList = w.getMaterials();
                            materialsList.add(m);
                        }
                        else {
                            materialsList.add(m);
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                });

                nameMaterial.setText("");
                nameSupplier.setText("");
                quantity.setText("");
                price.setText("");
                datePicker.setText("Enter Date");
            }
            catch (Exception e){
                Toast.makeText(view.getContext(), "Enter Data Properly", Toast.LENGTH_SHORT).show();
            }

        }

        if (view.getId() == R.id.btnSaveWorkData){

            try {
                mWork = nameWork.getText().toString().toLowerCase();
                Work w = new Work(mWork, materialsList);
                
                works.document(mWork).set(w)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Success"))
                        .addOnFailureListener(e -> Log.d(TAG, "Failed"));

                nameWork.setText("");
                materialsList.clear();
            }
            catch (Exception e){
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initViews(){
        datePicker = nestedScrollView.findViewById(R.id.datePicker);
        btnAddMaterial = nestedScrollView.findViewById(R.id.btnAddMaterial);
        btnSaveWorkData = nestedScrollView.findViewById(R.id.btnSaveWorkData);
        nameWork = nestedScrollView.findViewById(R.id.nameWork);
        nameMaterial = nestedScrollView.findViewById(R.id.nameMaterial);
        nameSupplier = nestedScrollView.findViewById(R.id.nameSupplier);
        quantity = nestedScrollView.findViewById(R.id.quantity);
        price = nestedScrollView.findViewById(R.id.price);
        nestedScrollView = nestedScrollView.findViewById(R.id.nestedScrollView);
        linerLayout1 = nestedScrollView.findViewById(R.id.linerLayout);
        linerLayout2 = nestedScrollView.findViewById(R.id.linerLayout2);
        linerLayout3 = nestedScrollView.findViewById(R.id.linerLayout3);

        ti1 = nestedScrollView.findViewById(R.id.ti1);
        ti2 = nestedScrollView.findViewById(R.id.ti2);
        ti3 = nestedScrollView.findViewById(R.id.ti3);
        ti4 = nestedScrollView.findViewById(R.id.ti4);
        ti5 = nestedScrollView.findViewById(R.id.ti5);

        btnAddMaterial.setOnClickListener(this);
        datePicker.setOnClickListener(this);
        btnSaveWorkData.setOnClickListener(this);
    }

}
