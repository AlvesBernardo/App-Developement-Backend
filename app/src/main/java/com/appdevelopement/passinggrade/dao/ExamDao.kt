package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.appdevelopement.passinggrade.models.Exam


@Dao
interface ExamDao {
    @Query("SELECT * FROM Exam")
    fun getAll(): List<Exam>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExam(exam: Exam): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertexam(exam: Exam)

    @Query("SELECT * FROM EXAM WHERE idCourse = :idCourse")
    suspend fun getExamsByCourseId(idCourse: Int): List<Exam>

    @Query("SELECT * FROM Exam where idExam= :examId")
    fun getExam(examId: Int): Exam?

    @Query("SELECT * FROM Exam where idTeacher= :teacherId")
    suspend fun getExamByTeacher(teacherId: Int): List<Exam>

    @Update
    suspend fun updateExam(exam: Exam)

}
