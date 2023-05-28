package com.nolek.application.data.di

import android.content.Context
import com.nolek.application.data.network.AuthenticationService
import com.nolek.application.data.network.PLCDataService
import com.nolek.application.data.network.room.dao.PlcDao
import com.nolek.application.data.network.room.dao.SuggestionDao
import com.nolek.application.data.network.room.db.AppDatabase
import com.nolek.application.data.repository.*
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        authRepository: AuthenticationRepository,
        plc: PlcDao
    ): PLCRepository {
        return NolekPLCMicroserviceRepository(plcApi, authRepository, plc)
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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    @Provides
    @Singleton
    fun provideSuggestionDao(appDB: AppDatabase): SuggestionDao {
        return appDB.suggestion()
    }

    @Singleton
    @Provides
    fun provideSuggestionRepository(dao: SuggestionDao): SuggestionRepository {
        return SQLiteSuggestionRepository(dao)
    }

    @Provides
    @Singleton
    fun providePlcDao(appDB: AppDatabase): PlcDao {
        return appDB.plc()
    }


}
