package com.bardisammar.elsalamcity.news;

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

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.notifecation.Data;

import java.util.ArrayList;

public class AdpterNews extends RecyclerView.Adapter<AdpterNews.ViewHolderVidio> {

    private ArrayList<Data> list;
    private Context mcontext;


    public AdpterNews(ArrayList<Data> list, Context mcontext) {

        this.list = list;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        ViewHolderVidio chatViewHolder = new ViewHolderVidio(view);
        return chatViewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        holder.name.setText(list.get(position).getTitle());
        holder.desc.setText(list.get(position).getBody());
final Data data=list.get(position);
        Glide.with(mcontext)
                .load(list.get(position).getImage())
                .into(holder.imageView);
      holder.imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              intentMothed(DetailsNewsActivity.class,data);

          }
      });


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
            name = itemView.findViewById(R.id.tv_title);
            desc = itemView.findViewById(R.id.tv_body);

        }


    }
    private void intentMothed(Class a, Data value) {

        Intent intent = new Intent(mcontext, a);
        intent.putExtra("news", value);
        mcontext.startActivity(intent);
    }


}
