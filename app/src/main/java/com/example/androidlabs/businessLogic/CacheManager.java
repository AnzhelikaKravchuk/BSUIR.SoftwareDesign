package com.example.androidlabs.businessLogic;

import com.example.androidlabs.dataAccess.roomdDb.AppDatabase;
import com.example.androidlabs.dataAccess.roomdDb.dao.NewsDao;
import com.example.androidlabs.dataAccess.roomdDb.entities.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class CacheManager {
    private static CacheManager instance;
    private NewsDao appFeedItemsDatabase;

    public static CacheManager getInstance(){
        return instance;
    }

    public CacheManager(AppDatabase appFeedItemsDatabase){
        this.appFeedItemsDatabase = appFeedItemsDatabase.feedItemDao();

        instance = this;
    }

    public void updateFeedItemsCache(ArrayList<News> feedItems){
        appFeedItemsDatabase.removeDeprecatedItems();

        ArrayList<NewsItem> feedCacheItems =
                new ArrayList<>();
        for(int i=0; i < (feedItems.size() < 10 ? feedItems.size(): 10); i++){
            feedCacheItems.add(new NewsItem(feedItems.get(i)));
        }

        appFeedItemsDatabase.updateItemsCache(feedCacheItems);
    }

    public ArrayList<News> getItemsFromCache(){
        List<NewsItem> feedCacheItems
                = appFeedItemsDatabase.getItemsFromCache();

        ArrayList<News> feedItems = new ArrayList<>();
        for(int i = 0; i < feedCacheItems.size(); i++){
            feedItems.add(feedCacheItems.get(i).toFeedItem());
        }

        return feedItems;
    }

    public void removeCache(){
        appFeedItemsDatabase.removeDeprecatedItems();
    }
}
