package com.appdevelopement.passinggrade.pages

import com.appdevelopement.passinggrade.models.Course //it cant find the Course for some reason. I assume its a connection issue
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserDashboardFragment : Fragment() {

    private val courses = listOf(

        Course("Kotlin Fundamentals", "Learn the basics of Kotlin programming."),
        Course("Android Development with Jetpack Compose", "Build modern Android UI with Compose."),
        Course("Information Management", "Understand the principles and techniques for managing information."),
        Course("Web Development", "Learn how to create dynamic websites using HTML, CSS, and JavaScript."),
        Course("Database Engineering", "Master the skills needed to design, implement, and manage databases."),
        Course("OOP1", "Introduction to Object-Oriented Programming principles."),
        Course("OOP2", "Advanced concepts in Object-Oriented Programming."),
        Course("Software Quality", "Learn about software testing, verification, and quality assurance."),
        Course("App Development", "Complete guide to developing applications for different platforms.")

    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_dashboard, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_courses)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CourseAdapter(courses)

        return view
    }

    private inner class CourseAdapter(private val courseList: List<Course>) :
        RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_course, parent, false)
            return CourseViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
            val course = courseList[position]
            holder.courseButton.text = course.name
            holder.courseButton.setOnClickListener {
                showCourseDialog(course)
            }
        }

        override fun getItemCount() = courseList.size

        inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val courseButton: Button = itemView.findViewById(R.id.btn_course)
        }
    }

    private fun showCourseDialog(course: Course) {
        AlertDialog.Builder(requireContext())
            .setTitle(course.name)
            .setMessage(course.description)
            .setPositiveButton("Close", null)
            .show()
    }
}
