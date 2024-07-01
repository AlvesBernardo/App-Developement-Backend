package com.appdevelopement.passinggrade.pages

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.models.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDashboardFragment : Fragment() {

    private var courses = mutableListOf<Course>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_dashboard, container, false)
        recyclerView = view.findViewById(R.id.rv_courses)

        recyclerView.layoutManager = LinearLayoutManager(context)
        courseAdapter = CourseAdapter(courses)
        recyclerView.adapter = courseAdapter

        activity
            ?.getSharedPreferences("Authentication", Context.MODE_PRIVATE)
            ?.getInt("idTeacher", -1)
            ?.let { teacherId ->
                lifecycleScope.launch {
                    fetchCourses(teacherId)
                }
            }

        return view
    }

    private suspend fun fetchCourses(teacherId: Int) {
        val fetchedCourses = getCoursesForTeacher(requireContext(), teacherId)
        Log.d("UserDashboardFragment", "Fetched ${fetchedCourses.size} courses for teacher ID: $teacherId")
        courses.clear()
        courses.addAll(fetchedCourses)
        courseAdapter.notifyDataSetChanged()
    }

    private inner class CourseAdapter(private val courseList: List<Course>) :
        RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
            return CourseViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
            val course = courseList[position]
            holder.courseButton.text = course.dtTitle
            Log.d("UserDashboardFragment", "Clicked on course with id ${course.idCourse}")
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
            .setTitle(course.dtTitle)
            .setMessage(course.idCourse.toString())
            .setPositiveButton("Close", null)
            .show()
    }

    private suspend fun getCoursesForTeacher(context: Context, teacherId: Int): List<Course> {
        Log.d("TeacherCourses", "Teacher ID: $teacherId")
        val dao = AppDatabase.getDatabase(context).courseDao()
        return withContext(Dispatchers.IO) {
            dao.getTeacherCourses(teacherId)
        }
    }
}
