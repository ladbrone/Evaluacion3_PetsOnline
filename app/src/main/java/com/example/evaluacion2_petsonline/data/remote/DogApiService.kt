package com.example.evaluacion2_petsonline.data.remote

import com.example.evaluacion2_petsonline.domain.model.DogResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DogApi {
    @GET("breeds/image/random")
    suspend fun getRandomDog(): DogResponse
}

// Objeto Singleton para esta API específica
object DogRetrofitClient {
    private const val BASE_URL = "https://dog.ceo/api/"

    val api: DogApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApi::class.java)
    }
}