package com.bardisammar.elsalamcity.cart.roomdatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.amulyakhare.textdrawable.TextDrawable;
import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.cart.DetailsCartActivity;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;
import com.bumptech.glide.Glide;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RoomAdpter extends RecyclerView.Adapter<RoomAdpter.MyViewHolder> {

    ArrayList<Pro_Of_Product> list;
    Context mcontext;
    private Callback mCallback;

    public ArrayList<Pro_Of_Product> getList() {
        return list;
    }

    public void setList(ArrayList<Pro_Of_Product> list) {
        this.list = list;
    }

    public Callback getmCallback() {
        return mCallback;
    }

    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    AppDatabase database = AppDatabase.getDatabaseInstance(mcontext);

    public RoomAdpter(ArrayList<Pro_Of_Product> names, Context mcontext) {
        this.list = names;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        MyViewHolder chatViewHolder = new MyViewHolder(view);
        return chatViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.txtcart_name.setText(list.get(position).getName());
        holder.tv_desc.setText(list.get(position).getDescShort());
        holder.txtcart_price.setText(" ج.م"+list.get(position).getPrice());
        holder.tv_nmarket.setText(list.get(position).getNameOfMarket());
        Glide.with(mcontext)
                .load(list.get(position)
                        .getImage())
                .into(holder.cart_imag);
        final Pro_Of_Product pro_of_product=list.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mcontext, DetailsCartActivity.class);
                intent.putExtra("pro_cato",pro_of_product);
                mcontext.startActivity(intent);
            }
        });



        final Pro_Of_Product mUser = list.get(position);

        holder.imagedelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.onDeleteClick(mUser);
                holder.onBind(position);

            }
        });
////        TextDrawable textDrawable = TextDrawable.builder()
////                .buildRound("" + list.get(position).getNumberQuntity(), Color.RED);
////        holder.cart_imag.setImageDrawable(textDrawable);
//        Locale locale = new Locale("ar", "EG");
//        NumberFormat ftm = NumberFormat.getCurrencyInstance(locale);
////        Double price = (Double.parseDouble(list.get(position).getPrice())) * (Integer.parseInt(list.get(position).getNumberQuntity()));
////        holder.txtcart_price.setText(ftm.format(price));
//        holder.txtcart_name.setText(list.get(position).getName());


    }




    public interface Callback {
        void onDeleteClick(Pro_Of_Product mUser);
    }

    public void addItems(List<Pro_Of_Product> userList) {
            list = (ArrayList<Pro_Of_Product>) userList;
            notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0) {
            Commen.countcart=list.size();
            return list.size();
        } else {
            return 0;
        }    }


    public class MyViewHolder extends BaseViewHolder {

        CircleImageView cart_imag;
        ImageView imagedelet;
        TextView txtcart_price,txtcart_name,tv_desc,tv_nmarket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtcart_name=itemView.findViewById(R.id.cart_item_name);
            txtcart_price=itemView.findViewById(R.id.cart_item_price);
            cart_imag=itemView.findViewById(R.id.card_item_v);
            imagedelet=itemView.findViewById(R.id.card_item_delet);
            tv_nmarket=itemView.findViewById(R.id.item_Nmarket);
            tv_desc=itemView.findViewById(R.id.tv_descp);
        }

        @Override
        protected void clear() {
            txtcart_name.setText("");

        }


    }
}

