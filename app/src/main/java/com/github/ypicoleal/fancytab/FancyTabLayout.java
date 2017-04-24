package com.github.ypicoleal.fancytab;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FancyTabLayout extends FrameLayout implements FancyTabAdapter.ListItemClickListener{

    static final int ONLYTEXT = 1;
    static final int IMGTITLE = 2;
    static final int ONLYIMG = 3;

    private ViewPager viewPager;
    private RecyclerView tabsContainer;
    private FancyTabAdapter fancyTabAdapter;
    private ImageLoader imageLoader;
    private TextView titleTV;
    private RelativeLayout floattingTab;

    private int tabFormat;
    private boolean circleImg;

    private boolean canScroll = false;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            canScroll = true;
            int paddinStart = getResources().getDimensionPixelOffset(R.dimen.fancy_tab_paddin_start);
            View tab = tabsContainer.getLayoutManager().findViewByPosition(position);

            if (tab != null) {
                int viewWidth = -tab.getWidth();

                float targetX = paddinStart + (viewWidth * positionOffset);

                int scroll = (int) -(targetX - tab.getX());

                tabsContainer.scrollBy(scroll, 0);
            } else {
                int firstItem = ((LinearLayoutManager) tabsContainer.getLayoutManager()).findFirstVisibleItemPosition();
                tab = tabsContainer.getLayoutManager().findViewByPosition(firstItem);
                int viewWidth = -tab.getWidth();
                float scroll = viewWidth * (1 - positionOffset);
                tabsContainer.scrollBy(Math.round(scroll), 0);
            }

            if (tabFormat == ONLYTEXT) {
                animateSelectionText(position, positionOffset);
            } else {
                setFloatingTabPosition(position, positionOffset);
                animateSelectionImg(position, positionOffset);
            }

            if (positionOffset == 0) {
                fancyTabAdapter.setSelected(position);
                int padding = tabsContainer.getWidth() - tab.getWidth() - getResources().getDimensionPixelOffset(R.dimen.fancy_padding_elem) - paddinStart;
                tabsContainer.setPadding(paddinStart, 0, padding, 0);
            } else if (positionOffset > 0.8) {
                tab = tabsContainer.getLayoutManager().findViewByPosition(position + 1);
                int padding = tabsContainer.getWidth() - tab.getWidth() - getResources().getDimensionPixelOffset(R.dimen.fancy_padding_elem) - paddinStart;
                tabsContainer.setPadding(paddinStart, 0, padding, 0);
            }

            canScroll = false;
        }

        private void setFloatingTabPosition(int position, float positionOffset) {

            Log.d("offset", "" + positionOffset);

            if (positionOffset <= 0.35 || positionOffset >= 0.65) {

                floattingTab.setVisibility(INVISIBLE);

                View currentPage = tabsContainer.getLayoutManager().findViewByPosition(position);

                if (positionOffset >= 0.65) {
                    currentPage = tabsContainer.getLayoutManager().findViewByPosition(position + 1);
                }


                ImageView floatingImg = (ImageView) floattingTab.findViewById(R.id.tab_image_circle);
                ImageView img = (ImageView) currentPage.findViewById(R.id.tab_image_circle);
                if (!circleImg) {
                    img = (ImageView) currentPage.findViewById(R.id.tab_image);
                    floatingImg = (ImageView) floattingTab.findViewById(R.id.tab_image);
                }

                floatingImg.setImageDrawable(img.getDrawable());
                ViewGroup.LayoutParams floatingImgLayoutParams = floatingImg.getLayoutParams();
                floatingImgLayoutParams.width = img.getWidth();
                floatingImgLayoutParams.height = img.getHeight();
                floatingImg.requestLayout();
                floatingImg.setColorFilter(Color.parseColor("#FF4081"));
                floatingImg.setAlpha(img.getAlpha());
                //img.setAlpha(0f);

                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) floattingTab.getLayoutParams();
                layoutParams.leftMargin = (int) currentPage.getX();
                layoutParams.topMargin = (int) (currentPage.getY() + tabsContainer.getY() + getY());

                floattingTab.setVisibility(VISIBLE);
            } else {
                floattingTab.setVisibility(GONE);
            }
        }

        private void animateSelectionText(int position, float positionOffset) {
            View tab = tabsContainer.getLayoutManager().findViewByPosition(position);
            if (tab != null) {
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

            if (position < tabsContainer.getAdapter().getItemCount() - 1) {
                View tab2 = tabsContainer.getLayoutManager().findViewByPosition(position + 1);
                TextView tabText2 = (TextView) tab2.findViewById(R.id.tab_text);

                float currTextSize = 16f;
                float targetSize = 24f;

                int startColor = ContextCompat.getColor(tabText2.getContext(), R.color.facyTabTextColor);
                int targetColor = ContextCompat.getColor(tabText2.getContext(), android.R.color.white);

                float size = currTextSize - ((currTextSize - targetSize) * positionOffset);

                ArgbEvaluator evaluator = new ArgbEvaluator();
                int color = (int) evaluator.evaluate(positionOffset, startColor, targetColor);

                tabText2.setTextSize(size);
                tabText2.setTextColor(color);
            }

        }

        private void animateSelectionImg(int position, float positionOffset) {
            View tab = tabsContainer.getLayoutManager().findViewByPosition(position);
            if (tab != null) {
                int img = R.id.tab_image;
                if (circleImg) {
                    img = R.id.tab_image_circle;
                }

                ImageView tabImage = (ImageView) tab.findViewById(img);

                int currSize = tabImage.getContext().getResources().getDimensionPixelSize(R.dimen.fancy_selected_img_size);
                float targetSize = tabImage.getContext().getResources().getDimensionPixelSize(R.dimen.fancy_deselected_img_size);

                float startOpacity = 1;
                float targetOpacity = 0.6f;

                int size = (int) (currSize - ((currSize - targetSize) * positionOffset));

                tabImage.getLayoutParams().width = size;
                tabImage.getLayoutParams().height = size;
                tabImage.requestLayout();

                float opacity = startOpacity - ((startOpacity - targetOpacity) * positionOffset);

                tabImage.setAlpha(opacity);
            }

            if (position < tabsContainer.getAdapter().getItemCount() - 1) {
                View tab2 = tabsContainer.getLayoutManager().findViewByPosition(position + 1);
                int img = R.id.tab_image;
                if (circleImg) {
                    img = R.id.tab_image_circle;
                }

                ImageView tabImage = (ImageView) tab2.findViewById(img);

                int currSize = tabImage.getContext().getResources().getDimensionPixelSize(R.dimen.fancy_deselected_img_size);
                float targetSize = tabImage.getContext().getResources().getDimensionPixelSize(R.dimen.fancy_selected_img_size);

                float startOpacity = 0.6f;
                float targetOpacity = 1f;

                int size = (int) (currSize - ((currSize - targetSize) * positionOffset));

                tabImage.getLayoutParams().width = size;
                tabImage.getLayoutParams().height = size;
                tabImage.requestLayout();

                float opacity = startOpacity - ((startOpacity - targetOpacity) * positionOffset);

                tabImage.setAlpha(opacity);
            }

            if (tabFormat == IMGTITLE) {
                if (positionOffset <= 0.3) {

                    float opacity = 1 - (positionOffset * 10);
                    float percent = 1 - (positionOffset * 3);

                    int currMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.fancy_selected_img_size) + getResources().getDimensionPixelOffset(R.dimen.fancy_tab_paddin_start);
                    int tagetMargin = currMargin - titleTV.getWidth();

                    int margin = (int) (currMargin - ((currMargin - tagetMargin) * (1 - percent)));

                    FrameLayout.LayoutParams layoutParams = (LayoutParams) titleTV.getLayoutParams();
                    layoutParams.leftMargin = margin;


                    titleTV.setAlpha(opacity);
                    titleTV.setText(viewPager.getAdapter().getPageTitle(position));
                    titleTV.requestLayout();
                } else if (positionOffset >= 0.7 && position < tabsContainer.getAdapter().getItemCount() - 1) {
                    float opacity = 1 - ((1 - positionOffset) * 10);
                    float percent = 1 - ((1 - positionOffset) * 3);

                    int currMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.fancy_selected_img_size) + getResources().getDimensionPixelOffset(R.dimen.fancy_tab_paddin_start);
                    int tagetMargin = currMargin + titleTV.getWidth();

                    int margin = (int) (currMargin - ((currMargin - tagetMargin) * (1 - percent)));

                    FrameLayout.LayoutParams layoutParams = (LayoutParams) titleTV.getLayoutParams();
                    layoutParams.leftMargin = margin;

                    titleTV.setAlpha(opacity);
                    titleTV.setText(viewPager.getAdapter().getPageTitle(position + 1));
                    layoutParams.leftMargin = margin;
                }
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            //Log.i("offset", "" + verticalOffset + ",  " + appBarLayout.getTotalScrollRange());
        }
    };

    public FancyTabLayout(@NonNull Context context) {
        super(context);
    }

    public FancyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
    }

    public FancyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FancyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setAttributes(context, attrs);
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FancyTabLayout);
        if (array != null) {
            tabFormat = array.getInt(R.styleable.FancyTabLayout_tabFormat, 1);
            circleImg = array.getBoolean(R.styleable.FancyTabLayout_circleImg, true);
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View root = inflate(getContext(), R.layout.fancy_tab_layout, this);
        tabsContainer = (RecyclerView) root.findViewById(R.id.tabs_container);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollHorizontally() {
                return canScroll;
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        tabsContainer.setLayoutManager(layoutManager);
        tabsContainer.setHasFixedSize(true);

        if (fancyTabAdapter != null){
            tabsContainer.setAdapter(fancyTabAdapter);

        }

        if (tabFormat == IMGTITLE) {
            titleTV = (TextView) root.findViewById(R.id.fancy_title);
            titleTV.setVisibility(View.VISIBLE);
        }
    }

    public void setupWithViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        fancyTabAdapter = new FancyTabAdapter(viewPager.getAdapter(), this, tabFormat, circleImg);
        if (imageLoader != null) {
            fancyTabAdapter.setImageLoader(imageLoader);
        }
        if (tabsContainer != null){
            tabsContainer.setAdapter(fancyTabAdapter);
        }
        this.viewPager.addOnPageChangeListener(pageChangeListener);
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        if (fancyTabAdapter != null) {
            fancyTabAdapter.setImageLoader(imageLoader);
        }
    }


    @Override
    public void onListItemClick(int position) {
        viewPager.setCurrentItem(position, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (tabFormat != ONLYTEXT && getParent().getClass().equals(AppBarLayout.class)) {
            setAppbarEvent();
        }

        if (tabFormat == ONLYTEXT) {
            View root = getChildAt(0);
            root.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            root.requestLayout();

            tabsContainer.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tabsContainer.requestLayout();
        }
    }

    private void setAppbarEvent() {
        AppBarLayout appBar = (AppBarLayout) getParent();
        appBar.addOnOffsetChangedListener(onOffsetChangedListener);

        if (appBar.getParent().getClass().equals(CoordinatorLayout.class)) {
            CoordinatorLayout rootLayout = (CoordinatorLayout) appBar.getParent();
            LayoutInflater inflater = LayoutInflater.from(rootLayout.getContext());
            floattingTab = (RelativeLayout) inflater.inflate(R.layout.fancy_floating_tab, rootLayout, false);
            rootLayout.addView(floattingTab);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (viewPager != null){
            viewPager.removeOnPageChangeListener(pageChangeListener);
        }

        if (tabFormat != ONLYTEXT && getParent().getClass().equals(AppBarLayout.class)) {
            AppBarLayout appBar = (AppBarLayout) getParent();
            appBar.removeOnOffsetChangedListener(onOffsetChangedListener);
        }
    }

    public interface ImageLoader {
        void loadImage(ImageView view, Object url);
    }
}
