package org.example

import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.message.MessageFormatMessage
import retrofit2.Response
import java.io.IOException

abstract class BaseNetworkTests {
    private val logger = LogManager.getLogger(Tests::javaClass)

    private val env by lazy { dotenv() }

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

    fun getStoredVariable(key: String) = env.get(key)

    fun logMessage(logMessage: String) = logger.info(logMessage)
    fun logMessage(logMessage: MessageFormatMessage) = logger.info(logMessage)
}