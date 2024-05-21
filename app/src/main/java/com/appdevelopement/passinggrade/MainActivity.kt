package com.appdevelopement.passinggrade


import AppDatabase
import GradeStudentFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // Uncommented for future use
                // R.id.home -> {
                //     replaceFragment(StudentPageActivity())
                //     true
                // }
                R.id.grade -> {
                    replaceFragment(GradeStudentFragment())
                    true
                }
                // R.id.profile -> {
                //     replaceFragment(ProfileFragment())
                //     true
                // }
                else -> false
            }
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNavigationItemView.selectedItemId = R.id.grade  // Default to grade fragment
        }

        // Initialize the database
        CoroutineScope(Dispatchers.IO).launch {
            db = AppDatabase.getDatabase(applicationContext)

            // Example of interaction with the database to ensure it's not null
            try {
                val exams = db.examDao().getAll()  // Make sure the method call is safe
                withContext(Dispatchers.Main) {
                    // Use exams in UI thread if needed
                    // Or, perform any UI task after ensuring db initialization
                }
            } catch (e: Exception) {
                // Handle exceptions, e.g., log to console
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
