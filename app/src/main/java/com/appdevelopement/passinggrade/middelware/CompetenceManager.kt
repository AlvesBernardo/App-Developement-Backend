package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Compentence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

object CompetenceManager {

    // just example competences, assign actual value to 'idExam' field
    val namingCompetence = Compentence(
        dtName = "Naming",
        dtDescription = "Competence related to proper naming conventions.",
        dtWeight = 20,
        dtMustPass = true,
        idExam = 1   //the id for "OOP1"
    )

    val codingConventionCompetence = Compentence(
        dtName = "Coding Convention",
        dtDescription = "Competence related to following coding conventions and standards.",
        dtWeight = 50,
        dtMustPass = true,
        idExam = 1   //the id for "OOP1"
    )

    val unitTestingCompetence = Compentence(
        dtName = "Unit Testing",
        dtDescription = "Competence related to writing and executing unit tests.",
        dtWeight = 30,
        dtMustPass = true,
        idExam = 1   //the id for "OOP1"
    )

    fun addCompetences(context: Context) {
        val dao = AppDatabase.getDatabase(context).compentenceDao()
        CoroutineScope(IO).launch {
            dao.insert(namingCompetence)
            dao.insert(codingConventionCompetence)
            dao.insert(unitTestingCompetence)
        }
    }
}