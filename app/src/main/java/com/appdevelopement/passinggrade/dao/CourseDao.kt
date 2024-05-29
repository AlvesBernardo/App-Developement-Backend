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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourses(course: Course): Long
//
//    @Delete
//    fun delete(user: User)
}
