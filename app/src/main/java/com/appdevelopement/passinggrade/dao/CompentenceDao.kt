package com.appdevelopement.passinggrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdevelopement.passinggrade.models.Compentence

@Dao
interface CompentenceDao {
    @Query("SELECT * FROM Compentence")
    fun getAll(): List<Compentence>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(compentence: Compentence)

}
