package com.appdevelopement.passinggrade.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Course")
data class Course(
    @PrimaryKey(autoGenerate = true) val idCourse: Int,
    @ColumnInfo(name = "dtTitle") val dtTitle: String,
    @ColumnInfo(name = "dtDescription") val dtDescription: String?
)
