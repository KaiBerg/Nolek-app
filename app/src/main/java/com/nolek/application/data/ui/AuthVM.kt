package com.nolek.application.data.ui

import androidx.lifecycle.ViewModel
import com.nolek.application.data.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(val auth: AuthenticationRepository) : ViewModel() {
    private var _id : String = ""
    private var _email : String = ""
    var name = ""
        get() {
            val atSign = _email.indexOf("@")
            return _email.substring(0,atSign)
        }
    var id = ""
        get() {
            return _id
        }
    var email = ""
        get() {
            return _email
        }

    fun getInfo() {
        val info = auth.getUserInfo()
        if (info != null) {
            _id = info.userId
            _email = info.email
        }
    }

}