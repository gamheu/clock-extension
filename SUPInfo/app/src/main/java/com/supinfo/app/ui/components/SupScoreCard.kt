package com.supinfo.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supinfo.app.domain.SupConditions
import com.supinfo.app.domain.SupScore
import com.supinfo.app.domain.SupScoreCalculator
import com.supinfo.app.ui.theme.*

@Composable
fun SupScoreCard(conditions: SupConditions, modifier: Modifier = Modifier) {
    val scoreColor = scoreColor(conditions.supScore)
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = scoreColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = SupScoreCalculator.toEmoji(conditions.supScore),
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = conditions.supScoreLabel.uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "SUP CONDITIONS",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.85f),
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = conditions.supTip,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

private fun scoreColor(score: SupScore): Color = when (score) {
    SupScore.EXCELLENT -> ScoreExcellent
    SupScore.GOOD      -> ScoreGood
    SupScore.MODERATE  -> ScoreModerate
    SupScore.DIFFICULT -> ScoreDifficult
    SupScore.DANGEROUS -> ScoreDangerous
}
