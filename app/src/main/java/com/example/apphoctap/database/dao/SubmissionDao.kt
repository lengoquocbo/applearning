package com.example.apphoctap.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apphoctap.model.Submission

@Dao
interface SubmissionDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission : Submission)

    @Update
    suspend fun updateSubmission(submission: Submission)

    @Delete
    suspend fun deleteSubmission(submission: Submission)

    @Query("SELECT * FROM submission")
    suspend fun selectAllSubmission() : List<Submission>

    @Query("SELECT * FROM submission WHERE studentID = :studentID")
    suspend fun selectSubByStudentID(studentID : String) : List<Submission>
}