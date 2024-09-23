package com.example.travelmate2.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmate2.R
import com.example.travelmate2.databinding.ActivityEmailPasswordSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EmailPasswordSignUp : AppCompatActivity() {

    private lateinit var binding: ActivityEmailPasswordSignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailPasswordSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set an OnClickListener for the textView2 to navigate back to EmailPasswordLogin
        binding.textView2.setOnClickListener {
            val intent = Intent(this, EmailPasswordLogin::class.java)
            startActivity(intent)
        }

        // Set an OnClickListener for imageView2 to navigate back to LoginPage
        binding.imageView2.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        // Handle the sign-up button click
        binding.button4.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                signUpWithEmailPassword(email, password)
            }
        }
    }

    private fun signUpWithEmailPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-up success, save user data to Firestore
                    val user = auth.currentUser
                    if (user != null) {
                        saveUserDataToFirestore(user.uid, user.email)
                    }
                    Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()

                    // Navigate to the main activity or another screen after successful sign-up
                    val intent = Intent(this, LoginSuccessPage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign-up fails, display a message to the user
                    Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserDataToFirestore(userId: String, email: String?) {
        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "uid" to userId,
            "email" to email,
            "userType" to "traveler",
            "name" to "",
            "mobile" to "",
            "country" to "",
            "address" to ""
        )

        // Save user data under the "users" collection
        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "User data saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
