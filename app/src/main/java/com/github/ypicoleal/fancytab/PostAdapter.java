package com.github.ypicoleal.fancytab;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;


class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private JSONArray posts;

    PostAdapter() {
    }

    void setPosts(JSONArray posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        try {
            JSONObject post = posts.getJSONObject(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.caption.setText(Html.fromHtml(post.getString("caption"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.caption.setText(Html.fromHtml(post.getString("caption")));
            }
            String notes = NumberFormat.getNumberInstance(Locale.US).format(post.getInt("note_count"));
            holder.notes.setText(holder.notes.getResources().getString(R.string.note_count, notes));

            JSONArray tags = post.getJSONArray("tags");
            holder.tags.setText("");
            for (int i = 0; i < tags.length(); i++) {
                String tag = tags.getString(i);
                if (i > 0) {
                    holder.tags.append(" ");
                }
                holder.tags.append("#" + tag);
            }
            JSONArray photos = post.getJSONArray("photos");

            for (int i = 0; i < photos.length(); i++) {
                JSONObject photoJson = photos.getJSONObject(i);
                ImageView photo;
                String photoUrl = photoJson.getJSONObject("original_size").getString("url");
                if (i < holder.photos.getChildCount()) {
                    photo = (ImageView) holder.photos.getChildAt(i);
                } else {
                    LayoutInflater inflater = LayoutInflater.from(holder.photos.getContext());
                    photo = (ImageView) inflater.inflate(R.layout.post_photo, holder.photos, false);
                    holder.photos.addView(photo);
                }
                Picasso.with(holder.photos.getContext())
                        .load(photoUrl)
                        .placeholder(R.drawable.gradient)
                        .into(photo);
            }

            while (photos.length() < holder.photos.getChildCount()) {
                holder.photos.removeViewAt(photos.length());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        int count = 0;
        try {
            count = posts.length();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return count;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        LinearLayout photos;
        TextView caption;
        TextView tags;
        TextView notes;

        PostViewHolder(View itemView) {
            super(itemView);

            photos = (LinearLayout) itemView.findViewById(R.id.photos);
            caption = (TextView) itemView.findViewById(R.id.caption);
            tags = (TextView) itemView.findViewById(R.id.tags);
            notes = (TextView) itemView.findViewById(R.id.notes);
        }
    }
}
