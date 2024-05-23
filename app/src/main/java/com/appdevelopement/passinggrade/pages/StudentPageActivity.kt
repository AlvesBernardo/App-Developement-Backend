
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
import com.appdevelopement.passinggrade.dto.StudentDto

class StudentPageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
//    private lateinit var etStudentNumberField: EditText
    private lateinit var searchView: SearchView
    private val studentList = arrayListOf(
        StudentDto("John Doe", 126345, true),
        StudentDto("Jane Smith", 678890, false),
        StudentDto("Alice Johnson", 547321, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_page)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        studentAdapter = StudentAdapter(studentList)
        recyclerView.adapter = studentAdapter

        searchView = findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterStudentByNumber(newText)
//                filterStudentByName(newText)
                return true
            }
        })

        //Spinner
        val filterItems = arrayOf("All", "Graded", "UnGraded")

        val filterAdapter = ArrayAdapter(
            this, R.layout.spinner_item, filterItems
        )

        val spinner: Spinner = findViewById(R.id.spnrFilterBy)
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

        private fun filterStudentByNumber(query: String?) {
        val filteredList: ArrayList<StudentDto> = if (query.isNullOrEmpty()) {
            studentList
        } else {
            ArrayList( studentList.filter {
                it.studentNumber.toString().contains(query)
            })
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
        }
        studentAdapter.updateData(filteredList)
    }

//    private fun filterStudentByName(query: String?){
//
//        val filteredList = ArrayList<StudentDto>()
//        if(query != null){
//
//
//            for(it in studentList){
//
//                if(it.studentName.lowercase(Locale.ROOT).contains(query)){
//                    filteredList.add(it)
//                }
//            }
//            if(filteredList.isEmpty()){
//                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
//            }else{
//                studentAdapter.updateData(filteredList)
//            }
//        }
//    }
    private fun filterStudentsByGradeStatus(filter: String) {
        val filteredList: ArrayList<StudentDto> = when (filter) {
            "Graded" -> ArrayList(studentList.filter { it.isGraded })
            "UnGraded" -> ArrayList(studentList.filter { !it.isGraded })
            else -> ArrayList(studentList) // Again, a copy of the original list
        }
        studentAdapter.updateData(filteredList)
    }
}


//        etStudentNumberField = findViewById(R.id.etStudentNumberField)
//
//        etStudentNumberField.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val filteredStudents: ArrayList<StudentDto> = if (s.isNullOrEmpty()) {
//                    studentList
//                } else ({
//                    studentList.filter { it.studentNumber.toString().contains(s.toString()) }
//                }) as ArrayList<StudentDto>
//                studentAdapter.updateData(filteredStudents)
//            }
//            override fun afterTextChanged(s: Editable?) {}
//        })




//class StudentPageActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var studentAdapter: StudentAdapter
//    private val studentList = arrayListOf(
//        StudentDto("John Doe", 126345, true),
//        StudentDto("Jane Smith",678890, false),
//        StudentDto("Alice Johnson", 547321, true)
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.student_page)
//
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        studentAdapter = StudentAdapter(studentList)
//        recyclerView.adapter = studentAdapter
//
//        val filterItems = arrayOf("All", "Graded", "UnGraded")
//
//        val filterAdapter = ArrayAdapter(
//            this, R.layout.spinner_item, filterItems
//        )
//
//        val spinner: Spinner = findViewById(R.id.spnrFilterBy)
//        spinner.adapter = filterAdapter
//
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                filterStudents(filterItems[position])
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Do nothing
//            }
////        findViewById<Spinner>(R.id.spnrFilterBy).adapter = filterAdapter
//    }
//
//    private fun filterStudents(filter: String) {
//        val filteredList: ArrayList<StudentDto> = when (filter) {
//            "All" -> ArrayList(studentList) // Make a copy of the original list
//            "Graded" -> ArrayList(studentList.filter { it.isGraded })
//            "UnGraded" -> ArrayList(studentList.filter { !it.isGraded })
//            else -> ArrayList(studentList) // Again, a copy of the original list
//        }
//        studentAdapter.updateData(filteredList)
//    }
//
//}
//}

//LATEST VERSIONN
//package com.appdevelopement.passinggrade.pages
//
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.SearchView
//import android.widget.Spinner
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.appdevelopement.passinggrade.R
//import com.appdevelopement.passinggrade.adapters.StudentAdapter
//import com.appdevelopement.passinggrade.dto.StudentDto
//import java.util.Locale
//
//class StudentPageActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var studentAdapter: StudentAdapter
//    private lateinit var svStudentNameSearch: SearchView
//    private val studentList = arrayListOf(
//        StudentDto("John Doe", 126345, true),
//        StudentDto("Jane Smith", 678890, false),
//        StudentDto("Alice Johnson", 547321, true)
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.student_page)
//
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)
//
//        studentAdapter = StudentAdapter(studentList)
//        recyclerView.adapter = studentAdapter
//
//        svStudentNameSearch = findViewById(R.id.svStudentNameSearch)
//
//        svStudentNameSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                filterStudentByName(newText)
//                return true
//            }
//        })
//
//        // Spinner
//        val filterItems = arrayOf("All", "Graded", "UnGraded")
//
//        val filterAdapter = ArrayAdapter(
//            this, R.layout.spinner_item, filterItems
//        )
//
//        val spinner: Spinner = findViewById(R.id.spnrFilterBy)
//        spinner.adapter = filterAdapter
//
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                filterStudentsByGradeStatus(filterItems[position])
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Do nothing
//            }
//        }
//    }
//
//    private fun filterStudentByName(query: String?) {
//        val filteredList: ArrayList<StudentDto> = if (query.isNullOrEmpty()) {
//            studentList
//        } else {
//            ArrayList( studentList.filter {
//                it.studentNumber.toString().contains(query)
//            })
//        }
//        if (filteredList.isEmpty()) {
//            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
//        }
//        studentAdapter.updateData(filteredList)
//    }
//
//    private fun filterStudentsByGradeStatus(filter: String) {
//        val filteredList: ArrayList<StudentDto> = when (filter) {
//            "Graded" -> ArrayList(studentList.filter { it.isGraded })
//            "UnGraded" -> ArrayList(studentList.filter { !it.isGraded })
//            else -> ArrayList(studentList)
//        }
//        studentAdapter.updateData(filteredList)
//    }
//}
