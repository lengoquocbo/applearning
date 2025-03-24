package com.example.apphoctap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.apphoctap.database.dao.AssignmentDao
import com.example.apphoctap.database.dao.ClassStudentDao
import com.example.apphoctap.database.dao.FlashcardDao
import com.example.apphoctap.database.dao.MinitestAnswerDao
import com.example.apphoctap.database.dao.MinitestDao
import com.example.apphoctap.database.dao.MinitestQuestionDao
import com.example.apphoctap.database.dao.MinitestResultDao
import com.example.apphoctap.database.dao.StudentAnswerDao
import com.example.apphoctap.database.dao.StudentDao
import com.example.apphoctap.database.dao.SubmissionDao
import com.example.apphoctap.database.dao.TeacherDao
import com.example.apphoctap.model.Student
import com.example.apphoctap.model.Submission
import com.example.apphoctap.model.Teacher
import com.example.apphoctap.model.Assignment
import com.example.apphoctap.model.ClassStudent
import com.example.apphoctap.model.Flashcard
import com.example.apphoctap.model.MinitestResult
import com.example.apphoctap.model.MinitestQuestion
import com.example.apphoctap.model.MinitestAnswer
import com.example.apphoctap.model.StudentAnswer
import com.example.apphoctap.model.Minitest

@Database(
    entities = [
        Submission::class, Teacher::class, Student::class, Assignment::class, ClassStudent::class,
        Flashcard::class, MinitestResult::class, MinitestQuestion::class, MinitestAnswer::class,
        StudentAnswer::class, Minitest::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    // Khai bao cac DAO
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

    companion object {
        /* Annotation Volatile dùng để đồng bộ hóa dữ liệu giữa các luồng từ đó tránh
           sự xung đột dữ liệu khi truy vấn DB */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //Sử dụng để khởi tạo và cung cấp 1 instance duy nhất của Appdatabase
        fun getDatabase(context: Context): AppDatabase {
            //Kiểm tra xem database đã khởi tạo chưa
            return INSTANCE ?: synchronized(this) { //synchronized(this) đảm bảo chỉ có 1 luồng được khởi tạo db
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Dùng Application Context để tránh rò rỉ bộ nhớ
                    AppDatabase::class.java, // Database class we created
                    "app_database" // Database Name
                ).build()
                INSTANCE = instance // Khởi tạo instance để sử dụng lại
                instance // Trả về instance cho lần gọi đầu tiên
            }
        }
    }
}