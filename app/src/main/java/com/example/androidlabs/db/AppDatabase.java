package com.example.yura.androidlabs.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.yura.androidlabs.db.entities.User;

import com.example.yura.androidlabs.db.dao.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
