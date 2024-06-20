package com.appdevelopement.passinggrade.pages

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.StudentAdapter
import com.appdevelopement.passinggrade.dao.ExamStudentCorssReferecne
import com.appdevelopement.passinggrade.dao.StudentDao
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.dto.StudentDTO
import com.appdevelopement.passinggrade.models.ExamStudentCrossRef
import com.appdevelopement.passinggrade.models.Student
import com.appdevelopement.passinggrade.utils.popups.ReadFromExcelFile
import kotlinx.coroutines.*
import java.io.IOException

class StudentPageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var searchView: SearchView
    private lateinit var studentDao: StudentDao
    private lateinit var examStudentCorssReferecne: ExamStudentCorssReferecne
    private var studentList = ArrayList<StudentDTO>()
    private lateinit var db: AppDatabase
    private var examId: Int = -1
    private var toDisplayList = ArrayList<StudentDTO>()


    companion object {
        private const val PICK_FILE_REQUEST_CODE = 1
        private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.student_page, container, false)
        val examId = arguments?.getInt("idExam") ?: -1
        fetchStudentsForExam(examId)

        db = AppDatabase.getDatabase(requireContext())
        studentDao = db.studentDao()
        examStudentCorssReferecne = db.examStudentCrossReference()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        studentAdapter = StudentAdapter(toDisplayList, parentFragmentManager)
        recyclerView.adapter = studentAdapter

        toDisplayList.clear()
        runBlocking {
            println("array has $toDisplayList")
            toDisplayList.addAll(getStudentsRelatedToExam(examId).map { convertToStudentDto(it) })
        }
        studentAdapter.notifyDataSetChanged()

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

        // Spinner for filter options
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
               // filterStudentsByGradeStatus(filterItems[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Import Excel Button
        val importButton: Button = view.findViewById(R.id.btnImportSheet)
        importButton.setOnClickListener {
            Log.d("Debug", "importButton clicked")
            pickFile()
        }

        return view
    }

    private fun checkPermissionAndPickFile() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        } else {
            pickFile()
        }
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
                        requireContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT
                    ).show()
                }
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
        val tempList = if (query.isNullOrBlank()) {
            ArrayList(studentList)
        } else {
            ArrayList(studentList.filter {
                it.studentNumber.toString().startsWith(query.toString())
            })
        }
        studentAdapter.updateData(tempList)
    }

//    private fun filterStudentsByGradeStatus(filter: String) {
//        val tempList: ArrayList<StudentDTO> = ArrayList(studentList).apply {
//            when (filter) {
//                "Graded" -> retainAll { it.isGraded }
//                "UnGraded" -> retainAll { !it.isGraded }
//            }
//        }
//        studentAdapter.updateData(tempList)
//    }

    private fun importExcelData(uri: Uri) {
        val readFromExcelFile = ReadFromExcelFile(requireContext())
        val examId = arguments?.getInt("idExam") ?: -1
        GlobalScope.launch(Dispatchers.IO) {
            try {
                db.withTransaction { // Start of the transaction
                    val existingExam = db.examDao().getExamsByCourseId(examId) // Check if exam exists
                    if (existingExam != null) { // Exam exists
                        val studentDTOList = readFromExcelFile.readFromExcel(uri) // Get students from Excel
                        for (studentDTO in studentDTOList) { // Loop through each student
                            val student = convertToStudentEntity(studentDTO) // Convert DTO to Entity
                            val existingStudent = studentDao.findStudent(student.studentNumber) // Check if student is already in the Student Table
                            if (existingStudent == null) { // If student does not exist, insert student
                                studentDao.insertStudent(student)
                            }
                            val existingCrossRef = examStudentCorssReferecne.getSpecificCrossRef(examId = examId, studentNumber = student.studentNumber) // Check if student has not already been added for this exam.
                            if (existingCrossRef == null) { // Only insert new cross ref if it does not already exist.
                                val crossRef = ExamStudentCrossRef(examId, student.studentNumber)
                                examStudentCorssReferecne.insert(crossRef) // Insert into ExamStudentCrossRef for each student
                            }
                        }
                    } else { // Exam does not exist, return
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Exam does not exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                } // End of the transaction

                val newStudentList = ArrayList(getStudentsRelatedToExam(examId).map { convertToStudentDto(it) })
                withContext(Dispatchers.Main) {
                    studentList = newStudentList
                    studentAdapter.updateData(newStudentList)
                    Toast.makeText(requireContext(), "Data Imported Successfully", Toast.LENGTH_SHORT).show()
                    fetchStudentsForExam(examId)

                }

            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error Importing Data", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    private fun getStudentsRelatedToExam(examId: Int): List<Student> {
        // Fetch the students that are participating in the exam from your database
        val studentsInExamIds = runBlocking {
            examStudentCorssReferecne.getStudentNumbersForExam(examId)
        }

        return runBlocking {
            studentsInExamIds.mapNotNull { studentNumber ->
                async(Dispatchers.IO) { studentDao.findStudent(studentNumber) }
            }.awaitAll().filterNotNull()
        }
    }

    private fun convertToStudentDto(student: Student): StudentDTO {
        return StudentDTO(

            student.studentNumber, student.studentName,
        )
    }

    private fun convertToStudentEntity(studentDto: StudentDTO): Student {
        return Student(
            studentNumber = studentDto.studentNumber,
            studentName = studentDto.studentName,
            //isGraded = studentDto.isGraded)
        )
    }
    private fun fetchStudentsForExam(examId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val students = studentDao.getStudentsForExam(examId).map { convertToStudentDto(it) }
            withContext(Dispatchers.Main) {
                toDisplayList.clear()
                toDisplayList.addAll(students)
                studentAdapter.notifyDataSetChanged()
            }
        }
    }

}
