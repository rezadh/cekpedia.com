# Cekpedia lokal

Demo Android offline terpisah (`me.cekpedia.local`), bukan migrasi backend aplikasi asli.
Memakai ikon kategori dari proyek asli dan data contoh yang jelas diberi label.
Beranda, filter kategori, pencarian, detail, serta favorit lokal dapat dicoba tanpa login, jaringan, atau Firebase.
Peta, telepon, dan input lokasi belum diimplementasikan. Tampilan ini rekonstruksi sederhana, bukan render layout aplikasi asli.

Build dengan `python3 local-demo/build.py`. Membutuhkan Android SDK platform 30,
build-tools 30.0.3, dan Java 11. Set `ANDROID_HOME` dan `JAVA_HOME` bila lokasinya berbeda.

Pasang `local-demo/build/cekpedia-local.apk` ke emulator, lalu buka aplikasi **Cekpedia Lokal**.
Favorit menggunakan SharedPreferences dan tetap tersimpan setelah aplikasi dibuka ulang.
APK menggunakan kunci demo lokal, bukan kunci rilis aplikasi asli.
