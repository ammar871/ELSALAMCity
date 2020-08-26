package com.elsalmcity.elsalamcity.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.databinding.ActivityInputSearshBinding;
import com.elsalmcity.elsalamcity.home.HomeActivity;

public class InputSearshActivity extends AppCompatActivity {
ActivityInputSearshBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_searsh);

binding.imagback.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent =new Intent(InputSearshActivity.this, HomeActivity.class);

        startActivity(intent);
        finish();
    }
});
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edSearsh.getText().toString().isEmpty()&&binding.edSearsh.getText().toString().equals("")){

                    Toast.makeText(InputSearshActivity.this, "اكتب كلمة البحث", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent =new Intent(InputSearshActivity.this,SearchActivity.class);
                    intent.putExtra("search",binding.edSearsh.getText().toString());
                    startActivity(intent);
                    finish();
                }

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(InputSearshActivity.this, HomeActivity.class);

        startActivity(intent);
        finish();
    }
}