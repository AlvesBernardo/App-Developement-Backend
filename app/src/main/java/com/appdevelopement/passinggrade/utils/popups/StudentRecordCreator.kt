package com.appdevelopement.passinggrade.utils.popups

import android.widget.LinearLayout
import com.appdevelopement.passinggrade.models.Student
import com.appdevelopement.passinggrade.pages.grading.CriterionRecord

class StudentRecordCreator {
    fun getStudentRecord(student: Student, totalGrade: Double, gradingAreaLayout: LinearLayout): Array<String> {
        val lastIndex = gradingAreaLayout.childCount - 1
        val finalRecord = gradingAreaLayout.getChildAt(lastIndex).tag as CriterionRecord
        return arrayOf(student.idStudent.toString(), totalGrade.toString(), finalRecord.comment)    }
}