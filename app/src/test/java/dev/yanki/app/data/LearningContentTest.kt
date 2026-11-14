package dev.yanki.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LearningContentTest {
    @Test fun missionsAreUniqueAndHaveMeaningfulPrompts() {
        val missions = LearningContent.missions
        assertTrue(missions.size >= 10)
        assertEquals(missions.size, missions.map { it.id }.distinct().size)
        assertTrue(missions.all { it.prompt.isNotBlank() && it.translation.isNotBlank() && it.hint.isNotBlank() })
    }
    @Test fun ipaExamplesContainDistinctSymbols() {
        assertEquals(LearningContent.sounds.size, LearningContent.sounds.map { it.symbol }.distinct().size)
        assertTrue(LearningContent.sounds.all { it.example.isNotBlank() && it.transcription.contains("/") })
    }
    @Test fun pairLessonsHaveTwoDifferentWords() {
        assertTrue(LearningContent.pairs.all { it.first != it.second && it.firstIpa != it.secondIpa })
    }
}
