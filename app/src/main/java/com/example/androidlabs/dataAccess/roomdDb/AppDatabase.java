package com.example.androidlabs.dataAccess.roomdDb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.androidlabs.dataAccess.roomdDb.entities.UserAdditionalInfo;

import com.example.androidlabs.dataAccess.roomdDb.dao.UserAdditionalInfoDao;

@Database(entities = {UserAdditionalInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserAdditionalInfoDao userDAdditionalInfo();
}
