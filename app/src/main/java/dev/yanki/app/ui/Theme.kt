package dev.yanki.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Paper = Color(0xFFF8F8F5)
val Ink = Color(0xFF161A1E)
val Muted = Color(0xFF737980)
val Lime = Color(0xFFC8FC26)
val Cobalt = Color(0xFF283CEB)
val Pane = Color(0xFFFFFFFF)
val Line = Color(0xFFEDF0EB)
val LightBlue = Color(0xFFE9EDFF)
val PaleLime = Color(0xFFEFF9D1)

private val colors = lightColorScheme(
    primary = Cobalt,
    onPrimary = Color.White,
    secondary = Lime,
    onSecondary = Ink,
    background = Paper,
    onBackground = Ink,
    surface = Pane,
    onSurface = Ink,
    surfaceVariant = LightBlue,
    outline = Line
)
private val typography = Typography(
    headlineLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.ExtraBold, fontSize = 34.sp, lineHeight = 37.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 30.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 25.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 23.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
)
@Composable
fun YankiTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}
