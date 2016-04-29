package com.movies.basel.mymoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Basel on 17/04/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>{
    ArrayList<Review> reviewsList;
    public ReviewsAdapter()
    {
        reviewsList=MySingleton.getInstance().getReviewsList();
        L.m(reviewsList.size()+"");
    }
    @Override
    public ReviewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        L.m("on createviewholder");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.MyViewHolder holder, int position) {
        holder.author.setText(reviewsList.get(position).getAuthor());
        holder.content.setText(reviewsList.get(position).getContent());
L.m("on bind author= "+holder.author.getText());
    }

    @Override
    public int getItemCount() {
        L.m("reviewlist size"+reviewsList.size());
        return reviewsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author,content;
        public MyViewHolder(View itemView) {
            super(itemView);
            author= (TextView) itemView.findViewById(R.id.author);
            content= (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
