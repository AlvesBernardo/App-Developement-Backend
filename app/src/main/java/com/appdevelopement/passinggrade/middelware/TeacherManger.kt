package com.appdevelopement.passinggrade.middelware

import android.content.Context
import android.util.Log
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Teacher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object TeacherManger {
//    private val teacher1 = Teacher(
//        idTeacher = 0,
//        dtEmail = "teacher1@example.com",
//        dtPassword = "securePassword",
//        dtName = "Teacher 1"
//    )
//
//    suspend fun addTeacher(context: Context): Teacher {
//        val dao = AppDatabase.getDatabase(context).teacherDao()
//        val teacherId = withContext(IO) {
//            dao.insertTeacher(teacher1)
//        }
//        return teacher1.copy(idTeacher = teacherId.toInt())
//    }

    suspend fun getTeacherByEmailAndPassword(context: Context, email: String, password: String): Teacher?{
        val dao = AppDatabase.getDatabase(context).teacherDao()
        return withContext(IO){
            dao.getTeacherByCredentials(email, password)
        }
    }
}
