package com.supinfo.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.supinfo.app.domain.SupConditions

@Composable
fun UvSunCard(conditions: SupConditions, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle("UV & Daylight")
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(label = "UV now",  value = String.format("%.1f", conditions.uvIndex),    unit = conditions.uvRating)
                StatItem(label = "UV max",  value = String.format("%.1f", conditions.uvIndexMax), unit = "today")
                StatItem(label = "Sunrise", value = conditions.sunrise, unit = "")
                StatItem(label = "Sunset",  value = conditions.sunset,  unit = "")
            }
            if (conditions.uvIndexMax >= 6) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "☀️ High UV — wear SPF 50+ and a rash guard.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
