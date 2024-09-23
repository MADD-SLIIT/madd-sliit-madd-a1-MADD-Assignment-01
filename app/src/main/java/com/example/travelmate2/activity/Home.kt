//package com.example.travelmate2.activity
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.travelmate2.R
//import com.google.firebase.firestore.FirebaseFirestore
//import com.squareup.picasso.Picasso
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView.Adapter
//import androidx.recyclerview.widget.RecyclerView.ViewHolder
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.recyclerview.widget.GridLayoutManager
//import com.example.travelmate2.model.Place
//import com.example.travelmate2.model.SinglePlace
//import com.example.travelmate2.databinding.ActivityHomeBinding
//import com.google.android.material.bottomnavigation.BottomNavigationView
//
//class Home : AppCompatActivity() {
//    private lateinit var binding: ActivityHomeBinding
//    private lateinit var placeAdapter: PlaceAdapter
//    private lateinit var singlePlaceAdapter: SinglePlaceAdapter
//    private val placesList = mutableListOf<Place>()
//    private val singlePlacesList = mutableListOf<SinglePlace>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Use view binding for better layout handling
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Set up RecyclerView for places (horizontal)
//        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        placeAdapter = PlaceAdapter(placesList) { place ->
//            // Navigate to LocationActivity when a place is clicked
//            navigateToLocationData(place.id)
//        }
//        binding.recyclerView.adapter = placeAdapter
//
//        // Set up RecyclerView for single places (grid, 2 columns)
//        binding.recyclerView2.layoutManager = GridLayoutManager(this, 2)
//        singlePlaceAdapter = SinglePlaceAdapter(singlePlacesList) { singlePlace ->
//            navigateToPlaceDetails(singlePlace.id)
//        }
//        binding.recyclerView2.adapter = singlePlaceAdapter
//
//        // Fetch data from Firestore for both RecyclerViews
//        fetchPlacesFromFirestore()
//        fetchSinglePlacesFromFirestore()
//
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.nav_home -> {
//                    // Already on Home, do nothing
//                    true
//                }
//                R.id.nav_booking -> {
//                    val intent = Intent(this, BookingList::class.java)
//                    startActivity(intent)
//                    true
//                }
//                R.id.nav_notifi -> {
//                    val intent = Intent(this, Notification::class.java)
//                    startActivity(intent)
//                    true
//                }
//                R.id.nav_profile -> {
//                    val intent = Intent(this, Profile::class.java)
//                    startActivity(intent)
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//
//    private fun navigateToLocationData(locationId: String) {
//        // Navigate to LocationActivity with the location_id
//        val intent = Intent(this, LocationActivity::class.java)
//        intent.putExtra("location_id", locationId) // Passing the location ID
//        startActivity(intent)
//    }
//
//    private fun navigateToPlaceDetails(placeId: String) {
//        // Navigate to PlaceDetailsActivity when a single place is clicked
//        val intent = Intent(this, PlaceDetailsActivity::class.java)
//        intent.putExtra("place_id", placeId) // Passing the place ID
//        startActivity(intent)
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun fetchSinglePlacesFromFirestore() {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("singleplace")
//            .get()
//            .addOnSuccessListener { result ->
//                singlePlacesList.clear()
//                for (document in result) {
//                    val singlePlace = document.toObject(SinglePlace::class.java)
//                    singlePlacesList.add(singlePlace)
//                }
//                singlePlaceAdapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener {
//                // Handle error
//            }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun fetchPlacesFromFirestore() {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("places")
//            .get()
//            .addOnSuccessListener { result ->
//                placesList.clear()
//                for (document in result) {
//                    val place = document.toObject(Place::class.java)
//                    placesList.add(place)
//                }
//                placeAdapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener {
//                // Handle error
//            }
//    }
//
//    // RecyclerView Adapter for displaying places (image and description)
//    class PlaceAdapter(
//        private val places: List<Place>,
//        private val onClick: (Place) -> Unit
//    ) : Adapter<PlaceAdapter.PlaceViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
//            return PlaceViewHolder(view, onClick)
//        }
//
//        override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
//            val place = places[position]
//            holder.bind(place)
//        }
//
//        override fun getItemCount(): Int {
//            return places.size
//        }
//
//        class PlaceViewHolder(itemView: View, private val onClick: (Place) -> Unit) : ViewHolder(itemView) {
//            private val imageView: ImageView = itemView.findViewById(R.id.imageView)
//
//            fun bind(place: Place) {
//                // Load image using Picasso
//                Picasso.get().load(place.url).into(imageView)
//
//                // Set click listener for navigating to location details
//                itemView.setOnClickListener {
//                    onClick(place)
//                }
//            }
//        }
//    }
//
//    // Adapter for SinglePlace with ClickListener for each item
//    class SinglePlaceAdapter(
//        private val singlePlaces: List<SinglePlace>,
//        private val onClick: (SinglePlace) -> Unit
//    ) : Adapter<SinglePlaceAdapter.SinglePlaceViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglePlaceViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
//            return SinglePlaceViewHolder(view, onClick)
//        }
//
//        override fun onBindViewHolder(holder: SinglePlaceViewHolder, position: Int) {
//            val singlePlace = singlePlaces[position]
//            holder.bind(singlePlace)
//        }
//
//        override fun getItemCount(): Int {
//            return singlePlaces.size
//        }
//
//        class SinglePlaceViewHolder(itemView: View, private val onClick: (SinglePlace) -> Unit) : ViewHolder(itemView) {
//            private val imageView: ImageView = itemView.findViewById(R.id.imageView)
//            private val descriptionView: TextView = itemView.findViewById(R.id.descriptionView)
//            private val moreButton: Button = itemView.findViewById(R.id.button9)
//
//            fun bind(singlePlace: SinglePlace) {
//                // Load image using Picasso
//                Picasso.get().load(singlePlace.url).into(imageView)
//                // Set description
//                descriptionView.text = singlePlace.des
//                descriptionView.textSize = 12f
//                descriptionView.setTypeface(null, android.graphics.Typeface.BOLD)
//
//                moreButton.setOnClickListener {
//                    onClick(singlePlace)
//                }
//            }
//        }
//    }
//}
package com.example.travelmate2.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate2.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travelmate2.MainActivity
import com.example.travelmate2.model.Place
import com.example.travelmate2.model.SinglePlace
import com.example.travelmate2.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var singlePlaceAdapter: SinglePlaceAdapter
    private val placesList = mutableListOf<Place>()
    private val singlePlacesList = mutableListOf<SinglePlace>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is signed in
        checkIfUserIsSignedIn()

        // Use view binding for better layout handling
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView for places (horizontal)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        placeAdapter = PlaceAdapter(placesList) { place ->
            // Navigate to LocationActivity when a place is clicked
            navigateToLocationData(place.id)
        }
        binding.recyclerView.adapter = placeAdapter

        // Set up RecyclerView for single places (grid, 2 columns)
        binding.recyclerView2.layoutManager = GridLayoutManager(this, 2)
        singlePlaceAdapter = SinglePlaceAdapter(singlePlacesList) { singlePlace ->
            navigateToPlaceDetails(singlePlace.id)
        }
        binding.recyclerView2.adapter = singlePlaceAdapter

        // Fetch data from Firestore for both RecyclerViews
        fetchPlacesFromFirestore()
        fetchSinglePlacesFromFirestore()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already on Home, do nothing
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

    // Check if user is signed in, if not redirect to MainActivity
    private fun checkIfUserIsSignedIn() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // If no user is signed in, navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish() // Finish the Home activity
        }
    }

    private fun navigateToLocationData(locationId: String) {
        // Navigate to LocationActivity with the location_id
        val intent = Intent(this, LocationActivity::class.java)
        intent.putExtra("location_id", locationId) // Passing the location ID
        startActivity(intent)
    }

    private fun navigateToPlaceDetails(placeId: String) {
        // Navigate to PlaceDetailsActivity when a single place is clicked
        val intent = Intent(this, PlaceDetailsActivity::class.java)
        intent.putExtra("place_id", placeId) // Passing the place ID
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchSinglePlacesFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("singleplace")
            .get()
            .addOnSuccessListener { result ->
                singlePlacesList.clear()
                for (document in result) {
                    val singlePlace = document.toObject(SinglePlace::class.java)
                    singlePlacesList.add(singlePlace)
                }
                singlePlaceAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Handle error
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchPlacesFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("places")
            .get()
            .addOnSuccessListener { result ->
                placesList.clear()
                for (document in result) {
                    val place = document.toObject(Place::class.java)
                    placesList.add(place)
                }
                placeAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Handle error
            }
    }

    // RecyclerView Adapter for displaying places (image and description)
    class PlaceAdapter(
        private val places: List<Place>,
        private val onClick: (Place) -> Unit
    ) : Adapter<PlaceAdapter.PlaceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
            return PlaceViewHolder(view, onClick)
        }

        override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
            val place = places[position]
            holder.bind(place)
        }

        override fun getItemCount(): Int {
            return places.size
        }

        class PlaceViewHolder(itemView: View, private val onClick: (Place) -> Unit) : ViewHolder(itemView) {
            private val imageView: ImageView = itemView.findViewById(R.id.imageView)

            fun bind(place: Place) {
                // Load image using Picasso
                Picasso.get().load(place.url).into(imageView)

                // Set click listener for navigating to location details
                itemView.setOnClickListener {
                    onClick(place)
                }
            }
        }
    }

    // Adapter for SinglePlace with ClickListener for each item
    class SinglePlaceAdapter(
        private val singlePlaces: List<SinglePlace>,
        private val onClick: (SinglePlace) -> Unit
    ) : Adapter<SinglePlaceAdapter.SinglePlaceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglePlaceViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
            return SinglePlaceViewHolder(view, onClick)
        }

        override fun onBindViewHolder(holder: SinglePlaceViewHolder, position: Int) {
            val singlePlace = singlePlaces[position]
            holder.bind(singlePlace)
        }

        override fun getItemCount(): Int {
            return singlePlaces.size
        }

        class SinglePlaceViewHolder(itemView: View, private val onClick: (SinglePlace) -> Unit) : ViewHolder(itemView) {
            private val imageView: ImageView = itemView.findViewById(R.id.imageView)
            private val descriptionView: TextView = itemView.findViewById(R.id.descriptionView)
            private val moreButton: Button = itemView.findViewById(R.id.button9)

            fun bind(singlePlace: SinglePlace) {
                // Load image using Picasso
                Picasso.get().load(singlePlace.url).into(imageView)
                // Set description
                descriptionView.text = singlePlace.des
                descriptionView.textSize = 12f
                descriptionView.setTypeface(null, android.graphics.Typeface.BOLD)

                moreButton.setOnClickListener {
                    onClick(singlePlace)
                }
            }
        }
    }
}
