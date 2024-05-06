import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appdevelopement.passinggrade.R
import org.ktorm.database.Database

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database: Database = DatabaseProvider.database

        // Verify connection
        val isConnectionSuccessful = database.useConnection { it.isValid(0) }
        println("Connection successful: $isConnectionSuccessful")

        // Create a table if it doesn't exist
        val tableName = "users"

        try {
            database.useConnection { connection ->
                val statement = connection.createStatement()
                statement.execute("""
                    CREATE TABLE IF NOT EXISTS $tableName (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(250) NOT NULL,
                      email VARCHAR(250) NOT NULL
                    )
                """.trimIndent())
            }

            println("Table '$tableName' checked/created successfully.")
        } catch (exception: Exception) {
            println("An error occurred: ${exception.localizedMessage}")
        }
    }
}
