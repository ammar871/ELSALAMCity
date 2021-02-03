package com.bardisammar.elsalamcity.cart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bardisammar.elsalamcity.sendEmail.JavaMailAPI;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.cart.roomdatabase.AppDatabase;
import com.bardisammar.elsalamcity.cart.roomdatabase.RoomAdpter;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityCartBinding;
import com.bardisammar.elsalamcity.home.HomeActivity;
import com.bardisammar.elsalamcity.notifecation.ApiServes;
import com.bardisammar.elsalamcity.notifecation.Claint;
import com.bardisammar.elsalamcity.notifecation.Data;
import com.bardisammar.elsalamcity.notifecation.MyResponse;
import com.bardisammar.elsalamcity.notifecation.Sender;
import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;
import com.bardisammar.elsalamcity.pojo.Request;
import com.bardisammar.elsalamcity.shardprefranse.SharedEditor;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RoomAdpter.Callback, OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
        , LocationListener {
    ActivityCartBinding binding;
    private AppDatabase database;
    private RoomAdpter mUserAdapter;
    private LinearLayoutManager mLayoutManager;
    SharedEditor sharedEditor;
    ArrayList<Pro_Of_Product> cart = new ArrayList<>();

    //location
    private Location mlastlocation;
    private GoogleApiClient mGooglapiclent;
    private LocationRequest mlocationrequest;

    private final static int UPDATE_INTERVAL = 5000;

    private final static int FASTEST_INTERVAL = 3000;
    private final static int DISPLAYCEMENT = 10;
    private final static int LOCATION_PER_REQUEST = 9999;
    private final static int PLAY_SER_REQUEST = 9997;
    private double latitude;
    private double longtude;
    private static String nameAdrees;

    FirebaseDatabase databasef;
    DatabaseReference requestd;
    ApiServes mservies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.lef_to_righit,
                R.anim.right_to_left);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder()

                .build();


        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest);
        mservies = Claint.getClient().create(ApiServes.class);


        database = AppDatabase.getDatabaseInstance(this);
        sharedEditor = new SharedEditor(this);
        databasef = FirebaseDatabase.getInstance();
        requestd = databasef.getReference("Orders");

        binding.imagback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });

/*
        binding.btnplaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.size() > 0) {
                    Alert_dialog();
                    //  setUp();

                } else {

                    Toast.makeText(CartActivity.this, "السلة فارغة ", Toast.LENGTH_SHORT).show();

                }
            }
        });*/

        setUp();
        //primssion location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION

                    }, LOCATION_PER_REQUEST);
        } else {
            if (Checkplayservic()) {

                buildgoogleapiclaint();
                creatlocationrequest();
            }
        }

sizeTheCart();
    }



    private void setUp() {
        cart = (ArrayList<Pro_Of_Product>) database.userDao().getAll();
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.listCart.setLayoutManager(mLayoutManager);
        binding.listCart.setItemAnimator(new DefaultItemAnimator());
        mUserAdapter = new RoomAdpter(cart, this);
        mUserAdapter.notifyDataSetChanged();
        mUserAdapter.setmCallback(this);
        binding.listCart.setAdapter(mUserAdapter);


        //total

        Double total = 0.0;
        for (Pro_Of_Product orders : cart) {

//
//            total += (Double.parseDouble(orders.getPrice())) * (Integer.parseInt(orders.getNumberQuntity()));
//            Locale locale = new Locale("ar", "EG");
//            NumberFormat ftm = NumberFormat.getCurrencyInstance(locale);
//            binding.total.setText(ftm.format(total));


        }
    }

    @Override
    public void onDeleteClick(Pro_Of_Product mUser) {
        database.userDao().delete(mUser);
        mUserAdapter.addItems(database.userDao().getAll());
        Commen.countcart = mUserAdapter.getItemCount();

        setUp();
        sizeTheCart();
    }

    private void sizeTheCart() {
        if (cart.size() == 0) {
           binding.linerNetWork.setVisibility(View.VISIBLE);
           binding.listCart.setVisibility(View.GONE);
        }
    }


    /*  private void Alert_dialog() {

          final AlertDialog.Builder alert = new AlertDialog.Builder(this);
          LayoutInflater inflater = this.getLayoutInflater();
          final View dialogView = inflater.inflate(R.layout.order_address_comment, null);


          final EditText edtaddresse = dialogView.findViewById(R.id.ednaddresse_dialog);

          final EditText edtcomment = dialogView.findViewById(R.id.comment);
          RadioButton retbshipetoaddress = dialogView.findViewById(R.id.shipToAddress);


          retbshipetoaddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  if (isChecked) {
                      try {
                          try {
                              GetLocation(latitude, longtude);
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                          edtaddresse.setText(nameAdrees);
                      } catch (Exception e) {
                          e.printStackTrace();
                          Toast.makeText(CartActivity.this, "من فضلك قم بتشغيل الموقع ", Toast.LENGTH_SHORT).show();
                      }


                  }

              }
          });

          alert.setView(dialogView);
          alert.setIcon(R.drawable.ic_baseline_add_shopping_cart_24);
          alert.setPositiveButton("ارسال", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  if (edtaddresse.getText().toString() != null && !edtaddresse.getText().toString().equalsIgnoreCase("")) {
                      Request request = new Request(
                              sharedEditor.loadData().get(SharedEditor.KEY_USER_PHONE),
                             "Null",
                              edtaddresse.getText().toString(),
                              binding.total.getText().toString(),
                              edtcomment.getText().toString(),
                              //      String.format("%s,$s",adresse.getLatLng().latitude,adresse.getLatLng().longitude),
                              cart
                      );

                      String order_number = String.valueOf(System.currentTimeMillis());
                      try {
                          requestd.child(order_number)
                                  .setValue(request);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                      requestd.child(order_number)
                              .setValue(request);
                          SendRetrofitNotifection(order_number, "طلب جديد ");






                      SuccsusFragment fragment = new SuccsusFragment();
                      fragment.show(getSupportFragmentManager(), null);
             *//*         sendToEmail(cart, sharedEditor.loadData().get(SharedEditor.KEY_USER_PHONE), sharedEditor.loadData().get(SharedEditor.KEY_USER_FNAME)
                            , edtaddresse.getText().toString(), binding.total.getText().toString());*//*
                    database.clearAllTables();
                    binding.total.setText("ج.م 00,000");
                    dialog.dismiss();
                    setUp();
                } else {
                    alert.setPositiveButton("Ok", null);
                    edtaddresse.setError("من فضلك أدخل عنوانك ");
                }

            }
        });
        alert.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }*/
    String name, nameofmarket, descshort,desc, quantity, price,dataAll;
/*    private void sendToEmail(ArrayList<Pro_Of_Product> cart, String s, String s1, String toString, String toString1) {

        for (Pro_Of_Product pro_of_product : cart) {
            if (pro_of_product != null) {
                name = pro_of_product.getName();
                nameofmarket = pro_of_product.getNameOfMarket() ;
                descshort = pro_of_product.getDescShort();
                desc= pro_of_product.getDescription();
                quantity = pro_of_product.getNumberQuntity();
                price = pro_of_product.getPrice()  ;
                dataAll+="[[  "+ " الاسم: "+name+ "\n"
                        +" اسم المحل :"+nameofmarket+ "\n"
                        +" وصف قصير :"+descshort+"\n"+
                       " وصف كامل :"+desc+"\n"+
                        "الكمية :  "+quantity+"\n"+
                        " السعر : "+price+"  ]]"+"\n";
            }
            Log.d("datada",name+"\n"+nameofmarket+"\n"+desc+"\n"+quantity+"\n"+price+"  order "+"\n");
        }
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, "elsalam.app@yahoo.com", "طلب جديد", s + " \n " + s1 + "\n "
                + toString + "\n" + toString1
                + " \n" + "detailes " +"\n"+dataAll
        );
        javaMailAPI.execute();
    }*/


    @Override
    public void onStart() {
        super.onStart();
        mUserAdapter.addItems(database.userDao().getAll());
        setUp();

    }

    @Override
    public void onResume() {
        super.onResume();
        mUserAdapter.addItems(database.userDao().getAll());
        setUp();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppDatabase.destroyInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            displaylocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startlocationupdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGooglapiclent.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mlastlocation = location;
        try {
            displaylocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    private boolean Checkplayservic() {
        int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultcode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode)) {

                GooglePlayServicesUtil.getErrorDialog(resultcode, this, PLAY_SER_REQUEST).show();
            } else {
                Toast.makeText(this, "Not Support", Toast.LENGTH_SHORT).show();

            }
            return false;
        }
        return true;
    }

    protected synchronized void buildgoogleapiclaint() {
        mGooglapiclent = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGooglapiclent.connect();
    }

    private void creatlocationrequest() {

        mlocationrequest = new LocationRequest();
        mlocationrequest.setInterval(UPDATE_INTERVAL);
        mlocationrequest.setFastestInterval(FASTEST_INTERVAL);
        mlocationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationrequest.setSmallestDisplacement(DISPLAYCEMENT);
    }

    private void startlocationupdates() {

        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.
                    requestLocationUpdates(mGooglapiclent, mlocationrequest, this);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void displaylocation() throws IOException {


        try {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        mlastlocation = LocationServices.FusedLocationApi.getLastLocation(mGooglapiclent);
        if (mlastlocation != null) {
            latitude = mlastlocation.getLatitude();
            longtude = mlastlocation.getLongitude();
            GetLocation(latitude, longtude);

            Log.v("location", "couldn't get Location " + latitude + "\n" + longtude);


        } else {
            Log.d("location", "couldn't get Location ");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PER_REQUEST: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (Checkplayservic()) {

                        buildgoogleapiclaint();
                        creatlocationrequest();

                        try {
                            displaylocation();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            }
        }
    }

    private void GetLocation(double MyLat, double MyLong) throws IOException {
        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
            nameAdrees = (countryName + cityName + stateName);
            Log.v("location", "couldn't get Location " + nameAdrees);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void SendRetrofitNotifection(final String title, String body) {
        try {
            Data notifacation = new Data(title, body, "com.mangerbaedis.elsalammanger.notifecation_TARGET_NOTIFICATION");
            Sender sender = new Sender("/topics/mangeBardis", notifacation);
            mservies.sendNotifectoin(sender).enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    if (response.code() == 200) {
                        Toast.makeText(CartActivity.this, "تم الارسال ", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<MyResponse> call,@NonNull Throwable t) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}