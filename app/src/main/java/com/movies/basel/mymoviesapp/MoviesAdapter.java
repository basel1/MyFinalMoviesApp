package com.movies.basel.mymoviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Basel on 12/03/2016.
 */
public class MoviesAdapter extends BaseAdapter implements AdapterView.OnItemClickListener ,MainFragment.OnFragmentInteractionListener {
    ArrayList<Movie> moviesList;
    private Context context;

    public MoviesAdapter() {
        moviesList = MySingleton.getInstance().getMoviesList();
    }

    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            context = parent.getContext();
            view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }
        ImageView poster = (ImageView) view.findViewById(R.id.poster_grid);
        TextView title = (TextView) view.findViewById(R.id.movieName_grid);
        if (moviesList.get(position).getPoster().contains("files"))
            Picasso.with(context).load(new File(moviesList.get(position).getPoster())).into(poster);
        else
        Picasso.with(context).load(moviesList.get(position).getPoster()).into(poster);
        title.setText(moviesList.get(position).getName());
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, MoviesDetails.class).putExtra("position",position);
       // context.startActivity(intent);
        this.onFragmentInteraction(position);
    }

    @Override
    public void onFragmentInteraction(int position) {
        FrameLayout f2= (FrameLayout)((MainActivity) context).findViewById(R.id.Pane2);
        if(null==f2)
        {
            Intent intent = new Intent(context, MoviesDetails.class).putExtra("position",position);
            context.startActivity(intent);
        }
        else
        {
            MainFragment detailsFragment= new MainFragment();
            Bundle extras= new Bundle();
            extras.putInt("position", position);
            detailsFragment.setArguments(extras);
            ((MainActivity) context).getFragmentManager().beginTransaction().replace(R.id.Pane2,detailsFragment).commit();
        }
    }
}

