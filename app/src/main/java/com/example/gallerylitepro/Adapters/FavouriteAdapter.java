package com.example.gallerylitepro.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.gallerylitepro.Activitys.EditingActivity;
import com.example.gallerylitepro.Activitys.ImageActivity;
import com.example.gallerylitepro.Activitys.VideoViewActivity;
import com.example.gallerylitepro.R;
import com.google.android.material.shape.CornerFamily;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
    ArrayList<String> objects = new ArrayList<>();
    private ArrayList<String> mArrayList = new ArrayList<>();
    ArrayList<String> mFavouriteImageList = new ArrayList<>();
    String json1;
    Boolean exist = false;
    int pos;

    public FavouriteAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 0) {
            itemView = LayoutInflater.from(activity).inflate(R.layout.image_view, parent, false);
            viewHolder = new ViewHolder(itemView);
        } else{
            itemView = LayoutInflater.from(activity).inflate(R.layout.video_view, parent, false);
            viewHolder = new ViewHolder(itemView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holders, int position) {
        File file = new File(objects.get(position));
        RequestOptions options = new RequestOptions();

        SharedPreferences sharedPreferences = activity.getSharedPreferences("Favourites_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        ViewHolder holder = (ViewHolder) holders;

        if (holder.getItemViewType() == 0){
            holder.like_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like_done));
            if (file.exists()) {
                holder.image_view.setAdjustViewBounds(true);
                Glide.with(activity)
                        .load(file.getPath())
                        .apply(options.centerCrop()
                                .skipMemoryCache(true)
                                .priority(Priority.LOW)
                                .format(DecodeFormat.PREFER_ARGB_8888))
                        .transition(withCrossFade())
                        .transition(new DrawableTransitionOptions().crossFade(500))
                        .into(holder.image_view);
            }
            holder.image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditingActivity slideShowActivity = new EditingActivity();
                    slideShowActivity.SetList(objects, position);
                    Intent in = new Intent(activity, EditingActivity.class);
                    in.putExtra("From", "Favs");
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(in);
                }
            });
            holder.like_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    json1 = sharedPreferences.getString("Fav_Image", "");
                    Type type1 = new TypeToken<ArrayList<String>>() {
                    }.getType();
                    mFavouriteImageList = gson.fromJson(json1, type1);

                    if (mFavouriteImageList == null) {
                        mFavouriteImageList = new ArrayList<>();
                    }

                    if (mFavouriteImageList.size() > 0) {
                        for (int i = 0; i < mFavouriteImageList.size(); i++) {
                            if (mFavouriteImageList.get(i).equals(file.getPath())) {
                                exist = true;
                                pos = i;
                                break;
                            } else {
                                exist = false;
                            }
                        }
                    }
                    if (!exist) {
                        mFavouriteImageList.add(file.getPath());
                        holder.like_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like_done));
                    } else {
                        mFavouriteImageList.remove(pos);
                        objects.remove(pos);
                        holder.like_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like));
                        exist = false;
                    }

                    json1 = gson.toJson(mFavouriteImageList);
                    editor.putString("Fav_Image", json1);
                    editor.apply();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });

                }
            });
        } else {
            holder.like_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like_done));
            if (file.exists()) {
                holder.image_view.setAdjustViewBounds(true);
                Glide.with(activity)
                        .load(file.getPath())
                        .apply(options.centerCrop()
                                .skipMemoryCache(true)
                                .priority(Priority.LOW)
                                .format(DecodeFormat.PREFER_ARGB_8888))
                        .transition(withCrossFade())
                        .transition(new DrawableTransitionOptions().crossFade(500))
                        .into(holder.image_view);
            }
            holder.image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditingActivity slideShowActivity = new EditingActivity();
                    slideShowActivity.SetList(objects, position);
                    Intent in = new Intent(activity, EditingActivity.class);
                    in.putExtra("From", "Favs");
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(in);
                }
            });
            holder.like_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    json1 = sharedPreferences.getString("Fav_Image", "");
                    Type type1 = new TypeToken<ArrayList<String>>() {
                    }.getType();
                    mFavouriteImageList = gson.fromJson(json1, type1);

                    if (mFavouriteImageList == null) {
                        mFavouriteImageList = new ArrayList<>();
                    }

                    if (mFavouriteImageList.size() > 0) {
                        for (int i = 0; i < mFavouriteImageList.size(); i++) {
                            if (mFavouriteImageList.get(i).equals(file.getPath())) {
                                exist = true;
                                pos = i;
                                break;
                            } else {
                                exist = false;
                            }
                        }
                    }
                    if (!exist) {
                        mFavouriteImageList.add(file.getPath());
                        holder.like_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like_done));
                    } else {
                        mFavouriteImageList.remove(pos);
                        objects.remove(pos);
                        holder.like_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like));
                        exist = false;
                    }

                    json1 = gson.toJson(mFavouriteImageList);
                    editor.putString("Fav_Image", json1);
                    editor.apply();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (objects.get(position).contains(".mp4")){
            return 1;
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view, like_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view = itemView.findViewById(R.id.image_view);
            like_image = itemView.findViewById(R.id.like_image);

        }
    }

    public void addAll(ArrayList<String> list) {

        mArrayList = new ArrayList<>();
        objects = list;
        mArrayList.addAll(list);
        notifyDataSetChanged();
    }

}
