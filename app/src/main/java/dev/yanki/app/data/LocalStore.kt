package dev.yanki.app.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LocalStore(context: Context) {
    private val preferences = context.getSharedPreferences("yanki_local_v1", Context.MODE_PRIVATE)
    var language by mutableStateOf(preferences.getString("language", "İngilizce") ?: "İngilizce")
        private set
    var goal by mutableStateOf(preferences.getString("goal", "Akıcı konuşmak") ?: "Akıcı konuşmak")
        private set
    var onboarded by mutableStateOf(preferences.getBoolean("onboarded", false))
        private set
    var selectedMission by mutableStateOf(preferences.getString("mission", "cafe") ?: "cafe")
        private set
    var firstRecording by mutableStateOf(preferences.getString("first_audio", "") ?: "")
        private set
    var secondRecording by mutableStateOf(preferences.getString("second_audio", "") ?: "")
        private set
    var firstDuration by mutableIntStateOf(preferences.getInt("first_duration", 0))
        private set
    var secondDuration by mutableIntStateOf(preferences.getInt("second_duration", 0))
        private set
    var completedIds by mutableStateOf(preferences.getStringSet("completed", emptySet())?.toSet() ?: emptySet())
        private set
    var practiceCount by mutableIntStateOf(preferences.getInt("practices", 0))
        private set
    var pairCorrect by mutableIntStateOf(preferences.getInt("pair_correct", 0))
        private set
    var pairAttempts by mutableIntStateOf(preferences.getInt("pair_attempts", 0))
        private set

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
        if (selectedMission != id) {
            selectedMission = id
            firstRecording = ""
            secondRecording = ""
            firstDuration = 0
            secondDuration = 0
            preferences.edit().putString("mission", id).remove("first_audio").remove("second_audio")
                .remove("first_duration").remove("second_duration").apply()
        }
    }
    fun saveRecording(path: String, duration: Int) {
        if (firstRecording.isBlank()) {
            firstRecording = path
            firstDuration = duration
            preferences.edit().putString("first_audio", path).putInt("first_duration", duration).apply()
        } else {
            secondRecording = path
            secondDuration = duration
            preferences.edit().putString("second_audio", path).putInt("second_duration", duration).apply()
        }
        practiceCount += 1
        preferences.edit().putInt("practices", practiceCount).apply()
    }
    fun markCompleted() {
        completedIds = completedIds + selectedMission
        preferences.edit().putStringSet("completed", completedIds).apply()
    }
    fun addPairResult(correct: Boolean) {
        pairAttempts += 1
        if (correct) pairCorrect += 1
        preferences.edit().putInt("pair_attempts", pairAttempts)
            .putInt("pair_correct", pairCorrect).apply()
    }
}
