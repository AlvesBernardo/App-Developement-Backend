package com.appdevelopement.passinggrade.pages

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.dao.TeacherDao
import com.appdevelopement.passinggrade.database.AppDatabase
import com.appdevelopement.passinggrade.middelware.TeacherManger
import com.appdevelopement.passinggrade.models.Teacher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var db: AppDatabase
    private lateinit var teacherDao: TeacherDao

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
            Toast.makeText(activity, "Username or password field can't be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val teacher = getTeacher(emailInput, passwordInput)
            withContext (Dispatchers.Main) {

                // Add a null check here before setting sharedPreferences
                if (teacher != null) {
                    val currentTime = System.currentTimeMillis()
                    val sharedPreferences = activity?.getSharedPreferences("Authentication", Context.MODE_PRIVATE)
                    sharedPreferences?.edit()?.putBoolean("loggedIn", true)
                        ?.putLong("loginTimestamp", currentTime)
                        ?.putInt("idTeacher", teacher.idTeacher)
                        ?.apply()
                    val fragmentManager = requireActivity().supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, UserDashboardFragment())
                    transaction.commit()
                } else {
                    Toast.makeText(activity, "Invalid username or password.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun View.adjustForKeyboardLogin(scrollView: ScrollView) {
        viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is opened
                scrollView.post {
                    scrollView.smoothScrollTo(0, bottom)
                }
            } else {
                // Keyboard is closed
                scrollView.post {
                    scrollView.smoothScrollTo(0, 0)
                }
            }
        }
    }

    suspend fun getTeacher(email: String, password: String): Teacher? {
        return TeacherManger.getTeacherByEmailAndPassword(requireContext(), email, password)
    }
}
