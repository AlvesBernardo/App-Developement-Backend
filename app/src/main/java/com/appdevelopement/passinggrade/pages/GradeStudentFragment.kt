
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.R

class GradeStudentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.grade_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentNameBox = view.findViewById<TextView>(R.id.StudentName)
        val gradingCriteria = arrayOf(
            "Coding conventions", "Testing", "Completion",
            "Functions", "Idk I dont know compenetenes", "More", "Even more"
        )
        val gradingAreaLayout = view.findViewById<LinearLayout>(R.id.competencyContainer)
        studentNameBox.text = "Bernardo Josué Correia Alves"

        // Create a horizontal LinearLayout for the header
        val headerLayout = LinearLayout(context)
        headerLayout.orientation = LinearLayout.HORIZONTAL
        headerLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Add a blank view to the header
        val blankView = TextView(context)
        blankView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        headerLayout.addView(blankView)

        // Add a TextView for comments to the header
        val commentsTextView = TextView(context)
        commentsTextView.text = "Comments"
        headerLayout.addView(commentsTextView)

        // Add the header to the grading area layout
        gradingAreaLayout.addView(headerLayout)

        for (criterion in gradingCriteria) {
            // Create a horizontal LinearLayout for each criterion
            val criterionLayout = LinearLayout(context)
            criterionLayout.orientation = LinearLayout.HORIZONTAL
            criterionLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // Create a TextView for the criterion name
            val criterionTextView = TextView(context)
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
                val checkBox = CheckBox(context)
                checkBox.text = percentage
                checkBox.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                criterionLayout.addView(checkBox)
            }

            // Add EditText for comments
            val commentsEditText = EditText(context)
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
