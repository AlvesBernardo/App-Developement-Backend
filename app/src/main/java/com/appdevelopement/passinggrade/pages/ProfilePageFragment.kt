package com.appdevelopement.passinggrade.pages

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.appdevelopement.passinggrade.R
import com.appdevelopement.passinggrade.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class ProfilePageFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var db: AppDatabase
    private lateinit var emailText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idTeacher =
            activity?.getSharedPreferences("Authentication", Context.MODE_PRIVATE)
                ?.getInt("idTeacher", -1) ?: -1
        emailText = view.findViewById(R.id.tvProfileEmail)
        lifecycleScope.launch {
            getEmail(idTeacher)
        }

        // DB connection
        db = AppDatabase.getDatabase(requireContext())

        profileImageView = view.findViewById(R.id.ivProfilePicture)

        val submitPasswordBttn = view.findViewById<Button>(R.id.bttnSubmitPassword)
        val newPasswordEt = view.findViewById<EditText>(R.id.etNewPassword)

        //Need to pass email to profile page then store it in profileEmail
        val profileEmail = view.findViewById<TextView>(R.id.tvProfileEmail)


        submitPasswordBttn.setOnClickListener {
            val password = newPasswordEt.text.toString()
            if (password.isNotEmpty()) {

                lifecycleScope.launch {

                    updateProfilePassword(password, idTeacher)
                }

            }
        }
    }

    private suspend fun updateProfilePassword(password: String, id: Int) {
        withContext(Dispatchers.IO) {
            val affectedRows = db.teacherDao().updateTeacherViaId(password, id)
            withContext(Dispatchers.Main) {
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private suspend fun getEmail(id: Int) {
        var teacherEmail = ""
        withContext(Dispatchers.IO) {
            val teacherData = db.teacherDao().getTeacherById(id)
            teacherEmail = teacherData
        }
        withContext(Dispatchers.Main) {
            emailText?.text = teacherEmail
        }
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                try {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        imageUri
                    )
                    profileImageView.setImageBitmap(bitmap)

                    //Then send store the content of profileImageView somewhere? or get a string representation, we have imageUri
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
}
