package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize


@Entity(
    tableName = "classstudent",
    primaryKeys = ["classStudentID"],
    foreignKeys = [
        ForeignKey(
            entity = Classes::class,
            parentColumns = ["classID"],
            childColumns = ["classID"],
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
@Parcelize
data class ClassStudent(
    val classStudentID: String,
    val classID: String,
    val studentID: String
): Parcelable