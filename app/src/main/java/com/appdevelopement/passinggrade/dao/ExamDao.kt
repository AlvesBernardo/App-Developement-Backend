package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Compentence
import com.appdevelopement.passinggrade.models.Exam


@Dao
interface ExamDao{
    @Query("SELECT * FROM Exam")
    fun getAll(): List<Exam>

    @Insert
    fun insertExam(exam: Exam)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertexam(exam: Exam)

}
