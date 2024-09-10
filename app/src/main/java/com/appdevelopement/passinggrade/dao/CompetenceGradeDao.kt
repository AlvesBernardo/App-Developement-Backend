package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdevelopement.passinggrade.models.CompetenceGrade

@Dao
interface CompetenceGradeDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCompetenceGrade(competenceGrade: CompetenceGrade)

  @Query("SELECT * FROM CompetenceGrade WHERE studentNumber = :studentId")
  suspend fun getStudentCompetenceGrades(studentId: Int): List<CompetenceGrade>

  @Query(
    "SELECT * FROM CompetenceGrade WHERE studentNumber = :studentId AND (SELECT idCompetence FROM Competence WHERE idExam = :idExam) ORDER BY idCompetenceGradeId DESC LIMIT (SELECT COUNT(idExam) FROM Competence WHERE idExam = :idExam)"
  )
  suspend fun getGradesForStudentAndExam(studentId: Int, idExam: Int): List<CompetenceGrade>
}
