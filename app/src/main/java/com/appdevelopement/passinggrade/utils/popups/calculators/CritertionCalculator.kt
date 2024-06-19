package com.appdevelopement.passinggrade.utils.popups.calculators

import android.graphics.Color
import android.widget.LinearLayout
import com.appdevelopement.passinggrade.models.Compentence
import com.appdevelopement.passinggrade.pages.grading.CriterionRecord
import kotlin.math.roundToInt

class CritertionCalculator {
    fun calculateTotalGrade(gradingAreaLayout: LinearLayout, competences: List<Compentence>): Int {
        var weightedSum = 0.0
        var weightSum = 0

        for (i in 0 until gradingAreaLayout.childCount) {
            val layout = gradingAreaLayout.getChildAt(i)
            val record = layout.tag as CriterionRecord
            val competence = competences.find { it.dtName == record.name }

            if (competence != null) {
                weightedSum += (record.progress.toDouble() * competence.dtCompetenceWeight ) // Use progress as a percentage
                weightSum += competence.dtCompetenceWeight
            }
        }
        val average = weightedSum / weightSum // The weight sum is the divider here
        return (average * 10).roundToInt() // This is to convert the grade to a scale of 0 to 10
    }

}