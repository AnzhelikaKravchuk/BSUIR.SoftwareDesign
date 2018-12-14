package com.example.androidlabs.dataAccess.roomdDb.entities;
import com.example.androidlabs.businessLogic.models.News;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewsItem {

    public NewsItem(){

    }

    public NewsItem(News feedItem) {
        this.title = feedItem.title;
        this.link = feedItem.link;
        this.description = feedItem.description;
        this.publicationDate = feedItem.publicationDate;
        this.thumbnailUrl = feedItem.thumbnailUrl;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String title;
    public String link;
    public String description;
    public String publicationDate;
    public String thumbnailUrl;

    public News toFeedItem(){
        return new News(
                this.title,
                this.link,
                this.description,
                this.publicationDate,
                this.thumbnailUrl
        ) ;

    }

}
