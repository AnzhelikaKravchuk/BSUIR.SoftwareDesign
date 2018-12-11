package com.example.androidlabs.businessLogic;

import java.util.Date;

public class News {
    public String title;
    public String link;
    public String description;
    public String pubDate; // set as Date
    public String thumbnailUrl;

    public News(){

    }

    public News(String title, String link, String description, String pubDate, String thumbnailUrl) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.thumbnailUrl = thumbnailUrl;
    }
}
