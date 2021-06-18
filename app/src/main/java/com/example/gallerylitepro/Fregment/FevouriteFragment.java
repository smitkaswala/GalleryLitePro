package com.example.gallerylitepro.Fregment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gallerylitepro.Adapters.FavouriteAdapter;
import com.example.gallerylitepro.Adapters.ImageAdapter;
import com.example.gallerylitepro.R;
import com.example.gallerylitepro.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class FevouriteFragment extends Fragment {

    ArrayList<String> mFavouriteImageList = new ArrayList<>();
    String json1;
    RecyclerView mImageRec;
    FavouriteAdapter favouriteAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fevourite, container, false);

        mImageRec = view.findViewById(R.id.images_grid);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Favourites_pref",MODE_PRIVATE);


        Gson gson = new Gson();
        mFavouriteImageList = new ArrayList<>();
        json1 = sharedPreferences.getString("Fav_Image","");
        Type type1 = new TypeToken<ArrayList<String>>(){}.getType();
        mFavouriteImageList = gson.fromJson(json1,type1);


        if(mFavouriteImageList!=null && mFavouriteImageList.size() > 0){

            favouriteAdapter = new FavouriteAdapter(getActivity());

            if(Utils.VIEW_TYPE.equals("Grid"))
            {
                mImageRec.setLayoutManager(new GridLayoutManager(getContext(), Utils.COLUMN));
                mImageRec.setLayoutAnimation(null);
            }

            mImageRec.setAdapter(favouriteAdapter);

            favouriteAdapter.addAll(mFavouriteImageList);
            favouriteAdapter.notifyDataSetChanged();

        }

    }
}