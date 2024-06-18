package com.appdevelopement.passinggrade.pages

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.pages.UserDashboardFragment

class LoginFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.login)

        loginButton.setOnClickListener { loginUser() }

        return view
    }

    private fun loginUser() {
        val emailInput = email.text.toString()
        val passwordInput = password.text.toString()

        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(activity, "Email or password field can't be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        val isLoginSuccessful = true

        if (isLoginSuccessful) {
            val currentTime = System.currentTimeMillis()
            val sharedPreferences = activity?.getSharedPreferences("Authentication", Context.MODE_PRIVATE)
            sharedPreferences?.edit()?.putBoolean("loggedIn",true)
                ?.putLong("loginTimestamp", currentTime)
                ?.apply()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, UserDashboardFragment())
            transaction.commit()
        } else {
            Toast.makeText(activity, "Invalid email or password.", Toast.LENGTH_SHORT).show()
        }
    }
}
