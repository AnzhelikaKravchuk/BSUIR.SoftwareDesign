package com.example.yura.androidlabs.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String email;

    public String password;

    public String pathToPhoto;
}
