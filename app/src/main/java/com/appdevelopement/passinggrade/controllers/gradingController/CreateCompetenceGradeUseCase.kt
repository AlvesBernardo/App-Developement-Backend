package com.appdevelopement.passinggrade.controllers.gradingController

import android.util.Log
import com.appdevelopement.passinggrade.dao.CompetenceDao
import com.appdevelopement.passinggrade.dao.CompetenceGradeDao
import com.appdevelopement.passinggrade.models.CompetenceGrade
import com.appdevelopement.passinggrade.pages.grading.CriterionRecord

class CreateCompetenceGradeUseCase(
    private val competenceGradeDao: CompetenceGradeDao,
    private val competenceDao: CompetenceDao
) {

  suspend fun execute(criterionRecord: List<CriterionRecord>, studentId: Int, examId: Int) {
    try {
      val competences = competenceDao.getCompetencesForExam(examId)
      competences.forEachIndexed { index, record ->
        val competenceGrade =
            CompetenceGrade(
                idCompetenceGradeId = 0,
                idTeacher = 1,
                studentNumber = studentId,
                idCompetence = record.idCompetence, // use this instead
                dtGrade = criterionRecord[index].progress.toDouble(),
                dtComment = criterionRecord[index].comment)
        competenceGradeDao.insertCompetenceGrade(competenceGrade)
      }
    } catch (e: Exception) {
      Log.e("CreateCompetenceGradeUseCase", "Error inserting CompetenceGrade", e)
    }
  }
}
