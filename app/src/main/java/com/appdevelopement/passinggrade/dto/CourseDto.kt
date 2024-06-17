package com.appdevelopement.passinggrade.dto

import androidx.room.ColumnInfo

data class CourseDto(
    @ColumnInfo(name = "dtTitle") val dtTitle: String,
    @ColumnInfo(name = "dtDescription") val dtDescription: String
)
