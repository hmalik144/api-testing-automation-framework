package model

data class BookingRequest (
    var firstname: String,
    var lastname: String,
    var totalprice: Int,
    var depositpaid: Boolean,
    var bookingdates: Bookingdates,
    var additionalneeds: String,
)


