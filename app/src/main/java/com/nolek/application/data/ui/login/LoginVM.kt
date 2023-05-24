package com.nolek.application.data.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nolek.application.data.network.LoginRequest
import com.nolek.application.data.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(private val auth: AuthenticationRepository) : ViewModel() {
    private var _loggedIn = MutableLiveData(auth.getUserInfo() != null)
    var loggedIn: LiveData<Boolean> = _loggedIn

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            val request = LoginRequest(email, password)
            val attempt = auth.Login(request)
            _loggedIn.postValue(attempt)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            if(loggedIn.value == true) {
                val attempt = auth.LogOut()
                // logout attempt is the opposite of loginattempt, therefore the result needs to be flipped
                _loggedIn.postValue(!attempt)
            }
        }
    }
}