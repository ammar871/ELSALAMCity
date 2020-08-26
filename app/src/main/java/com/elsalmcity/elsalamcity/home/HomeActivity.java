package com.elsalmcity.elsalamcity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.elsalmcity.elsalamcity.BuildConfig;
import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.about_app.AboutActivity;
import com.elsalmcity.elsalamcity.about_app.CallUsActivity;
import com.elsalmcity.elsalamcity.databinding.ActivityHomeBinding;
import com.elsalmcity.elsalamcity.news.NewsActivity;
import com.elsalmcity.elsalamcity.notifecation.NotificationActivity;
import com.elsalmcity.elsalamcity.notifecation.Token;
import com.elsalmcity.elsalamcity.pojo.Catogray;
import com.elsalmcity.elsalamcity.products.ProductsActivity;
import com.elsalmcity.elsalamcity.search.InputSearshActivity;
import com.elsalmcity.elsalamcity.search.SearchActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityHomeBinding binding;

ProgressDialog pd;
    TextView tv_home, tv_search, tv_notifecation, tv_news, tv_fllow, tv_about, tv_shareapp;

    ArrayList<Catogray> list;

    FirebaseRecyclerAdapter<Catogray, MenuViewHolder> firbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().subscribeToTopic("salam").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        pd = new ProgressDialog(HomeActivity.this);
        pd.setMessage("جارى التحميل...");
        Update(FirebaseInstanceId.getInstance().getToken());
        list = new ArrayList<>();
        iniItViewMenu();

try {
    Load();
}catch (Exception ignored){

}

        binding.toggles.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View view) {
                binding.draw.openDrawer(Gravity.RIGHT);
            }
        });

        binding.imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, InputSearshActivity.class));
                finish();
            }
        });


    }

    private void iniItViewMenu() {
        tv_home = findViewById(R.id.homee);
        tv_search = findViewById(R.id.tv_searsh);
        tv_fllow = findViewById(R.id.call_us);
        tv_news = findViewById(R.id.news);
        tv_notifecation = findViewById(R.id.notify);
        tv_about = findViewById(R.id.about_app);
        tv_shareapp = findViewById(R.id.share_nav);
        tv_home.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_fllow.setOnClickListener(this);
        tv_news.setOnClickListener(this);
        tv_notifecation.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        tv_shareapp.setOnClickListener(this);


    }

    private void Update(String token) {

        try {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference("Tokens");
            Token data = new Token(token, true);
            ref.child(token).setValue(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void Load() {

        pd.show();
        binding.listHome.setLayoutManager(new GridLayoutManager(this, 2));
        binding.listHome.setHasFixedSize(true);
        Query query = FirebaseDatabase.getInstance().getReference().child("category");


        FirebaseRecyclerOptions<Catogray> options = new FirebaseRecyclerOptions.Builder<Catogray>()
                .setQuery(query, Catogray.class)
                .build();

        firbase = new FirebaseRecyclerAdapter<Catogray, MenuViewHolder>(options) {


            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_catogray, parent, false);

                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder menuViewHolder, final int i, @NonNull final Catogray categoryb) {

                final String key = firbase.getRef(i).getKey();
                menuViewHolder.textView.setText(categoryb.getName());
                Glide.with(HomeActivity.this)
                        .load(categoryb.getImage())
                        .into(menuViewHolder.imageView);
                menuViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intentMothed(key, categoryb.getName());
                    }
                });


            }
        };
        binding.listHome.setAdapter(firbase);
        firbase.startListening();
        pd.dismiss();

    }

    private void intentMothed(String value, String namecato) {

        Intent intent = new Intent(this, ProductsActivity.class);
        intent.putExtra("menuId", value);
        intent.putExtra("name", namecato);
        this.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homee:
                startActivity(new Intent(this, HomeActivity.class));
                finish();

                break;
            case R.id.tv_searsh:
                startActivity(new Intent(this, InputSearshActivity.class));
                finish();
                break;
            case R.id.news:
                startActivity(new Intent(this, NewsActivity.class));
                finish();
                break;
            case R.id.notify:
                startActivity(new Intent(this, NotificationActivity.class));
                finish();
                break;
            case R.id.call_us:
                startActivity(new Intent(this, CallUsActivity.class));
                finish();
                break;
            case R.id.about_app:
                startActivity(new Intent(this, AboutActivity.class));
                finish();
                break;
            case R.id.share_nav:

                try {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String aboutMessage = "الان  ابليكيشن AL SALAM CITY لخدمات مدينة السلام" + "\n";


                    aboutMessage = aboutMessage + "https://play.google.com/store/apps/details?id="
                            + BuildConfig.APPLICATION_ID + "\n\n";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, aboutMessage);
                    startActivity(Intent.createChooser(sharingIntent, "تطبيق السلام سيتي"));
                } catch (Exception e) {
                    Toast.makeText(this, "فشلت العملية", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}