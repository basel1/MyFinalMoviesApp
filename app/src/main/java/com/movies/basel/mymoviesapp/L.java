package com.movies.basel.mymoviesapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Basel on 03/02/2016.
 */
public class L {
    public static void m(String message) {
        Log.d("my trace", "" + message);
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }
    public static void T(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }
}
