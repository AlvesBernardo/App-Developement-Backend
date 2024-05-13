import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.appdevelopement.passinggrade.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)

        loginButton.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val usernameInput = username.text.toString()
        val passwordInput = password.text.toString()

        if(usernameInput.isEmpty() || passwordInput.isEmpty()){
            Toast.makeText(this, "Username or password field can't be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        // Perform registration logic here

        Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()

        // Navigate to another activity or show a success message
    }
}
