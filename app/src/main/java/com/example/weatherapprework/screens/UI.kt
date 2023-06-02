package com.example.weatherapprework.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapprework.data.WeatherModel
import com.example.weatherapprework.ui.theme.BlueLight
import com.example.weatherapprework.ui.theme.DeepBlue

@Composable
fun MainList(list: List<WeatherModel>, currentDay: MutableState<WeatherModel>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(list.size) {
            ForecastWeatherCard(list[it], currentDay)
        }
    }
}

@Composable
fun ForecastWeatherCard(model: WeatherModel, currentDay: MutableState<WeatherModel>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
            .clickable {
                if (model.hours.isEmpty()) return@clickable
                currentDay.value = model
            },
        colors = CardDefaults.cardColors(containerColor = BlueLight),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Column() {
                Text(model.time, style = TextStyle(fontSize = 16.sp, color = DeepBlue))
                Text(model.weatherState, style = TextStyle(fontSize = 16.sp, color = Color.White))
            }

            Text(
                model.currentTemp.ifEmpty {
                    "${
                        model.maxTemp.toFloat().toInt()
                    }/${model.minTemp.toFloat().toInt()}°C"
                },
                style = TextStyle(fontSize = 20.sp, color = Color.White)
            )
            AsyncImage(
                model = "https:${model.iconLink}",
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .padding(end = 10.dp)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSearch(dialogState: MutableState<Boolean>, onSubmit: (String) -> Unit){
    val dialogText = remember{
        mutableStateOf("")
    }

    AlertDialog(onDismissRequest = {
        dialogState.value = false
    }, confirmButton = {
        TextButton(onClick = { dialogState.value = false }) {
            Text("ok")
            onSubmit(dialogText.value)
        }
    }, dismissButton = {
        TextButton(onClick = {dialogState.value = false }) {
            Text(text = "cancel")
        }
    }, title = {
        Column(modifier = Modifier.fillMaxWidth()){
            Text("Enter the city name")
            TextField(value = dialogText.value, onValueChange = {
                dialogText.value = it
            })
        }
    })
}