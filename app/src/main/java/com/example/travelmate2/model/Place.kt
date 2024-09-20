package com.example.travelmate2.model

class Place {
    val id: String = ""
    val url: String = ""
    val location: String = ""

}
class SinglePlace(
    val id: String = "",
    val url: String = "",
    val des: String = "",
    val place: String ="",
    val location: String = ""
)
 class Booking(
    val userId: String = "",
    val name: String = "",
    val mobile: String = "",
    val placeId: String = "",
    val pickupLocation: String = "",
    val destination: String = "",
    val pickupDate: String = "",
    val pickupTime: String = "",
    var documentId: String = "",
     var amount: String = "",

)
