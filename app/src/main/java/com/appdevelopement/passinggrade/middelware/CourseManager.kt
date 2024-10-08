package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CourseManager {
  private val course =
      Course(idCourse = 0, dtTitle = "OOP2", dtDescription = "Object-Oriented Programming 2")
  private val course2 =
      Course(idCourse = 0, dtTitle = "AppDev", dtDescription = "Native Programming")

  private val course3 = Course(idCourse = 0, dtTitle = "DataProcess", dtDescription = "Data Processing")
  private val course4 =
      Course(idCourse = 0, dtTitle = "DbEng", dtDescription = "Database Engineering")
  
  suspend fun addCourse(context: Context) {
    val dao = AppDatabase.getDatabase(context).courseDao()
    withContext(Dispatchers.IO) {
      try {
        val courseId = dao.insertCourses(course)
        course.copy(idCourse = courseId.toInt())
        val courseId2 = dao.insertCourses(course2)
        course2.copy(idCourse = courseId2.toInt())
        val courseId3 = dao.insertCourses(course3)
        course3.copy(idCourse = courseId3.toInt())
        val courseId4 = dao.insertCourses(course4)
        course4.copy(idCourse = courseId4.toInt())
      } catch (e: Exception) {
        // Handle the exception (e.g., log it, rethrow it, or return a default value)
        throw e
      }
    }
  }
  
//  suspend fun addCourse(context: Context): Course {
//    val dao = AppDatabase.getDatabase(context).courseDao()
//    return withContext(Dispatchers.IO) {
//      try {
//        val courseId = dao.insertCourses(course)
//        course.copy(idCourse = courseId.toInt())
//        val courseId2 = dao.insertCourses(course2)
//        course2.copy(idCourse = courseId2.toInt())
//        val courseId3 = dao.insertCourses(course3)
//        course3.copy(idCourse = courseId3.toInt())
//        val courseId4 = dao.insertCourses(course4)
//        course4.copy(idCourse = courseId4.toInt())
//      } catch (e: Exception) {
//        // Handle the exception (e.g., log it, rethrow it, or return a default value)
//        throw e
//      }
//    }
//  }
}
