package com.appdevelopement.passinggrade.dto

import androidx.room.ColumnInfo

data class StudnetCourseGradeDTO(
    @ColumnInfo(name = "dtStudentName") val studentName: String,
    @ColumnInfo(name = "dtCourseDescripiton") val courseDescription: String,
    @ColumnInfo(name = "dtGrade") val grade: Int
)
