package com.nolek.application.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

//"http://ip:5000"

data class LoginResponse(val userId: String, val token: String, val email: String)
data class LoginRequest(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Password")
    val password: String
)

data class RegisterRequest(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Password")
    val password: String,
    @SerializedName("Firstname")
    val firstName: String,
    @SerializedName("Lastname")
    val lastName: String,
    @SerializedName("UserName")
    val userName: String
)


interface AuthenticationService {
    @POST("api/Auth/login")
    fun login(
        @Body
        request: LoginRequest
    ): Call<LoginResponse>

    @POST("api/Auth/register")
    fun register(
        @Body
        request: RegisterRequest
    ): Call<Void>
}
