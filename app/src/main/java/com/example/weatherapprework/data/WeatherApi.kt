package com.example.weatherapprework.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapprework.data.Constants.API_KEY
import org.json.JSONArray
import org.json.JSONObject

object WeatherApi {
    fun getData(city: String,
        context: Context,
        daysList: MutableState<List<WeatherModel>>,
        currentDay: MutableState<WeatherModel>
    ) {
        val url =
            "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY&q=$city&days=5&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(Request.Method.GET, url, { response ->
            daysList.value = (getWeatherByDays(response))
            currentDay.value = (daysList.value[0])
        }, {
            Log.d("MyLog", "Volley error: $it")
        })

        queue.add(request)
    }

    private fun getWeatherByDays(response: String): List<WeatherModel> {
        if (response.isEmpty()) return emptyList()

        val mainObject = JSONObject(response)
        val city = mainObject.getJSONObject("location").getString("name")
        val forecastDays = mainObject.getJSONObject("forecast").getJSONArray("forecastday")

        return (0 until forecastDays.length()).map { i ->
            val item = forecastDays.getJSONObject(i)
            var time = item.getString("date")

            val day = item.getJSONObject("day")
            val maxTemp = day.getString("maxtemp_c")
            val minTemp = day.getString("mintemp_c")

            val condition = day.getJSONObject("condition")
            val iconLink = condition.getString("icon")
            val state = condition.getString("text")

            val hours = item.getJSONArray("hour").toString()

            WeatherModel(city, time, "", state, iconLink, maxTemp, minTemp, hours)
            if (i == 0) {
                val current = mainObject.getJSONObject("current")
                time = current.getString("last_updated")
                val currentTemp = current.getString("temp_c")

                WeatherModel(city, time, currentTemp, state, iconLink, maxTemp, minTemp, hours)
            } else {
                WeatherModel(city, time, "", state, iconLink, maxTemp, minTemp, hours)
            }
        }
    }

    fun getWeatherByHours(hours: String): List<WeatherModel> {
        if (hours.isEmpty()) return emptyList()

        val hoursArray = JSONArray(hours)
        val list: ArrayList<WeatherModel> = arrayListOf()

        for (i in 0 until hoursArray.length()) {
            val item = hoursArray[i] as JSONObject
            list.add(
                WeatherModel(
                    "",
                    item.getString("time"),
                    item.getString("temp_c").toFloat().toInt().toString() + "°C",
                    item.getJSONObject("condition").getString("text"),
                    item.getJSONObject("condition").getString("icon"),
                    "",
                    "",
                    ""
                )
            )
        }
        return list
    }
}