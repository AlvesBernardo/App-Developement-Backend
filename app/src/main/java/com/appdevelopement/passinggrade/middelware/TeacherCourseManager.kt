package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.TeacherCourse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

object TeacherCourseManager {
    // Here you can initialize your TeacherCourse objects or receive them as parameters in the function

    private val teacherCourse1 = TeacherCourse(
        idTeacher = 1,
        idCourse = 1
    )

    suspend fun addTeacherCourse(context: Context) {
        val dao = AppDatabase.getDatabase(context).teacherCourseDao() // Ensure you have a DAO (Data Access Object) for TeacherCourse
        withContext(IO) {
            dao.insertTeacherCourse(teacherCourse1) // Ensure you have a method named "insertTeacherCourse"
        }
    }
}