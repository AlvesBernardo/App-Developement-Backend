package com.appdevelopement.passinggrade.middelware


import android.content.Context
import android.util.Log
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


object AddStudent {
    val student = Student(
        idStudent = 0,
        studentName = "adas Doe",
        studentNumber = 2321,
        isGraded = false
    )

    val student2 = Student(
        idStudent = 0,
        studentName = "twas Doe",
        studentNumber = 123131313,
        isGraded = false
    )


    suspend fun addStudent(context: Context): Student {
        val dao = AppDatabase.getDatabase(context).studentDao()
        val studentId = withContext(Dispatchers.IO) {
            dao.insertStudent(student)
            dao.insertStudent(student2)
        }
        return student.copy(idStudent = studentId.toInt())
    }


}