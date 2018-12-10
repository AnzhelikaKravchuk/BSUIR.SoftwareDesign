package com.example.yura.androidlabs.managers;

import com.example.yura.androidlabs.room_db.AppDatabase;
import com.example.yura.androidlabs.room_db.dao.FeedItemDao;
import com.example.yura.androidlabs.rss_reader.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class CacheManager {
    private static CacheManager instance;
    private FeedItemDao appFeedItemsDatabase;

    public static CacheManager getInstance(){
        return instance;
    }

    public CacheManager(AppDatabase appFeedItemsDatabase){
        this.appFeedItemsDatabase = appFeedItemsDatabase.feedItemDao();

        instance = this;
    }

    public void updateFeedItemsCache(ArrayList<FeedItem> feedItems){
        appFeedItemsDatabase.removeDeprecatedItems();

        ArrayList<com.example.yura.androidlabs.room_db.entities.FeedItem> feedCacheItems =
                new ArrayList<>();
        for(int i=0; i < (feedItems.size() < 10 ? feedItems.size(): 10); i++){
            feedCacheItems.add(new com.example.yura.androidlabs.room_db.entities.FeedItem(
                    feedItems.get(i)
            ));
        }

        appFeedItemsDatabase.updateItemsCache(feedCacheItems);
    }

    public ArrayList<FeedItem> getItemsFromCache(){
        List<com.example.yura.androidlabs.room_db.entities.FeedItem> feedCacheItems
                = appFeedItemsDatabase.getItemsFromCache();

        ArrayList<FeedItem> feedItems = new ArrayList<>();
        for(int i = 0; i < feedCacheItems.size(); i++){
            feedItems.add(feedCacheItems.get(i).toFeedItem());
        }

        return feedItems;
    }

    public void removeCache(){
        appFeedItemsDatabase.removeDeprecatedItems();
    }
}
