package com.appdevelopement.passinggrade.dto

import androidx.room.ColumnInfo

data class TeacherWithCoursesDto(
    @ColumnInfo(name = "dtTeacherName") val tacehrName: String,
    @ColumnInfo(name = "dtCourse") val courses: List<String> // List of course descriptions
)
