package com.example.apphoctap.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize


@Entity(
    tableName = "Assignment",
    primaryKeys = ["assignmentID"],
    foreignKeys = [
        ForeignKey(
            entity = Classes::class,
            parentColumns = ["classID"],
            childColumns = ["classID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class Assignment  (
    val assignmentID:Int,
    val classID: String,
    val title: String,
    val type:String,
    val createAT:String,
    val limited:String
) : Parcelable
