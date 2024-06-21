package com.appdevelopement.passinggrade.database

import com.appdevelopement.passinggrade.models.*
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appdevelopement.passinggrade.dao.*

@Database(entities = [Teacher::class, Student::class, Course::class, Exam::class, TeacherCourse::class, Compentence::class, CompetenceGrade::class, ExamStudentCrossRef::class], version = 19)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teacherDao(): TeacherDao
    abstract fun studentDao(): StudentDao
    abstract fun courseDao(): CourseDao
    abstract fun examDao(): ExamDao
    abstract fun teacherCourseDao(): TeacherCourseDao
    abstract fun compentenceDao(): CompentenceDao
    abstract fun CompentenceGradeDao():CompentenceGradeDao
    abstract fun examStudentCrossReference(): ExamStudentCorssReferecne

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app-dev"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
