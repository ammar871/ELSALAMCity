package com.bardisammar.elsalamcity.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityProductsBinding;
import com.bardisammar.elsalamcity.pojo.Product;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ProductsActivity extends AppCompatActivity {
    public static String getMenuId;

    DatabaseReference catogry;
    FirebaseDatabase database;
    private LinearLayoutManager layoutManager;
    AdpterProduct adpterProduct;
    ArrayList<Product> list;
    ActivityProductsBinding binding;


    String nameCatogray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.lef_to_righit,
                R.anim.right_to_left);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products);



        if (getIntent() != null) {
            getMenuId = getIntent().getStringExtra("menuId");
            nameCatogray = getIntent().getStringExtra("name");
            binding.tvTitle.setText(nameCatogray);
        }


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        list = new ArrayList<>();
        binding.listProduct.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.listProduct.setHasFixedSize(true);
        //firebase
        database = FirebaseDatabase.getInstance();
        catogry = database.getReference().child("prodects");

        try {
            loadDataIntentnt();
        }catch (Exception ignored){

        }



    }

    private void loadDataIntentnt() {
        if (Commen.isNetworkOnline(this)){

            try {
                FirebaseDatabase.getInstance().getReference().child("prodects").orderByChild("id").equalTo(getMenuId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.d("Tagstorag", "onSuccess: " + dataSnapshot1.child("name").getValue());
                            Product blog = dataSnapshot1.getValue(Product.class);


                            list.add(blog);
                            Collections.reverse(list);

                        }
                        adpterProduct = new AdpterProduct(list, ProductsActivity.this);
                        binding.listProduct.setAdapter(adpterProduct);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {

            Toast.makeText(this, "انقطع الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
        }


    }
}
