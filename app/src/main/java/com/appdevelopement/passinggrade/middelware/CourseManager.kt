package com.appdevelopement.passinggrade.middleware
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
        return withContext(Dispatchers.IO) {
            try {
                val courseId = dao.insertCourses(course)
                course.copy(idCourse = courseId.toInt())
            } catch (e: Exception) {
                // Handle the exception (e.g., log it, rethrow it, or return a default value)
                throw e
            }
        }
    }
}