# Termux ile APK derleme

Bu repo Android/Kotlin projesidir. Android SDK, JDK 17 ve Gradle gerektirir. Android Gradle Plugin'in yerleşik aapt2'si ARM64/Termux üzerinde çalışmayabilir. Çalıştığını varsaymıyoruz.

## Tercih edilen güvenilir yol: GitHub Actions
GitHub'da Actions -> Android Debug APK -> Run workflow. Başarılı olursa artifacts içinden `yanki-debug-apk` indir. GitHub-hosted runner SDK ve x86_64 build araçlarını kullanır. Dakika/kota hesap türüne bağlıdır.

## Termux hazırlık
```sh
pkg update && pkg upgrade
pkg install openjdk-17 gradle git wget unzip
git clone https://github.com/mehmetdem2005/Dev-PLAN.git ~/yanki
cd ~/yanki
java -version
gradle --version
```

Android SDK'yı ayrıca kur; `ANDROID_HOME` SDK kökünü göstermeli, `platforms/android-35` ve `build-tools/35.0.0` mevcut olmalı. Ardından:
```sh
gradle :app:assembleDebug --stacktrace
```

`AAPT2` çalıştırma hatası alırsan x86_64 aapt2'yi ARM'de zorlamaya çalışma. Termux için uygun yerel aapt2 binary'si ve `android.aapt2FromMavenOverride=/absolute/path/to/aapt2` gerekir; sürüm uyumu doğrulanmalıdır. Aynı nedenle bazı Gradle sürümleri ARM'de sorun çıkarabilir.

Bu dosya, otomatik kurulmuş/başarıyla test edilmiş bir Termux zinciri iddia etmez.
