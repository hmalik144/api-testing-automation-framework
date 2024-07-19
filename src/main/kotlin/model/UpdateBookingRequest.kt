package model

data class UpdateBookingRequest(
    var firstname: String? = null,
    var lastname: String? = null,
    var totalprice: Int? = null,
    var depositpaid: Boolean? = null,
    var bookingdates: Bookingdates? = null,
    var additionalneeds: String? = null,
)
