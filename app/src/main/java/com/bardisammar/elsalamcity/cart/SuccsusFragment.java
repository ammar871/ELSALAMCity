package com.bardisammar.elsalamcity.cart;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.databinding.FragmentSuccsusBinding;
import com.bardisammar.elsalamcity.home.HomeActivity;


public class SuccsusFragment extends AppCompatDialogFragment {
    FragmentSuccsusBinding binding;
    public SuccsusFragment() {



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_succsus, container, false);
        View view = binding.getRoot();

        binding.btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });
        return view;
    }

}