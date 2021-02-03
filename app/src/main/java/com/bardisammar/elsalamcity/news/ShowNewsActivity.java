package com.bardisammar.elsalamcity.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bardisammar.elsalamcity.spilash.SpilashActivity;
import com.bumptech.glide.Glide;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityShowNewsBinding;
import com.bardisammar.elsalamcity.details.ShowFullScreenActivity;
import com.bardisammar.elsalamcity.home.HomeActivity;
import com.bardisammar.elsalamcity.notifecation.Data;
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

public class ShowNewsActivity extends AppCompatActivity {
ActivityShowNewsBinding binding;
    String title;
    DatabaseReference notyRef;
    FirebaseDatabase database;
    Data blog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_news);


        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder()

                .build();


        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest);

        if (getIntent()!=null) {
            title = getIntent().getStringExtra("title");

        }
        database = FirebaseDatabase.getInstance();
        notyRef = database.getReference().child("News");

        loadDataIntentnt();
        binding.imagbackd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowNewsActivity.this, SpilashActivity.class));
                finish();
            }
        });
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowNewsActivity.this, ShowFullScreenActivity.class);
                intent.putExtra("image", blog.getImage());
                startActivity(intent);
            }
        });
    }

    private void loadDataIntentnt() {
        if (Commen.isNetworkOnline(this)){
            try {
                Query q = notyRef.orderByChild("title").equalTo(title);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            blog = dataSnapshot1.getValue(Data.class);

                            binding.tvTitle.setText(blog.getTitle());
                            binding.tvBody.setText(blog.getBody());

                            Glide.with(ShowNewsActivity.this).load(blog.getImage())
                                    .into(binding.imageView);



                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        else {

            Toast.makeText(this, "انقطع الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShowNewsActivity.this, SpilashActivity.class));
        finish();
    }
}