package com.appdevelopement.passinggrade.pages.grading

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.controllers.gradingController.CreateCompetenceGradeUseCase
import com.appdevelopement.passinggrade.controllers.gradingController.GradingUseCase
import com.appdevelopement.passinggrade.controllers.gradingController.UpdateExamGradeUseCase
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.pages.StudentPageFragment
import com.appdevelopement.passinggrade.utils.popups.CommentPopUpHandler
import com.appdevelopement.passinggrade.utils.popups.StudentRecordCreator
import com.appdevelopement.passinggrade.utils.popups.WriteToExcelFile
import com.appdevelopement.passinggrade.utils.popups.calculators.CritertionCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CriterionRecord(val name: String, var progress: Int, var comment: String)

class GradeStudentFragment : Fragment() {

  private lateinit var db: AppDatabase
  private lateinit var createCompetenceGradeUseCase: CreateCompetenceGradeUseCase
  private lateinit var updateExamGradeUseCase: UpdateExamGradeUseCase
  private lateinit var gradingUseCase: GradingUseCase
  private lateinit var fragmentManager: FragmentManager

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    
    return inflater.inflate(R.layout.grade_student, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    db = AppDatabase.getDatabase(requireContext())
    createCompetenceGradeUseCase =
        CreateCompetenceGradeUseCase(db.CompentenceGradeDao(), db.compentenceDao())
    gradingUseCase = GradingUseCase(db)
    updateExamGradeUseCase = UpdateExamGradeUseCase(db.examDao())
      fragmentManager = parentFragmentManager
      
    val studentNameBox = view.findViewById<TextView>(R.id.StudentName)
    val gradingAreaLayout = view.findViewById<LinearLayout>(R.id.competencyContainer)
    val submitButton = view.findViewById<Button>(R.id.button)
    val criterionCalculator = CritertionCalculator()
    val studentRecordCreator = StudentRecordCreator()
    val ivBackButton = view.findViewById<ImageView>(R.id.ivBackButton)


    lifecycleScope.launch {
      val examId = arguments?.getInt("examId") ?: -1
      val studentId = arguments?.getInt("studentId") ?: -1
      Log.d("GradeStudentFragment", "Exam ID: $examId, Student ID: $studentId")

      val student = withContext(IO) { db.studentDao().findStudent(studentId) }

      val criterias =
          withContext(IO) { db.compentenceDao().getCompetencesForExam(examId) }

      val existingGrades =
          withContext(IO) {
            db.CompentenceGradeDao().getGradesForStudentAndExam(studentId, examId)
          }
      
        ivBackButton.setOnClickListener {
            val studentPageFragment = StudentPageFragment()
            
            // Passes the examId and studentId to the GradeStudentFragment
            val args = Bundle()
            args.putInt("idExam", examId)
            studentPageFragment.arguments = args
            
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, studentPageFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

      val gradingCriteria = criterias.map { it.dtName }

      withContext(Dispatchers.Main) {
        if (student != null) {
          studentNameBox.text = student.studentName
        }

        gradingCriteria.forEach { criterion ->
          val competence = criterias.find { it.dtName == criterion }
          val existingGrade = existingGrades.find { it.idComptence == competence?.idComptence }
          val progressValue = (existingGrade?.dtGrade ?: 0.0).toInt()
          val commentValue = existingGrade?.dtComment ?: ""

          val criterionLayout =
              LinearLayout(context).apply {
                layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                orientation = LinearLayout.VERTICAL
                setPadding(10, 10, 10, 10)
              }

          val seekBarLayout =
              LinearLayout(context).apply {
                layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                orientation = LinearLayout.HORIZONTAL
              }

          seekBarLayout.addView(
              TextView(context).apply {
                text = criterion
                layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                textSize = 20f
              })
          val progressTextView =
              TextView(context).apply {
                layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "$progressValue%"
              }

          val record = CriterionRecord(criterion, progressValue, commentValue)
          criterionLayout.tag = record
          seekBarLayout.addView(
              SeekBar(context).apply {
                max = 100
                progress = progressValue
                val heightInDp = 30
                val scale = resources.displayMetrics.density
                val heightInPixels = (heightInDp * scale + 0.5f).toInt()
                layoutParams = LinearLayout.LayoutParams(0, heightInPixels, 1f)

                setOnSeekBarChangeListener(
                    object : SeekBar.OnSeekBarChangeListener {
                      override fun onProgressChanged(
                          seekBar: SeekBar?,
                          progress: Int,
                          fromUser: Boolean
                      ) {
                        val steps = arrayOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 75, 100)
                        val closestStep = steps.minByOrNull { kotlin.math.abs(it - progress) } ?: 0
                        seekBar?.progress = closestStep
                        record.progress = closestStep
                        progressTextView.text = "$closestStep%"
                      }

                      override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                      override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })
              })

          seekBarLayout.addView(progressTextView)
          criterionLayout.addView(seekBarLayout)

          val commentDisplayTextView =
              TextView(context).apply {
                layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                text = commentValue
              }

          criterionLayout.addView(
              Button(context).apply {
                text = "Add comment"
                layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                val commentPopUpHandler = CommentPopUpHandler(context)
                setOnClickListener {
                  commentPopUpHandler.showCommentPopUp(criterion, record.comment) { newComment ->
                    record.comment = newComment
                    commentDisplayTextView.text = newComment
                  }
                }
              })

          gradingAreaLayout.addView(criterionLayout)
        }
      }

      submitButton.setOnClickListener {
        if (student != null) {
          val minimumScore = 5.5
          val totalGrade = criterionCalculator.calculateTotalGrade(gradingAreaLayout, criterias)
          val studentRecord = studentRecordCreator.getStudentRecord(student, totalGrade, gradingAreaLayout)
          val criterionRecords: List<CriterionRecord> =
              gradingAreaLayout.children
                  .filter { it is LinearLayout && it.tag is CriterionRecord }
                  .map { it.tag as CriterionRecord }
                  .toList()
            val comments: List<String> = criterionRecords.map { it.comment }  // Extract comments

            lifecycleScope.launch {
                val examName = getExamName(requireContext(), examId)
                Log.d("submitButton", "exam contains: $examName")
                
                val isPass =
                    gradingUseCase.hasPassedMandatoryCompetences(student.studentNumber) &&
                        totalGrade >= minimumScore
                val grades = criterionRecords.map { it.progress.toDouble() / 10.0 }
                val excelWriter = WriteToExcelFile(requireContext())
                val fileName = examName + " "+ student.studentName + " " + student.studentNumber.toString()
                excelWriter.writeToExcel(
                    fileName, // fileName
                    totalGrade * 10, // totalGrade as Double
                    criterionRecords,
                    comments // List<CriterionRecord>
                    )
                createCompetenceGradeUseCase.execute(criterionRecords, student.studentNumber, examId)
                if (isPass) {
                  
                  withContext(IO) {
                    updateExamGradeUseCase.execute(examId, totalGrade * 10, isPass)
                  }
                }
                studentHasBeenGraded(requireContext(), studentId, examId, true);
                
                Toast.makeText(
                        context,
                        "Graded successfully check your files in download folder",
                        Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
          Toast.makeText(context, "Error: Student is null", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }
    private suspend fun studentHasBeenGraded(context: Context, studentNumber: Int, examId: Int, graded: Boolean = true ) {
        
        val dao = AppDatabase.getDatabase(context).examStudentCrossReference()
        return withContext(IO) { dao.updateIsGraded(studentNumber,  examId, graded) }
    }
    
    private suspend fun getExamName(context: Context, examId:Int): String {
        
        val dao = AppDatabase.getDatabase(context).examDao()
        return withContext(IO) { dao.getExamName(examId)}
    }
}
