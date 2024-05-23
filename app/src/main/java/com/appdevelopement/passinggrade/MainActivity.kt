package com.appdevelopement.passinggrade

import GradeStudentFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Student
import com.appdevelopement.passinggrade.models.Teacher
import com.appdevelopement.passinggrade.pages.GradingSheetFragment
import com.appdevelopement.passinggrade.pages.StudentPageFragment
import com.appdevelopement.passinggrade.pages.UserDashboardFragment

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNavigationItemView.selectedItemId = R.id.home  // Default to home fragment
        }

        // Initialize the database
        CoroutineScope(Dispatchers.IO).launch {
            try {
                db = AppDatabase.getDatabase(applicationContext)

                // Check if there are any teachers in the database
                val teachers = db.teacherDao().getAll()
                if (teachers.isEmpty()) {
                    db.teacherDao().insertTeacher(
                        Teacher(
                            idTeacher = 0,
                            dtEmail = "teacher@example.com",
                            dtPassword = "password",
                            dtName = "John Doe"
                        )
                    )
                }

                // Check if there are any students in the database
                val students = db.studentDao().getAll()
                if (students.isEmpty()) {
                    db.studentDao().insertStudent(
                        Student(
                            idStudent = 0,
                            studentName = "Jane Doe",
                            studentNumber = 12345,
                            isGraded = false
                        )
                    )
                }

                // Optionally interact with the database and update UI
                val exams = db.examDao().getAll() // Interact with the database safely
                withContext(Dispatchers.Main) {
                    // Use exams in UI thread if needed
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
