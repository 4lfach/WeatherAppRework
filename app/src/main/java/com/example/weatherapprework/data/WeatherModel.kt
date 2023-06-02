package com.example.weatherapprework.data

data class WeatherModel(
    val city: String, val time: String,
    val currentTemp: String, val weatherState: String, val iconLink: String,
    val maxTemp: String, val minTemp: String,
    val hours: String
) {
}