package com.example.huckathon.ui
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ChatBotApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.101.12.18:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatBotApi::class.java)
    }
}
