package com.example.androidlabs.dataAccess.roomdDb.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserAdditionalInfo {

    @PrimaryKey
    @NonNull
    public String uid;

    public String name;

    public String surname;

    public String phoneNumber;

    public String pathToPhoto;

    public String rssNewsUrl = null;
}