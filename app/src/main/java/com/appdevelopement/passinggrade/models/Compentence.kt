package com.appdevelopement.passinggrade.models

import androidx.room.*

@Entity(
    indices = [Index("idExam")],
    foreignKeys = [
        ForeignKey(
            entity = Exam::class,
            parentColumns = arrayOf("examId"),
            childColumns = arrayOf("idExam"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Compentence(
    @PrimaryKey(autoGenerate = true) val idCompetence: Int = 0,
    @ColumnInfo(name = "dtName") val dtName : String,
    @ColumnInfo(name = "dtDescription") val dtDescription: String? = null,
    @ColumnInfo(name = "dtWeight") val dtWeight: Int,
    @ColumnInfo(name = "dtMustPass") val dtMustPass: Boolean,
    @ColumnInfo(name = "idExam") val idExam: Int
)