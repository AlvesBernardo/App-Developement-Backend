package com.appdevelopement.passinggrade

import GradeStudentFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.middelware.AddExam
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.appdevelopement.passinggrade.middelware.TeacherManger
import com.appdevelopement.passinggrade.middelware.AddStudent
import com.appdevelopement.passinggrade.middelware.CompetenceManager
import com.appdevelopement.passinggrade.models.Teacher
import com.appdevelopement.passinggrade.pages.GradingSheetFragment
import com.appdevelopement.passinggrade.pages.StudentPageFragment
import com.appdevelopement.passinggrade.pages.UserDashboardFragment
import com.appdevelopement.passinggrade.pages.ProfilePageFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(ProfilePageFragment())
//                    replaceFragment(GradingSheetFragment())
                    true
                }
                R.id.grade -> {
                    replaceFragment(GradeStudentFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(StudentPageFragment()) // You need to create this fragment
                    true
                }
                R.id.profilev2 -> {
                    replaceFragment(UserDashboardFragment()) // You need to create this fragment
                    true
                }
                else -> false
            }
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNavigationItemView.selectedItemId = R.id.home  // Default to home fragment
        }

        // Use middleware to add entities to database
        val database = AppDatabase.getDatabase(this)
        TeacherManger.addTeacher(this)
        AddStudent.addStundent(this)
        AddExam.addExam(this)
        CompetenceManager.addCompetences(this)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}