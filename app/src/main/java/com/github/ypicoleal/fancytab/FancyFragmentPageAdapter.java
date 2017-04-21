package com.github.ypicoleal.fancytab;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public abstract class FancyFragmentPageAdapter extends FragmentPagerAdapter {
    public FancyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public abstract Object getPageImageURL(int position);

}
