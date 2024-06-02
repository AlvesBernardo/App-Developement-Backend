package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.appdevelopement.passinggrade.models.CompetenceGrade
@Dao
interface CompentenceGradeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComptenceGrade(compentenceGrade: CompetenceGrade)
}