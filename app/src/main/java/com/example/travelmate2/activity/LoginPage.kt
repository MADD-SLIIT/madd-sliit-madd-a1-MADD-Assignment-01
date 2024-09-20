package com.example.travelmate2.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmate2.MainActivity
import com.example.travelmate2.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {

    // Declare the View Binding object
    private lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set an OnClickListener for the imageView2 to navigate back to MainActivity
        binding.imageView2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.button4.setOnClickListener {
            val intent = Intent(this, EmailPasswordLogin::class.java)
            startActivity(intent)
        }

        binding.textView2.setOnClickListener {
            val intent = Intent(this, EmailPasswordSignUp::class.java)
            startActivity(intent)
        }
    }
}
