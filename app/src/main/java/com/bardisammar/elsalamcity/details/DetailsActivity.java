package com.bardisammar.elsalamcity.details;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bardisammar.elsalamcity.BuildConfig;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.databinding.ActivityDetailsBinding;
import com.bardisammar.elsalamcity.pojo.Product;
import com.bardisammar.elsalamcity.products.AdpterImageDetail;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityDetailsBinding binding;
    Product product;
    AdpterImageDetail adpterImageDetail;
    ArrayList<String> listImage;

    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);


        binding.listImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        //adds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        getAdds();

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);


        binding.listImages.setHasFixedSize(true);
        binding.linerShare.setOnClickListener(this);
        binding.linerPage.setOnClickListener(this);
        binding.linerCall.setOnClickListener(this);
        binding.linerWhats.setOnClickListener(this);
        binding.tvMaps.setOnClickListener(this);
        binding.imageView.setOnClickListener(this);
        binding.imagbackd.setOnClickListener(this);
        listImage = new ArrayList<>();

        if (getIntent() != null) {
            product = getIntent().getParcelableExtra("product");

        }

        if (product != null) {
            binding.tvName.setText(product.getName());
            binding.tvDesc.setText(product.getDesc());
            binding.tvAddress.setText(product.getAddress());
            Glide.with(this).load(product.getUris().get(0))
                    .into(binding.imageView);
            binding.tvShowAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsActivity.this, ShowImagsActivity.class);
                    intent.putExtra("productimages", product);
                    startActivity(intent);
                }
            });

            int size = product.getUris().size();
            for (int i = 0; i < size; i++) {
                String image = product.getUris().get(i);
                listImage.add(image);
                Log.d("Tagstorag", "onSuccess: " + image);

            }

            adpterImageDetail = new AdpterImageDetail(listImage, DetailsActivity.this);
            binding.listImages.setAdapter(adpterImageDetail);

        }

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ShowFullScreenActivity.class);
                intent.putExtra("image", product.getUris().get(0));
                startActivity(intent);
            }
        });


    }
    private void getAdds() {
        mInterstitialAd = new InterstitialAd(this);
        MobileAds.initialize(this,"ca-app-pub-3315263708203388~3405012481");
        mInterstitialAd.setAdUnitId("ca-app-pub-3315263708203388/7910878488");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                //  mInterstitialAd.show();

            }
        });


        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {

                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liner_share:
                try {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareMessage = "الان علي ابليكيشن سوق برديس " + "\n";
                    String shareName = product.getName() + "\n";
                    String shareNumber = "رقم الهاتف " + product.getPhoneNumber() + "\n";
                    String shareAll = shareName + shareMessage + shareNumber;

                    shareMessage = shareAll + "https://play.google.com/store/apps/details?id="
                            + BuildConfig.APPLICATION_ID + "\n\n";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(sharingIntent, "تطبيق سوق برديس"));
                } catch (Exception e) {

                    Toast.makeText(this, "فشلت العملية", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.liner_page:
                PackageManager packageManager = this.getPackageManager();
                if (getOpenFacebookIntent().resolveActivity(packageManager) != null) {
                    startActivity(getOpenFacebookIntent());
                } else {
                    Toast.makeText(this, "هذه الصفحة غير موجودة", Toast.LENGTH_SHORT).show();
                }
               
                break;
            case R.id.liner_call:
                Uri number = Uri.parse("tel:" + product.getPhoneNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
                break;
            case R.id.liner_whats:
                WhatesSend();
                break;
            case R.id.tv_maps:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("location", product.getAddress());
                startActivity(intent);
                break;
            case R.id.imagbackd:
                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();

                onBackPressed();
                break;


        }
    }

    private void WhatesSend() {
        String contact = "+2" + product.getPhoneNumber();
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(DetailsActivity.this, "ليس لديه واتس ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse(product.getPageFace()));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(product.getPageFace()));
        }
    }
}