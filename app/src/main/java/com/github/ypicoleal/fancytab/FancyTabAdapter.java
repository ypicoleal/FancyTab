package com.github.ypicoleal.fancytab;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


class FancyTabAdapter extends RecyclerView.Adapter<FancyTabAdapter.FancyTabViewHolder>{

    private PagerAdapter pagerAdapter;
    private ListItemClickListener mOnClickListener;

    FancyTabAdapter(PagerAdapter viewPager, ListItemClickListener mOnClickListener){
        this.pagerAdapter = viewPager;
        this.mOnClickListener = mOnClickListener;
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

        holder.tabImage.setVisibility(View.GONE);
        holder.tabText.setText(pageTitle);
    }

    @Override
    public int getItemCount() {
        return pagerAdapter.getCount();
    }

    class FancyTabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView tabImage;
        TextView tabText;

        FancyTabViewHolder(View itemView) {
            super(itemView);
            tabImage = (ImageView) itemView.findViewById(R.id.tab_image);
            tabText = (TextView) itemView.findViewById(R.id.tab_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }

    interface ListItemClickListener{
        void  onListItemClick(int position);
    }
}
