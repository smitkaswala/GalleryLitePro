package com.example.gallerylitepro.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.gallerylitepro.Adapters.ViewPagerAdapter;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Utils.Constant;
import static com.example.gallerylitepro.Fregment.GalleryFragment.imageFolderAdapter;
import static com.example.gallerylitepro.Fregment.VideoFragment.videoFolderAdapter;

public class GalleryActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TextView tvTitle;
    RelativeLayout photos,video,like;
    LinearLayout photo_linear,video_linear,like_linear;
    TextView photo_text,video_text,favourite_text;
    EditText mSearch;
    RelativeLayout search_layout;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       runtimePermission();

    }

    private void startApp(){

        viewPager =findViewById(R.id.view_pager);
        photos =findViewById(R.id.photos);
        video =findViewById(R.id.video);
        like =findViewById(R.id.like);
        photo_linear =findViewById(R.id.photo_linear);
        video_linear =findViewById(R.id.video_linear);
        like_linear =findViewById(R.id.like_linear);
        photo_text = findViewById(R.id.photo_text);
        video_text = findViewById(R.id.video_text);
        favourite_text = findViewById(R.id.favourite_text);
        tvTitle = findViewById(R.id.tvTitle);
        mSearch = findViewById(R.id.mSearch);
        search_layout = findViewById(R.id.search_layout);
        mSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (viewPager.getCurrentItem()==0){
                    imageFolderAdapter.getFilter().filter(s);
                }
                if (viewPager.getCurrentItem()==1){
                    videoFolderAdapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setUpViewPager();

        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

    }

    private void runtimePermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else {
            //permission granted
            startApp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //permission granted
            startApp();
        } else {
            ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    private void setUpViewPager(){

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setCurrentItem(Constant.pagerPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(viewPagerAdapter.getPageTitle(position));
                switch (position){
                    case 0:
                        photo_linear.getBackground().setColorFilter(Color.parseColor("#8B92F9"), PorterDuff.Mode.SRC_ATOP);
                        video_linear.getBackground().setColorFilter(Color.parseColor("#FFFFFF"),PorterDuff.Mode.SRC_ATOP);
                        like_linear.getBackground().setColorFilter(Color.parseColor("#FFFFFF"),PorterDuff.Mode.SRC_ATOP);
                        photo_text.setTextColor(Color.WHITE);
                        video_text.setTextColor(Color.BLACK);
                        favourite_text.setTextColor(Color.BLACK);
                        search_layout.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        video_linear.getBackground().setColorFilter(Color.parseColor("#F8CF78"),PorterDuff.Mode.SRC_ATOP);
                        like_linear.getBackground().setColorFilter(Color.parseColor("#FFFFFF"),PorterDuff.Mode.SRC_ATOP);
                        photo_linear.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                        photo_text.setTextColor(Color.BLACK);
                        video_text.setTextColor(Color.WHITE);
                        favourite_text.setTextColor(Color.BLACK);
                        search_layout.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        like_linear.getBackground().setColorFilter(Color.parseColor("#EE716A"),PorterDuff.Mode.SRC_ATOP);
                        photo_linear.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                        video_linear.getBackground().setColorFilter(Color.parseColor("#FFFFFF"),PorterDuff.Mode.SRC_ATOP);
                        photo_text.setTextColor(Color.BLACK);
                        video_text.setTextColor(Color.BLACK);
                        favourite_text.setTextColor(Color.WHITE);
                        search_layout.setVisibility(View.GONE);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

    }

}