package dev.yanki.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.yanki.app.data.LearningContent
import dev.yanki.app.data.LocalStore
import java.io.File

@Composable
fun IpaPage(selected: Int, onSelect: (Int) -> Unit, onSpeak: (String) -> Unit) {
    val lesson = LearningContent.sounds[selected]
    Text("Sesi tanı. Nasıl üretildiğini anla.", style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(8.dp))
    Text("Artikülasyon çizimi bir eğitim şemasıdır; telefon dilinin yerini ölçmez.", color=Muted)
    Spacer(Modifier.height(18.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp), modifier=Modifier.fillMaxWidth()) {
        for (index in 0..2) {
            val l = LearningContent.sounds[index]
            Surface(onClick = { onSelect(index) }, color=if (selected==index) Lime else Pane,
                shape = RoundedCornerShape(15.dp), modifier=Modifier.weight(1f)) {
                Text(l.symbol, Modifier.padding(14.dp), textAlign=TextAlign.Center, fontWeight=FontWeight.Bold, fontSize=23.sp)
            }
        }
    }
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp), modifier=Modifier.fillMaxWidth()) {
        for (index in 3..5) {
            val l = LearningContent.sounds[index]
            Surface(onClick = { onSelect(index) }, color=if (selected==index) Lime else Pane,
                shape = RoundedCornerShape(15.dp), modifier=Modifier.weight(1f)) {
                Text(l.symbol, Modifier.padding(14.dp), textAlign=TextAlign.Center, fontWeight=FontWeight.Bold, fontSize=23.sp)
            }
        }
    }
    Spacer(Modifier.height(16.dp))
    Surface(color=Pane, shape=RoundedCornerShape(26.dp)) {
        Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment=Alignment.CenterHorizontally) {
            Text(lesson.symbol, fontSize=72.sp, fontWeight=FontWeight.Black, color=Cobalt)
            Text(lesson.name, style=MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Box(Modifier.fillMaxWidth().height(150.dp).background(LightBlue, RoundedCornerShape(20.dp))) {
                Canvas(Modifier.fillMaxSize()) {
                    val mid = size.height / 2
                    val outer = Path().apply {
                        moveTo(size.width * .15f, mid)
                        cubicTo(size.width*.30f,size.height*.1f,size.width*.75f,size.height*.06f,size.width*.84f,mid)
                        cubicTo(size.width*.74f,size.height*.92f,size.width*.36f,size.height*.92f,size.width*.15f,mid)
                    }
                    drawPath(outer, Cobalt, style=androidx.compose.ui.graphics.drawscope.Stroke(width=4.dp.toPx(),cap=StrokeCap.Round))
                    val tongue = Path().apply {
                        moveTo(size.width*.27f,size.height*.55f)
                        cubicTo(size.width*.47f,size.height*.72f,size.width*.67f,size.height*.65f,size.width*.79f,size.height*.45f)
                    }
                    drawPath(tongue, Color(0xFF8B9297), style=androidx.compose.ui.graphics.drawscope.Stroke(width=6.dp.toPx(),cap=StrokeCap.Round))
                    drawCircle(Lime, radius=9.dp.toPx(), center=Offset(size.width*.72f,size.height*.43f))
                }
                Pill("ŞEMATİK GÖSTERİM", Pane)
            }
            Spacer(Modifier.height(18.dp))
            Text(lesson.instruction, style=MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(12.dp))
            InfoNotice(lesson.contrast)
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment=Alignment.CenterVertically) {
                Text(lesson.example + "  " + lesson.transcription, modifier=Modifier.weight(1f), fontWeight=FontWeight.SemiBold)
                SoundButton("Dinle", onClick={ onSpeak(lesson.example) })
            }
        }
    }
    Spacer(Modifier.height(22.dp))
    InfoNotice("Fonem doğruluğu henüz otomatik hesaplanmıyor. Çalışma: örneği dinle, sesin üretim yerini öğren, ardından kendi kaydında dene.")
    Spacer(Modifier.height(28.dp))
}

@Composable
fun AnalysisPage(store: LocalStore, onPlay: (String) -> Unit, onRetry: () -> Unit,
    onFinish: () -> Unit, onShare: () -> Unit) {
    Text("Kendi sesindeki farkı dinle.", style=MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(8.dp))
    Text("Kayıt süreleri ve tepe genlikleri gerçek mikrofon verisidir; fonem doğruluğu analizi değildir.", color=Muted)
    Spacer(Modifier.height(20.dp))
    RecordingCard("01", "İlk deneme", store.firstRecording, store.firstDuration, store.firstPeaks, onPlay)
    Spacer(Modifier.height(12.dp))
    RecordingCard("02", "Sonraki deneme", store.secondRecording, store.secondDuration, store.secondPeaks, onPlay)
    Spacer(Modifier.height(18.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
        MetricCard("İlk kayıt süresi", (store.firstDuration / 1000.0).formatSeconds(), Modifier.weight(1f))
        MetricCard("Son kayıt süresi", (store.secondDuration / 1000.0).formatSeconds(), Modifier.weight(1f))
    }
    Spacer(Modifier.height(18.dp))
    InfoNotice("Süre kısalması tek başına akıcılığın veya telaffuz doğruluğunun arttığını göstermez. İlk ve sonraki kaydı kulakla karşılaştır.")
    Spacer(Modifier.height(20.dp))
    MajorButton(if (store.secondRecording.isBlank()) "Bir kez daha söyle" else "Yeni deneme yap", onRetry)
    Spacer(Modifier.height(10.dp))
    if(store.firstRecording.isNotBlank()) {
        OutlineButton("Görevi tamamla", onFinish)
        Spacer(Modifier.height(10.dp))
        OutlineButton("Paylaşım ekranı", onShare)
    }
    Spacer(Modifier.height(28.dp))
}
private fun Double.formatSeconds(): String = String.format(java.util.Locale.US, "%.1f sn", this)

@Composable
private fun RecordingCard(number: String, title: String, path: String, duration: Int, peaks: List<Int>, onPlay: (String) -> Unit) {
    Surface(color=Pane, shape=RoundedCornerShape(22.dp), modifier=Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment=Alignment.CenterVertically) {
                Pill(number)
                Spacer(Modifier.width(10.dp))
                Text(title, style=MaterialTheme.typography.titleMedium, modifier=Modifier.weight(1f))
                Text((duration/1000.0).formatSeconds(), color=Muted, fontSize=12.sp)
            }
            Spacer(Modifier.height(8.dp))
            MeasuredWaveform(peaks, Modifier.fillMaxWidth(), color = Cobalt)
            Spacer(Modifier.height(9.dp))
            OutlinedButton(onClick={ onPlay(path) }, enabled=path.isNotBlank() && File(path).exists(),
                shape=RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.PlayArrow, null)
                Spacer(Modifier.width(6.dp))
                Text(if(path.isBlank()) "Henüz kayıt yok" else "Kaydı dinle")
            }
        }
    }
}

@Composable
fun DailyPage(
    store: LocalStore, pairIndex: Int, pairTarget: Int, pairGuess: Int,
    onListen: () -> Unit, onGuess: (Int) -> Unit, onNext: () -> Unit, onIpa: () -> Unit
) {
    val pair = LearningContent.pairs[pairIndex]
    Pill("DİNLEME  •  MİNİMAL ÇİFTLER")
    Spacer(Modifier.height(16.dp))
    Text("Duyduğun kelime hangisi?", style=MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(10.dp))
    Text("Önce dinle; ardından gördüğün iki kelimeden birini seç. Bu, konuşma üretim testi değildir.", color=Muted)
    Spacer(Modifier.height(24.dp))
    Surface(color=LightBlue, shape=RoundedCornerShape(26.dp)) {
        Column(Modifier.fillMaxWidth().padding(26.dp), horizontalAlignment=Alignment.CenterHorizontally) {
            Box(Modifier.size(86.dp).background(Cobalt, CircleShape).clickable { onListen() },
                contentAlignment=Alignment.Center) {
                Icon(Icons.Default.Headphones, "Sesi dinle", tint=Color.White, modifier=Modifier.size(37.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text("DİNLEMEK İÇİN DOKUN", color=Cobalt, fontWeight=FontWeight.Bold, fontSize=11.sp)
            Spacer(Modifier.height(14.dp))
            Waveform(Modifier.fillMaxWidth(), color=Cobalt)
        }
    }
    Spacer(Modifier.height(18.dp))
    listOf(pair.first to pair.firstIpa, pair.second to pair.secondIpa).forEachIndexed { index, content ->
        val bg = when {
            pairGuess == -1 -> Pane
            index == pairTarget -> PaleLime
            index == pairGuess -> Color(0xFFFFE9E5)
            else -> Pane
        }
        Surface(onClick={ onGuess(index) }, enabled=pairGuess == -1,
            color=bg, shape=RoundedCornerShape(18.dp),
            modifier=Modifier.fillMaxWidth().padding(vertical=5.dp)) {
            Row(Modifier.padding(19.dp), verticalAlignment=Alignment.CenterVertically) {
                Text(content.first, fontWeight=FontWeight.Bold, fontSize=23.sp, modifier=Modifier.weight(1f))
                Text(content.second, color=Muted)
                if(pairGuess != -1 && index == pairTarget) {
                    Spacer(Modifier.width(10.dp))
                    Icon(Icons.Default.Check, "Doğru yanıt", tint=Cobalt)
                }
            }
        }
    }
    if(pairGuess != -1) {
        Spacer(Modifier.height(12.dp))
        InfoNotice(if(pairGuess==pairTarget) "Doğru! " + (if(pairTarget==0) pair.first else pair.second) +
            " kelimesini tanıdın." else "Bu turda duyulan kelime: " +
            (if(pairTarget==0) pair.first else pair.second) + ". Tekrar dinleyebilirsin.")
        Spacer(Modifier.height(14.dp))
        MajorButton("Sonraki ses", onNext)
    }
    Spacer(Modifier.height(16.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
        MetricCard("Doğru yanıt", store.pairCorrect.toString(), Modifier.weight(1f))
        MetricCard("Toplam deneme", store.pairAttempts.toString(), Modifier.weight(1f))
    }
    Spacer(Modifier.height(18.dp))
    OutlineButton("IPA laboratuvarını aç", onIpa)
    Spacer(Modifier.height(22.dp))
    InfoNotice("Örnek ses cihazın Text-to-Speech motorundan gelir. Dil paketi kurulu değilse ses oynatılamayabilir. Ses tanıma için ücretli bir API kullanılmaz.")
    Spacer(Modifier.height(20.dp))
}
