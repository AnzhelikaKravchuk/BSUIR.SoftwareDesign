package com.example.androidlabs.dataAccess.roomdDb.dao;

import com.example.yura.androidlabs.room_db.entities.FeedItem;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FeedItemDao {
    @Insert
    public void updateItemsCache(List<FeedItem> feedItems);

    @Query("SELECT * FROM FeedItem ORDER BY id")
    public List<FeedItem> getItemsFromCache();

    @Query("SELECT * FROM FeedItem ORDER BY id LIMIT 1")
    public FeedItem getFirstCacheItem();

    @Query("DELETE FROM FeedItem")
    public void removeDeprecatedItems();
}
