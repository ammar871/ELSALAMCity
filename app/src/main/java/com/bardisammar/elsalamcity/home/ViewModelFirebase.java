package com.bardisammar.elsalamcity.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bardisammar.elsalamcity.pojo.Catogray;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ViewModelFirebase extends ViewModel {

    public MutableLiveData<FirebaseRecyclerOptions<Catogray>> multibLiveData=new MutableLiveData<>();
    public void getData(){

        Query query = FirebaseDatabase.getInstance().getReference().child("category");

        FirebaseRecyclerOptions<Catogray> options = new FirebaseRecyclerOptions.Builder<Catogray>()
                .setQuery(query, Catogray.class)
                .build();
        multibLiveData.postValue(options);
    }

}
