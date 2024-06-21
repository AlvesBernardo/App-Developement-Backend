package com.appdevelopement.passinggrade.middelware


import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object AddStudent {
    val student = Student(
        studentNumber = 0,
        studentName = "Bernardo Alves",
    )

    val student2 = Student(
        studentNumber = 0,
        studentName = "Mehdi Sadghi",
    )


    suspend fun addStudent(context: Context): Student {
        val dao = AppDatabase.getDatabase(context).studentDao()
        val studentId = withContext(Dispatchers.IO) {
            dao.insertStudent(student)
            dao.insertStudent(student2)
        }
        return student.copy(studentNumber = studentId.toInt())
    }


}