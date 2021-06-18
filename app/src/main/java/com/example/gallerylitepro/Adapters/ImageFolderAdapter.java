package com.example.gallerylitepro.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.gallerylitepro.Activitys.ImageActivity;
import com.example.gallerylitepro.Classes.AlbumDetail;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Utils.Utils;
import com.google.android.material.shape.CornerFamily;

import java.io.File;
import java.util.ArrayList;

public class ImageFolderAdapter extends RecyclerView.Adapter<ImageFolderAdapter.ViewHolder> implements Filterable {

    ArrayList<AlbumDetail> objects = new ArrayList<>();
    Activity activity;
    ArrayList<AlbumDetail> filteredList = new ArrayList<>();

    public ImageFolderAdapter(Activity activity){
        this.activity = activity;
    }

    public ImageFolderAdapter() {
    }

    @NonNull
    @Override
    public ImageFolderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        ImageFolderAdapter.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0: {
                if (Utils.VIEW_TYPE.equals("Grid")) {
                    itemView = LayoutInflater.from(activity).inflate(R.layout.grid_image_folder, parent, false);
                    viewHolder = new ViewHolder(itemView);

//                    ViewGroup.LayoutParams params = itemView.getLayoutParams();
//                    if (params != null) {
//                        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//                        int width = wm.getDefaultDisplay().getWidth();
//                        params.height = width / Utils.COLUMN;
//                    }

                }
            }
            break;
        }

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ImageFolderAdapter.ViewHolder holder, int position) {
        RequestOptions options = new RequestOptions();

        ViewHolder holders = (ViewHolder)holder;
        AlbumDetail item = filteredList.get(position);
        holders.Grid.setClipToOutline(true);
        holders.Grid.setAdjustViewBounds(true);


        if (item.getPathList().size() > 0){
            Uri uri = Uri.fromFile(new File(item.getPathList().get(0)));
            try{
                Glide.with(activity)
                        .load(uri)
                        .apply(options.centerCrop()
                                .skipMemoryCache(true)
                                .priority(Priority.LOW)
                                .format(DecodeFormat.PREFER_ARGB_8888))
                        .into(holders.Grid);
            }catch (Exception e){
                Glide.with(activity)
                        .load(uri)
                        .apply(options.centerCrop()
                                .skipMemoryCache(true)
                                .priority(Priority.LOW))
                        .into(holder.Grid);
            }
            holder.folder_text.setText(item.getBucketName());
            holder.count_text.setText(item.getPathList().size()+ "");
            holder.folder_text.setSelected(true);

            holder.Grid.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    ImageActivity imageActivity = new ImageActivity();
                    imageActivity.SaveList(item);
                    Intent intent = new Intent(activity,ImageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int val = 0;
        try {
            if (filteredList != null && filteredList.size() > 0) {
                val = filteredList.get(position).getType();
            } else {
                val = 0;
            }
        } catch (Exception w) {
        }
        return val;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Grid
        ImageView Grid;
        TextView folder_text,count_text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //grid
            Grid = itemView.findViewById(R.id.grid);
            folder_text = itemView.findViewById(R.id.name_text);
            count_text = itemView.findViewById(R.id.count_text);

        }
    }

    public void Addall(ArrayList<AlbumDetail> itemdata) {

        objects = new ArrayList<>();
        objects.addAll(itemdata);
        filteredList = new ArrayList<>();
        filteredList.addAll(itemdata);
        notifyDataSetChanged();
        for (int i = 0; i <itemdata.size() ; i++) {
            Log.d("TAG", "Addall:*** " + itemdata.get(i).bucketName);
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()){
                    filteredList = objects;
                }else {
                    ArrayList<AlbumDetail> filteredList1 = new ArrayList<>();
                    for (AlbumDetail row : objects){
                        if (row.getBucketName().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList1.add(row);
                        }
                    }
                    filteredList = filteredList1;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<AlbumDetail>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
