package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Course

@Dao
interface CourseDao {
  @Query("SELECT * FROM Course") fun getAll(): List<Course>

  @Query("SELECT * FROM Course") fun getAllCourses(): List<Course>

  @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertCourses(course: Course): Long
  
  @Query("SELECT Course.dtDescription FROM Course" +
          " WHERE Course.idCourse= :courseId")
  suspend fun getCourseDescription(courseId: Int):String
}
