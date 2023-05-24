package com.nolek.application.data.di

import com.nolek.application.data.network.AuthenticationService
import com.nolek.application.data.network.PLCDataService
import com.nolek.application.data.repository.AuthenticationRepository
import com.nolek.application.data.repository.NolekAuthenticationRepository
import com.nolek.application.data.repository.NolekPLCMicroserviceRepository
import com.nolek.application.data.repository.PLCRepository
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://165.232.77.119"
    private const val AUTH_URL = "http://165.232.77.119:5000"
    private const val PLC_URL = "http://165.232.77.119:5002"

    @Singleton
    @Provides
    fun providePLCRepository(
        plcApi: PLCDataService,
        authRepository: AuthenticationRepository
    ): PLCRepository {
        return NolekPLCMicroserviceRepository(plcApi, authRepository)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authService: AuthenticationService): AuthenticationRepository {
        return NolekAuthenticationRepository(authService)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder()
            .add(Double::class.java, CustomJsonAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()

        return retrofit
    }

    @Provides
    @Singleton
    fun providePLCDataService(retrofit: Retrofit): PLCDataService =
        retrofit.newBuilder()
            .baseUrl(PLC_URL)
            .build()
            .create(PLCDataService::class.java)

    @Singleton
    @Provides
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService =
        retrofit.newBuilder()
            .baseUrl(AUTH_URL)
            .build()
            .create(AuthenticationService::class.java)

}
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