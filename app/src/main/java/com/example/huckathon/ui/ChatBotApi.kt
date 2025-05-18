package com.example.huckathon.ui
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatBotApi {
    @POST("/getResponse") // ← endpoint yolu tam buysa
    fun sendMessage(@Body message: MessageRequest): Call<ChatBotResponse>
}