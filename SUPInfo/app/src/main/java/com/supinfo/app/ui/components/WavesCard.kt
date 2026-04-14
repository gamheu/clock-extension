package com.supinfo.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.supinfo.app.domain.SupConditions

@Composable
fun WavesCard(conditions: SupConditions, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle("Waves 🌊")
            Spacer(modifier = Modifier.height(12.dp))

            if (conditions.waveHeight != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        label = "Height",
                        value = String.format("%.1f", conditions.waveHeight),
                        unit = "m"
                    )
                    StatItem(
                        label = "Direction",
                        value = conditions.waveDirectionText ?: "—",
                        unit = ""
                    )
                    StatItem(
                        label = "Period",
                        value = conditions.wavePeriod?.let { String.format("%.1f", it) } ?: "—",
                        unit = "s"
                    )
                    StatItem(
                        label = "Swell",
                        value = conditions.swellWaveHeight?.let { String.format("%.1f", it) } ?: "—",
                        unit = "m"
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = waveHeightComment(conditions.waveHeight),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                Text(
                    text = "No marine data for this location (inland or unsupported area).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

private fun waveHeightComment(height: Double): String = when {
    height < 0.3 -> "Flat water — ideal for beginners and flat-water touring"
    height < 0.6 -> "Small waves — easy conditions"
    height < 1.0 -> "Moderate waves — intermediate level"
    height < 1.5 -> "Large waves — experienced paddlers only"
    else         -> "Very large waves — experts and surf SUP only"
}
