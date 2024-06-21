package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdevelopement.passinggrade.models.ExamStudentCrossRef


@Dao
interface ExamStudentCorssReferecne {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(examStudentCrossRef: ExamStudentCrossRef)

    @Query("SELECT studentNumber FROM ExamStudentCrossRef WHERE studentNumber = :examId")
    suspend fun getStudentNumbersForExam(examId: Int): List<Int>

    @Query("SELECT * FROM ExamStudentCrossRef WHERE idExam = :examId AND studentNumber = :studentNumber")
    suspend fun findCrossRef(examId: Int, studentNumber: Int): ExamStudentCrossRef?

    @Query("SELECT * FROM ExamStudentCrossRef WHERE idExam = :examId AND studentNumber = :studentNumber")
    fun getSpecificCrossRef(examId: Int, studentNumber: Int): ExamStudentCrossRef?
}