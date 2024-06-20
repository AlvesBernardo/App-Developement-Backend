package com.appdevelopement.passinggrade.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.dto.StudentDTO
import com.appdevelopement.passinggrade.pages.grading.GradeStudentFragment

class StudentAdapter(
    private var studentArrayList: ArrayList<StudentDTO>,
    private val fragmentManager: FragmentManager // Add fragment manager parameter
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

    //        val imageDrawableResId = if (student.isGraded) {
    //            R.drawable.baseline_check_box_24
    //        } else {
    //            R.drawable.baseline_check_box_outline_blank_24
    //        }
    //        viewHolder.tvGraded.setImageResource(imageDrawableResId)

    // Set OnClickListener for btnChangeGrade
    viewHolder.btnChangeGrade.setOnClickListener {
      val transaction = fragmentManager.beginTransaction()
      transaction.replace(
          R.id.fragment_container, GradeStudentFragment()) // Adjust fragment_container ID as needed
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
    val tvGraded: ImageView = itemView.findViewById(R.id.tvGraded)
    val btnChangeGrade: Button = itemView.findViewById(R.id.btnChangeGrade)
  }
}
