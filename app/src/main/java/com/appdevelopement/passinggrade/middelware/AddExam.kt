package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Exam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AddExam {
    suspend fun addExam(context: Context, teacherId: Int, courseId: Int): Exam {
        val dao = AppDatabase.getDatabase(context).examDao()
        val exam = Exam(
            idExam = 0,
            examName = "OOP1 try 1",
            idTeacher = teacherId,
            idCourse = courseId
        )
        val examId = withContext(Dispatchers.IO) {
            dao.insertExam(exam)
        }
        return exam.copy(idExam = examId.toInt())
    }
}

