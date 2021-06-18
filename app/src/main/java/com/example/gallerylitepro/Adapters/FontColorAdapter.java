package com.example.gallerylitepro.Adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallerylitepro.Interface.ColorInterface;
import com.example.gallerylitepro.R;

public class FontColorAdapter extends RecyclerView.Adapter<FontColorAdapter.ViewHolder> {

    Activity context;
    int[] data;
    Typeface myTypeface;
    ColorInterface colorInterface;

    public FontColorAdapter(Activity context,int[] data,ColorInterface colorInterface) {
        this.context = context;
        this.data = data;
        this.colorInterface=colorInterface;

    }

    @Override
    public FontColorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_view, null);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FontColorAdapter.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mCardColor.setCardBackgroundColor(data[position]);
        viewHolder.mCardColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorInterface.ColorCode(data[position]);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardColor;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardColor =  itemView.findViewById(R.id.mCardColor);
        }
    }
}
