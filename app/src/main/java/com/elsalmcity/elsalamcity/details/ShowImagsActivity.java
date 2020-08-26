package com.elsalmcity.elsalamcity.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.elsalmcity.elsalamcity.R;

import com.elsalmcity.elsalamcity.databinding.ActivityShowImagsBinding;
import com.elsalmcity.elsalamcity.pojo.Product;


import java.util.ArrayList;

public class ShowImagsActivity extends AppCompatActivity {
    ActivityShowImagsBinding binding;

    AdpterShowAll adpterImageDetail;
    ArrayList<String> listImage;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_imags);
        binding.listImageShow.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        binding.listImageShow.setHasFixedSize(true);
        listImage=new ArrayList<>();
        if (getIntent()!=null){
            product=getIntent().getParcelableExtra("productimages");

        }
        if (product!=null){

            int size=product.getUris().size();
            for (int i=0;i<size;i++){
                String image= product.getUris().get(i);
                listImage.add(image);
                Log.d("showi", "onSuccess: " +image);

            }


            adpterImageDetail=new AdpterShowAll(listImage,ShowImagsActivity.this);
            binding.listImageShow.setAdapter(adpterImageDetail);
        }
        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}