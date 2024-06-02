package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdevelopement.passinggrade.models.CompetenceGrade
@Dao
interface CompentenceGradeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComptenceGrade(compentenceGrade: CompetenceGrade)

    @Query("SELECT * FROM CompetenceGrade WHERE idStudent = :studentId")
    suspend fun getStudentCompetenceGrades(studentId: Int): List<CompetenceGrade>
}