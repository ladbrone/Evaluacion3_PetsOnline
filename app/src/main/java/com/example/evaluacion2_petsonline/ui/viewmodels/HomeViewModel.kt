package com.example.evaluacion2_petsonline.ui.viewmodels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.data.remote.DogRetrofitClient
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var dogImageState by mutableStateOf<String?>(null)
        private set

    init {
        loadRandomDog()
    }

    fun loadRandomDog() {
        viewModelScope.launch {
            try {
                val response = DogRetrofitClient.api.getRandomDog()
                if (response.status == "success") {
                    dogImageState = response.message
                }
            } catch (e: Exception) {
                println("Error cargando perrito: ${e.message}")
            }
        }
    }
}