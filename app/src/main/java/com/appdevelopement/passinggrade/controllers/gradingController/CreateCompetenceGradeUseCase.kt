package com.appdevelopement.passinggrade.controllers.gradingController

import android.util.Log
import com.appdevelopement.passinggrade.dao.CompentenceDao
import com.appdevelopement.passinggrade.dao.CompentenceGradeDao
import com.appdevelopement.passinggrade.models.CompetenceGrade
import com.appdevelopement.passinggrade.pages.grading.CriterionRecord

class CreateCompetenceGradeUseCase(private val competenceGradeDao: CompentenceGradeDao, private val competenceDao: CompentenceDao) {

    suspend fun execute(criterionRecord: List<CriterionRecord>, studentId: Int, examId: Int) {
        try {
            val competences = competenceDao.getCompetencesForExam(examId)
            competences.forEachIndexed { index, record ->
                val competenceGrade = CompetenceGrade(
                    idCompetenceGradeId = 0,
                    idTeacher = 1,
                    studentNumber = studentId,
                    idComptence = record.idComptence, // use this instead
                    dtGrade = criterionRecord[index].progress.toDouble(),
                    dtComment = criterionRecord[index].comment
                );
                competenceGradeDao.insertComptenceGrade(competenceGrade)
            }
        }catch(e:Exception){
            Log.e("CreateCompetenceGradeUseCase", "Error inserting CompetenceGrade", e)
        }
    }
}