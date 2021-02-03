package com.bardisammar.elsalamcity.cart;

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
import com.bardisammar.elsalamcity.notifecation.DetailNotiyActivity;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.FragmentProOfCatogBinding;
import com.bardisammar.elsalamcity.home.MenuViewHolder;
import com.bardisammar.elsalamcity.pojo.Catogray;
import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;
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


public class Pro_Of_CatogFragment extends Fragment {


    FragmentProOfCatogBinding binding;
    AdpterPro adpterProduct;
    ArrayList<Pro_Of_Product> list;
    HashMap<String, String> imagelist;
    FirebaseDatabase database;
    ProgressDialog pd;
    ArrayList<Pro_Of_Product> listproduct = new ArrayList<>();
    FirebaseRecyclerAdapter<Catogray, MenuViewHolder> firbase;
    Handler handler = new Handler();
    private int dotscount = 0;
    GenericSliderAdapter<Pro_Of_Product> sliderAdapter;
    private ImageView[] dots;
    public Pro_Of_CatogFragment() {
        // Required empty public constructor
    }

    public static Pro_Of_CatogFragment newInstance() {
        return new Pro_Of_CatogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pro__of__catog, container, false);
        View view = binding.getRoot();
        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();




      //  loadDataIntentnt();
        Load();
       // stuepSlider();
loadSlider();
        return view;


    }

    private void loadSlider() {
        final DatabaseReference bunner = database.getReference("اعلانات");
        bunner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listproduct.clear();
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                    Pro_Of_Product blog = postsnapshot.getValue(Pro_Of_Product.class);
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

    private void initViewPager(List<Pro_Of_Product> silderList) {

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

    private void initSliderAdapter(final List<Pro_Of_Product> silderList) {
        sliderAdapter = new GenericSliderAdapter<Pro_Of_Product>(getContext(), silderList) {
            @Override
            public View getMyView(ViewGroup container, int position, final Pro_Of_Product data) {

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

        final DatabaseReference bunner =  database.getReference("اعلانات");
        Query mQuery = bunner.orderByChild("name").equalTo(name);
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                    key=postsnapshot.getKey();

                }
                Commen.print("slider", key);

                Intent intent = new Intent(getActivity(), DetailsCartActivity.class);
                intent.putExtra("proName",key);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    String keychild;

//    private void stuepSlider() {
//        try {
//            imagelist = new HashMap<>();
//            final DatabaseReference bunner = database.getReference("اعلانات");
//            bunner.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
//
//                        Pro_Of_Product banner = postsnapshot.getValue(Pro_Of_Product.class);
//                        keychild = postsnapshot.getKey();
//                        imagelist.put(banner.getName() + "ammar" + keychild, banner.getImage() + "");
//
//
//                    }
//                    for (final String key : imagelist.keySet()) {
//
//                        String[] keysplite = key.split("ammar");
//                        String nameoffood = keysplite[0];
//                        String idoffood = keysplite[1];
//
//                        //create slider
//                        final TextSliderView textSliderView = new TextSliderView(getContext());
//                        textSliderView
//                                .description(nameoffood)
//                                .image(imagelist.get(key))
//                                .setScaleType(BaseSliderView.ScaleType.Fit)
//                                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                    @Override
//                                    public void onSliderClick(BaseSliderView slider) {
//
//
//                                        Intent intent = new Intent(getActivity(), DetailsCartActivity.class);
//                                        intent.putExtras(textSliderView.getBundle());
//                                        startActivity(intent);
//
//                                    }
//                                });
//                        textSliderView.bundle(new Bundle());
//                        textSliderView.getBundle().putString("proName", idoffood);
//                        binding.slider.addSlider(textSliderView);
//
//                        bunner.removeEventListener(this);
//
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//            binding.slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//            binding.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//            binding.slider.setCustomAnimation(new DescriptionAnimation());
//            binding.slider.setDuration(4000);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

    private void loadDataIntentnt() {
        if (Commen.isNetworkOnline(getActivity())) {
            try {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("جارى التحميل...");
                pd.show();
                FirebaseDatabase.getInstance().getReference().child("منتجات").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            pd.dismiss();
                            Log.d("Tagstorag", "onSuccess: " + dataSnapshot1.child("name").getValue());
                            Pro_Of_Product blog = dataSnapshot1.getValue(Pro_Of_Product.class);


                            list.add(blog);


                        }
                        adpterProduct = new AdpterPro(list, getActivity());
                        binding.listHome.setAdapter(adpterProduct);


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
            Toast.makeText(getActivity(), "انقطع الاتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        }


    }
    private void Load() {
if (Commen.isNetworkOnline(getActivity())){

    pd = new ProgressDialog(getActivity());
    pd.setMessage("جارى التحميل...");
    pd.show();

    binding.listHome.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    binding.listHome.setHasFixedSize(true);
    Query query = FirebaseDatabase.getInstance().getReference().child("أقسام المنتجات");


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

            Typeface   face = Typeface.createFromAsset(getActivity().getAssets(),"homefont.otf");

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
    Toast.makeText(getActivity(), "لايوجد اتصال باللانترنت ", Toast.LENGTH_SHORT).show();
}


    }
    private void intentMothed(String value, String namecato) {

        Intent intent = new Intent(getActivity(), OffersActivity.class);
        intent.putExtra("menuId", value);
        intent.putExtra("name", namecato);
        this.startActivity(intent);
    }

}