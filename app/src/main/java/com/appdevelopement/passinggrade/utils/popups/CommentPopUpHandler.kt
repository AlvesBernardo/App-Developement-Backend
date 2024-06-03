package com.appdevelopement.passinggrade.utils.popups

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class CommentPopUpHandler(private val context: Context) {
    private val comments = mutableMapOf<String, String>()
    fun showCommentPopUp(criterion: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Comment for $criterion")

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            // Save the comment somewhere or handle it as needed
            val comment = input.text.toString()
            comments[comment] = comment
            Toast.makeText(context, "Comment saved: $comment", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun getComment(criterion: String): String{
        return  comments[criterion] ?: ""
    }
}