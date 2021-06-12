package com.example.gallerylitepro.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallerylitepro.Classes.AlbumDetail;
import com.example.gallerylitepro.Interface.FolderInterface;
import com.example.gallerylitepro.R;

import java.io.File;
import java.util.ArrayList;

public class FolderDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<AlbumDetail> objects = new ArrayList<>();
    Activity activity;
    public static String action;
    FolderInterface folderInterface;
    TextView mCancel, mOk;
    TextView mDialogTitle;
    EditText mAddedText;
    String newAlbum;

    public FolderDialogAdapter(Activity activity, ArrayList<AlbumDetail> objects, FolderInterface folderInterface) {
        this.activity = activity;
        this.objects = objects;
        this.folderInterface = folderInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {

            case 0: {
                itemView = LayoutInflater.from(activity).inflate(R.layout.list_image_folder, parent, false);
                viewHolder = new ViewHolder(itemView);
            }
            break;
            case 1: {
                itemView = LayoutInflater.from(activity).inflate(R.layout.create_folder_list, parent, false);
                viewHolder = new ViewHolder2(itemView);
            }
            break;

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            case 0: {
                ViewHolder holders = (ViewHolder) holder;
                AlbumDetail albumDetail = objects.get(position);

                if (albumDetail.getPathList().size() > 0) {
                    Uri uri = Uri.fromFile(new File(albumDetail.getPathList().get(0)));
                    Glide.with(activity)
                            .load(uri)
                            .centerCrop()
                            .into(((ViewHolder) holder).mImage);
                }

                holders.mAlbumName.setText(albumDetail.getBucketName());
                holders.mAlbumName.setSelected(true);
                holders.mCount.setText(albumDetail.getPathList().size() + " Photos");
                holders.rootRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        folderInterface.OnSelectFolder(action, albumDetail.getBucketPath());
                    }
                });
            }

            case 1:
                try {
                    AlbumDetail itemList = objects.get(position);
                    ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                    viewHolder2.rltrootview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CreateAlbum();
                        }
                    });
                } catch
                (Exception e) {

                }

        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        int val = 0;
        try {
            if (objects != null && objects.size() > 0) {
                val = objects.get(position).getType();
            } else {
                val = 0;
            }
        } catch (Exception w) {
        }
        return val;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mAlbumName, mCount;
        RelativeLayout rootRL;

        public ViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.folderImage);
            mAlbumName = itemView.findViewById(R.id.folder_name);
            mCount = itemView.findViewById(R.id.count_name);
            rootRL = itemView.findViewById(R.id.rootRL);

        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {

        public RelativeLayout rltrootview;
        public ImageView mImage;

        public ViewHolder2(View view) {
            super(view);

            rltrootview = view.findViewById(R.id.relative);
            mImage = view.findViewById(R.id.mImage);
        }
    }

    public void CreateAlbum() {

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        LayoutInflater factory = LayoutInflater.from(activity);
        final View view = factory.inflate(R.layout.text_dialog_view, null);
        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCancel = view.findViewById(R.id.mCancel);
        mOk = view.findViewById(R.id.mOk);
        mAddedText = view.findViewById(R.id.mEditText);
        mDialogTitle = view.findViewById(R.id.t1);
        mDialogTitle.setText("Create New Album");
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAlbum = mAddedText.getText().toString();
                if (!newAlbum.equals("")) {
                    String path = Environment.getExternalStorageDirectory() + File.separator + newAlbum;
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    folderInterface.OnSelectFolder(action, path);

                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

}
