package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Course
import com.appdevelopement.passinggrade.models.Exam


@Dao
interface CourseDao{
    @Query("SELECT * FROM Course")
    fun getAll(): List<Course>

    @Query("SELECT * FROM Course")
    fun getAllCourses(): List<Course>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourses(course: Course): Long

    @Query("""
        SELECT * FROM Course
        JOIN TeacherCourse ON Course.idCourse = TeacherCourse.idCourse
        WHERE TeacherCourse.idTeacher = :teacherId
        """)
    suspend fun getTeacherCourses(teacherId: Int):List<Course>
//
//    @Delete
//    fun delete(user: User)
}