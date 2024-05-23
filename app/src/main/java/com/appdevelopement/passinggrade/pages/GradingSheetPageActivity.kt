package com.appdevelopement.passinggrade.pages

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.StudentAdapter
import com.appdevelopement.passinggrade.dto.CourseDto
import com.appdevelopement.passinggrade.dto.StudentDto
class GradingSheetPageActivity : AppCompatActivity(){

    //Fields
    private lateinit var recyclerView: RecyclerView

    private val courseList = arrayListOf(
        CourseDto(1, "OOP 1"),
        CourseDto(2, "OOP 2"),
        CourseDto(3, "Software Quality")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grading_sheet)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)


        //Spinner
        val filterItems = courseList

        val filterAdapter = ArrayAdapter(
            this, R.layout.spinner_item, filterItems
        )

        val spinner: Spinner = findViewById(R.id.spnrFilterByCourse)
        spinner.adapter = filterAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                filterStudentsByGradeStatus(filterItems[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
}