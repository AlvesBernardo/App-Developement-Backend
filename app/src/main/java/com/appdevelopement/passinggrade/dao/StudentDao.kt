package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM Student")
    fun getAll(): List<Student>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Query("SELECT * FROM Student WHERE studentNumber == :studentNumber")
    suspend fun findStudent(studentNumber: Int): Student?

    @Query("""
SELECT student.* FROM student
INNER JOIN examstudentcrossref 
ON student.studentNumber = examstudentcrossref.studentNumber
WHERE examstudentcrossref.idExam = :examId
""")
    suspend fun getStudentsForExam(examId: Int): List<Student>
}
