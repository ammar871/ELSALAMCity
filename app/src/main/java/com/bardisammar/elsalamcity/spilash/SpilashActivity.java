package com.bardisammar.elsalamcity.spilash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.auth.LoginActivity;
import com.bardisammar.elsalamcity.authNumber.LoginNumberActivity;
import com.bardisammar.elsalamcity.databinding.ActivitySpilashBinding;
import com.bardisammar.elsalamcity.home.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SpilashActivity extends AppCompatActivity {
    private static final int WAIT_TIME = 1000;
    Animation top_anim, bottom_anim;
    ActivitySpilashBinding binding;
    Timer waitTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         binding = DataBindingUtil.setContentView(this, R.layout.activity_spilash);




        waitTimer = new Timer();
        //Check is login

        waitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SpilashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(SpilashActivity.this, LoginNumberActivity.class));
                        finish();

                    }
                });
            }
        }, WAIT_TIME);
    }

}