package com.elsalmcity.elsalamcity.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.databinding.ActivityProductsBinding;
import com.elsalmcity.elsalamcity.pojo.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ProductsActivity extends AppCompatActivity {
    public static String getMenuId;

    DatabaseReference catogry;
    FirebaseDatabase database;

    AdpterProduct adpterProduct;
    ArrayList<Product> list;
    ActivityProductsBinding binding;


    String nameCatogray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products);

        if (getIntent() != null) {
            getMenuId = getIntent().getStringExtra("menuId");
            nameCatogray = getIntent().getStringExtra("name");
            binding.tvTitle.setText(nameCatogray);
        }
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

        FirebaseDatabase.getInstance().getReference().child("prodects").orderByChild("id").equalTo(getMenuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
    }
}
