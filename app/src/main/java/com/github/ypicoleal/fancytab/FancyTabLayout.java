package com.github.ypicoleal.fancytab;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;


public class FancyTabLayout extends FrameLayout implements FancyTabAdapter.ListItemClickListener{
    private ViewPager viewPager;
    private RecyclerView tabsContainer;
    private FancyTabAdapter fancyTabAdapter;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        float oldPositionOffset = 0;
        int viewWidth;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int currPosition = viewPager.getCurrentItem();

            int firstItem = ((LinearLayoutManager) tabsContainer.getLayoutManager()).findFirstVisibleItemPosition();

            View tab = tabsContainer.getLayoutManager().findViewByPosition(firstItem);

            if(oldPositionOffset == 0){
                viewWidth = tab.getWidth();
                Log.i("offset", "zero " + tab.getWidth());
            }



            int scroll = (int) ((positionOffset * viewWidth) - (oldPositionOffset * viewWidth));

            if (position != currPosition){
                scroll = -scroll;
            }

            Log.i("scroll",  "" + scroll);

            tabsContainer.scrollBy(scroll, 0);
            oldPositionOffset = positionOffset;
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public FancyTabLayout(@NonNull Context context) {
        super(context);
    }

    public FancyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FancyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FancyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View root = inflate(getContext(), R.layout.fancy_tab_layout, this);
        tabsContainer = (RecyclerView) root.findViewById(R.id.tabs_container);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        tabsContainer.setLayoutManager(layoutManager);
        tabsContainer.setHasFixedSize(true);

        if (fancyTabAdapter != null){
            tabsContainer.setAdapter(fancyTabAdapter);
        }
    }

    public void setupWithViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        fancyTabAdapter = new FancyTabAdapter(viewPager.getAdapter(), this);
        if (tabsContainer != null){
            tabsContainer.setAdapter(fancyTabAdapter);
        }
        this.viewPager.addOnPageChangeListener(pageChangeListener);
    }


    @Override
    public void onListItemClick(int position) {
        viewPager.setCurrentItem(position, true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (viewPager != null){
            viewPager.removeOnPageChangeListener(pageChangeListener);
        }
    }
}
