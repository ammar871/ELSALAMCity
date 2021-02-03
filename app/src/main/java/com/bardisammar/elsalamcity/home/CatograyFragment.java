package com.bardisammar.elsalamcity.home;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bardisammar.elsalamcity.adpters.GenericSliderAdapter;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.databinding.FragmentCatograyBinding;
import com.bardisammar.elsalamcity.notifecation.DetailNotiyActivity;
import com.bardisammar.elsalamcity.pojo.Catogray;
import com.bardisammar.elsalamcity.pojo.Product;
import com.bardisammar.elsalamcity.products.ProductsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class CatograyFragment extends Fragment implements GenericSliderAdapter.SliderBinding{

    private static final String TAG = "ammar";
    FragmentCatograyBinding binding;
    ArrayList<Catogray> list;
    ProgressDialog pd;
    ArrayList<Catogray> listproduct = new ArrayList<>();
    Handler handler = new Handler();
    FirebaseRecyclerAdapter<Catogray, MenuViewHolder> firbase;
    HashMap<String, String> imagelist;
    FirebaseDatabase database;
    DatabaseReference reF;
    private int dotscount = 0;
    GenericSliderAdapter<Catogray> sliderAdapter;
    private ImageView[] dots;
    public CatograyFragment() {

    }


    public static CatograyFragment newInstance() {
        return new CatograyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_catogray, container, false);
        View view = binding.getRoot();

        database = FirebaseDatabase.getInstance();
        binding.listHome.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.listHome.setHasFixedSize(true);

        if (Commen.isNetworkOnline(getActivity())){

        }
        Load();

        loadSlider();

    //    dots = null;
        binding.sliderDots.removeAllViewsInLayout();

       // Log.d("countslider", "initViewPager"+sliderAdapter.getCount());

        //stuepSlider();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.listHome.setNestedScrollingEnabled(false);
        binding.listHome.setFocusable(false);
        binding.lautamp.requestFocus();
    }

    private void loadSlider() {
        final DatabaseReference bunner = database.getReference("Notifecations");
        bunner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listproduct.clear();
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                    Catogray blog = postsnapshot.getValue(Catogray.class);
                    listproduct.add(blog);

                }

                initSliderAdapter(listproduct);
                initViewPager(listproduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initViewPager(List<Catogray> silderList) {

        dotscount = sliderAdapter.getCount();

        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            binding.sliderDots.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageResource(R.drawable.nonactive_dot);
                }

                dots[position].setImageResource(R.drawable.active_dot);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        handler.postDelayed(new Runnable() {
           // int item_count = 0;

            @Override
            public void run() {


                if (binding.viewPager.getCurrentItem() < listproduct.size()-1 ) {
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                } else {
                    binding.viewPager.setCurrentItem(0);
                }
                handler.postDelayed(this, 7000);


            }
        }, 2000);


    }

    private void initSliderAdapter(final List<Catogray> silderList) {
        sliderAdapter = new GenericSliderAdapter<Catogray>(getContext(), silderList) {
            @Override
            public View getMyView(ViewGroup container, int position, final Catogray data) {

                LayoutInflater layoutInflater = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View item_view = layoutInflater.inflate(R.layout.slidingimages_layout, container, false);
                ImageView imgSlide = item_view.findViewById(R.id.image);


                Glide.with(getContext())
                        .load(silderList.get(position).getImage())
                        .placeholder(R.drawable.logo)
                        .into(imgSlide);
                ((TextView) item_view.findViewById(R.id.textslider))
                        .setText(silderList.get(position).getName());


                item_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getkey(data.getName());
                     //   Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();


                    }
                });


                container.addView(item_view);
                return item_view;
            }
        };
        binding.viewPager.setAdapter(sliderAdapter);
    }
   String key;
    private void getkey(String name) {

        final DatabaseReference bunner = database.getReference("Notifecations");
        Query mQuery = bunner.orderByChild("name").equalTo(name);
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                  key=postsnapshot.getKey();

                }
                Commen.print("slider", key);

                Intent intent = new Intent(getActivity(), DetailNotiyActivity.class);
                intent.putExtra("proName",key);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Load() {

        if (Commen.isNetworkOnline(getActivity())){

            pd = new ProgressDialog(getActivity());
            pd.setMessage("جارى التحميل...");
            pd.show();


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
                    Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"homefont.otf");
                    final String key = firbase.getRef(i).getKey();
                    menuViewHolder.textView.setText(categoryb.getName());
                    menuViewHolder.textView.setTypeface(face);
                    Glide.with(getActivity())
                            .load(categoryb.getImage())
                            .into(menuViewHolder.imageView);
                    menuViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intentMothed(key, categoryb.getName());
                        }
                    });
                    pd.dismiss();
                }
            };
            binding.listHome.setAdapter(firbase);
            firbase.startListening();


        }else {
            Toast.makeText(getActivity(), "لايوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        }

    }


    String keychild;

    @Override
    public void onResume() {
        super.onResume();

    }

  /*  private void stuepSlider() {
        try {
            imagelist = new HashMap<>();
            final DatabaseReference bunner = database.getReference("Notifecations");
            bunner.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                        Product blog = postsnapshot.getValue(Product.class);
                        keychild = postsnapshot.getKey();
                        imagelist.put(blog.getName() + "ammar" + keychild, blog.getImage() + "");


                    }
                    for (final String key : imagelist.keySet()) {

                        String[] keysplite = key.split("ammar");
                        String nameoffood = keysplite[0];
                        String idoffood = keysplite[1];

                        //create slider
                        final TextSliderView textSliderView = new TextSliderView(getContext());
                        textSliderView
                                .description(nameoffood)
                                .image(imagelist.get(key))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {


                                        Intent intent = new Intent(getActivity(), DetailNotiyActivity.class);
                                        intent.putExtras(textSliderView.getBundle());
                                        startActivity(intent);

                                    }
                                });
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("proName", idoffood);
                        binding.slider.addSlider(textSliderView);

                        bunner.removeEventListener(this);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            binding.slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            binding.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            binding.slider.setCustomAnimation(new DescriptionAnimation());
            binding.slider.setDuration(4000);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/

    private void intentMothed(String value, String namecato) {

        Intent intent = new Intent(getActivity(), ProductsActivity.class);
        intent.putExtra("menuId", value);
        intent.putExtra("name", namecato);
        this.startActivity(intent);
    }

    @Override
    public void onSliderLoad(View view) {

    }
}