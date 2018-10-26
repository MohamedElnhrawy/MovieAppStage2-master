package com.example.android.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapp.R;
import com.example.android.movieapp.model.Review;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolde> {

    private Context mContext;
    private List<Review> reviewList;

    public ReviewAdapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolde(view);
    }

    public void setReviewList(List<Review> reviewList) {

        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolde holder, int position) {
        holder.autherText.setText(reviewList.get(position).getAuthor());
        holder.contentText.setText(reviewList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewViewHolde extends RecyclerView.ViewHolder {
        TextView autherText, contentText;

        public ReviewViewHolde(View itemView) {
            super(itemView);

            autherText = (TextView) itemView.findViewById(R.id.auther_review_tv);
            contentText = (TextView) itemView.findViewById(R.id.content_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String mUrl = reviewList.get(position).getUrl();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
