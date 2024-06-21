package com.appdevelopement.passinggrade.controllers.gradingController

import com.appdevelopement.passinggrade.database.AppDatabase

class GradingUseCase(private val db: AppDatabase) {
  suspend fun hasPassedMandatoryCompetences(studentId: Int): Boolean {
    val minimunPassingScore = 5.5
    val studentCompentence = db.CompentenceGradeDao().getStudentCompetenceGrades(studentId)

    return studentCompentence.all { it.dtGrade >= minimunPassingScore }
  }

  companion object {
    const val MINIMUM_SCORE = 5.5
  }
}
