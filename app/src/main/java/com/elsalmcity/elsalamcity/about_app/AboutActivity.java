package com.elsalmcity.elsalamcity.about_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.databinding.ActivityAboutBinding;
import com.elsalmcity.elsalamcity.home.HomeActivity;
import com.elsalmcity.elsalamcity.search.SearchActivity;

public class AboutActivity extends AppCompatActivity {
ActivityAboutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        binding.imagback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}