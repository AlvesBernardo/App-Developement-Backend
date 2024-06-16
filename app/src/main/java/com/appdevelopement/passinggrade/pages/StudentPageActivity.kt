package com.appdevelopement.passinggrade.pages

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.StudentAdapter
import com.appdevelopement.passinggrade.dto.StudentDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.jar.Manifest
import kotlin.coroutines.CoroutineContext

class StudentPageActivity : AppCompatActivity() {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var searchView: SearchView
    val courseId = intent?.extras?.getInt("idCourse")


    companion object {
        private const val PICK_FILE_REQUEST_CODE = 1
        private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_page)

        job = Job()


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

        val importButton: Button = findViewById(R.id.btnAddCriteria)
        importButton.setOnClickListener {
            pickFile()
        }

        courseId = intent.extras?.getIN("idCourse")
    }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    pickFile()
                } else {
                    Toast.makeText(requireContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                importExcelData(uri)
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
            Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show()
        }
        studentAdapter.updateData(filteredList)
    }


    private fun filterStudentsByGradeStatus(filter: String) {
        val filteredList: ArrayList<StudentDTO> = when (filter) {
            "Graded" -> ArrayList(studentList.filter { it.isGraded })
            "UnGraded" -> ArrayList(studentList.filter { !it.isGraded })
            else -> ArrayList(studentList)
        }
        studentAdapter.updateData(filteredList)
    }

    private fun importExcelData(uri: Uri) {
        val readFromExcelFile = ReadFromExcelFile(requireContext())
        try {
            val studentDTOList = readFromExcelFile.readFromExcel(uri)
            val studentList = studentDTOList.map { convertToStudentEntity(it) }

            CoroutineScope(Dispatchers.IO).launch {
                studentList.forEach { student ->
                    studentDao.insertStudent(student)
                }
            }

            Toast.makeText(requireContext(), "Data Imported Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error Importing Data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertToStudentEntity(studentDTO: StudentDTO): Student {
        return Student(
            idStudent = 0,
            studentName = studentDTO.studentName,
            studentNumber = studentDTO.studentNumber,
            isGraded = studentDTO.isGraded
        )
    }
}
