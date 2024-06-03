package com.appdevelopement.passinggrade.utils.popups.calculators

import android.widget.LinearLayout
import com.appdevelopement.passinggrade.pages.grading.CriterionRecord

class CritertionCalculator {
    fun calculateTotalGrade(gradingAreaLayout: LinearLayout): Int {
        var total = 0
        for (i in 0 until gradingAreaLayout.childCount) {
            val layout = gradingAreaLayout.getChildAt(i)
            val record = layout.tag as CriterionRecord
            total += record.progress
        }
        return total / gradingAreaLayout.childCount
    }
}