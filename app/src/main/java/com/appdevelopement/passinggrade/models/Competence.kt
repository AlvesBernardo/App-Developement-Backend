package com.appdevelopement.passinggrade.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index("idExam")],
    foreignKeys =
        [
            ForeignKey(
                entity = Exam::class,
                parentColumns = arrayOf("idExam"),
                childColumns = arrayOf("idExam"),
                onDelete = ForeignKey.CASCADE)])
data class Competence(
    @PrimaryKey(autoGenerate = true) val idCompetence: Int,
    val idExam: Int,
    val dtName: String,
    val dtCompetenceWeight: Int,
    val dtMustPass: Boolean
)
