package model

data class BookingRequest (
    var firstname: String? = null,
    var lastname: String? = null,
    var totalprice: Int = 0,
    var depositpaid: Boolean = false,
    var bookingdates: Bookingdates? = null,
    var additionalneeds: String? = null,
)


