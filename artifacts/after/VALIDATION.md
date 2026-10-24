# Cekpedia after — pemeriksaan

- 24 layout/state aplikasi Android dirender di Pixel 5 API 30 dan diperiksa agar tidak menampilkan kegagalan inflate atau aplikasi lain.
- Seluruh XML resource Android valid dan berhasil dibangun menggunakan aapt2 melalui Apktool, dengan resource library dari APK rilis lama.
- Seluruh source Java aplikasi berhasil diperiksa kompilasinya menggunakan Android API 30 dan classpath dari dependensi APK lama. Ini pemeriksaan kompilasi/typecheck, bukan build produksi Gradle.
- Preview memakai layout source baru yang sama, dengan adapter data contoh. Peta menggunakan placeholder offline. Tidak ada operasi Firebase atau kirim data nyata pada preview.
- Navigasi dasar, filter pencarian huruf besar, hasil kosong, serta perpindahan daftar ke detail diperiksa di emulator.
- APK preview tidak menggantikan APK produksi: application ID `me.cekpedia.after`, memakai kunci debug preview, dan tidak mendaftarkan provider/backend aplikasi lama.
- Build produksi Gradle lama serta login/Firebase/Maps/upload end to end belum diverifikasi. Tidak ada deployment atau publikasi.
- Resource staging menghapus varian layout lama yang dihasilkan otomatis oleh build terdahulu agar preview memakai layout source baru pada API 30.

Arsip before tetap tersedia di `artifacts/before`. Semua foto di preview berasal dari aset proyek; tidak ada rating atau jarak palsu yang ditampilkan.
