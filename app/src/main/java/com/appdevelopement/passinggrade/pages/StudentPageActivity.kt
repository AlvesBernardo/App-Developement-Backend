import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.adapters.StudentAdapter
import com.appdevelopement.passinggrade.dto.StudentDTO
import com.appdevelopement.passinggrade.utils.popups.ReadFromExcelFile
import java.io.IOException

class StudentPageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var searchView: SearchView
    private lateinit var studentList: ArrayList<StudentDTO>

    companion object {
        private const val PICK_FILE_REQUEST_CODE = 1
        private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_page)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        studentList = arrayListOf(
            StudentDTO("John Doe", 126345, true),
            StudentDTO("Jane Smith", 678890, false),
            StudentDTO("Alice", 547321, true)
        )

        studentAdapter = StudentAdapter(studentList, supportFragmentManager)
        recyclerView.adapter = studentAdapter

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterStudentByNumber(newText)
                return true
            }
        })

        // Spinner
        val filterItems = arrayOf("All", "Graded", "UnGraded")
        val filterAdapter = ArrayAdapter(
            this, R.layout.spinner_item, filterItems
        )

        val spinner: Spinner = findViewById(R.id.spnrFilterBy)
        spinner.adapter = filterAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterStudentsByGradeStatus(filterItems[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Import Excel Button
        val importSheetButton: Button = findViewById(R.id.btnImportSheet)  // Ensure this ID matches your layout file
        importSheetButton.setOnClickListener {
            Log.d("StudentPageActivity", "Import button clicked")
            requestReadPermissionAndPickFile()
        }
    }

    private fun requestReadPermissionAndPickFile() {
        Log.d("StudentPageActivity", "Checking permissions")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            Log.d("StudentPageActivity", "Requesting read external storage permission")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        } else {
            Log.d("StudentPageActivity", "Permission already granted, picking file")
            pickFile()
        }
    }

    private fun pickFile() {
        Log.d("StudentPageActivity", "Launching file picker")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("StudentPageActivity", "onRequestPermissionsResult called")
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("StudentPageActivity", "Permission granted, picking file")
                    pickFile()
                } else {
                    Log.d("StudentPageActivity", "Permission denied")
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("StudentPageActivity", "onActivityResult called")
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d("StudentPageActivity", "File picker result OK")
            data?.data?.also { uri ->
                Log.d("StudentPageActivity", "File selected: $uri")
                importExcelData(uri)
            }
        } else {
            Log.d("StudentPageActivity", "File picker result not OK or request code mismatch")
        }
    }

    private fun filterStudentByNumber(query: String?) {
        val filteredList: ArrayList<StudentDTO> = if (query.isNullOrEmpty()) {
            studentList
        } else {
            ArrayList(studentList.filter {
                it.studentNumber.toString().contains(query)
            })
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
        }
        studentAdapter.updateData(filteredList)
    }

    private fun filterStudentsByGradeStatus(filter: String) {
        val filteredList: ArrayList<StudentDTO> = when (filter) {
            "Graded" -> ArrayList(studentList.filter { it.isGraded })
            "UnGraded" -> ArrayList(studentList.filter { !it.isGraded })
            else -> ArrayList(studentList)
        }
        studentAdapter.updateData(filteredList)
    }

    private fun importExcelData(uri: Uri) {
        val readFromExcelFile = ReadFromExcelFile(this)
        try {
            val data = readFromExcelFile.readFromExcel(uri)
            Log.d("StudentPageActivity", "Data read from Excel: $data")
            Toast.makeText(this, "Data Imported Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("StudentPageActivity", "Error reading Excel file", e)
            Toast.makeText(this, "Error Importing Data", Toast.LENGTH_SHORT).show()
        }
    }
}
