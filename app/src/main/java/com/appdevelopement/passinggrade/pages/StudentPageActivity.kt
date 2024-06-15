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
import com.appdevelopement.passinggrade.dto.StudentDTO

class StudentPageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var searchView: SearchView
    val courseId = intent?.extras?.getInt("idCourse")
    private val studentList = arrayListOf(
        StudentDTO("John Doe", 126345, true),
        StudentDTO("Jane Smith", 678890, false),
        StudentDTO("Alice Johnson", 547321, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_page)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        studentAdapter = StudentAdapter(studentList, supportFragmentManager) // Pass the FragmentManager here
        recyclerView.adapter = studentAdapter

        searchView = findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterStudentByNumber(newText)
                return true
            }
        })

        // Spinner
        val filterItems = arrayOf("All", "Graded", "UnGraded")

        val filterAdapter = ArrayAdapter(
            this, R.layout.spinner_item, filterItems
        )

        val spinner: Spinner = findViewById(R.id.spnrFilterBy)
        spinner.adapter = filterAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterStudentsByGradeStatus(filterItems[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun filterStudentByNumber(query: String?) {
        val filteredList: ArrayList<StudentDTO> = if (query.isNullOrEmpty()) {
            studentList
        } else {
            ArrayList(studentList.filter {
                it.studentNumber.toString().contains(query)
            })
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
        }
        studentAdapter.updateData(filteredList)
    }

    private fun filterStudentsByGradeStatus(filter: String) {
        val filteredList: ArrayList<StudentDTO> = when (filter) {
            "Graded" -> ArrayList(studentList.filter { it.isGraded })
            "UnGraded" -> ArrayList(studentList.filter { !it.isGraded })
            else -> ArrayList(studentList) // Again, a copy of the original list
        }
        studentAdapter.updateData(filteredList)
    }
}
