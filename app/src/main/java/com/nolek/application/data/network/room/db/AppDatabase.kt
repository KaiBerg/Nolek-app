package com.nolek.application.data.network.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nolek.application.data.model.PLCBundle
import com.nolek.application.data.model.Suggestion
import com.nolek.application.data.network.room.dao.PlcDao
import com.nolek.application.data.network.room.dao.SuggestionDao

@Database(entities = [PLCBundle::class, Suggestion::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plc(): PlcDao
    abstract fun suggestion(): SuggestionDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database",
                ).build().also { instance = it }
            }
        }
    }
}