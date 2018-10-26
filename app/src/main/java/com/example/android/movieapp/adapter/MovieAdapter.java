package com.example.android.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.movieapp.activity.DetailActivity;
import com.example.android.movieapp.R;
import com.example.android.movieapp.model.Movie;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmed on 04/09/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> movieList;

    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    public void setMovieList(List<Movie> movieList) {

        this.movieList = movieList;

    }

    @NonNull
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {

        holder.title.setText(movieList.get(position).getOriginalTitle());
        String vote = movieList.get(position).getVoteAverage();
        holder.userRating.setText(vote);

        String baseImageUrl = "http://image.tmdb.org/t/p/w500";
        String poster = baseImageUrl + movieList.get(position).getPosterPath();
        String backdropPath = baseImageUrl + movieList.get(position).getBackdropPath();
        Glide.with(mContext)
                .load(poster)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.title_card) TextView title;
        @BindView(R.id.userrating_card)TextView userRating;
        @BindView(R.id.thumbnail) ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Movie clickDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("movieitem", clickDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(view.getContext(), "you clicked " + clickDataItem.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void updateMovieList(List<Movie> movieList){
        this.movieList=movieList;
        notifyDataSetChanged();
    }
}
