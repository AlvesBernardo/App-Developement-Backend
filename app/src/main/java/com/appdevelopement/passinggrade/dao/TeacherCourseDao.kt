package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.appdevelopement.passinggrade.models.TeacherCourse

@Dao
interface TeacherCourseDao {
  @Query("SELECT * FROM TeacherCourse WHERE idTeacher = :teacherId")
  suspend fun getCoursesForTeacher(teacherId: Int): List<TeacherCourse>

  @Insert suspend fun insertTeacherCourse(teacherCourse: TeacherCourse): Long

  @Query("DELETE FROM TeacherCourse WHERE idTeacher = :teacherId AND idCourse = :courseId")
  suspend fun deleteTeacherCourse(teacherId: Int, courseId: Int)

  @Update suspend fun updateTeacherCourse(teacherCourse: TeacherCourse)
}
