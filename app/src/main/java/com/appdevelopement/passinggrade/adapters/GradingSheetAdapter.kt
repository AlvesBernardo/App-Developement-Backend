package com.appdevelopement.passinggrade.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.models.Compentence

class GradingSheetAdapter(private var competenceList: MutableList<Compentence>) :
    RecyclerView.Adapter<GradingSheetAdapter.ViewHolder>() {

  // Inflate the item layout and create the ViewHolder
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view =
        LayoutInflater.from(parent.context).inflate(R.layout.grading_criteria_item, parent, false)
    return ViewHolder(view)
  }

  // Bind the data to the view elements
  override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    val currentItem = competenceList[position]
    viewHolder.tvGradingCriteriaItem.text = currentItem.dtName

    val imageDrawableResId = R.drawable.baseline_close_24
    viewHolder.ivRemoveCriteria.setImageResource(imageDrawableResId)

    viewHolder.ivRemoveCriteria.setOnClickListener { removeCriteria(position) }
  }

  // Return the size of the data list
  override fun getItemCount(): Int {
    return competenceList.size
  }

  // ViewHolder class to hold the view elements
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvGradingCriteriaItem: TextView = itemView.findViewById(R.id.tvGradingCriteriaItem)
    val ivRemoveCriteria: ImageView = itemView.findViewById(R.id.ivRemoveCriteria)
  }

  private fun removeCriteria(position: Int) {
    competenceList.removeAt(position)
    notifyItemRemoved(position)
    notifyItemRangeChanged(position, competenceList.size)
  }

  fun addCriteria(competence: Compentence) {
    competenceList.add(competence)
    notifyItemInserted(competenceList.size - 1)
  }

  fun removeAllCriterias() {
    competenceList.clear()
    notifyDataSetChanged()
  }
}
