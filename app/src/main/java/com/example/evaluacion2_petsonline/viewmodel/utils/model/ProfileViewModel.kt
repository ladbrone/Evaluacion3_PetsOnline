package com.example.evaluacion2_petsonline.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.repository.AuthRepository
import com.example.evaluacion2_petsonline.data.local.repository.AvatarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Estado de la interfaz de usuario del perfil
data class ProfileUiState(
    val isLoading: Boolean = false,
    val email: String? = null,
    val error: String? = null,
    val avatarUri: String? = null // 🔹 agregamos el campo para la imagen
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)
    private val avatarRepo = AvatarRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
        observeAvatar() // 👀 Escucha cambios del avatar guardado
    }

    // 🔹 Carga datos del perfil (desde backend)
    private fun loadProfile() {
        viewModelScope.launch {
            val result = repository.getProfile()
            _uiState.value = result.fold(
                onSuccess = { ProfileUiState(isLoading = false, email = it) },
                onFailure = { ProfileUiState(isLoading = false, error = it.message) }
            )
        }
    }

    // 🔹 Escucha cambios del avatar en DataStore
    private fun observeAvatar() {
        viewModelScope.launch {
            avatarRepo.getAvatar().collectLatest { uri ->
                _uiState.value = _uiState.value.copy(avatarUri = uri)
            }
        }
    }

    // 🔹 Guarda avatar seleccionado
    fun saveAvatar(uri: String) {
        viewModelScope.launch {
            avatarRepo.saveAvatar(uri)
            _uiState.value = _uiState.value.copy(avatarUri = uri)
        }
    }

    // 🔹 Limpia el avatar (opcional)
    fun clearAvatar() {
        viewModelScope.launch {
            avatarRepo.clearAvatar()
            _uiState.value = _uiState.value.copy(avatarUri = null)
        }
    }
}
