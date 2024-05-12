package com.appdevelopement.passinggrade

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.pages.GradeStudent
import org.ktorm.database.Database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intend = Intent(this, GradeStudent::class.java)
        startActivity(intend)
        finish()
    }
}
