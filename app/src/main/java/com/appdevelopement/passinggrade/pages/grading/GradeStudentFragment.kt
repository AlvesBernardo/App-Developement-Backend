package com.appdevelopement.passinggrade.pages.grading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GradeStudentFragment : Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.grade_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getDatabase(requireContext())
        val studentNameBox = view.findViewById<TextView>(R.id.StudentName)
        val gradingAreaLayout = view.findViewById<LinearLayout>(R.id.competencyContainer)

        lifecycleScope.launch {
            val student = withContext(Dispatchers.IO){
                db.studentDao().findStudent(1)  // replace with actual studentId
            }

            val criterias = withContext(Dispatchers.IO){
                db.compentenceDao().getCompetencesForExam(1)  // replace with actual examId
            }

            val gradingCriteria = criterias.map { it.dtName } // change "dtName" if it's not the correct field

            withContext(Dispatchers.Main) {
                if (student != null) {
                    studentNameBox.text = student.studentName
                } // change "name" if it's not the correct field

                gradingCriteria.forEach { criterion ->
                    // ... add TextView for each criterion to gradingAreaLayout
                    // change "criterion" in TextView setup if that's not what you want displayed
                    val crtiterionLayout = LinearLayout(context).apply {
                        // Implementation based on your existing code
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        orientation = LinearLayout.HORIZONTAL

                        addView( TextView(context).apply{
                            text = criterion
                            layoutParams = LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1f
                            )
                        })
                        // Add other views to criterionLayout as per your requirement
                    }

                    // Add Criterion layout to grading area layout
                    gradingAreaLayout.addView(crtiterionLayout)
                }
            }
            }
        }
}