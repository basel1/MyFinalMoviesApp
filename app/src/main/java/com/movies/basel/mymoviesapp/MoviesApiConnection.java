package com.movies.basel.mymoviesapp;

import android.util.Log;

import com.movies.basel.mymoviesapp.Database.DbAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Basel on 09/03/2016.
 */
public class MoviesApiConnection {

    // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    public void connect(String type) {
        // Will contain the raw JSON response as a string.
        String moviesJson = null;

        try {

            URL url = new URL("https://api.themoviedb.org/3/movie/" + type + "?api_key=111b82c9d8e799b356225c4306603bbb");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                moviesJson = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                moviesJson = null;
            }
            moviesJson = buffer.toString();
            parseAndSaveData(moviesJson);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            moviesJson = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }
    }

    private void parseAndSaveData(String moviesJson) {
        try {
            //  Log.d("my trace","s ="+s);
            JSONObject e = new JSONObject(moviesJson);
            JSONArray a = e.getJSONArray("results");
            int moviesCount = a.length();
            ArrayList<Movie> movieList = new ArrayList<Movie>(moviesCount);

            //initalizing
            for (int i = 0; i < moviesCount; i++)
                movieList.add(new Movie());

            int index = 0;
            for (int i = 0; i < moviesCount; i++) {
                JSONObject c = a.getJSONObject(i);
                movieList.get(i).setName(c.getString("original_title"));
                movieList.get(i).setOverview(c.getString("overview"));
                movieList.get(i).setPoster(c.getString("poster_path"));
                movieList.get(i).setRelease_date(c.getString("release_date"));
                movieList.get(i).setVote_rating(c.getString("vote_average"));
                movieList.get(i).setMovie_id(c.getString("id"));
                Log.d("my trace", "loop num " + i + " article : " + c.getString("original_title"));
            }
            MySingleton.getInstance().setMoviesList(movieList);
            Log.d("my trace", "before call downloadArtPics");
        } catch (JSONException e1) {
            Log.d("my trace", "json excp " + e1.getMessage());

        }

    }

    public void getReviews(String Movie_id) {
        String reviewsJson;

        try {

            URL url = new URL("https://api.themoviedb.org/3/movie/" + Movie_id + "/reviews?api_key=111b82c9d8e799b356225c4306603bbb");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                reviewsJson = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                reviewsJson = null;
            }
            reviewsJson = buffer.toString();
            parseReviews(reviewsJson);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseReviews(String reviewsJson) throws JSONException {
        L.m("json review"+reviewsJson);
        JSONObject e = new JSONObject(reviewsJson);
        JSONArray a = e.getJSONArray("results");
        int reviewCount = a.length();
        L.m("reviewsCount "+reviewCount);
        ArrayList<Review> reviewList = new ArrayList<Review>(reviewCount);

        //initalizing
        for (int i = 0; i < reviewCount; i++)
            reviewList.add(new Review());

        int index = 0;
        for (int i = 0; i < reviewCount; i++) {
            JSONObject c = a.getJSONObject(i);
            reviewList.get(i).setAuthor(c.getString("author"));
            reviewList.get(i).setContent(c.getString("content"));

            L.m("loop num " + i + " review : " + c.getString("author"));
        }
        MySingleton.getInstance().setReviewsList(reviewList);
    }
    public void gettrailers(String Movie_id)
    {
        String reviewsJson;

        try {

            URL url = new URL("https://api.themoviedb.org/3/movie/" + Movie_id + "/videos?api_key=111b82c9d8e799b356225c4306603bbb");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                reviewsJson = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                reviewsJson = null;
            }
            reviewsJson = buffer.toString();
            parseVideos(reviewsJson);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseVideos(String VedioJson) throws JSONException {
        JSONObject e = new JSONObject(VedioJson);
        JSONArray a = e.getJSONArray("results");
        int reviewCount = a.length();
        L.m("trailers "+reviewCount);
        ArrayList<String> vedioList = new ArrayList<String>(reviewCount);


        int index = 0;
        for (int i = 0; i < reviewCount; i++) {
            JSONObject c = a.getJSONObject(i);
            vedioList.add(c.getString("key"));

            L.m("loop num " + i + " trailer : " + c.getString("key"));
        }
        MySingleton.getInstance().setTrailerList(vedioList);

    }
}