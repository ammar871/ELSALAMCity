package com.elsalmcity.elsalamcity.products;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.details.DetailsActivity;
import com.elsalmcity.elsalamcity.pojo.Product;

import java.util.ArrayList;


public class AdpterProduct extends RecyclerView.Adapter<AdpterProduct.ViewHolderVidio> {

    private ArrayList<Product> list;
    private Context mcontext;


    public AdpterProduct(ArrayList<Product> list, Context mcontext) {

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
        holder.desc.setText(list.get(position).getDescshort());

        final Product product = list.get(position);
        Glide.with(mcontext)
                .load(list.get(position)
                        .getUris().get(0))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMothed(DetailsActivity.class, product);
            }
        });

    }


    private void intentMothed(Class a, Product value) {

        Intent intent = new Intent(mcontext, a);
        intent.putExtra("product", value);
        mcontext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderVidio extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, desc;
        ImageView delete,update;


        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.tv_name);
            desc = itemView.findViewById(R.id.tv_desc);

        }


    }



}
