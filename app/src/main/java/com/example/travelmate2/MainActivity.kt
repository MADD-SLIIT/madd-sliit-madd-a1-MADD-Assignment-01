package com.example.travelmate2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmate2.activity.EmailPasswordLogin
import com.example.travelmate2.activity.LoginPage
import com.example.travelmate2.databinding.ActivityMainBinding // Import the generated binding class

class MainActivity : AppCompatActivity() {

    // Declare a binding object
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set an OnClickListener for the Next button using binding
        binding.button5.setOnClickListener {
            // Create an Intent to navigate to LoginPage
            val intent = Intent(this, EmailPasswordLogin::class.java)
            startActivity(intent)
        }
    }
}
