package com.example.apphoctap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apphoctap.database.dao.*
import com.example.apphoctap.model.*

@Database(
    entities = [
        Submission::class, Teacher::class, Student::class, Assignment::class, ClassStudent::class,
        Flashcard::class, Classes::class
    ],
    version = 3,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun submissionDao(): SubmissionDao
    abstract fun teacherDao(): TeacherDao
    abstract fun studentDao(): StudentDao
    abstract fun classStudentDao(): ClassStudentDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun assignmentDao(): AssignmentDao
}
