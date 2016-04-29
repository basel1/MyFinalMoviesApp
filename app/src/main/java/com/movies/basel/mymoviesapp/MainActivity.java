package com.movies.basel.mymoviesapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.movies.basel.mymoviesapp.Database.DbAdapter;

public class MainActivity extends AppCompatActivity {
    GridView moviesgridview;
    MoviesAdapter moviesAdapter;
    Toolbar toolbar;
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesgridview = (GridView) findViewById(R.id.moviesGridView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(isOnline())
            new DownloadMovies().execute("popular");
        else
        {
            new DbAdapter(this).getFavoriteData();
            moviesAdapter = new MoviesAdapter();
            moviesgridview.setAdapter(moviesAdapter);
            moviesgridview.setOnItemClickListener(moviesAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        if (item.getItemId() == R.id.popular) {
            L.T(this, "Popular");
            new DownloadMovies().execute("popular");
        }
        if (item.getItemId() == R.id.rating) {
            L.T(this, "Most Rated");
            new DownloadMovies().execute("top_rated");
        }
        if (item.getItemId() == R.id.favorite) {
            L.T(this, "Favorite");
            new DbAdapter(this).getFavoriteData();
            moviesAdapter = new MoviesAdapter();
            moviesgridview.setAdapter(moviesAdapter);
            moviesgridview.setOnItemClickListener(moviesAdapter);
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadMovies extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            new MoviesApiConnection().connect(params[0]);
            return false;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            moviesAdapter = new MoviesAdapter();
            moviesgridview.setAdapter(moviesAdapter);
            moviesgridview.setOnItemClickListener(moviesAdapter);
            new DbAdapter(getApplicationContext()).insertdata(MySingleton.getInstance().getMoviesList());


        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
