package com.example.gallerylitepro.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.gallerylitepro.Classes.AlbumDetail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static ArrayList<AlbumDetail> mFolderDialogList = new ArrayList<>();
    public static String VIEW_TYPE="Grid";
    public static String SORTING_TYPE = "";
    public static String SORTING_TYPE2 = "";
    public static int COLUMN = 2;
    public static boolean IsUpdate=false;
    public static File mOriginalFile;
    public static String mEditpath;
    public static Bitmap mOriginalBitmap;
    public static Bitmap mEditedBitmap;
    public static Uri mEditedURI;
    public static boolean IsFramed=false;
    public static boolean IsCropped=false;
    public static Context c;

    public static int GetRandomNumber(){
        int flag=0;
        flag=new Random().nextInt(1000);
        return flag;
    }

    public static String getSize(long size) {
        long kilo = 1024;
        long mega = kilo * kilo;
        long giga = mega * kilo;
        long tera = giga * kilo;
        String s = "";
        double kb = (double)size / kilo;
        double mb = kb / kilo;
        double gb = mb / kilo;
        double tb = gb / kilo;
        if(size < kilo) {
            s = size + " Bytes";
        } else if(size >= kilo && size < mega) {
            s =  String.format("%.2f", kb) + " KB";
        } else if(size >= mega && size < giga) {
            s = String.format("%.2f", mb) + " MB";
        } else if(size >= giga && size < tera) {
            s = String.format("%.2f", gb) + " GB";
        } else if(size >= tera) {
            s = String.format("%.2f", tb) + " TB";
        }
        return s;
    }

    public static String convertTimeFromUnixTimeStamp(String date) {

        try {
            DateFormat inputFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss zz yyy");
            Date d = null;
            try {
                d = inputFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");

            Locale loc = new Locale("en", "US");
            DateFormat outputFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc);
            return outputFormat.format(d);
        }catch (Exception e){
            return "not vaialble";
        }
    }

    public static boolean delete(final Context context,final File file) {

        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {
            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }

    public static MediaScannerConnection msConn;
    public static void scanPhoto(final String imageFileName) {
        msConn = new MediaScannerConnection(c, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
                msConn.scanFile(imageFileName, null);
                Log.i("msClient obj", "connection established");
            }

            public void onScanCompleted(String path, Uri uri) {
                msConn.disconnect();
                Log.i("msClient obj", "scan completed");
            }
        });
        msConn.connect();
    }

}
