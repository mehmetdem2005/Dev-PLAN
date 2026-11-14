package dev.yanki.app.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File
import java.time.LocalDate

class LocalStore(private val context: Context) {
    private val preferences = context.getSharedPreferences("yanki_local_v1", Context.MODE_PRIVATE)
    private fun key(name: String, mission: String = selectedMission) = "mission_" + mission + "_" + name
    var language by mutableStateOf(preferences.getString("language", "İngilizce") ?: "İngilizce")
        private set
    var goal by mutableStateOf(preferences.getString("goal", "Akıcı konuşmak") ?: "Akıcı konuşmak")
        private set
    var onboarded by mutableStateOf(preferences.getBoolean("onboarded", false))
        private set
    var selectedMission by mutableStateOf(preferences.getString("mission", "cafe") ?: "cafe")
        private set
    var firstRecording by mutableStateOf(preferences.getString(key("first"), "") ?: "")
        private set
    var secondRecording by mutableStateOf(preferences.getString(key("second"), "") ?: "")
        private set
    var firstDuration by mutableIntStateOf(preferences.getInt(key("first_duration"), 0))
        private set
    var secondDuration by mutableIntStateOf(preferences.getInt(key("second_duration"), 0))
        private set
    var firstPeaks by mutableStateOf(readPeaks("first_peaks"))
        private set
    var secondPeaks by mutableStateOf(readPeaks("second_peaks"))
        private set
    var completedIds by mutableStateOf(preferences.getStringSet("completed", emptySet())?.toSet() ?: emptySet())
        private set
    var practiceCount by mutableIntStateOf(preferences.getInt("practices", 0))
        private set
    var reviewVersion by mutableIntStateOf(0)
        private set
    var pairCorrect by mutableIntStateOf(preferences.getInt("pair_correct", 0))
        private set
    var pairAttempts by mutableIntStateOf(preferences.getInt("pair_attempts", 0))
        private set

    private fun readPeaks(name: String): List<Int> = preferences.getString(key(name), "")
        ?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()

    fun configure(newLanguage: String, newGoal: String) {
        language = newLanguage
        goal = newGoal
        preferences.edit().putString("language", language).putString("goal", goal).apply()
    }
    fun finishOnboarding() {
        onboarded = true
        preferences.edit().putBoolean("onboarded", true).apply()
    }
    fun selectMission(id: String) {
        selectedMission = id
        preferences.edit().putString("mission", id).apply()
        firstRecording = preferences.getString(key("first"), "") ?: ""
        secondRecording = preferences.getString(key("second"), "") ?: ""
        firstDuration = preferences.getInt(key("first_duration"), 0)
        secondDuration = preferences.getInt(key("second_duration"), 0)
        firstPeaks = readPeaks("first_peaks")
        secondPeaks = readPeaks("second_peaks")
    }
    fun saveRecording(path: String, duration: Int, peaks: List<Int> = emptyList()) {
        val editor = preferences.edit()
        if (firstRecording.isBlank()) {
            firstRecording = path
            firstDuration = duration
            firstPeaks = peaks.take(500)
            editor.putString(key("first"), path).putInt(key("first_duration"), duration)
                .putString(key("first_peaks"), firstPeaks.joinToString(","))
        } else {
            if (secondRecording.isNotBlank()) runCatching { File(secondRecording).delete() }
            secondRecording = path
            secondDuration = duration
            secondPeaks = peaks.take(500)
            editor.putString(key("second"), path).putInt(key("second_duration"), duration)
                .putString(key("second_peaks"), secondPeaks.joinToString(","))
        }
        practiceCount += 1
        editor.putInt("practices", practiceCount).apply()
    }
    fun markCompleted() {
        completedIds = completedIds + selectedMission
        val today = LocalDate.now().toEpochDay()
        val dueKey = "review_due_" + selectedMission
        val stageKey = "review_stage_" + selectedMission
        val currentDue = preferences.getLong(dueKey, -1L)
        val stage = preferences.getInt(stageKey, 0)
        val editor = preferences.edit().putStringSet("completed", completedIds)
        if (currentDue == -1L || currentDue <= today) {
            val newStage = (stage + 1).coerceAtMost(3)
            val afterDays = when(newStage) { 1 -> 1L; 2 -> 3L; else -> 7L }
            editor.putInt(stageKey, newStage).putLong(dueKey, today + afterDays)
        }
        editor.apply()
        reviewVersion += 1
    }
    fun dueMissionIds(): List<String> {
        reviewVersion
        val today = LocalDate.now().toEpochDay()
        return LearningContent.missions.filter {
            it.id in completedIds && preferences.getLong("review_due_" + it.id, Long.MAX_VALUE) <= today
        }.map { it.id }
    }
    fun nextReviewDay(id: String): Long {
        reviewVersion
        return preferences.getLong("review_due_" + id, -1L)
    }
    fun addPairResult(correct: Boolean) {
        pairAttempts += 1
        if (correct) pairCorrect += 1
        preferences.edit().putInt("pair_attempts", pairAttempts)
            .putInt("pair_correct", pairCorrect).apply()
    }
    fun deleteAllAudio() {
        context.filesDir.listFiles()?.filter { it.isFile && it.name.startsWith("voice_") && it.name.endsWith(".m4a") }
            ?.forEach { runCatching { it.delete() } }
        val editor = preferences.edit()
        preferences.all.keys.filter {
            it.startsWith("mission_") && (it.endsWith("_first") || it.endsWith("_second") ||
                it.endsWith("_first_duration") || it.endsWith("_second_duration") ||
                it.endsWith("_first_peaks") || it.endsWith("_second_peaks"))
        }.forEach { editor.remove(it) }
        editor.apply()
        firstRecording = ""
        secondRecording = ""
        firstDuration = 0
        secondDuration = 0
        firstPeaks = emptyList()
        secondPeaks = emptyList()
    }
}
