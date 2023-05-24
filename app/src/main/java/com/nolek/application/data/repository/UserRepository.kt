package com.nolek.application.data.repository

import android.util.Log
import com.nolek.application.data.model.AuthenticationInfo
import com.nolek.application.data.network.AuthenticationService
import com.nolek.application.data.network.LoginRequest
import com.nolek.application.data.network.LoginResponse
import com.nolek.application.data.network.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
interface AuthenticationRepository {
    fun getUserInfo(): AuthenticationInfo?
    suspend fun Login(info: LoginRequest): Boolean
    suspend fun LogOut() : Boolean
    suspend fun Register(info: RegisterRequest): Boolean
}

@Singleton
class NolekAuthenticationRepository @Inject constructor(val authApi: AuthenticationService) :
    AuthenticationRepository {
    private var userInfo: AuthenticationInfo? = null

    override fun getUserInfo(): AuthenticationInfo? {
        return userInfo
    }

    override suspend fun Login(info: LoginRequest): Boolean = suspendCoroutine { continuation ->
        authApi.login(info).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val body = response.body()
                if (body?.token != null && body.email.isNotEmpty() && body.userId.isNotEmpty()) {
                    userInfo = AuthenticationInfo(body.userId, body.email, body.token)
                    Log.d(
                        "AuthRepoLog",
                        "User logged in with credentials: email{${body.email}}, userId{${body.userId}}, token{${body.token}}"
                    )
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("AuthRepoLog", "Error logging in: ${t.message}", t)
                continuation.resume(false)
            }
        })
    }

    override suspend fun LogOut() : Boolean {
        userInfo = null
        return true
    }


    override suspend fun Register(info: RegisterRequest): Boolean =
        suspendCoroutine { continuation ->
            authApi.register(info).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    val body = response.body()
                    if (response.code() == 202) {
                        Log.d("AuthRepoLog", "registered: ${response}")
                        continuation.resume(true)
                    } else {
                        Log.e("AuthRepoLog", "Error registering: ${response}")
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("AuthRepoLog", "Error registering: ${t.message}", t)
                    continuation.resume(false)
                }
            })
        }

}