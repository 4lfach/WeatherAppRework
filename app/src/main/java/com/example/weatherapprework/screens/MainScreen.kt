package com.example.weatherapprework.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapprework.R
import com.example.weatherapprework.data.WeatherApi.getWeatherByHours
import com.example.weatherapprework.data.WeatherModel
import com.example.weatherapprework.ui.theme.BlueLight
import kotlinx.coroutines.launch

@Composable
fun MainCard(currentWeather: MutableState<WeatherModel>, onClickSync: () -> Unit, onClickSearch: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = BlueLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CreateFirstRow(currentWeather)
                //5 text elements in column that will go from up to down
                Text(
                    text = currentWeather.value.city,
                    style = TextStyle(fontSize = 22.sp),
                    color = Color.White
                )
                Text(
                    text = if (currentWeather.value.currentTemp.isNotEmpty()) "${
                        currentWeather.value.currentTemp.toFloat().toInt()
                    } °C" else "${
                        currentWeather.value.maxTemp.toFloat().toInt()
                    }/${currentWeather.value.minTemp.toFloat().toInt()}°C",
                    style = TextStyle(fontSize = 64.sp),
                    color = Color.White
                )
                Text(
                    text = currentWeather.value.weatherState,
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )
                Text(
                    text = "${
                        currentWeather.value.maxTemp.toFloat().toInt()
                    }/${currentWeather.value.minTemp.toFloat().toInt()}°C",
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.White
                )

                CreateLastRow(onClickSync, onClickSearch)
            }
        }

    }
}

@Composable
private fun CreateFirstRow(currentWeather: MutableState<WeatherModel>) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(top = 10.dp, start = 10.dp),
            text = currentWeather.value.time,
            style = TextStyle(fontSize = 15.sp),
            color = Color.White
        )

        AsyncImage(
            model = currentWeather.value.iconLink,
            contentDescription = null,
            modifier = Modifier
                .size(42.dp)
                .padding(end = 10.dp),
            alignment = Alignment.CenterEnd
        )
    }
}

@Composable
private fun CreateLastRow(onClickSync: () -> Unit, onClickSearch: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onClickSearch.invoke() }) {
            Icon(
                painter = painterResource(R.drawable.ic_search_24),
                contentDescription = null,
                tint = Color.White
            )
        }
        IconButton(onClick = { onClickSync.invoke() }) {
            Icon(
                painter = painterResource(R.drawable.ic_refresh_24),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(daysList: MutableState<List<WeatherModel>>, currentDay: MutableState<WeatherModel>) {
    val tabList = listOf("Hours", "Days")

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = BlueLight,
            contentColor = Color.White
        ) {
            tabList.forEachIndexed { index, s ->
                Tab(selected = false, text = { Text(text = s) }, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })

            }
        }
        HorizontalPager(
            state = pagerState, pageCount = tabList.size, modifier = Modifier.weight(1.0f)
        ) { index: Int ->
            val list = when (index) {
                0 -> {
                    getWeatherByHours(currentDay.value.hours)
                }

                1 -> {
                    daysList.value
                }

                else -> {
                    daysList.value
                }
            }

            MainList(list = list, currentDay = currentDay)
        }
    }

}
