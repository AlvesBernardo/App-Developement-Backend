package com.appdevelopement.passinggrade.controllers.gradingController

import android.util.Log
import com.appdevelopement.passinggrade.dao.CompentenceGradeDao
import com.appdevelopement.passinggrade.models.CompetenceGrade
import com.appdevelopement.passinggrade.pages.grading.CriterionRecord

class CreateCompetenceGradeUseCase(private val competenceGradeDao: CompentenceGradeDao) {
    suspend fun execute(criterionRecord: List<CriterionRecord>, studentId: Int) {
        try {
            criterionRecord.forEachIndexed { index, record ->
                val competenceGrade = CompetenceGrade(
                    idCompetenceGradeId = 0,
                    idTeacher = 1,
                    idComptence = index + 1+ studentId*10,
                    dtGrade = record.progress.toDouble(),
                    dtComment = record.comment
                )
                competenceGradeDao.insertComptenceGrade(competenceGrade)
            }
        }catch(e:Exception){
            Log.e("CreateCompetenceGradeUseCase", "Error inserting CompetenceGrade", e)
        }
    }
}