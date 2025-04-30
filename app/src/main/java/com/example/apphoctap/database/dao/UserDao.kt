package com.example.apphoctap.database.dao

import androidx.room.*
import com.example.apphoctap.database.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getLoggedInUser(): UserEntity?

    @Query("DELETE FROM user")
    suspend fun logout()
}