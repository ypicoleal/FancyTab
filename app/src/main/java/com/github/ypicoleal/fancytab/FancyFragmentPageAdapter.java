package com.github.ypicoleal.fancytab;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ImageView;


public abstract class FancyFragmentPageAdapter extends FragmentPagerAdapter {
    public FancyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public abstract String getPageImageURL(int position);


    public interface ImageLoader{
        public void loadImage(ImageView view, String url);
    }
}
