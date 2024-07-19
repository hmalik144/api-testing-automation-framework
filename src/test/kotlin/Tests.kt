import api.BookerApi
import api.RestfulBookerApi
import model.AuthRequest
import model.BookingRequest
import model.Bookingdates
import model.UpdateBookingRequest
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.export.HtmlExporter
import net.sf.jasperreports.engine.util.JRSaver
import net.sf.jasperreports.export.SimpleHtmlExporterOutput
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.message.MessageFormatMessage
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.*
import storage.OrdersDatabase
import utils.FileReader
import java.io.InputStream
import java.util.*


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class Tests : NetworkTests() {

    companion object {
        private lateinit var bookerApi: RestfulBookerApi
        private val fileReader = FileReader()
        private val logger = LogManager.getLogger("Test")

        val bookingsReportStream: InputStream = javaClass.getResourceAsStream("/bookingsReport.jrxml")
        val jasperReport: JasperReport = JasperCompileManager.compileReport(bookingsReportStream)

        private val storage = OrdersDatabase()

        private lateinit var bookingRequestTestOne: BookingRequest
        private lateinit var bookingRequestTestTwo: BookingRequest

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            bookerApi = BookerApi().invoke()

            bookingRequestTestOne = fileReader.readJsonFileFromResources("test1", BookingRequest::class.java)
            bookingRequestTestTwo = fileReader.readJsonFileFromResources("test2", BookingRequest::class.java)
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            storage.clearAllData()

            JRSaver.saveObject(jasperReport, "bookingReport.jasper");
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
        val bookingIds = responseUnwrap { bookerApi.getBookingIds() }
        assertThat(bookingIds.size)
            .withFailMessage("Did not find 3 bookings")
            .isGreaterThanOrEqualTo(3)

        JasperFillManager.
        logger.trace("Available booking IDs: ${bookingIds.joinToString()}")

        // Add the booking details and idea for later
        bookingResponses.forEach { response ->
            storage.insertBooking(response.bookingid, response.booking)

            logger.trace(
                MessageFormatMessage(
                    "Booking with ID: {0} has been added: {1}",
                    response.bookingid,
                    response.booking
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
        val auth = responseUnwrap {
            bookerApi.createAuthToken(
                AuthRequest(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString()
                )
            )
        }
        val tokenBuilder =
            StringBuilder("Basic ").append(Base64.getEncoder().encodeToString("admin:password123".toByteArray()))
                .toString()
        val updateTestOneResponse = responseUnwrap {
            bookerApi.partialUpdateBooking(
                id = orderIdTestOne.toString(),
                token = tokenBuilder,
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
                token = tokenBuilder,
                update = UpdateBookingRequest(
                    totalprice = 1500
                )
            )
        }.also {
            storage.updateCompleteOrder(orderIdTestTwo, it)
        }
        val updateResponseList = listOf(updateTestOneResponse, updateTestTwoResponse)

        /*
         * Then
         */
        logger.trace(
            MessageFormatMessage(
                "Booking with ID: {0} has been updated to the following: {1}",
                orderIdTestOne,
                updateTestOneResponse
            )
        )
        logger.trace(
            MessageFormatMessage(
                "Booking with ID: {0} has been updated to the following: {1}",
                orderIdTestTwo,
                updateTestTwoResponse
            )
        )
    }

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
        logger.trace(
            MessageFormatMessage(
                "Booking with ID: {0} has been delete and given the following response: {1}",
                idOfAny,
                deleteResponse
            )
        )
    }

    @Test()
    @Order(4)
    fun testScenarioFour() {
        /*
         * Given
         */

        /*
         * When
         */

        /*
         * Then
         */
        val exporter = HtmlExporter()

// Set input ...

// Set input ...
        exporter.exporterOutput = SimpleHtmlExporterOutput("bookingsReport.html")

        exporter.exportReport()
    }
}