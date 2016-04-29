package com.movies.basel.mymoviesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movies.basel.mymoviesapp.Database.DbAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "position";
    Movie theMovie;
    AppBarLayout movieAppBar;
    Toolbar toolbar;
    RecyclerView ReviewList;
    View MyView;
    // TODO: Rename and change types of parameters
    private int movie_position;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie_position = getArguments().getInt(ARG_PARAM1);

        }
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView;
        fragmentView=inflater.inflate(R.layout.activity_movies_details, container, false);
        MyView=fragmentView;
        toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);
               theMovie = MySingleton.getInstance().getMovieAt(movie_position);
        ReviewList = (RecyclerView)fragmentView.findViewById(R.id.reviews_List);

        movieAppBar = (AppBarLayout) fragmentView.findViewById(R.id.app_bar);
        ((TextView) fragmentView.findViewById(R.id.over_view)).setText("Overview : " + theMovie.getOverview());
        ((TextView) fragmentView.findViewById(R.id.release_date)).setText("Release Date : " + theMovie.getRelease_date());
        ((TextView) fragmentView.findViewById(R.id.voting_average)).setText("Vote Average : " + theMovie.getVote_rating());
        new getImage().execute(theMovie.getPoster(), theMovie.getMovie_id());


        return fragmentView;

    }
@Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Activity parentActivty= getActivity();
    ((MainActivity)getActivity()).setSupportActionBar(toolbar);
    ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getActivity(). setTitle(theMovie.getName());

}



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int position);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shareTopic) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,"http://www.youtube.com/watch?v="+MySingleton.getInstance().getTrailerList().get(0));

            this.startActivity(Intent.createChooser(shareIntent, "share via"));

        }
        if (id == R.id.addfavorite) {
            new DbAdapter(getActivity()).addNewFavourite(theMovie);
        }

        return super.onOptionsItemSelected(item);
    }


    private class getImage extends AsyncTask<String, Void, Drawable> {

        Drawable d;

        @Override
        protected Drawable doInBackground(String... params) {
            try {
                d = drawable_from_url(params[0]);
                new MoviesApiConnection().getReviews(params[1]);
                new MoviesApiConnection().gettrailers(params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return d;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                movieAppBar.setBackground(drawable);
            }
            L.m("nnn");
            ReviewList.setLayoutManager(new MyLinearLayoutManager(getActivity()));
            ReviewList.setItemAnimator(new DefaultItemAnimator());
            ReviewList.setNestedScrollingEnabled(false);
            ReviewList.setAdapter(new ReviewsAdapter());
            // trailer listner
            View.OnClickListener c = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int) v.getTag();
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + MySingleton.getInstance().getTrailerList().get(id - 1))));
                    L.m("http://www.youtube.com/watch?v=" + MySingleton.getInstance().getTrailerList().get(id - 1));
                }
            };
            LinearLayout section = (LinearLayout) MyView.findViewById(R.id.movieDetailsSection);
            for (int i = 1; i <= MySingleton.getInstance().getTrailerList().size(); i++) {
                Button b = new Button(getActivity());
                b.setText("Watch Trailer " + i);
                b.setTag(i);
                b.setOnClickListener(c);
                section.addView(b);
            }


        }
    }

    Drawable drawable_from_url(String url) throws IOException {
        Bitmap x;
        if (url.contains("files"))
            x = BitmapFactory.decodeFile(url);
        else {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.connect();
            InputStream input = connection.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            x = BitmapFactory.decodeStream(input);
        }
        L.m("imaage = " + url);
        return new BitmapDrawable(Resources.getSystem(), x);
    }

}



