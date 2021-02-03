package com.bardisammar.elsalamcity.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bardisammar.elsalamcity.R;


public class MenuViewHolder extends RecyclerView.ViewHolder  {

    public TextView textView;
   public ImageView imageView;
    ImageView delete,update;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv_item_title);
        imageView = itemView.findViewById(R.id.imag_item);

    }}