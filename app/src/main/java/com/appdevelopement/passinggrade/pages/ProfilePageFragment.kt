package com.appdevelopement.passinggrade.pages

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.appdevelopement.passinggrade.R
import com.google.android.material.textfield.TextInputLayout
import java.io.IOException


class ProfilePageFragment : Fragment() {
    private lateinit var profileImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ImageView
        profileImageView = view.findViewById(R.id.ivProfilePicture)

        // Initialize EditText & Buttons
        val changeProfilePictureBttn = view.findViewById<Button>(R.id.bttnChangePicture)
        val submitPasswordBttn = view.findViewById<Button>(R.id.bttnSubmitPassword)
        val newPasswordEt = view.findViewById<EditText>(R.id.etNewPassword)
//        val textInputLayout = view.findViewById<TextInputLayout>(R.id.textInputLayout)
//        val toggleButton = view.findViewById<ToggleButton>(R.id.toggleButton)

        changeProfilePictureBttn.setOnClickListener {
            openGallery()
        }

        submitPasswordBttn.setOnClickListener {
            val password = newPasswordEt.text.toString()
            if (password.isNotEmpty()) {
                val newPassword = password

                // Carry out procedure to store the new password
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val imageUri: Uri? = result.data?.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                profileImageView.setImageBitmap(bitmap)

                //Then send store the content of profileImageView somewhere? or get a string representation, we have imageUri
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
