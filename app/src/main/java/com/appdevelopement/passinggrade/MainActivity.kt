package com.appdevelopement.passinggrade

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.middelware.AddExam
import com.appdevelopement.passinggrade.middelware.CompetenceManager
import com.appdevelopement.passinggrade.middelware.TeacherManger
import com.appdevelopement.passinggrade.middelware.CourseManager
import com.appdevelopement.passinggrade.middleware.TeacherCourseManager
import com.appdevelopement.passinggrade.pages.GradingSheetFragment
import com.appdevelopement.passinggrade.pages.LoginFragment
import com.appdevelopement.passinggrade.pages.ProfilePageFragment
import com.appdevelopement.passinggrade.pages.UserDashboardFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  private lateinit var bottomNavigationItemView: BottomNavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    bottomNavigationItemView = findViewById(R.id.bottom_navigation)

    val sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getBoolean("loggedIn", false)
    val loginTimeStamp = sharedPreferences.getLong("loginTimestamp", 0)
    val oneHourInMilliSeconds = 60 * 60 * 1000
    val isSessionValid = isLoggedIn && ((System.currentTimeMillis() - loginTimeStamp) <= oneHourInMilliSeconds)

    updateBottomNavigationVisibility(isSessionValid)

    if (savedInstanceState == null) {
      if (isSessionValid) {
        replaceFragment(UserDashboardFragment())
      } else {
        replaceFragment(LoginFragment())
      }
    }

    bottomNavigationItemView.setOnItemSelectedListener { item ->
      handleNavigationSelection(item.itemId)
    }

    initializeDatabase()
  }

  private fun handleNavigationSelection(itemId: Int): Boolean {
    val sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE)
    val currentIsLoggedIn = sharedPreferences.getBoolean("loggedIn", false)
    val currentLoginTimeStamp = sharedPreferences.getLong("loginTimestamp", 0)
    val oneHourInMilliSeconds = 60 * 60 * 1000
    val currentIsSessionValid = currentIsLoggedIn &&
            ((System.currentTimeMillis() - currentLoginTimeStamp) <= oneHourInMilliSeconds)
    updateBottomNavigationVisibility(currentIsSessionValid)
    if (currentIsSessionValid) {
      when (itemId) {
        R.id.home -> {
          replaceFragment(UserDashboardFragment())
          return true
        }
        R.id.profile -> {
          replaceFragment(GradingSheetFragment())
          return true
        }
        R.id.profilev2 -> {
          replaceFragment(ProfilePageFragment())
          return true
        }
        else -> return false
      }
    } else {
      sharedPreferences.edit()?.remove("loggedIn")?.apply()
      replaceFragment(LoginFragment())
      Log.d("error", "User not logged in")
      return true
    }
  }

  private fun updateBottomNavigationVisibility(isVisible: Boolean) {
    bottomNavigationItemView.visibility = if (isVisible) View.VISIBLE else View.GONE
  }

  private fun replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
  }
  private fun initializeDatabase() {
    val context = this
    val sharedPreferences = getSharedPreferences("ApplicationPrefs", Context.MODE_PRIVATE)
    if (!sharedPreferences.getBoolean("isFirstRun", true)) {
      return
    }
    CoroutineScope(Dispatchers.IO).launch {

      // Insert default teachers
      TeacherManger.addTeacher(context)

      // Insert default courses
      CourseManager.addCourse(context)
      
      
      // Insert exams with specified names
      AddExam.addExam(context, 1, 1, "OOP2")
      AddExam.addExam(context, 1, 1, "OOP2 resit")
      AddExam.addExam(context, 1, 4, "DbEng")
      AddExam.addExam(context, 2, 3, "DataProcess")
      AddExam.addExam(context, 2, 2, "AppDev")
      
      //Inserts competences for specified exams
      CompetenceManager.addCompetences(context, 1)
      CompetenceManager.addCompetences(context, 2)
      CompetenceManager.addCompetences(context, 3)
      CompetenceManager.addCompetences(context, 4)

      with(sharedPreferences.edit()) {
        putBoolean("isFirstRun", false)
        apply()
      }
    }
  }

  fun onUserLoggedIn() {
    val sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE)
    val loginTimeStamp = sharedPreferences.getLong("loginTimestamp", 0)
    val oneHourInMilliSeconds = 60 * 60 * 1000
    val isSessionValid = ((System.currentTimeMillis() - loginTimeStamp) <= oneHourInMilliSeconds)

    updateBottomNavigationVisibility(isSessionValid)

    if (isSessionValid) {
      replaceFragment(UserDashboardFragment())
    }
  }

}
