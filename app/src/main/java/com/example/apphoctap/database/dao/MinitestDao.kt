package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apphoctap.model.Minitest

@Dao
interface MinitestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMinitest(minitest: Minitest)

    @Query("SELECT * FROM minitests WHERE assignmentID = :assignmentId")
    suspend fun getMinitestsByAssignment(assignmentId: String): List<Minitest>
}