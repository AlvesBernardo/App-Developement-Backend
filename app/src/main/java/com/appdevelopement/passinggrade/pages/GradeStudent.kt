package com.appdevelopement.passinggrade.pages

import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.appdevelopement.passinggrade.R

class GradeStudent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grade_student)

        val studentNameBox = findViewById<TextView>(R.id.StudentName)
        val gradingCriteria = arrayOf(
            "Coding conventions", "Testing", "Completion",
            "Functions", "Idk I dont know compenetenes", "More", "Even more"
        )
        val gradingAreaLayout = findViewById<LinearLayout>(R.id.competencyContainer)
        studentNameBox.text = "Bernardo Josué Correia Alves"    

        // Create a horizontal LinearLayout for the header
        val headerLayout = LinearLayout(this)
        headerLayout.orientation = LinearLayout.HORIZONTAL
        headerLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Add a blank view to the header
        val blankView = TextView(this)
        blankView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        headerLayout.addView(blankView)

        // Add a TextView for comments to the header
        val commentsTextView = TextView(this)
        commentsTextView.text = "Comments"
        headerLayout.addView(commentsTextView)

        // Add the header to the grading area layout
        gradingAreaLayout.addView(headerLayout)

        for (criterion in gradingCriteria) {
            // Create a horizontal LinearLayout for each criterion
            val criterionLayout = LinearLayout(this)
            criterionLayout.orientation = LinearLayout.HORIZONTAL
            criterionLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // Create a TextView for the criterion name
            val criterionTextView = TextView(this)
            criterionTextView.text = criterion
            criterionTextView.textSize = 20f
            criterionTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )

            // Add the TextView to the criterion layout
            criterionLayout.addView(criterionTextView)

            // Add CheckBoxes for each percentage
            val percentages = arrayOf("20%", "50%", "70%", "100%")
            for (percentage in percentages) {
                val checkBox = CheckBox(this)
                checkBox.text = percentage
                checkBox.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                criterionLayout.addView(checkBox)
            }

            // Add EditText for comments
            val commentsEditText = EditText(this)
            commentsEditText.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            commentsEditText.setBackgroundResource(android.R.color.white)
            criterionLayout.addView(commentsEditText)

            // Add the criterion layout to the grading area layout
            gradingAreaLayout.addView(criterionLayout)
        }
    }
}
