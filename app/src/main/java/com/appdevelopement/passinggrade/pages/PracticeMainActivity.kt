package com.appdevelopement.passinggrade.pages


import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import com.appdevelopement.passinggrade.R

class PracticeMainActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var etCountry: EditText
    private lateinit var btnApply: Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.practice_activity_main)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etBirthDate = findViewById(R.id.etBirthDate)
        etCountry = findViewById(R.id.etCountry)
        btnApply = findViewById(R.id.btnApply)

        // Set click listener for the Button
        btnApply.setOnClickListener {
            // Retrieve the text from EditText fields
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val birthDate = etBirthDate.text.toString()
            val country = etCountry.text.toString()

            // Do something with the retrieved values
            // For example, display them in a toast
            val message = "First Name: $firstName\nLast Name: $lastName\nBirth Date: $birthDate\nCountry: $country"
            Log.d("MainActivity", message)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        }
    }



}


//class MainActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: StudentAdapter
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
////        adapter = StudentAdapter(createSampleData()) { student ->
////            // Handle click action here, navigate to another page
////        }
//        recyclerView.adapter = adapter
//    }
//
//    private fun createSampleData(): List<Student> {
//        // Sample data, you can replace it with your own data source
//        return listOf(
//            Student("John Doe", "123456", true),
//            Student("Jane Smith", "654321", false),
//            Student("Alice Johnson", "987654", true),
//            Student("Bob Brown", "456789", false)
//        )
//    }
//}
//
//data class Student(val name: String, val number: String, val isGraded: Boolean)
//
//class StudentAdapter(
//    private val students: List<Student>,
//    private val onItemClick: (Student) -> Unit
//) : RecyclerView.Adapter<StudentViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_student, parent, false)
//        return StudentViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
//        val student = students[position]
//        holder.bind(student)
//        holder.itemView.setOnClickListener { onItemClick(student) }
//    }
//
//    override fun getItemCount(): Int {
//        return students.size
//    }
//}
//
//class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    fun bind(student: Student) {
//        itemView.findViewById<TextView>(R.id.txtName).text = student.name
//        itemView.findViewById<TextView>(R.id.txtNumber).text = student.number
//        itemView.findViewById<TextView>(R.id.txtIsGraded).text = student.isGraded.toString()
//    }
//}
