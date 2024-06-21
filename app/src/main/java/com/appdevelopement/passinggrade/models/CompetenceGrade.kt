package com.appdevelopement.passinggrade.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Teacher::class,
            parentColumns = arrayOf("idTeacher"),
            childColumns = arrayOf("idTeacher"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Student::class,
            parentColumns = arrayOf("studentNumber"),
            childColumns = arrayOf("studentNumber"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Compentence::class,
            parentColumns = arrayOf("idComptence"),
            childColumns = arrayOf("idComptence"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idTeacher"), Index("idComptence"), Index("studentNumber")]
)
data class CompetenceGrade(
    @PrimaryKey(autoGenerate = true) val idCompetenceGradeId: Int,
    val idTeacher: Int,
    val studentNumber: Int,
    val idComptence: Int,
    val dtGrade: Double,
    val dtComment: String?
)