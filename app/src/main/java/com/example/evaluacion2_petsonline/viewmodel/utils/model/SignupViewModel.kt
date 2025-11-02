package com.example.evaluacion2_petsonline.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class SignupViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)

    private val _ui = MutableStateFlow(SignupUiState())
    val ui: StateFlow<SignupUiState> = _ui

    fun onEmail(value: String) = _ui.value.run { _ui.value = copy(email = value) }
    fun onPassword(value: String) = _ui.value.run { _ui.value = copy(password = value) }
    fun onConfirmPassword(value: String) = _ui.value.run { _ui.value = copy(confirmPassword = value) }

    fun signup() {
        val s = _ui.value
        when {
            s.email.isBlank() || s.password.isBlank() || s.confirmPassword.isBlank() ->
                _ui.value = s.copy(error = "Todos los campos son obligatorios")
            s.password.length < 8 ->
                _ui.value = s.copy(error = "La contraseña debe tener al menos 8 caracteres")
            s.password != s.confirmPassword ->
                _ui.value = s.copy(error = "Las contraseñas no coinciden")
            else -> viewModelScope.launch {
                _ui.value = s.copy(isLoading = true, error = null)
                val result = repository.signup(s.email, s.password)
                _ui.value = result.fold(
                    onSuccess = { s.copy(isLoading = false, success = true) },
                    onFailure = { s.copy(isLoading = false, error = it.message) }
                )
            }
        }
    }
}
