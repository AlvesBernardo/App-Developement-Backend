package com.appdevelopement.passinggrade.models

import org.ktorm.schema.Table
import org.ktorm.schema.int

object ExamTeacher : Table<Nothing>("tblExamTeacher") {
    val idExam = int("idExam")
    val idTeacher = int("idTeacher")
}