package com.example.inventory.data

// UserDao.kt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_table WHERE username = :username")
    suspend fun getUser(username: String): User?
}
