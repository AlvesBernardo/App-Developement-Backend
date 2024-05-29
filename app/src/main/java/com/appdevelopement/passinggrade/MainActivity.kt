package com.appdevelopement.passinggrade

import com.appdevelopement.passinggrade.pages.grading.GradeStudentFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.middelware.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.appdevelopement.passinggrade.pages.GradingSheetFragment
import com.appdevelopement.passinggrade.pages.StudentPageFragment
import com.appdevelopement.passinggrade.pages.UserDashboardFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
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

        // Use middleware to add entities to database
        initializeDatabase()

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

            // Insert teacher
            val teacher = TeacherManger.addTeacher(context)
            val teacherId = teacher.idTeacher

            // Insert student
            val student = AddStudent.addStudent(context)
            val studentId = student.idStudent

            // Insert course
            val course = CourseManager.addCourse(context)
            val courseId = course.idCourse

            // Insert exam
            val exam = AddExam.addExam(context, teacherId, studentId, courseId)
            val examId = exam.idExam

            // Insert competences
            CompetenceManager.addCompetences(context, examId)
        }
    }

}