# Cekpedia after preview

Penampil offline untuk **layout source Android baru**, terpisah dari aplikasi Firebase.
Resource library dipulihkan dari APK rilis lama agar visual dapat dibangun tanpa
menunggu pemulihan Gradle/JCenter. Resource aplikasi diambil dari `app/src/main/res`.
APK preview tidak menjalankan activity/provider Firebase dari APK lama.

1. Unduh Apktool resmi ke `/tmp/cekpedia-apktool.jar` atau set `APKTOOL_JAR`.
2. Dengan Java 11: `python3 tools/redesign/build_resources.py`.
3. `python3 tools/after-preview/build.py`.
4. Pasang `tools/after-preview/build/cekpedia-after.apk` ke emulator.
5. `python3 tools/after-preview/capture.py` untuk screenshot seluruh halaman.
6. Jalankan `contact-sheets.swift`, lalu `python3 tools/after-preview/package.py`.

Membutuhkan Android SDK platform 30, build-tools 30.0.3, Java 11, dan emulator lokal.
Application ID preview: `me.cekpedia.after`. Data tempat merupakan fixture;
layanan eksternal hanya menampilkan penjelasan preview offline. Keyboard
dinonaktifkan hanya pada intent `--ez capture true` untuk pengambilan screenshot.

`tools/redesign/write_layouts.py` meregenerasi sistem visual/layout aplikasi.
Sesudah mengubah layout langsung, samakan perubahan pada generator bila diperlukan.
Build produksi aplikasi asli tetap menggunakan konfigurasi Gradle proyek.
