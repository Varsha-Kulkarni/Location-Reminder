package com.udacity.project4.authentication

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


/**
 * Created By Varsha Kulkarni on 27/01/21
 */

class FirebaseUserLiveData : LiveData<FirebaseUser?>() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener { authState ->
        value = authState.currentUser
    }

    override fun onActive() {
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }
}