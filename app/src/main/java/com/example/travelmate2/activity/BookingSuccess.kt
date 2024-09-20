package com.example.travelmate2.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelmate2.MainActivity
import com.example.travelmate2.R
import com.example.travelmate2.databinding.ActivityBookingSuccessBinding



class BookingSuccess : AppCompatActivity() {
    private lateinit var binding: ActivityBookingSuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_success)

        binding.imageView9.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }


}