package com.example.yura.androidlabs.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.yura.androidlabs.db.entities.User;

@Dao
public interface UserDao {

    @Insert
    public void addUser(User user);

    @Query("SELECT * FROM user WHERE id = 1")
    public User getUser();

    @Update
    public void updateUser(User user);

}
