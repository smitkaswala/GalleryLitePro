package com.example.gallerylitepro.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.gallerylitepro.Activitys.EditingActivity;
import com.example.gallerylitepro.Classes.AlbumDetail;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolader> {


    Activity activity;
    ArrayList<String> objects = new ArrayList<>();
    private ArrayList<String> mArrayList = new ArrayList<>();
    ArrayList<String> mFavouriteImageList = new ArrayList<>();
    String json1;
    Boolean exist = false;
    int pos;

    public ImageAdapter(Activity activity){
        this.activity = activity;

    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolader onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        ImageAdapter.ViewHolader view = null;

            itemView = LayoutInflater.from(activity).inflate(R.layout.image_view, parent, false);
            view = new ViewHolader(itemView);

        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolader holder, int position) {
        RequestOptions options = new RequestOptions();
        File file = new File(objects.get(position));

        SharedPreferences sharedPreferences = activity.getSharedPreferences("Favourites_pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        mFavouriteImageList = new ArrayList<>();
        json1 = sharedPreferences.getString("Fav_Image","");
        Type type1 = new TypeToken<ArrayList<String>>(){
        }.getType();
        mFavouriteImageList = gson.fromJson(json1,type1);

        if(mFavouriteImageList == null){
            mFavouriteImageList = new ArrayList<>();
        }

        ViewHolader hol = (ViewHolader) holder;
        if(mFavouriteImageList.size() > 0){
            for(int i = 0;i < mFavouriteImageList.size();i++){
                if(mFavouriteImageList.get(i).equals(file.getPath())){
                    holder.like_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like_done));
                }
            }
        }

        if (file.exists()) {
            holder.image_view.setAdjustViewBounds(true);
            Glide.with(activity)
                    .load(file.getPath())
                    .apply(options.centerCrop()
                            .skipMemoryCache(true)
                            .priority(Priority.LOW)
                            .format(DecodeFormat.PREFER_ARGB_8888))
                    .into(holder.image_view);

            holder.image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EditingActivity editingActivity = new EditingActivity();
                            editingActivity.SetList(objects, position);
                            Intent intent = new Intent(activity,EditingActivity.class);
                            intent.putExtra("From","Album");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }
                    });
                }
            });
            holder.like_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    json1 = sharedPreferences.getString("Fav_Image","");
                    Type type1 = new TypeToken<ArrayList<String>>(){
                    }.getType();
                    mFavouriteImageList = gson.fromJson(json1,type1);

                    if(mFavouriteImageList == null){
                        mFavouriteImageList = new ArrayList<>();
                    }

                    if(mFavouriteImageList.size() > 0){
                        for(int i = 0;i < mFavouriteImageList.size();i++){
                            if(mFavouriteImageList.get(i).equals(file.getPath())){
                                exist = true;
                                pos = i;
                                break;
                            }else{
                                exist = false;
                            }
                        }
                    }
                    if(!exist){
                        mFavouriteImageList.add(file.getPath());
                        holder.like_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like_done));
                    }else{
                        mFavouriteImageList.remove(pos);
                        holder.like_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like));
                        exist = false;
                    }

                    json1 = gson.toJson(mFavouriteImageList);
                    editor.putString("Fav_Image",json1);
                    editor.apply();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public class ViewHolader extends RecyclerView.ViewHolder {

        ImageView image_view,like_image;

        public ViewHolader(@NonNull View itemView) {
            super(itemView);

            image_view = itemView.findViewById(R.id.image_view);
            like_image = itemView.findViewById(R.id.like_image);

        }
    }

    public void Addall(ArrayList<String> list) {
        for (int i = 0; i <list.size() ; i++) {
            Log.d("TAG", "Addall:+++ "+list.get(i));
        }
        mArrayList = new ArrayList<>();
        objects = list;
        mArrayList.addAll(list);
        notifyDataSetChanged();
    }
}
