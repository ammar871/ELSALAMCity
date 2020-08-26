package com.elsalmcity.elsalamcity.spilash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.home.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SpilashActivity extends AppCompatActivity {
    private static final int WAIT_TIME =3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spilash);

        Timer waitTimer = new Timer();
        //Check is login

        waitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SpilashActivity .this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(SpilashActivity.this, HomeActivity.class));
                        finish();

                    }
                });
            }
        }, WAIT_TIME);
    }

}