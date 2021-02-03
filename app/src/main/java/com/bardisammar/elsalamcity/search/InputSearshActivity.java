package com.bardisammar.elsalamcity.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.cart.AdpterTabSearsh;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityInputSearshBinding;
import com.bardisammar.elsalamcity.home.HomeActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class InputSearshActivity extends AppCompatActivity {
    ActivityInputSearshBinding binding;
    DatabaseReference catogry;
    FirebaseDatabase database;
    AdpterTabSearsh adapter;
    String name_cato = "";
    ArrayList<String> listSpinner = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.lef_to_righit,
                R.anim.right_to_left);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_searsh);

        //adds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        //firebase
        database = FirebaseDatabase.getInstance();
        catogry = database.getReference().child("category");

        binding.imagback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();
            }
        });
initTapAndPager();
    }
    private void initTapAndPager() {

        adapter = new AdpterTabSearsh(getSupportFragmentManager());
        adapter.addFragment(MarketsSearshFragment.newInstance(), "المحلات");
        adapter.addFragment(ProductesSearshFragment.newInstance(), "المنتجات والعروض ");
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);


    }
  /*      binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Commen.isNetworkOnline(InputSearshActivity.this)){
                    if (binding.edSearsh.getText().toString().isEmpty() && binding.edSearsh.getText().toString().equals("")
                    ) {

                        Toast.makeText(InputSearshActivity.this, "اكتب كلمة البحث", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinner.getSelectedItem().toString().equalsIgnoreCase("اختار اى قسم ")) {
                        Toast.makeText(InputSearshActivity.this, "اختار أى قسم ", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(InputSearshActivity.this, SearchActivity.class);
                        intent.putExtra("search", binding.edSearsh.getText().toString());
                        intent.putExtra("nameCto", name_cato);
                        startActivity(intent);
                        finish();
                    }
                    
                }else {
                    Toast.makeText(InputSearshActivity.this, "انقطع الاتصال بالانترنت ", Toast.LENGTH_SHORT).show();
                }
             

            }
        });


    }

    private void loadarrtFire() {
        if (Commen.isNetworkOnline(this)){
            try {
                catogry.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String title = dataSnapshot1.getKey().toString();
                            Commen.print(title, "ammmm");
                            listSpinner.add(title);


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            getActionSpinner(listSpinner);
        }else {

            Toast.makeText(this, "انقطع الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
        }





    }

    private void getActionSpinner(final ArrayList<String> listSpinner) {


        listSpinner.add(0, "اختار اى قسم ");
        final ArrayAdapter adapter = new ArrayAdapter(InputSearshActivity.this
                , android.R.layout.simple_spinner_item, listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                name_cato = listSpinner.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}