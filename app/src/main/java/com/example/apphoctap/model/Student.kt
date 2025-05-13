package com.example.apphoctap.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "student")
@Parcelize
data class Student (
    @PrimaryKey val studentID:String,
    val userID: String,
    val name:String,
) : Parcelable

@Parcelize
data class StudentRequest(
    val email: String
): Parcelable
@Parcelize
data class StudentDeleteRequest(
    val studentID : String,
): Parcelable

@Parcelize
data class StudentResponse(
    val classID: String,
    val studentID: String,
    val studentname: String,
) : Parcelable

@Parcelize
data class StudentResponseWithEnrollMentKey(
    val userId : String,
    val classID: String,
    val studentID: String,
    val studentname: String,
    val enrollmentKey: String
) : Parcelable
@Parcelize
data class AddStudentRequest(
    val email: String,
    val classID: String
): Parcelable


@Parcelize
data class DeleteStudentRequest(
    val studentID: String,
    val classID: String
):Parcelable

@Parcelize
data class DeleteStudentResponse(
    val userId : String,
    val enrollmentKey: String,
    val message : String
) : Parcelable

