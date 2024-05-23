package com.appdevelopement.passinggrade.adapters

import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.dto.GradingSheetDto
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.appdevelopement.passinggrade.R

class GradingSheetAdapter(private var gradingSheetArrayList: ArrayList<GradingSheetDto>) :
    RecyclerView.Adapter<GradingSheetAdapter.ViewHolder>() {

    // Inflate the item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.grading_criteria_item, parent, false)
        return ViewHolder(view)
    }

    // Bind the data to the view elements
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem = gradingSheetArrayList[position]
        viewHolder.tvGradingCriteriaItem.text = currentItem.dtDescription

        val imageDrawableResId =  R.drawable.baseline_close_24
        viewHolder.ivRemoveCriteria.setImageResource(imageDrawableResId)

        viewHolder.ivRemoveCriteria.setOnClickListener{
            removeCriteria(position)
        }
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return gradingSheetArrayList.size
    }

    // ViewHolder class to hold the view elements
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGradingCriteriaItem: TextView = itemView.findViewById(R.id.tvGradingCriteriaItem)
        val ivRemoveCriteria: ImageView = itemView.findViewById(R.id.ivRemoveCriteria)
    }

    private fun removeCriteria(position: Int){
        gradingSheetArrayList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, gradingSheetArrayList.size)
    }

}