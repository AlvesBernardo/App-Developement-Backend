package pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Course(val name: String, val description: String)

@Composable
fun UserDashboard() {
    val courses = listOf(
        Course("Kotlin Fundamentals", "Learn the basics of Kotlin programming."),
        Course("Android Development with Jetpack Compose", "Build modern Android UI with Compose."),
        // ... add more courses
    )

    var showDialog by remember { mutableStateOf(false) }
    var selectedCourse by remember { mutableStateOf<Course?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "My Courses", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(16.dp))

        courses.forEach { course ->
            Button(
                onClick = {
                    selectedCourse = course
                    showDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = course.name)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = selectedCourse?.name ?: "") },
            text = { Text(text = selectedCourse?.description ?: "") },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
