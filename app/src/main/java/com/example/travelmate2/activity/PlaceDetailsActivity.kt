package com.example.travelmate2.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmate2.R
import com.example.travelmate2.model.SinglePlace
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

import android.content.Intent
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlaceDetailsActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var descriptionView: TextView
    private lateinit var buttonBookTrip: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        imageView = findViewById(R.id.imageView)
        descriptionView = findViewById(R.id.descriptionView)
        buttonBookTrip = findViewById(R.id.button8)

        // Get the place_id from intent extras
        val placeId = intent.getStringExtra("place_id")

        // Fetch data from Firestore using placeId
        fetchPlaceDetails(placeId)

        // Navigate to BookingActivity on button click with placeId as extra
        buttonBookTrip.setOnClickListener {
            val intent = Intent(this, Booking::class.java)
            intent.putExtra("place_id", placeId)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.clearAnimation()
        bottomNavigationView.selectedItemId = R.id.nav_notifi
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
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }
    }

    private fun fetchPlaceDetails(placeId: String?) {
        val db = FirebaseFirestore.getInstance()
        placeId?.let {
            db.collection("singleplace").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val singlePlace = document.toObject(SinglePlace::class.java)
                        singlePlace?.let { place ->
                            Picasso.get().load(place.url).into(imageView)
                            descriptionView.text = place.des
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }
}
