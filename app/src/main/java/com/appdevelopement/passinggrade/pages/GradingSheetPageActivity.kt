package com.appdevelopement.passinggrade.pages

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.GradingSheetAdapter
import com.appdevelopement.passinggrade.dto.CourseDto
import com.appdevelopement.passinggrade.dto.GradingSheetDto

class GradingSheetPageActivity : AppCompatActivity(){

    //Fields
    private lateinit var recyclerView: RecyclerView
    private lateinit var gradingSheetAdapter: GradingSheetAdapter

    private val courseList = arrayListOf(
        CourseDto("as", "OOP 1"),
        CourseDto("sd", "OOP 2"),
        CourseDto("sds", "Software Quality")
    )

    private val gradingSheetItemList = arrayListOf(
        GradingSheetDto( "Grading Sheet criteria")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grading_sheet)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        gradingSheetAdapter = GradingSheetAdapter(gradingSheetItemList)
        recyclerView.adapter = gradingSheetAdapter

        val gradingSheetItem = findViewById<EditText>(R.id.etGradingCriteria)
        val addCriteriaBttn = findViewById<Button>(R.id.btnAddCriteria)

        //Button to add grading criteria
        addCriteriaBttn.setOnClickListener {
            val text = gradingSheetItem.text.toString()
            if (text.isNotEmpty()) {
                val newGradingSheetItemsId = gradingSheetItemList.size + 1
                gradingSheetItemList.add(GradingSheetDto(newGradingSheetItemsId.toString()))
                gradingSheetAdapter.notifyItemInserted(gradingSheetItemList.size - 1)
                gradingSheetItem.text.clear()
            }
        }

        //Spinner
        val descriptions = courseList.map { it.dtDescription }

        val filterAdapter = ArrayAdapter(
            this, R.layout.spinner_item, descriptions
        )

        val spinner: Spinner = findViewById(R.id.spnrFilterByCourse)
        spinner.adapter = filterAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
}
