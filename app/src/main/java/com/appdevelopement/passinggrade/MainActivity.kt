package com.appdevelopement.passinggrade

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.dao.TeacherCourseDao
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.middelware.*
import com.appdevelopement.passinggrade.middelware.AddStudent
import com.appdevelopement.passinggrade.middelware.CompetenceManager
import com.appdevelopement.passinggrade.models.Teacher
import com.appdevelopement.passinggrade.middelware.TeacherManagerV2
import com.appdevelopement.passinggrade.middleware.CourseManager
import com.appdevelopement.passinggrade.middleware.TeacherCourseManager
import com.appdevelopement.passinggrade.pages.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.appdevelopement.passinggrade.pages.*
import com.appdevelopement.passinggrade.pages.grading.GradeStudentFragment
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load LoginFragment initially
        if (savedInstanceState == null) {
            replaceFragment(LoginFragment())
        }


        val sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("loggedIn", false)
        val loginTimeStamp = sharedPreferences.getLong("loginTimestamp", 0)
        val oneHourInMilliSeconds = 60 * 60 * 1000
        val isSessionValid = isLoggedIn && ((System.currentTimeMillis() - loginTimeStamp) <= oneHourInMilliSeconds)

        if (isSessionValid) {
            replaceFragment(UserDashboardFragment())
        } else {
            replaceFragment(LoginFragment())
        }

        val database = AppDatabase.getDatabase(this)
        val bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationItemView.setOnItemSelectedListener { item ->
            val currentIsLoggedIn = sharedPreferences.getBoolean("loggedIn", false)
            val currentLoginTimeStamp = sharedPreferences.getLong("loginTimestamp", 0)
            val currentIsSessionValid = currentIsLoggedIn && ((System.currentTimeMillis() - currentLoginTimeStamp) <= oneHourInMilliSeconds)
            if (currentIsSessionValid) {
                when (item.itemId) {
                    R.id.home -> {
//                    replaceFragment(SignUpPageFragment())
//                    replaceFragment(ProfilePageFragment())
                    replaceFragment(UserDashboardFragment())
//                        replaceFragment(LoginFragment())
                        true
                    }
                    R.id.profile -> {
//                        replaceFragment(GradingSheetFragment())
//                        replaceFragment((SheetManagementFragment()))
                        replaceFragment(GradeStudentFragment())
                        true
                    }
                    R.id.profilev2 -> {
                        replaceFragment(ProfilePageFragment())
                        true
                    }
                    else -> false
                }
            } else {
                sharedPreferences.edit()?.remove("loggedIn")?.apply() // Remove the loggedIn flag
                replaceFragment(LoginFragment())
                Log.d("error", "User not logged in")
                true
            }
        }
        /*Line 75 is uncommented, 76 is commented on  login-styling branch*/
//         TeacherManagerV2.addTeacher(this)
        initializeDatabase()
        // AddStudent.addStundent(this)
        // AddExam.addExam(this)
        // CompetenceManager.addCompetences(this)

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun initializeDatabase() {
        val context = this
        CoroutineScope(Dispatchers.IO).launch {
            // Ensure that all middleware operations are completed in the correct sequence

             //Insert teacher
            val teacher = TeacherManger.addTeacher(context)
            val teacherId = teacher.idTeacher

////             Insert student
//            val student = AddStudent.addStudent(context)
//            val studentId = student.studentNumber

//            // Insert course
            val course = CourseManager.addCourse(context)
            val courseId = course.idCourse
//
            // Insert exam
            val exam = AddExam.addExam(context, 1, 2)
            val examId = exam.idExam


//            CompetenceManager.addCompetences(context, examId)
//                  TeacherCourseManager.addTeacherCourse(context, teacherId = 43, courseId = 11)
//                    TeacherCourseManager.addTeacherCourse(context, teacherId = 43, courseId = 12)
        }
    }

}
//Incoming change from login-styling branch

    // private fun initializeDatabase() {
    //     val context = this
    //     CoroutineScope(Dispatchers.IO).launch {
    //         // Ensure that all middleware operations are completed in the correct sequence
    //         // Insert teacher
    //         val teacher = TeacherManger.addTeacher(context)
    //         val teacherId = teacher.idTeacher
    //         // Insert student
    //         // val student = AddStudent.addStudent(context)
    //         // val studentId = student.idStudent
    //         // Insert course
    //         val course = CourseManager.addCourse(context)
    //         val courseId = course.idCourse
    //         // Insert exam
    //         // val exam = AddExam.addExam(context, teacherId, studentId, courseId)
    //         // val examId = exam.idExam
    //         // Insert competences
    //         // CompetenceManager.addCompetences(context, examId)
    //     }
    // }
//}

