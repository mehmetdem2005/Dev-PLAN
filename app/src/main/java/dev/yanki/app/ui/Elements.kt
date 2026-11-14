package dev.yanki.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

@Composable
fun Pill(text: String, color: Color = PaleLime, ink: Color = Ink) {
    Box(Modifier.clip(CircleShape).background(color).padding(horizontal = 12.dp, vertical = 7.dp)) {
        Text(text, color = ink, style = MaterialTheme.typography.labelLarge, fontSize = 12.sp)
    }
}
@Composable
fun MajorButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, dark: Boolean = false, enabled: Boolean = true) {
    Button(
        onClick = onClick, enabled = enabled, modifier = modifier.fillMaxWidth().height(54.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (dark) Cobalt else Lime, contentColor = if (dark) Color.White else Ink,
            disabledContainerColor = Line, disabledContentColor = Muted),
        shape = RoundedCornerShape(17.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
    }
}
@Composable
fun OutlineButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick, modifier = modifier.fillMaxWidth().height(50.dp),
        border = BorderStroke(1.dp, Line), shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink)
    ) { Text(text, fontWeight = FontWeight.SemiBold) }
}
@Composable
fun SectionTitle(text: String, action: String? = null, onAction: (() -> Unit)? = null) {
    Row(Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text, style = MaterialTheme.typography.titleLarge)
        if (action != null && onAction != null) Text(action, color = Cobalt, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onAction() })
    }
}
@Composable
fun SelectRow(title: String, subtitle: String? = null, chosen: Boolean = false, onClick: () -> Unit) {
    val bg = if (chosen) PaleLime else Pane
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(16.dp))
        .background(bg).border(1.dp, if (chosen) Lime else Line, RoundedCornerShape(16.dp))
        .clickable { onClick() }.padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = Ink)
            if (subtitle != null) Text(subtitle, color = Muted, fontSize = 12.sp)
        }
        if (chosen) Icon(Icons.Default.Check, contentDescription = "Seçili", tint = Cobalt)
        else Box(Modifier.size(19.dp).border(1.dp, Muted, CircleShape))
    }
}
@Composable
fun Tile(title: String, subtitle: String, onClick: () -> Unit, color: Color = Pane, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(20.dp)).background(color).clickable { onClick() }
        .border(1.dp, if (color == Pane) Line else color, RoundedCornerShape(20.dp))
        .padding(16.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Muted)
        Spacer(Modifier.height(14.dp))
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Cobalt, modifier = Modifier.size(19.dp))
    }
}
@Composable
fun Waveform(modifier: Modifier = Modifier, color: Color = Cobalt, bars: Int = 52) {
    Canvas(modifier.height(72.dp)) {
        val mid = size.height / 2
        val step = size.width / (bars + 1f)
        for (i in 0 until bars) {
            val n = kotlin.math.abs(sin((i + 2) * .49) * sin((i + 7) * .16))
            val h = (size.height * .12 + n * size.height * .70).toFloat()
            val x = step * (i + 1)
            drawLine(color, Offset(x, mid - h / 2), Offset(x, mid + h / 2),
                strokeWidth = (step * .45f).coerceAtLeast(2f), cap = StrokeCap.Round)
        }
    }
}
@Composable
fun MountainArt(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        drawCircle(Lime, radius = size.minDimension * .24f, center = Offset(size.width * .69f, size.height * .30f))
        val back = Path().apply {
            moveTo(0f,size.height*.76f)
            lineTo(size.width*.22f,size.height*.37f)
            lineTo(size.width*.44f,size.height*.64f)
            lineTo(size.width*.66f,size.height*.27f)
            lineTo(size.width,size.height*.78f)
            lineTo(size.width,size.height)
            lineTo(0f,size.height)
            close()
        }
        drawPath(back, Color(0xFFBEC3BE))
        val front = Path().apply {
            moveTo(0f,size.height*.84f)
            lineTo(size.width*.32f,size.height*.55f)
            lineTo(size.width*.54f,size.height*.79f)
            lineTo(size.width*.81f,size.height*.46f)
            lineTo(size.width,size.height*.69f)
            lineTo(size.width,size.height)
            lineTo(0f,size.height)
            close()
        }
        drawPath(front, Color(0xFF686D69))
        for(i in 1..5) {
            val x=size.width*i/6f
            drawLine(Color.White.copy(alpha=.45f),Offset(x,size.height*.89f),Offset(x+size.width*.04f,size.height*.83f), strokeWidth=2.dp.toPx())
        }
    }
}
@Composable
fun InfoNotice(text: String) {
    Surface(color = LightBlue, shape = RoundedCornerShape(16.dp)) {
        Text(text, modifier = Modifier.padding(14.dp), fontSize = 12.sp, lineHeight = 18.sp, color = Ink)
    }
}
@Composable
fun MetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(18.dp)).background(Pane).border(1.dp, Line, RoundedCornerShape(18.dp)).padding(16.dp)) {
        Text(value, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Cobalt)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = Muted)
    }
}
@Composable
fun SoundButton(text: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = LightBlue, shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment=Alignment.CenterVertically) {
            Icon(Icons.Default.PlayArrow, null, tint = Cobalt)
            Text(text, modifier = Modifier.padding(start = 7.dp), color = Cobalt, fontWeight=FontWeight.SemiBold)
        }
    }
}
