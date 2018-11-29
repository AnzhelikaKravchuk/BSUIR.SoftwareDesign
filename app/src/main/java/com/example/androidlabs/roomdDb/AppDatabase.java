package com.example.androidlabs.roomdDb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.androidlabs.roomdDb.entities.UserAdditionalInfo;

import com.example.androidlabs.roomdDb.dao.UserAdditionalInfoDao;

@Database(entities = {UserAdditionalInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserAdditionalInfoDao userDAdditionalInfo();
}
