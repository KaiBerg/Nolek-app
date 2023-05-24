package com.nolek.application.data.model

data class AuthenticationInfo(
    val userId: String,
    val email: String,
    val token: String
)