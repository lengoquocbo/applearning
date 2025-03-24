package com.example.apphoctap.model

import java.time.LocalDateTime
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity (
    tableName = "minitestResult",
    primaryKeys = ["resultId"],
    foreignKeys = [
        ForeignKey(
            entity = Minitest::class,
            parentColumns = ["minitestID"],
            childColumns = ["minitestID"],
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
data class MinitestResult(
    val resultId : Int,
    val minitestId : Int,
    val studentId : Int,
    val submittedAt : LocalDateTime
) : Parcelable