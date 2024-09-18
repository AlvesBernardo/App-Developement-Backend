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

  @Query("SELECT studentNumber FROM ExamStudentCrossRef WHERE ExamStudentCrossRef.idExam = :examId")
  suspend fun getStudentNumbersForExam(examId: Int): List<Int>

  @Query(
      "SELECT * FROM ExamStudentCrossRef WHERE idExam = :examId AND studentNumber = :studentNumber")
  suspend fun findCrossRef(examId: Int, studentNumber: Int): ExamStudentCrossRef?

  @Query(
      "SELECT * FROM ExamStudentCrossRef WHERE idExam = :examId AND studentNumber = :studentNumber")
  fun getSpecificCrossRef(examId: Int, studentNumber: Int): ExamStudentCrossRef?
    
  @Query("UPDATE ExamStudentCrossRef SET isGraded = :isGraded WHERE studentNumber = :studentNumber AND idExam = :examId")
  fun updateIsGraded(studentNumber: Int, examId: Int, isGraded: Boolean)
  
  @Query("SELECT ExamStudentCrossRef.isGraded FROM ExamStudentCrossRef WHERE studentNumber = :studentNumber AND idExam = :examId")
  fun getIsGradedByStudentNumber(studentNumber: Int, examId: Int): Boolean
}
