package com.example.travelmate2.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate2.R
import com.example.travelmate2.model.Booking
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminBookList : AppCompatActivity() {

    private lateinit var bookingAdapter: BookingAdapter
    private val bookingList = mutableListOf<Booking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book_list)

        // Set up RecyclerView
        val recyclerViewBookings = findViewById<RecyclerView>(R.id.recyclerViewBookings)
        recyclerViewBookings.layoutManager = LinearLayoutManager(this)

        bookingAdapter = BookingAdapter(bookingList
        ) { booking ->
            cancelBooking(booking)
        }

        recyclerViewBookings.adapter = bookingAdapter

        // Fetch bookings from Firestore
        fetchBookings()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_notifi
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, AdminHome::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_booking -> {
                    val intent = Intent(this, CustomerRequest::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_notifi -> {

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

    // Adapter for RecyclerView
    class BookingAdapter(
        private val bookingList: List<Booking>,
        private val onCancelClick: (Booking) -> Unit
    ) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

        class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val destination: TextView = itemView.findViewById(R.id.bookingDestination)
            val pickupLocation: TextView = itemView.findViewById(R.id.bookingPickupLocation)
            val cancelButton: Button = itemView.findViewById(R.id.buttonCancelBooking)
            val bookingAmount: TextView = itemView.findViewById(R.id.bookingAmount)
            val userName: TextView = itemView.findViewById(R.id.userName)
            val pickupDate: TextView = itemView.findViewById(R.id.bookingPickupDate)
            val pickupTime: TextView = itemView.findViewById(R.id.bookingPickupTime)
            val bookingId: TextView = itemView.findViewById(R.id.bookingId)
            val mobile: TextView = itemView.findViewById(R.id.textView20)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking2, parent, false)
            return BookingViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
            val booking = bookingList[position]
            val db = FirebaseFirestore.getInstance()

            holder.destination.text = booking.destination
            holder.pickupLocation.text = booking.pickupLocation
            holder.pickupDate.text = booking.pickupDate
            holder.pickupTime.text = booking.pickupTime
            holder.userName.text = booking.name
            holder.bookingId.text = booking.documentId
            holder.mobile.text = booking.mobile

            // Fetch the latest `isRequestQuote` and `amount` values from Firestore for this booking
            db.collection("bookings").document(booking.documentId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val isRequestQuote = document.getBoolean("isRequestQuote") ?: true
                        val amount = document.getString("amount") ?: "Amount not available"

                        // Check if `isRequestQuote` is true or false and update the UI accordingly
                        if (isRequestQuote) {
                            holder.bookingAmount.text = "Quote Pending"
                        } else {
                            holder.bookingAmount.text = "$$amount"
                        }
                    } else {
                        holder.bookingAmount.text = "Document not found"
                    }
                }
                .addOnFailureListener { exception ->
                    holder.bookingAmount.text = "Error fetching quote: ${exception.message}"
                }

            // Set click listeners for buttons
            holder.cancelButton.setOnClickListener {
                onCancelClick(booking)
            }

        }


        override fun getItemCount(): Int {
            return bookingList.size
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchBookings() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("bookings")
                .whereEqualTo("isBooking", true)
                .whereEqualTo("isConfirmed", true)
                .get()
                .addOnSuccessListener { result ->
                    bookingList.clear() // Clear the list before adding new data
                    for (document in result) {
                        val booking = document.toObject(Booking::class.java).apply {
                            this.documentId = document.id // Store the document ID
                        }
                        bookingList.add(booking) // Add each booking to the list
                    }

                    if (bookingList.isEmpty()) {
                        // If no bookings exist, navigate to the Home activity
                        Toast.makeText(this, "No bookings found. Redirecting to Home...", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish() // Optional: Finish the current activity
                    } else {
                        bookingAdapter.notifyDataSetChanged() // Notify the adapter to refresh the list
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error fetching bookings: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }


    // Handle booking cancellation
    private fun cancelBooking(booking: Booking) {
        val db = FirebaseFirestore.getInstance()

        // Ensure that you use the Firestore document ID for the update
        db.collection("bookings").document(booking.documentId)
            .update(mapOf(
                "isCancel" to true,
                "isBooking" to false
            ))
            .addOnSuccessListener {
                Toast.makeText(this, "Booking cancelled successfully!", Toast.LENGTH_SHORT).show()
                fetchBookings() // Refresh the list after cancellation
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error cancelling booking: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
