package com.nolek.application.data.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "plc_measurements")
data class PLCMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val LogTime: String,
    val StepNo: Int,
    val CircuitName: String,
    val TMP1: Double,
    val TMP2: Double,
    val B31: Double,
    val B32: Double,
    val B21: Double,
    val B22: Double,
    val P101: Double,
    val RegulatorSP: Double,
    val RegulatorFB: Double
)

@Entity(tableName = "bundle")
@TypeConverters(PLCMeasurementConverter::class)
data class PLCBundle(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "index")
    val index: String,
    val dataPoints: List<PLCMeasurement>
)

class PLCMeasurementConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<PLCMeasurement> {
        val listType = object : TypeToken<List<PLCMeasurement>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<PLCMeasurement>): String {
        return gson.toJson(list)
    }
}