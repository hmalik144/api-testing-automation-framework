package api

import model.*
import retrofit2.Response
import retrofit2.http.*

interface RestfulBookerApi {

    @Headers("Content-Type:application/json")
    @POST("auth")
    suspend fun createAuthToken(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Response<AuthResponse>

    @GET("booking")
    suspend fun getBookingIds(
        @Field("firstname") firstname: String? = null,
        @Field("lastname") lastname: String? = null,
        @Field("checkin") checkin: String? = null,
        @Field("checkout") checkout: String? = null,
    ): Response<List<BookingIdResponse>>

    @GET("booking/{id}")
    suspend fun getSingleBooking(
        @Path("id") id: String
    ): Response<BookingResponse>

    @Headers("Content-Type:application/json")
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
        @Field("firstname") firstname: String? = null,
        @Field("lastname") lastname: String? = null,
        @Field("totalprice") totalprice: Float? = null,
        @Field("depositpaid") depositpaid: Boolean? = null,
        @Field("checkin") checkin: String? = null,
        @Field("checkout") checkout: String? = null,
        @Field("additionalneeds") additionalneeds: String? = null
    ): Response<BookingResponse>

    @DELETE("booking/{id}")
    suspend fun deleteBooking(
        @Path("id") id: String
    ): Response<Any>
}