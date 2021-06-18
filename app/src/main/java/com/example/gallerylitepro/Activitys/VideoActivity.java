package com.example.gallerylitepro.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gallerylitepro.Adapters.ImageAdapter;
import com.example.gallerylitepro.Adapters.VideoAdapter;
import com.example.gallerylitepro.Classes.AlbumDetail;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Utils.Utils;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView ic_back;
    TextView folder_name;
    VideoAdapter imageAdapter;

    public static ArrayList<String> pathlist;
    public static String FolderPath;
    public static String Bucket_Id;
    public static String Title;
    public String Duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        imageAdapter = new VideoAdapter(VideoActivity.this);

        recyclerView =findViewById(R.id.image_recycler);
        ic_back = findViewById(R.id.ic_back);
        folder_name = findViewById(R.id.folder_name);

        initView();

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void SaveList(AlbumDetail albumDetail) {
        pathlist = new ArrayList<>();
        pathlist = albumDetail.getPathList();
        Log.d("TAG", "SaveList:*** " + albumDetail.pathList);
        FolderPath = albumDetail.getBucketPath();
        Bucket_Id = albumDetail.getBucket_id();
        Title = albumDetail.getBucketName();
        Duration = albumDetail.getDuration();
    }



    private void initView(){

        folder_name.setText(Title);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), Utils.COLUMN));
        recyclerView.setLayoutAnimation(null);

        recyclerView.setAdapter(imageAdapter);

        if (pathlist != null && pathlist.size() > 0) {
            imageAdapter.addAll(pathlist);
            imageAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.IsUpdateVideos){
            initView();
        }
    }

}