package com.example.turnierplaner.googlesignin.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnierplaner.googlesignin.util.LoadingState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch



class LoginScreenViewModel : ViewModel() {

  val loadingState = MutableStateFlow(LoadingState.IDLE)

  fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
    try {
      loadingState.emit(LoadingState.LOADING)
      Firebase.auth.signInWithEmailAndPassword(email, password)
      loadingState.emit(LoadingState.LOADED)
    } catch (e: Exception) {
      loadingState.emit(LoadingState.error(e.localizedMessage))
    }
  }

  fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
    try {
      loadingState.emit(LoadingState.LOADING)
      Firebase.auth.signInWithCredential(credential)
      loadingState.emit(LoadingState.LOADED)
    } catch (e: Exception) {
      loadingState.emit(LoadingState.error(e.localizedMessage))
    }
  }
}