package com.example.weatherapp.ui.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherForeCastResult
import com.example.weatherapp.ui.presentation.viewModel.WeatherForecastVM
import com.example.weatherapp.ui.presentation.viewModel.WeatherHomeUiEvent
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherHomeScreen(
    modifier: Modifier = Modifier,
    navigateToDetailsScreen: (WeatherForeCastResult) -> Unit,
) {
    val weatherForecastVM = hiltViewModel<WeatherForecastVM>()
    val state = weatherForecastVM.weatherForeCastState.collectAsState().value

    var searchQuery by remember {
        mutableStateOf("")
    }

    if (state.isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xff201D1D)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xff201D1D))
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // the search field: search for a locality
            LocationSearchField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(54.dp),
                onDone = {
                    weatherForecastVM.onEvent(WeatherHomeUiEvent.SearchIconPressed(searchQuery))
                }
            )

            // the location card (Weather info card)
            Spacer(modifier = Modifier.height(24.dp))
            WeatherInfoCard(
                onWeatherCardClicked = { navigateToDetailsScreen(state.data!!) },
                dateFromApi = state.data!!.data[0].datetime,
                weatherDesc = state.data.data[0].weather.description,
                temperature = state.data.data[0].temp.toString(),
                location = state.data.cityName,
                weatherCode = state.data.data[0].weather.code,
            )

            // hourly weather heading
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Daily",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            // daily weather card
            Spacer(modifier = Modifier.height(16.dp))
            DailyWeatherCardSummary(
                tomorrowPrediction = state.data.data[1].weather.description,
            )

            // the daily weather
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                state.data.data.forEachIndexed { index, data ->
                    if (index != 0) {
                        DailyWeatherCard(
                            weatherDesc = data.weather.description,
                            temperature = data.temp.toString(),
                            dateFromApi = data.datetime,
                            weatherCode = data.weather.code
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun LocationSearchField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    onDone: (KeyboardActionScope.() -> Unit)?,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        trailingIcon = {
            Icon(Icons.Default.Search, contentDescription = "search icon", tint = Color.LightGray)
        },
        shape = RoundedCornerShape(15.dp),
        placeholder = {
            Text(text = "Search", color = Color.LightGray)
        },
        colors = TextFieldDefaults.colors(
            cursorColor = Color.LightGray,
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            focusedContainerColor = Color(0xff242222),
            unfocusedContainerColor = Color(0xff242222),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        keyboardActions = KeyboardActions(
            onDone = onDone
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun WeatherInfoCard(
    modifier: Modifier = Modifier,
    onWeatherCardClicked: () -> Unit,
    dateFromApi: String,
    temperature: String,
    weatherDesc: String,
    location: String,
    weatherCode: Int,
) {
    val gradientColors = listOf(Color(0xff4F7FFA), Color(0xff335FD1))

    // get the current time and date
    val currentTime = getFormattedTime()
    val date = getFormattedDate(dateFromApi)

    // get the corresponding weatherCode
    val currentWeatherImage = getImage(weatherCode)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(193.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors
                )
            )
            .clickable { onWeatherCardClicked() }
            .padding(24.dp)
    ) {
        // the date and day of the weather
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = date, fontSize = 14.sp, color = Color.White)
                Text(text = currentTime, fontSize = 14.sp, color = Color.White)
            }

            // the weather details
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // the image
                Image(
                    painter = painterResource(id = currentWeatherImage),
                    contentDescription = weatherDesc,
                    modifier = Modifier.size(height = 80.dp, width = 80.dp)
                )
                // the column for the weather
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = location.uppercase(),
                        fontSize = 30.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$temperature°C",
                        fontSize = 22.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = weatherDesc,
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyWeatherCardSummary(
    tomorrowPrediction: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xffE7755C))
            .padding(12.dp)
    ) {
        // the parent row
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Rounded.Info,
                contentDescription = "Info",
                tint = Color.White,
                modifier = Modifier
                    .size(height = 32.dp, width = 32.dp)
                    .padding(end = 12.dp)
            )

            // the column for the information
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tomorrow's weather is likely to be $tomorrowPrediction",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                if (tomorrowPrediction == "Thunderstorm with heavy rain") {
                    Text(
                        text = "Don't forget to bring an umbrella",
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }

            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyWeatherCard(
    modifier: Modifier = Modifier,
    weatherDesc: String,
    temperature: String,
    dateFromApi: String,
    weatherCode: Int,
) {
    val date = getFormattedDate(dateFromApi)
    val currentWeatherIcon = getImage(weatherCode)

    Box(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(86.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xffD2DFFF))
            .padding(16.dp)
    ) {
        // the parent row
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = currentWeatherIcon),
                contentDescription = weatherDesc,
                modifier = Modifier.size(height = 40.dp, width = 40.dp)
            )

            // the column for the information
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = date,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = weatherDesc,
                    fontSize = 13.sp,
                )
            }

            // the text
            Text(text = "$temperature°C", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedTime(): String {
    // Get the current time
    val currentTime = LocalTime.now()
    // Define a formatter for the 12-hour format with AM/PM
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    // Format the current time to a 12-hour format
    return currentTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedDate(dateString: String): String {
    // Parse the string to LocalDate
    val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
    // Define a formatter for the desired output format
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
    // Format the date to a human-readable string
    return date.format(formatter)
}


fun getImage(weatherCode: Int): Int {
    when (weatherCode) {
        in 200 until 203 -> {
            return R.drawable.t01d
        }

        in 230 until 233 -> {
            return R.drawable.t04d
        }

        in 300 until 302 -> {
            return R.drawable.d01d
        }

        in 500 until 501 -> {
            return R.drawable.r01d
        }

        502 -> {
            return R.drawable.r03d
        }

        in 511 until 520 -> {
            return R.drawable.f01d
        }

        521 -> {
            return R.drawable.r05d
        }

        522 -> {
            return R.drawable.f01d
        }

        in 600 until 623 -> {
            return R.drawable.s01d
        }

        in 700 until 750 -> {
            return R.drawable.a01d
        }

        800 -> {
            return R.drawable.c01d
        }

        in 801 until 802 -> {
            return R.drawable.c02d
        }

        else -> {
            return R.drawable.c04d
        }
    }
}