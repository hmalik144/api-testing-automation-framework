package org.example.api

import org.example.model.*
import retrofit2.Response
import retrofit2.http.*

interface RestfulBookerApi {

    @Headers("Content-Type:application/json", "Accept: application/json")
    @POST("auth")
    suspend fun createAuthToken(
        @Body authRequest: AuthRequest
    ): Response<AuthResponse>

    @GET("booking")
    suspend fun getBookingIds(

    ): Response<List<BookingIdResponse>>

    @GET("booking/{id}")
    suspend fun getSingleBooking(
        @Path("id") id: String
    ): Response<BookingResponse>

    @Headers("Content-Type:application/json", "Accept: application/json")
    @POST("booking")
    suspend fun createBooking(
        @Body booking: BookingRequest,
    ): Response<CreateBookingResponse>

    @Headers("Content-Type:application/json", "Accept: application/json")
    @PUT("booking/{id}")
    suspend fun updateBooking(
        @Path("id") id: String,
        @Body booking: BookingRequest,
        @Header("Authorization") token: String
    ): Response<BookingResponse>

    @Headers("Content-Type:application/json", "Accept: application/json")
    @PATCH("booking/{id}")
    suspend fun partialUpdateBooking(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body update: UpdateBookingRequest
    ): Response<BookingResponse>

    @DELETE("booking/{id}")
    suspend fun deleteBooking(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<Any>
}