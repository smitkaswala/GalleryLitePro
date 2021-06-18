package com.example.gallerylitepro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.example.gallerylitepro.R;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private final List<ThumbnailItem> thumbnailItems;
    private final ThumbnailsAdapterListener listener;
    private final Context mContext;
    private int selectedIndex = 0;

    public FilterAdapter(List<ThumbnailItem> thumbnailItems, Context mcontext ,ThumbnailsAdapterListener listener) {
        this.mContext = mcontext;
        this.thumbnailItems = thumbnailItems;
        this.listener = listener;
    }

    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_items, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {

        final ThumbnailItem thumbnailItem = thumbnailItems.get(position);

        Glide.with(mContext)
                .load(thumbnailItem.image)
                .into( holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFilterSelected(thumbnailItem.filter);
                selectedIndex = position;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return thumbnailItems.size();
    }

    public interface ThumbnailsAdapterListener {
        void onFilterSelected(Filter filter);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        GestureImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.imgFilterView);
        }
    }
}
