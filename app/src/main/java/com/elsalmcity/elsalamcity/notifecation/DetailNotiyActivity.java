package com.elsalmcity.elsalamcity.notifecation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elsalmcity.elsalamcity.BuildConfig;
import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.databinding.ActivityDetailNotiyBinding;
import com.elsalmcity.elsalamcity.details.DetailsActivity;
import com.elsalmcity.elsalamcity.details.MapsActivity;
import com.elsalmcity.elsalamcity.details.ShowImagsActivity;
import com.elsalmcity.elsalamcity.pojo.Product;

public class DetailNotiyActivity extends AppCompatActivity implements View.OnClickListener {
    Product product;
    ActivityDetailNotiyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_notiy);
        if (getIntent() != null) {
            product = getIntent().getParcelableExtra("productNotiy");

        }

        binding.linerShare.setOnClickListener(this);
        binding.linerPage.setOnClickListener(this);
        binding.linerCall.setOnClickListener(this);
        binding.linerWhats.setOnClickListener(this);
        binding.tvMaps.setOnClickListener(this);
        binding.imageView.setOnClickListener(this);
        binding.imagbackd.setOnClickListener(this);

        if (product != null) {
            binding.tvName.setText(product.getName());
            binding.tvDesc.setText(product.getDesc());
            binding.tvAddress.setText(product.getAddress());
            Glide.with(this).load(product.getImage())
                    .into(binding.imageView);
            Glide.with(this).load(product.getImage())
                    .into(binding.showImages);


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liner_share:
                try {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareMessage = "الان علي ابليكيشن AL SALAM CITY" + "\n";
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
            case R.id.liner_page:
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                startActivity(getOpenFacebookIntent());
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
}