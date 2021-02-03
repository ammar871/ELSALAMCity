package com.bardisammar.elsalamcity.about_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.databinding.ActivityCallUsBinding;
import com.bardisammar.elsalamcity.home.HomeActivity;


public class CallUsActivity extends AppCompatActivity {
ActivityCallUsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.lef_to_righit,
                R.anim.right_to_left);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_us);

        binding.imagback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });
        binding.sendWhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WhatesSend();
            }
        });

        binding.sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ammarebrahim@gmail.com"});

                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(CallUsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
binding.sendFace.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sendMessanger();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void sendMessanger(){

        PackageManager packageManager = this.getPackageManager();
        if (getOpenFacebookIntent().resolveActivity(packageManager) != null) {
            startActivity(getOpenFacebookIntent());
        } else {
            Toast.makeText(this, "هذه الصفحة غير موجودة", Toast.LENGTH_SHORT).show();
        }}

    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/%D8%B3%D9%88%D9%82-%D8%A8%D8%B1%D8%AF%D9%8A%D8%B3-102254105187750"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/%D8%B3%D9%88%D9%82-%D8%A8%D8%B1%D8%AF%D9%8A%D8%B3-102254105187750"));
        }
    }
}