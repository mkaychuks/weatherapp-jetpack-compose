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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherForeCastResult
import kotlinx.coroutines.delay

data class Description(
    val icon: Int,
    val description: String,
    val value: Any,
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDetailScreen(
    modifier: Modifier = Modifier,
    onNavigationBackButtonPressed: () -> Unit,
    weatherForeCastResult: WeatherForeCastResult,
) {

    val description: List<Description> = listOf(
        Description(
            icon = R.drawable.blaze_line,
            description = "Relative Humidity",
            value = "${weatherForeCastResult.data[0].rh}%"
        ),
        Description(
            icon = R.drawable.haze_2_line,
            description = "Air Pressure",
            value = weatherForeCastResult.data[0].pres
        ),
        Description(
            icon = R.drawable.windy_line,
            description = "Wind Velocity",
            value = "${weatherForeCastResult.data[0].wind_spd}m/s"
        ),
        Description(
            icon = R.drawable.mist_line,
            description = "Fog",
            value = "${weatherForeCastResult.data[0].clouds}%"
        ),
    )


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xff201D1D))
    ) {
        // the back button and the location
        WeatherDetailsCard(
            onNavigationBackButtonPressed = onNavigationBackButtonPressed,
            location = weatherForeCastResult.city_name,
            country = weatherForeCastResult.country_code,
            weatherCode = weatherForeCastResult.data[0].weather.code,
            dateFromApi = weatherForeCastResult.data[0].datetime,
            weatherDesc = weatherForeCastResult.data[0].weather.description,
            temperature = weatherForeCastResult.data[0].temp.toString()
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            // the location card (Weather info card)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Information Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            // the daily weather
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(description.size) {
                    WeatherUnitDetails(
                        icon = description[it].icon,
                        value = description[it].value,
                        description = description[it].description
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun WeatherDetailsCard(
    modifier: Modifier = Modifier,
    onNavigationBackButtonPressed: () -> Unit,
    location: String,
    country: String,
    dateFromApi: String,
    weatherCode: Int,
    weatherDesc: String,
    temperature: String,
) {
    val gradientColors = listOf(Color(0xff4F7FFA), Color(0xff335FD1))
    val date = getFormattedDate(dateFromApi)
    val currentWeatherImage = getImage(weatherCode)


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors
                )
            )
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Column {

            // the parent row for the location and back arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        onNavigationBackButtonPressed()
                    }
                )

                Text(
                    text = "$location, $country",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)

            }

            // the column for the details of the weather
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = date, fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = currentWeatherImage),
                    contentDescription = weatherDesc,
                    modifier = Modifier.size(height = 64.dp, width = 64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "$temperature°C",
                    fontSize = 32.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = weatherDesc,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
private fun WeatherUnitDetails(
    modifier: Modifier = Modifier,
    icon: Int,
    description: String,
    value: Any,
) {
    Box(
        modifier = modifier
            .width(163.dp)
            .height(69.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xff242425))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = icon),
                contentDescription = null,
                Modifier.size(width = 24.dp, height = 24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = value.toString(), fontSize = 13.sp, color = Color.White)
                Text(text = description, fontSize = 13.sp, color = Color.White)
            }
        }
    }
}
