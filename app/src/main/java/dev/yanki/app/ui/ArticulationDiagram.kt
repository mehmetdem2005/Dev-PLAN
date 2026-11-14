package dev.yanki.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Educational cutaway, not a camera/microphone measurement of the speaker's tongue.
 * The emphasized tongue shape changes for the target sound.
 */
@Composable
fun ArticulationDiagram(symbol: String, modifier: Modifier = Modifier) {
    Column(modifier.background(LightBlue, RoundedCornerShape(18.dp)).padding(12.dp)) {
        Text("YAN KESİT  •  ŞEMATİK", color=Cobalt, fontSize=10.sp)
        Canvas(Modifier.fillMaxWidth().height(155.dp)) {
            val scaleX = size.width / 360f
            val scaleY = size.height / 155f
            fun p(x: Float, y: Float) = Offset(x*scaleX,y*scaleY)
            fun trace(path: Path, color: Color, thickness: Float) =
                drawPath(path,color,style=Stroke(width=thickness.dp.toPx(),cap=StrokeCap.Round))

            val roof=Path().apply {
                moveTo(45f*scaleX,33f*scaleY)
                cubicTo(90f*scaleX,18f*scaleY,210f*scaleX,17f*scaleY,271f*scaleX,40f*scaleY)
            }
            trace(roof, Ink, 4f)
            val palate=Path().apply {
                moveTo(50f*scaleX,45f*scaleY)
                cubicTo(119f*scaleX,42f*scaleY,196f*scaleX,34f*scaleY,267f*scaleX,56f*scaleY)
            }
            trace(palate, Color(0xFFBCC6C9),3f)
            val jaw=Path().apply {
                moveTo(44f*scaleX,116f*scaleY)
                cubicTo(113f*scaleX,146f*scaleY,232f*scaleX,142f*scaleY,283f*scaleX,111f*scaleY)
            }
            trace(jaw, Ink, 4f)
            val isDental = symbol=="/θ/" || symbol=="/ð/"
            val isHigh = symbol=="/iː/" || symbol=="/ɪ/"
            val isLow = symbol=="/æ/"
            val isLip = symbol=="/w/"
            // The front teeth are drawn as a fixed landmark.
            drawRoundRect(Color.White, topLeft=p(264f,48f),
                size=androidx.compose.ui.geometry.Size(13f*scaleX,25f*scaleY),
                cornerRadius=androidx.compose.ui.geometry.CornerRadius(2f*scaleX))
            drawRoundRect(Color.White, topLeft=p(265f,86f),
                size=androidx.compose.ui.geometry.Size(12f*scaleX,24f*scaleY),
                cornerRadius=androidx.compose.ui.geometry.CornerRadius(2f*scaleX))
            val tongue=Path().apply {
                moveTo(70f*scaleX,125f*scaleY)
                when {
                    isDental -> {
                        cubicTo(151f*scaleX,126f*scaleY,202f*scaleX,84f*scaleY,273f*scaleX,77f*scaleY)
                        cubicTo(235f*scaleX,110f*scaleY,149f*scaleX,152f*scaleY,70f*scaleX,125f*scaleY)
                    }
                    isHigh -> {
                        val top=if(symbol=="/iː/") 57f else 72f
                        cubicTo(125f*scaleX,114f*scaleY,188f*scaleX,top*scaleY,236f*scaleX,(top+12f)*scaleY)
                        cubicTo(252f*scaleX,104f*scaleY,132f*scaleX,148f*scaleY,70f*scaleX,125f*scaleY)
                    }
                    isLow -> {
                        cubicTo(138f*scaleX,139f*scaleY,200f*scaleX,120f*scaleY,243f*scaleX,120f*scaleY)
                        cubicTo(248f*scaleX,134f*scaleY,130f*scaleX,151f*scaleY,70f*scaleX,125f*scaleY)
                    }
                    else -> {
                        cubicTo(133f*scaleX,117f*scaleY,199f*scaleX,101f*scaleY,237f*scaleX,112f*scaleY)
                        cubicTo(221f*scaleX,130f*scaleY,126f*scaleX,148f*scaleY,70f*scaleX,125f*scaleY)
                    }
                }
                close()
            }
            drawPath(tongue,Cobalt)
            val lipsX=if(isLip) 283f else 279f
            val lipTop=if(isLip) 69f else 66f
            val lipBottom=if(isLip) 90f else 99f
            drawLine(Color(0xFFB55A61),p(lipsX,lipTop),p(if(isLip) 303f else 289f,77f),
                strokeWidth=(if(isLip) 10 else 6).dp.toPx(),cap=StrokeCap.Round)
            drawLine(Color(0xFFB55A61),p(lipsX,lipBottom),p(if(isLip) 303f else 291f,90f),
                strokeWidth=(if(isLip) 10 else 6).dp.toPx(),cap=StrokeCap.Round)
            val mark=when {
                isDental -> p(272f,78f)
                isHigh -> p(214f, if(symbol=="/iː/") 77f else 88f)
                isLow -> p(212f,123f)
                isLip -> p(298f,83f)
                else -> p(234f,113f)
            }
            drawCircle(Lime, radius=11.dp.toPx(), center=mark)
            drawCircle(Ink, radius=3.dp.toPx(), center=mark)
            drawLine(Cobalt,p(311f,84f),p(344f,84f),strokeWidth=3.dp.toPx(),cap=StrokeCap.Round)
            drawLine(Cobalt,p(336f,77f),p(345f,84f),strokeWidth=3.dp.toPx())
            drawLine(Cobalt,p(336f,91f),p(345f,84f),strokeWidth=3.dp.toPx())
        }
        Text(when(symbol) {
            "/θ/","/ð/" -> "Vurgu: dil ucu ön dişlerin arasında; hava öne akıyor."
            "/iː/" -> "Vurgu: dil ön ve yüksek; dudaklar hafif yaygın."
            "/ɪ/" -> "Vurgu: dil ön-yüksek ama /iː/ sesinden daha gevşek."
            "/æ/" -> "Vurgu: çene açık; dil önde ve aşağıda."
            "/w/" -> "Vurgu: iki dudak kısa süre yuvarlanıyor."
            else -> "Dilin şeklini ve hava yolunu incele."
        }, color=Muted, fontSize=12.sp)
    }
}
