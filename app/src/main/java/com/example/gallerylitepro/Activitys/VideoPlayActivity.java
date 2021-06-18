package com.example.gallerylitepro.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gallerylitepro.Classes.AlbumDetail;
import com.example.gallerylitepro.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.File;
import java.text.CharacterIterator;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;

public class VideoPlayActivity extends AppCompatActivity {

    private static final String TAG ="activitylifecycle";
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;

    private TextView tvVideoTitle;

    private String path;
    private String size;
    private String dateAdded;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenView();
        setContentView(R.layout.activity_video_play2);

        playerView = findViewById(R.id.exo_player_view);

        tvVideoTitle = findViewById(R.id.video_title);

        path = getIntent().getStringExtra("path");
        size = getIntent().getStringExtra("size");
//        dateAdded = getIntent().getStringExtra("dateAdd");


        tvVideoTitle.setText(new File(path).getName());


    }

    private void fullScreenView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
        initializePlayer();
    }

    private void initializePlayer() {

        simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(simpleExoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(String.valueOf(path));
        simpleExoPlayer.setMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_IDLE || state == Player.STATE_ENDED){
                    playerView.setKeepScreenOn(false);
                }else {
                    playerView.setKeepScreenOn(true);
                }

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: " );
        releasePlayer();
    }

    public void onVideoInfoClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.video_details_dialog, null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        ImageView imageView = view.findViewById(R.id.btn_details_ok);
        TextView tvImageSize = view.findViewById(R.id.tv_image_size);
        TextView tvImagePath = view.findViewById(R.id.tv_image_path);
        TextView tvImageName = view.findViewById(R.id.tv_image_name);

        tvImageSize.setText(humanReadableByteCountSI(new File(path).length()));
        tvImagePath.setText(path);
        tvImageName.setText(new File(path).getName());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();



    }

    public void onExit(View view){
        finish();
    }
    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public String convertTimeDateModified(long time) {
        Date date = new Date(time * 1000);
        @SuppressLint("SimpleDateFormat") Format format = new SimpleDateFormat("dd.MM.yyyy , HH:mm:aa");
        return format.format(date);
    }


}