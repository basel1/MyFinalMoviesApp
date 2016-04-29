package com.movies.basel.mymoviesapp;

import com.movies.basel.mymoviesapp.Database.DbAdapter;

import java.util.ArrayList;

/**
 * Created by Basel on 12/03/2016.
 */
public class MySingleton {
    private static MySingleton mInstance = new MySingleton();
private ArrayList<Movie> moviesList=new ArrayList<>();
    private ArrayList<Review> reviewsList=new ArrayList<>();
    private ArrayList<String> trailerList;
    private ArrayList<Movie> favoritemovies;

    public static MySingleton getInstance() {
        if(mInstance == null)
        {
            mInstance = new MySingleton();
        }
        return mInstance;

    }

    public void setMoviesList(ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;

    }

    public ArrayList<Movie> getMoviesList() {
        return moviesList;
    }
    public Movie getMovieAt(int position) {
        return moviesList.get(position);
    }

    public void setReviewsList(ArrayList<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public ArrayList<Review> getReviewsList() {
        if(reviewsList.equals(null))
            reviewsList=new ArrayList<>();
        return reviewsList;
    }

    public void setTrailerList(ArrayList<String> trailerList) {
        this.trailerList = trailerList;
    }

    public ArrayList<String> getTrailerList() {
        if (trailerList==(null))
            trailerList=new ArrayList<>();
        return trailerList;
    }

    public void setFavoriteMovies(ArrayList<Movie> favoriteArticle) {
        this.moviesList = favoriteArticle;
    }

    public ArrayList<Movie> getFavoritemovies() {
        return favoritemovies;
    }
}
