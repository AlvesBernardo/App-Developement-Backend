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
    viewHolder.tvGradingCriteriaName.text = currentItem.dtName
    viewHolder.tvGradingCriteriaWeight.text = currentItem.dtCompetenceWeight.toString()
    viewHolder.tvMustPass.text = if (currentItem.dtMustPass) "Yes" else "No"
    
    viewHolder.ivCloseBttn.setOnClickListener{
        removeCompetence(viewHolder.adapterPosition)
    }
    
  }

  // Return the size of the data list
  override fun getItemCount(): Int {
    return competenceList.size
  }

  // ViewHolder class to hold the view elements
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvGradingCriteriaName: TextView = itemView.findViewById(R.id.tvGradingCriteriaName)
    val tvGradingCriteriaWeight: TextView = itemView.findViewById(R.id.tvGradingCriteriaWeight)
    val tvMustPass: TextView = itemView.findViewById(R.id.tvMustPass)
    val ivCloseBttn: ImageView = itemView.findViewById(R.id.ivCloseBttn)
  }

  private fun removeCompetence(position: Int) {
    if (position >= 0 && position < competenceList.size) {
      competenceList.removeAt(position)  // Remove the item at the specified position
      notifyItemRemoved(position)  // Notify the adapter about item removal
      notifyItemRangeChanged(position, competenceList.size)  // Update the remaining items
    }
  }

  fun addCompetence(competence: Compentence) {
    competenceList.add(competence)
    notifyItemInserted(competenceList.size - 1)
  }

  fun addAllCompetences(newCompetenceList: List<Compentence>) {
    competenceList.clear()
    competenceList.addAll(newCompetenceList)
    notifyDataSetChanged()
  }

  fun removeAllCompetences() {
    competenceList.clear()
    notifyDataSetChanged()
  }
}
