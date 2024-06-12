package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Teacher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object TeacherManagerV2 {
//    val teacher1 = Teacher(
//        idTeacher = 0,
//        dtEmail = "jan@email.com",
//        dtPassword = "JanPaw",
//        dtName = "Teacher 1"
//    )
//        val teacher2 = Teacher(
//            idTeacher = 0,
//    dtEmail = "gerjan@email.com",
//    dtPassword = "GerjanPaw",
//    dtName = "Teacher 2"
//    )

//    fun addTeacher(context: Context) {
//        val dao = AppDatabase.getDatabase(context).teacherDao()
//        CoroutineScope(IO).launch {
//            dao.insertTeacher(teacher1)
//        }
//    }

    suspend fun getTeacherByCredentials(context: Context, username: String, password: String): Teacher? {
        val dao = AppDatabase.getDatabase(context).teacherDao()
        return withContext(Dispatchers.IO) {
            dao.getTeacherByCredentials(username, password)
        }
    }
}