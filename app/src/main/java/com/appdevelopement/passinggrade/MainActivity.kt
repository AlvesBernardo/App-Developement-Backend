package com.appdevelopement.passinggrade

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appdevelopement.passinggrade.R
import org.ktorm.database.Database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            Config.load(this@MainActivity)

            val database = DatabaseProvider.database

            if (database != null) {
                try {
                    val isConnectionSuccessful = database.useConnection { it.isValid(0) }

                    if (isConnectionSuccessful) {
                        println("Connection successful: $isConnectionSuccessful")

                        // Create a table if it doesn't exist
                        val tableName = "users"

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
                    } else {
                        println("Connection unsuccessful.")
                    }
                } catch (exception: Exception) {
                    println("An error occurred: ${exception.localizedMessage}")
                }
            } else {
                println("Database connection was null.")
            }
        }
    }
}