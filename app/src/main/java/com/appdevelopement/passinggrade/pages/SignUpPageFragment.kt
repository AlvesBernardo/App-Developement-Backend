package com.appdevelopement.passinggrade.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.R

// TODO: Rename parameter arguments, choose names that match

class SignUpPageFragment : Fragment() {
  // TODO: Rename and change types of parameters
  //    private lateinit var companyLogoView: ImageView

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_sign_up_page, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    //        companyLogoView = view.findViewById(R.id.ivCompanyLogo)
    val emailEt = view.findViewById<EditText>(R.id.etEmail)
    val passwordEt = view.findViewById<EditText>(R.id.etPassword)
    val signUpBttn = view.findViewById<Button>(R.id.bttnSignUp)

    signUpBttn.setOnClickListener {
      val email = emailEt.text.toString()
      val password = passwordEt.text.toString()
      if (email.isNotEmpty() && password.isNotEmpty()) {
        if (isValidEmail(email) && isValidPassword(password)) {
          // Carry out the process of adding user to db

        } else {
          if (!isValidEmail(email)) {
            emailEt.error = "Invalid email address"
          } else if (!isValidPassword(password)) {
            passwordEt.error =
                "Password must be at least 8 characters long, and include a number and a special character"
          }
        }
      } else {
        Toast.makeText(requireContext(), "email & password are required", Toast.LENGTH_SHORT).show()
      }
    }
  }

  private fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.isNotEmpty() && email.matches(emailPattern.toRegex())
  }

  private fun isValidPassword(password: String): Boolean {
    val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$")
    return password.isNotEmpty() && password.matches(passwordPattern)
  }
}
