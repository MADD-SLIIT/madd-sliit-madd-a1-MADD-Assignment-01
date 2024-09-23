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
import com.google.android.material.textfield.TextInputEditText

class CustomerRequest : AppCompatActivity() {

    private lateinit var bookingAdapter: BookingAdapter
    private val bookingList = mutableListOf<Booking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_list)

        // Set up RecyclerView
        val recyclerViewBookings = findViewById<RecyclerView>(R.id.recyclerViewBookings)
        recyclerViewBookings.layoutManager = LinearLayoutManager(this)

        bookingAdapter = BookingAdapter(bookingList, { booking ->
            cancelBooking(booking)
        }, { booking, updatedAmount ->
            confirmBooking(booking, updatedAmount)
        })

        recyclerViewBookings.adapter = bookingAdapter

        // Fetch bookings from Firestore
        fetchBookings()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_booking
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, AdminHome::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_booking -> {

                    true
                }

                R.id.nav_notifi -> {
                    val intent = Intent(this, AdminBookList::class.java)
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

    // Adapter for RecyclerView
    class BookingAdapter(
        private val bookingList: List<Booking>,
        private val onCancelClick: (Booking) -> Unit,
        private val onConfirmClick: (Booking, String) -> Unit // Pass updated amount here
    ) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

        class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val destination: TextView = itemView.findViewById(R.id.bookingDestination)
            val pickupLocation: TextView = itemView.findViewById(R.id.bookingPickupLocation)
            val cancelButton: Button = itemView.findViewById(R.id.buttonCancelBooking)
            val confirmButton: Button = itemView.findViewById(R.id.button11)
            val editTextAmount: TextInputEditText = itemView.findViewById(R.id.editTextAmount) // Fetch EditText for amount
            val userName: TextView = itemView.findViewById(R.id.userName)
            val pickupDate: TextView = itemView.findViewById(R.id.bookingPickupDate)
            val pickupTime: TextView = itemView.findViewById(R.id.bookingPickupTime)
            val bookingId: TextView = itemView.findViewById(R.id.bookingId)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.cutomer_reuest_list, parent, false)
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

            // Set click listeners for buttons
            holder.cancelButton.setOnClickListener {
                onCancelClick(booking)
            }

            holder.confirmButton.setOnClickListener {
                val updatedAmount = holder.editTextAmount.text.toString()
                    .trim() // Get the updated amount and trim spaces

                // Check if the amount is empty
                if (updatedAmount.isEmpty()) {
                    Toast.makeText(
                        holder.itemView.context,
                        "Amount cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // If amount is not empty, proceed with confirmation
                    onConfirmClick(booking, updatedAmount)
                }
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
                .whereEqualTo("isConfirmed", false)
                .whereEqualTo("isRequestQuote", true)
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

    // Handle booking confirmation and update the amount
    private fun confirmBooking(booking: Booking, updatedAmount: String) {
        val db = FirebaseFirestore.getInstance()

        // Ensure that you use the Firestore document ID for the update
        db.collection("bookings").document(booking.documentId)
            .update(mapOf(
                "isRequestQuote" to false,
                "amount" to updatedAmount // Update Firestore with the new amount
            ))
            .addOnSuccessListener {
                Toast.makeText(this, "Booking confirmed successfully!", Toast.LENGTH_SHORT).show()
                fetchBookings() // Refresh the list after confirmation
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error confirming booking: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
