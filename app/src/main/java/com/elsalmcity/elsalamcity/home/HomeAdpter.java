package com.elsalmcity.elsalamcity.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elsalmcity.elsalamcity.R;
import com.elsalmcity.elsalamcity.pojo.Catogray;

import java.util.ArrayList;

public class HomeAdpter extends RecyclerView.Adapter<HomeAdpter.ViewHolderVidio> {

    private ArrayList<Catogray> list;
    private Context mcontext;


    public HomeAdpter(ArrayList<Catogray> list, Context mcontext) {

        this.list = list;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catogray, parent, false);
        ViewHolderVidio chatViewHolder = new ViewHolderVidio(view);
        return chatViewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        holder.textView.setText(list.get(position).getName());
        Glide.with(mcontext)
                .load(list.get(position)
                        .getImage())
                .into(holder.imageView);

    }


    private void intentMothed(Class a, String value) {

        Intent intent = new Intent(mcontext, a);
        intent.putExtra("video", value);
        mcontext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderVidio extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;


        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item_title);
            imageView = itemView.findViewById(R.id.imag_item);
        }


    }
}

