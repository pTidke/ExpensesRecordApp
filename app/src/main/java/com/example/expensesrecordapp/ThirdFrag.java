package com.example.expensesrecordapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensesrecordapp.model.Material;
import com.example.expensesrecordapp.model.Work;
import com.example.expensesrecordapp.ui.main.SupplierAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.expensesrecordapp.R.layout.list_view;

public class ThirdFrag extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference worksRef = db.collection("Works");

    private List<Material> materialsList = new ArrayList<>();
    private SupplierAdapter adapter1;
    private List<String> lv = new ArrayList<>();
    private CollectionReference works = db.collection("Works");
    ListView lvSuppliers;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_third, container, false);
        lvSuppliers = view.findViewById( R.id.LvSuppliers );
        setList();

        lvSuppliers.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nameS = parent.getItemAtPosition( position ).toString();
                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(parent.getContext());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.bottom_dailog, null);
                RecyclerView suppliersRecyclerView = dialogView.findViewById(R.id.list1);
                TextView tv = dialogView.findViewById(R.id.tv1);
                tv.setText(toTitleCase( nameS ));
                TextView gt = dialogView.findViewById(R.id.grandTotal1);
                materialsList.clear();

                worksRef.get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            float sum = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Work w = document.toObject(Work.class);
                                List<Material> m = w.getMaterials();
                                for (Material material : m){
                                    if(material.getNameSupplier().equals( nameS )){
                                        Log.d( "TAG", "onComplete: " + material.getNameMaterial() );
                                        materialsList.add( material );
                                        sum = sum + material.getCostTotal();
                                        adapter1.notifyDataSetChanged();
                                    }
                                    gt.setText("Grand Total : " + sum);
                                }

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                } );

                adapter1 = new SupplierAdapter( materialsList );
                suppliersRecyclerView.setLayoutManager(new LinearLayoutManager(mBottomSheetDialog.getContext()));
                suppliersRecyclerView.setAdapter(adapter1);

                mBottomSheetDialog.setContentView(dialogView);
                mBottomSheetDialog.show();
                materialsList.clear();
            }
        } );

        return view;
    }

    private void setList() {

        works.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Work w = document.toObject(Work.class);
                                List<Material> m = w.getMaterials();
                                for (Material material : m){
                                    if(!lv.contains(material.getNameSupplier()))
                                        lv.add(material.getNameSupplier());
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( getContext(), list_view, lv ){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Set the border of View (ListView Item)
                view.setBackground(getContext().getDrawable(R.drawable.border));

                // Return the view
                return view;
            }

        };
        arrayAdapter.notifyDataSetChanged();
        lvSuppliers.setClickable( false );
        lvSuppliers.setAdapter( arrayAdapter );

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

    @Override
    public void onResume() {
        super.onResume();
        setList();
    }

}

