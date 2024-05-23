package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM Student")
    fun getAll(): List<Student>

    @Insert
    suspend fun insertStudent(student: Student)

}
