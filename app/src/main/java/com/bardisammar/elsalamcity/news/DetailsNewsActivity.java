package com.bardisammar.elsalamcity.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.databinding.ActivityDetailsNewsBinding;
import com.bardisammar.elsalamcity.details.ShowFullScreenActivity;
import com.bardisammar.elsalamcity.notifecation.Data;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class DetailsNewsActivity extends AppCompatActivity {
ActivityDetailsNewsBinding binding;
Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_news);
        if (getIntent()!=null){
            data=getIntent().getParcelableExtra("news");
            if (data!=null){

                binding.tvTitle.setText(data.getTitle());
                binding.tvBody.setText(data.getBody());

                Glide.with(DetailsNewsActivity.this).load(data.getImage())
                        .into(binding.imageView);


                binding.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailsNewsActivity.this, ShowFullScreenActivity.class);
                        intent.putExtra("image", data.getImage());
                        startActivity(intent);
                    }
                });
            }

        }
        //adds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.imagbackd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
    }
}