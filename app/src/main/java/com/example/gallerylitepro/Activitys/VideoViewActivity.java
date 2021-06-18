package com.example.gallerylitepro.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gallerylitepro.Adapters.VideoShowAdapter;
import com.example.gallerylitepro.Classes.AlbumDetail;
import com.example.gallerylitepro.Classes.CustomViewPager;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoViewActivity extends AppCompatActivity {

    CustomViewPager mImagePager;
    LinearLayout first_linear,second_linear;
    ImageView ic_back,mDelete,favourite_image,mShare;
    public static ArrayList<String> list;
    public static int currenpositionToDisplay = 0;
    String json1;
    TextView image_name;
    VideoShowAdapter videoShowAdapter;
    Dialog dial, deleteDialog;
    TextView mDeleteFile;
    TextView mDialogTitle;
    CardView mDialogCancel, mDialogDelete;
    Boolean exist = false;
    int pos;
    String From = "";
    ArrayList<String> mFavouriteImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        startApp();
        From = getIntent().getStringExtra("From");
        init();

    }


    private void startApp(){

        mImagePager = findViewById(R.id.mImagePager);
        first_linear = findViewById(R.id.first_linear);
        second_linear = findViewById(R.id.second_linear);
        image_name = findViewById(R.id.image_name);

        ic_back = findViewById(R.id.ic_back);
        mDelete = findViewById(R.id.mDelete);
        favourite_image = findViewById(R.id.favourite_image);
        mShare = findViewById(R.id.mShare);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog = new Dialog(VideoViewActivity.this, android.R.style.Theme_DeviceDefault);
                deleteDialog.requestWindowFeature(1);
                deleteDialog.setContentView(R.layout.delete_dialog);
                mDeleteFile = (TextView) deleteDialog.findViewById(R.id.txt_filename);
                mDialogCancel = (CardView) deleteDialog.findViewById(R.id.mCancel);
                mDialogDelete = (CardView) deleteDialog.findViewById(R.id.mDelete);
                deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                deleteDialog.setCanceledOnTouchOutside(true);
                Utils.IsUpdateVideos = true;
                int position = mImagePager.getCurrentItem();
                deleteDialog.show();
                File file = new File(list.get(position));
                mDeleteFile.setText(file.getName());
                mDialogDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                        videoShowAdapter.DeleteAction(position, From);
                    }
                });
                mDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                    }
                });
            }
        });

        favourite_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAsFavourite();
            }
        });

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path;
                File file;
                Uri uri;

                path = list.get(mImagePager.getCurrentItem());
                file = new File(path);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(VideoViewActivity.this, getPackageName() + ".provider", file);
                } else {
                    uri = Uri.fromFile(file);
                }

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sharingIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                sharingIntent.setType("video/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Share Via"));

            }
        });
    }

    public void init() {

        SharedPreferences sharedPreferences = getSharedPreferences("Favourites_pref",MODE_PRIVATE);
        Gson gson = new Gson();

        mFavouriteImageList = new ArrayList<>();
        json1 = sharedPreferences.getString("Fav_Image", "");
        Type type1 = new TypeToken<ArrayList<String>>() {
        }.getType();
        mFavouriteImageList = gson.fromJson(json1, type1);

        if (mFavouriteImageList == null) {
            mFavouriteImageList = new ArrayList<>();
        }

        System.gc();
        videoShowAdapter = new VideoShowAdapter(this, list, mImagePager);
        mImagePager.setAdapter(videoShowAdapter);
        mImagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (list != null) {
                    File file = new File(list.get(position));
                    image_name.setText((position + 1) + " / " + (list.size()));
                    if (mFavouriteImageList.size() > 0) {
                        if (mFavouriteImageList.contains(file.getPath())) {
                            favourite_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_done));
                        } else {
                            favourite_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        File file = new File(list.get(currenpositionToDisplay));
        image_name.setText((currenpositionToDisplay + 1) + " / " + (list.size()));
        if (mFavouriteImageList.size() > 0) {
            if (mFavouriteImageList.contains(file.getPath())) {
                favourite_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_done));
            } else {
                favourite_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
            }
        }

        mImagePager.setCurrentItem(currenpositionToDisplay);

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(Utils.IsUpdateVideos){
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    if (videoShowAdapter != null) {
                        videoShowAdapter.notifyDataSetChanged();
                    }
                    registerReceiver(DeleteCompletedBroadcast, new IntentFilter("DeleteCompleted"));
                }
            });
        }else {
            Utils.IsUpdateVideos = true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (DeleteCompletedBroadcast != null) {
                unregisterReceiver(DeleteCompletedBroadcast);
            }
        } catch (Exception e) {
        }
    }

    public void SetAsFavourite() {
        File file = new File(list.get(mImagePager.getCurrentItem()));
        SharedPreferences sharedPreferences = getSharedPreferences("Favourites_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
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
            favourite_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_done));
        } else {
            mFavouriteImageList.remove(pos);
            favourite_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
            exist = false;
        }

        Utils.IsUpdate = true;
        json1 = gson.toJson(mFavouriteImageList);
        editor.putString("Fav_Image", json1);
        editor.apply();
    }

    private BroadcastReceiver DeleteCompletedBroadcast = new BroadcastReceiver() {

        public ProgressDialog pr_dialog;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null) {
                if (intent != null) {
                    if (intent.getBooleanExtra("started", false)) {
                        try {
                            pr_dialog = ProgressDialog.show(context, "Deleting",
                                    "Deleting files...",
                                    true, false);
                        } catch (Exception e) {
                        }
                    }

                    if (intent.getBooleanExtra("completed", false)) {
                        try {
                            //-----------------data refresh-----
                            if (videoShowAdapter != null) {
                                videoShowAdapter.RefreshDeleteData(mImagePager.getCurrentItem());
                            }
                            //----------------------------
                        } catch (Exception e) {
                        }
                        if (pr_dialog != null)
                            pr_dialog.dismiss();
                    }
                }
            }
        }
    };

    public void SetList(ArrayList<String> pathlist, int position) {
        try {
            list = pathlist;
            currenpositionToDisplay = position;
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
    }

}