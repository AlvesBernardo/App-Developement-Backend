package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Query
import com.appdevelopement.passinggrade.dto.Student

@Dao
interface StudentDao {
    @Query("SELECT dtStudentName, dtStudentNumber, dtIsGrades FROM Student")
    fun getAll(): List<Student>
}
