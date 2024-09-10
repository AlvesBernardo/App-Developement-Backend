package com.appdevelopement.passinggrade.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.models.Competence

class GradingSheetAdapter(private var competenceList: MutableList<Competence>) :
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
    viewHolder.tvGradingCriteriaName.text = currentItem.dtName
    viewHolder.tvGradingCriteriaWeight.text = currentItem.dtCompetenceWeight.toString()
  }

  // Return the size of the data list
  override fun getItemCount(): Int {
    return competenceList.size
  }

  // ViewHolder class to hold the view elements
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvGradingCriteriaName: TextView = itemView.findViewById(R.id.tvGradingCriteriaName)
    val tvGradingCriteriaWeight: TextView = itemView.findViewById(R.id.tvGradingCriteriaWeight)
  }

  private fun removeCriteria(position: Int) {
    competenceList.removeAt(position)
    notifyItemRemoved(position)
    notifyItemRangeChanged(position, competenceList.size)
  }

  fun addCriteria(competence: Competence) {
    competenceList.add(competence)
    notifyItemInserted(competenceList.size - 1)
  }

  fun addAllCriteria(newCompetenceList: List<Competence>) {
    competenceList.clear()
    competenceList.addAll(newCompetenceList)
    notifyDataSetChanged()
  }

  fun removeAllCriterias() {
    competenceList.clear()
    notifyDataSetChanged()
  }
}
