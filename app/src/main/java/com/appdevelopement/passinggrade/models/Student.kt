package com.appdevelopement.passinggrade.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey(autoGenerate = true) val idStudent: Int,
    @ColumnInfo(name = "dtStudentName") val studentName: String,
    @ColumnInfo(name = "dtStudentNumber") val studentNumber: Int,
    @ColumnInfo(name = "dtIsGrades") val isGraded: Boolean
)
