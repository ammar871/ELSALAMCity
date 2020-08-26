package com.elsalmcity.elsalamcity.about_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.databinding.ActivityCallUsBinding;
import com.elsalmcity.elsalamcity.home.HomeActivity;
import com.elsalmcity.elsalamcity.search.SearchActivity;


public class CallUsActivity extends AppCompatActivity {
ActivityCallUsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_us);

        binding.imagback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CallUsActivity.this, HomeActivity.class));
                finish();
            }
        });
        binding.linerCallWhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhatesSend();
            }
        });

    }

    private void WhatesSend() {
        String contact = "+201011629271";
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(CallUsActivity.this, "ليس لديه واتس ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}