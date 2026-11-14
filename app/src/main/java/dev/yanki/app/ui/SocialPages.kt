package dev.yanki.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.yanki.app.data.LearningContent
import dev.yanki.app.data.LocalStore

@Composable
fun CommunityPage(onShare: () -> Unit, onDaily: () -> Unit) {
    Text("Birlikte pratik yapmak daha kolay.", style=MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(10.dp))
    Text("Arkadaşına bir görev gönder; aynı cümleyi kendi telefonunda denesin.", color=Muted)
    Spacer(Modifier.height(20.dp))
    Surface(color=PaleLime, shape=RoundedCornerShape(26.dp)) {
        Column(Modifier.fillMaxWidth().padding(22.dp)) {
            Pill("ARKADAŞ MEYDAN OKUMASI", Pane)
            Spacer(Modifier.height(14.dp))
            Icon(Icons.Default.EmojiEvents, null, tint=Cobalt, modifier=Modifier.size(43.dp))
            Spacer(Modifier.height(8.dp))
            Text("Ship mi, sheep mi?", style=MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("Üç ses turunu tamamla. Sonra arkadaşından da denemesini iste.", color=Muted)
            Spacer(Modifier.height(18.dp))
            MajorButton("Meydan okumayı aç", onDaily, dark=true)
        }
    }
    Spacer(Modifier.height(16.dp))
    Surface(color=Pane, shape=RoundedCornerShape(24.dp)) {
        Column(Modifier.padding(20.dp)) {
            Text("Konuşma zincirini başlat", style=MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("İlk cümleni sen söyle, ardından arkadaşın bir yanıt bulsun. Bu sürümde davet metin olarak gönderilir.",
                color=Muted)
            Spacer(Modifier.height(16.dp))
            MajorButton("Arkadaşıma görev gönder", onShare)
        }
    }
    Spacer(Modifier.height(16.dp))
    InfoNotice("Gerçek kullanıcı listesi, sohbet veya canlı lig şu anda yoktur. Gösteriş için sahte sıralama kullanmıyoruz. Çok kullanıcılı sistem sonraki sürümde kurulabilir.")
    Spacer(Modifier.height(28.dp))
}

@Composable
fun ProgressPage(store: LocalStore, onAnalysis: () -> Unit, onDaily: () -> Unit) {
    Text("Gelişimini kanıtlarla gör.", style=MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(9.dp))
    Text("Bunlar gerçek uygulama etkinliklerinden gelen sayılardır; tahmini CEFR veya aksan puanı değildir.", color=Muted)
    Spacer(Modifier.height(20.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
        MetricCard("Tamamlanan görev", store.completedIds.size.toString(), Modifier.weight(1f))
        MetricCard("Sesli deneme", store.practiceCount.toString(), Modifier.weight(1f))
    }
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
        MetricCard("Dinleme yanıtı", store.pairAttempts.toString(), Modifier.weight(1f))
        MetricCard("Doğru ayırt etme", store.pairCorrect.toString(), Modifier.weight(1f))
    }
    SectionTitle("Görev ilerlemesi")
    Surface(color=Pane, shape=RoundedCornerShape(22.dp)) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("Bitirilen görev oranı", color=Muted, fontSize=12.sp)
            Spacer(Modifier.height(7.dp))
            Text(store.completedIds.size.toString() + " / " + LearningContent.missions.size,
                fontSize=30.sp, fontWeight=FontWeight.Black, color=Ink)
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress={store.completedIds.size.toFloat()/LearningContent.missions.size},
                modifier=Modifier.fillMaxWidth().height(12.dp),
                color=Lime, trackColor=Line
            )
        }
    }
    SectionTitle("Önce / sonra kayıtların")
    Surface(color=LightBlue, shape=RoundedCornerShape(24.dp)) {
        Column(Modifier.fillMaxWidth().padding(18.dp)) {
            Text("İlk deneme", fontSize=12.sp, color=Muted)
            MeasuredWaveform(store.firstPeaks, Modifier.fillMaxWidth(), color=Cobalt)
            Text("Sonraki deneme", fontSize=12.sp, color=Muted)
            MeasuredWaveform(store.secondPeaks, Modifier.fillMaxWidth(), color=Ink)
            InfoNotice(if(store.firstRecording.isBlank()) "Önce bir konuşma görevine girip kayıt oluştur." else
                "Bu grafik kayıttaki tepe genlik örneklerinden oluşturuldu. Fonem skoru değildir.")
            Spacer(Modifier.height(12.dp))
            OutlineButton("Kayıt karşılaştırmasını aç", onAnalysis)
        }
    }
    Spacer(Modifier.height(18.dp))
    MajorButton("Dinleme çalışmasına devam et", onDaily)
    Spacer(Modifier.height(28.dp))
}

@Composable
fun SharePage(store: LocalStore, onShare: () -> Unit) {
    Text("Öğrendiklerini paylaş.", style=MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(8.dp))
    Text("Cihazında 1080 × 1920 boyutunda gerçek PNG sonuç kartı oluştur. İnternet gerekmez.", color=Muted)
    Spacer(Modifier.height(22.dp))
    Box(Modifier.fillMaxWidth().height(400.dp).background(Pane, RoundedCornerShape(28.dp))) {
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment=Alignment.CenterHorizontally) {
            Pill("YANKI  /  KONUŞMA GÜNLÜĞÜ", Lime)
            Spacer(Modifier.height(34.dp))
            Text("KONUŞMAYA\nCESARET ET.", fontSize=30.sp, fontWeight=FontWeight.Black,
                lineHeight=34.sp, color=Color.White, textAlign=TextAlign.Center)
            Spacer(Modifier.height(20.dp))
            MeasuredWaveform(store.secondPeaks.ifEmpty {store.firstPeaks}, Modifier.fillMaxWidth(), color=Cobalt)
            Spacer(Modifier.height(18.dp))
            Text("İlk deneme  " + (store.firstDuration/1000) + " sn", color=Ink, fontSize=15.sp)
            Spacer(Modifier.height(12.dp))
            Text("Sonraki deneme  " + (store.secondDuration/1000) + " sn", color=Color.White, fontSize=15.sp)
            Spacer(Modifier.weight(1f))
            Text("KENDİ SESİN • KENDİ HİKÂYEN", color=Cobalt, fontWeight=FontWeight.Bold, fontSize=11.sp)
        }
    }
    Spacer(Modifier.height(18.dp))
    InfoNotice("Paylaş düğmesi gerçek bir PNG kartını oluşturur. MP4 video ve sesli Reels düzenleyicisi henüz uygulanmadı.")
    Spacer(Modifier.height(18.dp))
    MajorButton("PNG sonuç kartını paylaş", onShare, dark=true)
    Spacer(Modifier.height(28.dp))
}

@Composable
fun ProfilePage(store: LocalStore, onGoals: () -> Unit, onIpa: () -> Unit, onProgress: () -> Unit) {
    var showDelete by remember { mutableStateOf(false) }
    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Ses kayıtları silinsin mi?") },
            text = { Text("Uygulamadaki bütün M4A konuşma kayıtları ve ses genlikleri cihazdan silinecek. Bu işlem geri alınamaz.") },
            confirmButton = { TextButton(onClick = { store.deleteAllAudio(); showDelete = false }) { Text("Kalıcı sil") } },
            dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Vazgeç") } }
        )
    }
    Surface(color=Pane, shape=RoundedCornerShape(24.dp)) {
        Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment=Alignment.CenterVertically) {
            Box(Modifier.size(68.dp).background(Lime, CircleShape), contentAlignment=Alignment.Center) {
                Icon(Icons.Default.Person, null, tint=Ink, modifier=Modifier.size(36.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text("YANKI öğrenicisi", style=MaterialTheme.typography.titleLarge)
                Text(store.language + "  •  " + store.goal, color=Muted, fontSize=12.sp)
                Text("Yerel profil • hesap gerektirmez", color=Cobalt, fontSize=11.sp)
            }
        }
    }
    Spacer(Modifier.height(18.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
        MetricCard("Çalışma kaydı", store.practiceCount.toString(), Modifier.weight(1f))
        MetricCard("Tamamlanan", store.completedIds.size.toString(), Modifier.weight(1f))
    }
    SectionTitle("Öğrenme ve uygulama")
    SelectRow("Hedeflerimi değiştir", "Dil hedefi ve çalışma amacı", onClick=onGoals)
    SelectRow("IPA ve ağız konumları", "Gerçek ölçüm değil, öğretici şema", onClick=onIpa)
    SelectRow("İlerleme kayıtlarım", "Yerel ses denemelerim", onClick=onProgress)
    SelectRow("Ses kayıtlarımı sil", "Tüm yerel M4A kayıtlarını kalıcı sil", onClick={ showDelete = true })
    Spacer(Modifier.height(18.dp))
    InfoNotice("Gizlilik: Ses dosyaları uygulamanın özel depolamasında tutulur ve sunucuya yüklenmez. Uygulama kaldırılırsa yerel veriler silinir. Bu sürüm ücretli API, reklam, hesap veya bulut erişimi kullanmaz.")
    Spacer(Modifier.height(16.dp))
    Text("YANKI v0.1 • Kotlin + Jetpack Compose", fontSize=12.sp, color=Muted)
    Spacer(Modifier.height(26.dp))
}
