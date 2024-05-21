package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Query
import com.appdevelopement.passinggrade.models.TeacherCourse


@Dao
interface TeacherCourseDao{
    @Query("SELECT * FROM TeacherCourse")
    fun getAll(): List<TeacherCourse>
}
