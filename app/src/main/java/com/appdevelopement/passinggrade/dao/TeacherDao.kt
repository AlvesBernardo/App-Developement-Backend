package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Teacher


//could be defined later ask jan
//TODO provides abstract interface to database
//hide information like image image hidding a grade
@Dao
interface TeacherDao{
    @Query("Select * From Teacher")
    fun getAll() : List<Teacher>



}
