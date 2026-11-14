# YANKI — Android konuşma ve dil öğrenme uygulaması

Referans: kullanıcının seçtiği açık zemin, neon-lime ve kobalt-mavi tasarım konsepti.

## Gerçekten çalışan v0.1
- 13 bölümden oluşan ekran akışı: karşılama, hedef, seviye testi, ana sayfa, senaryo, konuşma, IPA, ses özeti, günlük görev, topluluk, ilerleme, paylaşım, profil.
- Android mikrofonu ile yerel M4A ses kaydı, oynatma, iki deneme karşılaştırması.
- Türkçe arayüz, çevrimdışı çalışan denetlenmiş İngilizce görevler ve minimal çift dinleme etkinliği.
- Yerel SharedPreferences kayıtları (gün, görev, öğrenme dili, cevaplar).
- Android paylaşım menüsüyle metin tabanlı görev daveti.
- Abonelik, reklam, ücretli API, sahte aksan / CEFR yüzdesi yok.

## Henüz yapılmayanlar
Offline ASR model entegrasyonu, insanla doğrulanmış fonem puanı, gerçek çok kullanıcılı senkronizasyon, MP4 video oluşturma, üretim kalitesinde FSRS scheduler ve içerik ses kayıtları. Arayüzde bunlar tamamlanmış özellik gibi gösterilmez.

## Geliştirme
JDK 17, Android SDK 35 ve Gradle 8.9 gereklidir. Masaüstünde:
```sh
gradle :app:assembleDebug
```
APK: `app/build/outputs/apk/debug/app-debug.apk`

Termux için `docs/TERMUX.md` dosyasına bakın. Android Gradle Plugin'in aapt2 dağıtımı Termux/ARM cihazında doğrudan çalışmayabilir; çözüm cihazdan cihaza değişir. GitHub Actions alternatifi vardır.

## Gizlilik
Kayıtlar yalnızca uygulamanın özel dosya alanına yazılır. İlk sürümde sunucuya ses yüklenmez. Veriler uygulama kaldırılırsa silinir.

## Lisans
Uygulama kodu MIT. Harici ses modelleri ve ağırlıkları ayrı lisans koşullarına tabidir.
