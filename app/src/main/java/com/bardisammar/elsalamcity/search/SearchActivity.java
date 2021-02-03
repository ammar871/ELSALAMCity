package com.bardisammar.elsalamcity.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Toast;

import com.bardisammar.elsalamcity.R;

import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivitySearchBinding;

import com.bardisammar.elsalamcity.pojo.Product;
import com.bardisammar.elsalamcity.products.AdpterProduct;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    DatabaseReference catogry;
    FirebaseDatabase database;
    AdpterProduct adpterProduct;
    ArrayList<Product> list;
    String textSearch;
    String nameCato;


static int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        //adds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        database = FirebaseDatabase.getInstance();
        catogry = database.getReference().child("prodects");
        list = new ArrayList<>();
        if (getIntent() != null) {
            textSearch = getIntent().getStringExtra("search");
            nameCato = getIntent().getStringExtra("nameCto");
            loadDatabasclothFame(textSearch);
        }
        //firebase

        binding.imagback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, InputSearshActivity.class));
                finish();
            }
        });

        //recycler
        binding.recyclerSearsh.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        binding.recyclerSearsh.setHasFixedSize(true);











    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void loadDataCto(String nameCato) {
        if (Commen.isNetworkOnline(this)){
            try {

                Query applesQuery = catogry.orderByChild("id").equalTo(nameCato);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.d("Tagstorag", "name: " + dataSnapshot.child("name").getValue());
                            Product blog = dataSnapshot1.getValue(Product.class);


                            list.add(blog);
                        }

                        adpterProduct = new AdpterProduct(list, SearchActivity.this);


                        binding.recyclerSearsh.setAdapter(adpterProduct);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }




    private void loadDatabasclothFame(String text) {
if (Commen.isNetworkOnline(this)){
    try {


        Query applesQuery = catogry.orderByChild("name").equalTo(text);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d("Tagstorag", "name: " + dataSnapshot.child("name").getValue());
                    Product blog = dataSnapshot1.getValue(Product.class);


                    list.add(blog);
                }
                count=list.size();
                adpterProduct = new AdpterProduct(list, SearchActivity.this);


                binding.recyclerSearsh.setAdapter(adpterProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (count==0)
            loadDataCto(nameCato);
    } catch (Exception e) {
        e.printStackTrace();
    }

}else {

    Toast.makeText(this, "انقطع الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
}

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchActivity.this, InputSearshActivity.class);

        startActivity(intent);
        finish();
    }
}