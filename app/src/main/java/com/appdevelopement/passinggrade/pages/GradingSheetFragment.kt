package com.appdevelopement.passinggrade.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.GradingSheetAdapter
import com.appdevelopement.passinggrade.dto.CourseDto
import com.appdevelopement.passinggrade.dto.GradingSheetDto

class GradingSheetFragment : Fragment() {

    //Fields
    private lateinit var recyclerView: RecyclerView
    private lateinit var gradingSheetAdapter: GradingSheetAdapter

    private val courseList = arrayListOf(
        CourseDto(1, "OOP 1"),
        CourseDto(2, "OOP 2"),
        CourseDto(3, "Software Quality")
    )

    private val gradingSheetItemList = arrayListOf(
        GradingSheetDto(1, "Grading Sheet criteria")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_grading_sheet, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        gradingSheetAdapter = GradingSheetAdapter(gradingSheetItemList)
        recyclerView.adapter = gradingSheetAdapter

        // Initialize EditText and Button
        val gradingSheetItem = view.findViewById<EditText>(R.id.etGradingCriteria)
        val addCriteriaBttn = view.findViewById<Button>(R.id.btnAddCriteria)

        // Button to add grading criteria
        addCriteriaBttn.setOnClickListener {
            val text = gradingSheetItem.text.toString()
            if (text.isNotEmpty()) {
                val newGradingSheetItemsId = gradingSheetItemList.size + 1
                gradingSheetItemList.add(GradingSheetDto(newGradingSheetItemsId, text))
                gradingSheetAdapter.notifyItemInserted(gradingSheetItemList.size - 1)
                gradingSheetItem.text.clear()
            }
        }

        // Initialize Spinner
        val descriptions = courseList.map { it.dtDescription }

        val filterAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_item, descriptions
        )

        val spinner: Spinner = view.findViewById(R.id.spnrFilterByCourse)
        spinner.adapter = filterAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle item selection
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        return view
    }
}
