package com.appdevelopement.passinggrade.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.dto.StudentDTO

class StudentAdapter(private var studentArrayList: ArrayList<StudentDTO>) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_student_main, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvStudentName.text = studentArrayList[position].studentName
        viewHolder.tvStudentNumber.text = studentArrayList[position].studentNumber.toString()
//        viewHolder.tvGraded.text = studentArrayList[position].isGraded.toString()

        // Set the image drawable based on the isGraded value
        val imageDrawableResId = if (studentArrayList[position].isGraded) {
            R.drawable.baseline_check_box_24 // Assuming this is your checkmark drawable
        } else {
            R.drawable.baseline_check_box_outline_blank_24 // Assuming this is your cross drawable
        }
        viewHolder.tvGraded.setImageResource(imageDrawableResId)
    }

    override fun getItemCount(): Int {
        return studentArrayList.size
    }

    fun updateData(filteredItems: ArrayList<StudentDTO>){
        studentArrayList = filteredItems
        notifyDataSetChanged() // Notify RecyclerView that the dataset has changed
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
        val tvStudentNumber: TextView = itemView.findViewById(R.id.tvStudentNumber)
        val tvGraded: ImageView = itemView.findViewById(R.id.tvGraded)
    }
}
