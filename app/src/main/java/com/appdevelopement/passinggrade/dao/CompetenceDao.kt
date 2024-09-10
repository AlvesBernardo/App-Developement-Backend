package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Competence

@Dao
interface CompetenceDao {
  @Query("SELECT * FROM Competence") fun getAll(): List<Competence>

  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(competence: Competence)

  @Query("SELECT * FROM Competence WHERE idExam = :idExam")
  suspend fun getCompetencesForExam(idExam: Int): List<Competence>
}
