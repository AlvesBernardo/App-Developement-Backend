package com.appdevelopement.passinggrade.pages

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.GradingSheetAdapter
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Compentence
import com.appdevelopement.passinggrade.models.Exam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradingSheetFragment : Fragment() {
  
  private lateinit var db: AppDatabase
  private var mustPassIsToggled: Boolean = false
  private lateinit var recyclerView: RecyclerView
  private lateinit var gradingSheetAdapter: GradingSheetAdapter
  private lateinit var gradingSheetItem: EditText
  private lateinit var addCriteriaBtn: Button
  private lateinit var createSheetBtn: Button
  private lateinit var mustPassToggle: ImageView
  private lateinit var competenceWeight: EditText
  private var maxTotalCompetenceWeight: Int = 100
  private val examsList = mutableListOf<Exam>()
  private var teacherId: Int = -1
  private var selectedExamId: Int = -1
  private val competenceList = mutableListOf<Compentence>()

  @SuppressLint("MissingInflatedId")
  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    teacherId =
        activity
            ?.getSharedPreferences("Authentication", android.content.Context.MODE_PRIVATE)
            ?.getInt("idTeacher", -1) ?: -1

    val view = inflater.inflate(R.layout.fragment_grading_sheet, container, false)

    // DB connection
    db = AppDatabase.getDatabase(requireContext())
    
    val scrollView: ScrollView = view.findViewById(R.id.scrollView)

    recyclerView = view.findViewById(R.id.recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(requireContext())
    recyclerView.setHasFixedSize(true)

    gradingSheetAdapter = GradingSheetAdapter(competenceList)
    recyclerView.adapter = gradingSheetAdapter

    gradingSheetItem = view.findViewById(R.id.etGradingCriteria)
    addCriteriaBtn = view.findViewById(R.id.btnAddCriteria)
    createSheetBtn = view.findViewById(R.id.btnCreateSheet)
    mustPassToggle = view.findViewById(R.id.ivMustPassToggle)
    competenceWeight = view.findViewById(R.id.etCriteriaWeight)
    

    // Call the utility function to adjust for keyboard visibility|| to put the view above the keyboard
    gradingSheetItem.adjustForKeyboardGrading(scrollView)
    competenceWeight.adjustForKeyboardGrading(scrollView)

    lifecycleScope.launch { loadExams() }

    mustPassToggle.setOnClickListener {
      mustPassIsToggled = !mustPassIsToggled
      updateImageViewState(mustPassToggle)
    }

    // Button to add grading criteria
    addCriteriaBtn.setOnClickListener {
      val text = gradingSheetItem.text.toString()
      val competenceW = competenceWeight.text.toString().toInt()
      
      if (text.isNotEmpty() && competenceW != null && selectedExamId != -1) {
        if (canAddNewCompetence(competenceW)) {
          val competenceRecord = Compentence(
            idComptence = 0,
            idExam = selectedExamId,
            dtName = text,
            dtCompetenceWeight = competenceW,
            dtMustPass = mustPassIsToggled
          )
          Log.d("CompetenceRecord", competenceRecord.toString())
          gradingSheetAdapter.addCompetence(competenceRecord)
          resetVars()
        } else {
          Toast.makeText(requireContext(), "Error! Max total weight exceeded.", Toast.LENGTH_SHORT).show()
        }
      } else {
        Toast.makeText(requireContext(), "Please enter valid competence and weight.", Toast.LENGTH_SHORT).show()
      }
    }

    createSheetBtn.setOnClickListener {
  
      lifecycleScope.launch {
        if (totalCompetenceWeight() == 100 && selectedExamId != -1) {
          withContext(Dispatchers.IO) {
            db.examStudentCrossReference().resetAllGradedStatusforExam(selectedExamId)
            
            db.compentenceDao().deleteCompetencesByExamId(selectedExamId) // Deletes existing competences by examId
            
            competenceList.forEach { competence ->
              db.compentenceDao().insert(competence)
            }
          }
          Log.d("Competences Size: " + competenceList.size, competenceList.toString())
          Toast.makeText(requireContext(), "Grading Sheet created successfully!", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(requireContext(), "Error! Total weight must equal 100%", Toast.LENGTH_SHORT).show()
        }
      }
    }

    return view
  }

  private fun loadExams() {
    lifecycleScope.launch {
      getExamsForTeacher(requireContext(), teacherId)?.let { examsList.addAll(it) }
      Log.d("UpdateExamList: ", "$examsList")

      if (examsList.isNotEmpty()) {
        val examNames = examsList.map { it.examName }

        val examFilterAdapter = ArrayAdapter(requireContext(), R.layout.grading_sheet_spinner_item, examNames)

        val examSpinner: Spinner? = view?.findViewById(R.id.spnrFilterByExam)
        examSpinner?.adapter = examFilterAdapter

        examSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
              override fun onItemSelected(
                  parent: AdapterView<*>?,
                  view: View?,
                  position: Int,
                  id: Long
              ) {
                selectedExamId = examsList[position].idExam
                
                lifecycleScope.launch {
                  val competences = getCompetencesOfSelectedExam()
                  addAllCompetences(competences)
                }
              }
              override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
      }
    }
  }

  private suspend fun getCompetencesOfSelectedExam(): List<Compentence>? {
    return withContext(Dispatchers.IO) { db.compentenceDao().getCompetencesForExam(selectedExamId) }
  }

  private fun updateImageViewState(mustPassToggle: ImageView?) {
    if (mustPassIsToggled) {
      mustPassToggle?.setImageResource(R.drawable.baseline_check_box_24)
    } else {
      mustPassToggle?.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
    }
  }

  private fun addAllCompetences(newCompetenceList: List<Compentence>?) {
    competenceList.clear()
    if (newCompetenceList != null) {
      competenceList.addAll(newCompetenceList)
      gradingSheetAdapter.addAllCompetences(newCompetenceList)
    }
  }

  private fun removeAllCompetences() {
    competenceList.clear()
    gradingSheetAdapter.removeAllCompetences()
  }

  private suspend fun insertCompetenceToDb(competence: Compentence) {
    withContext(Dispatchers.IO) { db.compentenceDao().insert(competence) }
  }

  private fun totalCompetenceWeight(): Int {
    var totalWeight = 0

    for (competence in competenceList) {
      totalWeight += competence.dtCompetenceWeight
    }

    return totalWeight
  }

  private fun canAddNewCompetence(competenceWeight: Int): Boolean {

    val newTotalCompetenceWeight = totalCompetenceWeight() + competenceWeight
    return newTotalCompetenceWeight <= maxTotalCompetenceWeight
    
  }

  private fun resetVars() {
    gradingSheetItem.text.clear()
    competenceWeight.text.clear()
    mustPassToggle.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
  }

  private suspend fun getExamsFromDb(courseId: Int): List<Exam>? {
    return withContext(Dispatchers.IO) { db.examDao().getExamsByCourseId(courseId) }
  }

  private suspend fun getExamsForTeacher(
      context: android.content.Context,
      teacherId: Int
  ): List<Exam>? {
    Log.d("TeacherCourses", "Teacher ID: $teacherId")
    val dao = AppDatabase.getDatabase(context).examDao()
    return withContext(Dispatchers.IO) { dao.getExamByTeacher(teacherId) }
  }
}

fun View.adjustForKeyboardGrading(scrollView: ScrollView) {
  viewTreeObserver.addOnGlobalLayoutListener {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom

    if (keypadHeight > screenHeight * 0.15) {
      // Keyboard is opened
      scrollView.post { scrollView.smoothScrollTo(0, bottom) }
    } else {
      // Keyboard is closed
      scrollView.post { scrollView.smoothScrollTo(0, 0) }
    }
  }
}
