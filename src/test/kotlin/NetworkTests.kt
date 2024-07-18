import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.io.IOException

abstract class NetworkTests {

    fun <T : Any> responseUnwrap(
        call: suspend () -> Response<T>
    ): T {

        val response = runBlocking { call.invoke() }
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()

            throw IOException(error ?: "Unable to handle end point")
        }
    }
}