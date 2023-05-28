package com.nolek.application.data.ui.list

import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nolek.application.data.model.PLCBundle
import com.nolek.application.data.repository.PLCRepository
import com.nolek.application.data.repository.SuggestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class plcVM @Inject constructor(
    private val plc: PLCRepository,
    private val sug: SuggestionRepository
) : ViewModel() {
    private var fullList = listOf<PLCBundle>()
    private var _data = MutableLiveData<List<PLCBundle>>()
    var data: LiveData<List<PLCBundle>> = _data
    private var _search = MutableLiveData("")
    val search: LiveData<String>
        get() = _search

    private var _suggestions = MutableLiveData<List<String>>()
    var suggestions: LiveData<List<String>> = _suggestions
    fun get() {
        viewModelScope.launch {
            plc.getData(1000).collect {
                d("plcVM", "data recieved from repo ${data}")
                fullList = it
                _data.postValue(fullList.filter { plcB ->
                    plcB.index.contains(
                        _search.value!!,
                        true
                    )
                })
            }
        }
        viewModelScope.launch {
            _suggestions.postValue(sug.getSuggestions())
        }
    }

    fun search(query: String) {
        _search.value = query
        _data.postValue(fullList.filter { plcB -> plcB.index.contains(query, true) })
        viewModelScope.launch {
            sug.add(query)
        }
    }
}

