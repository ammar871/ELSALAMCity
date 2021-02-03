package com.bardisammar.elsalamcity.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdpterPro extends RecyclerView.Adapter<AdpterPro.ViewHolderVidio> {


    private ArrayList<Pro_Of_Product> list;
    private Context mcontext;
    public static String key;

    public AdpterPro(ArrayList<Pro_Of_Product> list, Context mcontext) {

        this.list = list;
        this.mcontext = mcontext;
    }


    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pro, parent, false);
        return new ViewHolderVidio(view);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        holder.text_name.setText(list.get(position).getName());
        holder.tv_desc.setText(list.get(position).getDescShort());
        holder.tv_price.setText(" ج.م" + list.get(position).getPrice());
        holder.tv_nameMarket.setText(list.get(position).getNameOfMarket());
        Glide.with(mcontext)
                .load(list.get(position)
                        .getImage())
                .into(holder.imageView);
     //   isLikes(list.get(position).getName());

        DatabaseReference likesRf = FirebaseDatabase.getInstance().getReference().child("الاعجابات");
        likesRf.orderByChild("prouductKey")
                .equalTo(list.get(position).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.tv_likes.setText(dataSnapshot.getChildrenCount()+"");
                //  Log.d("sizess", "isLikes: " + count[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       // Log.d("sizess1", "isLikes: " + isLikes(list.get(position).getName()));

        final Pro_Of_Product pro_of_product = list.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, DetailsCartActivity.class);
                intent.putExtra("pro_cato", pro_of_product);
                mcontext.startActivity(intent);

            }
        });


    }



    int isLikes(final String name) {
        final int[] count = {0};
        DatabaseReference likesRf = FirebaseDatabase.getInstance().getReference().child("الاعجابات");
       likesRf.orderByChild("prouductKey")
                .equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        count[0] =(int) dataSnapshot.getChildrenCount();
                      //  Log.d("sizess", "isLikes: " + count[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//        Query query = likesRf.orderByChild("prouductKey").equalTo(name);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
return count[0];
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderVidio extends RecyclerView.ViewHolder {
        TextView text_name, tv_desc, tv_price, tv_nameMarket,tv_likes;
        ImageView imageView;
        ImageView delete, update;

        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);

            tv_price = itemView.findViewById(R.id.item_pricep);
            tv_desc = itemView.findViewById(R.id.tv_descp);
            tv_nameMarket = itemView.findViewById(R.id.item_Nmarket);
            text_name = itemView.findViewById(R.id.tv_namep);
            imageView = itemView.findViewById(R.id.imageViewp);
            tv_likes = itemView.findViewById(R.id.card_item_likes);
        }


    }


}
