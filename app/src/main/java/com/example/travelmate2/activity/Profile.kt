//package com.example.travelmate2.activity
//
//import android.os.Bundle
//import android.widget.Button
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.travelmate2.R
//import com.google.android.material.textfield.TextInputEditText
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//
//class Profile : AppCompatActivity() {
//
//    private lateinit var editTextName: TextInputEditText
//    private lateinit var editTextMobile: TextInputEditText
//    private lateinit var editTextCountry: TextInputEditText
//    private lateinit var editTextAddress: TextInputEditText
//    private lateinit var editTextEmail: TextInputEditText
//    private lateinit var updateButton: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
//
//        // Initialize the TextInputEditText fields
//        editTextName = findViewById(R.id.editTextName)
//        editTextMobile = findViewById(R.id.editTextMobile)
//        editTextCountry = findViewById(R.id.editTextCountry)
//        editTextAddress = findViewById(R.id.editTextAddress)
//        editTextEmail = findViewById(R.id.editTextEmail)
//        updateButton = findViewById(R.id.updateButton)
//
//        // Fetch and display user data
//        fetchUserData()
//
//        // Handle update button click
//        updateButton.setOnClickListener {
//            updateUserProfile()
//        }
//    }
//
//    // Fetch current user data from Firestore and populate the fields
//    private fun fetchUserData() {
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val db = FirebaseFirestore.getInstance()
//
//        userId?.let {
//            db.collection("users").document(it)
//                .get()
//                .addOnSuccessListener { document ->
//                    if (document != null && document.exists()) {
//                        // Populate the fields with user data
//                        editTextName.setText(document.getString("name"))
//                        editTextMobile.setText(document.getString("mobile"))
//                        editTextCountry.setText(document.getString("country"))
//                        editTextAddress.setText(document.getString("address"))
//                        editTextEmail.setText(document.getString("email"))  // Email is typically not editable
//                    } else {
//                        Toast.makeText(this, "No profile data found", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Error fetching profile data", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//
//    // Update user profile information in Firestore
//    private fun updateUserProfile() {
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val db = FirebaseFirestore.getInstance()
//
//        val updatedUserData = hashMapOf(
//            "name" to editTextName.text.toString(),
//            "mobile" to editTextMobile.text.toString(),
//            "country" to editTextCountry.text.toString(),
//            "address" to editTextAddress.text.toString(),
//            // Note: We usually don't allow users to update their email like this. It's better to use Firebase Auth for that.
//        )
//
//        userId?.let {
//            db.collection("users").document(it)
//                .update(updatedUserData as Map<String, Any>)
//                .addOnSuccessListener {
//                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//}

package com.example.travelmate2.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmate2.MainActivity
import com.example.travelmate2.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Profile : AppCompatActivity() {

    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextMobile: TextInputEditText
    private lateinit var editTextCountry: TextInputEditText
    private lateinit var editTextAddress: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var updateButton: Button
    private lateinit var signOutButton: Button // Sign-out button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize the TextInputEditText fields
        editTextName = findViewById(R.id.editTextName)
        editTextMobile = findViewById(R.id.editTextMobile)
        editTextCountry = findViewById(R.id.editTextCountry)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextEmail = findViewById(R.id.editTextEmail)
        updateButton = findViewById(R.id.updateButton)
        signOutButton = findViewById(R.id.signOutButton) // Initialize sign-out button

        // Fetch and display user data
        fetchUserData()

        // Handle update button click
        updateButton.setOnClickListener {
            updateUserProfile()
        }

        // Handle sign-out button click
        signOutButton.setOnClickListener {
            signOutUser()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_profile
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_booking -> {
                    val intent = Intent(this, BookingList::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_notifi -> {
                    val intent = Intent(this, Notification::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {

                    true
                }
                else -> false
            }
        }
    }

    // Fetch current user data from Firestore and populate the fields
    private fun fetchUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        userId?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Populate the fields with user data
                        editTextName.setText(document.getString("name"))
                        editTextMobile.setText(document.getString("mobile"))
                        editTextCountry.setText(document.getString("country"))
                        editTextAddress.setText(document.getString("address"))
                        editTextEmail.setText(document.getString("email"))  // Email is typically not editable
                    } else {
                        Toast.makeText(this, "No profile data found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error fetching profile data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Update user profile information in Firestore
    private fun updateUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        val updatedUserData = hashMapOf(
            "name" to editTextName.text.toString(),
            "mobile" to editTextMobile.text.toString(),
            "country" to editTextCountry.text.toString(),
            "address" to editTextAddress.text.toString(),
            // Note: We usually don't allow users to update their email like this. It's better to use Firebase Auth for that.
        )

        userId?.let {
            db.collection("users").document(it)
                .update(updatedUserData as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Sign-out function
    private fun signOutUser() {
        FirebaseAuth.getInstance().signOut() // Sign out from Firebase

        // Redirect to login screen or another activity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // Clear the back stack
        startActivity(intent)
        finish() // Finish the current activity
    }
}
