package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Exam


@Dao
interface ExamDao{
    @Query("SELECT * FROM Exam")
    fun getAll(): List<Exam>
}
