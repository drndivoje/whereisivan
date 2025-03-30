package rocks.drnd.whereisivan.client.ui.screen

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight


fun getLabelTextStyle(typography: Typography) = TextStyle(
    fontSize = typography.titleMedium.fontSize,
    fontWeight = FontWeight.Bold,
    color = Color.Gray
)

fun getMetricTextStyle(typography: Typography) = TextStyle(
    fontSize = typography.titleMedium.fontSize,
    fontWeight = FontWeight.Bold,
    color = Color.Black
)