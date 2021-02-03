package com.bardisammar.elsalamcity.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityNewsBinding;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class NewsActivity extends AppCompatActivity {

    ActivityNewsBinding binding;

    ArrayList<Data> list;
    AdpterNews adpterNotifecation;
    DatabaseReference notyRef;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.lef_to_righit,
                R.anim.right_to_left);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder()

                .build();


        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest);
        list = new ArrayList<>();
        binding.rcList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rcList.setHasFixedSize(true);

        //firebase
        database = FirebaseDatabase.getInstance();
        notyRef = database.getReference().child("News");
        try {
            loadDataIntentnt();
        }catch (Exception ignored){

        }


        binding.imagbackd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });

    }
    private void loadDataIntentnt() {
        if (Commen.isNetworkOnline(this)){
            try {
                binding.rcList.setVisibility(View.VISIBLE);
                binding.linerNetWork.setVisibility(View.GONE);
                notyRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.d("Tagstorag", "onSuccess :" + dataSnapshot1.child("name").getValue());
                            Data blog = dataSnapshot1.getValue(Data.class);


                            list.add(blog);
                            Collections.reverse(list);

                        }
                        adpterNotifecation = new AdpterNews(list, NewsActivity.this);
                        binding.rcList.setAdapter(adpterNotifecation);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            binding.rcList.setVisibility(View.GONE);
            binding.linerNetWork.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }}