package com.bardisammar.elsalamcity.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityOffersBinding;
import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OffersActivity extends AppCompatActivity {
String getMenuId,catoname;
    AdpterPro adpterProduct;
    ArrayList<Pro_Of_Product> list;
    HashMap<String, String> imagelist;
    FirebaseDatabase database;
    ProgressDialog pd;
    ActivityOffersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.lef_to_righit,
                R.anim.right_to_left);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offers);
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder()

                .build();


        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest);

        if (getIntent() != null) {
            getMenuId = getIntent().getStringExtra("menuId");
            catoname = getIntent().getStringExtra("name");
            Log.d("getget",getMenuId+catoname);
            binding.tvName.setText(catoname);


        }
        binding.imagbackd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        binding.lispro.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.lispro.setHasFixedSize(true);
        loadDataIntentnt();

    }


    private void loadDataIntentnt() {
        if (Commen.isNetworkOnline(this)) {
            try {
                pd = new ProgressDialog(this);
                pd.setMessage("جارى التحميل...");
                pd.show();
                FirebaseDatabase.getInstance().getReference().child("منتجات").orderByChild("idcatogray")
                        .equalTo(getMenuId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pd.dismiss();
                        list.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            pd.dismiss();
                            Log.d("Tagstorag", "onSuccess: " + dataSnapshot1.child("name").getValue());
                            Pro_Of_Product blog = dataSnapshot1.getValue(Pro_Of_Product.class);


                            list.add(blog);


                        }
                        adpterProduct = new AdpterPro(list, OffersActivity.this);
                        binding.lispro.setAdapter(adpterProduct);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pd.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "انقطع الاتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        }


    }
}