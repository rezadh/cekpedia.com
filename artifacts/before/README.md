# Cekpedia — Before

Dokumentasi dibuat 22 Oktober 2026 pada emulator Pixel 5 API 30, PNG 1080 × 2340.
Buka `index.html` untuk galeri; klik gambar untuk ukuran penuh.

## Keaslian dan batas dokumentasi

- `01-login-apk-asli.png`: screenshot runtime APK `cekpedia.apk` dari root proyek.
- `02`–`22`: render resource asli `app/release/app-release.apk` melalui penampil terpisah `me.cekpedia.before`. Bukan screenshot sesi pengguna aplikasi asli, bukan demo lokal rekonstruksi, dan bukan desain baru.
- Resource `res/*` dan `resources.arsc` disalin tanpa perubahan dan diverifikasi byte per byte. SHA-256 sumber tersimpan dalam `provenance.json`.
- Beranda memakai layout utama, fragment home, dan adapter kategori asli. Banner Firebase tidak dimuat.
- Isi daftar dan teks detail adalah fixture untuk dokumentasi; foto adalah aset bawaan APK. Fixture tidak berasal dari database pengguna. Header drawer memakai isi bawaan APK.
- Fragmen peta diganti area abu-abu berlabel offline agar preview tidak bergantung layanan eksternal.
- Form tidak mengirim data, login tidak dijalankan pada penampil, dan penampil tidak mempunyai permission internet.
- Menu Profil, Share, Send pada kode asli hanya menampilkan pesan pengembangan; Near Me tidak memiliki aksi. Tidak ada halaman penuh untuk keempat menu tersebut. Layout fragment profil placeholder tetap disertakan dan diberi label.
- Versi login `cekpedia.apk` dan APK rilis berbeda; keduanya disertakan dengan keterangan sumber.
- Loading/error Firebase, akun asli, hasil pencarian live, keyboard, dan isi peta live tidak termasuk arsip ini.

## Reproduksi

`JAVA_HOME=<Java 11> python3 tools/before-preview/build.py`

Pasang `tools/before-preview/build/cekpedia-before.apk` ke emulator, lalu jalankan
`python3 tools/before-preview/capture.py` dan `python3 tools/before-preview/package.py`.
Build memakai Android platform 30 dan build-tools 30.0.3. Tidak memerlukan root emulator.

Nama layar dan keterangan per gambar ada di `screens.json`.
