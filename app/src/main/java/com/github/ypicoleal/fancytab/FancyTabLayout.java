package com.github.ypicoleal.fancytab;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


public class FancyTabLayout extends FrameLayout implements FancyTabAdapter.ListItemClickListener{
    private ViewPager viewPager;
    private RecyclerView tabsContainer;
    private FancyTabAdapter fancyTabAdapter;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        float oldPositionOffset = 0;
        int viewWidth;
        float residual;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int currPosition = viewPager.getCurrentItem();

            int firstItem = ((LinearLayoutManager) tabsContainer.getLayoutManager()).findFirstVisibleItemPosition();

            View tab = tabsContainer.getLayoutManager().findViewByPosition(firstItem);

            if(oldPositionOffset == 0){
                viewWidth = tab.getWidth();
                residual = 0;
                if (position != currPosition){
                    oldPositionOffset = 1;
                }
            }

            float scrollF = ((positionOffset - oldPositionOffset) * viewWidth) + residual;
            residual = scrollF % 1;

            int scroll = (int) scrollF;

            if(positionOffset == 0){
                tab = tabsContainer.getLayoutManager().findViewByPosition(position);
                scroll = (int) tab.getX();
            }

            Log.i("scroll",  scrollF + ", " + scroll);

            tabsContainer.scrollBy(scroll, 0);

            Log.i("values", "current: " + currPosition + ", position: " + position + ", offset: " + positionOffset + " residual: " + residual);

            animateSelection(position, positionOffset);

            oldPositionOffset = positionOffset;
        }

        private void animateSelection(int position, float positionOffset) {
            View tab = tabsContainer.getLayoutManager().findViewByPosition(position);
            TextView tabText = (TextView) tab.findViewById(R.id.tab_text);

            float currTextSize = 24f;
            float targetSize = 16f;

            int startColor = ContextCompat.getColor(tabText.getContext(), android.R.color.white);
            int targetColor = ContextCompat.getColor(tabText.getContext(), R.color.facyTabTextColor);

            float size = currTextSize - ((currTextSize - targetSize) * positionOffset);

            ArgbEvaluator evaluator = new ArgbEvaluator();
            int color = (int) evaluator.evaluate(positionOffset, startColor, targetColor);

            tabText.setTextSize(size);
            tabText.setTextColor(color);
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
