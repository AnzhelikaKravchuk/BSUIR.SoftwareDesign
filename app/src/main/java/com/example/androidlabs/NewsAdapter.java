package com.example.androidlabs;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.androidlabs.businessLogic.UserManagementService;
import com.bumptech.glide.Glide;

import com.example.androidlabs.businessLogic.models.News;
import com.example.androidlabs.businessLogic.models.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.AdapterViewHolder> {
    public List<News> getFeedItems() {
        return feedItems;
    }

    private ArrayList<News> feedItems;
    private Context context;

    public NewsAdapter(Context context, ArrayList<News> feedItems) {
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
        UserManagementService userManager = UserManagementService.getInstance();
        User user = userManager.getCurrentUser();
        final News feedItem = feedItems.get(position);
        ((TextView)holder.itemView.findViewById(R.id.cardTitleTextView)).setText(feedItem.title);
        ((TextView)holder.itemView.findViewById(R.id.cardPubDateTextView)).setText(feedItem.publicationDate);

        if (user.rssNewsUrl.contains("news.google.com"))
            ((TextView)holder.itemView.findViewById(R.id.cardTextTextView)).setText(Html.fromHtml(feedItem.description));
        else
            ((TextView)holder.itemView.findViewById(R.id.cardTextTextView)).setText(feedItem.description);


        if (feedItem.thumbnailUrl !=null)
            Glide.with(context).load(feedItem.thumbnailUrl).into((ImageView)holder.itemView.findViewById(R.id.cardImageTextView));

        try {
            URL url = new URL(feedItem.link);
            String a = "https://".concat(url.getAuthority().concat("/favicon.ico"));
            Glide.with(context).load(a).into((ImageView)holder.itemView.findViewById(R.id.favicon));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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