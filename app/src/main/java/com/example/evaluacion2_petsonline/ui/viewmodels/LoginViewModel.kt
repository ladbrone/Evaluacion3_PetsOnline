package com.example.evaluacion2_petsonline.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.evaluacion2_petsonline.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(
    application: Application,
    private val repository: AuthRepository = AuthRepository(application)
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$".toRegex()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun login() {
        val state = _uiState.value

        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Todos los campos son obligatorios") }
            return
        }

        if (!isValidEmail(state.email)) {
            _uiState.update { it.copy(error = "Correo electrónico inválido") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = repository.login(state.email, state.password)

            result.onSuccess { response ->
                println("✅ Login Exitoso en Render!")
                println("📩 Token recibido: ${response.data?.accessToken}")

                _uiState.update { it.copy(isLoading = false, success = true) }
            }.onFailure { error ->
                println("❌ Error en Login: ${error.message}")

                _uiState.update {
                    it.copy(isLoading = false, error = error.message ?: "Error desconocido")
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }

    fun resetSuccess() {
        _uiState.update { it.copy(success = false) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return LoginViewModel(
                    application = application,
                    repository = AuthRepository(application)
                ) as T
            }
        }
    }
}