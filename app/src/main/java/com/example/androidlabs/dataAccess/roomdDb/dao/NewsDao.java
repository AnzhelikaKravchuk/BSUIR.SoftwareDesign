package com.example.androidlabs.dataAccess.roomdDb.dao;

import com.example.androidlabs.dataAccess.roomdDb.entities.NewsItem;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NewsDao {
    @Insert
    public void updateItemsCache(List<NewsItem> feedItems);

    @Query("SELECT * FROM NewsItem ORDER BY id")
    public List<NewsItem> getItemsFromCache();

    @Query("SELECT * FROM NewsItem ORDER BY id LIMIT 1")
    public NewsItem getFirstCacheItem();

    @Query("DELETE FROM NewsItem")
    public void removeDeprecatedItems();
}
