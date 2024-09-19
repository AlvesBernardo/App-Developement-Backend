package com.appdevelopement.passinggrade.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.models.Course

class CoursesAdapter(private val courses: List<Course>) :
    RecyclerView.Adapter<CoursesAdapter.ViewHolder>() {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val courseName: TextView = view.findViewById(R.id.tv_course_description)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val course = courses[position]
    holder.courseName.text = course.dtTitle
  }

  override fun getItemCount() = courses.size
}
