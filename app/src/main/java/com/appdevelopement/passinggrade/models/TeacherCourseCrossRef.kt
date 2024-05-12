package com.appdevelopement.passinggrade.models

import androidx.room.Entity

@Entity(primaryKeys = ["idTeacher","idCourse"])
class TeacherCourseCrossRef(
    val idTeacher: Int,
    val idCourse: Int
)