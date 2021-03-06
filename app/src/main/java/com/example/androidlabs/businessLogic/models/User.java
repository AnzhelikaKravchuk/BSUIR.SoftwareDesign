package com.example.androidlabs.dataAccess.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@IgnoreExtraProperties
public class User {
    public String  uid;
    public String name;
    public String surname;
    public String email;
    public String pathToPhoto;
    public String phoneNumber;
    public String rssNewsUrl;


    public User(){

    }

    public User(
            String uid,
            String name, String surname,
            String email,
            String phoneNumber, String pathToPhoto,
            String rssNewsUrl
    ) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pathToPhoto = pathToPhoto;
        this.rssNewsUrl = rssNewsUrl;
    }

}