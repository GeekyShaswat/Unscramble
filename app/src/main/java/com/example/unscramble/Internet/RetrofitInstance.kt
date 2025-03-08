package com.example.unscramble.Internet
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitInstance {
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/GeekyShaswat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}