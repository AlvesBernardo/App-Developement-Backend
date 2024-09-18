package com.appdevelopement.passinggrade.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.dto.StudentDTO
import com.appdevelopement.passinggrade.pages.grading.GradeStudentFragment
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StudentAdapter(
    private var studentArrayList: ArrayList<StudentDTO>,
    private val fragmentManager: FragmentManager,
    private val examId: Int,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    val v: View =
        LayoutInflater.from(viewGroup.context).inflate(R.layout.item_student_main, viewGroup, false)
    return ViewHolder(v)
  }

  override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    val student = studentArrayList[position]
    viewHolder.tvStudentName.text = student.studentName
    viewHolder.tvStudentNumber.text = student.studentNumber.toString()
    
    
    lifecycleOwner.lifecycleScope.launch {
      val isGraded = getIsGradedByStudentNumber(context, student.studentNumber, examId)
      updateGradedImage(viewHolder.ivGraded, isGraded)
    }
    // Set OnClickListener for btnChangeGrade
    viewHolder.btnChangeGrade.setOnClickListener {
      val gradeStudentFragment = GradeStudentFragment()

      // Pass the examId and studentId to the GradeStudentFragment
      val args = Bundle()
      args.putInt("examId", examId)
      args.putInt("studentId", student.studentNumber)
      gradeStudentFragment.arguments = args

      val transaction = fragmentManager.beginTransaction()
      transaction.replace(R.id.fragment_container, gradeStudentFragment)
      transaction.addToBackStack(null)
      transaction.commit()
    }
  }

  override fun getItemCount(): Int {
    return studentArrayList.size
  }

  fun updateData(newStudentList: List<StudentDTO>) {
    studentArrayList = ArrayList(newStudentList)
    notifyDataSetChanged()
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
    val tvStudentNumber: TextView = itemView.findViewById(R.id.tvStudentNumber)
    val ivGraded: ImageView = itemView.findViewById(R.id.ivGraded)
    val btnChangeGrade: Button = itemView.findViewById(R.id.btnChangeGrade)
  }
  
  private fun updateGradedImage(imageView: ImageView, isGraded: Boolean) {
    // Update the image based on the grading status
    if (isGraded) {
      imageView.setImageResource(R.drawable.baseline_check_box_24) // Replace with your graded image resource
    } else {
      imageView.setImageResource(R.drawable.baseline_check_box_outline_blank_24) // Replace with your not-graded image resource
    }
  }
  
  private suspend fun getIsGradedByStudentNumber(context: Context, studentNumber: Int, examId: Int): Boolean{
    
    val dao = AppDatabase.getDatabase(context).examStudentCrossReference()
    return withContext(IO) { dao.getIsGradedByStudentNumber(studentNumber, examId) }
  }
}
