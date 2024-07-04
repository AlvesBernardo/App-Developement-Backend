package com.appdevelopement.passinggrade.utils.popups

import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.appdevelopement.passinggrade.R

class CommentPopUpHandler(private val context: Context) {
  private val commentsMap = mutableMapOf<String, String>()

  fun showCommentPopUp(
      criterion: String,
      existingComment: String = "",
      onCommentConfirmed: (String) -> Unit
  ) {
    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.comment_popup, null)

    val commentEditText = view.findViewById<EditText>(R.id.comment_edit_text)
    commentEditText.setText(existingComment)

    val builder = AlertDialog.Builder(context, R.style.MyDialogTheme)
    builder.setView(view)

    builder.setPositiveButton("OK") { dialog, _ ->
      val comment = commentEditText.text.toString()
      commentsMap[criterion] = comment
      onCommentConfirmed(comment)
      dialog.dismiss()
    }
    builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
    builder.show()
  }

  fun getComment(criterion: String): String {
    return commentsMap[criterion] ?: ""
  }
}
