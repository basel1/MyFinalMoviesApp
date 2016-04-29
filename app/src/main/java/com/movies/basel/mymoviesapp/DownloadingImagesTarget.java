package com.movies.basel.mymoviesapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.movies.basel.mymoviesapp.Database.DbAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Basel on 28/04/2016.
 */
public class DownloadingImagesTarget implements com.squareup.picasso.Target {
    Context context;
    int counter;
    DbAdapter dbAdapter;
    ArrayList<String> urls;
    public DownloadingImagesTarget(Context c , ArrayList<String>imageUrls)
    {
     context=c;
        counter=-1;
        urls=imageUrls;
         dbAdapter=new DbAdapter(c);
    }
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        counter++;
        File folder = context.getExternalFilesDir("art_data");
        File e = new File(folder, String.valueOf(Calendar.getInstance().getTimeInMillis()) +new Random().nextInt(100)+".PNG");
        try {
            FileOutputStream out=new FileOutputStream(e);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            L.m(from.name() + "///" + from.toString());
            dbAdapter.updatePhotoPath(e.getPath(),urls.get(counter));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
