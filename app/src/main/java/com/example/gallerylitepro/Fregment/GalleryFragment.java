package com.example.gallerylitepro.Fregment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gallerylitepro.Adapters.ImageFolderAdapter;
import com.example.gallerylitepro.Classes.AlbumDetail;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Service.GetFileList;
import com.example.gallerylitepro.Utils.NotificationUtils;
import com.example.gallerylitepro.Utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;

public class GalleryFragment extends Fragment {

    RecyclerView recyclerView;
    LottieAnimationView aviLoader;
    RelativeLayout rl_progress;
    ImageFolderAdapter imageFolderAdapter;
    ArrayList<AlbumDetail> albumDetails = new ArrayList<>();

    public static Handler album_handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gallery, container, false);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recyclerView = view.findViewById(R.id.image_rec);
        aviLoader = view.findViewById(R.id.aviLoader);
        rl_progress = view.findViewById(R.id.rl_progress);

        imageFolderAdapter = new ImageFolderAdapter(getActivity());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationUtils notiUtils = new NotificationUtils(getActivity());
        }

        intializehandler();

        return view;

    }

    public void intializehandler(){

        album_handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int code = msg.what;
                if (code == 23){
                    try{
                        albumDetails = new ArrayList<>();
                        albumDetails = (ArrayList<AlbumDetail>)msg.obj;
                        Utils.mFolderDialogList=new ArrayList<>();
                        AlbumDetail FirstModel1 = new AlbumDetail();
                        FirstModel1.setFolderName("Create_Album");
                        FirstModel1.setType(1);
                        Utils.mFolderDialogList.add(0,FirstModel1);
                        Utils.mFolderDialogList.addAll(albumDetails);

                        if(albumDetails != null && albumDetails.size() > 0){
                            imageFolderAdapter.Addall(albumDetails);
                            Log.d("TAG", "handleMessage:--- " + albumDetails.size());
                        }
                        if(Utils.VIEW_TYPE.equals("Grid")){
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),Utils.COLUMN));
                            recyclerView.setLayoutAnimation(null);
                        }
                        recyclerView.setAdapter(imageFolderAdapter);
                        imageFolderAdapter.notifyDataSetChanged();
                        stopAnim();

                    }catch (Exception e){
                        Log.e("Error",e.getMessage());
                    }
                }
                return false;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnim();
                // activity.startService(new Intent(activity, GetFileList.class).putExtra("action","album"));
                if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    getActivity().startForegroundService(new Intent(getActivity(), GetFileList.class).putExtra("action", "album"));
                }else{
                    getActivity().startService(new Intent(getActivity(), GetFileList.class).putExtra("action", "album"));
                }

            }
        }, 100);

    }

    public  void startAnim() {
        rl_progress.setVisibility(View.VISIBLE);
        aviLoader.setVisibility(View.VISIBLE);
    }
    public  void stopAnim() {
        rl_progress.setVisibility(View.GONE);
        aviLoader.setVisibility(View.GONE);
    }

    class NameNoComparator implements Comparator<AlbumDetail> {

        @Override
        public int compare(AlbumDetail o1, AlbumDetail o2) {
            return Integer.compare(o1.getPathList().size(), o2.getPathList().size());
        }
    }

}