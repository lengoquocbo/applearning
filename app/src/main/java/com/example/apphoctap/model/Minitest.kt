package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
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
    val minitestID: Int,
    val assignmentID: Int,
    val title: String,
    val duration: Int,
    val createdAt: String
) : Parcelable
