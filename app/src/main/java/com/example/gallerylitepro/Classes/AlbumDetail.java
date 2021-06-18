package com.example.gallerylitepro.Classes;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class AlbumDetail implements Serializable {

    boolean isDirectory = false;
    public String bucket_id;
    //Directory Name
    public String bucketName;

    public String getBucketPath() {
        return bucketPath;
    }

    public void setBucketPath(String bucketPath) {
        this.bucketPath = bucketPath;
    }

    public String bucketPath;
    public String id;
    public String Name;
    public String folderName;
    public ArrayList<String> pathList;
    public String FolderPath;
    public Bitmap bitmap;
    public String date;
    public long size;
    public String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    int type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getBucket_id() {
        return bucket_id;
    }

    public void setBucket_id(String bucket_id) {
        this.bucket_id = bucket_id;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public void setPathList(ArrayList<String> pathList) {
        this.pathList = pathList;
    }

    public String getFolderPath() {
        return FolderPath;
    }

    public void setFolderPath(String folderPath) {
        FolderPath = folderPath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "AlbumDetail{" +
                "isDirectory=" + isDirectory +
                ", bucket_id='" + bucket_id + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", bucketPath='" + bucketPath + '\'' +
                ", id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", folderName='" + folderName + '\'' +
                ", pathList=" + pathList +
                ", FolderPath='" + FolderPath + '\'' +
                ", bitmap=" + bitmap +
                ", date='" + date + '\'' +
                ", size=" + size +
                ", duration='" + duration + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof AlbumDetail) {
            sameSame = this.getFolderName().equals(((AlbumDetail) object).getFolderName());
        }

        return super.equals(object);
    }

}
