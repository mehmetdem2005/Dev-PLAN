# YANKI — Termux üzerinde Android APK

**Durum:** Kaynak kod GitHub üzerinde. Termux/ARM64 için henüz doğrulanmış APK derlemesi yok. Android Gradle Plugin varsayılan AAPT2 derleyicisini x86_64 için indirebilir; bu dosya ARM64 üzerinde çalışmayabilir.

## İlk adım: yerel araçları kontrol et

```sh
pkg update
pkg install git openjdk-17 gradle
java -version
gradle --version
git clone https://github.com/mehmetdem2005/Dev-PLAN.git ~/yanki
cd ~/yanki
```

Gradle 8.9 ve Android SDK platform 35 ile build-tools 35.0.0 gerekir. Termux paketindeki Gradle sürümü farklı olabilir. `gradle --version` çıktısını kontrol et. `ANDROID_HOME` SDK konumunu göstermelidir.

```sh
echo "$ANDROID_HOME"
ls "$ANDROID_HOME/platforms/android-35"
ls "$ANDROID_HOME/build-tools/35.0.0"
gradle :app:assembleDebug --no-daemon --stacktrace
```

APK başarılı derlenirse:
`app/build/outputs/apk/debug/app-debug.apk`

## AAPT2 hatası durumunda

Hata `Exec format error`, `aapt2 not found` veya ARM64 uyumsuzluğu olabilir. Rastgele aapt2 veya SDK dosyalarını indirmek yerine sistemine uygun, sürümü eşleşen yerel ARM64 AAPT2 gerekir. Gradle'a `android.aapt2FromMavenOverride=/mutlak/yol/aapt2` verilebilir; araç sürümü uyumlu olmalı. Bu çözüm bu cihazda **henüz test edilmiş değildir**.

## Güvenilir alternatif: GitHub Actions

Depoda `.github/workflows/android.yml` mevcut. GitHub > Dev-PLAN > Actions > Android Debug APK > Run workflow ile Ubuntu x86_64 runner üzerinde derle. Başarılı olursa `yanki-debug-apk` artifact'ını indir. Runner sınırları ve Actions kotaları GitHub hesabına bağlıdır.

Önce GitHub derlemesinin hatasız olması, ardından Termux ARM64 portu önerilir.
