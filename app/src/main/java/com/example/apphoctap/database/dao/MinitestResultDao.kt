package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apphoctap.model.MinitestResult

@Dao
interface MinitestResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMinitestResult(result: MinitestResult)

    @Query("SELECT * FROM minitestresult WHERE minitestID = :minitestId")
    suspend fun getResultsByMinitest(minitestId: String): List<MinitestResult>
}