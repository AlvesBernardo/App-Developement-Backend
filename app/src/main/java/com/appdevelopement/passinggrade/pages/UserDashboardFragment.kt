package com.appdevelopement.passinggrade.pages

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Exam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDashboardFragment : Fragment() {

  private var exams = listOf<Exam>()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_user_dashboard, container, false)
    val recyclerView: RecyclerView = view.findViewById(R.id.rv_courses)

    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.adapter = ExamAdapter(exams)

    activity
        ?.getSharedPreferences("Authentication", Context.MODE_PRIVATE)
        ?.getInt("idTeacher", -1)
        ?.let { teacherId ->
          lifecycleScope.launch {
            val exams = getExamsForTeacher(requireContext(), teacherId)
            recyclerView.adapter = ExamAdapter(exams)
          }
        }

    return view
  }

  private inner class ExamAdapter(private val examList: List<Exam>) :
      RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
      val itemView =
          LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
      return ExamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
      val exam = examList[position]
      holder.examButton.text = exam.examName
        lifecycleScope.launch{
            holder.courseDescription.text = getCourseDescriptionByCourseId(requireContext(), exam.idCourse)
        }

        Log.d("UserDashboardFragment", "exam ${exam.idCourse}")
      Log.d("UserDashboardFragment", "Clicked on exam with id ${exam.idExam}")
      holder.examButton.setOnClickListener {
        val fragment =
            StudentPageFragment().apply {
              arguments = Bundle().apply { putInt("idExam", exam.idExam) }
            }
        requireActivity().supportFragmentManager.beginTransaction().apply {
          replace(R.id.fragment_container, fragment)
          addToBackStack(null)
          commit()
        }
      }
    }

    override fun getItemCount() = examList.size

    inner class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      val examButton: Button = itemView.findViewById(R.id.btn_course)
        val courseDescription: TextView = itemView.findViewById(R.id.tv_course_description)
    }
  }

  private fun showExamDialog(exam: Exam) {
    AlertDialog.Builder(requireContext())
        .setTitle(exam.examName)
        .setMessage(exam.idExam.toString())
        .setPositiveButton("Close", null)
        .show()
  }

  private suspend fun getExamsForTeacher(context: Context, teacherId: Int): List<Exam> {
    Log.d("TeacherCourses", "Teacher ID: $teacherId")
    val dao = AppDatabase.getDatabase(context).examDao()
    return withContext(Dispatchers.IO) { dao.getExamByTeacher(teacherId) }
  }

    private suspend fun getCourseDescriptionByCourseId(context: Context, courseId: Int): String{
        Log.d("CourseId", "Course ID: $courseId")
        val dao = AppDatabase.getDatabase(context).courseDao()
        return withContext(Dispatchers.IO) { dao.getCourseDescription(courseId) }
    }
}
