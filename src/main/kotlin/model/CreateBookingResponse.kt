package model

data class CreateBookingResponse(
    var bookingid: Int,
    var booking: BookingResponse
)