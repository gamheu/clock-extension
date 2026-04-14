package com.supinfo.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supinfo.app.domain.SupConditions

@Composable
fun WindCard(conditions: SupConditions, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle("Wind")
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Direction arrow + label
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Wind direction",
                        // Navigation arrow points North (up) at 0° — rotate to match wind direction
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(conditions.windDirection.toFloat()),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = conditions.windDirectionText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Speed
                StatItem(
                    label = "Speed",
                    value = "${conditions.windSpeed.toInt()}",
                    unit = "km/h"
                )
                // Gusts
                StatItem(
                    label = "Gusts",
                    value = "${conditions.windGusts.toInt()}",
                    unit = "km/h"
                )
                // Daily max
                StatItem(
                    label = "Max today",
                    value = "${conditions.maxWindSpeed.toInt()}",
                    unit = "km/h"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = beaufortLabel(conditions.windSpeed),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

private fun beaufortLabel(kmh: Double): String {
    val bf = when {
        kmh < 2   -> 0
        kmh < 6   -> 1
        kmh < 12  -> 2
        kmh < 20  -> 3
        kmh < 29  -> 4
        kmh < 39  -> 5
        kmh < 50  -> 6
        kmh < 62  -> 7
        kmh < 75  -> 8
        kmh < 89  -> 9
        kmh < 103 -> 10
        kmh < 118 -> 11
        else -> 12
    }
    val desc = listOf(
        "Calm", "Light air", "Light breeze", "Gentle breeze", "Moderate breeze",
        "Fresh breeze", "Strong breeze", "Near gale", "Gale",
        "Strong gale", "Storm", "Violent storm", "Hurricane"
    )
    return "Beaufort $bf — ${desc[bf]}"
}
