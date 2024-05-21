package com.appdevelopement.passinggrade


import GradeStudentFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import com.appdevelopement.passinggrade.database.AppDatabase // Ensure proper import

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationItemView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.grade -> {
                    replaceFragment(GradeStudentFragment())
                    true
                }
                else -> false
            }
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNavigationItemView.selectedItemId = R.id.grade  // Default to grade fragment
        }

        // Initialize the database
        CoroutineScope(Dispatchers.IO).launch {
            try {
                db = AppDatabase.getDatabase(applicationContext)
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
