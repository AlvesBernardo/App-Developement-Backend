package com.appdevelopement.passinggrade.models

import androidx.room.*

@Entity(
    indices = [Index("idExam")],
    foreignKeys =
        [
            ForeignKey(
                entity = Exam::class,
                parentColumns = arrayOf("idExam"),
                childColumns = arrayOf("idExam"),
                onDelete = ForeignKey.CASCADE)])
data class Compentence(
    @PrimaryKey(autoGenerate = true) val idComptence: Int,
    val idExam: Int,
    val dtName: String,
    val dtCompetenceWeight: Int,
    val dtMustPass: Boolean
)
