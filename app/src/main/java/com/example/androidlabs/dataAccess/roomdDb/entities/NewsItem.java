package com.example.androidlabs.dataAccess.roomdDb.entities;
import com.example.androidlabs.businessLogic.News;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class NewsItem {

    public NewsItem(){

    }

    public NewsItem(News feedItem) {
        this.title = feedItem.title;
        this.link = feedItem.link;
        this.description = feedItem.description;
        this.pubDate = feedItem.pubDate;
        this.thumbnailUrl = feedItem.thumbnailUrl;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String title;
    public String link;
    public String description;
    public String pubDate;
    public String thumbnailUrl;

    public News toFeedItem(){
        return new News(
                this.title,
                this.link,
                this.description,
                this.pubDate,
                this.thumbnailUrl
        ) ;

    }

}
