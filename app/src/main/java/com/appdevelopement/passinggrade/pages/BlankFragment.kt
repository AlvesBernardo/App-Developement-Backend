package com.appdevelopement.passinggrade.pages



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdevelopement.passinggrade.R
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable



class BlankFragment : Fragment() {



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

            BlankFragment().apply {

                arguments = Bundle().apply {

                    // Any arguments you want

                }

            }

    }

}



// Compose functions



@Composable

fun SheetsManagementFragment() {

    val tabs = listOf("Create/Edit sheet", "Read Gradesheet", "Import file", "Assign course")

    val (selectedTab, onTabSelected) = remember { mutableStateOf(tabs[0]) }



    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        Box(modifier = Modifier.verticalScroll(rememberScrollState())) {

            HorizontalScrollableTabRow(

                selectedTabIndex = tabs.indexOf(selectedTab),

                backgroundColor = Color.Transparent,

                contentColor = MaterialTheme.colors.onSurface,

                edgePadding = 16.dp,

                divider = {},

                indicator = { tabPositions ->

                    Box(

                        modifier = Modifier

                            .padding(end = TabRowDefaults.IndicatorWidth / 2f, bottom = 2.dp)

                            .background(MaterialTheme.colors.primary, TabRowDefaults.IndicatorShape)

                            .size(TabRowDefaults.IndicatorWidth, TabRowDefaults.IndicatorHeight)

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



@Preview

@Composable

fun PreviewSheetsManagementFragment() {

    SheetsManagementFragment()

}