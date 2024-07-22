package org.example

import org.example.api.BookerApi
import org.example.api.RestfulBookerApi
import org.example.model.BookingRequest
import org.example.model.UpdateBookingRequest
import org.apache.logging.log4j.message.MessageFormatMessage
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.example.api.createBasicAuthTokenForHeader
import org.junit.jupiter.api.*
import org.example.storage.OrdersDatabase
import org.example.utils.TestHelper
import org.example.utils.PASSWORD_KEY
import org.example.utils.USERNAME_KEY
import java.util.*


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class Tests : BaseNetworkTests() {

    companion object {
        private lateinit var bookerApi: RestfulBookerApi
        private val testHelper = TestHelper()

        private val storage = OrdersDatabase()

        private lateinit var bookingRequestTestOne: BookingRequest
        private lateinit var bookingRequestTestTwo: BookingRequest

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            bookerApi = BookerApi().invoke()

            bookingRequestTestOne = testHelper.readJsonFileFromResources("test1", BookingRequest::class.java)
            bookingRequestTestTwo = testHelper.readJsonFileFromResources("test2", BookingRequest::class.java)
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            storage.clearAllData()
        }
    }


    /**
     * 1.      Generate 3 new bookings --> Log below scenarios to a log file
     *         o	 All available booking ID's
     *         o	 Above added 3 new booking details
     */
    @Test()
    @Order(1)
    fun testScenarioOne() {
        /*
         * Given
         */
        val bookingRequestThree = testHelper.readJsonFileFromResources("test3", BookingRequest::class.java)

        /*
         * When
         */
        val createBookingOneResponse = responseUnwrap { bookerApi.createBooking(bookingRequestTestOne) }
        val createBookingTwoResponse = responseUnwrap { bookerApi.createBooking(bookingRequestTestTwo) }
        val createBookingThreeResponse = responseUnwrap { bookerApi.createBooking(bookingRequestThree) }
        val bookingResponses = listOf(createBookingOneResponse, createBookingTwoResponse, createBookingThreeResponse)

        /*
         * Then
         */
        val bookingIdsResponse = responseUnwrap { bookerApi.getBookingIds() }
        assertThat(bookingIdsResponse.size)
            .withFailMessage("Did not find 3 bookings")
            .isGreaterThanOrEqualTo(3)
        val bookingIds = bookingIdsResponse.map { it.bookingid }.joinToString()

        logMessage("Available booking IDs: $bookingIds")

        // Add the booking details and idea for later
        bookingResponses.forEach { response ->
            storage.insertBooking(response.bookingid, response.booking)

            logMessage(
                MessageFormatMessage(
                    "Booking with ID: {0} has been added: {1}",
                    response.bookingid,
                    testHelper.toJsonString(response.booking)
                )
            )
        }
    }

    /**
     * 2.      Modify total price for test1 to 1000 and test2 to 1500. Log this data to same log file.
     * Test data :
     * •	Test1 with total price 500 who deposit is paid with random check in and checkout dates and having preference of lunch as additional need.
     *
     */
    @Test()
    @Order(2)
    fun testScenarioTwo() {
        /*
         * Given
         */
        // Find my booking ids
        val orderIdTestOne = storage.getIdsOfOrderBasedOnValues(
            firstname = bookingRequestTestOne.firstname,
            lastname = bookingRequestTestOne.lastname,
            checkin = bookingRequestTestOne.bookingdates.checkin,
            checkout = bookingRequestTestOne.bookingdates.checkout
        ).first()
        val orderIdTestTwo = storage.getIdsOfOrderBasedOnValues(
            firstname = bookingRequestTestTwo.firstname,
            lastname = bookingRequestTestTwo.lastname,
            checkin = bookingRequestTestTwo.bookingdates.checkin,
            checkout = bookingRequestTestTwo.bookingdates.checkout
        ).first()

        /*
         * When
         */
        val tokenBuilder = buildBasicAuthToken()
        val updateTestOneResponse = responseUnwrap {
            bookerApi.partialUpdateBooking(
                id = orderIdTestOne.toString(),
                basicHeaderToken = tokenBuilder,
                update = UpdateBookingRequest(
                    totalprice = 1000
                )
            )
        }.also {
            storage.updateCompleteOrder(orderIdTestOne, it)
        }
        val updateTestTwoResponse = responseUnwrap {
            bookerApi.partialUpdateBooking(
                id = orderIdTestTwo.toString(),
                basicHeaderToken = tokenBuilder,
                update = UpdateBookingRequest(
                    totalprice = 1500
                )
            )
        }.also {
            storage.updateCompleteOrder(orderIdTestTwo, it)
        }

        /*
         * Then
         */
        logMessage(
            MessageFormatMessage(
                "Booking with ID: {0} has been updated to the following: {1}",
                orderIdTestOne,
                testHelper.toJsonString(updateTestOneResponse)
            )
        )
        logMessage(
            MessageFormatMessage(
                "Booking with ID: {0} has been updated to the following: {1}",
                orderIdTestTwo,
                testHelper.toJsonString(updateTestTwoResponse)
            )
        )
    }

    /**
     * 3.      Delete one of the booking --> Log return status to same file
     */
    @Test()
    @Order(3)
    fun testScenarioThree() {
        /*
         * Given
         */
        val idOfAny = storage.getIdsOfBookingsAvailable().random()
        val tokenBuilder =
            StringBuilder("Basic ").append(Base64.getEncoder().encodeToString("admin:password123".toByteArray()))
                .toString()

        /*
         * When
         */
        val deleteResponse = responseUnwrap {
            bookerApi.deleteBooking(id = idOfAny.toString(), tokenBuilder)
        }.also { storage.deleteSingleEntry(id = idOfAny) }

        /*
         * Then
         */
        logMessage(
            MessageFormatMessage(
                "Booking with ID: {0} has been delete and given the following response: {1}",
                idOfAny,
                testHelper.toJsonString(deleteResponse)
            )
        )
    }

    /**
     * 4.      Present the data in log file as html report.
     */
    @Test()
    @Order(4)
    fun testScenarioFour() {
        /*
         * Given
         */
        println("The log4j library will create the report in the root of the folder")

        /*
         * When
         */

        /*
         * Then
         */
    }

    /**
     * build a basic header token needed for using end points like partial update
     */
    private fun buildBasicAuthToken(): String {
        val username = getStoredVariable(USERNAME_KEY)
        val password = getStoredVariable(PASSWORD_KEY)

        return createBasicAuthTokenForHeader(username, password)
    }
}