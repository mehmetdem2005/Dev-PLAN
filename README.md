# YANKI — Android konuşma deneyimi

**Tasarım yönü:** beyaz/kırık beyaz zemin, lime vurgular, kobalt mavi eylemler, editoryal tipografi ve dağ/ses dalgası görselleri. Referans, kullanıcının seçtiği **YANKI (konsept 2/5)** tasarımıdır. Bu depo başka bir marka veya uygulamaya dönüştürülmeyecektir.

## Şu anda kaynak kodda bulunanlar

- Kotlin / Jetpack Compose tabanlı Android uygulaması.
- Açılış, hedef seçimi, ısınma testi, ana sayfa, görevler, senaryo, konuşma, IPA, kayıt karşılaştırması, minimal çift dinleme, topluluk, ilerleme, paylaşım, profil olmak üzere **14 ekrana** bağlı navigasyon.
- İngilizce için 10 denetlenmiş gündelik senaryo, 6 farklı artikülasyon şemalı IPA ses çalışması ve 5 minimal çift.
- Görev tamamlandıktan sonra 1/3/7 gün aralıklı, cihazda işleyen temel tekrar planı (tam FSRS değil).
- Android MediaRecorder ile gerçek yerel M4A kayıt, MediaPlayer ile oynatma, kayıt süresi, önce/sonra karşılaştırması.
- Cihazın mevcut TextToSpeech motoruyla örnek sözcük ve cümleleri dinletme (İngilizce ses paketinin kurulu olması gerekebilir).
- Uygulama özel depolamasında görev bazlı kayıtlar, gerçek mikrofon tepe genlik grafikleri ve SharedPreferences içinde öğrenme ilerlemesi.
- Android Sharesheet üzerinden metin tabanlı görev daveti ve **gerçek 1080×1920 PNG sonuç kartı** paylaşımı.
- İnternet izni, hesap, ücretli API, zorunlu reklam, abonelik, sahte fonem yüzdesi **yok**.

## Henüz mevcut olmayanlar

Offline ASR (sherpa-onnx/whisper.cpp/Vosk modeli), PCM örnek düzeyinde ses dalgası ve spektrogram, kalibre edilmiş fonem puanlama, FSRS aralıklı tekrar zamanlayıcısı, MP4 kısa video oluşturma, web tabanlı görev bağlantıları, çok kullanıcılı senkronizasyon ve sosyal sıralamalar. Arayüz bunları tamamlanmış gibi göstermiyor. Kayıt esnasında tepe genlikleri gerçekten örneklenir; kimi arayüz motifleri dekoratiftir.

## Derleme

Gereksinimler: JDK 17, Gradle 8.9, Android SDK platform 35, build-tools 35.0.0.

```sh
gradle :app:assembleDebug
```

Çıktı: `app/build/outputs/apk/debug/app-debug.apk`.

GitHub Actions derlemesi: Actions > Android Debug APK > Run workflow. ARM64 üzerinde Termux konusunda **docs/TERMUX.md**'yi oku; AAPT2 uyum problemi nedeniyle çalışacağını varsaymıyoruz.

## Mimari ve yol haritası

Detaylar: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

Tüm çekirdek kod ücretsiz API'ler ve açık kaynak kütüphanelerle çalışacak şekilde tasarlandı. Bu sürümde bir sunucu veya ücretli model çağrısı yoktur. TTS, kullanıcının cihazında kurulu olan Android konuşma sentezi motoruna bağlıdır.

## Gizlilik

Kayıtlar yalnızca cihazdaki uygulama-özel alanda tutulur; paylaşım işlemi kullanıcı tarafından manuel başlatılır. Buluta ses gönderilmez. Uygulama kaldırıldığında yerel kayıtlar silinir. Kayıtların tek tek silinmesi için arayüz gelecekte genişletilecektir.

Uygulamanın kod lisansı: MIT. Harici modeller, TTS sesleri ve veri setleri kendi lisanslarına tabidir.
