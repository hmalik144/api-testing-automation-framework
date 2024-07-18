package api

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookerApi {
    private val baseUrl = "https://restful-booker.herokuapp.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun buildOkHttpClient(timeoutSeconds: Long = 30L): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder
            .addInterceptor(loggingInterceptor)
            .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)

        return builder.build()
    }

    fun invoke(): RestfulBookerApi {
        val okHttpClient = buildOkHttpClient()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestfulBookerApi::class.java)
    }
}