package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Teacher


//could be defined later ask jan
//TODO provides abstract interface to database
//hide information like image image hidding a grade
@Dao
interface TeacherDao{

    @Query("SELECT * FROM Teacher WHERE dtEmail = :username AND dtPassword = :password LIMIT 1")
    suspend fun getTeacherByCredentials(username: String, password: String): Teacher?

    @Query("Select * From Teacher")
    fun getAll() : List<Teacher>

    @Insert
    suspend fun insertTeacher(teacher: Teacher): Long



}
