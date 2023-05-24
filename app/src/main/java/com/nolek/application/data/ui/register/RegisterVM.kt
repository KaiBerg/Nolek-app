package com.nolek.application.data.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nolek.application.data.network.RegisterRequest
import com.nolek.application.data.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor(private val auth: AuthenticationRepository) : ViewModel() {
    private var _registered = MutableLiveData(false)
    var registered: LiveData<Boolean> = _registered

    var email = ""
    var password = ""
    var firstName = ""
    var lastName = ""

    fun register() {
        viewModelScope.launch {
            val request =
                RegisterRequest(email, password, firstName, lastName, "${firstName}${lastName}")
            val attempt = auth.Register(request)
            _registered.postValue(attempt)
        }
    }
}