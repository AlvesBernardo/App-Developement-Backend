package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.appdevelopement.passinggrade.models.TeacherCourse

@Dao
interface TeacherCourseDao {
    @Query("SELECT * FROM TeacherCourse WHERE idTeacher = :teacherId")
    fun getCoursesForTeacher(teacherId: Int): List<TeacherCourse>

    @Insert
    suspend fun insertTeacherCourse(teacherCourse: TeacherCourse): Long
}
