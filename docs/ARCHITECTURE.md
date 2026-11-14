# YANKI mimari notları

## Ürün sınırı
YANKI ilk aşamada çevrimdışı konuşma alıştırması uygulamasıdır. Uçtan uca gerçek ve dürüst deneyim önceliklidir. Kullanıcıya geçerli bir fonem skoru, CEFR seviyesi, aksan yüzdesi veya sosyal sıralama **uydurulmaz**.

## Modüller
- `MainActivity.kt`: Compose başlangıcı.
- `ui/Theme.kt`: açık arayüzün renkleri ve yazı stili.
- `ui/Elements.kt`: yeniden kullanılabilir bileşenler, illüstrasyon, dekoratif dalga.
- `ui/YankiApp.kt`: ekran yönlendirme, mikrofon izin akışı, paylaşma eylemleri.
- `ui/CorePages.kt`: onboarding, senaryo, görev, kayıt ekranları.
- `ui/PracticePages.kt`: IPA/anatomi şeması, iki kaydın dinlenmesi, minimal çift testleri.
- `ui/SocialPages.kt`: sosyal davet, gerçek istatistik, metin paylaşımı, profil.
- `data/Content.kt`: denetlenmiş İngilizce görev ve fonetik örnekleri.
- `data/LocalStore.kt`: düşük karmaşıklıklı yerel kalıcılık.
- `audio/VoiceEngine.kt`: Android ses kayıt/oynatma ve cihaz TTS'i.

## Yapılan ve yapılmayan ölçümler

**Yapılan:** gerçek ses kaydı, süre, tamamlanan görev sayısı, dinleme testi cevap sayısı, doğru cevap sayısı.

**Henüz yapılmayan:** sesin yazıya dökülmesi (ASR), sesle eşleşen dalga grafiği, formant/F0 ölçümü, fonem hatası teşhisi, bağımsız CEFR/aksan puanı. Uygulamadaki dekoratif ses dalgası ölçüm aracı değildir.

## Sadece ücretsiz / açık kaynak kullanım ilkesi

- Android API, Kotlin, Jetpack Compose, AndroidX, Gradle.
- Local MediaRecorder/MediaPlayer ve TextToSpeech (cihaz motoruna bağlı).
- Veri için SharedPreferences, ileride Room.
- İleride tamamen ücretsiz ve yerel modeller: sherpa-onnx, Vosk, whisper.cpp. Önce lisans, model boyutu ve Android ARM64 testleri.
- MP4 üretimi için AndroidX Media3 Transformer; henüz entegre edilmedi.
- Aralıklı tekrar için lisans kontrolü sonrası FSRS uyarlaması.
- Sunucu zorunlu olmayacak; sosyal işlevler gelirse ücretsiz/self-host seçeneklerin kotaları ayrıca değerlendirilecek.

## Öncelikli teknik backlog

1. CI build onarımı ve Android 11 cihazda kayıt testi.
2. Ücretsiz açık kaynak TTS ses kayıtları veya lisansı uygun ses dosyaları için çevrimdışı yedek.
3. Gerçek dalga biçimi: M4A decode sonrası örnek/spektrogram hesaplama.
4. Fonksiyonel aralıklı tekrar: yerel görev geçmişi ve dueDate.
5. Cihaz üzerinde offline ASR modeli, net belirsizlik/güven gösterimi.
6. Ses paylaşımı için kullanıcı kontrollü görsel kart veya MP4; Media3 ile.
7. Gerçek sosyal özellikler ancak kullanıcı talebi ve güvenlik modeli netleşirse.
8. Yerel veri silme ve dışa aktarma; izinlere yönelik otomatik testler.

## Gizlilik ve güvenlik
- Android `RECORD_AUDIO` yalnızca kullanıcı kaydı başlatınca istenir.
- Kayıtlar uygulama özel klasöründe.
- Bu sürümde `INTERNET` izni yok.
- Otomatik ses yüklemesi veya sosyal ağa paylaşım yok.
- Cihaz TTS motorunun veri işlemesi, kurulu motorun sağlayıcısının koşullarına tabi olabilir. Uygulama ayrıca buluta ses göndermez.
