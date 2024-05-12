package com.appdevelopement.passinggrade.models

import org.ktorm.schema.Table
import org.ktorm.schema.int

object TeacherCourse : Table<Nothing>("tblTeacherCourse") {
    val idTeacher = int("idTeacher")
    val idCourse = int("idCourse")
}
