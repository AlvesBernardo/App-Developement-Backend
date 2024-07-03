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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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

  private val importActivityResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        result.data?.data?.also { uri ->
          importExcelData(uri)
        }
      }
    }

  private val requestPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
      if (isGranted) {
        pickFile()
      } else {
        Toast.makeText(
          requireContext(),
          "Permission denied to read your External storage",
          Toast.LENGTH_SHORT
        ).show()
      }
    }

  companion object {
    private const val PICK_FILE_REQUEST_CODE = 1
    private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 2
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.student_page, container, false)
    val examId = arguments?.getInt("idExam") ?: -1

    db = AppDatabase.getDatabase(requireContext())
    studentDao = db.studentDao()
    examStudentCorssReferecne = db.examStudentCrossReference()

    recyclerView = view.findViewById(R.id.recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(requireContext())
    recyclerView.setHasFixedSize(true)

    studentAdapter = StudentAdapter(toDisplayList, parentFragmentManager, examId)
    recyclerView.adapter = studentAdapter

    fetchStudentsForExam(examId)

    toDisplayList.clear()
    runBlocking {
      println("array has $toDisplayList")
      toDisplayList.addAll(getStudentsRelatedToExam(examId).map { convertToStudentDto(it) })
    }
    studentAdapter.notifyDataSetChanged()

    searchView = view.findViewById(R.id.searchView)

    searchView.setOnQueryTextListener(
        object : SearchView.OnQueryTextListener {
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
    val filterAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, filterItems)

    val spinner: Spinner = view.findViewById(R.id.spnrFilterBy)
    spinner.adapter = filterAdapter

    spinner.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
          override fun onItemSelected(
              parent: AdapterView<*>?,
              view: View?,
              position: Int,
              id: Long
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
        requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
        PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
          requireActivity(),
          arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
          READ_EXTERNAL_STORAGE_REQUEST_CODE)
    } else {
      pickFile()
    }
  }

  private fun pickFile() {
    val intent =
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
          addCategory(Intent.CATEGORY_OPENABLE)
          type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        }
    importActivityResult.launch(intent)
  }


  private fun filterStudentByNumber(query: String?) {
    val tempList =
        if (query.isNullOrBlank()) {
          ArrayList(studentList)
        } else {
          ArrayList(studentList.filter { it.studentNumber.toString().startsWith(query.toString()) })
        }
    studentAdapter.updateData(tempList)
  }

  private fun importExcelData(uri: Uri) {
    val readFromExcelFile = ReadFromExcelFile(requireContext())
    val examId = arguments?.getInt("idExam") ?: -1
    GlobalScope.launch(Dispatchers.IO) {
      try {
        db.withTransaction {
          val existingExam = db.examDao().getExamsByCourseId(examId)
          if (existingExam != null) {
            val studentDTOList = readFromExcelFile.readFromExcel(uri)
            for (studentDTO in studentDTOList) {
              val student = convertToStudentEntity(studentDTO)
              val existingStudent = studentDao.findStudent(student.studentNumber)
              if (existingStudent == null) {
                studentDao.insertStudent(student)
              }
              val existingCrossRef =
                  examStudentCorssReferecne.getSpecificCrossRef(
                      examId = examId, studentNumber = student.studentNumber)
              if (existingCrossRef == null) {
                val crossRef = ExamStudentCrossRef(examId, student.studentNumber)
                examStudentCorssReferecne.insert(crossRef)
              }
            }
          } else { // Exam does not exist, return
            withContext(Dispatchers.Main) {
              Toast.makeText(requireContext(), "Exam does not exist", Toast.LENGTH_SHORT).show()
            }
          }
        } // End of the transaction

        val newStudentList =
            ArrayList(getStudentsRelatedToExam(examId).map { convertToStudentDto(it) })
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
    val studentsInExamIds = runBlocking {
      examStudentCorssReferecne.getStudentNumbersForExam(examId)
    }

    return runBlocking {
      studentsInExamIds
          .mapNotNull { studentNumber ->
            async(Dispatchers.IO) { studentDao.findStudent(studentNumber) }
          }
          .awaitAll()
          .filterNotNull()
    }
  }

  private fun convertToStudentDto(student: Student): StudentDTO {
    return StudentDTO(
        student.studentNumber,
        student.studentName,
    )
  }

  private fun convertToStudentEntity(studentDto: StudentDTO): Student {
    return Student(
        studentNumber = studentDto.studentNumber,
        studentName = studentDto.studentName,
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
