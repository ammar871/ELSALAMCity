package com.bardisammar.elsalamcity.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.FragmentMarketsSearshBinding;
import com.bardisammar.elsalamcity.pojo.Product;
import com.bardisammar.elsalamcity.products.AdpterProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MarketsSearshFragment extends Fragment {
    DatabaseReference catogry;
    FirebaseDatabase database;
    AdpterProduct adpterProduct;
    ArrayList<Product> list;

    public MarketsSearshFragment() {
        // Required empty public constructor
    }

    public static MarketsSearshFragment newInstance() {
        return new MarketsSearshFragment();
    }

    FragmentMarketsSearshBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_markets_searsh, container, false);
        View view = binding.getRoot();
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        catogry = database.getReference().child("prodects");
        //recycler
        binding.listSearsh.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false));
        binding.listSearsh.setHasFixedSize(true);

        binding.edSearsh.addTextChangedListener(watch);

        return view;
    }

    TextWatcher watch = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable arg0) {


        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {


        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            list.clear();
            loadDataCto(binding.edSearsh.getText().toString());
        }
    };

    private void loadDataCto(String nameCato) {
        if (Commen.isNetworkOnline(getActivity())) {
            try {

                Query applesQuery = catogry.orderByChild("name").startAt(nameCato);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.d("Tagstorag", "name: " + dataSnapshot.child("name").getValue());
                            Product blog = dataSnapshot1.getValue(Product.class);


                            list.add(blog);
                        }

                        adpterProduct = new AdpterProduct(list, getActivity());


                        binding.listSearsh.setAdapter(adpterProduct);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getActivity(), "لايوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        }

    }
}