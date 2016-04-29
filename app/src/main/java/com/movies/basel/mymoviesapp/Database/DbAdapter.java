package com.movies.basel.mymoviesapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.movies.basel.mymoviesapp.DownloadingImagesTarget;
import com.movies.basel.mymoviesapp.L;
import com.movies.basel.mymoviesapp.Movie;
import com.movies.basel.mymoviesapp.MySingleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Basel on 16/04/2016.
 */
public class DbAdapter {
    MoviesDatabase moviesDatabase;
    Context context;

    public DbAdapter(Context c) {
        context = c;
        moviesDatabase = new MoviesDatabase(context);
    }

    public void insertdata(ArrayList<Movie> Movies) {

        int rows = Movies.size();
        SQLiteDatabase db = moviesDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < rows; i++) {
            contentValues.put(TableModel.movie_id_ID, Movies.get(i).getMovie_id());
            contentValues.put(TableModel.movie_NAME, Movies.get(i).getName());
            contentValues.put(TableModel.overview, Movies.get(i).getOverview());
            contentValues.put(TableModel.Poster, Movies.get(i).getPoster());
            contentValues.put(TableModel.release_date, Movies.get(i).getRelease_date());
            contentValues.put(TableModel.vote_average, Movies.get(i).getVote_rating());
            contentValues.put(TableModel.vote_count, Movies.get(i).getVote_rating());
            long n ;
            try {
                n = db.insert(TableModel.TABLE_Movie, null, contentValues);
            } catch (Exception e) {
                break;
            }
            L.m("in inserting" + n);

        }

    }

    public void addNewFavourite(Movie Movie) {
        SQLiteDatabase db = moviesDatabase.getWritableDatabase();
        try {
            db.execSQL("insert into favorite ("+TableModel.movie_id_ID+")  select "+TableModel.movie_id_ID+" from Movie  where movie_name='" + Movie.getName() + "' ");
            L.T(context, Movie.getName() + " \nhave been added to favourite");
            ArrayList<String> m = new ArrayList<>();
            m.add(Movie.getPoster());
            downloadMoviePics(m);
        } catch (SQLException e) {
            Toast.makeText(context, "the movie already exist ", Toast.LENGTH_LONG).show();

        }
        L.m("inserting in favorite query");
    }

    public void getFavoriteData() {
        Log.d("my trace", "in begin getFavoritedatafrom sql");
        SQLiteDatabase db = moviesDatabase.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select a.*,f.* from " + TableModel.TABLE_Movie + " as a ,favorite as f " +
                "where a." + TableModel.movie_id_ID + "=f." + TableModel.movie_id_ID + "", null);
        int rows = cursor.getCount();
        if (rows == 0) {

        } else {
            ArrayList<Movie> MovieList = new ArrayList<>(rows);
for (int i=0;i<rows;i++)
    MovieList.add(new Movie());
            int movie_id = cursor.getColumnIndex(TableModel.movie_id_ID);
            int movie_name = cursor.getColumnIndex(TableModel.movie_NAME);
            int overview = cursor.getColumnIndex(TableModel.overview);
            int poster = cursor.getColumnIndex(TableModel.Poster);
            int release_date = cursor.getColumnIndex(TableModel.release_date);
            int vote_average = cursor.getColumnIndex(TableModel.vote_average);
            int index = 0;
            while (cursor.moveToNext()) {
                MovieList.get(index).setMovie_id(cursor.getString(movie_id));
                MovieList.get(index).setName(cursor.getString(movie_name));
                MovieList.get(index).setVote_rating(cursor.getString(vote_average));
                MovieList.get(index).setRelease_date(cursor.getString(release_date));
                MovieList.get(index).setPoster(cursor.getString(poster));
                MovieList.get(index).setOverview(cursor.getString(overview));
                index++;
                Log.d("my trace", "before setting favorite data" + index + cursor.getString(movie_name));
            }

            MySingleton.getInstance().setFavoriteMovies(MovieList);
        }
    }

    public void downloadMoviePics(final ArrayList<String> movires_posters) {


        DownloadingImagesTarget target=new DownloadingImagesTarget(context,movires_posters);
        for (int i=0;i<movires_posters.size();i++){
        Picasso.with(context).load(movires_posters.get(i)).into(target);
    }
    }

    public void updatePhotoPath(String path, String oldurl) {
        ContentValues c = new ContentValues();
        c.put(TableModel.Poster, path);
        SQLiteDatabase db = moviesDatabase.getWritableDatabase();
        String where = TableModel.Poster + " =? ";
        String[] whereArgs = new String[]{oldurl};
        int count = db.update(TableModel.TABLE_Movie, c, where, whereArgs);
        Log.i("my trace", "here updatephotopath count= " + count);
    }
}
