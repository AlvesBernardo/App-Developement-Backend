package com.appdevelopement.passinggrade

import GradeStudentFragment
import LoginFragment
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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load LoginFragment initially
        if (savedInstanceState == null) {
            replaceFragment(LoginFragment())
        }

        val bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(GradingSheetFragment())
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

        // Use middleware to add entities to database
        val database = AppDatabase.getDatabase(this)
//        TeacherManger.addTeacher(this)
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