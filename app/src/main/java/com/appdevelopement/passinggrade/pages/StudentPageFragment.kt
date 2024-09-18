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
import com.appdevelopement.passinggrade.dto.StudentWithGradedStatusDTO
import com.appdevelopement.passinggrade.models.ExamStudentCrossRef
import com.appdevelopement.passinggrade.models.Student
import com.appdevelopement.passinggrade.utils.popups.ReadFromExcelFile
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import androidx.lifecycle.lifecycleScope
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
  private lateinit var db: AppDatabase
  private var toDisplayList= ArrayList<StudentWithGradedStatusDTO>()
  private var examId: Int = -1
//  private var toDisplayList = ArrayList<StudentDTO>()

  
  
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
    return view
  }
 
  override fun onResume() {
    super.onResume()

    // Fetch the updated student list when fragment is resumed
    val examId = arguments?.getInt("idExam") ?: -1
    Log.d("Exam clicked on: ", "Clicked on exam with id $examId")
    
    
    db = AppDatabase.getDatabase(requireContext())
    studentDao = db.studentDao()
    examStudentCorssReferecne = db.examStudentCrossReference()
    
    recyclerView = requireView().findViewById(R.id.recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(requireContext())
    recyclerView.setHasFixedSize(true)
    
  
    
    studentAdapter = StudentAdapter(ArrayList(), parentFragmentManager, examId, requireContext(), viewLifecycleOwner)
    recyclerView.adapter = studentAdapter
    
    viewLifecycleOwner.lifecycleScope.launch {
      toDisplayList = fetchStudentsForExam(examId)
      studentAdapter.updateData(ArrayList(toDisplayList))
    }
    
    Log.d("Exam passed to adapter: ", "exam passed to student Adapter $examId")
    
    setupSearchView()
    setupSpinner()
//    studentAdapter = StudentAdapter(toDisplayList, parentFragmentManager, examId, requireContext(), viewLifecycleOwner)
//    recyclerView.adapter = studentAdapter
    
    
    
//    toDisplayList.clear()
//    Log.d("StudentPageFragment cleared", "Updated toDisplayList: ${toDisplayList.size} students")
    
//    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
//      val studentList = getStudentsRelatedToExam(examId).map {  student ->
//        val isGraded = examStudentCorssReferecne.getIsGradedByStudentNumber(student.studentNumber, examId)
//        StudentWithGradedStatusDTO(convertToStudentDto(student), isGraded) }
//
//      withContext(Dispatchers.Main) {
//        println("array has $studentList")
////        toDisplayList.addAll(studentList)
//        studentAdapter.updateData(studentList)
//        studentAdapter.notifyDataSetChanged()
//      }
//    }


//    runBlocking {
//      println("array has $toDisplayList")
//      toDisplayList.addAll(getStudentsRelatedToExam(examId).map { convertToStudentDto(it) })
//    }
//    Log.d("StudentPageFragment populated", "Updated toDisplayList: ${toDisplayList.size} students")
    
//    studentAdapter.notifyDataSetChanged()
    
//
//    searchView = requireView().findViewById(R.id.searchView)
//
//    searchView.setOnQueryTextListener(
//      object : SearchView.OnQueryTextListener {
//        override fun onQueryTextSubmit(query: String?): Boolean {
//          return false
//        }
//
//        override fun onQueryTextChange(newText: String?): Boolean {
//          filterStudentByNumber(newText)
//          return true
//        }
//      })
//
//    // Spinner for filter options
//    val filterItems = arrayOf("All", "Graded", "UnGraded")
//    val filterAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, filterItems)
//
//    val spinner: Spinner = requireView().findViewById(R.id.spnrFilterBy)
//    spinner.adapter = filterAdapter
//
//    spinner.onItemSelectedListener =
//      object : AdapterView.OnItemSelectedListener {
//        override fun onItemSelected(
//          parent: AdapterView<*>?,
//          view: View?,
//          position: Int,
//          id: Long
//        ) {
//          when (position) {
//            0 -> { // "All" selected
//              showAllStudents()
//            }
//            1 -> { // "Graded" selected
//              showGradedStudents()
//            }
//            2 -> { // "UnGraded" selected
//              showUnGradedStudents()
//            }
//          }
//        }
//        override fun onNothingSelected(parent: AdapterView<*>?) {}
//      }
//
    // Import Excel Button
    val importButton: Button = requireView().findViewById(R.id.btnImportSheet)
    importButton.setOnClickListener {
      Log.d("Debug", "importButton clicked")
      pickFile()
    }
    
  }
  
  private fun setupSearchView() {
    searchView = requireView().findViewById(R.id.searchView)
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }
      
      override fun onQueryTextChange(newText: String?): Boolean {
        filterStudentByNumber(newText)
        return true
      }
    })
  }
  
  private fun setupSpinner() {
    val filterItems = arrayOf("All", "Graded", "UnGraded")
    val filterAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, filterItems)
    
    val spinner: Spinner = requireView().findViewById(R.id.spnrFilterBy)
    spinner.adapter = filterAdapter
    
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
          0 -> showAllStudents()
          1 -> showGradedStudents()
          2 -> showUnGradedStudents()
        }
      }
      
      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
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
    val filteredList = if (query.isNullOrBlank()) {
      ArrayList(toDisplayList) // Show all if no query
    } else {
      ArrayList(toDisplayList.filter { it.student.studentNumber.toString().startsWith(query) })
    }
    
    studentAdapter.updateData(filteredList)
  }
  
//  private fun filterStudentByNumber(query: String?) {
////    val studentsWithStatus =  fetchStudentsForExam(examId)
//
//    viewLifecycleOwner.lifecycleScope.launch {
//      val studentsWithStatus = fetchStudentsForExam(examId)
//
//      val tempList =
//
//        if (query.isNullOrBlank()) {
//          studentsWithStatus
//        } else {
//          studentsWithStatus.filter { it.student.studentNumber.toString().startsWith(query) } //.toString()
//        }
//      Log.d("filterStudentByNumber", "tempList $tempList")
//      Log.d("filterStudentByNumber", "Filter Student by Number ${tempList.size}")
//      studentAdapter.updateData(tempList)
//      studentAdapter.notifyDataSetChanged()
//    }
//  }

  private fun importExcelData(uri: Uri) {
    val readFromExcelFile = ReadFromExcelFile(requireContext())
    val examId = arguments?.getInt("idExam") ?: -1
    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
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
        
        val newStudentList = getStudentsRelatedToExam(examId).map { student ->
          val isGraded = examStudentCorssReferecne.getIsGradedByStudentNumber(student.studentNumber, examId)
          StudentWithGradedStatusDTO(convertToStudentDto(student), isGraded)
        }
        
//        val newStudentList =
//            ArrayList(getStudentsRelatedToExam(examId).map { convertToStudentDto(it) })
        val studentsWithStatus =  fetchStudentsForExam(examId)
        Log.d("getStudentsRelatedToExam studentList", "Imported students: $newStudentList")
        withContext(Dispatchers.Main) {
          toDisplayList.clear()
          toDisplayList.addAll(newStudentList)
          studentAdapter.updateData(ArrayList(newStudentList))
          fetchStudentsForExam(examId)
          Toast.makeText(requireContext(), "Data Imported Successfully", Toast.LENGTH_SHORT).show()
        }
        
//        withContext(Dispatchers.Main) {
//          toDisplayList = newStudentList
//          studentAdapter.updateData(newStudentList)
//          fetchStudentsForExam(examId)
//          Toast.makeText(requireContext(), "Data Imported Successfully", Toast.LENGTH_SHORT).show()
//        }
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
    Log.d("getStudentsRelatedToExam", "Fetching students for examId: $examId")
    Log.d("studentsInExamIds", "Fetched students: $studentsInExamIds")
    
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

  // Method to show all students
//  private fun showAllStudents() {
//    // Display all students
//    viewLifecycleOwner.lifecycleScope.launch {
//      val studentsWithStatus = fetchStudentsForExam(examId)
//      studentAdapter.updateData(studentsWithStatus)
//      studentAdapter.notifyDataSetChanged()
//    }
//  }
  private fun showAllStudents() {
    studentAdapter.updateData(ArrayList(toDisplayList))
  }
  
  // Method to show only graded students
  private fun showGradedStudents() {
    // Filter the list to show only graded students
    viewLifecycleOwner.lifecycleScope.launch {
      
      val gradedStudents = toDisplayList.filter { it.isGraded }
      
      Log.d("showGradedStudents", "gradedStudents $gradedStudents")
      Log.d("showGradedStudents", "graded Students by Number ${gradedStudents.size}")
      
      studentAdapter.updateData(ArrayList(gradedStudents))
    }
  }

  // Method to show only ungraded students
  private fun showUnGradedStudents() {
    // Filter the list to show only ungraded students
    viewLifecycleOwner.lifecycleScope.launch {
      val ungradedStudents = toDisplayList.filter { !it.isGraded }
      Log.d("showUnGradedStudents", "gradedStudents $ungradedStudents")
      Log.d("showUnGradedStudents", "graded Students by Number ${ungradedStudents.size}")
      
      studentAdapter.updateData(ArrayList(ungradedStudents))
      studentAdapter.notifyDataSetChanged()
    }
  }
  
  private suspend fun fetchStudentsForExam(examId: Int): ArrayList<StudentWithGradedStatusDTO> {
    return withContext(Dispatchers.IO) {
      val studentsWithStatus = ArrayList<StudentWithGradedStatusDTO>()
      val students = studentDao.getStudentsForExam(examId)
      students.forEach { student ->
        val isGraded = examStudentCorssReferecne.getIsGradedByStudentNumber(student.studentNumber, examId)
        studentsWithStatus.add(StudentWithGradedStatusDTO(convertToStudentDto(student), isGraded))
      }
      studentsWithStatus
    }
  }



//  private fun fetchStudentsForExam(examId: Int): ArrayList<StudentWithGradedStatusDTO> {
//
//  val studentsWithStatus = ArrayList<StudentWithGradedStatusDTO>()
//    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
//
//
//      // Fetch all students related to the exam
//      val students = studentDao.getStudentsForExam(examId)
//      students.forEach { student ->
//        // Fetch the isGraded status from the cross-reference table
//        val isGraded = examStudentCorssReferecne.getIsGradedByStudentNumber(student.studentNumber, examId)
//        studentsWithStatus.add(StudentWithGradedStatusDTO(convertToStudentDto(student), isGraded))
//      }
//      Log.d("fetchStudentsForExam total students", "Students size ${students.size}")
////      withContext(Dispatchers.Main) {
////        Log.d("fetchStudentsForExam total students", "toDSPLAY size before clear ${toDisplayList.size}")
////        toDisplayList.clear()
////        Log.d("fetchStudentsForExam total students", "toDSPLAY size after clear ${toDisplayList.size}")
////        toDisplayList.addAll(studentsWithStatus) // studentList now holds StudentWithGradedStatusDTO
////
////        Log.d("fetchStudentsForExam total students", "toDSPLAY size after initialization w studentsWStatus ${toDisplayList.size}")
////
////      }
//    }
//  return studentsWithStatus
////    GlobalScope.launch(Dispatchers.IO) {
////      val students = studentDao.getStudentsForExam(examId).map { convertToStudentDto(it) }
////      withContext(Dispatchers.Main) {
////        toDisplayList.clear()
////        toDisplayList.addAll(students)
////        studentAdapter.notifyDataSetChanged()
////      }
////    }
//  }
}
