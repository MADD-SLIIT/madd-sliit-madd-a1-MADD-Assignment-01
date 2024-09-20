package com.example.travelmate2.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate2.R
import com.example.travelmate2.model.Place
import com.example.travelmate2.model.SinglePlace
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class LocationActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var descriptionView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var singlePlaceAdapter: SinglePlaceAdapter
    private val singlePlacesList = mutableListOf<SinglePlace>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        imageView = findViewById(R.id.imageView)
        descriptionView = findViewById(R.id.descriptionView)
        recyclerView = findViewById(R.id.recyclerViewSinglePlaces)

        // Set up the RecyclerView with GridLayout (2 images per row)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        singlePlaceAdapter = SinglePlaceAdapter(singlePlacesList) { singlePlace ->
            navigateToPlaceDetails(singlePlace.id)
        }
        recyclerView.adapter = singlePlaceAdapter

        // Get the location_id from intent extras
        val locationId = intent.getStringExtra("location_id")

        // Fetch and display location details from Firestore using the locationId
        fetchLocationDetails(locationId)

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

    private fun fetchLocationDetails(locationId: String?) {
        val db = FirebaseFirestore.getInstance()
        locationId?.let {
            db.collection("places").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val place = document.toObject(Place::class.java)
                        place?.let {
                            // Load image and description
                            Picasso.get().load(place.url).into(imageView)
                            descriptionView.text = place.location

                            // Fetch single places that match the location
                            fetchSinglePlacesByLocation(place.location)
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchSinglePlacesByLocation(location: String?) {
        val db = FirebaseFirestore.getInstance()
        location?.let {
            // Use Firestore query with case-insensitive comparison (optional if case is an issue)
            db.collection("singleplace")
                .whereEqualTo("location", it.trim())  // Make sure to trim any extra spaces
                .get()
                .addOnSuccessListener { result ->
                    singlePlacesList.clear()
                    if (result != null && !result.isEmpty) {
                        for (document in result) {
                            val singlePlace = document.toObject(SinglePlace::class.java)
                            singlePlacesList.add(singlePlace)
                        }
                        singlePlaceAdapter.notifyDataSetChanged() // Notify the adapter once all data is added
                    }
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }

    private fun navigateToPlaceDetails(placeId: String) {
        // Navigate to PlaceDetailsActivity when an item is clicked
        val intent = Intent(this, PlaceDetailsActivity::class.java)
        intent.putExtra("place_id", placeId)
        startActivity(intent)
    }

    // Adapter for SinglePlace with a 2-column layout
    class SinglePlaceAdapter(
        private val singlePlaces: List<SinglePlace>,
        private val onClick: (SinglePlace) -> Unit
    ) : RecyclerView.Adapter<SinglePlaceAdapter.SinglePlaceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglePlaceViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item2, parent, false)
            return SinglePlaceViewHolder(view, onClick)
        }

        override fun onBindViewHolder(holder: SinglePlaceViewHolder, position: Int) {
            val singlePlace = singlePlaces[position]
            holder.bind(singlePlace)
        }

        override fun getItemCount(): Int {
            return singlePlaces.size
        }

        class SinglePlaceViewHolder(itemView: View, private val onClick: (SinglePlace) -> Unit) : RecyclerView.ViewHolder(itemView) {
            private val imageView: ImageView = itemView.findViewById(R.id.imageView)
            private val placeView: TextView = itemView.findViewById(R.id.placeView)
            private val descriptionView: TextView = itemView.findViewById(R.id.descriptionView)
            private val moreButton: Button = itemView.findViewById(R.id.button9)

            fun bind(singlePlace: SinglePlace) {
                // Load image using Picasso
                Picasso.get().load(singlePlace.url).into(imageView)

                // Set description and place
                descriptionView.text = singlePlace.des
                placeView.text = singlePlace.place

                // Set button click listener to navigate to PlaceDetailsActivity
                moreButton.setOnClickListener {
                    onClick(singlePlace)
                }
            }
        }
    }

}
