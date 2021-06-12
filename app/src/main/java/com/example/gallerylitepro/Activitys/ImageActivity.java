package com.example.gallerylitepro.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.gallerylitepro.Adapters.ImageAdapter;
import com.example.gallerylitepro.Adapters.ImageFolderAdapter;
import com.example.gallerylitepro.Classes.AlbumDetail;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Utils.Utils;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView ic_back;
    TextView folder_name;
    ImageAdapter imageAdapter;
    public static ArrayList<String> pathlist;
    public static String FolderPath;
    public static String Bucket_Id;
    public static String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageAdapter = new ImageAdapter(ImageActivity.this);

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
    }
    private void initView(){
        folder_name.setText(Title);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), Utils.COLUMN));
        recyclerView.setLayoutAnimation(null);

        recyclerView.setAdapter(imageAdapter);

        if (pathlist != null && pathlist.size() > 0) {
            imageAdapter.Addall(pathlist);
            imageAdapter.notifyDataSetChanged();
        }

    }
}