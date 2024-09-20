package com.example.travelmate2.activity
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmate2.R
import com.example.travelmate2.databinding.ActivityLoginPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Booking : AppCompatActivity() {
    private lateinit var bookingDescriptionView: TextView
    private lateinit var bookingPlaceView: TextView
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextMobile: EditText
    private lateinit var editTextPickupLocation: EditText
    private lateinit var editTextDestination: EditText
    private lateinit var buttonSubmit: Button


    @SuppressLint("CutPasteId", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)



        bookingDescriptionView = findViewById(R.id.locationTextView)
        bookingPlaceView = findViewById(R.id.textInputEditText6)
        editTextDate = findViewById(R.id.editTextDate)
        editTextTime = findViewById(R.id.editTextTime)
        editTextName = findViewById(R.id.textInputEditText2)
        editTextMobile = findViewById(R.id.textInputEditText3)
        editTextPickupLocation = findViewById(R.id.textInputEditText7)
        editTextDestination = findViewById(R.id.textInputEditText6)
        buttonSubmit = findViewById(R.id.button10)


        // Set up date and time pickers
        editTextDate.setOnClickListener { showDatePickerDialog() }
        editTextTime.setOnClickListener { showTimePickerDialog() }

        // Fetch user's name and mobile number from Firestore
        fetchUserData()

        // Get the place_id from intent extras
        val placeId = intent.getStringExtra("place_id")

        // Fetch data from Firestore using placeId and display booking details
        fetchPlaceDetailsForBooking(placeId)

        // Set click listener for submit button to save booking data
        buttonSubmit.setOnClickListener {
            saveBookingDataToFirestore(placeId)
        }




        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
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

    // Fetch details from Firestore
    private fun fetchPlaceDetailsForBooking(placeId: String?) {
        val db = FirebaseFirestore.getInstance()
        placeId?.let {
            db.collection("singleplace").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val place = document.getString("place") ?: ""
                        val location = document.getString("location") ?: ""
                        bookingPlaceView.text = place
                        bookingDescriptionView.text = location
                    }
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }

    // Fetch the current user's name and mobile number from Firestore
    private fun fetchUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        userId?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userName = document.getString("name") ?: ""
                        val mobileNumber = document.getString("mobile") ?: ""
                        editTextName.setText(userName)
                        editTextMobile.setText(mobileNumber)
                    }
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }

    // Show DatePickerDialog to choose a date
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            editTextDate.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    // Show TimePickerDialog to choose a time
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            editTextTime.setText(formattedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    // Save booking data to Firestore
    // Save booking data to Firestore
    private fun saveBookingDataToFirestore(placeId: String?) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Create a booking data map without the documentId yet
        val bookingData = hashMapOf(
            "userId" to userId,
            "name" to editTextName.text.toString(),
            "mobile" to editTextMobile.text.toString(),
            "placeId" to placeId,
            "pickupLocation" to editTextPickupLocation.text.toString(),
            "destination" to editTextDestination.text.toString(),
            "pickupDate" to editTextDate.text.toString(),
            "pickupTime" to editTextTime.text.toString(),
            "isBooking" to true,
            "isCancel" to false,
            "isConfirmed" to false,
            "isRequestQuote" to true,
            "amount" to ""
        )

        // Add bookingData to Firestore and get the generated document ID
        db.collection("bookings")
            .add(bookingData)
            .addOnSuccessListener { documentReference ->
                // Get the document ID
                val documentId = documentReference.id

                // Update the document with the document ID
                db.collection("bookings").document(documentId)
                    .update("documentId", documentId)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Booking saved successfully!", Toast.LENGTH_SHORT).show()

                        // Navigate to BookingSuccessPage
                        val intent = Intent(this, BookingSuccess::class.java)
                        startActivity(intent)

                        // Optionally clear fields or finish the current activity
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update documentId!", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save booking!", Toast.LENGTH_SHORT).show()
            }
    }

}

