package com.appdevelopement.passinggrade

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.middelware.AddExam
import com.appdevelopement.passinggrade.middelware.CompetenceManager
import com.appdevelopement.passinggrade.middelware.TeacherManger
import com.appdevelopement.passinggrade.middleware.CourseManager
import com.appdevelopement.passinggrade.pages.LoginFragment
import com.appdevelopement.passinggrade.pages.ProfilePageFragment
import com.appdevelopement.passinggrade.pages.SheetManagementFragment
import com.appdevelopement.passinggrade.pages.UserDashboardFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val isSessionValid =
        isLoggedIn && ((System.currentTimeMillis() - loginTimeStamp) <= oneHourInMilliSeconds)

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
      val currentIsSessionValid =
          currentIsLoggedIn &&
              ((System.currentTimeMillis() - currentLoginTimeStamp) <= oneHourInMilliSeconds)
      if (currentIsSessionValid) {
        when (item.itemId) {
          R.id.home -> {
            replaceFragment(UserDashboardFragment())
            true
          }


          R.id.profile -> {
            replaceFragment((SheetManagementFragment()))
            true
          }

           // Insert course
            val course = CourseManager.addCourse(context)
            val courseId = course.idCourse

            // Insert exam
            val exam = AddExam.addExam(context, 1, 2)
            val exam1 = AddExam.addExam(context, 2, 2)
            val exam2 = AddExam.addExam(context, 1,3 )
            val exam3 = AddExam.addExam(context, 2, 4)


          R.id.profilev2 -> {
            replaceFragment(ProfilePageFragment())
            true
          }

          else -> false

            CompetenceManager.addCompetences(context, 1)
            CompetenceManager.addCompetences(context, 2)
            CompetenceManager.addCompetences(context, 3)
            CompetenceManager.addCompetences(context, 4)
//               TeacherCourseManager.addTeacherCourse(context, teacherId = 43, courseId = 11)
//                    TeacherCourseManager.addTeacherCourse(context, teacherId = 43, courseId = 12)

        }
      } else {
        sharedPreferences.edit()?.remove("loggedIn")?.apply()
        replaceFragment(LoginFragment())
        Log.d("error", "User not logged in")
        true
      }
    }
    initializeDatabase()
  }

  private fun replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
  }

  private fun initializeDatabase() {
    val context = this
    CoroutineScope(Dispatchers.IO).launch {
      // Ensure that all middleware operations are completed in the correct sequence

      // Insert teacher
      val teacher = TeacherManger.addTeacher(context)
      val teacherId = teacher.idTeacher

      // Insert course
      val course = CourseManager.addCourse(context)
      val courseId = course.idCourse

      // Insert exam
      val exam = AddExam.addExam(context, 1, 2)
      val exam1 = AddExam.addExam(context, 2, 2)
      val exam2 = AddExam.addExam(context, 1, 3)
      val exam3 = AddExam.addExam(context, 2, 4)

      CompetenceManager.addCompetences(context, 1)
      CompetenceManager.addCompetences(context, 2)
      CompetenceManager.addCompetences(context, 3)
      CompetenceManager.addCompetences(context, 4)
    }
  }
}
