package com.example.apphoctap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apphoctap.database.dao.*
import com.example.apphoctap.model.*

@Database(
    entities = [
        Submission::class, Teacher::class, Student::class, Assignment::class, ClassStudent::class,
        Flashcard::class, MinitestResult::class, MinitestQuestion::class, MinitestAnswer::class,
        StudentAnswer::class, Minitest::class, Classes::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun submissionDao(): SubmissionDao
    abstract fun teacherDao(): TeacherDao
    abstract fun studentDao(): StudentDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun classStudentDao(): ClassStudentDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun miniTestResultDao(): MinitestResultDao
    abstract fun miniTestQuestionDao(): MinitestQuestionDao
    abstract fun miniTestAnswerDao(): MinitestAnswerDao
    abstract fun studentAnswerDao(): StudentAnswerDao
    abstract fun miniTestDao(): MinitestDao
}
