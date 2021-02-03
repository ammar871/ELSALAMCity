package com.bardisammar.elsalamcity.notifecation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bardisammar.elsalamcity.BuildConfig;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityDetailNotiyBinding;
import com.bardisammar.elsalamcity.details.MapsActivity;
import com.bardisammar.elsalamcity.details.ShowFullScreenActivity;
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

public class DetailNotiyActivity extends AppCompatActivity implements View.OnClickListener {
    Product product;
    ActivityDetailNotiyBinding binding;
    String foodId;
    FirebaseDatabase database;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_notiy);
        //firebase
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Notifecations");

        if (getIntent() != null) {
            product = getIntent().getParcelableExtra("productNotiy");
            foodId = getIntent().getStringExtra("proName");
          //  Commen.print("slider", foodId);
            getDetailId(foodId);

        }
        //adds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);


        binding.linerShare.setOnClickListener(this);

        binding.linerCall.setOnClickListener(this);
        binding.linerWhats.setOnClickListener(this);

        binding.imagbackd.setOnClickListener(this);

        if (product != null) {
            binding.tvName.setText(product.getName());
            binding.tvDesc.setText(product.getDesc());

            Glide.with(this).load(product.getImage())
                    .into(binding.imageView);


        }


        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailNotiyActivity.this, ShowFullScreenActivity.class);
                intent.putExtra("image", product.getImage());
                startActivity(intent);
            }
        });

        binding.tvMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (product!=null){
                    Intent intent = new Intent(DetailNotiyActivity.this, MapsActivity.class);
                    intent.putExtra("location", product.getAddress());
                    startActivity(intent);
                }

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
                    startActivity(Intent.createChooser(sharingIntent, "تطبيق السلام سيتي"));
                } catch (Exception e) {

                    Toast.makeText(this, "فشلت العملية", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(DetailNotiyActivity.this, "ليس لديه واتس ", Toast.LENGTH_SHORT).show();
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


    private void getDetailId(String foodid) {

        try {
            if (foodid != null) {
                ref.child(foodid).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            product = dataSnapshot.getValue(Product.class);

                            if (product!=null){

                                binding.tvName.setText(product.getName());
                                binding.tvDesc.setText(product.getDesc());

                                Glide.with(DetailNotiyActivity.this).load(product.getImage())
                                        .into(binding.imageView);

                            }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}