package com.appdevelopement.passinggrade

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.pages.GradeStudent
import com.appdevelopement.passinggrade.pages.StudentPageActivity
import org.ktorm.database.Database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val studnetPageButton = findViewById<Button>(R.id.studentPage)
        val gradingStudentButton =findViewById<Button>(R.id.graddingStudent)


        studnetPageButton.setOnClickListener{
            val intent = Intent(this, StudentPageActivity::class.java)
            startActivity(intent)
        }

        gradingStudentButton.setOnClickListener{
            val intent = Intent(this, GradeStudent::class.java)
            startActivity(intent)
        }

//        val intend = Intent(this, GradeStudent::class.java)
//        startActivity(intend)
//        finish()
    }
}
