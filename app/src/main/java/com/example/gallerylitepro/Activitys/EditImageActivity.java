package com.example.gallerylitepro.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.gallerylitepro.Adapters.FontColorAdapter;
import com.example.gallerylitepro.Adapters.FontStyleAdapter;
import com.example.gallerylitepro.Classes.CallStickerView;
import com.example.gallerylitepro.Classes.CustomViewPager;
import com.example.gallerylitepro.Classes.RotateTransformation;
import com.example.gallerylitepro.Fregment.FilterFragment;
import com.example.gallerylitepro.Interface.ColorInterface;
import com.example.gallerylitepro.Interface.FontStyleInterface;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditImageActivity extends AppCompatActivity implements View.OnClickListener, FilterFragment.FilterFragmentListener {

    LinearLayout mTopLayer,mBottomLayer,mTextRL,mAddText,mTextStyle,mTextColor,sticker_linear;
    ImageView ic_back,mClose,mDone,mCrop,mFilter,mdoodle,mText,mFrame;
    ImageView mEditCrop,mEditRotate,mEditVertical,mEditHorizontal;
    ImageView image1,image2,image3,image4,image5,image6,image7,image8,image9,image10,
            image11,image12,image13,image14,image15,image16,image17,image18,image19,image20,
            image21,image22,image23,image24,image25,image26,image27,image28,image29,image30,
            image31,image32,image33,image34,image35,image36,image37,image38,image39;
    RelativeLayout mMainRL,mEditLayer,mFilterRL,mCropRL,mDoodleRL;
    FrameLayout mMainframe;
    FontColorAdapter mAdapter;
    FontStyleAdapter mFontAdapter;
    GestureImageView mImage;
    EditText mAddedText;
    Bitmap mFilterBitmap;
    TextView mCancel, mOk;
    TextView mDialogTitle;
    FilterFragment filtersListFragment;
    protected StickerView stickerView;
    RecyclerView mStyleRec;
    String mStyleList[];
    int[] colors;
    String newText="";
    CustomViewPager mFilterPager;
    FontStyleInterface fontStyleInterface;
    CallStickerView callStickerView;
    ColorInterface colorInterface;
    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        init();

        InrializeFontArrayList("textfonts");
        IntializeColorArrayList();

        fontStyleInterface = new FontStyleInterface() {
            @Override
            public void Font(Typeface typeface) {
                Sticker sticker = callStickerView.GetStickerView();
                if (sticker != null) {
                    ((TextSticker) sticker).setTypeface(typeface);
                    callStickerView.UpdateStickerDetail(sticker);
                } else {
                    Toast.makeText(getApplicationContext(), "Click on text to apply font", Toast.LENGTH_LONG).show();
                }
            }
        };

        colorInterface = new ColorInterface() {
            @Override
            public void ColorCode(int color) {

                Sticker sticker = callStickerView.GetStickerView();
                if (sticker != null) {
                    ((TextSticker) sticker).setTextColor(color);
                    callStickerView.UpdateStickerDetail(sticker);
                } else {
                    Toast.makeText(getApplicationContext(), "Click on text to fill color", Toast.LENGTH_LONG).show();
                }
            }
        };

        mFontAdapter = new FontStyleAdapter(EditImageActivity.this, mStyleList, fontStyleInterface);
        mAdapter = new FontColorAdapter(EditImageActivity.this, colors, colorInterface);

//


    }

    public Bitmap BitmapFromPath(String path){
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(path,bmOptions);
    }



    public void init(){

        mImage=findViewById(R.id.mImage);
        mMainframe =findViewById(R.id.mMainframe);

        mEditLayer = findViewById(R.id.mEditLayer);
        mTopLayer = findViewById(R.id.mTopLayer);
        mBottomLayer = findViewById(R.id.mBottomLayer);
        mFilterRL = findViewById(R.id.mFilterRL);
        mCropRL = findViewById(R.id.mCropRL);
        mTextRL = findViewById(R.id.mTextRL);
        mDoodleRL = findViewById(R.id.mDoodleRL);
        sticker_linear = findViewById(R.id.sticker_linear);

        mClose = findViewById(R.id.mClose);
        mDone = findViewById(R.id.mDone);
        mClose.setOnClickListener(this);
        mDone.setOnClickListener(this);

        ic_back = findViewById(R.id.ic_back);
        mFilter = findViewById(R.id.mFilter);
        mCrop = findViewById(R.id.mCrop);
        mText = findViewById(R.id.mText);
        mFrame = findViewById(R.id.mFrame);
        mdoodle = findViewById(R.id.mDoodle);

        ic_back.setOnClickListener(this);
        mFilter.setOnClickListener(this);
        mCrop.setOnClickListener(this);
        mText.setOnClickListener(this);
        mFrame.setOnClickListener(this);
        mdoodle.setOnClickListener(this);

        mEditCrop = findViewById(R.id.mEditCrop);
        mEditCrop.setOnClickListener(this);
        mEditRotate = findViewById(R.id.mEditRotate);
        mEditRotate.setOnClickListener(this);
        mEditVertical = findViewById(R.id.mEditVertical);
        mEditVertical.setOnClickListener(this);
        mEditHorizontal = findViewById(R.id.mEditHorizontal);
        mEditHorizontal.setOnClickListener(this);

        mAddText = findViewById(R.id.mAddText);
        mAddText.setOnClickListener(this);
        mTextStyle = findViewById(R.id.mTextStyle);
        mTextStyle.setOnClickListener(this);
        mTextColor = findViewById(R.id.mTextColor);
        mTextColor.setOnClickListener(this);

        image1 = findViewById(R.id.image1); image1.setOnClickListener(this);
        image2 = findViewById(R.id.image2); image2.setOnClickListener(this);
        image3 = findViewById(R.id.image3); image3.setOnClickListener(this);
        image4 = findViewById(R.id.image4); image4.setOnClickListener(this);
        image5 = findViewById(R.id.image5); image5.setOnClickListener(this);
        image6 = findViewById(R.id.image6); image6.setOnClickListener(this);
        image7 = findViewById(R.id.image7); image7.setOnClickListener(this);
        image8 = findViewById(R.id.image8); image8.setOnClickListener(this);
        image9 = findViewById(R.id.image9); image9.setOnClickListener(this);
        image10 = findViewById(R.id.image10); image10.setOnClickListener(this);
        image11 = findViewById(R.id.image11); image11.setOnClickListener(this);
        image12 = findViewById(R.id.image12); image12.setOnClickListener(this);
        image13 = findViewById(R.id.image13); image13.setOnClickListener(this);
        image14 = findViewById(R.id.image14); image14.setOnClickListener(this);
        image15 = findViewById(R.id.image15); image15.setOnClickListener(this);
        image16 = findViewById(R.id.image16); image16.setOnClickListener(this);
        image17 = findViewById(R.id.image17); image17.setOnClickListener(this);
        image18 = findViewById(R.id.image18); image18.setOnClickListener(this);
        image19 = findViewById(R.id.image19); image19.setOnClickListener(this);
        image20 = findViewById(R.id.image20); image20.setOnClickListener(this);
        image21 = findViewById(R.id.image21); image21.setOnClickListener(this);
        image22 = findViewById(R.id.image22); image22.setOnClickListener(this);
        image23 = findViewById(R.id.image23); image23.setOnClickListener(this);
        image24 = findViewById(R.id.image24); image24.setOnClickListener(this);
        image25 = findViewById(R.id.image25); image25.setOnClickListener(this);
        image26 = findViewById(R.id.image26); image26.setOnClickListener(this);
        image27 = findViewById(R.id.image27); image27.setOnClickListener(this);
        image28 = findViewById(R.id.image28); image28.setOnClickListener(this);
        image29 = findViewById(R.id.image29); image29.setOnClickListener(this);
        image30 = findViewById(R.id.image30); image30.setOnClickListener(this);
        image31 = findViewById(R.id.image31); image31.setOnClickListener(this);
        image32 = findViewById(R.id.image32); image32.setOnClickListener(this);
        image33 = findViewById(R.id.image33); image33.setOnClickListener(this);
        image34 = findViewById(R.id.image34); image34.setOnClickListener(this);
        image35 = findViewById(R.id.image35); image35.setOnClickListener(this);
        image36 = findViewById(R.id.image36); image36.setOnClickListener(this);
        image37 = findViewById(R.id.image37); image37.setOnClickListener(this);
        image38 = findViewById(R.id.image38); image38.setOnClickListener(this);
        image39 = findViewById(R.id.image39); image39.setOnClickListener(this);

        mFilterPager=findViewById(R.id.mFilterPager);

        stickerView = (StickerView) findViewById(R.id.sticker_view);
        stickerView.setOnClickListener(this);

        callStickerView = new CallStickerView(this, stickerView);
        callStickerView.IntializeStickerView();
        callStickerView.IntializeStickerEvent();
        callStickerView.ShowBorder();

        mStyleRec =findViewById(R.id.mStyleRec);
        mStyleRec.setVisibility(View.GONE);
        mStyleRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.IsFramed){
            Uri uri=Utils.mEditedURI;
            try {
                Utils.mEditedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                mFilterBitmap=Utils.mEditedBitmap;
                Glide.with(EditImageActivity.this)
                        .load(Utils.mEditedBitmap)
                        .into(mImage);
                Utils.IsFramed=false;
            } catch (IOException e) {
                Log.e("Enable to frame","!!!");
                e.printStackTrace();
            }
        }else{
            if(!Utils.IsCropped) {
                String mEditFile = Utils.mEditpath;
                Utils.mOriginalFile = new File(mEditFile);

                Glide.with(EditImageActivity.this)
                        .load(Utils.mOriginalFile.getPath())
                        .into(mImage);
                Utils.mOriginalBitmap = BitmapFromPath(Utils.mOriginalFile.getAbsolutePath());
                Utils.mEditedBitmap = Utils.mOriginalBitmap;
                mFilterBitmap = Utils.mOriginalBitmap;
            }
            if(!Utils.IsFramed) {

                Glide.with(EditImageActivity.this)
                        .load(Utils.mEditedBitmap)
                        .into(mImage);
                mFilterBitmap = Utils.mEditedBitmap;
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ic_back:{

                onBackPressed();

            }
            break;

            case R.id.mFilter:{
                mTopLayer.setVisibility(View.GONE);
                mEditLayer.setVisibility(View.VISIBLE);
                mFilterRL.setVisibility(View.VISIBLE);
                mCropRL.setVisibility(View.GONE);
                mTextRL.setVisibility(View.GONE);
                mBottomLayer.setVisibility(View.GONE);
                callStickerView.HideBorder();
                setupViewPager();

            }
            break;

            case R.id.mCrop: {

                mTopLayer.setVisibility(View.GONE);
                mEditLayer.setVisibility(View.VISIBLE);
                mCropRL.setVisibility(View.VISIBLE);
                mFilterRL.setVisibility(View.GONE);
                mTextRL.setVisibility(View.GONE);
                mBottomLayer.setVisibility(View.GONE);
                mDoodleRL.setVisibility(View.GONE);
                callStickerView.HideBorder();

            }
            break;

            case R.id.mText: {

                mTopLayer.setVisibility(View.GONE);
                mEditLayer.setVisibility(View.VISIBLE);
                mCropRL.setVisibility(View.GONE);
                mFilterRL.setVisibility(View.GONE);
                mTextRL.setVisibility(View.VISIBLE);
                mDoodleRL.setVisibility(View.GONE);
                mBottomLayer.setVisibility(View.GONE);

                AddText();
            }
            break;

            case R.id.mFrame: {

                callStickerView.HideBorder();
                mMainframe.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(mMainframe.getDrawingCache());
                mMainframe.setDrawingCacheEnabled(false);
                Utils.mEditedURI = getImageUri(EditImageActivity.this,bitmap);
                Intent in=new Intent(EditImageActivity.this,FrameActivity.class);
                startActivity(in);
                finish();

            }
            break;

            case R.id.mDoodle: {

                mTopLayer.setVisibility(View.GONE);
                mEditLayer.setVisibility(View.VISIBLE);
                mCropRL.setVisibility(View.GONE);
                mFilterRL.setVisibility(View.GONE);
                mTextRL.setVisibility(View.GONE);
                mDoodleRL.setVisibility(View.VISIBLE);
                mBottomLayer.setVisibility(View.GONE);
                sticker_linear.setVisibility(View.VISIBLE);

            }
                break;

            case R.id.mClose: {

                if(mEditLayer.getVisibility()==View.VISIBLE){
                    onBackPressed();
                }
                OriginalView();
            }
            break;

            case R.id.image1:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_celebration_0002));

            }
            break;

            case R.id.image2:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_celebration_0003));

            }
            break;

            case R.id.image3:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_celebration_0004));

            }
            break;

            case R.id.image4:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_celebration_0005));

            }
            break;

            case R.id.image5:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_celebration_0012));

            }
            break;

            case R.id.image6:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_celebration_0013));

            }
            break;

            case R.id.image7:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_celebration_0014));

            }
            break;

            case R.id.image8:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_celebration_0015));

            }
            break;

            case R.id.image9:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_celebration_0020));

            }
            break;

            case R.id.image10:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0011));

            }
            break;

            case R.id.image11:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0013));

            }
            break;

            case R.id.image12:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0014));

            }
            break;

            case R.id.image13:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0015));

            }
            break;

            case R.id.image14:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0016));

            }
            break;

            case R.id.image15:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0017));

            }
            break;
            case R.id.image16:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0018));

            }
            break;

            case R.id.image17:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0020));

            }
            break;

            case R.id.image18:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0024));

            }
            break;

            case R.id.image19:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0025));

            }
            break;

            case R.id.image20:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0026));

            }
            break;

            case R.id.image21:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0031));

            }
            break;
            case R.id.image22:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0043));

            }
            break;
            case R.id.image23:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0044));

            }
            break;
            case R.id.image24:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0045));

            }
            break;
            case R.id.image25:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0046));

            }
            break;
            case R.id.image26:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0047));

            }
            break;
            case R.id.image27:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0048));

            }
            break;
            case R.id.image28:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0049));

            }
            break;
            case R.id.image29:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0050));

            }
            break;
            case R.id.image30:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0051));

            }
            break;
            case R.id.image31:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0052));

            }
            break;
            case R.id.image32:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0053));

            }
            break;
            case R.id.image33:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0054));

            }
            break;
            case R.id.image34:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0055));

            }
            break;
            case R.id.image35:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0056));

            }
            break;
            case R.id.image36:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_smileys_0049));

            }
            break;
            case R.id.image37:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_smileys_0050));

            }
            break;
            case R.id.image38:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_emotion_0052));

            }
            break;
            case R.id.image39:{

                callStickerView.AdImageSticker(getResources().getDrawable(R.drawable.emoji_smileys_0056));

            }
            break;

            case R.id.mDone: {
                OriginalView();
                new AsynchSaveImage().execute((Void[]) null);
            }
            break;

            case R.id.mEditCrop: {
                Utils.IsCropped=true;
                callStickerView.HideBorder();
                startCropImageActivity(getImageUri(EditImageActivity.this,Utils.mEditedBitmap));

            }
            break;

            case R.id.mEditRotate: {
                callStickerView.HideBorder();
                new AsynchRotateImage().execute((Void[]) null);

            }
            break;

            case R.id.mEditVertical: {
                callStickerView.HideBorder();
                Utils.mEditedBitmap=flipImage(Utils.mEditedBitmap,3);
                mFilterBitmap=Utils.mEditedBitmap;
                mImage.setImageBitmap(Utils.mEditedBitmap);
            }
            break;

            case R.id.mEditHorizontal: {
                callStickerView.HideBorder();
                Utils.mEditedBitmap=flipImage(Utils.mEditedBitmap,4);
                mFilterBitmap=Utils.mEditedBitmap;
                mImage.setImageBitmap(Utils.mEditedBitmap);
            }
            break;

            case R.id.mAddText:{
                mStyleRec.setVisibility(View.GONE);
                AddText();
            }
            break;

            case R.id.mTextStyle:{
                mStyleRec.setVisibility(View.VISIBLE);
                mStyleRec.setAdapter(mFontAdapter);
                mStyleRec.setItemAnimator(new DefaultItemAnimator());
            }
            break;

            case R.id.mTextColor:{
                mStyleRec.setVisibility(View.VISIBLE);
                mStyleRec.setAdapter(mAdapter);
                mStyleRec.setItemAnimator(new DefaultItemAnimator());
            }
            break;

            case R.id.sticker_view:{
                mTopLayer.setVisibility(View.GONE);
                mBottomLayer.setVisibility(View.GONE);
                mEditLayer.setVisibility(View.VISIBLE);
                mTextRL.setVisibility(View.VISIBLE);
            }
            break;
        }
    }

    private void InrializeFontArrayList(String dirFrom) {
        Resources res = getResources(); //if you are in an activity
        AssetManager am = res.getAssets();
        mStyleList = new String[0];
        try {
            mStyleList = am.list(dirFrom);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void IntializeColorArrayList() {
        TypedArray ta = getResources().obtainTypedArray(R.array.rainbow);
        colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
    }

    public Bitmap flipImage(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if(type == 3) {

            matrix.preScale(1.0f,-1.0f);
        }
        // if horizonal
        else if(type == 4) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    int CurrentAngle_Postition = -1;
    Integer Angle[] = new Integer[]{90, 180, -90, 0};
    public void rotate() {
        try {

            CurrentAngle_Postition--;
            if (CurrentAngle_Postition == -2) {
                CurrentAngle_Postition = 2;
            }
            if (CurrentAngle_Postition < 0) {
                CurrentAngle_Postition = 3;
            }


            Glide.with(EditImageActivity.this)
                    .asBitmap()
                    .load(Utils.mEditedBitmap)
                    .transform(new RotateTransformation(EditImageActivity.this, Angle[CurrentAngle_Postition]))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource,@Nullable Transition<? super Bitmap> transition) {
                            mImage.setImageBitmap(resource);
                            Utils.mEditedBitmap=resource;
                            mFilterBitmap=Utils.mEditedBitmap;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        } catch (Exception e) {
            Log.e("Error:",e.getMessage());
        }
    }

    public class AsynchRotateImage extends AsyncTask<Void, Void, Void>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressDialog = new ProgressDialog(EditImageActivity.this);
                progressDialog.setMessage("Wait..");
                progressDialog.show();
            } catch (Exception e) {
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            rotate();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (progressDialog != null) {

                    if (!EditImageActivity.this.isFinishing() && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }catch (Exception e){}
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAllowRotation(false)
                .start(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // handle result of CropImageActivity
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Bitmap bitmap = null;
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    mImage.setImageBitmap(bitmap);
                    Utils.mEditedBitmap=bitmap;
                    mFilterBitmap=bitmap;

                }catch(IOException e){
                    Toast.makeText(EditImageActivity.this,"Problem in cropping image!!!",Toast.LENGTH_SHORT).show();
                }

            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(this,"Cropping failed: " + result.getError(),Toast.LENGTH_LONG).show();
            }
        }
    }

    public void OriginalView(){
        mEditLayer.setVisibility(View.VISIBLE);
        mBottomLayer.setVisibility(View.VISIBLE);
        mFilterRL.setVisibility(View.GONE);
        mCropRL.setVisibility(View.GONE);
        mTextRL.setVisibility(View.GONE);
        mStyleRec.setVisibility(View.GONE);
        mDoodleRL.setVisibility(View.GONE);
        sticker_linear.setVisibility(View.GONE);

    }

    public class AsynchSaveImage extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                callStickerView.HideBorder();
                progressDialog = new ProgressDialog(EditImageActivity.this);
                progressDialog.setMessage("Wait..");
                progressDialog.show();
            } catch (Exception e) {
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mMainframe.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(mMainframe.getDrawingCache());
            mMainframe.setDrawingCacheEnabled(false);
            Uri uri=getImageUri(EditImageActivity.this,bitmap);
            Utils.CaptureImage(uri,EditImageActivity.this);
            Utils.IsUpdate = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (progressDialog != null) {

                    if (!EditImageActivity.this.isFinishing() && progressDialog.isShowing()) {
                        progressDialog.dismiss();

                    }
                }
            }catch (Exception e){}

        }

    }

    private void setupViewPager() {

        mFilterPager.setVisibility(View.VISIBLE);
        FilterPagerAdapter adapter = new FilterPagerAdapter(getSupportFragmentManager());

        filtersListFragment = new FilterFragment();
        filtersListFragment.setListener(this);

        adapter.addFragment(filtersListFragment, "Filters");

        mFilterPager.setAdapter(adapter);

    }

    @Override
    public void onBackPressed(){
        if(mFilterRL.getVisibility()==View.VISIBLE || mCropRL.getVisibility()==View.VISIBLE || mTextRL.getVisibility()==View.VISIBLE || mStyleRec.getVisibility()==View.VISIBLE || mDoodleRL.getVisibility()==View.VISIBLE){
            Log.e("back on","OriginalView");
            OriginalView();
        }else{
            Log.e("back on","backpress super");
            Utils.IsCropped = false;
            super.onBackPressed();

        }

    }

    public void AddText(){

        AlertDialog alert = new AlertDialog.Builder(this).create();
        LayoutInflater factory = LayoutInflater.from(EditImageActivity.this);
        final View view = factory.inflate(R.layout.text_dialog_view,null);
        alert.setView(view);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCancel = view.findViewById(R.id.mCancel);
        mOk = view.findViewById(R.id.mOk);
        mAddedText = view.findViewById(R.id.mEditText);
        mDialogTitle = view.findViewById(R.id.t1);
        mDialogTitle.setText("Add text");
        mCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                alert.dismiss();
            }
        });

        mOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                newText=mAddedText.getText().toString();
                if(!newText.equals("")) {
                    callStickerView.AdTextViewSticker(newText, null);
                    callStickerView.ShowBorder();
                }
                alert.dismiss();
            }
        });

        alert.show();
    }

    @Override
    public void onFilterSelected(Filter filter) {
        Bitmap bitmap=mFilterBitmap;
        final int maxSize = 960;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight =bitmap.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap,outWidth,outHeight,true);

        Bitmap bitmap1 = filter.processFilter(bitmap);
        mImage.setImageBitmap(bitmap1);
        Utils.mEditedBitmap=bitmap1;
    }

    static class FilterPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public FilterPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }

}