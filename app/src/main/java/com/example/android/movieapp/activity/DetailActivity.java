package com.example.android.movieapp.activity;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.movieapp.AppExecuters;
import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.R;
import com.example.android.movieapp.adapter.ReviewAdapter;
import com.example.android.movieapp.adapter.TrailerAdapter;
import com.example.android.movieapp.api.Client;
import com.example.android.movieapp.api.MovieService;
import com.example.android.movieapp.data.AppDatabase;
import com.example.android.movieapp.data.Favorite;
import com.example.android.movieapp.model.Movie;
import com.example.android.movieapp.model.Review;
import com.example.android.movieapp.model.ReviewResponse;
import com.example.android.movieapp.model.Trailer;
import com.example.android.movieapp.model.TrailerResponse;
import com.example.android.movieapp.viewmodels.AddFavoriteViewModel;
import com.example.android.movieapp.viewmodels.AddFavoriteViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity {


    List<Trailer> trailerList;
    TrailerAdapter trailerAdapter;

    List<Review> reviewList;
    ReviewAdapter reviewAdapter;

    AddFavoriteViewModelFactory viewModelFactory;
    AppDatabase mDb;

    @BindView(R.id.thumbnail_image_header)
    ImageView posterImageIV;
    @BindView(R.id.movie_title_tv)
    TextView movieNameTV;
    @BindView(R.id.plot_tv)
    TextView plotSynopsisTV;
    @BindView(R.id.rating_tv)
    TextView userRatingTV;
    @BindView(R.id.release_tv)
    TextView releaseDateTV;
    @BindView(R.id.favorite_button)
    FloatingActionButton favoriteButton;
    @BindView(R.id.poster_iv)
    ImageView smallPosterIV;
    @BindView(R.id.recycler_trailer)
    RecyclerView recyclerTrailer;
    @BindView(R.id.recycler_review)
    RecyclerView recyclerReview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        mDb = AppDatabase.getInstance(getApplicationContext());
        movieRecievedFromIntent();



    }


    private void movieRecievedFromIntent() {


        Intent intent = getIntent();
        if (intent.hasExtra("movieitem")) {
            Movie movie = intent.getParcelableExtra("movieitem");

            String poster = movie.getPosterPath();
            String movieTitle = movie.getOriginalTitle();
            String synopsis = movie.getOverview();
            String rating = movie.getVoteAverage();
            String dateRelease = movie.getReleaseDate();
            final int movie_id = movie.getId();
            String backdropPath = movie.getBackdropPath();


            Glide.with(this)
                    .load("http://image.tmdb.org/t/p/w500" + poster)
                    .into(smallPosterIV);

            Glide.with(this)
                    .load("http://image.tmdb.org/t/p/w500" + movie.getBackdropPath())
                    .into(posterImageIV);

            movieNameTV.setText(movieTitle);
            plotSynopsisTV.setText(synopsis);
            userRatingTV.setText(rating);
            releaseDateTV.setText(dateRelease);

            ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle("Movie Details");

            loadTrailers(movie_id);
            loadReviews(movie_id);
            final Favorite favorite = new Favorite(movie_id, movieTitle, rating, poster, synopsis, backdropPath, dateRelease);
             viewModelFactory = new AddFavoriteViewModelFactory(mDb, movie_id);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkIsFavorited(movie_id, favorite);
                }
            });
        } else {
            Toast.makeText(this, "No API data", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadReviews(int movie_id) {

        try {

            MovieService apiService = Client.getClient().create(MovieService.class);

            Call<ReviewResponse> call = apiService.getMovieReview(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                    if (response.body() != null) {
                        List<Review> reviews = response.body().getResults();
                        recyclerReview.setAdapter(new ReviewAdapter(getApplicationContext(), reviews));
                        reviewAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(getApplicationContext(), "Api Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void initViews() {
        trailerList = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(this, trailerList);

        recyclerTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerTrailer.setAdapter(trailerAdapter);

        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList);

        recyclerReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerReview.setAdapter(reviewAdapter);

        recyclerTrailer.smoothScrollToPosition(0);
        recyclerReview.smoothScrollToPosition(0);


    }

    private void loadTrailers(int movie_id) {

        try {

            MovieService apiService = Client.getClient().create(MovieService.class);

            Call<TrailerResponse> call = apiService.getMovieTrailer(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                    if (response.body() != null) {
                        List<Trailer> trailers = response.body().getResults();
                        recyclerTrailer.setAdapter(new TrailerAdapter(getApplicationContext(), trailers));
                        trailerAdapter.notifyDataSetChanged();


                    }
                }

                @Override
                public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(getApplicationContext(), "Api Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFavorite(final Favorite favorite) {

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().insertFavorite(favorite);

            }
        });
    }

    private void deleteFavorite(final int movie_id) {

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().deleteFavoriteWithId(movie_id);

            }
        });
    }


    private void checkIsFavorited(final int movie_id, final Favorite saved) {


        final AddFavoriteViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddFavoriteViewModel.class);

        viewModel.getFavorite().observe(this, new Observer<Favorite>() {
            @Override
            public void onChanged(@Nullable Favorite favorite) {
                viewModel.getFavorite().removeObserver(this);
                if (favorite != null) {

                    deleteFavorite(movie_id);
                    favoriteButton.setImageDrawable(ContextCompat.
                            getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_white_24dp));
                } else {

                    saveFavorite(saved);
                    favoriteButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.ic_favorite_white_24dp));
                }
            }
        });
    }


}
