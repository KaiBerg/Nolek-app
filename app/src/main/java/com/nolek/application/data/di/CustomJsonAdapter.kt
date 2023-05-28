package com.nolek.application.data.di

import com.squareup.moshi.*

class CustomJsonAdapter : JsonAdapter<Double>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Double? {
        if (reader.peek() != JsonReader.Token.STRING) {
            return reader.nextDouble()
        }
        val value = reader.nextString()
        return value.replace(",", ".").toDoubleOrNull()
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Double?) {
        writer.value(value)
    }
}