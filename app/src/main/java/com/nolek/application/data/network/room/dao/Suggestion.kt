package com.nolek.application.data.network.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nolek.application.data.model.Suggestion

@Dao
interface SuggestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: Suggestion)

    @Query("SELECT DISTINCT text FROM suggestions ORDER BY id DESC LIMIT 10")
    suspend fun getRecentSuggestions(): List<String>
}