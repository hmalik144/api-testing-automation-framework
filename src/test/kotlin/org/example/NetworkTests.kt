package org.example

import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.io.IOException

abstract class NetworkTests {

    // Call retrofit API and unwrap response or throw exception
    fun <T : Any> responseUnwrap(
        call: suspend () -> Response<T>
    ): T {

        val response = runBlocking { call.invoke() }
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = StringBuilder().append(response.code()).append(" : ")
                .append(response.errorBody()?.string() ?: "Unable to handle end point").toString()
            print(response.raw())
            throw IOException(error)
        }
    }

}