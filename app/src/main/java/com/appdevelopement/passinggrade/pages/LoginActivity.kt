import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.appdevelopement.passinggrade.R

class LoginActivity : AppCompatActivity() {

	private lateinit var username: EditText
	private lateinit var password: EditText
	private lateinit var loginButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		username = findViewById(R.id.username)
		password = findViewById(R.id.password)
		loginButton = findViewById(R.id.login)

		loginButton.setOnClickListener { loginUser() }
	}

	private fun loginUser() {
		val usernameInput = username.text.toString()
		val passwordInput = password.text.toString()

		if(usernameInput.isEmpty() || passwordInput.isEmpty()){
			Toast.makeText(this, "Username or password field can't be empty.", Toast.LENGTH_SHORT).show()
			return
		}

		// login
	}
}
