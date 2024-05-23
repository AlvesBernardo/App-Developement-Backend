package com.appdevelopement.passinggrade.dto

import androidx.room.ColumnInfo

data class StudentDTO(
    @ColumnInfo(name = "dtStudentName") val studentName: String,
    @ColumnInfo(name = "dtStudentNumber") val studentNumber: Int,
    @ColumnInfo(name = "dtIsGrades") val isGraded: Boolean
)
