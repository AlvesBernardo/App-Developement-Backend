package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Competence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CompetenceManager {
  suspend fun addCompetences(context: Context, examId: Int) {
    val dao = AppDatabase.getDatabase(context).competenceDao()
    val competence1 = Competence(0, examId, "Naming", 20, true)
    val competence2 = Competence(0, examId, "Coding Convention", 50, true)
    val competence3 = Competence(0, examId, "Unit Testing", 30, true)

    withContext(Dispatchers.IO) {
      dao.insert(competence1)
      dao.insert(competence2)
      dao.insert(competence3)
    }
  }
}
