package com.example.apphoctap.database.dao

import androidx.room.*
import com.example.apphoctap.model.Submission

@Dao
public interface SubmissionDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission : Submission)

    @Update
    suspend fun updateSubmission(submission: Submission)

    @Delete
    suspend fun deleteSubmission(submission: Submission)

}