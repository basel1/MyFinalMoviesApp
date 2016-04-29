package com.movies.basel.mymoviesapp;

/**
 * Created by Basel on 12/03/2016.
 */
public class Movie {
    String Name;
    String Poster;
    String overview;
    String release_date;
    String vote_rating;
String movie_id;
    public Movie(String name, String poster) {
        Name = name;
        Poster = poster;
    }

    public Movie() {
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_rating(String vote_rating) {
        this.vote_rating = vote_rating;
    }

    public String getName() {
        return Name;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster() {
L.m("Poster "+Poster);
        if (Poster.contains("files"))
            return Poster;
        return "http://image.tmdb.org/t/p/w185/"+Poster;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_rating() {
        return vote_rating;
    }

}
