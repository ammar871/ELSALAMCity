package com.bardisammar.elsalamcity.adpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.cart.DetailsCartActivity;
import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdpterHomeProducts extends RecyclerView.Adapter<AdpterHomeProducts.ViewHolderVidio> {

    private ArrayList<Pro_Of_Product> list;
    private Context mcontext;


    public AdpterHomeProducts(ArrayList<Pro_Of_Product> list, Context mcontext) {

        this.list = list;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        ViewHolderVidio chatViewHolder = new ViewHolderVidio(view);
        return chatViewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.desc.setText(list.get(position).getDescShort());

        final Pro_Of_Product product = list.get(position);
        Glide.with(mcontext)
                .load(list.get(position).getImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intentMothed(DetailsCartActivity.class, product,product.getName());
            }
        });

    }


    private void intentMothed(Class a, Pro_Of_Product value,String name) {

        Intent intent = new Intent(mcontext, a);
        intent.putExtra("pro_cato", value);
        intent.putExtra("proName", name);
        mcontext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderVidio extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, desc;



        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.tv_name);
            desc = itemView.findViewById(R.id.tv_desc);

        }


    }



}
