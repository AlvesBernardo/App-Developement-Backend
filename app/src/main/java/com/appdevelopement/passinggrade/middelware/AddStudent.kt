package com.appdevelopement.passinggrade.middelware


import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


object AddStudent {
    val student = Student(
        idStudent = 0,
        studentName = "Jane Doe",
        studentNumber = 12345,
        isGraded = false
    )

    fun addStundent(context: Context) {
        val dao = AppDatabase.getDatabase(context).studentDao()
        CoroutineScope(IO).launch {
            dao.insertStudent(student)
        }
    }
}