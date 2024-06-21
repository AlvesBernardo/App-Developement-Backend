package com.appdevelopement.passinggrade.utils.popups

import android.widget.LinearLayout
import androidx.core.view.children
import com.appdevelopement.passinggrade.models.Student
import com.appdevelopement.passinggrade.pages.grading.CriterionRecord

class StudentRecordCreator {
  fun getStudentRecord(
      student: Student,
      totalGrade: Double,
      gradingAreaLayout: LinearLayout
  ): Array<String> {
    val criterionRecord: List<CriterionRecord> =
        gradingAreaLayout.children
            .filter { it is LinearLayout && it.tag is CriterionRecord }
            .map { it.tag as CriterionRecord }
            .toList()

    val generalComment = criterionRecord.joinToString("\n") { "${it.name}: ${it.comment}" }

    return arrayOf(student.studentNumber.toString(), totalGrade.toString())
  }
}
