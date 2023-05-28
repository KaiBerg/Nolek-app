package com.nolek.application.data.repository

import com.nolek.application.data.model.Suggestion
import com.nolek.application.data.network.room.dao.SuggestionDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
interface SuggestionRepository {
    suspend fun getSuggestions(): List<String>
    suspend fun add(suggestion: String)
}

class SQLiteSuggestionRepository @Inject constructor(private val suggestionDao: SuggestionDao) :
    SuggestionRepository {
    override suspend fun getSuggestions(): List<String> {
        return suggestionDao.getRecentSuggestions()
    }

    override suspend fun add(suggestion: String) {
        if (suggestion.isNotEmpty()) {
            suggestionDao.insertSuggestion(
                Suggestion(
                    0,
                    suggestion.trim().filter { it.isLetterOrDigit() })
            )
        }
    }

}