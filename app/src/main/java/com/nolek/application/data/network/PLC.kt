package com.nolek.application.data.network

import com.google.gson.annotations.SerializedName
import com.nolek.application.data.model.PLCMeasurement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class PLCResponse(
    val isSuccess: Boolean,
    val result: List<PLCItem>,
    val message: String,
    val errorMessages: String?
)

data class PLCItem(
    val id: String,
    val index: String,
    val creationDateTime: String,
    val item: PLCMeasurement
)

data class PLCRequest(
    @SerializedName("index")
    val index: String,
    @SerializedName("query")
    val query: String,
    @SerializedName("size")
    val size: Int
)

interface PLCDataService {
    @POST("api/Data/get")
    fun GetData(
        @Header("Authorization")
        token: String,
        @Body
        request: PLCRequest
    )
            : Call<PLCResponse>
}