package com.appdevelopement.passinggrade.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.appdevelopement.passinggrade.dao.CompetenceDao
import com.appdevelopement.passinggrade.dao.CompetenceGradeDao
import com.appdevelopement.passinggrade.dao.CourseDao
import com.appdevelopement.passinggrade.dao.ExamDao
import com.appdevelopement.passinggrade.dao.ExamStudentCrossReferenceDao // Fixed typo
import com.appdevelopement.passinggrade.dao.StudentDao
import com.appdevelopement.passinggrade.dao.TeacherCourseDao
import com.appdevelopement.passinggrade.dao.TeacherDao
import com.appdevelopement.passinggrade.models.Competence // Fixed typo
import com.appdevelopement.passinggrade.models.CompetenceGrade
import com.appdevelopement.passinggrade.models.Course
import com.appdevelopement.passinggrade.models.Exam
import com.appdevelopement.passinggrade.models.ExamStudentCrossRef
import com.appdevelopement.passinggrade.models.Student
import com.appdevelopement.passinggrade.models.Teacher
import com.appdevelopement.passinggrade.models.TeacherCourse

@Database(
    entities = [
        Teacher::class,
        Student::class,
        Course::class,
        Exam::class,
        TeacherCourse::class,
        Competence::class, // Fixed typo
        CompetenceGrade::class,
        ExamStudentCrossRef::class
    ],
    version = 21
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun teacherDao(): TeacherDao

    abstract fun studentDao(): StudentDao

    abstract fun courseDao(): CourseDao

    abstract fun examDao(): ExamDao

    abstract fun teacherCourseDao(): TeacherCourseDao

    abstract fun competenceDao(): CompetenceDao // Fixed typo

    abstract fun competenceGradeDao(): CompetenceGradeDao // Fixed method name

    abstract fun examStudentCrossReferenceDao(): ExamStudentCrossReferenceDao // Fixed typo


    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        // Define migration from version 1 to 2
//        val MIGRATION_20_21 = object : Migration(20, 21) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Add the new 'isGraded' column, default to false for existing rows
//                database.execSQL("ALTER TABLE ExamStudentCrossRef ADD COLUMN isGraded INTEGER NOT NULL DEFAULT 0")
////                database.execSQL("""
////                CREATE TABLE IF NOT EXISTS `Competence` (
////                    `idCompetence` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
////                    `idExam` INTEGER NOT NULL,
////                    `dtName` TEXT NOT NULL,
////                    `dtCompetenceWeight` INTEGER NOT NULL,
////                    `dtMustPass` INTEGER NOT NULL,
////                    FOREIGN KEY(`idExam`) REFERENCES `Exam`(`idExam`) ON DELETE CASCADE
////                    )
////                """)
//            }
//        }

        // Get the database instance
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app-dev"
                )
                    .fallbackToDestructiveMigration() // Keep this if you want destructive migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


//package com.appdevelopement.passinggrade.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.room.migration.Migration
//import androidx.sqlite.db.SupportSQLiteDatabase
//import com.appdevelopement.passinggrade.dao.CompetenceDao
//import com.appdevelopement.passinggrade.dao.CompetenceGradeDao
//import com.appdevelopement.passinggrade.dao.CourseDao
//import com.appdevelopement.passinggrade.dao.ExamDao
//import com.appdevelopement.passinggrade.dao.ExamStudentCrossReferenceDao
//import com.appdevelopement.passinggrade.dao.StudentDao
//import com.appdevelopement.passinggrade.dao.TeacherCourseDao
//import com.appdevelopement.passinggrade.dao.TeacherDao
//import com.appdevelopement.passinggrade.models.Competence
//import com.appdevelopement.passinggrade.models.CompetenceGrade
//import com.appdevelopement.passinggrade.models.Course
//import com.appdevelopement.passinggrade.models.Exam
//import com.appdevelopement.passinggrade.models.ExamStudentCrossRef
//import com.appdevelopement.passinggrade.models.Student
//import com.appdevelopement.passinggrade.models.Teacher
//import com.appdevelopement.passinggrade.models.TeacherCourse
//
//@Database(
//    entities =
//        [
//            Teacher::class,
//            Student::class,
//            Course::class,
//            Exam::class,
//            TeacherCourse::class,
//            Competence::class,
//            CompetenceGrade::class,
//            ExamStudentCrossRef::class],
//    version = 20)
//abstract class AppDatabase : RoomDatabase() {
//
//  abstract fun teacherDao(): TeacherDao
//
//  abstract fun studentDao(): StudentDao
//
//  abstract fun courseDao(): CourseDao
//
//  abstract fun examDao(): ExamDao
//
//  abstract fun teacherCourseDao(): TeacherCourseDao
//
//  abstract fun competenceDao(): CompetenceDao
//
//  abstract fun CompetenceGradeDao(): CompetenceGradeDao
//
//  abstract fun examStudentCrossReference(): ExamStudentCrossReferenceDao
//
//
//
//    companion object {
//    @Volatile private var INSTANCE: AppDatabase? = null
//
//         val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Add the new 'isGraded' column, default to false for existing rows
//                database.execSQL("ALTER TABLE ExamStudentCrossRef ADD COLUMN isGraded INTEGER NOT NULL DEFAULT 0")
//            }
//        }
//        val MIGRATION_1_: Migration = MIGRATION_1_2
//    fun getDatabase(context: Context): AppDatabase {
//      return INSTANCE
//          ?: synchronized(this) {
//            val instance =
//                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app-dev")
//                    .fallbackToDestructiveMigration()
//                    .build()
//            INSTANCE = instance
//            instance
//          }
//    }
//  }
//}
