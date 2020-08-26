package com.elsalmcity.elsalamcity.notifecation;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingltion {

    private static  MySingltion mInastans;
    private static Context mcontsxt;
    private RequestQueue requestQueue;

    public MySingltion(Context context) {

        this.mcontsxt=context;
        this.requestQueue = getRequestQue();
    }

    private RequestQueue getRequestQue() {

        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(mcontsxt.getApplicationContext());


        }
        return requestQueue;
    }
    public static synchronized MySingltion getInstance(Context context){

        if (mInastans==null){
            mInastans=new MySingltion((context));
        }
        return mInastans;
    }

    public <T> void addToRequest (Request<T> request){

       getRequestQue().add(request);
    }


}
