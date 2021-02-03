package com.bardisammar.elsalamcity.products;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bardisammar.elsalamcity.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

   public ImageView imageView;
  public   TextView name, desc;
    ImageView delete,update;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView);
        name = itemView.findViewById(R.id.tv_name);
        desc = itemView.findViewById(R.id.tv_desc);

    }


}