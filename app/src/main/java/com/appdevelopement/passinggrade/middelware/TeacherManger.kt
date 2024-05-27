package com.appdevelopement.passinggrade.middelware

import android.content.Context
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Teacher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

object TeacherManger {
    val teacher1 = Teacher(
        idTeacher = 0,
        dtEmail = "teacher1@example.com",
        dtPassword = "securePassword",
        dtName = "Teacher 1"
    )

    fun addTeacher(context: Context) {
        val dao = AppDatabase.getDatabase(context).teacherDao()
        CoroutineScope(IO).launch {
            dao.insertTeacher(teacher1)
        }
    }
}