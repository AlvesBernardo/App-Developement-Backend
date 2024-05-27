package com.appdevelopement.passinggrade.models
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.w3c.dom.Comment

@Entity(
    indices = [Index("idTeacher"), Index("idStudent")],
    foreignKeys = [
        ForeignKey(
            entity = Teacher::class,
            parentColumns = arrayOf("idTeacher"),
            childColumns = arrayOf("idTeacher"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Student::class,
            parentColumns = arrayOf("idStudent"),
            childColumns = arrayOf("idStudent"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Exam(

    @PrimaryKey(autoGenerate = true) val examId: Int,
    val examName: String,
    val idTeacher: Int,
    val idStudent: Int,
    val grade: Int,
    val comment: String?
)
