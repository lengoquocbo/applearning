package com.example.apphoctap.model

import java.time.LocalDateTime
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "minitests",
    primaryKeys = ["minitestID"],
    foreignKeys = [
        ForeignKey(
            entity = Assignment::class,
            parentColumns = ["assignmentID"],
            childColumns = ["assignmentID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Minitest(
    @PrimaryKey val minitestsID: Int,
    val assignmentId: Int,
    val title: String,
    val duration: Int,
    val createdAt: LocalDateTime
) : Parcelable
