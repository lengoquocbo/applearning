package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.apphoctap.enums.Submit
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "submission",
    primaryKeys = ["submitID"],
    foreignKeys = [
        ForeignKey(
            entity = Assignment::class,
            parentColumns = ["assignmentID"],
            childColumns = ["assignmentID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Student::class,
            parentColumns = ["studentID"],
            childColumns = ["studentID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Submission(
    val submisID: Int,
    val studentID: String,
    val assignmentID: Int,
    val fileSub: String,
    val score: Int,
    val submitAt: String,
    val feedBack: String,
    val status: Submit,
    val isSynced : Boolean = false
): Parcelable



