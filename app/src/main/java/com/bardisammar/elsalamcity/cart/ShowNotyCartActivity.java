package com.bardisammar.elsalamcity.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityShowNotyCartBinding;
import com.bardisammar.elsalamcity.details.ShowFullScreenActivity;
import com.bardisammar.elsalamcity.home.HomeActivity;
import com.bardisammar.elsalamcity.notifecation.ShowNotiyActivity;
import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;
import com.bardisammar.elsalamcity.pojo.Product;
import com.bardisammar.elsalamcity.spilash.SpilashActivity;
import com.bumptech.glide.Glide;
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

public class ShowNotyCartActivity extends AppCompatActivity {
    String title;
    ActivityShowNotyCartBinding binding;
    DatabaseReference notyRef;
    FirebaseDatabase database;
    Pro_Of_Product blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_noty_cart);
        if (getIntent()!=null) {
            title = getIntent().getStringExtra("title");
            Log.d("titlaaa", title);

        }

        //adds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        database = FirebaseDatabase.getInstance();
        notyRef = database.getReference().child("اعلانات");

        loadDataIntentnt();
        binding.imagbackd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowNotyCartActivity.this, SpilashActivity.class));
                finish();
            }
        });


        binding.showImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowNotyCartActivity.this, ShowFullScreenActivity.class);
                intent.putExtra("image", blog.getImage());
                startActivity(intent);
            }
        });

        binding.linerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowNotyCartActivity.this, DetailsCartActivity.class);
                intent.putExtra("proName",key);
                startActivity(intent);
            }
        });
    }


    private void loadDataIntentnt() {
        if (Commen.isNetworkOnline(this)){
            try {
                Query q = notyRef.orderByChild("name").equalTo(title);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            blog = dataSnapshot1.getValue(Pro_Of_Product.class);

                            binding.tvName.setText(blog.getName());
                            binding.tvDesc.setText(blog.getDescShort());
                            binding.tvDescCompleted.setText(blog.getDescription());

                            Glide.with(ShowNotyCartActivity.this).load(blog.getImage())
                                    .into(binding.showImages);


                        }

                           getkey(blog.getName());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShowNotyCartActivity.this, SpilashActivity.class));
        finish();
    }
    String key;
    private void getkey(String name) {

        final DatabaseReference bunner =  database.getReference("اعلانات");
        Query mQuery = bunner.orderByChild("name").equalTo(name);
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                    key=postsnapshot.getKey();

                }
                Commen.print("sliderkey", key);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}