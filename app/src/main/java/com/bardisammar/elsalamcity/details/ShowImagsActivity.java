package com.bardisammar.elsalamcity.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bardisammar.elsalamcity.R;

import com.bardisammar.elsalamcity.databinding.ActivityShowImagsBinding;
import com.bardisammar.elsalamcity.pojo.Product;


import java.util.ArrayList;

public class ShowImagsActivity extends AppCompatActivity implements AdpterShowAll.OnItemClickListener{
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


            adpterImageDetail=new AdpterShowAll(listImage,ShowImagsActivity.this,this);
            binding.listImageShow.setAdapter(adpterImageDetail);
        }
        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClick(View view) {

    }
}