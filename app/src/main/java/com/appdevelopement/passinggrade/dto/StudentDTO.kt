package com.appdevelopement.passinggrade.dto

import androidx.room.ColumnInfo

data class StudentDTO(
    @ColumnInfo(name = "dtStudentNumber") val studentNumber: Int,
    @ColumnInfo(name = "dtStudentName") val studentName: String,
)
