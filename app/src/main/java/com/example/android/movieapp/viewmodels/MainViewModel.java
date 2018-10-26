package com.example.android.movieapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.movieapp.data.AppDatabase;
import com.example.android.movieapp.data.Favorite;

import java.util.List;

/**
 * Created by ahmed on 24/09/18.
 */

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Favorite>> favorite;

    public MainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        favorite= database.favoriteDao().loadAllFavorite();
    }

    public LiveData<List<Favorite>> getFavorite() {
        return favorite;
    }
}
