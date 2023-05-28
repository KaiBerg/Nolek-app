package com.nolek.application.data.repository

import android.util.Log
import com.nolek.application.data.model.AuthenticationInfo
import com.nolek.application.data.model.PLCBundle
import com.nolek.application.data.network.PLCDataService
import com.nolek.application.data.network.PLCRequest
import com.nolek.application.data.network.PLCResponse
import com.nolek.application.data.network.room.dao.PlcDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
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
    suspend fun getData(count: Int): Flow<List<PLCBundle>>
}

class NolekPLCMicroserviceRepository @Inject constructor(
    val plcApi: PLCDataService,
    val auth: AuthenticationRepository,
    val plcDao: PlcDao
) : PLCRepository {
    private var startDate = "2023-05-16"
    private val bannedDates = listOf("2023-05-17")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override suspend fun getData(count: Int): Flow<List<PLCBundle>> {
        val info = auth.getUserInfo() ?: return emptyFlow()
        var guard: Boolean
        withContext(Dispatchers.IO) {

            guard = plcDao.getCount().first() == 0 || !lastIndexIsToday()
        }
        if (guard) {
            val topic = "plc_testing-general"
            val l = mutableListOf<PLCBundle>()
            for (date in getIndexes(startDate, startInclusive = false)) {
                val request = PLCRequest(
                    "${date}-${topic}",
                    "index:${date}-${topic}",
                    count
                )
                withContext(Dispatchers.IO) {
                    Log.d("plc", request.toString())
                    val res = apiCall(info, request)
                    if (res != null)
                        l.add(res)
                }
            }
            withContext(Dispatchers.IO) {
                plcDao.insert(*l.toTypedArray())
            }
        }

        return plcDao.getAll()
    }

    private suspend fun lastIndexIsToday(): Boolean {
        val today = dateFormatter.format(LocalDate.now())
        val last = plcDao.getLastBundle().first()
        startDate = last.index.substring(0, startDate.count())
        return last.index.startsWith(today)
    }


    private fun getIndexes(
        startDate: String,
        startInclusive: Boolean = true,
        endInclusive: Boolean = true
    ): List<String> {
        val result = mutableListOf<String>()

        if (startInclusive)
            result.add(startDate)

        val currentDate = LocalDate.now()

        var iterDate = LocalDate.parse(startDate, dateFormatter).plusDays(1)
        while (iterDate < currentDate || endInclusive && iterDate == currentDate) {
            val date = iterDate.format(dateFormatter)
            if (!bannedDates.contains(date))
                result.add(date)
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
                            val resultItem =
                                PLCBundle(index = list[0].index, dataPoints = list.map { it.item })
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
