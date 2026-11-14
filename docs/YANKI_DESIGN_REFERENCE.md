# YANKI Visual Specification — Selected Reference

This document locks the selected YANKI concept. No alternative brand or visual style is authorized.

## Identity
- Name: YANKI, uppercase black heavy wordmark, small lime + cobalt soundwave mark.
- Design: warm-white / off-white backgrounds, black text, restrained neon lime and cobalt highlights.
- Token colors: background #F8F8F5, cards #FFFFFF, neutral #EDF0EB, ink #161A1E, muted #737980, lime #C8FC26, cobalt #283CEB.
- Rounded white cards, thin neutral separators, subtle borders/shadows; not a dark or neon gaming aesthetic.
- Material 3 Android with readable Turkish text, touch targets at least 48dp, edge-safe layouts.
- The top of the concept shows grayscale architectural / mountain photography and elegant lime accent. Imagery may be added only with proper rights; placeholder is intentional for first implementation.

## 13 reference screens
1. Onboarding / Karşılama: YANKI wordmark, mountain/city hero, one big promise, lime start button.
2. Dil ve Hedef: two-by-two language choices and concise goal choices, cobalt continue.
3. Hızlı Seviye Tespiti: quiz progress, contextual sentence, clear multiple-choice options.
4. Ana Sayfa: daily progress ring, three action cards, bottom 5-tab navigation.
5. Senaryo Görevi: high-quality cafe photograph, task objective, four-step flow, cobalt CTA.
6. Rehberli Konuşma: contextual image, speech bubbles, waveform, large blue microphone control.
7. Telaffuz Eğitmeni: IPA /θ/, actual place-of-articulation explanation and reference words.
8. Konuşma Analizi: before-after playback, transparent metrics and evidence labeling.
9. Günlük Meydan Okuma: one prompt per card, microphone, optional timer.
10. Topluluk: friends/challenges and cooperative dialogue — no fake leaderboard.
11. İlerleme: retention and speech timeline, before/after audio comparison.
12. Paylaş: 9:16 video preview, captions, clean controls; actual MP4 generation is later milestone.
13. Profil: language/goal, privacy, progress and settings.

## Development rules
- Implement real interactions first. Never invent a pronunciation percent, accent metric, or CEFR level.
- Initial package supports English scenarios only. Unsupported languages must be clearly indicated until content exists.
- Audio is stored locally; no secret key or paid service.
- Offline ASR models, real social accounts, media export and FSRS need separate integrations/testing.
- All dependencies must be free and preferably open source; provider free tiers are not necessarily open source or uncapped.
- Termux APK compilation requires compatible ARM build tools; GitHub Actions is the build fallback.
