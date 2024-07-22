package org.example.model


data class BookingResponse(
    var firstname: String,
    var lastname: String,
    var totalprice: Int,
    var depositpaid: Boolean,
    var bookingdates: Bookingdates,
    var additionalneeds: String?
)