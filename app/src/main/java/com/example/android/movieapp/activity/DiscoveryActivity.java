package com.example.android.movieapp.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.R;
import com.example.android.movieapp.adapter.MovieAdapter;
import com.example.android.movieapp.api.Client;
import com.example.android.movieapp.api.MovieService;
import com.example.android.movieapp.data.Favorite;
import com.example.android.movieapp.model.Movie;
import com.example.android.movieapp.model.MovieResponse;
import com.example.android.movieapp.utils.CommonUtils;
import com.example.android.movieapp.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.preference.PreferenceManager;


public class DiscoveryActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.discovery_main)
    SwipeRefreshLayout refreshLayout;

    private MovieAdapter adapter;
    private ArrayList<Movie> movieData = new ArrayList<>();


    private static final String LIST_STATE = "list-state";
    private Parcelable savedRecyclerViewLayoutState;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler-layout";

    private GridLayoutManager mGridLayoutManager;

    MainViewModel viewModel;
    public static final String LOG_TAG = MovieAdapter.class.getName();

    int columns;
    private ProgressDialog mProgressDialog;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descovery_screen);
        ButterKnife.bind(this);
         sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        configChanged();
        setUpRecyclerview();
        setupSharedPreferences(sharedPreferences);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        refreshLayout.setOnRefreshListener(this);

    }

    private void setUpRecyclerview() {
        adapter = new MovieAdapter(this, movieData);
        mGridLayoutManager = new GridLayoutManager(this, columns);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void configChanged() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            columns = 2;
        } else {

            columns = 4;
        }
    }


    private void loadJSONMostPopular() {

        showLoading();
        try {

            MovieService apiService =
                    Client.getClient().create(MovieService.class);
            Call<MovieResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MovieResponse>() {

                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.body() != null) {
                        List<Movie> movieList = response.body().getResults();

                        movieData.clear();
                        movieData.addAll(movieList);
                        updateAdapterData(movieData);
                        Log.e("popsize", "" + movieData.size());
                    }
                    hideLoading();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                    Log.d("Error", t.getMessage());
                    Toast.makeText(DiscoveryActivity.this, "Error fetching data!", Toast.LENGTH_SHORT).show();
                    hideLoading();

                }


            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    private void loadJSONTopRated() {

        showLoading();
        try {

            MovieService apiService =
                    Client.getClient().create(MovieService.class);
            Call<MovieResponse> call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MovieResponse>() {

                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.body() != null) {
                        List<Movie> movieList = response.body().getResults();
                        movieData.clear();
                        movieData.addAll(movieList);
                        updateAdapterData(movieData);
                    }
                    hideLoading();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                    Log.d("Error", t.getMessage());
                    Toast.makeText(DiscoveryActivity.this, "Error fetching data!", Toast.LENGTH_SHORT).show();
                    hideLoading();
                }

            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        setupSharedPreferences(sharedPreferences);
        Log.d(LOG_TAG, "Preferences updated");

        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    private void displayFavorites() {
        movieData.clear();
        viewModel.getFavorite().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(@Nullable List<Favorite> favorites) {

                if (favorites != null) {
                    for (Favorite entry : favorites) {
                        Movie movie = new Movie();
                        movie.setId(entry.getMovieid());
                        movie.setOverview(entry.getOverview());
                        movie.setOriginalTitle(entry.getTitle());
                        movie.setPosterPath(entry.getPoster());
                        movie.setVoteAverage(entry.getRating());
                        movie.setBackdropPath(entry.getBackdrop());
                        movie.setReleaseDate(entry.getReleaseDate());
                        movieData.add(movie);
                    }
                    updateAdapterData(movieData);
                }


            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(LIST_STATE, movieData);

        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieData = savedInstanceState.getParcelableArrayList(LIST_STATE);
        savedRecyclerViewLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
    }

    private void setupSharedPreferences(SharedPreferences sharedPreferences) {


        String prefValue = sharedPreferences.getString(this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_sort_order_popular));
        if (prefValue.equals(this.getString(R.string.pref_sort_order_popular))) {
            Log.e("pop", "pop");

            loadJSONMostPopular();
        } else if (prefValue.equals(this.getString(R.string.pref_sort_order_top_rated))) {

            loadJSONTopRated();
            Log.e("top", "top");

        } else {

            displayFavorites();
            Log.e("dis", "dis");

        }

    }

    public void updateAdapterData(ArrayList<Movie> movieData) {
        adapter.updateMovieList(movieData);

    }


    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);

    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override public void run() {

                refreshLayout.setRefreshing(false);
                setupSharedPreferences(sharedPreferences);

            }

        }, 2000);

    }
}
