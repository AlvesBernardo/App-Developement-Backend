package com.appdevelopement.passinggrade.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index("idTeacher"), Index("idCourse")],
    foreignKeys = [
        ForeignKey(
            entity = Teacher::class,
            parentColumns = arrayOf("idTeacher"),
            childColumns = arrayOf("idTeacher"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Course::class,
            parentColumns = arrayOf("idCourse"),
            childColumns = arrayOf("idCourse"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Exam(
    @PrimaryKey(autoGenerate = true) val idExam: Int,
    val examName: String,
    val idTeacher: Int,
    val idCourse: Int,
)