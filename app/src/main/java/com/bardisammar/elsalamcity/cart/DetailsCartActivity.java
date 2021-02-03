package com.bardisammar.elsalamcity.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bardisammar.elsalamcity.BuildConfig;
import com.bardisammar.elsalamcity.details.DetailsActivity;
import com.bardisammar.elsalamcity.details.MapsActivity;
import com.bardisammar.elsalamcity.news.AdpterNews;
import com.bardisammar.elsalamcity.news.NewsActivity;
import com.bardisammar.elsalamcity.notifecation.Data;
import com.bardisammar.elsalamcity.pojo.ModelLike;
import com.bardisammar.elsalamcity.shardprefranse.SharedEditor;
import com.bumptech.glide.Glide;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.cart.roomdatabase.AppDatabase;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityDetailsCartBinding;
import com.bardisammar.elsalamcity.details.ShowFullScreenActivity;
import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class DetailsCartActivity extends AppCompatActivity {
    ActivityDetailsCartBinding binding;
    Pro_Of_Product pro_of_product, pro_adds;
    static int quntity = 1;
    AppDatabase database;
    Animation open, hide;
    DatabaseReference catogry, likesRf;
    FirebaseDatabase databasefirbase;
    ModelLike modelLike;
    String foodId;
    ProgressDialog pd;
    private int oldScrollYPostion = 0;

    SharedEditor sharedEditor;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedEditor = new SharedEditor(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_cart);
        getAdda();

        pd = new ProgressDialog(this);
        pd.setMessage("جارى التحميل...");
        pd.show();
        database = AppDatabase.getDatabaseInstance(this);
        // binding.tvQuntity.setText(quntity + "");

        //textQuantity();
        //firebase
        databasefirbase = FirebaseDatabase.getInstance();
        catogry = databasefirbase.getReference().child("اعلانات");
        likesRf = databasefirbase.getReference().child("الاعجابات");

        if (getIntent() != null) {
            pro_of_product = getIntent().getParcelableExtra("pro_cato");
            foodId = getIntent().getStringExtra("proName");

//            Commen.print("slider", pro_of_product.getName());

            getDetailId(foodId);
            if (pro_of_product != null) {

                binding.nameDetailes.setText(pro_of_product.getName());
                binding.descDetail.setText(pro_of_product.getDescription());
                binding.tvProce.setText(" ج.م" + pro_of_product.getPrice());
                binding.tvNameMarket.setText(" اسم المحل: " + pro_of_product.getNameOfMarket());
                binding.nameDetailes.setText(pro_of_product.getName());
                Glide.with(this).load(pro_of_product.getImage()).into(binding.imageDetail);
                pd.dismiss();
                binding.imageDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DetailsCartActivity.this, ShowFullScreenActivity.class);
                        intent.putExtra("image", pro_of_product.getImage());
                        startActivity(intent);
                    }
                });


                isFavorite(pro_of_product.getName());
                isLikes(pro_of_product.getName());
                getLikesCounter();
            }


        }
        getCountCart();

        addToCart();
        onCall();
        // onScroll();
        getLikes();

        getLikesCounter();
        clickButtuns();

        clickColors();

        callOnDelevary();
    }

    private void callOnDelevary() {
        if (pro_adds != null) {
            if (pro_adds.isDeleivary()) {

                binding.layoutDelivary.setVisibility(View.VISIBLE);
            } else {
                binding.layoutDelivary.setVisibility(View.GONE);
            }
        }

        if (pro_of_product != null) {
            if (pro_of_product.isDeleivary()) {

                binding.layoutDelivary.setVisibility(View.VISIBLE);
            } else {
                binding.layoutDelivary.setVisibility(View.GONE);
            }
        }

    }

    private void clickButtuns() {
        binding.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pro_adds != null) {
                    shareTheProduct(pro_adds);
                }
                if (pro_of_product != null) {
                    shareTheProduct(pro_of_product);
                }
            }
        });
        binding.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pro_adds != null) {
                    Intent intent = new Intent(DetailsCartActivity.this, MapsActivity.class);
                    intent.putExtra("location", pro_adds.getLocationOfMarket());
                    startActivity(intent);
                }
                if (pro_of_product != null) {
                    Intent intent = new Intent(DetailsCartActivity.this, MapsActivity.class);
                    intent.putExtra("location", pro_of_product.getLocationOfMarket());
                    startActivity(intent);
                }

            }
        });


        binding.fabCalldelavary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callWithNumb("01223138059");
                hideButtons();
            }
        });

        binding.fabCallwahtsdelevary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhatesSend("01223138059");
                hideButtons();
            }
        });
    }

    void getLikesCounter() {
        DatabaseReference likesRf = FirebaseDatabase.getInstance().getReference().child("الاعجابات");
        if (pro_adds != null) {

            likesRf.orderByChild("prouductKey")
                    .equalTo(pro_adds.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    binding.tvCounter.setText(dataSnapshot.getChildrenCount() + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (pro_of_product != null) {

            likesRf.orderByChild("prouductKey")
                    .equalTo(pro_of_product.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    binding.tvCounter.setText(dataSnapshot.getChildrenCount() + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    private void hideButtons() {
        binding.fabCalldelavary.setVisibility(View.GONE);
        binding.fabCallwahtsdelevary.setVisibility(View.GONE);
        binding.tvCalldelevary.setVisibility(View.VISIBLE);

        getAnimation(binding.fabCalldelavary, hide);
        getAnimation(binding.fabCallwahtsdelevary, hide);


    }

    private void clickColors() {

        open = AnimationUtils.loadAnimation(this, R.anim.open);
        hide = AnimationUtils.loadAnimation(this, R.anim.hide);
        binding.fabdelivaryhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.fabCalldelavary.getVisibility() == View.VISIBLE &&
                        binding.fabCallwahtsdelevary.getVisibility() == View.VISIBLE

                ) {
                    binding.fabCalldelavary.setVisibility(View.GONE);
                    binding.fabCallwahtsdelevary.setVisibility(View.GONE);
                    binding.tvCalldelevary.setVisibility(View.VISIBLE);
                    DetailsCartActivity.this.getAnimation(binding.fabCalldelavary, hide);
                    DetailsCartActivity.this.getAnimation(binding.fabCallwahtsdelevary, hide);


                } else {

                    binding.fabCalldelavary.setVisibility(View.VISIBLE);
                    binding.fabCallwahtsdelevary.setVisibility(View.VISIBLE);

                    getAnimation(binding.fabCalldelavary, open);
                    getAnimation(binding.fabCallwahtsdelevary, open);
                    binding.tvCalldelevary.setVisibility(View.GONE);

                }

            }
        });

    }

    private void getAnimation(View view, Animation animation) {
        view.clearAnimation();
        view.setAnimation(animation);
        view.getAnimation().start();
    }


    private void getLikes() {

        binding.layLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pro_adds != null) {
                    modelLike = new ModelLike(pro_adds.getName(), sharedEditor.loadData().get(SharedEditor.KEY_USER_PHONE), true);
                    likesRf.push().setValue(modelLike);
                    binding.layLike.setVisibility(View.INVISIBLE);
                    binding.layIslike.setVisibility(View.VISIBLE);
                    getLikesCounter();
                }

                if (pro_of_product != null) {
                    modelLike = new ModelLike(pro_of_product.getName(), sharedEditor.loadData().get(SharedEditor.KEY_USER_PHONE), true);
                    likesRf.push().setValue(modelLike);
                    binding.layLike.setVisibility(View.INVISIBLE);
                    binding.layIslike.setVisibility(View.VISIBLE);
                    getLikesCounter();
                }
            }
        });

        binding.layIslike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pro_adds != null) {

                    delettingTheLikes(pro_adds.getName());
                    //  isLikes(pro_adds.getName());
                    //getLikesCounter();
                }

                if (pro_of_product != null) {
                    delettingTheLikes(pro_of_product.getName());
                    // isLikes(pro_of_product.getName());
                    //getLikesCounter();
                }


            }
        });

    }

    private void delettingTheLikes(String name) {
        Query query = likesRf.orderByChild("prouductKey").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    ModelLike blog = appleSnapshot.getValue(ModelLike.class);
                    if (sharedEditor.loadData().get(SharedEditor.KEY_USER_PHONE).equalsIgnoreCase(blog.getPhoneNumber())) {
                        appleSnapshot.getRef().removeValue();
                        binding.layLike.setVisibility(View.VISIBLE);
                        binding.layIslike.setVisibility(View.GONE);
                        getLikesCounter();
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    void isLikes(final String name) {
        likesRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    ModelLike blog = dataSnapshot1.getValue(ModelLike.class);

                    if (blog.getProuductKey().equalsIgnoreCase(name) &&
                            blog.getPhoneNumber().equalsIgnoreCase(sharedEditor.loadData().get(SharedEditor.KEY_USER_PHONE))) {
                        binding.layLike.setVisibility(View.GONE);
                        binding.layIslike.setVisibility(View.VISIBLE);

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAdda() {

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder()

                .build();


        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest);
    }

    private void onCall() {

        binding.fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodId != null) {


                    callWithNumb(pro_adds.getPhoneOfMarket());

                } else {

                    callWithNumb(pro_of_product.getPhoneOfMarket());
                }

            }
        });

        binding.fabCallwahts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodId != null) {

                    WhatesSend(pro_adds.getPhoneOfMarket());

                } else {

                    WhatesSend(pro_of_product.getPhoneOfMarket());
                }

            }
        });
    }

    private void callWithNumb(String pro_adds) {
        Uri number = Uri.parse("tel:" + pro_adds);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }


    private void getCountCart() {
        if (database.userDao().getCount().size() == 0) {
            binding.fabcont.hide();
        } else {
            binding.fabcont.show();
            binding.fabcont.setCount(database.userDao().getCount().size());
        }

    }

    void isFavorite(String name) {
        database = AppDatabase.getDatabaseInstance(this);
        int siz = database.userDao().getAll().size();
        List<Pro_Of_Product> list = database.userDao().getAll();
        for (int i = 0; i < siz; i++) {
            if (name.equalsIgnoreCase(list.get(i).getName())) {
                binding.layFab.setVisibility(View.INVISIBLE);
                binding.layIsfab.setVisibility(View.VISIBLE);

            }

        }


    }

    void deleting(String name) {
        database = AppDatabase.getDatabaseInstance(this);
        int siz = database.userDao().getAll().size();
        List<Pro_Of_Product> list = database.userDao().getAll();
        for (int i = 0; i < siz; i++) {
            if (name.equalsIgnoreCase(list.get(i).getName())) {
                database.userDao().delete(list.get(i));

                binding.layFab.setVisibility(View.VISIBLE);
                binding.layIsfab.setVisibility(View.GONE);
                Toast.makeText(this, "تم الحذف", Toast.LENGTH_SHORT).show();
                getCountCart();

            }

        }


    }


    private void addToCart() {
        binding.fabcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsCartActivity.this, CartActivity.class));
                finish();
            }
        });
        binding.layFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (foodId != null) {

                    database.userDao().insertUser(new Pro_Of_Product(pro_adds.getName()
                            , pro_adds.getDescShort()
                            , pro_adds.getDescription()
                            , pro_adds.getPrice(),
                            pro_adds.getNameOfMarket(),
                            pro_adds.getPhoneOfMarket(),
                            pro_adds.getLocationOfMarket()
                            , pro_adds.getImage(),
                            pro_adds.isDeleivary()
                    ));

                } else {
                    database.userDao().insertUser(new Pro_Of_Product(pro_of_product.getName()
                            , pro_of_product.getDescShort()
                            , pro_of_product.getDescription()
                            , pro_of_product.getPrice(),
                            pro_of_product.getNameOfMarket(),
                            pro_of_product.getPhoneOfMarket()
                            , pro_of_product.getLocationOfMarket()
                            , pro_of_product.getImage(),
                            pro_of_product.isDeleivary()


                    ));

                }
                binding.layFab.setVisibility(View.INVISIBLE);
                binding.layIsfab.setVisibility(View.VISIBLE);

                getCountCart();
                Toast.makeText(DetailsCartActivity.this, "تم اضافتها للمفضلة", Toast.LENGTH_SHORT).show();


            }
        });

        binding.layIsfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = AppDatabase.getDatabaseInstance(DetailsCartActivity.this);
                if (pro_adds != null) {
                    deleting(pro_adds.getName());
                } else if (pro_of_product != null) {
                    deleting(pro_of_product.getName());
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //textQuantity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // textQuantity();
        addToCart();
        getCountCart();

    }

    /* private void textQuantity() {
         binding.tvPlus.setOnClickListener(new View.OnClickListener() {
             @SuppressLint("SetTextI18n")
             @Override
             public void onClick(View view) {
                 if (quntity <= 19) {
                     quntity++;
                     binding.tvQuntity.setText(quntity + "");
                 } else {
                     Toast.makeText(DetailsCartActivity.this, "لا يمكن طلب أكثر من هذا العدد", Toast.LENGTH_SHORT).show();
                 }

             }
         });

         binding.tvMinus.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (quntity > 1) {
                     quntity--;
                     binding.tvQuntity.setText(quntity + "");
                 }

             }
         });


     }
 */
    private void getDetailId(String foodid) {

        try {
            if (foodid != null) {
                if (Commen.isNetworkOnline(DetailsCartActivity.this)) {
                    catogry.child(foodid).addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            pd.dismiss();
                            pro_adds = dataSnapshot.getValue(Pro_Of_Product.class);
                            if (pro_adds != null) {
                                binding.nameDetailes.setText(pro_adds.getName());
                                binding.descDetail.setText(pro_adds.getDescription());
                                binding.tvProce.setText(" ج.م" + pro_adds.getPrice());
                                binding.tvNameMarket.setText("اسم المحل :" + pro_adds.getNameOfMarket());
                                binding.nameDetailes.setText(pro_adds.getName());
                                Glide.with(DetailsCartActivity.this).load(pro_adds.getImage()).into(binding.imageDetail);
                                binding.imageDetail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(DetailsCartActivity.this, ShowFullScreenActivity.class);
                                        intent.putExtra("image", pro_adds.getImage());
                                        startActivity(intent);
                                    }
                                });
                                isFavorite(pro_adds.getName());
                                isLikes(pro_adds.getName());
                                getLikesCounter();
                                callOnDelevary();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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
            Toast.makeText(DetailsCartActivity.this, "ليس لديه واتس ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    void shareTheProduct(Pro_Of_Product pro_of_product) {
        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareMessage = "الان علي ابليكيشن سوق برديس " + "\n";
            String shareName = pro_of_product.getName() + "\n";
            String shareNumber = "رقم الهاتف " + pro_of_product.getPhoneOfMarket() + "\n";
            String shareNamMarket = "اسم المحل :" + pro_of_product.getNameOfMarket() + "\n";
            String sharelocationMarket = "عنوان المحل : " + pro_of_product.getLocationOfMarket() + "\n";
            String shareAll = shareName + shareMessage + shareNumber + shareNamMarket + sharelocationMarket;

            shareMessage = shareAll + "https://play.google.com/store/apps/details?id="
                    + BuildConfig.APPLICATION_ID + "\n\n";
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(sharingIntent, "تطبيق سوق برديس"));
        } catch (Exception e) {

            Toast.makeText(this, "فشلت العملية", Toast.LENGTH_SHORT).show();
        }
    }

}