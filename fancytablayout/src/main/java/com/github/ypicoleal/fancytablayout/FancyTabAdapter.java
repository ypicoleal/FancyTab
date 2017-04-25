package com.github.ypicoleal.fancytablayout;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


class FancyTabAdapter extends RecyclerView.Adapter<FancyTabAdapter.FancyTabViewHolder>{

    private float opacity = 0.6f;
    private int selectedImgSize = -1;
    private PagerAdapter pagerAdapter;
    private ListItemClickListener mOnClickListener;
    private int tabFormat;
    private boolean circleImg;
    private FancyTabLayout.ImageLoader imageLoader;
    private int selected = 0;

    FancyTabAdapter(PagerAdapter viewPager, ListItemClickListener mOnClickListener, int tabFormat, boolean circleImg) {
        this.pagerAdapter = viewPager;
        this.mOnClickListener = mOnClickListener;
        this.tabFormat = tabFormat;
        this.circleImg = circleImg;
    }

    void setImageLoader(FancyTabLayout.ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        notifyDataSetChanged();
    }

    void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    void setOpacity(float opacity) {
        this.opacity = opacity;
        notifyDataSetChanged();
    }

    void setSelectedImgSize(int selectedImgSize) {
        this.selectedImgSize = selectedImgSize;
        notifyDataSetChanged();
    }

    @Override
    public FancyTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fancy_tab, parent, false);
        return new FancyTabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FancyTabViewHolder holder, int position) {
        CharSequence pageTitle = pagerAdapter.getPageTitle(position);


        switch (tabFormat) {
            case FancyTabLayout.ONLYTEXT:
                holder.tabImage.setVisibility(View.GONE);
                holder.tabImageCircle.setVisibility(View.GONE);
                holder.tabText.setVisibility(View.VISIBLE);
                break;
            default:
                holder.tabText.setVisibility(View.GONE);
                try {
                    Object url = ((FancyFragmentPageAdapter) pagerAdapter).getPageImageURL(position);
                    int imgSize = holder.tabImage.getContext().getResources().getDimensionPixelSize(R.dimen.fancy_deselected_img_size);
                    float opacity = this.opacity;
                    if (selected == position) {
                        if (imgSize != -1) {
                            imgSize = this.selectedImgSize;
                        } else {
                            imgSize = holder.tabImage.getContext().getResources().getDimensionPixelSize(R.dimen.fancy_selected_img_size);
                        }
                        opacity = 1;
                    }
                    if (circleImg) {
                        holder.tabImage.setVisibility(View.GONE);
                        holder.tabImageCircle.setVisibility(View.VISIBLE);

                        holder.tabImageCircle.getLayoutParams().width = imgSize;
                        holder.tabImageCircle.getLayoutParams().height = imgSize;
                        holder.tabImageCircle.requestLayout();
                        holder.tabImageCircle.setAlpha(opacity);

                        imageLoader.loadImage(holder.tabImageCircle, url);
                    } else {
                        holder.tabImage.setVisibility(View.VISIBLE);
                        holder.tabImageCircle.setVisibility(View.GONE);

                        holder.tabImage.getLayoutParams().width = imgSize;
                        holder.tabImage.getLayoutParams().height = imgSize;
                        holder.tabImage.requestLayout();
                        holder.tabImage.setAlpha(opacity);

                        imageLoader.loadImage(holder.tabImage, url);
                    }

                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                break;
        }

        holder.tabText.setText(pageTitle);


        if (selected == position) {
            holder.tabText.setTextSize(24);
            holder.tabText.setTextColor(ContextCompat.getColor(holder.tabText.getContext(), android.R.color.white));
        } else {
            holder.tabText.setTextSize(16);
            holder.tabText.setTextColor(ContextCompat.getColor(holder.tabText.getContext(), R.color.facyTabTextColor));
        }
    }

    @Override
    public int getItemCount() {
        return pagerAdapter.getCount();
    }

    interface ListItemClickListener {
        void onListItemClick(int position);
    }

    class FancyTabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView tabImage;
        TextView tabText;
        CircleImageView tabImageCircle;

        FancyTabViewHolder(View itemView) {
            super(itemView);
            tabImage = (ImageView) itemView.findViewById(R.id.tab_image);
            tabImageCircle = (CircleImageView) itemView.findViewById(R.id.tab_image_circle);
            tabText = (TextView) itemView.findViewById(R.id.tab_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
