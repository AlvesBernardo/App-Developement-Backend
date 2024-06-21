package com.appdevelopement.passinggrade.utils.popups.calculators

import android.widget.LinearLayout
import com.appdevelopement.passinggrade.models.Compentence
import com.appdevelopement.passinggrade.pages.grading.CriterionRecord

class CritertionCalculator {
  fun calculateTotalGrade(gradingAreaLayout: LinearLayout, competences: List<Compentence>): Double {
    var weightedSum = 0.0
    var weightSum = 0

    for (i in 0 until gradingAreaLayout.childCount) {
      val layout = gradingAreaLayout.getChildAt(i)
      val record = layout.tag as CriterionRecord
      val competence = competences.find { it.dtName == record.name }

      if (competence != null) {
        weightedSum +=
            (record.progress.toDouble() *
                competence.dtCompetenceWeight) // Use progress as a percentage
        weightSum += competence.dtCompetenceWeight
      }
    }

    return if (weightSum > 0) {
      weightedSum / weightSum // Calculate the weighted average
    } else {
      0.0 // Return 0 if there are no competences to grade
    }
  }
}
