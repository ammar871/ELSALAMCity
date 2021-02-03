package com.bardisammar.elsalamcity.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bardisammar.elsalamcity.R;

import java.util.ArrayList;

public class AdpterShowAll extends RecyclerView.Adapter<AdpterShowAll.ViewHolderVidio> {

    private ArrayList<String> list;
    private Context mcontext;

    private final OnItemClickListener listener;




    public interface OnItemClickListener {
        void onItemClick(View view);
    }


    public AdpterShowAll(ArrayList<String> list, Context mcontext, OnItemClickListener listener) {
        this.list = list;
        this.mcontext = mcontext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_image, parent, false);
        return new ViewHolderVidio(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {

        Glide.with(mcontext)
                .load(list.get(position))
                .into(holder.imageView);




    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderVidio extends RecyclerView.ViewHolder {

        ImageView imageView;


        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_show);
        }


    }
}

