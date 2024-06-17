package com.appdevelopement.passinggrade.pages

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.StudentAdapter
import com.appdevelopement.passinggrade.dao.StudentDao
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.dto.StudentDTO
import com.appdevelopement.passinggrade.models.Student
import com.appdevelopement.passinggrade.utils.popups.ReadFromExcelFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class StudentPageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var searchView: SearchView
    private lateinit var studentList: ArrayList<StudentDTO>
    private lateinit var studentDao: StudentDao
    private var examId: Int = -1

    companion object {
        private const val PICK_FILE_REQUEST_CODE = 1
        private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            examId = it.getInt("idExam", -1)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.student_page, container, false)
        Log.d("StudentPageFragment", "Loading students for exam ID: $examId")
        // Initialize studentDao
        val db = AppDatabase.getDatabase(requireContext())
        studentDao = db.studentDao()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        studentList = arrayListOf(
            StudentDTO("John Doe", 126345, true),
            StudentDTO("Jane Smith", 678890, false),
            StudentDTO("Alice Johnson", 547321, true)
        )

        studentAdapter = StudentAdapter(studentList, parentFragmentManager)
        recyclerView.adapter = studentAdapter

        searchView = view.findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
            requireContext(), R.layout.spinner_item, filterItems
        )

        val spinner: Spinner = view.findViewById(R.id.spnrFilterBy)
        spinner.adapter = filterAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                filterStudentsByGradeStatus(filterItems[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Import Excel Button
        val importButton: Button = view.findViewById(R.id.btnImportSheet)
        importButton.setOnClickListener {
            pickFile()
        }

        return view
    }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    pickFile()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permission denied to read your External storage",
                        Toast.LENGTH_SHORT
                    ).show()
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
