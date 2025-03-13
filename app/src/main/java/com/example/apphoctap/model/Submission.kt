package com.example.apphoctap.model

data class Submission(
    val submisID: Int,
    val studentID: Student,
    val assignmentID: Assignment,
    val fileSub: String,
    val score: Int,
    val submisAt: String,
    val feedBack: String
)
