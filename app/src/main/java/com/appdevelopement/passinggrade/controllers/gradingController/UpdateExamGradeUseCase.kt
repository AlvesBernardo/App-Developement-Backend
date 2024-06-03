package com.appdevelopement.passinggrade.controllers.gradingController

import android.util.Log
import com.appdevelopement.passinggrade.dao.ExamDao
import com.appdevelopement.passinggrade.models.Exam

class UpdateExamGradeUseCase(private val examDao: ExamDao) {
    suspend  fun execute(examId: Int, finalGrade: Double, pass: Boolean) {
        try {
            val exam = examDao.getExam(examId)
            if (exam != null) {
                var updatedExam = Exam(
                    idExam = exam.idExam,
                    examName = exam.examName,
                    idTeacher = exam.idTeacher,
                    idStudent = exam.idStudent,
                    idCourse = exam.idCourse,
                    dtFinalGrade = finalGrade
                )
                if (pass) {
                    examDao.updateExam(updatedExam)
                } else {
                    Log.i("UpdateExamGradeUseCase", "Grade did not pass for Exam ID: $examId with grade: $finalGrade")
                }
            } else {
                Log.e("UpdateExamGradeUseCase", "Exam not found with ID: $examId")
            }
        } catch (e: Exception) {
            Log.e("UpdateExamGradeUseCase", "Error updating Exam grade", e)
        }
    }
}