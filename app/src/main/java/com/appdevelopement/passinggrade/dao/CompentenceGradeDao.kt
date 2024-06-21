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

  @Query("SELECT * FROM CompetenceGrade WHERE studentNumber = :studentId")
  suspend fun getStudentCompetenceGrades(studentId: Int): List<CompetenceGrade>

  @Query(
      "SELECT * FROM CompetenceGrade WHERE studentNumber = :studentId AND (SELECT idComptence FROM Compentence WHERE idExam = :idExam) ORDER BY idCompetenceGradeId DESC LIMIT (SELECT COUNT(idExam) FROM Compentence WHERE idExam = :idExam)")
  suspend fun getGradesForStudentAndExam(studentId: Int, idExam: Int): List<CompetenceGrade>
}
