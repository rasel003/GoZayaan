package com.rasel.androidbaseapp.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rasel.androidbaseapp.cache.entities.CURRENT_USER_ID
import com.rasel.androidbaseapp.cache.entities.User

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun upsert(user: User) : Long

    @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
    fun getuser() : LiveData<User>

    @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
    fun getUserInfo() : User?

}
