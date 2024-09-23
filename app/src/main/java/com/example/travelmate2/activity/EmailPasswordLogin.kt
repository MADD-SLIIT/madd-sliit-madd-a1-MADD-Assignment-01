package com.example.travelmate2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.example.travelmate2.R
import com.example.travelmate2.databinding.ActivityEmailPasswordLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EmailPasswordLogin : AppCompatActivity() {

    private lateinit var binding: ActivityEmailPasswordLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailPasswordLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Click listener for back button (imageView2)
        binding.imageView2.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        binding.textView5.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }

        // Click listener for signup text (textView2)
        binding.textView2.setOnClickListener {
            val intent = Intent(this, EmailPasswordSignUp::class.java)
            startActivity(intent)
        }

        // Sign in button click listener
        binding.button4.setOnClickListener {
            val email = binding.textInputEditText.text.toString().trim()
            val password = binding.textInputPassword.text.toString().trim()  // Make sure to replace the ID with the correct one for password input

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            } else {
                signInWithEmailAndPassword(email, password)
            }
        }
    }

    class LoginPage {

    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Get the currently signed-in user
                    val user = auth.currentUser
                    if (user != null) {
                        // Reference to Firestore
                        val db = FirebaseFirestore.getInstance()

                        // Fetch user data from Firestore based on UID
                        val userDocRef = db.collection("users").document(user.uid)
                        userDocRef.get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    // Extract the userType field

                                    // Check user type and navigate accordingly using 'when' expression
                                    when (val userType = document.getString("userType")) {
                                        "traveler" -> {
                                            // Navigate to LoginSuccessPage if user is a traveler
                                            val intent = Intent(this, Home::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        "admin" -> {
                                            // Navigate to AdminHome if user is an admin
                                            val intent = Intent(this, AdminHome::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        else -> {
                                            // Handle the case if userType is missing or unknown
                                            Toast.makeText(this, "Unknown user type: $userType", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    // Handle the case where the document does not exist
                                    Toast.makeText(this, "User data not found in Firestore", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                // Handle Firestore retrieval failure
                                Toast.makeText(this, "Failed to retrieve user data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Handle authentication failure
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
