package dev.yanki.app.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import dev.yanki.app.audio.VoiceEngine
import dev.yanki.app.data.LearningContent
import dev.yanki.app.data.LocalStore
import dev.yanki.app.share.ShareCardExporter
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class Page {
    WELCOME, SETUP, CHECK, HOME, MISSIONS, SCENARIO, RECORD, IPA, ANALYSIS, DAILY,
    COMMUNITY, PROGRESS, SHARE, PROFILE
}

@Composable
fun YankiApp(store: LocalStore, voice: VoiceEngine) {
    val context = LocalContext.current
    var page by remember { mutableStateOf(if(store.onboarded) Page.HOME else Page.WELCOME) }
    var chosenLanguage by remember { mutableStateOf(store.language) }
    var chosenGoal by remember { mutableStateOf(store.goal) }
    var checkAnswer by remember { mutableIntStateOf(-1) }
    var recording by remember { mutableStateOf(false) }
    var began by remember { mutableLongStateOf(0L) }
    var seconds by remember { mutableIntStateOf(0) }
    val recordedPeaks = remember { mutableStateListOf<Int>() }
    var selectedSound by remember { mutableIntStateOf(0) }
    var pairIndex by remember { mutableIntStateOf(0) }
    var pairTarget by remember { mutableIntStateOf(Random.nextInt(2)) }
    var pairGuess by remember { mutableIntStateOf(-1) }
    val mission = LearningContent.missions.firstOrNull { it.id == store.selectedMission }
        ?: LearningContent.missions.first()

    fun goto(target: Page) { page = target }
    fun share(message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(Intent.createChooser(intent, "Paylaş"))
    }
    fun say(text: String) {
        if (!voice.speak(text))
            Toast.makeText(context, "Cihazda kullanılabilir İngilizce TTS motoru bulunamadı.", Toast.LENGTH_LONG).show()
    }
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted && voice.start()) {
            recordedPeaks.clear()
            began = SystemClock.elapsedRealtime()
            recording = true
        } else {
            Toast.makeText(context, "Mikrofon izni veya ses kaydı başlatma işlemi başarısız.", Toast.LENGTH_LONG).show()
        }
    }
    fun toggleRecord() {
        if (recording) {
            val result = voice.stop()
            recording = false
            if (result != null) {
                store.saveRecording(result.first, result.second, recordedPeaks.toList())
                page = Page.ANALYSIS
            } else Toast.makeText(context, "Kayıt kaydedilemedi. Yeniden deneyin.", Toast.LENGTH_LONG).show()
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permission.launch(Manifest.permission.RECORD_AUDIO)
            } else if (voice.start()) {
                recordedPeaks.clear()
                began = SystemClock.elapsedRealtime()
                recording = true
            } else Toast.makeText(context, "Mikrofon başlatılamadı.", Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(recording) {
        while (recording) {
            seconds = ((SystemClock.elapsedRealtime() - began) / 1000L).toInt()
            if (recordedPeaks.size < 500) recordedPeaks.add(voice.peakAmplitude())
            delay(160)
        }
    }
    BackHandler(page != Page.HOME && page != Page.WELCOME) {
        if (recording) {
            voice.stop()
            recording = false
        }
        page = if (page == Page.CHECK) Page.SETUP else Page.HOME
    }
    when(page) {
        Page.WELCOME -> WelcomePage(onStart = { goto(Page.SETUP) })
        Page.SETUP -> SetupPage(chosenLanguage, chosenGoal, onLanguage = { chosenLanguage = it },
            onGoal = { chosenGoal = it }, onNext = {
                store.configure(chosenLanguage, chosenGoal)
                goto(Page.CHECK)
            })
        Page.CHECK -> CheckPage(checkAnswer, onAnswer = { checkAnswer = it }, onNext = {
            store.finishOnboarding()
            goto(Page.HOME)
        })
        else -> {
            AppFrame(
                title = when(page) {
                    Page.HOME -> "YANKI"
                    Page.MISSIONS -> "Senaryolar"
                    Page.SCENARIO -> "Görev brifingi"
                    Page.RECORD -> "Konuşma stüdyosu"
                    Page.IPA -> "Telaffuz laboratuvarı"
                    Page.ANALYSIS -> "Kayıt karşılaştırması"
                    Page.DAILY -> "Günlük meydan okuma"
                    Page.COMMUNITY -> "Topluluk"
                    Page.PROGRESS -> "İlerleme"
                    Page.SHARE -> "Paylaşım stüdyosu"
                    Page.PROFILE -> "Profil ve ayarlar"
                    else -> "YANKI"
                },
                showBack = page != Page.HOME,
                onBack = { if (recording) { voice.stop(); recording = false }; goto(Page.HOME) },
                page = page,
                onNavigate = { goto(it) }
            ) {
                when(page) {
                    Page.HOME -> HomePage(store, onNavigate = { goto(it) },
                        onMission = { store.selectMission(it); goto(Page.SCENARIO) })
                    Page.MISSIONS -> MissionsPage(store, onMission = { store.selectMission(it); goto(Page.SCENARIO) })
                    Page.SCENARIO -> ScenarioPage(mission, onPlay = { say(mission.prompt) }, onBegin = { goto(Page.RECORD) })
                    Page.RECORD -> RecordPage(mission, recording, seconds, recordedPeaks.toList(),
                        firstExists = store.firstRecording.isNotBlank(),
                        onRecord = { toggleRecord() }, onExample = { say(mission.prompt) },
                        onAnalysis = { goto(Page.ANALYSIS) })
                    Page.IPA -> IpaPage(selectedSound, onSelect = { selectedSound = it },
                        onSpeak = { say(it) })
                    Page.ANALYSIS -> AnalysisPage(store, onPlay = {
                        if (!voice.play(it)) Toast.makeText(context, "Kayıt bulunamadı.", Toast.LENGTH_SHORT).show()
                    }, onRetry = { goto(Page.RECORD) },
                        onFinish = { store.markCompleted(); goto(Page.PROGRESS) },
                        onShare = { goto(Page.SHARE) })
                    Page.DAILY -> DailyPage(store, pairIndex, pairTarget, pairGuess,
                        onListen = {
                            val pair = LearningContent.pairs[pairIndex]
                            say(if (pairTarget == 0) pair.first else pair.second)
                        },
                        onGuess = { guess ->
                            if (pairGuess == -1) {
                                pairGuess = guess
                                store.addPairResult(guess == pairTarget)
                            }
                        },
                        onNext = {
                            pairIndex = (pairIndex + 1) % LearningContent.pairs.size
                            pairTarget = Random.nextInt(2)
                            pairGuess = -1
                        },
                        onIpa = { goto(Page.IPA) })
                    Page.COMMUNITY -> CommunityPage(onShare = {
                        share("YANKI konuşma meydan okuması: " + mission.title +
                            "\nSen de söyle: " + mission.prompt + "\nBenimle denemek ister misin?")
                    }, onDaily = { goto(Page.DAILY) })
                    Page.PROGRESS -> ProgressPage(store, onAnalysis = { goto(Page.ANALYSIS) },
                        onDaily = { goto(Page.DAILY) })
                    Page.SHARE -> SharePage(store, onShare = {
                        runCatching { ShareCardExporter.share(context, mission, store.firstDuration, store.secondDuration) }
                            .onFailure {
                                Toast.makeText(context, "Görsel paylaşımı başlatılamadı.", Toast.LENGTH_LONG).show()
                            }
                    })
                    Page.PROFILE -> ProfilePage(store, onGoals = { goto(Page.SETUP) },
                        onIpa = { goto(Page.IPA) }, onProgress = { goto(Page.PROGRESS) })
                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun AppFrame(
    title: String,
    showBack: Boolean,
    onBack: () -> Unit,
    page: Page,
    onNavigate: (Page) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(Modifier.fillMaxSize().background(Paper)) {
        Row(Modifier.fillMaxWidth().padding(start = 18.dp, end = 20.dp, top = 12.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically) {
            if (showBack) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Geri", tint = Ink) }
                Spacer(Modifier.width(4.dp))
            }
            Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f), color = Ink)
            if(page == Page.HOME) Pill("ÇEVRİMDIŞI", PaleLime)
        }
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
            content = content
        )
        if (page in listOf(Page.HOME, Page.MISSIONS, Page.DAILY, Page.COMMUNITY, Page.PROGRESS, Page.PROFILE)) {
            NavigationBar(containerColor = Pane, tonalElevation = 0.dp) {
                val items = listOf(
                    Triple(Page.HOME, "Ana sayfa", Icons.Default.Home),
                    Triple(Page.MISSIONS, "Görevler", Icons.Default.Mic),
                    Triple(Page.COMMUNITY, "Topluluk", Icons.Default.People),
                    Triple(Page.PROGRESS, "İlerleme", Icons.Default.BarChart),
                    Triple(Page.PROFILE, "Profil", Icons.Default.Person)
                )
                for ((destination, name, icon) in items) {
                    NavigationBarItem(
                        selected = destination == page,
                        onClick = { onNavigate(destination) },
                        icon = { Icon(icon, name) },
                        label = { Text(name, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Ink, selectedTextColor = Ink,
                            indicatorColor = Lime, unselectedIconColor = Muted, unselectedTextColor = Muted
                        )
                    )
                }
            }
        }
    }
}
