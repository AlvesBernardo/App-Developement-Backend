package com.appdevelopement.passinggrade.pages

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.StudentAdapter
import com.appdevelopement.passinggrade.dto.StudentDto

class StudentPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_page)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val studentList = arrayListOf(
            StudentDto("John Doe", 126345, true),
            StudentDto("Jane Smith",678890, false),
            StudentDto("Alice Johnson", 547321, true)
        )

        val studentAdapter = StudentAdapter(studentList)
        recyclerView.adapter = studentAdapter

        val filterItems = arrayOf("All", "Graded", "UnGraded")

        val filterAdapter = ArrayAdapter(
            this, R.layout.spinner_item, filterItems
        )

        findViewById<Spinner>(R.id.spnrFilterBy).adapter = filterAdapter
    }
}