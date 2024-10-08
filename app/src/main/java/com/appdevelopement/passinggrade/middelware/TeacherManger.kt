package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Teacher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

object TeacherManger {
  private val teacher1 =
      Teacher(idTeacher = 0, dtEmail = "jan@email.com", dtPassword = "JanPaw", dtName = "Jan")

  private val teacher2 =
      Teacher(
          idTeacher = 0, dtEmail = "martijn@email.com", dtPassword = "MarPaw", dtName = "Martijn")
    
    suspend fun addTeacher(context: Context) {
        val dao = AppDatabase.getDatabase(context).teacherDao()
        withContext(IO) {
            dao.insertTeacher(teacher1)
            dao.insertTeacher(teacher2)
        }
    }


//  suspend fun addTeacher(context: Context): Teacher {
//    val dao = AppDatabase.getDatabase(context).teacherDao()
//    val teacherId =
//        withContext(IO) {
//          dao.insertTeacher(teacher1)
//          dao.insertTeacher(teacher2)
//        }
//    return teacher1.copy(idTeacher = teacherId.toInt())
//  }

  suspend fun getTeacherByEmailAndPassword(
      context: Context,
      email: String,
      password: String
  ): Teacher? {
    val dao = AppDatabase.getDatabase(context).teacherDao()
    return withContext(IO) { dao.getTeacherByCredentials(email, password) }
  }
}
