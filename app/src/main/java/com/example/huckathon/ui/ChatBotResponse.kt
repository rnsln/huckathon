package com.example.huckathon.ui

data class ChatBotResponse(
    val response: String
)
data class MessageRequest(
    val question: String
)
data class EnergyZone(
    val X: Float,
    val Y: Float,
    val Radius: Float
)
data class SleepData(
    val hours: Float
)