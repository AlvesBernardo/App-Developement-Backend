import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.material.AlertDialog
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Exam
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import com.appdevelopement.passinggrade.utils.popups.CommentPopUpHandler


class GradeStudentFragment : Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.grade_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDatabase(requireContext())

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

            // Add SeekBar for each percentage
            val seekBar = SeekBar(context).apply {
                max = 100
                progress = 0
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }
            val percentageTextView = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "0%"
            }
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val steps = arrayOf(20, 50, 75, 100)
                    val closestStep = steps.minByOrNull { Math.abs(it - progress) } ?: 0
                    seekBar?.progress = closestStep
                    percentageTextView.text = "$closestStep%"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            criterionLayout.addView(seekBar)
            criterionLayout.addView(percentageTextView)

            // Add EditText for comments
            val commentButton = Button(context).apply {
                text = "Add comment"
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setOnClickListener {
                    CommentPopUpHandler(context).showCommentPopUp(criterion)
                }
            }

            criterionLayout.addView(commentButton)

            gradingAreaLayout.addView(criterionLayout)



    }


    }

//        val submitButton = view.findViewById<Button>(R.id.button)
//        submitButton.setOnClickListener{
//            val grade = calculateGrade(gradingAreaLayout)
////
////            val exam = Exam(
////                examId = 0,
////                idTeacher = 1,
////                idStudent =1,
////                grade =
////            )
//
//            viewLifecycleOwner.lifecycleScope.launch{
//            withContext(Dispatchers.IO) {
//                    db.examDao().insertExam(Exam)
//                }
//            }
//        }



//    private fun calculateGrade(gradingAreaLayout: LinearLayout): Int {
//        var totalPercentage = 0
//        var criteriaCount = 0
//
//        for (i in 1 until gradingAreaLayout.childCount) {
//            val criterionLayout = gradingAreaLayout.getChildAt(i) as LinearLayout
//            var criterionPercentage = 0
//
//            for (j in 1 until criterionLayout.childCount - 1) {
//                val checkBox = criterionLayout.getChildAt(j) as CheckBox
//                if (checkBox.isChecked) {
//                    criterionPercentage = when (checkBox.text.toString()) {
//                        "20%" -> 20
//                        "50%" -> 50
//                        "70%" -> 70
//                        "100%" -> 100
//                        else -> 0
//                    }
//                }
//            }
//
//            if (criterionPercentage > 0) {
//                totalPercentage += criterionPercentage
//                criteriaCount++
//            }
//        }
//
//        return if (criteriaCount > 0) totalPercentage / criteriaCount else 0
//    }
}
