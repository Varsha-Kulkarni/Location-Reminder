package com.udacity.project4.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map


/**
 * Created By Varsha Kulkarni on 27/01/21
 */
enum class AuthenticationState {
    AUTHENTICATED, UNAUTHENTICATED
}

class AuthenticationViewModel : ViewModel() {

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else
            AuthenticationState.UNAUTHENTICATED
    }
}