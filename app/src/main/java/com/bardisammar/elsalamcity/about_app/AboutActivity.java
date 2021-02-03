package com.bardisammar.elsalamcity.about_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.cart.CartActivity;
import com.bardisammar.elsalamcity.cart.SuccsusFragment;
import com.bardisammar.elsalamcity.databinding.ActivityAboutBinding;
import com.bardisammar.elsalamcity.home.HomeActivity;
import com.bardisammar.elsalamcity.pojo.Request;
import com.bardisammar.elsalamcity.shardprefranse.SharedEditor;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.IOException;

public class AboutActivity extends AppCompatActivity  implements View.OnClickListener , RewardedVideoAdListener {
ActivityAboutBinding binding;
private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.lef_to_righit,
                R.anim.right_to_left);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        MobileAds.initialize(this, "ca-app-pub-3315263708203388~3405012481");

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        // Make sure that the MainActivity class implements the required
        // interface: RewardedVideoAdListener
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();
        binding.imagback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();
            }
        });

        inItView();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void inItView() {
        binding.laoutHelp.setOnClickListener(this);
        binding.linerStar.setOnClickListener(this);
        binding.linerUpdate.setOnClickListener(this);


    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.liner_update:

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pname:com.bardisammar.elsalamcity"));
                startActivity(intent);
                break;
            case R.id.liner_star:
                rateMyApp();
                break;
            case R.id.laout_help:
                binding.progressbar.setVisibility(View.VISIBLE);
                binding.laoutHelp.setVisibility(View.INVISIBLE);
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);


               alert.setMessage("مشاهدة فيديو اعلانى ");
                alert.setIcon(R.drawable.ic_baseline_ondemand_video_24);
                alert.setPositiveButton("مشاهدة ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mRewardedVideoAd.isLoaded()) {
                            binding.laoutHelp.setVisibility(View.VISIBLE);
                            binding.progressbar.setVisibility(View.GONE);
                            mRewardedVideoAd.show();
                        }else {
                            loadRewardedVideoAd();
                            if (mRewardedVideoAd.isLoaded()) {
                                binding.laoutHelp.setVisibility(View.VISIBLE);
                                binding.progressbar.setVisibility(View.GONE);
                                mRewardedVideoAd.show();
                        }}
                    }
                });
                alert.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();




                break;
        }
    }

    private void rateMyApp() {
        Uri uri = Uri.parse("market://details?id=" + "com.bardisammar.elsalamcity");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
    private void loadRewardedVideoAd() {

        mRewardedVideoAd.loadAd("ca-app-pub-3315263708203388/9735752580",
                new AdRequest.Builder().build());
    }


    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        binding.laoutHelp.setVisibility(View.VISIBLE);
        binding.progressbar.setVisibility(View.GONE);
        Toast.makeText(this, "شكرا لك ", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}