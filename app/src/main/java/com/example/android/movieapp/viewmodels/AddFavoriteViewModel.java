package com.example.android.movieapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.movieapp.data.AppDatabase;
import com.example.android.movieapp.data.Favorite;

/**
 * Created by ahmed on 24/09/18.
 */

public class AddFavoriteViewModel extends ViewModel {

    private LiveData<Favorite> favorite;

    public AddFavoriteViewModel(AppDatabase database, int favoriteId) {
        favorite = database.favoriteDao().loadFavoriteById(favoriteId);
    }

    public LiveData<Favorite> getFavorite() {
        return favorite;
    }
}
