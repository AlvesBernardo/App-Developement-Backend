package com.appdevelopement.passinggrade.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import com.appdevelopement.passinggrade.R


class SheetManagementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).setContent {
                SheetsManagementFragment()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SheetManagementFragment()
    }
}

// Compose functions

@Composable
fun SheetsManagementFragment() {
    val tabs = listOf("Create/Edit sheet", "Read Gradesheet", "Import file", "Assign course")
    val (selectedTab, onTabSelected) = remember { mutableStateOf(tabs[0]) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TabRow(
                selectedTabIndex = tabs.indexOf(selectedTab),
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
                            .padding(vertical = 8.dp) // Adjust vertical padding as needed
                            .size(width = 4.dp, height = TabRowDefaults.IndicatorHeight)
                    )
                }
            ) {
                tabs.forEach { tab ->
                    Tab(
                        text = { Text(tab) },
                        selected = selectedTab == tab,
                        onClick = { onTabSelected(tab) }
                    )
                }
            }

            when (selectedTab) {
                "Create/Edit sheet" -> CreateEditSheet()
                "Read Gradesheet" -> ReadGradesheet()
                "Import file" -> ImportFile()
                "Assign course" -> AssignCourse()
            }
        }
    }
}

@Composable
fun CreateEditSheet() {
    Box(modifier = Modifier.padding(16.dp)) {
        Text("Create/Edit sheet functionality")
    }
}

@Composable
fun ReadGradesheet() {
    Box(modifier = Modifier.padding(16.dp)) {
        Text("Read Gradesheet functionality")
    }
}

@Composable
fun ImportFile() {
    Box(modifier = Modifier.padding(16.dp)) {
        Text("Import file functionality")
    }
}

@Composable
fun AssignCourse() {
    Box(modifier = Modifier.padding(16.dp)) {
        Text("Assign course functionality")
    }
}

@Composable
fun PreviewSheetsManagementFragment() {
    SheetsManagementFragment()
}
