package com.nolek.application.data.model

data class PLCMeasurement(
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

data class PLCBundle(
    val index : String,
    val dataPoints : List<PLCMeasurement>
)