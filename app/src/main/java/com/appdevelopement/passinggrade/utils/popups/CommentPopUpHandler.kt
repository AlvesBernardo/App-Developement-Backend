package com.appdevelopement.passinggrade.utils.popups

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class CommentPopUpHandler(private val context: Context) {
    private val commentsMap = mutableMapOf<String, String>()

    fun showCommentPopUp(criterion: String, existingComment: String = "") {
        val builder = AlertDialog.Builder(context)
        val input = EditText(context)
        input.setText(existingComment)
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, _ ->
            commentsMap[criterion] = input.text.toString()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    fun getComment(criterion: String): String {
        return commentsMap[criterion] ?: ""
    }
}