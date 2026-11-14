package dev.yanki.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.yanki.app.data.LearningContent
import dev.yanki.app.data.LocalStore
import dev.yanki.app.data.Mission

@Composable
fun WelcomePage(onStart: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Paper).statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(24.dp)) {
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.height(42.dp), verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.foundation.Canvas(Modifier.size(22.dp)) {
                    val xs = listOf(.15f,.3f,.5f,.7f,.85f)
                    val hs = listOf(.4f,.8f,1f,.6f,.3f)
                    for(i in xs.indices) {
                        val x = size.width * xs[i]
                        val h = size.height * hs[i]
                        drawLine(Cobalt, androidx.compose.ui.geometry.Offset(x,(size.height-h)/2),
                            androidx.compose.ui.geometry.Offset(x,(size.height+h)/2), strokeWidth = 4.dp.toPx())
                    }
                }
                Spacer(Modifier.width(10.dp))
                Text("YANKI", fontWeight = FontWeight.Black, fontSize = 31.sp, color = Ink)
            }
            Spacer(Modifier.weight(1f))
            Pill("ANDROID  •  ÜCRETSİZ")
        }
        Spacer(Modifier.height(38.dp))
        Box(Modifier.fillMaxWidth().height(265.dp).clip(RoundedCornerShape(30.dp)).background(Color(0xFFE7E8E2))) {
            MountainArt(Modifier.fillMaxSize())
            Box(Modifier.align(Alignment.BottomStart).padding(18.dp).clip(RoundedCornerShape(30.dp))
                .background(Pane).padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text("KONUŞ  •  KEŞFET  •  GELİŞ", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Cobalt)
            }
        }
        Spacer(Modifier.height(34.dp))
        Text("Bir dili, gerçek konuşma deneyimiyle öğren.", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        Text("Dinle, konuş, analiz et, tekrar et. Hedefin ne olursa olsun kendi hızında ilerle.",
            color = Muted, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(28.dp))
        MajorButton("Hadi başlayalım", onStart)
        Spacer(Modifier.height(16.dp))
        Text("Hesap gerektirmez  •  Sesler cihazında saklanır",
            color = Muted, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun SetupPage(
    language: String, goal: String, onLanguage: (String) -> Unit,
    onGoal: (String) -> Unit, onNext: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Paper).statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(24.dp)) {
        Spacer(Modifier.height(20.dp))
        Pill("01 / 02  •  BAŞLANGIÇ")
        Spacer(Modifier.height(24.dp))
        Text("Dilini ve hedefini seç.", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text("İlk sürümde İngilizce konuşma görevleri hazır. Diğer diller daha sonra gelecek.", color = Muted)
        Spacer(Modifier.height(18.dp))
        for (item in LearningContent.languages) {
            val enabled = item == "İngilizce"
            Surface(
                onClick = { if(enabled) onLanguage(item) },
                modifier = Modifier.padding(vertical = 4.dp),
                enabled = enabled,
                color = if (item == language) PaleLime else Pane,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if(item == language) Lime else Line)
            ) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(item, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    Text(if(enabled) "Hazır" else "Yakında", color = if(enabled) Cobalt else Muted, fontSize = 12.sp)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        SectionTitle("Asıl hedefin ne?")
        for (item in LearningContent.goals) {
            SelectRow(item, chosen = item == goal, onClick = { onGoal(item) })
        }
        Spacer(Modifier.height(20.dp))
        MajorButton("Devam et", onNext, dark = true)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun CheckPage(answer: Int, onAnswer: (Int) -> Unit, onNext: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Paper).statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(24.dp)) {
        Spacer(Modifier.height(20.dp))
        Pill("02 / 02  •  ISINMA")
        Spacer(Modifier.height(24.dp))
        Text("Küçük bir ısınma.", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(12.dp))
        Text("Bu bir seviye sertifikası değil. Sadece ilk göreve hazırlık.", color = Muted)
        Spacer(Modifier.height(35.dp))
        Surface(color = Pane, shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Pill("İNGİLİZCE  •  KELİME")
                Spacer(Modifier.height(18.dp))
                Text("They ____ to the meeting yesterday.", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(18.dp))
                listOf("go", "went", "going").forEachIndexed { index, option ->
                    SelectRow(option, chosen = answer == index, onClick = { onAnswer(index) })
                }
                if (answer != -1) {
                    Spacer(Modifier.height(10.dp))
                    Text(if(answer == 1) "Doğru: 'went' geçmiş zaman biçimi." else "Doğrusu 'went'. 'Yesterday' geçmiş zamanı gösterir.",
                        color = if(answer == 1) Color(0xFF297045) else Muted)
                }
            }
        }
        Spacer(Modifier.height(36.dp))
        MajorButton(if(answer == -1) "Testi atla ve başla" else "İlk görevime geç", onNext)
        Spacer(Modifier.height(15.dp))
    }
}

@Composable
fun HomePage(store: LocalStore, onNavigate: (Page) -> Unit, onMission: (String) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Merhaba, hoş geldin.", color = Muted, fontSize = 13.sp)
                Text("Bugün sesini duyur.", style = MaterialTheme.typography.headlineMedium)
            }
            Pill("İNGİLİZCE", PaleLime)
        }
        Spacer(Modifier.height(22.dp))
        Surface(color = Pane, shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("TAMAMLANAN GÖREV", fontSize = 11.sp, color = Muted, fontWeight = FontWeight.Bold)
                    Text(store.completedIds.size.toString() + " / " + LearningContent.missions.size,
                        fontSize = 29.sp, fontWeight = FontWeight.Black, color = Ink)
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(progress = { store.completedIds.size.toFloat() / LearningContent.missions.size },
                        modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
                        color = Lime, trackColor = Line)
                }
                Spacer(Modifier.width(14.dp))
                Box(Modifier.size(66.dp).clip(CircleShape).background(Lime), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Mic, contentDescription = null, tint = Ink, modifier = Modifier.size(28.dp))
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Box(Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(26.dp)).background(Color(0xFFE2E5E0))) {
            MountainArt(Modifier.fillMaxSize())
            Surface(
                onClick = { onMission("cafe") },
                color = Pane,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(12.dp)
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("BUGÜNÜN GÖREVİ", color = Cobalt, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Kafede sipariş ver", style = MaterialTheme.typography.titleMedium)
                        Text("İlk kaydını oluştur", color = Muted, fontSize = 12.sp)
                    }
                    Icon(Icons.Default.ArrowForward, null, tint = Cobalt)
                }
            }
        }
        val dueMissions = store.dueMissionIds()
    if (dueMissions.isNotEmpty()) {
        Spacer(Modifier.height(16.dp))
        Surface(onClick = { onMission(dueMissions.first()) }, color = PaleLime,
            shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("BUGÜN HATIRLAMA ZAMANI", color = Cobalt, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text("Tekrar bekleyen " + dueMissions.size + " görev", fontWeight = FontWeight.Bold)
                    Text("İpucu olmadan yeniden söyle", color = Muted, fontSize = 12.sp)
                }
                Icon(Icons.Default.ArrowForward, null, tint = Cobalt)
            }
        }
    }
    SectionTitle("Pratiğe devam et")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Tile("Konuş", "Gerçek hayat görevleri", { onNavigate(Page.MISSIONS) }, color = PaleLime, modifier = Modifier.weight(1f))
            Tile("IPA", "Ses ve artikülasyon", { onNavigate(Page.IPA) }, color = LightBlue, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Tile("Tekrar", "Duyduğunu ayırt et", { onNavigate(Page.DAILY) }, modifier = Modifier.weight(1f))
            Tile("İlerleme", "Önce ve sonra", { onNavigate(Page.PROGRESS) }, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(18.dp))
        InfoNotice("Öğrenme planın: konuşma → düzeltme → yeniden deneme → 1, 3 ve 7 gün sonra hatırlama. Bu basit aralıklı tekrar planıdır, FSRS değildir.")
        Spacer(Modifier.height(26.dp))
    }
}

@Composable
fun MissionsPage(store: LocalStore, onMission: (String) -> Unit) {
    var filter by remember { mutableStateOf("Tümü") }
    Text("Dili gerçek hayatta kullan.", style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(10.dp))
    Text("Kısa, denetlenmiş diyaloglar. Sesli denemeleri cihazına kaydet.", color = Muted)
    Spacer(Modifier.height(18.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (name in listOf("Tümü", "Günlük yaşam", "İş ve kariyer")) {
            Surface(onClick = { filter = name }, color = if(filter == name) Lime else Pane,
                shape = CircleShape, border = androidx.compose.foundation.BorderStroke(1.dp, Line)) {
                Text(name, Modifier.padding(horizontal = 11.dp, vertical = 9.dp), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
    Spacer(Modifier.height(16.dp))
    LearningContent.missions.filter { filter == "Tümü" || it.category == filter }.forEachIndexed { index, m ->
        Surface(onClick = { onMission(m.id) }, color = Pane, shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(if (index % 2 == 0) PaleLime else LightBlue),
                    contentAlignment=Alignment.Center) {
                    Text((index + 1).toString().padStart(2,'0'), fontWeight=FontWeight.Black, color=Cobalt)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(m.title, fontWeight=FontWeight.SemiBold)
                    Text(m.setting + "  ·  " + m.level + "  ·  " + m.minutes + " dk", fontSize=12.sp, color=Muted)
                }
                if (m.id in store.completedIds) Icon(Icons.Default.CheckCircle, "Tamamlandı", tint = Color(0xFF4B9E5C))
                else Icon(Icons.Default.ArrowForward, null, tint = Cobalt)
            }
        }
    }
    Spacer(Modifier.height(20.dp))
}

@Composable
fun ScenarioPage(mission: Mission, onPlay: () -> Unit, onBegin: () -> Unit) {
    Pill(mission.setting + "  •  " + mission.level)
    Spacer(Modifier.height(14.dp))
    Text(mission.title, style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(8.dp))
    Text("Görevi önce dinle. Ardından kendi sesinle dene.", color = Muted)
    Spacer(Modifier.height(20.dp))
    Box(Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(24.dp)).background(LightBlue)) {
        MountainArt(Modifier.fillMaxSize())
        Box(Modifier.align(Alignment.TopStart).padding(14.dp)) { Pill("GERÇEK HAYAT SENARYOSU") }
    }
    Spacer(Modifier.height(22.dp))
    Surface(color = Pane, shape = RoundedCornerShape(22.dp)) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("SÖYLEMEYİ DENE", color = Cobalt, fontWeight=FontWeight.Bold, fontSize=11.sp)
            Spacer(Modifier.height(12.dp))
            Text(mission.prompt, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(mission.translation, color=Muted)
            Spacer(Modifier.height(18.dp))
            SoundButton("Örnek sesi dinle", onPlay)
        }
    }
    Spacer(Modifier.height(18.dp))
    InfoNotice("Görev: " + mission.hint + " İlk denemeden sonra tekrar kaydedip iki sesi karşılaştırabileceksin.")
    Spacer(Modifier.height(22.dp))
    MajorButton("Konuşmaya başla", onBegin, dark = true)
    Spacer(Modifier.height(22.dp))
}

@Composable
fun RecordPage(
    mission: Mission, recording: Boolean, seconds: Int, peaks: List<Int>, firstExists: Boolean,
    onRecord: () -> Unit, onExample: () -> Unit, onAnalysis: () -> Unit
) {
    Pill(if(firstExists) "İKİNCİ DENEME" else "İLK DENEME")
    Spacer(Modifier.height(18.dp))
    Text("Sıra sende.", style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(8.dp))
    Text("Aşağıdaki cümleyi kendi hızında söyle. Mikrofon yalnızca bastığında açılır.", color=Muted)
    Spacer(Modifier.height(20.dp))
    Surface(color=Pane, shape=RoundedCornerShape(24.dp)) {
        Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(mission.prompt, style=MaterialTheme.typography.titleLarge, textAlign=TextAlign.Center)
            Spacer(Modifier.height(12.dp))
            SoundButton("Örneği dinle", onExample)
            Spacer(Modifier.height(18.dp))
            MeasuredWaveform(peaks, Modifier.fillMaxWidth(), color = Cobalt)
            Spacer(Modifier.height(10.dp))
            Text(if(recording) "KAYDEDİLİYOR • " + seconds + " sn" else "KAYIT BEKLENİYOR",
                color=if(recording) Cobalt else Muted, fontWeight=FontWeight.Bold, fontSize=12.sp)
            Spacer(Modifier.height(22.dp))
            Surface(onClick=onRecord, color=if(recording) Cobalt else Lime, shape=CircleShape) {
                Box(Modifier.size(100.dp), contentAlignment=Alignment.Center) {
                    Icon(Icons.Default.Mic, if(recording) "Kaydı bitir" else "Kaydı başlat",
                        tint=if(recording) Color.White else Ink, modifier=Modifier.size(42.dp))
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(if(recording) "Bitirmek için tekrar dokun" else "Kayda başlamak için dokun", fontSize=12.sp, color=Muted)
        }
    }
    Spacer(Modifier.height(16.dp))
    InfoNotice("Mikrofonun kaydettiği tepe genlikleri gösterilir; bu fonem doğruluğu ölçümü değildir. Sesin cihazda M4A olarak saklanır.")
    if(firstExists) {
        Spacer(Modifier.height(14.dp))
        OutlineButton("İlk kaydımı dinle", onAnalysis)
    }
    Spacer(Modifier.height(24.dp))
}
