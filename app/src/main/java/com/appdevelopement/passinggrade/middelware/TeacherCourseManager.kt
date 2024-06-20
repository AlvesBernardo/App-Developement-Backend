package com.appdevelopement.passinggrade.middleware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.TeacherCourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TeacherCourseManager {

  suspend fun addTeacherCourse(context: Context, teacherId: Int, courseId: Int) {
    val dao = AppDatabase.getDatabase(context).teacherCourseDao()
    withContext(Dispatchers.IO) {
      dao.insertTeacherCourse(TeacherCourse(idTeacher = teacherId, idCourse = courseId))
    }
  }

  suspend fun getCoursesByTeacher(context: Context, teacherId: Int): List<TeacherCourse> {
    val dao = AppDatabase.getDatabase(context).teacherCourseDao()
    return withContext(Dispatchers.IO) { dao.getCoursesForTeacher(teacherId) }
  }

  suspend fun deleteTeacherCourse(context: Context, teacherId: Int, courseId: Int) {
    val dao = AppDatabase.getDatabase(context).teacherCourseDao()
    withContext(Dispatchers.IO) { dao.deleteTeacherCourse(teacherId, courseId) }
  }

  suspend fun updateTeacherCourse(context: Context, teacherCourse: TeacherCourse) {
    val dao = AppDatabase.getDatabase(context).teacherCourseDao()
    withContext(Dispatchers.IO) { dao.updateTeacherCourse(teacherCourse) }
  }
}
