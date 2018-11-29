package com.example.androidlabs.roomdDb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidlabs.roomdDb.entities.UserAdditionalInfo;

@Dao
public interface UserAdditionalInfoDao {

    @Insert
    void addUserAdditionalInfo(UserAdditionalInfo userAdditionalInfo);

    @Query("SELECT * FROM UserAdditionalInfo WHERE uid = :uid")
    UserAdditionalInfo getUserAdditionalInfo(String uid);

    @Update
    void updateUserAdditionalInfo(UserAdditionalInfo userAdditionalInfo);

}
