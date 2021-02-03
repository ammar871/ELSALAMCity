package com.bardisammar.elsalamcity.adpters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


/**
 * Created by mahmoud elhofy on 22/06/2020.
 */

public abstract class GenericSliderAdapter<T>  extends PagerAdapter {

    private List<T> tList;
    private Context context;


    public GenericSliderAdapter(Context context, List<T> tList) {
        this.context = context;
        this.tList = tList;
    }

    @Override
    public int getCount() {
        if (tList == null)
            return 0;
        return tList.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==(T) object;
    }

    public abstract View getMyView(ViewGroup container, int position,T t);


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return getMyView(container,position,tList.get(position));
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface SliderBinding {
        void onSliderLoad(View view);
    }

}
