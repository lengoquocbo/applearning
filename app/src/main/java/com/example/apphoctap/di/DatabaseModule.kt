package com.example.apphoctap.di

import android.content.Context
import androidx.room.Room
import com.example.apphoctap.database.AppDatabase
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn()
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatbase(@ApplicationContext context: Context) : AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides fun provideSubmissionDao(db: AppDatabase): SubmissionDao = db.submissionDao()
    @Provides fun provideTeacherDao(db: AppDatabase): TeacherDao = db.teacherDao()
    @Provides fun provideStudentDao(db: AppDatabase): StudentDao = db.studentDao()
    @Provides fun provideAssignmentDao(db: AppDatabase): AssignmentDao = db.assignmentDao()
    @Provides fun provideClassStudentDao(db: AppDatabase): ClassStudentDao = db.classStudentDao()
    @Provides fun provideFlashcardDao(db: AppDatabase): FlashcardDao = db.flashcardDao()
    @Provides fun provideMinitestDao(db: AppDatabase): MinitestDao = db.miniTestDao()
    @Provides fun provideMinitestResultDao(db: AppDatabase): MinitestResultDao = db.miniTestResultDao()
    @Provides fun provideMinitestQuestionDao(db: AppDatabase): MinitestQuestionDao = db.miniTestQuestionDao()
    @Provides fun provideMinitestAnswerDao(db: AppDatabase): MinitestAnswerDao = db.miniTestAnswerDao()
    @Provides fun provideStudentAnswerDao(db: AppDatabase): StudentAnswerDao = db.studentAnswerDao()
}