package com.appdevelopement.passinggrade.pages

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.GradingSheetAdapter
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Compentence
import com.appdevelopement.passinggrade.models.Course
import com.appdevelopement.passinggrade.models.Exam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradingSheetFragment : Fragment() {

    // Fields
    private lateinit var db: AppDatabase
    private var mustPassIsToggled: Boolean = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var gradingSheetAdapter: GradingSheetAdapter
    private lateinit var gradingSheetItem: EditText
    private lateinit var addCriteriaBtn: Button
    private lateinit var createSheetBtn: Button
    private lateinit var mustPassToggle: ImageView
    private lateinit var competenceWeight: EditText
    private var maxTotalCompetenceWeight: Int = 99
    private val examsArray = ArrayList<Exam>()

    private var teacherId: Int = -1
    private var selectedCourseId: Int = -1
    private var selectedExamId: Int = -1
    private var examId: Int = -1
    private var studentNumber: Int = -1

    private val competenceList = mutableListOf<Compentence>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        examId = arguments?.getInt("examId") ?: -1
        studentNumber = arguments?.getInt("studentNumber") ?: -1

        val view = inflater.inflate(R.layout.fragment_grading_sheet, container, false)

        val examIdTextView: TextView = view.findViewById(R.id.examIdTextView)
        val studentNumberTextView: TextView = view.findViewById(R.id.studentNumberTextView)

        examIdTextView.text = "Exam ID: $examId"
        studentNumberTextView.text = "Student Number: $studentNumber"

        // DB connection
        db = AppDatabase.getDatabase(requireContext())

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

        // Get the ScrollView from the layout
        val scrollView: ScrollView = view.findViewById(R.id.scrollView)

        // Call the utility function to adjust for keyboard visibility
        gradingSheetItem.adjustForKeyboardGrading(scrollView)
        competenceWeight.adjustForKeyboardGrading(scrollView)

        teacherId = activity?.getSharedPreferences("Authentication", android.content.Context.MODE_PRIVATE)
            ?.getInt("idTeacher", -1) ?: -1

        lifecycleScope.launch {
            val examsArray = getCoursesForTeacher(requireContext(), teacherId)
        }

        lifecycleScope.launch {
            val courses = getCoursesFromDb()

            if (courses != null) {
                // Initialize Spinner
                val courseTitle = courses.map { it.dtTitle }

                val filterAdapter = ArrayAdapter(
                    requireContext(), R.layout.spinner_item, courseTitle
                )

                loadExamsForSelectedCourse(selectedCourseId)

                createSheetBtn.setOnClickListener {
                    lifecycleScope.launch {
                        if (totalCompetenceWeight() > 0) {
                            for (competence in competenceList) {
                                insertCompetenceToDb(competence)
                                Log.d("Competence: ", competence.toString())
                            }
                            removeAllCompetences()
                            Log.d("Competences Size: " + competenceList.size, competenceList.toString())
                        }
                    }
                }
            }
        }

        mustPassToggle.setOnClickListener {
            mustPassIsToggled = !mustPassIsToggled
            updateImageViewState(mustPassToggle)
        }

        // Button to add grading criteria
        addCriteriaBtn.setOnClickListener {
            val text = gradingSheetItem.text.toString()
            val competenceW = competenceWeight.text.toString().toInt()

            if (canAddNewCompetence(competenceW)) {
                if (text.isNotEmpty() && selectedCourseId != -1 && selectedExamId != -1) {
                    val competenceRecord = Compentence(
                        idComptence = 0,
                        idExam = selectedExamId,
                        dtName = text,
                        dtCompetenceWeight = competenceW,
                        dtMustPass = mustPassIsToggled
                    )
                    Log.d("CompetenceRecord", competenceRecord.toString())
                    gradingSheetAdapter.addCriteria(competenceRecord)
                    resetVars()
                }
            } else {
                Toast.makeText(requireContext(), "Error! Max competence weight " + totalCompetenceWeight() + " will be exceeded:", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        fun newInstance(examId: Int, studentNumber: Int): GradingSheetFragment {
            val fragment = GradingSheetFragment()
            val args = Bundle()
            args.putInt("examId", examId)
            args.putInt("studentNumber", studentNumber)
            fragment.arguments = args
            return fragment
        }
    }

    private fun updateImageViewState(mustPassToggle: ImageView?) {
        if (mustPassIsToggled) {
            mustPassToggle?.setImageResource(R.drawable.baseline_check_box_24)
        } else {
            mustPassToggle?.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
        }
    }

    private suspend fun getCoursesFromDb(): List<Course>? {
        return withContext(Dispatchers.IO) {
            db.courseDao().getAllCourses()
        }
    }

    private fun removeAllCompetences() {
        competenceList.clear()
        gradingSheetAdapter.removeAllCriterias()
    }

    private suspend fun insertCompetenceToDb(competence: Compentence) {
        withContext(Dispatchers.IO) {
            db.compentenceDao().insert(competence)
        }
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

    private fun loadExamsForSelectedCourse(courseId: Int) {
        lifecycleScope.launch {
            examsArray.clear()
            examsArray.addAll(getCoursesForTeacher(requireContext(), teacherId))

            if (examsArray.isNotEmpty()) {
                val examNames = examsArray.map { it.examName }

                val examFilterAdapter = ArrayAdapter(
                    requireContext(), R.layout.spinner_item, examNames
                )

                val examSpinner: Spinner? = view?.findViewById(R.id.spnrFilterByExam)
                examSpinner?.adapter = examFilterAdapter

                examSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedExamId = examsArray[position].idExam
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Handle when nothing is selected
                    }
                }
            }
        }
    }

    private suspend fun getExamsFromDb(courseId: Int): List<Exam>? {
        return withContext(Dispatchers.IO) {
            db.examDao().getExamsByCourseId(courseId)
        }
    }

    private suspend fun getCoursesForTeacher(context: android.content.Context, teacherId: Int): List<Exam> {
        Log.d("TeacherCourses", "Teacher ID: $teacherId")
        val dao = AppDatabase.getDatabase(context).examDao()
        return withContext(Dispatchers.IO) {
            dao.getExamByTeacher(teacherId)
        }
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
            scrollView.post {
                scrollView.smoothScrollTo(0, bottom)
            }
        } else {
            // Keyboard is closed
            scrollView.post {
                scrollView.smoothScrollTo(0, 0)
            }
        }
    }
}
