package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Exam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
object AddExam {

    val oop1Exam = Exam(
        examId = 0,
        idTeacher = 1,
        idStudent = 1,
        grade = 100,
        comment = "Excellent",
        examName = "OOP1"
    )

    fun addExam(context: Context) {
        val dao = AppDatabase.getDatabase(context).examDao()
        CoroutineScope(IO).launch {
            dao.insertexam(oop1Exam)
        }
    }
}