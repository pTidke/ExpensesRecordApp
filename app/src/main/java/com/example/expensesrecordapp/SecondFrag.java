package com.example.expensesrecordapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.expensesrecordapp.model.Material;
import com.example.expensesrecordapp.model.Work;
import com.example.expensesrecordapp.ui.main.MaterialAdapter;
import com.example.expensesrecordapp.ui.main.WorkAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class SecondFrag extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference worksRef = db.collection("Works");

    private WorkAdapter adapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, container, false);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {

        Query query = worksRef;

        FirestoreRecyclerOptions<Work> options = new FirestoreRecyclerOptions.Builder<Work>()
                .setQuery(query, Work.class)
                .build();

        adapter = new WorkAdapter(options);

        RecyclerView worksRecyclerView = view.findViewById(R.id.worksRecyclerView);
        worksRecyclerView.setHasFixedSize(true);
        worksRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        worksRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            Work work = documentSnapshot.toObject(Work.class);
            List<Material> mList = work.getMaterials();
            BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getContext());
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
            TextView tv = dialogView.findViewById(R.id.tv);
            tv.setText(toTitleCase(work.getNameWork()));
            TextView gt = dialogView.findViewById(R.id.grandTotal);
            gt.setText("Grand Total : " + Float.toString(work.getGrandTotal()));
            RecyclerView listView = dialogView.findViewById(R.id.list);
            MaterialAdapter adapter = new MaterialAdapter(mList);
            listView.setHasFixedSize(true);
            listView.setLayoutManager(new LinearLayoutManager(mBottomSheetDialog.getContext()));
            listView.setAdapter(adapter);

            mBottomSheetDialog.setContentView(dialogView);
            mBottomSheetDialog.show();
        });

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

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService( Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
