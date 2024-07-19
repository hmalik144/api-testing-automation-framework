package org.example.storage

import org.example.model.BookingResponse
import org.example.model.Bookingdates

class OrdersDatabase {
    private val storage = mutableMapOf<Int, BookingResponse>()

    // Create
    fun insertBooking(id: Int, booking: BookingResponse) {
        if (storage.contains(id)) {
            storage.replace(id, booking)
        } else {
            storage[id] = booking
        }
    }

    // Read
    fun getIdsOfBookingsAvailable() = storage.keys.toList()
    fun getBookingsAvailable() = storage.values.toList()
    fun getIdsAndBookings() = storage.toMap()

    fun getIdsOfOrderBasedOnValues(
        firstname: String? = null,
        lastname: String? = null,
        totalprice: Int? = null,
        depositpaid: Boolean? = null,
        checkin: String? = null,
        checkout: String? = null,
        additionalneeds: String? = null
    ): List<Int> {
        return storage.filterValues {
            firstname?.let { f -> f == it.firstname } ?: true &&
                    lastname?.let { f -> f == it.lastname } ?: true &&
                    totalprice?.let { f -> f == it.totalprice } ?: true &&
                    depositpaid?.let { f -> f == it.depositpaid } ?: true &&
                    checkin?.let { f -> f == it.bookingdates.checkin } ?: true &&
                    checkout?.let { f -> f == it.bookingdates.checkout } ?: true &&
                    additionalneeds?.let { f -> f == it.additionalneeds } ?: true
        }.keys.toList()
    }

    fun getIdsOfOrderBasedOnValues(
        id: Int
    ): BookingResponse? {
        return storage[id]
    }

    // Update
    fun updateCompleteOrder(id: Int, newBookingResponse: BookingResponse) {
        insertBooking(id, newBookingResponse)
    }

    fun updateOrderPartial(
        id: Int,
        firstname: String? = null,
        lastname: String? = null,
        totalprice: Int? = null,
        depositpaid: Boolean? = null,
        checkin: String? = null,
        checkout: String? = null,
        additionalneeds: String? = null
    ) {
        if (storage.keys.remove(id)) {
            storage.compute(id) { k, v ->
                val mFirstName = firstname ?: v!!.firstname
                val mlastname = lastname ?: v!!.lastname
                val mTotalprice = totalprice ?: v!!.totalprice
                val mDepositpaid = depositpaid ?: v!!.depositpaid
                val mCheckin = checkin ?: v!!.bookingdates.checkin
                val mCheckout = checkout ?: v!!.bookingdates.checkout
                val mAdditionalneeds = additionalneeds ?: v!!.additionalneeds
                BookingResponse(
                    firstname = mFirstName,
                    lastname = mlastname,
                    totalprice = mTotalprice,
                    depositpaid = mDepositpaid,
                    bookingdates = Bookingdates(
                        checkin = mCheckin,
                        checkout = mCheckout
                    ),
                    additionalneeds = mAdditionalneeds
                )
            }
        }
    }

    // Delete
    fun clearAllData() = storage.clear()

    fun deleteSingleEntry(id: Int) = storage.remove(id)
}