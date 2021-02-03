package com.bardisammar.elsalamcity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bardisammar.elsalamcity.R;

import com.bardisammar.elsalamcity.about_app.AboutActivity;
import com.bardisammar.elsalamcity.about_app.CallUsActivity;
import com.bardisammar.elsalamcity.cart.AdpterTabSearsh;
import com.bardisammar.elsalamcity.cart.CartActivity;
import com.bardisammar.elsalamcity.cart.DetailsCartActivity;
import com.bardisammar.elsalamcity.cart.Pro_Of_CatogFragment;
import com.bardisammar.elsalamcity.cart.roomdatabase.AppDatabase;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityHomeBinding;
import com.bardisammar.elsalamcity.news.NewsActivity;
import com.bardisammar.elsalamcity.notifecation.NotificationActivity;

import com.bardisammar.elsalamcity.pojo.Catogray;
import com.bardisammar.elsalamcity.search.InputSearshActivity;

import com.bardisammar.elsalamcity.BuildConfig;

import com.bardisammar.elsalamcity.shardprefranse.SharedEditor;
import com.facebook.login.LoginManager;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityHomeBinding binding;
    AdpterTabSearsh adapter;
    AppDatabase database;
    ProgressDialog pd;
    TextView tv_home, tv_search, tv_notifecation, tv_news, tv_fllow, tv_about, tv_shareapp, tv_count, tv_count_home, tv_exite, tv_nameUser, tv_counter_cart;
    RelativeLayout re_hid_noty, clicl_noty, linCart,relatev_hid_cart;
    SharedEditor sharedEditor;

    ArrayList<Catogray> list;


    private FrameLayout adContainerView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.lef_to_righit,
                R.anim.right_to_left);
        FirebaseMessaging.getInstance().subscribeToTopic("bardis").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        Get_hash_key();
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        sharedEditor = new SharedEditor(this);
        Typeface face = Typeface.createFromAsset(this.getAssets(),"homefont.otf");

        if (sharedEditor.loadDataEnter().get(SharedEditor.KEY_IS_ENTERED)==null
                ||!sharedEditor.loadDataEnter().get(SharedEditor.KEY_IS_ENTERED)){
            new GuideView.Builder(this)
                    .setTitle("الدليفرى")
                    .setContentText("")

                    .setTitleTypeFace(face)
                    .setContentTypeFace(face)
                    .setTargetView(binding.imageDelevary)
                    .setContentTextSize(15)
                    .setContentSpan((Spannable) Html.fromHtml("<font color='red'>......اطلب اللى انت عاوزه ....</p>"))
                    .setDismissType(DismissType.anywhere)
                    .setGuideListener(new GuideListener() {
                        @Override
                        public void onDismiss(View view) {
                            sharedEditor.saveDataEnterd(true);
                        }
                    })
                    .build()
                    .show();

        }
      binding.imageDelevary.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);


              alert.setMessage("هل تريد الطلب من الدليفرى؟");
              alert.setIcon(R.drawable.ic_baseline_ondemand_video_24);
              alert.setPositiveButton("نعم ", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      WhatesSend("01223138059");

                  }
              });
              alert.setNegativeButton("لا, شكرا", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                  }
              });
              alert.show();



          }
      });



        database = AppDatabase.getDatabaseInstance(this);
        //adds
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder()
                .build();


        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest);
        initTapAndPager();








        list = new ArrayList<>();
        iniItViewMenu();

        tv_nameUser.setText(" أهلا بك " + sharedEditor.loadData().get(SharedEditor.KEY_USER_PHONE));

        if (Commen.count == 0) {
            tv_count.setVisibility(View.GONE);
            re_hid_noty.setVisibility(View.GONE);
        } else {
            tv_count.setVisibility(View.VISIBLE);
            re_hid_noty.setVisibility(View.VISIBLE);
            tv_count.setText(Commen.count + "");
            tv_count_home.setText(Commen.count + "");
        }




        binding.toggles.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"RtlHardcoded", "WrongConstant"})
            @Override
            public void onClick(View view) {
                binding.draw.openDrawer(Gravity.RIGHT);
            }
        });

        binding.imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, InputSearshActivity.class));

            }
        });

        createLocationRequest();
    }

    private void iniItViewMenu() {
        re_hid_noty = findViewById(R.id.relatev_hid);
        tv_exite = findViewById(R.id.nav_logout);
        clicl_noty = findViewById(R.id.lin_noty);
        linCart = findViewById(R.id.lin_cart);
        relatev_hid_cart = findViewById(R.id.relatev_hid_cart);
        tv_home = findViewById(R.id.homee);
        tv_count = findViewById(R.id.tv_count);
        tv_counter_cart = findViewById(R.id.tv_counter_cart);
        tv_nameUser = findViewById(R.id.tv_name);
        tv_count_home = findViewById(R.id.tv_counter_noty);
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
        clicl_noty.setOnClickListener(this);
        tv_exite.setOnClickListener(this);
        tv_nameUser.setOnClickListener(this);
        linCart.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        Commen.countcart = database.userDao().getCount().size();
        if (Commen.count == 0) {
            tv_count.setVisibility(View.GONE);
            re_hid_noty.setVisibility(View.GONE);
        } else {
            tv_count.setVisibility(View.VISIBLE);
            re_hid_noty.setVisibility(View.VISIBLE);
            tv_count.setText(Commen.count + "");
            tv_count_home.setText(Commen.count + "");
        }
        if (Commen.countcart == 0) {
            tv_counter_cart.setVisibility(View.GONE);
            relatev_hid_cart.setVisibility(View.GONE);

        } else {
            tv_counter_cart.setVisibility(View.VISIBLE);
            relatev_hid_cart.setVisibility(View.VISIBLE);
            tv_counter_cart.setText(Commen.countcart + "");

        }
        if (binding.adView != null) {
            binding.adView.resume();
        }
    }


    private void initTapAndPager() {

        adapter = new AdpterTabSearsh(getSupportFragmentManager());
        adapter.addFragment(CatograyFragment.newInstance(), "المحلات");
        adapter.addFragment(Pro_Of_CatogFragment.newInstance(), "المنتجات والعروض ");
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homee:
            binding.draw.closeDrawer(Gravity.RIGHT);

                break;
            case R.id.tv_searsh:
                startActivity(new Intent(HomeActivity.this, InputSearshActivity.class));
                binding.draw.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.news:
                startActivity(new Intent(HomeActivity.this, NewsActivity.class));
                binding.draw.closeDrawer(Gravity.RIGHT);
                break;

            case R.id.notify:
            case R.id.lin_noty:
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                binding.draw.closeDrawer(Gravity.RIGHT);
                Commen.count = 0;
                break;
            case R.id.call_us:
                startActivity(new Intent(HomeActivity.this, CallUsActivity.class));
                binding.draw.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.about_app:
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                binding.draw.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.share_nav:

                try {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String aboutMessage = "الان  ابليكيشن سوق برديس  لخدمات مدينة برديس " + "\n";


                    String sharApp = aboutMessage + "https://play.google.com/store/apps/details?id="
                            + BuildConfig.APPLICATION_ID + "\n\n";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharApp);
                    startActivity(Intent.createChooser(sharingIntent, "تطبيق سوق برديس"));
                } catch (Exception e) {
                    Toast.makeText(this, "فشلت العملية", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_logout:

////            face
//                LoginManager.getInstance().logOut();
//
//                GoogleSignInOptions gso = new GoogleSignInOptions.
//                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
//                        build();
////              google
//                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
//                googleSignInClient.signOut();
              //  FirebaseAuth.getInstance().signOut();

                //shared
                sharedEditor.logOut();
                finish();

                break;
            case R.id.lin_cart:
                startActivity(new Intent(HomeActivity.this, CartActivity.class));

                break;


            default:
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (binding.adView != null) {
            binding.adView.pause();
        }
        super.onPause();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (binding.adView != null) {
            binding.adView.destroy();
        }
        super.onDestroy();
    }

    @SuppressLint("PackageManagerGetSignatures")
    public void Get_hash_key() {

        byte[] sha1 = {
                (byte) 0xcf, 0x37, 0x1c, (byte)0x2c, (byte)0xcc, (byte) 0xea, 0x27, (byte)0xb8, (byte)0x02, (byte)0x44, (byte)0xcd, 0x66,
                0x35, 0x37, 0x64, (byte)0x7f, (byte) 0xab, (byte) 0xce, (byte)0x9d, (byte)0x57
        };
       Log.d("keyhash", Base64.encodeToString(sha1, Base64.NO_WRAP));
//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo("com.elsalmcity.elsalamcity", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.d("hashkey", something);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.d("name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("exception", e.toString());
//        }
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);


// ...

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {

                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(HomeActivity.this,
                                1);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }

    private void WhatesSend(String pro_of_product) {
        String contact = "+2" + pro_of_product;
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(HomeActivity.this, "ليس لديه واتس ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}