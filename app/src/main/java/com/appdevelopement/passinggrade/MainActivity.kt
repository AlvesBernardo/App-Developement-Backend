package com.appdevelopement.passinggrade

<<<<<<< HEAD
import GradeStudentFragment
import LoginFragment
=======
import com.appdevelopement.passinggrade.pages.grading.GradeStudentFragment
>>>>>>> main-dev
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.middelware.*
import com.google.android.material.bottomnavigation.BottomNavigationView
<<<<<<< HEAD
import com.appdevelopement.passinggrade.middelware.AddStudent
import com.appdevelopement.passinggrade.middelware.CompetenceManager
import com.appdevelopement.passinggrade.models.Teacher
import com.appdevelopement.passinggrade.pages.GradingSheetFragment
import com.appdevelopement.passinggrade.pages.StudentPageFragment
import com.appdevelopement.passinggrade.pages.UserDashboardFragment
import com.appdevelopement.passinggrade.middelware.TeacherManagerV2
import com.appdevelopement.passinggrade.pages.GradingSheetFragment
import com.appdevelopement.passinggrade.pages.StudentPageFragment
import com.appdevelopement.passinggrade.pages.UserDashboardFragment
import com.appdevelopement.passinggrade.pages.ProfilePageFragment
import com.appdevelopement.passinggrade.pages.SignUpPageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
//                    replaceFragment(SignUpPageFragment())
//                    replaceFragment(ProfilePageFragment())
                    replaceFragment(GradingSheetFragment())
                    true
                }

                R.id.grade -> {
                    replaceFragment(GradeStudentFragment())
                    true
                }

                R.id.profile -> {
                    replaceFragment(StudentPageFragment())
                    true
                }

                R.id.profilev2 -> {
                    replaceFragment(ProfilePageFragment()) // You need to create this fragment

                    true
                }

                else -> false
            }
        }

        // Use middleware to add entities to database
        val database = AppDatabase.getDatabase(this)
        TeacherManagerV2.addTeacher(this)
//        AddStudent.addStundent(this)
//        AddExam.addExam(this)
//        CompetenceManager.addCompetences(this)
//        initializeDatabase()

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