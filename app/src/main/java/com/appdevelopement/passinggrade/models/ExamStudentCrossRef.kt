package com.appdevelopement.passinggrade.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["studentNumber", "idExam"],
    foreignKeys =
        [
            ForeignKey(
                entity = Exam::class,
                parentColumns = ["idExam"],
                childColumns = ["idExam"],
                onDelete = ForeignKey.CASCADE),
            ForeignKey(
                entity = Student::class,
                parentColumns = ["studentNumber"],
                childColumns = ["studentNumber"],
                onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["idExam"]), Index(value = ["studentNumber"])])
data class ExamStudentCrossRef(val idExam: Int, val studentNumber: Int, val isGraded: Boolean = false)
