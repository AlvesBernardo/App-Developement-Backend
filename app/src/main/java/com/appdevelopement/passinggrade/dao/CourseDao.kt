package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Course


@Dao
interface CourseDao{
    @Query("SELECT * FROM Course")
    fun getAll(): List<Course>
//    @Insert
//    fun insertAll(vararg users: User)
//
//    @Delete
//    fun delete(user: User)
}
