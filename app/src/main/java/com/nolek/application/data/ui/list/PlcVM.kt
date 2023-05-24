package com.nolek.application.data.ui.list

import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nolek.application.data.model.PLCBundle
import com.nolek.application.data.repository.PLCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class plcVM @Inject constructor(private val plc: PLCRepository) : ViewModel() {
    private var _data = MutableLiveData<List<PLCBundle>>()
    var data: LiveData<List<PLCBundle>> = _data

    fun get() {
        viewModelScope.launch {
            val data = plc.getData(1000)
            d("plcVM", "data recieved from repo ${data}")
            _data.postValue(data)
        }
    }
}

