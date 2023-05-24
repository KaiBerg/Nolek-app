package com.nolek.application.data.repository

import android.util.Log
import com.nolek.application.data.model.AuthenticationInfo
import com.nolek.application.data.model.PLCBundle
import com.nolek.application.data.network.PLCDataService
import com.nolek.application.data.network.PLCRequest
import com.nolek.application.data.network.PLCResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
interface PLCRepository {
    suspend fun getData(count: Int): List<PLCBundle>
}

class NolekPLCMicroserviceRepository @Inject constructor(
    val plcApi: PLCDataService,
    val auth: AuthenticationRepository
) : PLCRepository {

    private var data = mutableListOf<PLCBundle>()

    override suspend fun getData(count: Int): List<PLCBundle> {
        val info = auth.getUserInfo() ?: return emptyList()

        if (data.isEmpty()) {
            val topic = "plc_testing-general"
            val startDate = "2023-05-16"

            for (date in getIndexes(startDate)) {
                val request = PLCRequest(
                    "${date}-${topic}",
                    "index:${date}-${topic}",
                    count
                )
                val res = apiCall(info, request)
                if (res != null)
                    data.add(res)
            }
        }

        return data
    }


    private fun getIndexes(startDate: String): List<String> {
        val result = mutableListOf<String>(startDate)
        val currentDate = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var iterDate = LocalDate.parse(startDate, dateFormatter).plusDays(2)

        while (iterDate <= currentDate) {
            result.add(iterDate.format(dateFormatter))
            iterDate = iterDate.plusDays(1)
        }
        return result
    }

    private suspend fun apiCall(info: AuthenticationInfo, request: PLCRequest): PLCBundle? =
        suspendCoroutine { continuation ->
            plcApi.GetData("Bearer ${info.token}", request).enqueue(object : Callback<PLCResponse> {
                override fun onResponse(call: Call<PLCResponse>, response: Response<PLCResponse>) {
                    val body = response.body()
                    if (body?.isSuccess != false) {
                        val list = body?.result
                        if (list != null) {
                            val resultItem = PLCBundle(list[0].index, list.map { it.item })
                            Log.d("PLCRepoLog", "PLC DATA RECIEVED: ${list}")
                            continuation.resume(resultItem)
                        }

                    } else {
                        Log.e("PLCRepoLog", "Error getting data: ${body}")
                        continuation.resume(null)
                    }
                }

                override fun onFailure(call: Call<PLCResponse>, t: Throwable) {
                    Log.e("PLCRepoLog", "Error getting data: ${t.message}", t)
                    continuation.resume(null)
                }


            })
        }
}
