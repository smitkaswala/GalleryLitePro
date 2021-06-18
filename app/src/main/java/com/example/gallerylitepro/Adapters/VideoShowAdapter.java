package com.example.gallerylitepro.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.example.gallerylitepro.Activitys.ImageActivity;
import com.example.gallerylitepro.Activitys.VideoPlayActivity;
import com.example.gallerylitepro.Activitys.VideoViewActivity;
import com.example.gallerylitepro.Classes.AlbumDetail;
import com.example.gallerylitepro.Classes.CustomViewPager;
import com.example.gallerylitepro.Classes.RotateTransformation;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.gallerylitepro.Activitys.EditingActivity.mOptionRL;
import static com.example.gallerylitepro.Activitys.EditingActivity.mRotateRL;

public class VideoShowAdapter extends PagerAdapter {

    Activity _activity;
    public static ArrayList<String> filesPath;
    ArrayList<String> objects = new ArrayList<>();

    AlbumDetail filteredList;

    GestureImageView mImage;
    ImageView play_button;
    CustomViewPager pager;
    ArrayList<String> mFavouriteImageList = new ArrayList<>();
    String json1;

    public VideoShowAdapter(Activity _activity, ArrayList<String> filesPath, CustomViewPager pager){
        this._activity = _activity;
        this.filesPath = filesPath;
        this.pager = pager;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.fullscreen_videos, container, false);
        String FileListModel = filesPath.get(position);
        AlbumDetail albumDetail = new AlbumDetail();


        mImage = (GestureImageView) viewLayout.findViewById(R.id.mImage);
        mImage.getController().getSettings().setMaxZoom(3f).setDoubleTapZoom(3f);
        mImage.getController().getSettings()
                .setPanEnabled(true)
                .setZoomEnabled(true)
                .setDoubleTapEnabled(true)
                .setRotationEnabled(true)
                .setRestrictRotation(true);

        Glide.with(_activity)
                .load(FileListModel)
                .override(900, 900)
                .into(mImage);

        play_button = viewLayout.findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, VideoPlayActivity.class);
                intent.putExtra("path",filesPath.get(position));
                intent.putExtra("size",filesPath.get(position));
                intent.putExtra("dateAdd",filesPath.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _activity.startActivity(intent);

            }
        });

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            mImage.getController().enableScrollInViewPager(pager);
        }

        ((ViewPager) container).addView(viewLayout);
        return viewLayout;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    @Override
    public int getCount() {
        try {
            if (filesPath != null && filesPath.size() > 0) {
                return filesPath.size();
            } else {
                return 0;
            }
        } catch (Exception w) {
            return 0;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    public void DeleteAction(int position,String from){
        if(from.equals("Album")) {
            File file = new File(filesPath.get(position));
            boolean isDelete = Utils.delete(_activity, file);
            if (!isDelete)
                isDelete = file.delete();
            if (isDelete) {
                filesPath.remove(position);
                if (position == filesPath.size() - 1) {
                    _activity.onBackPressed();
                } else {
                    notifyDataSetChanged();
                }

                if (filesPath.size() == 0)
                    _activity.onBackPressed();
                Toast.makeText(_activity, "Image Successfully deleted!!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(_activity, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
            }
        }else if(from.equals("Favs")){
            SharedPreferences sharedPreferences = _activity.getSharedPreferences("Favourites_pref",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            json1 = sharedPreferences.getString("Fav_Image", "");
            Type type1 = new TypeToken<ArrayList<String>>() {
            }.getType();
            mFavouriteImageList = gson.fromJson(json1, type1);

            if (mFavouriteImageList == null) {
                mFavouriteImageList = new ArrayList<>();
            }

            mFavouriteImageList.remove(position);
            filesPath.remove(position);
            _activity.onBackPressed();

            json1 = gson.toJson(mFavouriteImageList);
            editor.putString("Fav_Image", json1);
            editor.apply();

        }
    }

    public void RefreshDeleteData(int currentItem) {

        filesPath.remove(currentItem);
        notifyDataSetChanged();

    }

}
