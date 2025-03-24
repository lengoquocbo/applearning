package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apphoctap.model.Assignment

@Dao
interface AssignmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(LAssignment : List<Assignment>)

    @Update
    suspend fun updateAssignment(Assignment : Assignment)

    @Delete
    suspend fun deleteAssignment(Assignment : Assignment)

    @Query("SELECT * FROM assignment WHERE classID = :classId")
    suspend fun getAssignmentsByClass(classId: String): List<Assignment>

}