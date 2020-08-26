package com.elsalmcity.elsalamcity.notifecation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elsalmcity.elsalamcity.BuildConfig;
import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.commen.Commen;
import com.elsalmcity.elsalamcity.databinding.ActivityShowNotiyBinding;
import com.elsalmcity.elsalamcity.details.MapsActivity;
import com.elsalmcity.elsalamcity.home.HomeActivity;
import com.elsalmcity.elsalamcity.pojo.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ShowNotiyActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityShowNotiyBinding binding;
String title;
    DatabaseReference notyRef;
    FirebaseDatabase database;
    Product blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_notiy);
        if (getIntent()!=null) {
            title = getIntent().getStringExtra("title");

        }
        binding.linerShare.setOnClickListener(this);
        binding.linerPage.setOnClickListener(this);
        binding.linerCall.setOnClickListener(this);
        binding.linerWhats.setOnClickListener(this);
        binding.tvMaps.setOnClickListener(this);
        binding.imageView.setOnClickListener(this);
        binding.imagbackd.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        notyRef = database.getReference().child("Notifecations");

        loadDataIntentnt();
        binding.imagbackd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowNotiyActivity.this, HomeActivity.class));
                finish();
            }
        });

    }

    private void loadDataIntentnt() {
        Query q = notyRef.orderByChild("name").equalTo(title);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    blog = dataSnapshot1.getValue(Product.class);

                    binding.tvName.setText(blog.getName());
                    binding.tvDesc.setText(blog.getDesc());
                    binding.tvAddress.setText(blog.getAddress());
                    Glide.with(ShowNotiyActivity.this).load(blog.getImage())
                            .into(binding.imageView);
                    Glide.with(ShowNotiyActivity.this).load(blog.getImage())
                            .into(binding.showImages);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShowNotiyActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liner_share:
                try {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareMessage = "الان علي ابليكيشن AL SALAM CITY" + "\n";
                    String shareName = blog.getName() + "\n";
                    String shareNumber = "رقم الهاتف " + blog.getPhoneNumber() + "\n";
                    String shareAll = shareName + shareMessage + shareNumber;

                    shareMessage = shareAll + "https://play.google.com/store/apps/details?id="
                            + BuildConfig.APPLICATION_ID + "\n\n";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(sharingIntent, "تطبيق السلام سيتي"));
                } catch (Exception e) {

                    Toast.makeText(this, "فشلت العملية", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.liner_page:
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                startActivity(getOpenFacebookIntent());
                break;
            case R.id.liner_call:
                Uri number = Uri.parse("tel:" + blog.getPhoneNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
                break;
            case R.id.liner_whats:
                WhatesSend();
                break;
            case R.id.tv_maps:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("location", blog.getAddress());
                startActivity(intent);
                break;
            case R.id.imagbackd:
                onBackPressed();
                break;


        }
    }

    private void WhatesSend() {
        String contact = "+2" + blog.getPhoneNumber();
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(ShowNotiyActivity.this, "ليس لديه واتس ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse(blog.getPageFace()));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(blog.getPageFace()));
        }
    }

}