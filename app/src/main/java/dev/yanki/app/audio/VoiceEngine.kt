package dev.yanki.app.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.SystemClock
import android.speech.tts.TextToSpeech
import java.io.File
import java.util.Locale

class VoiceEngine(private val context: Context) {
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var beganAt = 0L
    var isRecording = false
        private set
    private var tts: TextToSpeech? = null
    private var ttsReady = false

    init {
        tts = TextToSpeech(context.applicationContext) { code ->
            ttsReady = code == TextToSpeech.SUCCESS
            if (ttsReady) tts?.language = Locale.US
        }
    }
    fun start(): Boolean {
        if (isRecording) return false
        return try {
            val output = File(context.filesDir, "voice_" + System.currentTimeMillis() + ".m4a")
            val newRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }
            newRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            newRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            newRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            newRecorder.setAudioEncodingBitRate(96000)
            newRecorder.setAudioSamplingRate(44100)
            newRecorder.setOutputFile(output.absolutePath)
            newRecorder.prepare()
            newRecorder.start()
            recorder = newRecorder
            beganAt = SystemClock.elapsedRealtime()
            lastFile = output.absolutePath
            isRecording = true
            true
        } catch (_: Exception) {
            runCatching { recorder?.release() }
            recorder = null
            isRecording = false
            false
        }
    }
    private var lastFile = ""
    fun stop(): Pair<String, Int>? {
        if (!isRecording) return null
        val elapsed = (SystemClock.elapsedRealtime() - beganAt).toInt()
        return try {
            recorder?.stop()
            val result = lastFile to elapsed
            result
        } catch (_: Exception) {
            File(lastFile).delete()
            null
        } finally {
            recorder?.release()
            recorder = null
            isRecording = false
        }
    }
    fun play(path: String): Boolean {
        val file = File(path)
        if (!file.exists()) return false
        return try {
            player?.release()
            player = MediaPlayer().apply {
                setDataSource(path)
                setOnCompletionListener { it.release(); player = null }
                prepare()
                start()
            }
            true
        } catch (_: Exception) { false }
    }
    fun speak(text: String): Boolean {
        if (!ttsReady) return false
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "yanki_example")
        return true
    }
    fun close() {
        if (isRecording) stop()
        player?.release()
        player = null
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
