package com.nolek.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nolek.application.data.model.PLCBundle

class paneVM : ViewModel() {

    private var _selected = MutableLiveData<PLCBundle>()
    var selected: LiveData<PLCBundle> = _selected

    fun sel(item : PLCBundle) {
        _selected.postValue(item)
    }

    fun getArray(propertyName: String): Array<Any> {
        return selected.value!!.dataPoints.map { measurement ->
            when (propertyName) {
                "LogTime" -> measurement.LogTime
                "StepNo" -> measurement.StepNo
                "CircuitName" -> measurement.CircuitName
                "TMP1" -> measurement.TMP1
                "TMP2" -> measurement.TMP2
                "B31" -> measurement.B31
                "B32" -> measurement.B32
                "B21" -> measurement.B21
                "B22" -> measurement.B22
                "P101" -> measurement.P101
                "RegulatorSP" -> measurement.RegulatorSP
                "RegulatorFB" -> measurement.RegulatorFB
                else -> throw IllegalArgumentException("Invalid property name: $propertyName")
            }
        }.toTypedArray()
    }

}