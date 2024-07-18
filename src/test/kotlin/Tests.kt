import api.BookerApi
import api.RestfulBookerApi
import io.restassured.RestAssured.given
import kotlinx.coroutines.runBlocking
import model.BookingRequest
import model.Bookingdates
import org.apache.logging.log4j.LogManager
import org.junit.FixMethodOrder
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.runners.MethodSorters
import utils.FileReader

@FixMethodOrder(MethodSorters.DEFAULT)
class Tests : NetworkTests(){

    companion object {
        private lateinit var bookerApi: RestfulBookerApi
        private lateinit var fileReader: FileReader
        private val logger = LogManager.getLogger(Tests::class.java)

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            bookerApi = BookerApi().invoke()
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {

        }
    }


    /**
     * 1.      Generate 3 new bookings --> Log below scenarios to a log file
     *         o	 All available booking ID's
     *         o	 Above added 3 new booking details
     */
    @Test()
    fun testScenarioOne() {
        // Given
        val bookingRequestOne = fileReader.readJsonFileFromResources<BookingRequest>("test1")
        val bookingRequestTwo = fileReader.readJsonFileFromResources<BookingRequest>("test2")
        val bookingRequestThree = BookingRequest(
            firstname = "Mark",
            lastname = "Wahlberg",
            totalprice = 750,
            depositpaid = true,
            bookingdates = Bookingdates(
                checkin = "2025-01-01",
                checkout = "2025-01-10"
            ),
            additionalneeds = "Breakfast"
        )

        // When
        val createBookingOneResponse = responseUnwrap { bookerApi.createBooking(bookingRequestOne) }
        val createBookingTwoResponse = responseUnwrap { bookerApi.createBooking(bookingRequestTwo) }
        val createBookingThreeResponse = responseUnwrap { bookerApi.createBooking(bookingRequestThree) }

        // Then
        val bookingIds = responseUnwrap { bookerApi.getBookingIds() }
        logger.trace("Available booking IDs: ${bookingIds.joinToString()}")
        bookingIds.forEach {
            val currentBookingResponse = responseUnwrap { bookerApi.getSingleBooking("$it") }
            logger.trace(currentBookingResponse)
        }
    }

    /**
     * 2.      Modify total price for test1 to 1000 and test2 to 1500. Log this data to same log file.
     * Test data :
     * •	Test1 with total price 500 who deposit is paid with random check in and checkout dates and having preference of lunch as additional need.
     *
     */
    @Test()
    fun testScenarioTwo() {
        // Given

        // When

        // Then
    }

    @Test()
    fun testScenarioThree() {
        // Given

        // When

        // Then
    }

    @Test()
    fun testScenarioFour() {
        // Given

        // When

        // Then
    }
}