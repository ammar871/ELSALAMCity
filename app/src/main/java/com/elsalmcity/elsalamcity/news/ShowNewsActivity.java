package com.elsalmcity.elsalamcity.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.databinding.ActivityShowNewsBinding;
import com.elsalmcity.elsalamcity.home.HomeActivity;
import com.elsalmcity.elsalamcity.notifecation.Data;
import com.elsalmcity.elsalamcity.notifecation.ShowNotiyActivity;
import com.elsalmcity.elsalamcity.pojo.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShowNewsActivity extends AppCompatActivity {
ActivityShowNewsBinding binding;
    String title;
    DatabaseReference notyRef;
    FirebaseDatabase database;
    Data blog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_news);
        if (getIntent()!=null) {
            title = getIntent().getStringExtra("title");

        }
        database = FirebaseDatabase.getInstance();
        notyRef = database.getReference().child("News");

        loadDataIntentnt();
        binding.imagbackd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowNewsActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void loadDataIntentnt() {
        Query q = notyRef.orderByChild("title").equalTo(title);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    blog = dataSnapshot1.getValue(Data.class);

                    binding.tvTitle.setText(blog.getTitle());
                    binding.tvBody.setText(blog.getBody());

                    Glide.with(ShowNewsActivity.this).load(blog.getImage())
                            .into(binding.imageView);



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
        startActivity(new Intent(ShowNewsActivity.this, HomeActivity.class));
        finish();
    }
}