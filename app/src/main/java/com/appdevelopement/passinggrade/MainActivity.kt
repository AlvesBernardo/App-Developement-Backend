package com.appdevelopement.passinggrad

import android.app.Application
import android.health.connect.datatypes.SleepSessionRecord
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.sql.DriverManager

class CourseDropdownApp : Application() {

    private val comboBox by lazy { ComboBox<String>() }

    override fun start(primaryStage: SleepSessionRecord.Stage) {
        val root = VBox()
        root.children.add(comboBox)

        primaryStage.scene = Scene(root, 300.0, 200.0)
        primaryStage.title = "Course Dropdown"
        primaryStage.show()

        loadCourses()
    }

    private fun loadCourses() {
        val connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_development", "root", "")
        connection.use { conn ->
            val statement = conn.createStatement()
            statement.use { stmt ->
                val resultSet = stmt.executeQuery("SELECT dtDescription FROM courses")
                resultSet.use { rs ->
                    while (rs.next()) {
                        comboBox.items.add(rs.getString("dtDescription"))
                    }
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(CourseDropdownApp::class.java, *args)
}