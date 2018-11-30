package com.example.androidlabs.dataAccess.roomdDb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidlabs.dataAccess.roomdDb.entities.UserAdditionalInfo;

@Dao
public interface UserAdditionalInfoDao {

    @Insert
    public void addUserAdditionalInfo(UserAdditionalInfo userAdditionalInfo);

    @Query("SELECT * FROM UserAdditionalInfo WHERE uid = :uid")
    public UserAdditionalInfo getUserAdditionalInfo(String uid);

    @Update
    public void updateUserAdditionalInfo(UserAdditionalInfo userAdditionalInfo);


}
