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
            val student = withContext(Dispatchers.IO) {
                db.studentDao().findStudent(1)  // replace with actual studentId
            }

            val criterias = withContext(Dispatchers.IO) {
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
                    val criterionLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        orientation = LinearLayout.VERTICAL
                        setPadding(10, 10, 10, 10) // set padding
                    }

                    // Create a layout for SeekBar section
                    val seekBarLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        orientation = LinearLayout.HORIZONTAL
                    }

                    // Add TextView to seekBarLayout
                    seekBarLayout.addView(TextView(context).apply {
                        text = criterion
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        )
                        textSize = 20f
                    })
                    val progressTextView = TextView(context).apply { layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) }

                    seekBarLayout.addView(SeekBar(context).apply {
                        max = 100
                        progress = 0
                        val heightInDp = 30
                        val scale = resources.displayMetrics.density
                        val heightInPixels = (heightInDp * scale + 0.5f).toInt()
                        layoutParams = LinearLayout.LayoutParams(0, heightInPixels, 1f)

                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                val steps = arrayOf(5,10,15,20,25,30,35,40,45,50,75,100)
                                val closestStep = steps.minByOrNull { kotlin.math.abs(it - progress) } ?: 0
                                seekBar?.progress = closestStep
                                progressTextView.text = "$closestStep%"
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                        })
                    })

                    seekBarLayout.addView(progressTextView)

                    // Add seekBarLayout to top level criterionLayout
                    criterionLayout.addView(seekBarLayout)

                    // Add Button to top level criterionLayout
                    criterionLayout.addView(Button(context).apply {
                        text = "Add comment"
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        setOnClickListener {
                            // You need to handle the comment popup here
                        }
                    })

                    // Add Criterion layout to grading area layout
                    gradingAreaLayout.addView(criterionLayout)

                }
            }
        }
    }
}