package com.supinfo.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supinfo.app.domain.SupConditions

@Composable
fun WeatherCard(conditions: SupConditions, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle("Weather")
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = conditions.weatherEmoji, fontSize = 48.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = conditions.weatherDescription,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "${conditions.temperature.toInt()}°C  •  Feels ${conditions.apparentTemperature.toInt()}°C",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Precipitation",
                    value = "${conditions.precipitationProbability}",
                    unit = "%"
                )
                StatItem(
                    label = "UV Index",
                    value = "${conditions.uvIndex.toInt()}",
                    unit = conditions.uvRating
                )
            }
        }
    }
}
