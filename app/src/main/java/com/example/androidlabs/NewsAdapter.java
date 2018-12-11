package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.androidlabs.businessLogic.News;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.AdapterViewHolder> {
    public List<News> getFeedItems() {
        return feedItems;
    }

    private List<News> feedItems;
    private Context context;

    public NewsAdapter(Context context, List<News> feedItems) {
        this.feedItems = feedItems;
        this.context = context;
    }

    public void pushNews(News feedItem, int position){
        feedItems.add(position, feedItem);
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rss_element,parent,false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        final News feedItem = feedItems.get(position);
        ((TextView)holder.itemView.findViewById(R.id.cardTitleTextView)).setText(feedItem.title);
        ((TextView)holder.itemView.findViewById(R.id.cardPubDateTextView)).setText(feedItem.pubDate);


        ((TextView)holder.itemView.findViewById(R.id.cardTextTextView)).setText(Html.fromHtml(feedItem.description));


        Glide.with(context).load(feedItem.thumbnailUrl).into((ImageView)holder.itemView.findViewById(R.id.cardImageTextView));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String linkToItem = feedItem.link;
                NewsLoaderFragmentDirections.ActionRssReaderFragmentToNewsDetails action =
                        new NewsLoaderFragmentDirections.ActionRssReaderFragmentToNewsDetails(linkToItem);
                onItemClick.navigateToDetails(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {
        AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private OnItemClick onItemClick;

    public interface OnItemClick{
        void navigateToDetails(
                NewsLoaderFragmentDirections.ActionRssReaderFragmentToNewsDetails action
        );
    }

    public void setOnItemCLick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
}