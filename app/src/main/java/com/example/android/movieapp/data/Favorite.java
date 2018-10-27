package com.example.android.movieapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.icu.text.Replaceable;
import android.support.annotation.NonNull;

/**
 * Created by ahmed on 24/09/18.
 */
@Entity(tableName = "favorite")
public class Favorite {


    @PrimaryKey()
    private int movieid;

    private String title;

    private String rating;

    private String poster;

    private String overview;

    private String backdrop;

    private String releaseDate;


    public Favorite(int movieid, String title, String rating, String poster, String overview, String backdrop, String releaseDate) {
        this.movieid = movieid;
        this.title = title;
        this.rating = rating;
        this.poster = poster;
        this.overview = overview;
        this.backdrop = backdrop;
        this.releaseDate = releaseDate;
    }

//    public Favorite(@NonNull int id, int movieid, String title, String rating, String poster, String overview, String backdrop, String releaseDate) {
//        this.id = id;
//        this.movieid = movieid;
//        this.title = title;
//        this.rating = rating;
//        this.poster = poster;
//        this.overview = overview;
//        this.backdrop = backdrop;
//        this.releaseDate = releaseDate;
//    }

    @Ignore
    public Favorite() {
    }

//    @NonNull
//    public int getId() {
//        return id;
//    }
//
//    public void setId(@NonNull int id) {
//        this.id = id;
//    }

    public int getMovieid() {
        return movieid;
    }

    public void setMovieid(int movieid) {
        this.movieid = movieid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
