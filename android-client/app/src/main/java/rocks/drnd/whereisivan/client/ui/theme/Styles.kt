package rocks.drnd.whereisivan.client.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight


fun getLabelTextStyle(typography: Typography) = TextStyle(
    fontSize = typography.titleMedium.fontSize,
    fontWeight = FontWeight.Bold,
    color = Color.Gray
)

@Composable
fun getTextStyle(color: Color = Color.Black) = TextStyle(
    fontSize = MaterialTheme.typography.titleMedium.fontSize,
    fontWeight = FontWeight.Bold,
    color = color
)
