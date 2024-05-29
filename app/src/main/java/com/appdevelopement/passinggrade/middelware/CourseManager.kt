
package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CourseManager {
    private val course = Course(
        idCourse = 0,
        dtTitle = "OOP1",
        dtDescription = "Object-Oriented Programming"
    )

    suspend fun addCourse(context: Context): Course {
        val dao = AppDatabase.getDatabase(context).courseDao()
        val courseId = withContext(Dispatchers.IO) {
            dao.insertCourses(course)
        }
        return course.copy(idCourse = courseId.toInt())
    }
}