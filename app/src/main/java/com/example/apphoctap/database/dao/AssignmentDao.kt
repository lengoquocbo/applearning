package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apphoctap.model.Assignment
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(LAssignment : List<Assignment>)

    @Update
    suspend fun updateAssignment(Assignment : Assignment)

    @Delete
    suspend fun deleteAssignment(Assignment : Assignment)

    @Query("SELECT * FROM assignment WHERE classID = :classId")
    fun getAssignmentsByClass(classId: String): Flow<List<Assignment>>

    @Query("SELECT * FROM assignment WHERE assignmentID = :assignmentId")
    suspend fun getAssignmentById(assignmentId: Int): Assignment?

}