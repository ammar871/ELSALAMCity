package com.elsalmcity.elsalamcity.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.databinding.ActivitySearchBinding;
import com.elsalmcity.elsalamcity.details.DetailsActivity;
import com.elsalmcity.elsalamcity.pojo.Product;
import com.elsalmcity.elsalamcity.products.AdpterProduct;
import com.elsalmcity.elsalamcity.products.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    DatabaseReference catogry;
    FirebaseDatabase database;
    List<String> suggestlist = new ArrayList<>();
    AdpterProduct adpterProduct;
    ArrayList<Product> list;

    String textSearch;
    //search bar
    FirebaseRecyclerAdapter<Product, ProductViewHolder> searchadpter;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        if (getIntent() != null) {
            textSearch = getIntent().getStringExtra("search");
        }
        //firebase
        database = FirebaseDatabase.getInstance();
        catogry = database.getReference().child("prodects");
        list = new ArrayList<>();
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


        if (textSearch != null)
            loadDatabasclothFame(textSearch);

    }




    private void intentMothed(Class a, Product value) {

        Intent intent = new Intent(this, a);
        intent.putExtra("product", value);
        startActivity(intent);
    }

    private void startSearch(CharSequence text) {

        Query query = catogry.orderByChild("name").equalTo(text.toString());

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        searchadpter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {


            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_product, parent, false);

                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder menuViewHolder, int i, @NonNull final Product product) {
                menuViewHolder.name.setText(product.getName());
                menuViewHolder.desc.setText(product.getDescshort());


                Glide.with(SearchActivity.this)
                        .load(product.getUris().get(0))
                        .into(menuViewHolder.imageView);

                menuViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intentMothed(DetailsActivity.class, product);
                    }
                });

            }


        };
        binding.recyclerSearsh.setAdapter(searchadpter);
        searchadpter.startListening();

    }

    private void loadDatabasclothFame(String text) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchActivity.this, InputSearshActivity.class);

        startActivity(intent);
        finish();
    }
}