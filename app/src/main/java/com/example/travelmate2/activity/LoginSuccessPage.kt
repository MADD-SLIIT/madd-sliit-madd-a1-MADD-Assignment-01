package com.example.travelmate2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelmate2.R
import android.content.Intent
import com.example.travelmate2.databinding.ActivityLoginSuccessPageBinding
class LoginSuccessPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSuccessPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_success_page)

        binding = ActivityLoginSuccessPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button7.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

    }
}