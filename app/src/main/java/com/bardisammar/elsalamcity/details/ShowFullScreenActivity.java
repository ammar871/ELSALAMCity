package com.bardisammar.elsalamcity.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.databinding.ActivityShowFullScreenBinding;
import com.facebook.drawee.backends.pipeline.Fresco;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;

public class ShowFullScreenActivity extends AppCompatActivity {
    ActivityShowFullScreenBinding binding;
    String getImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_full_screen);

        if (getIntent() != null) {
            getImage = getIntent().getStringExtra("image");

            fullScreen();
//
//            Glide.with(this).load(getImage)
//                    .into(binding.imageShow);
//            binding.tvBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onBackPressed();
//                }
//            });
//        }


    }}

    private void fullScreen() {
        binding.photoDraweeView.setPhotoUri(Uri.parse(getImage));
        binding.photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override public void onPhotoTap(View view, float x, float y) {
                Toast.makeText(view.getContext(), "onPhotoTap :  x =  " + x + ";" + " y = " + y,
                        Toast.LENGTH_SHORT).show();
            }
        });
        binding.photoDraweeView.setOnViewTapListener(new OnViewTapListener() {
            @Override public void onViewTap(View view, float x, float y) {
                Toast.makeText(view.getContext(), "onViewTap", Toast.LENGTH_SHORT).show();
            }
        });

        binding.photoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "onLongClick", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}