//package com.example.travelmate2.activity
//
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.travelmate2.R
//import com.google.firebase.auth.FirebaseAuth
//
//class ForgetPassword : AppCompatActivity() {
//
//    // Declare FirebaseAuth
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_forget_password)
//
//        // Initialize FirebaseAuth
//        auth = FirebaseAuth.getInstance()
//
//        // Get references to UI elements
//        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
//        val resetButton = findViewById<Button>(R.id.buttonResetPassword)
//
//        // Set up onClickListener for reset button
//        resetButton.setOnClickListener {
//            val email = emailEditText.text.toString().trim()
//
//            if (email.isEmpty()) {
//                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
//            } else {
//                resetPassword(email)
//            }
//        }
//    }
//
//    // Method to reset password
//    private fun resetPassword(email: String) {
//        auth.sendPasswordResetEmail(email)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//}
package com.example.travelmate2.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmate2.R
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {

    // Declare FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Get references to UI elements
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val resetButton = findViewById<Button>(R.id.buttonResetPassword)

        // Set up onClickListener for reset button
        resetButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                resetPassword(email)
            }
        }
    }

    // Method to reset password
    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()

                    // Navigate to SignIn page after successful password reset email
                    val intent = Intent(this, EmailPasswordLogin::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear back stack
                    startActivity(intent)
                    finish() // Finish current activity
                } else {
                    Toast.makeText(this, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
