package com.appdevelopement.passinggrade.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey val studentNumber: Int,
    @ColumnInfo(name = "dtStudentName") val studentName: String,
)
