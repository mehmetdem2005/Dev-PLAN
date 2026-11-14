package dev.yanki.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import dev.yanki.app.audio.VoiceEngine
import dev.yanki.app.data.LocalStore
import dev.yanki.app.ui.YankiApp
import dev.yanki.app.ui.YankiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val store = remember { LocalStore(applicationContext) }
            val voice = remember { VoiceEngine(applicationContext) }
            DisposableEffect(voice) { onDispose { voice.close() } }
            YankiTheme { YankiApp(store, voice) }
        }
    }
}
