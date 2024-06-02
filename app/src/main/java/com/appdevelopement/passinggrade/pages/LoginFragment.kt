import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.R

class LoginFragment : Fragment() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?, 
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.login)

        loginButton.setOnClickListener { loginUser() }

        return view
    }

    private fun loginUser() {
        val usernameInput = username.text.toString()
        val passwordInput = password.text.toString()

        if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(activity, "Username or password field can't be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        // Implement dynamic login logic here
        // This could involve making a network request to validate the user credentials
    }
}