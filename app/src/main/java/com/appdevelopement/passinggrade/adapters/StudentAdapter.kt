package com.appdevelopement.passinggrade.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.dto.StudentDto

class StudentAdapter(private var studentArrayList: ArrayList<StudentDto>) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_student_main, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvStudentName.text = studentArrayList[position].studentName
        viewHolder.tvStudentNumber.text = studentArrayList[position].studentNumber.toString()
        viewHolder.tvGraded.text = studentArrayList[position].isGraded.toString()

//        Log.d(TAG, "onBindViewHolder")
    }

    override fun getItemCount(): Int {
        return studentArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
        val tvStudentNumber: TextView = itemView.findViewById(R.id.tvStudentNumber)
        val tvGraded: TextView = itemView.findViewById(R.id.tvGraded)
    }
}
