package com.movies.basel.mymoviesapp.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.movies.basel.mymoviesapp.L;

/**
 * Created by Basel on 05/04/2016.
 */
public class MoviesDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Movies";

    private static final String TABLE_Movie = "Movie";

    private static final String movie_id_ID = "movie_id";
    private static final String movie_NAME = "movie_name";
    private static final String Poster = "movie_poster";
    private static final String overview = "movie_overview";
    private static final String release_date = "movie_release_date";
    private static final String vote_count = "movie_vote_count";
    private static final String vote_average = "movie_vote_average";


    public MoviesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table "+TABLE_Movie+" " +
                    "("+movie_id_ID+" integer primary key ," +
                    ""+movie_NAME+" varchar(29) NOT NULL," +
                    "  "+vote_count+" varchar(200) NOT NULL," +
                    "  "+vote_average+" varchar(200) NOT NULL," +
                    "  "+overview+" text NOT NULL," +
                    "  "+release_date+" varchar(20) NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "  "+Poster+" varchar(200) DEFAULT NULL )" );
            db.execSQL("create table favorite " +
                    "(fav_id integer primary key autoincrement," +
                    ""+movie_id_ID+" integer NOT NULL unique ," +
                    " FOREIGN KEY("+movie_id_ID+") REFERENCES "+TABLE_Movie+"("+movie_id_ID+")" +
                    ")");
        } catch (SQLException e) {
            L.m(e.getMessage());
        }

        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
