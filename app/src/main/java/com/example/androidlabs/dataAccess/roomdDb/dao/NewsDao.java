package com.example.androidlabs.dataAccess.roomdDb.dao;

import com.example.androidlabs.dataAccess.roomdDb.entities.NewsItem;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

//Represents Data Access Object for Storaging Cache
@Dao
public interface NewsDao {
    @Insert
    public void updateCache(List<NewsItem> feedItems);

    @Query("SELECT * FROM NewsItem ORDER BY id")
    public List<NewsItem> getCache();

    @Query("SELECT * FROM NewsItem ORDER BY id LIMIT 1")
    public NewsItem getFirstCacheItem();

    @Query("DELETE FROM NewsItem")
    public void removeOldCache();
}
