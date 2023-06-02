package com.example.weatherapprework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.weatherapprework.data.WeatherApi
import com.example.weatherapprework.data.WeatherModel
import com.example.weatherapprework.screens.DialogSearch
import com.example.weatherapprework.screens.MainCard
import com.example.weatherapprework.screens.TabLayout
import com.example.weatherapprework.ui.theme.WeatherAppReworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppReworkTheme {
                val daysList = remember{
                    mutableStateOf(listOf<WeatherModel>())
                }
                val currentDay = remember{
                    mutableStateOf(WeatherModel("","","10.0","","","10.0","10.0","",))
                }
                val dialogState = remember{
                    mutableStateOf(false)
                }
                if(dialogState.value) DialogSearch(dialogState, onSubmit = {
                    WeatherApi.getData(it,this@MainActivity, daysList, currentDay)
                })

                WeatherApi.getData("Astana", this, daysList, currentDay)

                Image(
                    painter = painterResource(id = R.drawable.app_background),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(1.0f),
                    contentDescription = "appBackground"
                )
                Column(modifier = Modifier.fillMaxSize()){
                    MainCard(currentDay, onClickSync = {
                        WeatherApi.getData("Astana",this@MainActivity, daysList, currentDay)
                    }, onClickSearch = {
                        dialogState.value = true
                    })
                    TabLayout(daysList, currentDay)
                }
            }
        }
    }
}
