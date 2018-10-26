package com.example.android.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmed on 28/09/18.
 */

public class ReviewResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<Review> results;

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("total_results")
    private Integer totalResults;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
