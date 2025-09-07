package rocks.drnd.whereisivan.client.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00E5FF), // Neon Cyan
    secondary = Color(0xFF00C853), // Neon Green
    tertiary = Color(0xFFFF4081), // Neon Pink
    background = Color(0xFF121212), // Dark Background
    surface = Color(0xFF1E1E1E), // Slightly lighter dark surface
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFFE0E0E0), // Light text on dark background
    onSurface = Color(0xFFE0E0E0) // Light text on dark surface
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EA), // Deep Purple
    secondary = Color(0xFF03DAC6), // Teal
    tertiary = Color(0xFFFF0266), // Bright Pink
    background = Color(0xFFFFFFFF), // White Background
    surface = Color(0xFFF5F5F5), // Light Grey Surface
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.Black, // Dark text on light background
    onSurface = Color.Black // Dark text on light surface
)

@Composable
fun WhereIsIvanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}